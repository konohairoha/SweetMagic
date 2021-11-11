package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.api.recipe.oven.OvenRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockOven extends BaseFaceBlock {

	public static boolean keepInventory = false;
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getCookList());

	public BlockOven(String name) {
		super(Material.IRON, name);
		setHardness(0.3F);
		setResistance(1024F);
		setSoundType(SoundType.STONE);
		this.setLightLevel(0.25F);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));
		disableStats();
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFlourMill();//TileEntityは処理自体ほぼ同じなため製粉機を指定
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileFlourMill tile = (TileFlourMill) world.getTileEntity(pos);

		// 未起動
		if (this.getCook(state).isOFF()) {

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;

			// 手持ちアイテムからレシピと一致するかを検索
			OvenRecipeInfo recipeInfo = SweetMagicAPI.getOvenRecipeInfo(stack, pInv);

			// 入れるアイテム、完成品はItemStackリストに突っ込む
			ItemStack handitem = stack.copy();
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();

			// クラフト失敗
			if (!recipeInfo.canComplete) { return false; }

			// クラフト成功
			else {

				// クラフト処理
				RecipeUtil recipeUtil = RecipeHelper.recipeAllCraft(recipeInfo, player, stack);
				handitem = recipeUtil.getHand();
				inputs.addAll(recipeUtil.getInput());
				results.addAll(recipeUtil.getResult());
			}

			tile.handItem = handitem;
			tile.inPutList = inputs;
			tile.outPutList = results;
			tile.tickTime = 0;
			tile.tickSet = true;

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);
		}

		//完成後
		else if (this.getCook(state).isFIN()) {

			// 結果アイテムのドロップ
			this.spawnItem(world, player, tile.outPutList);
			this.setState(world, pos);

			// ハンドアイテムとかの初期化
			TileFlourMill nextTile = (TileFlourMill) world.getTileEntity(pos);
			nextTile.clearItem();
		}

		return true;
	}

    public static void setState(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;
		world.setBlockState(pos, EnumCook.transitionCook(world.getBlockState(pos), COOK), 3);
        keepInventory = false;
        if (tile != null){
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (keepInventory) { return; }

		// 製粉機（オフ状態）か製粉機（稼働状態）のときtileの入力リストを取り出す
		if (!this.getCook(state).isOFF()) {
			TileFlourMill tile = (TileFlourMill) world.getTileEntity(pos);
			this.spawnItem(world, pos, tile.inPutList);
			world.spawnEntity(tile.getEntityItem(pos, tile.handItem));
		}
		spawnAsEntity(world, pos, new ItemStack(this));
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

	public EnumCook getCook (IBlockState state) {
		return state.getValue(COOK);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, COOK });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + state.getValue(COOK).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		FaceCookMeta fcMeta = EnumCook.getMeta(meta);
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(fcMeta.getMeta())).withProperty(COOK, fcMeta.getCook());
	}
}
