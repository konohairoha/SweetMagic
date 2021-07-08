package sweetmagic.init.block.crop;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipeInfo;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.item.sm.magic.MFTime;
import sweetmagic.init.item.sm.magic.MFWeather;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;

public class BlockAlstroemeria extends BlockBush implements IGrowable, ISMCrop {

	private static final String MF = "mf";

	/**
	*　　BlockState総取っ替え
	*　　BlockState必須中身変更記述部分：当たり判定　コンストラクタ　createBlockState()　getSweetState()	 getMaxBlockState()
	* 　　※どれも仕様に大きく関わるため必ず中身を使用するStageに合わせること
	*
	*/

	//=====================記述変更開始==============================

	private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
		new AxisAlignedBB(0.25D, 0D, 0.25D, 0.75D, 0.5D, 0.75D),
		new AxisAlignedBB(0.125D, 0D, 0.125D, 0.875D, 0.6D, 0.875D)
	};

	public BlockAlstroemeria(String name) {
		this.setTickRandomly(true);
		this.setHardness(0F);
        this.setResistance(1024F);
		this.setSoundType(SoundType.PLANT);
		this.disableStats();
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setLightLevel(0.6f);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SweetState.STAGE2, 0));
		this.setCreativeTab(null);
		BlockInit.magicList.add(this);
	}

	// 当たり判定
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return CROPS_AABB[getNowStateMeta(state)];
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { SweetState.STAGE2 });
	}

	public static PropertyInteger getSweetState() {
		return SweetState.STAGE2;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileAlstroemeria();
	}

	// 最大成長段階の取得（int用）上書き忘れ注意
	public int getMaxBlockState() {
		return 1;
	}

	//=====================記述変更終わり==============================

	// 最大成長段階の取得（IBlockState用）
	public IBlockState getGrownState() {
		return this.getDefaultState().withProperty(this.getSweetState(), 1);
	}

	// 基底成長段階の指定
	public IBlockState setGroundState(IBlockState state) {
		return state.withProperty(this.getSweetState(), 0);
	}

	// 最重要メソッド　ワールド読み込みなどで呼ばれるやつ。
	@Override
	public IBlockState getStateFromMeta(int meta) {
		int i = meta;
		if (i > this.getMaxBlockState() || i < 0) { i = 0; }
		return this.getDefaultState().withProperty(this.getSweetState(), i);
	}

	// いじる必要なし
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(this.getSweetState());
	}

	// 現在の成長段階をintで取得するためのもの。
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

	// Crop系必須メソッド
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) { }

	// 超必須メソッド　これがないとStatic参照ができずうまくBlockStateをやりくりしにくい
	// このメソッドが受け持つ役割は基本的に作物の成長段階を外からいじるときに使う。いじらなくてもよい
	public static IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(getSweetState(), age);
	}

	// ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(Item.getItemFromBlock(BlockInit.twilight_alstroemeria), 1));
	}

	// 右クリックの処理
	@Nonnull
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (!world.isRemote) {

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;
			Boolean tmFlg = false; 						// 時間操作用
			Random rand = world.rand;
			int age = getNowStateMeta(state);			// 花の状態を取得
			ItemStack stack = player.getHeldItem(hand);	// 手に持ってるアイテム

			if (stack.getItem() == ItemInit.veil_darkness) {

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-16, 0, -16), pos.add(16, 6, 16))) {

					IBlockState sta = world.getBlockState(p);
					Block block = sta.getBlock();
					if (block == Blocks.AIR) { continue; }

					if (block.isFullCube(sta)) {
						player.sendMessage(new TextComponentTranslation("tip.twas_scope.name", new Object()));
						return true;
					}
				}

				TileAlstroemeria tile = (TileAlstroemeria) world.getTileEntity(pos);

				if (!tile.isSummon) {

					tile.isSummon = true;
					tile.markDirty();
					world.notifyBlockUpdate(pos, state, state, 3);

					if (!PlayerHelper.isCleative(player)) { stack.shrink(1); }
				}

				return true;
			}


			//時間操作処理
			if (player.isSneaking()) {
				tmFlg = this.timeSet(world, pInv, player);
			}

			// 花が咲いていたら
			else if (age >= this.getMaxBlockState()) {


				// アルストロメリアレシピの取得
				this.getRecipeAlstroemeria(world, pos, pInv, player, stack, rand);
			}

			// 時間操作時の音
			if (tmFlg) {
				world.playSound(null, pos, SMSoundEvent.CHANGETIME, SoundCategory.VOICE, 0.5F, 1F);
			}
		}
		return true;
	}

	// 時間、天気操作
	public boolean timeSet (World world, NonNullList<ItemStack> pInv, EntityPlayer player) {

		Boolean tmFlg = false;

		Object[] objSun = SMUtil.getStackFromPInv(pInv, ItemInit.sannyflower_petal, (byte) 1),
				objMoon = SMUtil.getStackFromPInv(pInv, ItemInit.moonblossom_petal, (byte) 1),
				objMyosotis = SMUtil.getStackFromPInv(pInv, ItemInit.dm_flower, (byte) 1),
				obFire = SMUtil.getStackFromPInv(pInv, ItemInit.fire_nasturtium_petal, (byte) 1
		);

		Container container = player.inventoryContainer;

		// ドリズリィ・ミオソチスのお花を持っている状態だったら1日雨にする
		if (objMyosotis != null) {
			MFWeather item = (MFWeather) ItemInit.dm_flower;
			item.changeWeather(world);
			SMUtil.decrPInvMin(player, 1, objMyosotis[0]);
			tmFlg = true;
		}

		// ファイアーナスタチウムを持っていたら晴れにする
		else if (obFire != null) {
			MFWeather item = (MFWeather) ItemInit.fire_nasturtium_petal;
			item.changeWeather(world);
			SMUtil.decrPInvMin(player, 1, obFire[0]);
			tmFlg = true;
		}

		// 時間操作のお花の処理
		else if (objSun != null) {

			// 夕方設定
			if (objMoon != null) {
				this.setTime(world, 11000);
				SMUtil.decrPInvMin(player, 1, objSun[0], objMoon[0]);
				tmFlg = true;
			}

			// 朝に設定
			else {
				this.setTime(world, this.getMFTime(SMUtil.getItem(objSun[1])).getTime());
				SMUtil.decrPInvMin(player, 1, objSun[0]);
				tmFlg = true;
			}

			container.detectAndSendChanges();

		}

		// 夜に設定
		else if (objMoon != null) {
			this.setTime(world, this.getMFTime(SMUtil.getItem(objMoon[1])).getTime());
			SMUtil.decrPInvMin(player, 1, objMoon[0]);
			container.detectAndSendChanges();
			tmFlg = true;
		}

		return tmFlg;
	}

	public void setTime (World world, int time) {

		// 時間設定
		if (world instanceof WorldServer) {
			int dayTime = 24000;
            WorldServer sever = world.getMinecraftServer().getWorld(0);
            long day = (sever.getWorldTime() / dayTime) + 1;
            sever.setWorldTime(time + (day * dayTime));
		}
	}

	// アルストロメリアレシピの取得
	public void getRecipeAlstroemeria (World world, BlockPos pos, NonNullList<ItemStack> pInv, EntityPlayer player, ItemStack stack, Random rand) {

		//----------------------------------レシピ-----------------------------------------
		//sneaking状態ではない、夕方だったら(常に稼働させる場合、BlockStateを常にMaxにしなければならない)
		//※特殊処理を優先
		//所持アイテムが砂利もしくは火打ち石のとき

		AlstroemeriaRecipeInfo recipeInfo = SweetMagicAPI.getAlstroemeriaRecipeInfo(stack, pInv);

		// canComplete = Falseの場合レシピ処理をしない
		if (!recipeInfo.canComplete) { return; }

		// NBTを取得
		RecipeUtil recipeUtil = RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);
		AdvancedInit.astral_craft.triggerFor(player);

		for (ItemStack result : recipeUtil.getResult()) {
			world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, result));
			ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.LAVA);
		}

		// 変換時の音
		this.playerSound(world, pos);
	}

	public void playerSound (World world, BlockPos pos) {
		world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_BLAST_FAR, SoundCategory.VOICE,
				0.5F, 1F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
	}

	// 骨粉が使用できるかどうか
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return false;
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return false;
	}

	// 時間操作アイテムを取得
	public MFTime getMFTime(Item item) {
		return (MFTime) item;
	}
}
