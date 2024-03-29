package sweetmagic.init.block.crop;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseMagicalCrops;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.tile.plant.TileSannyFlower;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class MagiaFlower extends BaseMagicalCrops implements ISMCrop {

	public final int data;
	private static final Random srand = new Random();
	public static boolean flagDaytime = false;

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.0625D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.3125D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.4375D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.6250D, 0.9D)
	};

	public MagiaFlower(String name, int meta) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(0.0F);
        this.setResistance(1024F);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setCreativeTab(null);
		this.data = meta;
		BlockInit.noTabList.add(this);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE4, 0));
		if (data == 0) {
			this.setLightLevel(0.5F);
		}

		else if (data == 1) {
			this.setLightLevel(0.4F);
		}
	}

	// 当たり判定
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[this.getNowStateMeta(state)];
	}

	public IBlockState getGrownState() { //最大成長段階の取得（IBlockState用）
		return this.getDefaultState().withProperty(SweetState.STAGE4, 3);
	}

	@Override
	public int getMaxBlockState() { //最大成長段階の取得（int用 ステート初期は０なので-1するのを忘れないでおく。）
		return 3;
	}

	public static int getNowStateMeta_open(IBlockState state) {
		return SweetState.getInt(state, SweetState.STAGE4);
	}

	public IBlockState setGroundState(IBlockState state) { //基底成長段階の指定
		return state.withProperty(SweetState.STAGE4, 0);
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(SweetState.STAGE4, meta & 3);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SweetState.STAGE4);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state;
	}

	public boolean canSustainBush(IBlockState state) {
		Material mate = state.getMaterial();
		return mate == Material.GROUND || mate == Material.GRASS ;
	}

	//BlockState総取っ替え
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE4 });
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getVec(state, world, pos);
	}

	/**
	 * 0 = サニーフラワー
	 * 1 = ムーンブロッサム
	 * 2 = 霧雨の忘れ草
	 */

	//ドロップする種
	@Override
	protected Item getSeed() {

		switch (this.data) {
		case 0: return ItemInit.sannyflower_seed;
		case 1: return ItemInit.moonblossom_seed;
		case 2: return ItemInit.dm_seed;
		}

		return null;
	}

	//ドロップする作物
	@Override
	public Item getCrop() {

		switch (this.data) {
		case 0: return ItemInit.sannyflower_petal;
		case 1: return ItemInit.moonblossom_petal;
		case 2: return ItemInit.dm_flower;
		}

		return null;
	}

	@Override
	protected int getBonemealAgeIncrease(World world) {
		return 0;
	}

	@Override //やりやすいように書き直すテスト
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int i = this.getNowStateMeta(state) + this.getBonemealAgeIncrease(world);
		int j = this.getMaxBlockState();
		if (i > j) { i = j; }
		world.setBlockState(pos, SweetState.setInt(state, SweetState.STAGE4, i), 2);
	}

	public IBlockState withAge(int age) {
		return this.getDefaultState().withProperty(SweetState.STAGE4, age);
	}

	public IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(SweetState.STAGE4, age);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		// TileEntityが昼かどうかを取得して成長判定を渡すようにする。
		float value = 3.4F;	// 成長判定を通すかを判断するための基準値とする。
		float f = SMUtil.getGrowthChance(this, world, pos, 2F);
		int meta = this.getNowStateMeta(state);
		boolean isGrow = rand.nextInt((int) (value / f) + 1) == 0;

		//サニーフラワー用
		if (this.isSanny()) {
			super.updateTick(world, pos, state, rand);
			boolean isAge = meta < this.getMaxBlockState();
			this.onUpdate(world, pos, state, meta, isGrow, isAge);
		}

		// ムーンブロッサム用
		else if (this.isMoon()) {
			super.updateTick(world, pos, state, rand);
			boolean isAge = meta < 3;
			this.onUpdate(world, pos, state, meta, isGrow, isAge);
		}

		// ドリズリィ用
		else if (this.isDM()) {
			super.updateTick(world, pos, state, rand);
			boolean isAge = meta < 3;
			this.onUpdate(world, pos, state, meta, isGrow, isAge);
		}
	}

	// 成長処理
	public void onUpdate (World world, BlockPos pos, IBlockState state, int meta, boolean isGrow, boolean isAge) {
		if (!world.isAreaLoaded(pos, 1) || world.getLightFromNeighbors(pos.up()) >= 9 || !isAge) { return; }
		world.setBlockState(pos, this.withAge(meta + 1), 2);
		ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
	}

	//ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		if (this.isMaxAge(state)) {

			boolean p1 = this.srand.nextBoolean();
			int randInt = this.srand.nextInt(3) + 1;

			//サニーフラワー用、TileEntity側で世界の時間を取得してアイテムが出るようにする
			if (this.isSanny()) {
				this.addDrop(drops, randInt, p1);

			// ムーンブロッサム用
			} else if (this.isMoon()) {
				this.addDrop(drops, randInt, p1);

			// ドリズリィ用
			} else if (this.isDM()) {
				this.addDrop(drops, randInt, p1);
			}

			else {
				drops.add(new ItemStack(this.getSeed(), 1));
			}
		}

		else {
			drops.add(new ItemStack(this.getSeed(), 1));
		}
	}

	public void addDrop (NonNullList<ItemStack> drops, int randInt, boolean p1) {
		drops.add(new ItemStack(this.getSeed(), randInt));
		drops.add(new ItemStack(this.getCrop(), this.srand.nextInt(p1 ? 6 : 4) + 1));
	}

	// 植木鉢用
	public boolean isFlower () {
		return this.isSanny() || this.isMoon() || this.isDM();
	}

	// サニーフラワーなら
	public boolean isSanny () {
		return this.data == 0 && TileSannyFlower.flagDaytime;
	}

	// ムーンブロッサムなら
	public boolean isMoon () {
		return this.data == 1 && !TileSannyFlower.flagDaytime;
	}

	// ドリズリィなら
	public boolean isDM () {
		return this.data == 2 && TileSannyFlower.flagRaining;
	}

	//右クリックの処理
	@Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (world.isRemote) { return true; }

		ItemStack stack = player.getHeldItem(hand);

		if (!this.isSickle(world, player, pos, stack)) {
			this.onRicghtClick(world, player, state, pos, stack);
		}
		return true;
    }

	// 右クリック回収時に戻る成長段階
	@Override
	public int RCSetState () {
		return 1;
	}

	// ドロップ数
	@Override
	public int getDropValue (Random rand, int fortune) {
		return Math.max(1, rand.nextInt(3) + 1);
	}

	// 右クリック
	public void onRicghtClick (World world, EntityPlayer player, IBlockState state, BlockPos pos, ItemStack stack) {

        if(this.isMaxAge(state)) {

            Random rand = world.rand;
            EntityItem drop = this.getDropItem(world, player, stack, this.getCrop(), this.getDropValue(rand, 0));
            world.spawnEntity(drop);
            world.setBlockState(pos, this.withStage(world, state, this.RCSetState()), 2);        //成長段階を2下げる
			this.playCropSound(world, rand, pos);
        }
	}

	public boolean isMaxAge(IBlockState state) {
		return this.getNowStateMeta(state) >= this.getMaxBlockState();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileSannyFlower();
	}
}
