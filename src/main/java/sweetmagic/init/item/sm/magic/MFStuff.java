package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.item.sm.sweetmagic.SMItem;

public class MFStuff extends SMItem {

	public MFStuff (String name) {
		super(name);
	}

	// ブロックを右クリック
	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		System.out.println("==" + world.getActualHeight());
		System.out.println("==" + world.getTopSolidOrLiquidBlock(new BlockPos(pos.getX(), 0, pos.getY())));
		if (player.isSneaking()) { return EnumActionResult.PASS; }

		NBTTagCompound tags = stack.getTagCompound();
		if (tags == null || !tags.hasKey("X")) { return EnumActionResult.PASS; }

		if (!world.isRemote) {
			player.sendMessage(new TextComponentTranslation("tip.posremo.name", new Object[0]));
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

		NBTTagCompound tags = stack.getTagCompound();
		String click = new TextComponentTranslation("tip.mfstuff.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.RED + click));

		if (tags != null && tags.hasKey("X")) {

			String pos = new TextComponentTranslation("tip.pos.name", new Object[0]).getFormattedText();
			int x = tags.getInteger("X");
			int y = tags.getInteger("Y");
			int z = tags.getInteger("Z");
			tooltip.add(I18n.format(TextFormatting.GREEN + pos + " : " + x + ", " + y + ", " + z));

			Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
			String blockTip = new TextComponentTranslation(block.getUnlocalizedName() + ".name", new Object[0]).getFormattedText();
			String tip = new TextComponentTranslation("tip.registerblock.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GREEN + tip + " ： " + blockTip));
		}
	}
}
