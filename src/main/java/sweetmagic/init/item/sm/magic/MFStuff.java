package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class MFStuff extends SMItem {

	public int magiaflux;

	public MFStuff (String name) {
		super(name);
	}

	// ブロックを右クリック
	public EnumActionResult useStack (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (player.isSneaking()) { return EnumActionResult.PASS; }

		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null || !tags.hasKey("X")) { return EnumActionResult.PASS; }

		if (!world.isRemote) {
			String tip = new TextComponentTranslation("tip.posremo.name", new Object[0]).getFormattedText();
			player.sendMessage(new TextComponentString(tip));
		} else {
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1, 1);
		}

		tags.removeTag("X");
		tags.removeTag("Y");
		tags.removeTag("Z");
		return EnumActionResult.SUCCESS;
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		String click = new TextComponentTranslation("tip.mfstuff.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.RED + click));

		NBTTagCompound tags = stack.getTagCompound();

		if (tags != null && tags.hasKey("X")) {
			String pos = new TextComponentTranslation("tip.pos.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GREEN + pos + " : " + tags.getInteger("X") + ", " + tags.getInteger("Y") + ", " + tags.getInteger("Z")));
		}
	}
}
