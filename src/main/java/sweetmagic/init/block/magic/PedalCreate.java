package sweetmagic.init.block.magic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.pedal.PedalRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class PedalCreate extends BaseMFBlock {

    public PedalCreate(String name) {
		super(name);
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		TilePedalCreate tile = (TilePedalCreate) world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);

		if (!world.isRemote && !tile.isCharge) {

			ItemStack copy = stack.copy();

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;
			PedalRecipeInfo recipeInfo = SweetMagicAPI.getPedalRecipeInfo(stack, pInv);

			// canComplete = Falseの場合レシピ処理をしない
			if (!recipeInfo.canComplete) { return; }

			// 入れるアイテム、完成品はItemStackリストに突っ込む
			RecipeUtil recipeUtil = RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);

			ItemHandlerHelper.insertItemStacked(tile.handInv, copy, false);

			for (ItemStack input : recipeUtil.getInput()) {
				ItemHandlerHelper.insertItemStacked(tile.inputInv, input, false);
			}

			for (ItemStack result : recipeUtil.getResult()) {
				ItemHandlerHelper.insertItemStacked(tile.outPutInv, result, false);
			}

			tile.isCharge = true;
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePedalCreate();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.075, 0, 0.075, 0.925, 0.7, 0.925);
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TilePedalCreate tile = (TilePedalCreate) world.getTileEntity(pos);

		for (ItemStack s : tile.getList()) {
			world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s.copy()));
			s.shrink(s.getCount());
		}

		for (int i = 0; i < 8; i++) {

			ItemStack stack = tile.getoutPutItem(i);
			if (stack.isEmpty()) { continue; }

			stack.shrink(stack.getCount());
		}

		tile.isCharge = false;

		spawnAsEntity(world, pos, new ItemStack(this));
    }
}
