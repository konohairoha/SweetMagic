package sweetmagic.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

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
		else if (item == ItemInit.alt_pick && !player.isSneaking() /*&& !dropList.isEmpty()*/) {

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
			if ( ( state.getBlockHardness(world, pos) >= 1D ||
					block == Blocks.QUARTZ_BLOCK ) && block.isFullCube(state) && block != Blocks.MOB_SPAWNER ) {

				event.setCanceled(true);
				return;
			}
		}

		// アイテムを何を持ってないなら終了
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) { return; }

		Item item = stack.getItem();

		// オルタナティブピッケル
		if (item == ItemInit.alt_pick && !player.isSneaking()) {

			// 必要情報の取得
			BlockPos pos = event.getPos();
			IBlockState state = event.getState();
			Block block = state.getBlock();

			// シルクタッチが可能なら経験値をドロップしない
			if (block.canSilkHarvest(world, pos, state, player)) {
				event.setExpToDrop(0);
			}
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
}
