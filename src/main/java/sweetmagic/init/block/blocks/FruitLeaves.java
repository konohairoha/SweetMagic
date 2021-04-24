package sweetmagic.init.block.blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class FruitLeaves extends BlockBush implements IGrowable, IShearable, ISMCrop {

	private static final Random srand = new Random();
	private static float toGrowValue = 10.0F;	//成長乱数（小さいほど早く成長しやすい）
	private final int data;

	/**
	*　　BlockState総取っ替え
	*　　BlockState必須中身変更記述部分：当たり判定　コンストラクタ　createBlockState()　getSweetState()	 getMaxBlockState()
	* 　　※どれも仕様に大きく関わるため必ず中身を使用するStageに合わせること
	*
	*/

	//=====================記述変更開始==============================

	public FruitLeaves(String name, int data) {
		this.setGrowValue(8.5F);
		this.setTickRandomly(true);
		this.setHardness(0F);
        setResistance(1024F);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		setCreativeTab((CreativeTabs) null);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE3, 0));
		BlockInit.blockList.add(this);
		this.data = data;
	}

	/**
	 * 0 = レモン
	 * 1 = みかん
	 * 2 = エストール
	 */

	@Override
	public ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(this, 1);
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1);
	}

	//インターフェース　IShearableで実装 ハサミで回収できるようにする
	//---------------------------------------------------------------------------------
	public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos) {
        return true;
    }

	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Arrays.asList(new ItemStack(this, 1, 0));
    }
	//---------------------------------------------------------------------------------

	@Override	//当たり判定を1ブロックサイズへ変更
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
        return FULL_BLOCK_AABB;
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return FULL_BLOCK_AABB;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE3 });
	}

	public static PropertyInteger getSweetState() {
		return SweetState.STAGE3;
	}

	//最大成長段階の取得（int用）上書き忘れ注意
	public int getMaxBlockState() {
		return 2;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
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
		if(i > this.getMaxBlockState() || i < 0) {
			i = 0;
		}
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

	// IPlantable
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Crop;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return state.getBlock() != this ? this.getDefaultState() : state;
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
			float f = SMUtil.getGrowthChance(this, world, pos, 2F);
			if (ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt((int) (toGrowValue / f) + 1) == 0)) {
				world.setBlockState(pos, this.withStage(world, state, i + 1), 2);
				ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
			}
		}
	}

	//Crop系必須メソッド
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		this.grow(world, pos, state);
	}

	//上のメソッドで使用するためのもの
	public void grow(World world, BlockPos pos, IBlockState state) {
		int i = this.getNowStateMeta(state) + this.getBonemealAgeIncrease(world);
		int j = this.getMaxBlockState();
		if (i > j) { i = j; }
		world.setBlockState(pos, SweetState.setInt(state, this.getSweetState(), i), 2);
	}

	//超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	//このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。いじらなくてもよい
	public static IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	//ドロップする種
	protected Item getSeed() {

		Block block = null;

		switch (this.data) {
		case 0:
			block = BlockInit.lemon_sapling;
			break;
		case 1:
			block = BlockInit.orange_sapling;
			break;
		case 2:
			block = BlockInit.estor_sapling;
		break;
		}

		return SMUtil.getItemBlock(block);
	}

	//ドロップする作物
	protected Item getCrop() {

		switch (this.data) {
		case 0:	return ItemInit.lemon;
		case 1:	return ItemInit.orange;
		case 2:	return ItemInit.estor_apple;
		}

		return null;
	}

	//ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {

		int age = getNowStateMeta(state);

		// 最大成長してるなら
		if (age >= this.getMaxBlockState()) {
	    	drops.add(new ItemStack(this.getCrop(), this.srand.nextInt(4) + 1));
		}

	    float fl = this.srand.nextFloat();
	    if(fl < 0.1F) { drops.add(new ItemStack(this.getSeed(), 1)); }
	}

	//右クリックの処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

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
            EntityItem drop = new EntityItem(world, player.posX,player.posY, player.posZ, new ItemStack(this.getCrop(), rand.nextInt(3) + 1));
            world.spawnEntity(drop);
            world.setBlockState(pos, this.withStage(world, state, 0), 2);        //作物の成長段階を2下げる
            world.playSound((EntityPlayer)null, pos,SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
			this.playCropSound(world, rand, pos);
        }

        else {

            ItemStack stackB = new ItemStack(Items.DYE,1,15);
            if(ItemStack.areItemsEqual(stack, stackB)) {
            	ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
            	if (!PlayerHelper.isCleative(player)) { stack.shrink(1); }
                world.setBlockState(pos, this.withStage(world, state, getNowStateMeta(state) + 1), 2);
            }
        }
	}

	//土、草、耕地に置いても壊れないようにする
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return this.canPlaceBlockAt(world, pos);
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return true;
	}

	//骨粉が使用できるかどうか
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	//必ず骨粉が効くようにする場合ここをいじろう。
	protected int getBonemealAgeIncrease(World worldIn) {
		return 1;
	}

	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return !this.isMaxAge(state);
	}

	//成長段階を簡単に見られる。
	public boolean isMaxAge(IBlockState state) {
		return this.getNowStateMeta(state) >= this.getMaxBlockState();
	}

	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
}
