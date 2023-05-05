package sweetmagic.init.block.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.recipe.pedal.PedalRecipeInfo;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.item.sm.armor.MagiciansPouch;
import sweetmagic.init.tile.magic.TileAltarCreat;
import sweetmagic.init.tile.magic.TileAltarCreationStar;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.packet.PedalCreatePKT;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class PedalCreate extends BaseMFBlock {

	private final int data;
	private static final String MF = "mf";
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.075D, 0D, 0.075D, 0.925D, 0.7D, 0.925D);
	private static final AxisAlignedBB ALTAR = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.925D, 1D);
	private static final AxisAlignedBB STAR = new AxisAlignedBB(0D, 0D, 0D, 1D, 1.25D, 1D);

    public PedalCreate(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
		BlockInit.pedalList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		TilePedalCreate tile = (TilePedalCreate) world.getTileEntity(pos);
		IBlockState state = world.getBlockState(pos);

		if (!tile.isCharge) {

			if (!world.isRemote) {

				if (!tile.isHaveBlock) {

					List<ITextComponent> textList = new ArrayList<>();

					if (!tile.isCrystal) {

						textList.add(new TextComponentTranslation("tip.pedal_not_block.name"));

						TextComponentTranslation crystal = new TextComponentTranslation(new ItemStack(tile.getCrystal()).getUnlocalizedName() + ".name");
						TextComponentTranslation text = new TextComponentTranslation("tip.pedal_need_block.name");
						textList.add(text.appendSibling(crystal));
					}

					else {


						textList.add(new TextComponentTranslation("tip.pedal_not_encha.name"));

						String enchaPower = (this.data == 1 ? 15 : 35) + "";
						TextComponentTranslation text = new TextComponentTranslation("tip.pedal_need_encha.name");
						textList.add(text.appendText(enchaPower));
					}


					for (ITextComponent tip : textList) {
						player.sendMessage(tip);
					}

					return;
				}

				if (stack.isEmpty()) {
					TextComponentTranslation tip = new TextComponentTranslation("tip.mf_amount.name");
					player.sendMessage(tip.appendText(String.format("%,d", tile.getMF())));
					return;
				}

				ItemStack copy = stack.copy();
				Item copyItem = copy.getItem();

				// プレイヤーのInventoryの取得
				NonNullList<ItemStack> pInv = player.inventory.mainInventory;
				PedalRecipeInfo recipeInfo = SweetMagicAPI.getPedalRecipeInfo(stack, pInv);
				boolean canComPlete = recipeInfo.canComplete;

				// canComplete = Falseの場合またはMFが他入りない場合はレシピ処理をしない
				if (!canComPlete || recipeInfo.needMF > tile.getMF()) {
					String text = !canComPlete ? "tip.pedal_not_item.name" : "tip.pedal_not_mf.name";
					TextComponentTranslation tip = new TextComponentTranslation(text);
					player.sendMessage(tip.appendText(!canComPlete ? "" : "： " + String.format("%,d", recipeInfo.needMF)));
					return;
				}

				// 入れるアイテム、完成品はItemStackリストに突っ込む
				RecipeUtil recipeUtil = RecipeHelper.recipeSingleCraft(recipeInfo, player, stack);
				copy.setCount(recipeInfo.getHandList().get(0).getCount());
				ItemHandlerHelper.insertItemStacked(tile.handInv, copy, false);
				AdvancedInit.witch_craft.triggerFor(player);

				// NBTを取得
				NBTTagCompound bookTag = null;

				for (ItemStack input : recipeUtil.getInput()) {

					if (input.getItem() == ItemInit.magic_book_cosmic && copy.hasTagCompound()) {
						bookTag = input.getTagCompound();
					}

					ItemHandlerHelper.insertItemStacked(tile.inputInv, input, false);
				}

				// NBTを取得
				NBTTagCompound tags = copy.getTagCompound();

				for (ItemStack result : recipeUtil.getResult()) {

					// 魔術書なら
					if (result.getItem() == ItemInit.magic_book_cosmic && bookTag != null) {
						result.setTagCompound(bookTag);
					}

					// NBTを保持するなら
					else if ( recipeInfo.keepTag && copy.hasTagCompound()) {

						// ブロックにMFを持ってたら
						if (tags != null && tags.hasKey(MF) && !(copyItem instanceof IMFTool)) {
							this.blockItemSpawn(world, player, tags, result);
						}

						else if (copyItem instanceof IMFTool || copyItem instanceof IPouch) {
				    		result.setTagCompound(copy.getTagCompound());
						}

						else {
				    		result.setTagCompound(copy.getTagCompound());
						}
					}

					ItemHandlerHelper.insertItemStacked(tile.outPutInv, result, false);
				}

				tile.setMF(tile.getMF() - recipeInfo.needMF);
			}

			else {

				int time = MagiciansPouch.hasAcce(player, ItemInit.witch_scroll) ? 5 : 10;
				tile.needChargeTime = time;
				PacketHandler.sendToServer(new PedalCreatePKT(time, pos));
			}

			tile.isCharge = true;
			tile.markDirty();
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TilePedalCreate tile = (TilePedalCreate) world.getTileEntity(pos);
		tile.checkBlock();
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

		switch(this.data) {
		case 0: return new TilePedalCreate();
		case 1: return new TileAltarCreat();
		case 2: return new TileAltarCreationStar();
		}
		return new TilePedalCreate();
	}

	@Override
	public int getMaxMF() {
		switch (this.data) {
		case 0:	  return 20000;
		case 1:	  return 200000;
		case 2:	  return 5000000;
		}
		return super.getMaxMF();
	}

	@Override
	public int getTier() {
		return this.data + 1;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0: return AABB;
		case 1: return ALTAR;
		case 2: return STAR;
		}

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

		tile.nowTick = 0;
		tile.isCharge = false;
		super.breakBlock(world, pos, state);
    }
}
