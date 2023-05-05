package sweetmagic.init.block.blocks;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileMagiaFluxCore;

public class BlockMagiaFluxCore extends BaseModelBlock {

	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.55D, 0.2D, 0.8D, 1.15D, 0.8D);

	public BlockMagiaFluxCore (String name) {
        super(Material.GLASS, name);
        setSoundType(SoundType.GLASS);
        setLightLevel(1F);
		setHardness(0.5F);
		setResistance(1024F);
		BlockInit.magicList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileMagiaFluxCore();
	}

	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return false;
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 5;
	}

	@Deprecated
    @Override
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {

		IBlockState under = world.getBlockState(pos.down());
		Block block = under.getBlock();

		if (block == Blocks.AIR) {
			return super.getOffset(state, world, pos);
		}

		double topY = block.getBoundingBox(under, world, pos.down()).maxY;
		double y = topY == 1D ? -0.5D : -1.5D + block.getBoundingBox(under, world, pos.down()).maxY * 1D;
		return new Vec3d(0D, y, 0D);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.enchantpower.name") + " : " + 5.0F ));
	}
}
