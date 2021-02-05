package sweetmagic.init.block.crop;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.util.SweetState;

public class BlockLierRose extends BlockBush implements IGrowable, ISMCrop {

	/**
	*　　BlockState総取っ替え
	*　　BlockState必須中身変更記述部分：当たり判定　コンストラクタ　createBlockState()　getSweetState()	 getMaxBlockState()
	* 　　※どれも仕様に大きく関わるため必ず中身を使用するStageに合わせること
	*/

	//=====================記述変更開始==============================

	public BlockLierRose(String name) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(0.0F);
		this.setLightLevel(0.6f);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE3, 0));
		this.setCreativeTab(null);
		BlockInit.blockList.add(this);
	}

	// 当たり判定
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6250D, 1.0D);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE3 });
	}

	public static PropertyInteger getSweetState() {
		return SweetState.STAGE3;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFlourMill();
	}

	//最大成長段階の取得（int用）上書き忘れ注意
	public int getMaxBlockState() {
		return 2;
	}

	//=====================記述変更終わり==============================

	//最大成長段階の取得（IBlockState用）
	public IBlockState getGrownState() {
		return this.getDefaultState().withProperty(this.getSweetState(), 2);
	}

	//基底成長段階の指定
	public IBlockState setGroundState(IBlockState state) {
		return state.withProperty(this.getSweetState(), 0);
	}

	//最重要メソッド　ワールド読み込みなどで呼ばれるやつ。
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int i = meta;
		if (i > this.getMaxBlockState() || i < 0) { i = 0; }
		return this.getDefaultState().withProperty(this.getSweetState(), i);
	}

	//いじる必要なし
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.getSweetState());
	}

	//現在の成長段階をintで取得するためのもの。
	public static int getNowStateMeta(IBlockState state) {
		return SweetState.getInt(state, getSweetState());
	}

	/* IPlantable */
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() != this ? this.getDefaultState() : state;
	}

	//自然成長に必須。　ランダムTick更新処理の書き直しをするときはここをオーバーライドすること
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	//Crop系必須メソッド
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
	}

	//超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	//このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。いじらなくてもよい
	public static IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	//ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(Item.getItemFromBlock(BlockInit.lier_rose), 1));
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		if (this.getNowStateMeta(state) == 1 && world.getBlockState(pos.up()).getMaterial() == Material.AIR && rand.nextInt(2) == 0) {
			double randDouble = rand.nextDouble();
			double d0 = (double) pos.getX() + 0.1D + randDouble * 0.8D;
			double d1 = (double) pos.getY() + 1D + randDouble * 0.3D;
			double d2 = (double) pos.getZ() + 0.1D + randDouble * 0.8D;
			world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d0, d1, d2, 0, 0D, 0);
		}
	}

	//右クリックの処理
	@Nonnull
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return true;
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		Block block = world.getBlockState(pos.down()).getBlock();
		if (block == this) {
			return false;
		} else if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.FARMLAND) {
			return false;
		} else {
			return true;
		}
	}

	//骨粉が使用できるかどうか
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}
}
