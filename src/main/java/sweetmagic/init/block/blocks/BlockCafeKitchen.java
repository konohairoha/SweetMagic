package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;

public class BlockCafeKitchen extends BlockWoodChest {

	private static final PropertyBool TOP = PropertyBool.create("top");

	public BlockCafeKitchen(String name, int data) {
		super(name, data);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false));
	}

	// getOppositeでプレイヤーの向きを取得
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float x, float y, float z, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return state.withProperty(TOP, block == this || block == BlockInit.cafe_kitchen_sink);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TOP });
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, world, tooltip, advanced);
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.cafekitchen.name")));
	}
}
