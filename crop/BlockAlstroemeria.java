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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.EnumPlantType;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipeInfo;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.item.sm.magic.MFTime;
import sweetmagic.init.item.sm.magic.MFWeather;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;
import sweetmagic.util.SMUtil;
import sweetmagic.util.SweetState;
import sweetmagic.util.WorldHelper;

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

	public PropertyInteger getSweetState() {
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
	public int getNowStateMeta(IBlockState state) {
		return SweetState.getInt(state, this.getSweetState());
	}

	@Deprecated
	public Vec3d getOffset(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState under = world.getBlockState(pos.down());
		double topY = under.getBlock().getBoundingBox(under, world, pos.down()).maxY;
		double y = topY == 1D ? 0D : -1D + under.getBlock().getBoundingBox(under, world, pos.down()).maxY * 1D;
		return new Vec3d(0D, y, 0D);
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
	public IBlockState withStage(World world, IBlockState state, int age) {
		return state.getBlock().getDefaultState().withProperty(this.getSweetState(), age);
	}

	// ドロップ数を変更
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(new ItemStack(BlockInit.twilight_alstroemeria));
	}

	// 右クリックの処理
	@Nonnull
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (!world.isRemote) {

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;
			Boolean tmFlg = false; 						// 時間操作用
			Random rand = world.rand;
			ItemStack stack = player.getHeldItem(hand);		// 手に持ってるアイテム
			Item item = stack.getItem();
			TileAlstroemeria tile = (TileAlstroemeria) world.getTileEntity(pos);

			// 召喚中なら終了
			if (tile.isSummon) { return false; }

			// ピースフル以外かついずれかのアイテムを持っている
			if (!WorldHelper.isPeaceful(world) && (item == ItemInit.veil_darkness || item == ItemInit.magic_book_scarlet)) {
				return this.summonBoss(world, pos, state, tile, player, stack, item, pInv, rand);
			}

			// 黄昏の光を持っている場合
			else if (item == Item.getItemFromBlock(BlockInit.twilightlight) && world.isAirBlock(pos.up())) {

            	world.setBlockState(pos.up(), this.getBlock(stack).getDefaultState(), 3);
                if (!player.isCreative()) { stack.shrink(1); }

                SoundType sound = SoundType.STONE;
                this.playerSound(world, pos.up(), sound.getPlaceSound(),(sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F);
    			world.setBlockState(pos, this.withStage(world, state, 1), 2);
			}

			//時間操作処理
			if (player.isSneaking() && stack.isEmpty()) {
				tmFlg = this.timeSet(world, pInv, player);
			}

			// 花が咲いていたらアルストロメリアレシピの取得
			if (!tmFlg && !stack.isEmpty() && this.isMaxAge(state)) {
				this.getRecipeAlstroemeria(world, pos, pInv, player, stack, false);
			}

			// 時間操作時の音
			if (tmFlg) {
				world.playSound(null, pos, SMSoundEvent.CHANGETIME, SoundCategory.BLOCKS, 0.5F, 1F);
			}
		}
		return true;
	}

	// ボス召喚
	public boolean summonBoss(World world, BlockPos pos, IBlockState state, TileAlstroemeria tile, EntityPlayer player, ItemStack stack, Item item, NonNullList<ItemStack> pInv, Random rand) {

		if (item == ItemInit.veil_darkness) {

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-16, 0, -16), pos.add(16, 6, 16))) {

				IBlockState sta = world.getBlockState(p);
				Block block = sta.getBlock();
				if (block == Blocks.AIR) { continue; }

				if (block.isFullCube(sta)) {
					if (!this.getRecipeAlstroemeria(world, pos, pInv, player, stack, false)) {
						player.sendMessage(new TextComponentTranslation("tip.twas_scope.name", new Object()));
					}
					return true;
				}
			}

			tile.mobData = 1;
			if (!player.isCreative()) { stack.shrink(1); }
		}

		else if (item == ItemInit.magic_book_scarlet) {

			IMFTool mfTool = (IMFTool) item;

			// mfが足りない
			if (mfTool.getMF(stack) < 6000000) {
				player.sendMessage(new TextComponentTranslation("tip.twas_nomf.name"));
				return true;
			}

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-32, 0, -32), pos.add(32, 5, 32))) {

				IBlockState sta = world.getBlockState(p);
				Block block = sta.getBlock();
				if (block == Blocks.AIR) { continue; }

				// 平地になっていない
				if (block.isFullCube(sta)) {
					if (!this.getRecipeAlstroemeria(world, pos, pInv, player, stack, false)) {
						player.sendMessage(new TextComponentTranslation("tip.twas_scope2.name", new Object()));
					}
					return true;
				}
			}

			for (BlockPos p : BlockPos.getAllInBox(pos.add(-32, -1, -32), pos.add(32, -1, 32))) {

				IBlockState sta = world.getBlockState(p);
				Block block = sta.getBlock();
				if (block.isFullCube(sta)) { continue; }

				// 平地になっていない
				if (!this.getRecipeAlstroemeria(world, pos, pInv, player, stack, false)) {
					player.sendMessage(new TextComponentTranslation("tip.twas_scope2.name", new Object()));
				}
				return true;
			}

			tile.mobData = 2;
			mfTool.setMF(stack, 0);
		}

		tile.isSummon = true;
		tile.markDirty();
		world.notifyBlockUpdate(pos, state, state, 3);

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
				this.setTime(world, 9500);
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

	public boolean isMaxAge (IBlockState state) {
		return getNowStateMeta(state) >= this.getMaxBlockState();
	}

    public Block getBlock (ItemStack stack) {
    	return ((ItemBlock) stack.getItem()).getBlock();
    }

	// サウンド
	public void playerSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	// サウンド
	public void playerSound (World world, EntityPlayer player, SoundEvent sound, float vol, float pit) {
		this.playerSound(world, new BlockPos(player), sound, vol, pit);
	}

	// アルストロメリアレシピの取得
	public boolean getRecipeAlstroemeria (World world, BlockPos pos, NonNullList<ItemStack> pInv, EntityPlayer player, ItemStack stack, boolean isAllCraft) {

		//----------------------------------レシピ-----------------------------------------
		//sneaking状態ではない、夕方だったら(常に稼働させる場合、BlockStateを常にMaxにしなければならない)
		//※特殊処理を優先
		//所持アイテムが砂利もしくは火打ち石のとき

		// canComplete = Falseの場合レシピ処理をしない
		AlstroemeriaRecipeInfo recipeInfo = SweetMagicAPI.getAlstroemeriaRecipeInfo(stack, pInv);
		if (!recipeInfo.canComplete) { return false; }

		// クラフト処理
		RecipeUtil recipeUtil = isAllCraft ? RecipeHelper.recipeAllCraft(recipeInfo, player, stack) : RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);
		AdvancedInit.astral_craft.triggerFor(player);

		for (ItemStack result : recipeUtil.getResult()) {
			world.spawnEntity(new EntityMagicItem(world, player, result));
			ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.LAVA);
		}

		// 変換時の音
		this.playerSound(world, pos);
		return true;
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

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileAlstroemeria tile = (TileAlstroemeria) world.getTileEntity(pos);

    	// 召喚中なら夜の帳をドロップ
    	if (tile.isSummon) {
    		if (tile.mobData == 1) {
        		spawnAsEntity(world, pos, new ItemStack(ItemInit.veil_darkness));
    		}

    		else {
        		spawnAsEntity(world, pos, new ItemStack(BlockInit.magiaflux_block, 6));
    		}
    	}

    	super.breakBlock(world, pos, state);
    }
}
