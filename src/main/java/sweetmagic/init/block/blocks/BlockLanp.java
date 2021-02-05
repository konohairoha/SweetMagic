package sweetmagic.init.block.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLanp extends BlockEncPower {

	protected static final AxisAlignedBB CANDLE_AABB = new AxisAlignedBB(0.3D, 0.8D, 0.3D, 0.7D, 0D, 0.7D);

	public BlockLanp(String name, float hardness, float resistance, int harvestLevel, float light, float encPower, SoundType sType, List<Block> list) {
		super(name, hardness, resistance, harvestLevel, light, encPower, sType);
		list.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CANDLE_AABB;
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, World world, boolean dropBlock) {

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (dropBlock) {
			block.dropBlockAsItem(world, pos, state, 0);
		}

		spawnAsEntity(world, pos, new ItemStack(Item.getItemFromBlock(this)));

		return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}
}
