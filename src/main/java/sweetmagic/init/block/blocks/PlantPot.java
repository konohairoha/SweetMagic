package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.block.crop.MagiaFlower;

public class PlantPot extends BaseModelBlock {

	private final int data;
	private static final PropertyBool BACK = PropertyBool.create("back");
	private static final PropertyBool FORWARD = PropertyBool.create("forward");
	private static final PropertyBool LEFT = PropertyBool.create("left");
	private static final PropertyBool RIGHT = PropertyBool.create("right");

	public PlantPot(String name, SoundType sound, int data) {
		super(Material.GROUND, name);
		setSoundType(sound);
		setHardness(0.5F);
        setResistance(1024F);
		this.setTickRandomly(true);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(BACK, false)
				.withProperty(FORWARD, false)
				.withProperty(LEFT, false)
				.withProperty(RIGHT, false));
		BlockInit.blockList.add(this);
		this.data = data;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return true;
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
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		boolean back = world.getBlockState(pos.south()).getBlock() == this;
		boolean forward = world.getBlockState(pos.north()).getBlock() == this;
		boolean left = world.getBlockState(pos.west()).getBlock() == this;
		boolean right = world.getBlockState(pos.east()).getBlock() == this;
		return state.withProperty(BACK, back).withProperty(FORWARD, forward).withProperty(LEFT, left).withProperty(RIGHT, right);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { BACK, FORWARD, LEFT, RIGHT });
	}
}
