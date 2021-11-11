package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAwningTent extends BlockCounterTable {

	private static final PropertyInteger CENTER = PropertyInteger.create("center", 0, 5);
	private static final AxisAlignedBB AWNINGTENT = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.5D, 1D);

	public BlockAwningTent (String name, int data) {
		super(Material.WOOD, name, data);
        setSoundType(SoundType.CLOTH);
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(CENTER, 0));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AWNINGTENT;
	}

	@Override
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new Vec3d(0D, -0.325D, 0D);
	}

	// 両サイドにブロックがあるかどうか
	@Override
	public int checkCenter (IBlockAccess world, BlockPos pos) {

		IBlockState north = world.getBlockState(pos.north());
		IBlockState south = world.getBlockState(pos.south());
		IBlockState west = world.getBlockState(pos.west());
		IBlockState east = world.getBlockState(pos.east());

		if (this.isAwning(north, south, west, east)) {
			return 5;
		}

		else if (this.isCenter(north, south, west, east)) {
			return 0;
		}

		return super.checkCenter(world, pos);
	}

	public boolean isAwning (IBlockState state) {
		return state.getBlock() instanceof BlockAwningTent;
	}

	public boolean isAwning (IBlockState... stateArray) {
		for (IBlockState state : stateArray) {
			if (!this.isAwning(state)) { return false; }
		}
		return true;
	}

	public boolean isCenter (IBlockState... stateArray) {
		int count = 0;
		for (IBlockState state : stateArray) {
			if (this.isAwning(state)) { count++; }
		}
		return count >= 3;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(CENTER, this.checkCenter(world, pos));
	}

	// 繋がるかのチェック
	@Override
	public boolean canConnectBlock(IBlockState state, EnumFacing face) {
		return this.isAwning(state) && face == state.getValue(FACING);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CENTER });
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.awning_tent.name")));
	}
}
