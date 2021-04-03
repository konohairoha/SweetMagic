package sweetmagic.init.base;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.init.tile.plant.TileSannyFlower;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class BaseMagicalCrops extends BlockBush implements IGrowable {

	//	public final int data;
	Random srand = new Random();
	public static float toGrowValue = 10.0F;

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE4 });
	}

	public static PropertyInteger getSweetState() {
		return SweetState.STAGE4;
	}

	// tile持たない作物はstateを隠す
	public PropertyInteger getSweetState_hide() {
		return SweetState.STAGE4;
	}

	@Override
	public Block setCreativeTab(CreativeTabs tab) {
		return null;
	}

	// 最大成長段階の取得（int用）上書き忘れ注意
	public int getMaxBlockState() {
		return 3;
	}

	// 最重要メソッド　ワールド読み込みなどで呼ばれるやつ。
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int i = meta;
		if (i > this.getMaxBlockState() || i < 0) { i = 0; }
		return this.getDefaultState().withProperty(this.getSweetState_hide(), i);
	}

	// 最大成長段階の取得（IBlockState用）
	public IBlockState getGrownState() {
		return this.getDefaultState().withProperty(this.getSweetState_hide(), 3);
	}

	// 基底成長段階の指定　いらないかも
	public IBlockState setGroundState(IBlockState state) {
		return state.withProperty(this.getSweetState(), 0);
	}

	// いじる必要なし
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state;
	}

	// いじる必要なし
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.getSweetState_hide());
	}

	// 現在の成長段階をintで取得するためのもの。
	public int getNowStateMeta(IBlockState state) {
		return SweetState.getInt(state, this.getSweetState_hide());
	}

	// Crop系必須メソッド
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int i = this.getNowStateMeta(state) + this.getBonemealAgeIncrease(world);
		int j = this.getMaxBlockState();
		if (i > j) { i = j; }
		world.setBlockState(pos, SweetState.setInt(state, this.getSweetState(), i), 2);
	}

	// 自然成長に必須。　ランダムTick更新処理の書き直しをするときはここをオーバーライドすること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);
		if (!world.isAreaLoaded(pos, 1) || world.getLightFromNeighbors(pos.up()) < 9) { return; }

		int i = this.getNowStateMeta(state);
		if (i < this.getMaxBlockState()) {

			float f = SMUtil.getGrowthChance(this, world, pos, 2F);
			if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (toGrowValue / f) + 1) <= 0)) {

				world.setBlockState(pos, this.growStage(world, state, i + 1), 2);
				ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
			}
		}
	}

	// 超必須メソッド　内部でBlockState色々いじる用
	public IBlockState growStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(this.getSweetState_hide(), age);
	}

	// 骨粉が使用できるかどうか
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	// 必ず骨粉が効くようにする場合ここをいじろう。
	protected int getBonemealAgeIncrease(World worldIn) {
		return 0;
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	// 成長段階を簡単に見られる。
	public boolean isMaxAge(IBlockState state) {
		return this.getNowStateMeta(state) >= this.getMaxBlockState();
	}

	//AGEが最大のときに種と作物をドロップ
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
	}

	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getSeed());
	}

	// ドロップする種をここで記述
	protected Item getSeed() {
		return null;
	}

	// ドロップする作物をここで記述
	protected Item getCrop() {
		return null;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true; // 使うときにはここをtrueへ変える
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileSannyFlower(); // TileEntityクラスを返す
	}
}
