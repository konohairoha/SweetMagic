package sweetmagic.init.item.sm.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IRangePosTool;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.init.tile.inventory.InventoryRobe;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.key.SMKeybind;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.WorldHelper;

public class StarLightWand extends SMItem implements IMFTool, IRangePosTool {

	private int maxMF;

	public final static String REPLACE = "replace";
	public final static String STARTX = "sX";
	public final static String STARTY = "sY";
	public final static String STARTZ = "sZ";
	public final static String ENDX = "eX";
	public final static String ENDY = "eY";
	public final static String ENDZ = "eZ";
	public final static String BLOCKDATA = "block_data";
	private final static String METADATA = "metadata";

	public StarLightWand (String name) {
		super(name, ItemInit.magicList);
		this.setMaxMF(100000);
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// 破壊無効がついていたら設置できない
		ItemStack stack = player.getHeldItem(hand);
		if (player.isPotionActive(PotionInit.breakblock)) { return new ActionResult(EnumActionResult.PASS, stack); }

		NBTTagCompound tags = ItemHelper.getNBT(stack);

		if (tags.hasKey(STARTX)) {

			BlockPos startPos = this.getStartPos(tags);
			BlockPos endPos = null;
			Block block = this.loadTagToBlock(tags);
			if (block == null) { return new ActionResult(EnumActionResult.PASS, stack); }

			if (tags.hasKey(ENDX)) {
				endPos = this.getEndPos(tags);
			}

			boolean isCreative = player.isCreative();
			boolean isReplace = tags.getBoolean(REPLACE);
			IBlockState state = block.getStateFromMeta(tags.getInteger(METADATA));

			// プレイヤーのInventoryの取得
			NonNullList<ItemStack> pInv = player.inventory.mainInventory;
			int mf = this.getMF(stack);
			int useMF = 0;
			int costMF = 10 - Math.min(9, this.getEnchantLevel(EnchantInit.mfCostDown, stack));
			if (!isCreative && mf <= costMF) { return new ActionResult(EnumActionResult.PASS, stack); }

    		// リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();
			ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
			boolean isRobe = chest.getItem() instanceof IRobe;

			// インベントリを取得
			InventoryRobe inv = isRobe ? new InventoryRobe(player) : null;
			IItemHandlerModifiable robeInv = isRobe ? inv.inventory : null;

			// 座標分回す
			for (BlockPos pos : BlockPos.getAllInBox(startPos, endPos != null ? endPos : startPos)) {

				// ブロックが一緒か岩盤なら次へ
				IBlockState blockState = world.getBlockState(pos);
				Block b = blockState.getBlock();
				if (b == block || block == Blocks.BEDROCK || (!isReplace && b != Blocks.AIR) ) { continue; }

				// ブロックが見つかったかフラグ
				boolean isFound = false;

				// クリエなら
				if (isCreative) {
					world.setBlockState(pos, state, 3);
					continue;
				}

				// クリエ以外なら
				else {

					// インベントリ分回す
					for (ItemStack blockStack : pInv) {

						// アイテムが空かブロック以外なら次へ
						if (blockStack.isEmpty() || !(blockStack.getItem() instanceof ItemBlock)) { continue; }

						// 設置ブロック以外なら終了

						IBlockState setBlock = ((ItemBlock) blockStack.getItem()).getBlock().getStateFromMeta(blockStack.getMetadata());
						if (setBlock != state) { continue; }

						useMF += costMF;
						this.setBlock(world, player, blockStack, inv, b, blockState, state, pos, drop, isReplace);
						isFound = true;
						break;
					}

					// ブロックが見つからずローブを来てるなら
					if (!isFound && isRobe) {

						for (int i = 0; i < robeInv.getSlots(); i++) {

							// アイテムが空かブロック以外なら次へ
							ItemStack blockStack = robeInv.getStackInSlot(i);
							if (blockStack.isEmpty() || !(blockStack.getItem() instanceof ItemBlock)) { continue; }

							// 設置ブロック以外なら終了
							IBlockState setBlock = ((ItemBlock) blockStack.getItem()).getBlock().getStateFromMeta(blockStack.getMetadata());
							if (setBlock != state) { continue; }

							useMF += costMF;
							inv.writeBack();
							this.setBlock(world, player, blockStack, inv, b, blockState, state, pos, drop, isReplace);
							isFound = true;
							break;
						}
					}

					// ブロックが見つかんないなら終了
					if (!isFound) {
						this.setMF(stack, mf - useMF);

						// リストに入れたアイテムをドロップさせる
						if (!drop.isEmpty() && !world.isRemote) {
							WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
						}

						return new ActionResult(EnumActionResult.SUCCESS, stack);
					}

					// 消費MFより多くなったら終了
					if (mf <= useMF) { break; }
				}
			}

			// ブロック設置して終了
			this.setMF(stack, mf - useMF);

			// リストに入れたアイテムをドロップさせる
			if (!drop.isEmpty() && !world.isRemote) {
				WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			}

	        SoundType sound = block.getSoundType(state, world, startPos, player);
			world.playSound(null, player.getPosition(), sound.getPlaceSound(), SoundCategory.NEUTRAL, (sound.getVolume() + 1F) * 0.5F, sound.getPitch() * 0.8F);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	public void setBlock (World world, EntityPlayer player, ItemStack stack, InventoryRobe inv, Block block, IBlockState state, IBlockState setState, BlockPos pos, List<ItemStack> drop, boolean isReplace) {

		stack.shrink(1);

		// 置換なら設置前のブロックをドロップ
		if (isReplace) {
			drop.addAll(WorldHelper.getBlockDrops(world, player, state, block, pos, false, 0));
		}

		world.setBlockState(pos, setState, 3);
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		// スニークしてないなら終了
        if (!player.isSneaking()) { return EnumActionResult.PASS; }

		NBTTagCompound tags = ItemHelper.getNBT(stack);
    	StarLightWand wand = (StarLightWand) stack.getItem();
    	wand.saveBlockToTag(tags, world.getBlockState(pos));
    	player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 0.5F, 1F);
		return EnumActionResult.SUCCESS;
	}

	// ブロックの保存
	public void saveBlockToTag (NBTTagCompound tags, IBlockState state) {

		// nbtのリストを作成
		Block block = state.getBlock();
		NBTTagList nbtList = new NBTTagList();
        ItemStack stack = new ItemStack(block);
        nbtList.appendTag(stack.writeToNBT(new NBTTagCompound()));
        tags.setTag(BLOCKDATA, nbtList);
        tags.setInteger(METADATA, block.getMetaFromState(state));
	}

	// ブロックの書き出し
	public Block loadTagToBlock (NBTTagCompound tags) {

		NBTTagList nbtList = tags.getTagList(BLOCKDATA, 10);

		for (int i = 0; i < nbtList.tagCount(); ++i) {

			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
			Item item = stack.getItem();

			if (item instanceof ItemBlock) {
				return ((ItemBlock) item).getBlock();
			}
		}

		return null;
	}

	// リプレースモード切替
	public void repaceMode (ItemStack stack) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);

