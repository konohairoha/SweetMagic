package sweetmagic.init.block.crop.icrop;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.item.sm.sweetmagic.SMHoe;
import sweetmagic.init.item.sm.sweetmagic.SMSickle;

public interface ISMCrop {

	int getMaxBlockState();

	// 右クリックアイテムの取得
	default EntityItem getDropItem (World world, EntityPlayer player, ItemStack hand, Item item, int amount) {

		// 追加ドロップ数
		int addDrop = 0;

		// 鍬を持っていたら
		if (hand.getItem() instanceof SMHoe) {
			addDrop += world.rand.nextInt(2) + 1;
			hand.damageItem(20, player);
		}

		return new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(item, amount + addDrop));
	}

	// 右クリック時の処理
	default void onRicghtClick (World world, EntityPlayer player, IBlockState state, BlockPos pos, ItemStack stack) { }

	default void getPickPlant (World world, EntityPlayer player, BlockPos pos, ItemStack stack) {

		int area = 2;

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, area, area))) {

			IBlockState state = world.getBlockState(p);
			Block block = state.getBlock();
			if (!(block instanceof ISMCrop)) { continue; }

			// 右クリック呼び出し
			this.onRicghtClick(world, player, state, p, stack);
//			ISMCrop crop = (ISMCrop) block;
		}
	}

	// 鎌なら作物回収呼び出し
	default boolean isSickle (World world, EntityPlayer player, BlockPos pos, ItemStack stack) {
		if (stack.getItem() instanceof SMSickle) {
			this.getPickPlant(world, player, pos, stack);
		}
		return false;
	}
}
