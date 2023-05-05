package sweetmagic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Lists;

import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.block.magic.MagicBarrier;
import sweetmagic.util.WorldHelper;

public class SMHarvestEvent {

	// ブロック採掘時のアイテムドロップイベント
	@SubscribeEvent
	public void onMining(HarvestDropsEvent event) {

		if (event.getHarvester() == null || event.getWorld().isRemote) { return; }

		// メインハンドに何も持ってないなら終了
		EntityPlayer player = event.getHarvester();
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) { return; }

		// 耐久値とItem、ドロップリストの取得
		int maxDama = stack.getMaxDamage();
		Item item = stack.getItem();
		List<ItemStack> dropList = event.getDrops();

		// シャベルなら
		if (item instanceof ItemSpade && maxDama >= 200) {

			// 幸運レベルを取得
			int level = event.getFortuneLevel() + 1;
			float f = event.getWorld().rand.nextFloat();

			// ブロックの取得
			IBlockState state = event.getState();
			Block block = state.getBlock();

			// 粘土なら
			if (block == Blocks.CLAY) {

				// f = ドロップ率（数字が小さいほど確率が低い）
				if (f < 0.02F * level) {
					dropList.add(new ItemStack(ItemInit.sticky_stuff_seed));
				}
			}

			// ポドゾルなら
			else if (block == Blocks.DIRT && block.getMetaFromState(state) == 2) {

				if (f < 0.2F * level) {
					dropList.add(new ItemStack(ItemInit.sweetpotato));
				}
			}
		}

