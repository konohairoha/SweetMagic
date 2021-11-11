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
import net.minecraftforge.common.ForgeHooks;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class SweetCrops_STAGE5 extends BlockBush implements IGrowable, ISMCrop  {

	public Random srand = new Random();
	public int stage = 4;
	public final int metaCrop;		//作物種別
	public final int stayGrnd;		//作物がその場に留まる条件
	public final float growValue;	//成長のしやすさ(少ないほど育ちやすい)
	public int RC_SetStage = 1;	//右クリック回収時セットする成長段階(0～)

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.0625D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.3125D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.4375D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.6250D, 0.9D),
		new AxisAlignedBB(0.1D, 0.0D, 0.1D, 0.9D, 0.6250D, 0.9D)
	};

	//  当たり判定
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[getNowStateMeta(state)];
	}

	public SweetCrops_STAGE5(String name, int data, int grnd, float growVal) {
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setHardness(0.0F);
        this.setResistance(1024F);
		this.setTickRandomly(true);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.stayGrnd = grnd;
		this.metaCrop = data;
		this.growValue = growVal;
		this.setCreativeTab(null);
		BlockInit.noTabList.add(this);
		//必ずメソッド含めすべてのBlockStateの上書きをすること。SweetStateのプロパティ入りBlockStateであるSTAGE(任意の数字)を使う。
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE5, 0));
		this.RC_SetStage  = 2;
	}

	/**
	 * コピペ時更新必須ブロック情報
	 */

	// スタート
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE5 });
	}

	public static PropertyInteger getSweetState() {
		return SweetState.STAGE5;
	}

	public int getMaxBlockState() {
		return 4;
	}

	// 成長できるか
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	//骨粉が使用できるかどうか
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	//必ず骨粉が効くようにする場合ここをいじる
	protected int getBonemealAgeIncrease(World world) {
		return 1;
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		return new Vec3d(0D, this.getPosY(world, pos.down()), 0D);
	}

	/**
	 * 作物情報
	 */

	/**
	 * 0 = ブルーベリー
	 * 1 = オリーブ
	 * 2 = バニラ
	 * 3 = 玉ねぎ
	 * 4 = スッテキー
	 * 5 = コーヒー豆
	 * 6 = パイナップル
	 */
	protected Item getSeed() {

		switch (this.metaCrop) {
		case 0:	return ItemInit.blueberry;
		case 1:	return ItemInit.olive;
		case 2:	return ItemInit.vannila_pods;
		case 3:	return ItemInit.onion;
		case 4:	return ItemInit.sticky_stuff_seed;
		case 5:	return ItemInit.coffee_seed;
		case 6:	return ItemInit.pineapple_seed;
		}

		return null;
	}

	// ドロップする作物
	protected Item getCrop() {

		switch (this.metaCrop) {
		case 0:	return ItemInit.blueberry;
		case 1:	return ItemInit.olive;
		case 2:	return ItemInit.vannila_pods;
		case 3:	return ItemInit.onion;
		case 4:	return ItemInit.sticky_stuff_petal;
		case 5:	return ItemInit.coffee_seed;
		case 6:	return ItemInit.pineapple;
		}

		return null;
	}

	// ドロップする作物
	public Item getDropItem () {
		return this.getCrop();
	}

	// 終わり

	//最重要メソッド　ワールド読み込みなどで呼ばれるやつ。
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int i = meta;
		if (i > this.getMaxBlockState() || i < 0) { i = 0; }
		return this.getDefaultState().withProperty(this.getSweetState(), i);
	}

	//超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	//このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。
	public static IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	//いじる必要なし
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state;
	}

	//いじる必要なし
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.getSweetState());
	}

	//現在の成長段階をintで取得するためのもの。いじる必要なし
	public int getNowStateMeta(IBlockState state) {
		return SweetState.getInt(state, this.getSweetState());
	}

	//Crop系必須メソッド
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int i = this.getNowStateMeta(state) + this.getBonemealAgeIncrease(world);
		int j = this.getMaxBlockState();
		if (i > j) { i = j; }
		world.setBlockState(pos, SweetState.setInt(state, this.getSweetState(), i), 2);
	}

	//自然成長に必須。
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {

		super.updateTick(world, pos, state, rand);
		if (!world.isAreaLoaded(pos, 1) || world.getLightFromNeighbors(pos.up()) < 9) { return; }

		int i = this.getNowStateMeta(state);

		if (i < this.getMaxBlockState()) {
			float f = SMUtil.getGrowthChance(this, world, pos, 2F);
			if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (this.growValue / f) + 1) == 0)) {
				world.setBlockState(pos, this.growStage(world, state, i + 1), 2);
				ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
			}
		}
	}

	//超必須メソッド　内部でBlockState色々いじる用
	public IBlockState growStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(this.getSweetState(), age);
	}

	//成長段階を簡単に見られる。
	public boolean isMaxAge(IBlockState state) {
		return this.getNowStateMeta(state) >= this.getMaxBlockState();
	}

	//AGEが最大のときに種と作物をドロップ
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
	}

	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getSeed());
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {

		if (this.stayGrnd == 1) {
	        if (state.getBlock() == this) {
	            IBlockState soil = world.getBlockState(pos.down());
	            return soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
	        }
	        return this.canSustainBush(world.getBlockState(pos.down()));
		}

        IBlockState soil = world.getBlockState(pos.down());
        return (world.getLight(pos) >= 8 || world.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
	}

	public boolean canSustainBush(IBlockState state) {
		Material mate = state.getMaterial();
		return mate == Material.GROUND || mate == Material.GRASS ;
	}

	//ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		int age = getNowStateMeta(state);
		if (age >= this.getMaxBlockState()) {
			drops.add(new ItemStack(this.getCrop(), 3 + fortune, 0));
			for (int i = this.srand.nextInt(4) + 1; i > 0; i--) {
				drops.add(new ItemStack(this.getSeed(), 1, 0));
			}
		}

		// 最大成長Ageではない場合、種を落とすようにするための処理
		else {
			drops.add(new ItemStack(this.getSeed(), 1, 0));
		}
	}

	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){

		if (world.isRemote) { return true; }

		ItemStack stack = player.getHeldItem(hand);

		if (!this.isSickle(world, player, pos, stack)) {
			this.onRicghtClick(world, player, state, pos, stack);
		}
		return true;
	}

	// 右クリック
	public void onRicghtClick (World world, EntityPlayer player, IBlockState state, BlockPos pos, ItemStack stack) {

		int age = this.getNowStateMeta(state);

		if(age >= this.getMaxBlockState()) {
			Random rand = new Random();
	    	EntityItem drop = this.getDropItem(world, player, stack, this.getCrop(), rand.nextInt(3) + 1);
			world.spawnEntity(drop);
			world.setBlockState(pos, this.withStage(world, state, this.RC_SetStage), 2);        //指定の成長段階まで下げる
			this.playCropSound(world, rand, pos);
		}

		else {

			ItemStack stackB = new ItemStack(Items.DYE,1,15);
			if(ItemStack.areItemsEqual(stack, stackB)) {

				ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				if (!player.isCreative()) { stack.shrink(1); }
				world.setBlockState(pos, this.withStage(world, state, this.getNowStateMeta(state) + 1), 2);
			}
		}
	}
}
