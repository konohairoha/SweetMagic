package sweetmagic.init.item.sm.sweetmagic;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import sweetmagic.init.ItemInit;
import sweetmagic.util.WorldHelper;

public class SMAxe extends ItemAxe {

	public SMAxe(String name, ToolMaterial material, float damage, float speed) {
		super(material,damage,speed);
		setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
	}

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos pos, EntityPlayer player) {

    	World world = player.getEntityWorld();
		stack.damageItem(1, player);
        if (!isLog(world, pos) || player.isSneaking()) { return false; }
        if(world.isRemote) { return true; }

        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack) / 2;
        MinecraftForge.EVENT_BUS.register(new TreeChopTask(pos, player, 1 + level));
		stack.damageItem(7, player);
        return true;
    }

    // 原木チェック
    public static boolean isLog(World world, BlockPos pos) {

    	Block block = world.getBlockState(pos).getBlock();
		String oreName;

		try {
			oreName = checkLog(OreDictionary.getOreIDs(new ItemStack(block)));
		}

		catch (Throwable e) { return false; }

        return oreName.equals("logWood") || oreName.equals("treeLeaves");
    }

    // 鉱石辞書を返す
	public static String checkLog (int[] recipeId) {

		String recipe = "";
		if (recipeId.length == 0) { return recipe; }

		// 鉱石辞書に登録してる分回す
		for (int id : recipeId) {

			String name = OreDictionary.getOreName(id);

			// 原木か葉っぱなら検索終了
			if (name.equals("logWood") || name.equals("treeLeaves")) {
				recipe = name;
				break;
			}
		}
		return recipe;
	}

    public class TreeChopTask {

        public final World world;
        public final EntityPlayer player;
        public final int blockTick;
        public Queue<BlockPos> blocks = Lists.newLinkedList();
        public Set<BlockPos> posSet = new THashSet<>();

        public TreeChopTask(BlockPos start, EntityPlayer player, int blockTick) {
            this.world = player.getEntityWorld();
            this.player = player;
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
            EnumFacing[] allFace = new EnumFacing[] { EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST };

            // 見つかるまで回す
            while(left > 0) {

            	// 空なら終了
                if(this.blocks.isEmpty()) {
                	this.finish();
                    return;
                }

                pos = this.blocks.remove();

                // 原木じゃないなら次へ
                if(!this.posSet.add(pos) || !isLog(this.world, pos)) { continue; }

				// 4方向確認
				for (EnumFacing face : allFace) {
                    BlockPos posFace = pos.offset(face);
                    if(!this.posSet.contains(posFace)) { this.blocks.add(posFace); }
                }

				// 範囲確認
				for (int x = 0; x < 5; x++) {
					for (int z = 0; z < 5; z++) {

						BlockPos p2 = pos.add(-1 + x, 1, -1 + z);
						if (!this.posSet.contains(p2)) {
							this.blocks.add(p2);
						}
                    }
                }

				// ドロップ処理
				List<ItemStack> drop = new ArrayList<>();
				BlockPos p = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
				IBlockState state = world.getBlockState(p);
				drop.addAll(WorldHelper.getBlockDrops(this.world, this.player, state, state.getBlock(), p, false, 0));
				this.world.destroyBlock(p, false);

				//リストに入れたアイテムをドロップさせる
				WorldHelper.createLootDrop(drop, this.world, this.player.posX, this.player.posY, this.player.posZ);
				left--;
            }
        }

		// イベント終了
		public void finish() {
            MinecraftForge.EVENT_BUS.unregister(this);
        }
    }

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_axe.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tip));
	}
}
