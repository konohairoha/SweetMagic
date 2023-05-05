package sweetmagic.init.block.crop;

import java.util.Random;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.blocks.SMLeaves;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class SweetCrops_STAGE3 extends BlockBush implements IGrowable, ISMCrop {

	private static final Random srand = new Random();
	private static float toGrowValue = 10F;	//成長乱数（小さいほど早く成長しやすい）
	private final int data;

	/**
	*　　BlockState総取っ替え
	*　　BlockState必須中身変更記述部分：当たり判定　コンストラクタ　createBlockState()　getSweetState()	 getMaxBlockState()
	* 　　※どれも仕様に大きく関わるため必ず中身を使用するStageに合わせること
	*/

	//=====================記述変更開始==============================

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
		new AxisAlignedBB(0.35D, 0.45D, 0.35D, 0.65D, 1D, 0.65D),
		new AxisAlignedBB(0.2D, 0.25D, 0.2D, 0.8D, 1D, 0.8D),
		new AxisAlignedBB(0.1D, 0.1D, 0D, 0.9D, 1D, 0.9D)
	};

	public SweetCrops_STAGE3(String name, int data) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setGrowValue(8.5F);
		this.setHardness(0F);
        this.setResistance(1024F);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		setCreativeTab((CreativeTabs) null);
		this.setGrowValue(10F + SMConfig.glowthRate * 2.5F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE3, 0));
		this.data = data;
		BlockInit.noTabList.add(this);
	}

	/**
	 * 0 = 栗
	 * 1 = ヤシ
	 * 2 = バナナ
	 */

	// 当たり判定
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[getNowStateMeta(state)];
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE3 });
	}

	public PropertyInteger getSweetState() {
		return SweetState.STAGE3;
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
		if(i > this.getMaxBlockState() || i < 0) { i = 0; }
		return this.getDefaultState().withProperty(this.getSweetState(), i);
	}

	//いじる必要なし
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.getSweetState());
	}

	//現在の成長段階をintで取得するためのもの。
	public int getNowStateMeta(IBlockState state) {
		return SweetState.getInt(state, this.getSweetState());
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return this.getVec(state, world, pos);
	}

	/* IPlantable */
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() != this ? getDefaultState() : state;
	}

	public void setGrowValue(float value) {
		this.toGrowValue = value;
	}

	//自然成長に必須。　ランダムTick更新処理の書き直しをするときはここをオーバーライドすること
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);
		if (!world.isAreaLoaded(pos, 1) || world.getLightFromNeighbors(pos.up()) < 9) { return; }

		int i = this.getNowStateMeta(state);
		if (i < this.getMaxBlockState()) {
			float f = SMUtil.getGrowthChance(this, world, pos, 0.5F);
			if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (this.toGrowValue / f) + 1) == 0)) {
				world.setBlockState(pos, this.withStage(world, state, i + 1), 2);
				ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
			}
		}
	}

	//Crop系必須メソッド
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int i = this.getNowStateMeta(state) + this.getBonemealAgeIncrease(world);
		int j = this.getMaxBlockState();
		i = i > j ? j : i;
		world.setBlockState(pos, SweetState.setInt(state, this.getSweetState(), i), 2);
	}

	//超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	//このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。いじらなくてもよい
	public IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	//ドロップする種
	protected Item getSeed() {
		return null;
	}

	//ドロップする作物
	public Item getCrop() {
		switch (this.data) {
		case 0: return ItemInit.chestnut;
		case 1: return ItemInit.coconut;
		case 2: return ItemInit.banana;
		}
		return null;
	}

	// ドロップする作物
	public Item getDropItem () {
		return this.getCrop();
	}

	//ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		// 最大成長Ageではない場合、種を落とすようにするための処理
		int age = this.getNowStateMeta(state);
		if (age < this.getMaxBlockState()) { return; }

		drops.add(new ItemStack(this.getCrop(), Math.max(1, srand.nextInt(3) + 3 + fortune + SMConfig.glowthValue), 0));
	}

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
		return 0;
	}

	// ドロップ数
	@Override
	public int getDropValue (Random rand, int fortune) {
		return Math.max(1, rand.nextInt(4) + 1 + SMConfig.glowthValue);
	}

	// 右クリック
	public void onRicghtClick (World world, EntityPlayer player, IBlockState state, BlockPos pos, ItemStack stack) {

	    Random rand = new Random();
		int age = this.getNowStateMeta(state);

		if (age >= this.getMaxBlockState()) {
            EntityItem drop = this.getDropItem(world, player, stack, this.getCrop(), this.getDropValue(rand, 0));
			world.spawnEntity(drop);
			world.setBlockState(pos, this.withStage(world, state, 0), 2); //作物の成長段階を2下げる
			this.playCropSound(world, rand, pos);
		}

		else {

			ItemStack stackB = new ItemStack(Items.DYE, 1, 15);

			if (ItemStack.areItemsEqual(stack, stackB)) {
				ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				if (!player.isCreative()) { stack.shrink(1); }
				world.setBlockState(pos, this.withStage(world, state, this.getNowStateMeta(state) + 1), 2);
			}
		}
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return world.getBlockState(pos.up()).getBlock() instanceof SMLeaves;
	}

	public boolean canSustainBush(IBlockState state) {
		return this.data == 0 ? state.getMaterial() == Material.AIR : super.canSustainBush(state);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos.up()).getBlock() instanceof SMLeaves;
	}

	//骨粉が使用できるかどうか
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	//必ず骨粉が効くようにする場合ここをいじろう。
	protected int getBonemealAgeIncrease(World worldIn) {
		return 1;
	}

	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	//成長段階を簡単に見られる。
	public boolean isMaxAge(IBlockState state) {
		return this.getNowStateMeta(state) >= this.getMaxBlockState();
	}

	public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {

		float chance = player.getHeldItemMainhand().getItem() == ItemInit.alt_shears ? 1F : 0.33F;
		if (chance > player.world.rand.nextFloat()) {
			this.spawnAsEntity(player.world, pos, new ItemStack(this));
		}

		return super.canHarvestBlock(world, pos, player);
	}
}
