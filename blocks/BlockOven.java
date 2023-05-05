package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.api.recipe.oven.OvenRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseCookBlock;
import sweetmagic.init.tile.cook.TileFlourMill;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockOven extends BaseCookBlock {

	private static boolean keepInventory = false;
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getCookList());
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.0625D, 0D, 0.0625D, 0.9375D, 0.78125D, 0.9375D);
	private final int data;

	public BlockOven(String name, int data, boolean isRegister) {
		super(name);
		this.setLightLevel(0.25F);
		this.data = data;
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));

		if (isRegister) {
			BlockInit.ovenList.add(this);
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return  this.data != 1 ? FULL_BLOCK_AABB : AABB;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFlourMill();
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
			if (!recipeInfo.canComplete) { return true; }

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
			tile.hasFork = this.hasFork(player);
			tile.tickTime = 0;
			tile.tickSet = true;

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);
		}

		//完成後
		else if (this.getCook(state).isFIN()) {

			// 結果アイテムのドロップ
			this.spawnXp(player, tile.outPutList, tile.hasFork);
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

	public int getData () {
		return this.data;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		if (this.data == 0) { return; }

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_oven.name")));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
