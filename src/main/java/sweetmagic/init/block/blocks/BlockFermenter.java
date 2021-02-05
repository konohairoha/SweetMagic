package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.fermenter.FermenterRecipeInfo;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileFermenter;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockFermenter extends BaseFaceBlock {

	public BlockFermenter(String name, List<Block> list) {
		super(Material.GLASS, name);
		setHardness(1.0F);
		setResistance(16F);
		setSoundType(SoundType.GLASS);
		disableStats();
		list.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.8D, 0.8D, 0.8D, 0.2D, 0D, 0.2D);
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileFermenter tile = (TileFermenter) world.getTileEntity(pos);

		// 稼働中なら
		if (!tile.isWorking && !tile.isFinish) {

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;

			// 手持ちアイテムからレシピと一致するかを検索
			FermenterRecipeInfo recipeInfo = SweetMagicAPI.getFermenterRecipeInfo(stack, pInv);

			// 入れるアイテム、完成品はItemStackリストに突っ込む
			ItemStack handitem = recipeInfo.getHandItem();
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();

			// クラフト失敗
			if (!recipeInfo.canComplete) { return false; }

			// クラフト処理
			RecipeUtil recipeUtil = RecipeHelper.recipeAllCraft(recipeInfo, player, stack);
			handitem = recipeUtil.getHand();
			inputs.addAll(recipeUtil.getInput());
			results.addAll(recipeUtil.getResult());

			tile.handItem = handitem;
			tile.inPutList = inputs;
			tile.outPutList = results;
			tile.isWorking = true;
			tile.tickTime = 0;

			this.playerSound(world, pos, SoundEvents.ENTITY_ITEM_PICKUP, 0.5F, 1F);

			tile.markDirty();
            world.notifyBlockUpdate(pos, state, state, 3);
		}

		// 終わってるなら
		else if (!tile.isWorking && tile.isFinish) {

			// 結果アイテムのドロップ
			this.spawnItem(world, player, tile.outPutList);

			// 初期化
			tile.clear();
			tile.markDirty();
            world.notifyBlockUpdate(pos, state, state, 3);
		}

		return true;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFermenter();
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

    	TileFermenter tile = (TileFermenter) world.getTileEntity(pos);

		// 結果アイテムのドロップ
		spawnAsEntity(world, pos, tile.handItem);
		this.spawnItem(world, pos, tile.inPutList);
		spawnAsEntity(world, pos, new ItemStack(this));
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
