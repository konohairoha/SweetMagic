package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.TeleportUtil;

public class MFTeleport extends MFSlotItem implements IElementItem {


	public MFTeleport (String name, boolean isShrink) {
		super(name, SMType.AIR, SMElement.TIME, 1, 40, 10, isShrink);
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {
		toolTip.add("tip.magic_teleport.name");
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		IWand wand = IWand.getWand(stack);

		if (wand.isCreativeWand()) {
			TeleportUtil.teleportToDimension(player, 1222, new BlockPos(player));
			return true;
		}

		// 選択中のアイテムを取得
		ItemStack slotStack= wand.getSlotItem(player, stack, wand.getNBT(stack));
		NBTTagCompound tags = slotStack.getTagCompound();

		// テレポート後のディメンションが違うなら
		if (tags.hasKey("dim") && player.dimension != tags.getInteger("dim")) {

			int dim = tags.getInteger("dim");
            BlockPos pos = new BlockPos(tags.getInteger("posX") + 0.5, tags.getInteger("posY") + 1, tags.getInteger("posZ") + 0.5);
			TeleportUtil.teleportToDimension(player, dim, pos);
		}

		else {
			player.setPositionAndUpdate(tags.getInteger("posX") + 0.5F, tags.getInteger("posY") + 1F, tags.getInteger("posZ") + 0.5F);
		}

		player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);

		return true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tags = stack.getTagCompound();

		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		tags.setInteger("posX", (int) player.posX);
		tags.setInteger("posY", (int) player.posY);
		tags.setInteger("posZ", (int) player.posZ);
		tags.setInteger("dim", world.provider.getDimension());


		if (!world.isRemote) {
			player.sendMessage(new TextComponentString(this.getTip("tip.posregi.name")));
		}

		else {
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 選択中のアイテムを取得
		IWand wand = IWand.getWand(stack);
		ItemStack slotStack= wand.getSlotItem(player, stack, wand.getNBT(stack));
		NBTTagCompound nbt = slotStack.getTagCompound();

		return nbt != null && nbt.hasKey("posX");
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {

		NBTTagCompound tags = stack.getTagCompound();
		if (tags != null && tags.hasKey("posX")) {
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.pos.name") + " : " + tags.getInteger("posX") + ", " + tags.getInteger("posY") + ", " + tags.getInteger("posZ")));
		}
	}
}
