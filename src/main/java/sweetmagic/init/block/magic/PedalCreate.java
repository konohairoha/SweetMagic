package sweetmagic.init.block.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.recipe.pedal.PedalRecipeInfo;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class PedalCreate extends BaseMFBlock {

	private static final String MF = "mf";
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.075D, 0D, 0.075D, 0.925D, 0.7D, 0.925D);

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
			Item copyItem = copy.getItem();

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;
			PedalRecipeInfo recipeInfo = SweetMagicAPI.getPedalRecipeInfo(stack, pInv);

			// canComplete = Falseの場合レシピ処理をしない
			if (!recipeInfo.canComplete) { return; }

			// 入れるアイテム、完成品はItemStackリストに突っ込む
			RecipeUtil recipeUtil = RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);
			copy.setCount(recipeInfo.getHandList().get(0).getCount());
			ItemHandlerHelper.insertItemStacked(tile.handInv, copy, false);
			AdvancedInit.witch_craft.triggerFor(player);

			for (ItemStack input : recipeUtil.getInput()) {
				ItemHandlerHelper.insertItemStacked(tile.inputInv, input, false);
			}

			// NBTを取得
			NBTTagCompound tags = copy.getTagCompound();

			for (ItemStack result : recipeUtil.getResult()) {

				// NBTを保持するなら
				if (recipeInfo.keepTag && copy.hasTagCompound()) {

					// ブロックにMFを持ってたら
					if (tags != null && tags.hasKey(MF) && !(copyItem instanceof IMFTool)) {
						this.blockItemSpawn(world, player, tags, result);
					}

					else if (copyItem instanceof IMFTool || copyItem instanceof IPouch) {
			    		result.setTagCompound(copy.getTagCompound());
					}
				}

				ItemHandlerHelper.insertItemStacked(tile.outPutInv, result, false);
			}

			tile.isCharge = true;
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	// アイテムスポーン
	public void blockItemSpawn (World world, EntityPlayer player, NBTTagCompound tags, ItemStack result) {

		List<ItemStack> stackList = new ArrayList<>();
		stackList.addAll(this.getItemList(tags, "ItemList"));

		int crystalCount = tags.getInteger(MF) / 600;
		if (crystalCount > 0) {
			stackList.add(new ItemStack(ItemInit.aether_crystal, crystalCount));
		}

		for (ItemStack s : stackList) {
			world.spawnEntity(new EntityMagicItem(world, player, s));
		}

		NBTTagCompound nbt = (NBTTagCompound) tags.getTag("BlockEntityTag");
		nbt.removeTag("Input");
		nbt.removeTag(MF);
		this.removeTags(nbt);
		tags.setTag("BlockEntityTag", nbt);
	}

	// 特定のNBTを除去
	public void removeTags (NBTTagCompound tags) {

		if (tags.hasKey("wand")) {
			tags.removeTag("wand");
		}

		if (tags.hasKey("Output")) {
			tags.removeTag("Output");
		}

		if (tags.hasKey("Crystal")) {
			tags.removeTag("Crystal");
		}
	}

	// アイテムリストを取得
	public List<ItemStack> getItemList(NBTTagCompound tags, String name) {

		NBTTagList nbtList = tags.getTagList(name, 10);
		List<ItemStack> list = new ArrayList<ItemStack>();

		for (int i = 0; i < nbtList.tagCount(); ++i) {
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
			list.add(stack);
		}

		tags.removeTag(name);

		return list;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePedalCreate();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		TilePedalCreate tile = (TilePedalCreate) world.getTileEntity(pos);

		for (ItemStack s : tile.getList()) {
			world.spawnEntity(new EntityMagicItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, s.copy()));
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
