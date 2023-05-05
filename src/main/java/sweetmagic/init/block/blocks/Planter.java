package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.util.FaceAABB;

public class Planter extends BaseFaceBlock {

	private static final PropertyBool SIDE = PropertyBool.create("side");
	private static final PropertyBool TOP = PropertyBool.create("top");
    private static final AxisAlignedBB[] BOT_AABB = new FaceAABB(0.1875D, 0D, 0D, 0.8125D, 0.53125D, 1D).getRotatedBounds();
    private static final AxisAlignedBB[] TOP_AABB = new FaceAABB(0.1875D, 0.46875D, 0D, 0.8125D, 1D, 1D).getRotatedBounds();
	private final int data;

	public Planter(String name, int data) {
		super(Material.GROUND, name);
		this.data = data;
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(TOP, false).withProperty(SIDE, false));
		BlockInit.blockList.add(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return !state.getValue(TOP) ? BOT_AABB[state.getValue(FACING).getHorizontalIndex()] : TOP_AABB[state.getValue(FACING).getHorizontalIndex()];
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, TOP, SIDE });
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new Vec3d(0D, state.getValue(TOP) ? 0.5D : 0D, 0D);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFertile(World world, BlockPos pos) {
		return true;
	}

	//Tick更新処理が必要なブロックには必ず入れること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		if (rand.nextInt(10) < 3) { return; }
		if (this.data == 3 && rand.nextBoolean()) { return; }

		IBlockState target = world.getBlockState(pos.up());
		Block block = target.getBlock();

		// 特殊な花なら
		if (block instanceof MagiaFlower && ((MagiaFlower) block).canGrow(world, pos.up(), target, false)) {

			MagiaFlower flower = (MagiaFlower) block;
			if (!flower.isFlower() || rand.nextBoolean()) { return; }

			// 現在の成長段階を取って成長させる
			int meta = flower.getNowStateMeta(target);
			world.setBlockState(pos.up(), flower.withAge(meta + 1), 2);
		}

		// 通常の作物
		else {

			// 成長処理呼び出し
			this.growPlant(world, pos.up(), rand);
			this.growPlant(world, pos.up(2), rand);
		}
	}

	public void growPlant (World world, BlockPos pos, Random rand) {

		//ブロックを取得するための定義
		IBlockState target = world.getBlockState(pos);
		Block crop = target.getBlock();

		if (!(crop instanceof IGrowable) || crop instanceof BlockGrass
				|| crop instanceof BlockDoublePlant|| crop instanceof BlockTallGrass
				|| crop instanceof MagiaFlower) { return; }

		IGrowable grow = (IGrowable) crop;
		if (!grow.canGrow(world, pos, target, false)) { return; }

		grow.grow(world, world.rand, pos.toImmutable(), target);
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face, IPlantable plante) {

		switch (plante.getPlantType(world, pos.offset(face))) {
            case Desert: return true;
            case Nether: return false;
            case Crop:   return true;
            case Cave:   return true;
            case Plains: return true;
            case Water:  return false;
            case Beach:  return true;
        }
		return super.canSustainPlant(state, world, pos, face, plante);
	}

	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		for (EnumFacing face : EnumFacing.values()) {
			if (this.canPlaceBlock(world, pos, face)) { return true; }
		}
		return false;
	}

	protected boolean canPlaceBlock(World world, BlockPos pos, EnumFacing face) {

		BlockPos bpos = pos.offset(face.getOpposite());
		IBlockState state = world.getBlockState(bpos);
		boolean flag = state.getBlockFaceShape(world, bpos, face) == BlockFaceShape.SOLID;
		Block block = state.getBlock();

		if (face == EnumFacing.UP) {
			return state.isTopSolid() || !isExceptionBlockForAttaching(block) && flag;
		}

		else {
			return !isExceptBlockForAttachWithPiston(block) && flag;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return true;
	}

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float x, float y, float z, int meta, EntityLivingBase placer) {

		boolean isSide = true;

    	if (face == EnumFacing.UP) {
    		isSide = false;
    		y = 0F;
    	}

    	else if (face == EnumFacing.DOWN) {
    		isSide = false;
    		y = 1F;
    	}

        return this.getStateFromMeta(meta).withProperty(TOP, y >= 0.5F).withProperty(SIDE, isSide).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

	// IBlockStateからItemStackのmetadataを生成。ドロップ時とテクスチャ・モデル参照時に呼ばれる
	@Override
	public int getMetaFromState(IBlockState state) {
		return super.getMetaFromState(state) + (state.getValue(TOP) ? 4 : 0) + (state.getValue(SIDE) ? 8 : 0);
	}

	// ItemStackのmetadataからIBlockStateを生成。設置時に呼ばれる
	@Override
	public IBlockState getStateFromMeta(int meta) {

		boolean isTop = false;
		boolean isSide = false;

		if (meta >= 8) {
			meta -= 8;
			isSide = true;
		}

		if (meta >= 4) {
			meta -= 4;
			isTop = true;
		}

		return super.getStateFromMeta(meta).withProperty(TOP, isTop).withProperty(SIDE, isSide);
	}
}
