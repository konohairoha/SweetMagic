package sweetmagic.init.item.sm.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.init.BlockInit;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.worldgen.structures.WorldGenStructure;

public class MagicMeal extends SMItem implements IElementItem {

	public SMElement ele;
	public final WorldGenStructure SM_HOUSE = new WorldGenStructure("witch_house");
	public static List<ItemStack> loot = new ArrayList<>();

	public MagicMeal(String name) {
		super(name);
		this.setElement(SMElement.BLAST);
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		Random rand = world.rand;

		if (!world.isRemote) {

			Block block = world.getBlockState(pos).getBlock();

			// 地面が土だった時の処理
			if (block instanceof BlockGrass || block instanceof BlockDirt) {

				//設置するブロックの内容（追加も可能）
				Block bloS[] = new Block[] {
						BlockInit.moonblossom_plant, BlockInit.sugarbell_plant, BlockInit.sannyflower_plant
				};

				this.dirtAction(world, rand, block, pos, player, stack, bloS, 0);
			}
		}

		world.playSound(player, pos, SoundEvents.ENTITY_FIREWORK_BLAST_FAR, SoundCategory.VOICE, 0.8F,
				1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);

		return EnumActionResult.SUCCESS;
	}

	// 地面が土だった時の処理
	public void dirtAction (World world, Random rand, Block block, BlockPos pos, EntityPlayer player, ItemStack stack, Block[] bloS, int count) {

		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }

		//渡された座標から再開
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {

			if (count >= 3) { return; }

			Block checkAir = world.getBlockState(p).getBlock();

			int setRand = rand.nextInt(2); 				//ブロック設置の乱数
			int blockRand = rand.nextInt(bloS.length); 	//設置するブロックの乱数

			if (setRand != 0 || checkAir != Blocks.AIR) { continue; }

			Block checkDirt = world.getBlockState(p.down()).getBlock();
			if (checkDirt instanceof BlockGrass || checkDirt instanceof BlockDirt) {
				count++;
				world.setBlockState(p, bloS[blockRand].getDefaultState(), 2);
			}
		}
	}

	@Override
	public SMElement getElement() {
		return this.ele;
	}

	@Override
	public void setElement(SMElement ele) {
		this.ele = ele;
	}
}
