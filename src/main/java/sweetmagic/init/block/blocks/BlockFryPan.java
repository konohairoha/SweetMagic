package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.api.recipe.pan.PanRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockFryPan extends BaseFaceBlock {

	public static boolean keepInventory = false;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.2D, 0.1D, 0.8D, 0D, 0.8D);
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getCookList());

	public BlockFryPan(String name) {
		super(Material.IRON, name);
		setHardness(0.1F);
		setResistance(1024F);
		setSoundType(SoundType.STONE);
		setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));
		disableStats();
		BlockInit.furniList.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TilePot();//TileEntityは処理自体ほぼ同じなため製粉機を指定
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

        if (!this.getCook(state).isFIN()) {

            // 下のブロックがコンロではないなら終了
            Block uBlock = world.getBlockState(pos.down()).getBlock();
            if (!(uBlock instanceof BlockStove)) { return false;}
        }

		// tileとプレイヤーのInventoryの取得
		TilePot pot = (TilePot) world.getTileEntity(pos);
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;

		// 未起動
		if (this.getCook(state).isOFF()) {

			// 手持ちアイテムからレシピと一致するかを検索
			PanRecipeInfo recipeInfo = SweetMagicAPI.getPanRecipeInfo(stack, pInv);

			//入れるアイテム、完成品はItemStackリストに突っ込む
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();
			ItemStack copy = stack.copy();

			// クラフト失敗
			if (!recipeInfo.canComplete) {

				ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(stack).copy();
				if (smeltResult.isEmpty() || !(smeltResult.getItem() instanceof ItemFood)) { return false; }

	    		// 必要燃焼時間を超えていないまたはスロットに燃焼アイテムがないなら終了
	    		TileStove stove = (TileStove) world.getTileEntity(pos.down());
	    		if (!stove.canWork()) { return false; }

				inputs.add(stack);
				results.add(smeltResult);
				smeltResult.setCount(stack.getCount());
				stack.shrink(stack.getCount());
			}

			// クラフト成功
			else {

	    		// 必要燃焼時間を超えていないまたはスロットに燃焼アイテムがないなら終了
	    		TileStove stove = (TileStove) world.getTileEntity(pos.down());
	    		if (!stove.canWork()) { return false; }


				// クラフト処理
				RecipeUtil recipeUtil = RecipeHelper.recipeAllCraft(recipeInfo, player, stack);
				copy = recipeUtil.getHand();
				inputs.addAll(recipeUtil.getInput());
				results.addAll(recipeUtil.getResult());
			}

			pot.startFlag = true;
			pot.handItem = copy;
			pot.inPutList = inputs;
			pot.outPutList = results;

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);
		}

		// 完成後
		else if (this.getCook(state).isFIN()) {

			this.spawnItem(world, player, pot.outPutList);
			this.setState(world, pos);

			// ハンドアイテムとかの初期化
			TilePot nextTile = (TilePot) world.getTileEntity(pos);
			nextTile.clearItem();
		}
		return true;
	}

    public static void setState(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;
		world.setBlockState(pos, EnumCook.transitionCook(world.getBlockState(pos), COOK), 3);
		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }


	public EnumCook getCook (IBlockState state) {
		return state.getValue(COOK);
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (!keepInventory) {

			TilePot tile = (TilePot) world.getTileEntity(pos);

			// オフ状態以外ならアイテム取り出し
			if (!this.getCook(state).isOFF()) {
				this.spawnItem(world, pos, tile.inPutList);
				world.spawnEntity(tile.getEntityItem(pos, tile.handItem));
			}

			tile.startFlag = false;
			spawnAsEntity(world, pos, new ItemStack(this));
		}

		if (BlockStove.isStoveOn(world, pos.down())) {
			BlockStove.setState(world, pos.down());
		}
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
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