		if (tags.hasKey(REPLACE)) {
			tags.setBoolean(REPLACE, !tags.getBoolean(REPLACE));
		}

		else {
			tags.setBoolean(REPLACE, true);
		}
	}

  	// 最大MFを取得
	@Override
  	public int getMaxMF (ItemStack stack) {
		float addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 7.5F) * (this.maxMF / 100F);
  		return (int) (this.maxMF + addMaxMF);
  	}

	// エンチャレベル取得
	public int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

	@Override
	public void setMaxMF(int maxMF) {
		this.maxMF = maxMF;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getMF(stack) != this.getMaxMF(stack);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ( (double) this.getMF(stack) / (double) this.getMaxMF(stack) );
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.starlightwand_left.name")));
		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.starlightwand_right.name")));
		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.starlightwand_set.name")));

		// シフトを押したとき
		if (Keyboard.isKeyDown(42)) {
			NBTTagCompound tags = ItemHelper.getNBT(stack);
			BlockPos startPos = this.getStartPos(tags);
			BlockPos endPos = this.getEndPos(tags);
			String start = tags.hasKey(STARTX) ? startPos.getX() + ", " + startPos.getY() + ", " + startPos.getZ() : this.getTip(TextFormatting.RED, "tip.starlightwand_empty.name");
			String end = tags.hasKey(ENDX) ? endPos.getX() + ", " + endPos.getY() + ", " + endPos.getZ() : this.getTip(TextFormatting.RED, "tip.starlightwand_empty.name");
			String mode = tags.getBoolean(REPLACE) ? this.getTip(TextFormatting.GOLD, "tip.starlightwand_replace.name") : this.getTip(TextFormatting.GOLD, "tip.starlightwand_inst.name");

			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.sl_reset.name") + TextFormatting.GOLD + ClientKeyHelper.getKeyName(SMKeybind.NEXT)));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.sl_repelace.name") + TextFormatting.GOLD + ClientKeyHelper.getKeyName(SMKeybind.BACK)));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_start.name") + start));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_end.name") + end));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_mode.name") + mode));

			Block block = this.loadTagToBlock(tags);
			if (block == null) { return; }

			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_block.name") + this.getTip(TextFormatting.GOLD, block.getUnlocalizedName() + ".name")));
		}

		else {
			tooltip.add(I18n.format(this.getTip(TextFormatting.RED, "tip.shift.name")));
		}

	}

	public String getTip (TextFormatting type, String tip) {
		return type + new TextComponentTranslation(tip).getFormattedText();
	}
}