		// オルタナティブピッケルなら
		else if (item == ItemInit.alt_pick && !player.isSneaking()) {

			// 必要情報の取得
			World world = player.world;
			IBlockState state = event.getState();
			BlockPos pos = event.getPos();

			// シルクタッチと同様の処理でドロップアイテムの取得
			List<ItemStack> stackList = this.getBlockDrops(world, player, state, state.getBlock(), pos);
			if (stackList.isEmpty()) { return; }

			// ドロップアイテム分回す
			for (ItemStack s : stackList ) {
				world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, s));
			}

			// ドロップアイテムのクリア
			event.getDrops().clear();
		}
	}

	// ブロックを破壊したときのイベント
	@SubscribeEvent
	public void onBlockBreakEvent (BreakEvent event) {

		World world = event.getWorld();
		if (event.getPlayer() == null || world.isRemote) { return; }

		EntityPlayer player = event.getPlayer();

		// 破壊無効が付いているなら
		if (player.isPotionActive(PotionInit.breakblock) && !player.isCreative()) {

			// 必要情報の取得
			BlockPos pos = event.getPos();
			IBlockState state = event.getState();
			Block block = state.getBlock();

			// 固さが1以上かクオーツブロックかつfullcubeかつモブスポナー以外なら
			if ( ( ( state.getBlockHardness(world, pos) >= 1D || block == Blocks.QUARTZ_BLOCK )
					&& block.isFullCube(state) && block != Blocks.MOB_SPAWNER ) || block instanceof BlockGlass) {

				event.setCanceled(true);
				return;
			}

			else if (player.getActivePotionEffect(PotionInit.breakblock).getAmplifier() >= 1) {
				event.setCanceled(true);
				return;
			}
		}

		// 必要情報の取得
		BlockPos pos = event.getPos();
		IBlockState state = event.getState();
		Block block = state.getBlock();

		if (block instanceof MagicBarrier) {
			for (EnumFacing face : EnumFacing.VALUES) {
				BlockPos facePos = pos.offset(face);
				if (world.getBlockState(facePos).getBlock() instanceof MagicBarrier) {
			        MinecraftForge.EVENT_BUS.register(new BrrierBreakEvent(facePos, player, 1));
				}
			}
		}

		// アイテムを何を持ってないなら終了
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) { return; }

		Item item = stack.getItem();

		// オルタナティブピッケル
		if (item == ItemInit.alt_pick && !player.isSneaking()) {

			// シルクタッチが可能なら経験値をドロップしない
			if (block.canSilkHarvest(world, pos, state, player)) {
				event.setExpToDrop(0);
			}
		}

		else if (item == ItemInit.startlight_wand || item == ItemInit.magicianbeginner_book || (player.isSneaking() && item instanceof IWand)) {
			event.setCanceled(true);
		}
	}

	// ブロックからドロップするアイテムリストの取得
	public List<ItemStack> getBlockDrops(World world, EntityPlayer player, IBlockState state, Block block, BlockPos pos) {

		if (block == Blocks.LIT_REDSTONE_ORE || block == Blocks.REDSTONE_ORE) {
			return Arrays.<ItemStack> asList(new ItemStack(Blocks.REDSTONE_ORE));
		}

		else if (block instanceof IShearable && block instanceof BlockLeaves) {
			return Arrays.<ItemStack> asList(new ItemStack(block, 1, block.damageDropped(state)));
		}

		else if (block.canSilkHarvest(world, pos, state, player)) {
			ItemStack stack = this.getSilkTouchDrop(state, block);
			return !stack.isEmpty() ? Arrays.<ItemStack> asList(stack) : new ArrayList<>();
		}

		return block.getDrops(world, pos, state, 0);
	}

	// シルクタッチでドロップするアイテムの取得
	public ItemStack getSilkTouchDrop(IBlockState state, Block block) {
		Item item = Item.getItemFromBlock(block);
		int i = item.getHasSubtypes() ? block.getMetaFromState(state) : 0;
		return item != Items.AIR ? new ItemStack(item, 1, i) : ItemStack.EMPTY;
	}

    public static class BrrierBreakEvent {

        private final World world;
        private final EntityPlayer player;
        private final boolean isCreative;
        private final int blockTick;
        private Queue<BlockPos> blocks = Lists.newLinkedList();
        private Set<BlockPos> posSet = new THashSet<>();

		// ドロップ処理
		private List<ItemStack> drop = new ArrayList<>();

        public BrrierBreakEvent(BlockPos start, EntityPlayer player, int blockTick) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.isCreative = player.isCreative();
            this.blockTick = blockTick;
            this.blocks.add(start);
        }

		@SubscribeEvent
		public void chopChop(TickEvent.WorldTickEvent event) {

			// クライアントなら終了
        	if(event.side.isClient()) {
                this.finish();
                return;
            }

        	// ディメンションが違うなら終了
        	if(event.world.provider.getDimension() != this.world.provider.getDimension()) { return; }

            int left = this.blockTick;
            BlockPos pos;

            // 見つかるまで回す
            while(left > 0) {

            	// 空なら終了
                if(this.blocks.isEmpty()) {

        			//リストに入れたアイテムをドロップさせる
        			WorldHelper.createLootDrop(this.drop, this.world, this.player.posX, this.player.posY, this.player.posZ);
        			this.drop.clear();
                	this.finish();
                    return;
                }

                pos = this.blocks.remove();

                // 原木じゃないなら次へ
                if(!this.posSet.add(pos) || !isLog(this.world, pos)) { continue; }

				// 4方向確認
				for (EnumFacing face : EnumFacing.VALUES) {
                    BlockPos posFace = pos.offset(face);
                    if(!this.posSet.contains(posFace)) { this.blocks.add(posFace); }
                }

				// 範囲確認
				for (int x = 0; x < 1; x++) {
					for (int z = 0; z < 1; z++) {

						BlockPos p2 = pos.add(-1 + x, 1, -1 + z);
						if (!this.posSet.contains(p2)) {
							this.blocks.add(p2);
						}
                    }
                }

				BlockPos p = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
				IBlockState state = this.world.getBlockState(p);

				if (!this.isCreative) {
					this.drop.addAll(WorldHelper.getBlockDrops(this.world, this.player, state, state.getBlock(), p, false, 0));
				}
				this.world.destroyBlock(p, false);

				left--;
            }

			//リストに入れたアイテムをドロップさせる
			if (!this.isCreative) {
				WorldHelper.createLootDrop(this.drop, this.world, this.player.posX, this.player.posY, this.player.posZ);
			}
			this.drop.clear();
        }

		// イベント終了
		public void finish() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }

	    // バリアチェック
	    public boolean isLog(World world, BlockPos pos) {
	    	return BlockInit.magicbarrier_off == world.getBlockState(pos).getBlock();
	    }
    }
}
