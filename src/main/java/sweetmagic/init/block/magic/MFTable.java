package sweetmagic.init.block.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IWand;
import sweetmagic.api.recipe.mftable.MFTableRecipeInfo;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileMFMMTable;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.util.SMUtil;

public class MFTable extends BaseMFBlock {

	public final int data;

    public MFTable(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return; }

		TileMFTable tile = (TileMFTable)world.getTileEntity(pos);

		// 杖のItemStackを取得
		ItemStack wandStack = tile.getWandItem(0);
		ItemStack copyWand = wandStack.copy();

		// 杖じゃなかったら
		if (wandStack.isEmpty() || !(wandStack.getItem() instanceof IWand)) {
			this.openGui(world, player, pos);
			return;
		}

		// 杖の取得
		IWand wand = (IWand) wandStack.getItem();
		int tier = wand.getTier();

		// クリエワンドなら
		if (wand.isCreativeWand() || tier >= 7) {
			this.openGui(world, player, pos);
			return;
		}

		// レベルの取得
//		int reLevel = wand.getMaxLevel();
//
//		// レベルが満たしていたら
//		if (reLevel <= wand.getLevel(wandStack)) {

		NonNullList<ItemStack> pInv = player.inventory.mainInventory;
		MFTableRecipeInfo recipeInfo = SweetMagicAPI.getMFTableRecipeInfo(wandStack, pInv);

		// canComplete = Falseの場合レシピ処理をしない
		if (recipeInfo == null || !recipeInfo.canComplete || recipeInfo.getOutputItems() == null || recipeInfo.getOutputItems().isEmpty()) {
			this.openGui(world, player, pos);
			return;
		}

		//レシピアイテム情報からアイテムを取得し、減らしたい個数に変換してinputsリストに追加する
		for (Object[] recipe : recipeInfo.getinputs()) {
			Integer invPos = (Integer) recipe[0];
			int shrinkCnt = (int) recipe[2];
			SMUtil.decrPInvMin(player, shrinkCnt, invPos);
		}

		// 杖のNBTを取得
		NBTTagCompound tagOld = wandStack.getTagCompound();

		// 次のワンドを用意してNBTを入れる
		ItemStack newStack = ((ItemStack) recipeInfo.getOutputItems().get(0)).copy();

		if (newStack == null || newStack.isEmpty()) {
			if (!world.isRemote) {

				List<ItemStack> list = new ArrayList<>();
				list.add(copyWand);

				for (Object[] recipe : recipeInfo.getinputs()) {
					ItemStack input = (ItemStack) recipe[1];
					input.setCount((int) recipe[2]);
					list.add(input);
				}

				for (ItemStack s : list) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s));
				}
			}
			return;
		}

		newStack.setTagCompound(tagOld);

		// 杖のNBTを取得
		NBTTagCompound tagNew = wandStack.getTagCompound();

		// スロットの数を増やす
		tagNew.setInteger("slotCount", ((IWand) newStack.getItem()).getSlot());
		wandStack.shrink(1);
		ItemHandlerHelper.insertItemStacked(tile.getWand(), newStack, false);
		tile.markDirty();

		// 変換時の音
		world.playSound(null, pos, SoundEvents.ENTITY_FIREWORK_BLAST_FAR, SoundCategory.VOICE,
				0.5F, 1F / (world.rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);

		this.openGui(world, player, pos);
	}

	// GUIを開く
	public void openGui (World world, EntityPlayer player, BlockPos pos) {

		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.MFTABLE_GUI;
			break;
		case 1:
			guiId = SMGuiHandler.MFTABLE_ADVANCED_GUI;
			break;
		case 2:
			guiId = SMGuiHandler.MMTABLE;
			break;
		}

		this.openGui(world, player, pos, guiId);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0:	return new TileMFTable();
		case 1:	return new TileMFTableAdvanced();
		case 2:	return new TileMFMMTable();
		}

		return null;
	}
}
