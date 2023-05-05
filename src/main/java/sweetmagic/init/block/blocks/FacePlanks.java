package sweetmagic.init.block.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;

public class FacePlanks extends Block {

	private static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);

	public FacePlanks(String name) {
		super(Material.WOOD);
		setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(SoundType.WOOD);
        setHardness(1F);
        setResistance(1024F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		BlockInit.blockList.add(this);
    }

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float x, float y, float z, int meta, EntityLivingBase placer) {
		return super.getStateForPlacement(world, pos, face, x, y, z, meta, placer).withProperty(FACING, face);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
