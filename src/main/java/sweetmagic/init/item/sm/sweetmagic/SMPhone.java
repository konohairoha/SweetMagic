package sweetmagic.init.item.sm.sweetmagic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.blocks.BlockNotePC;
import sweetmagic.init.tile.chest.TileNotePC;
import sweetmagic.util.ItemHelper;

public class SMPhone extends SMItem {

	private static final String ACTIVE = "Active";
	private static final String PCX = "pcX";
	private static final String PCY = "pcY";
	private static final String PCZ = "pcZ";
	protected static final ResourceLocation NAME = new ResourceLocation(SweetMagicCore.MODID, "active");
	private static final IItemPropertyGetter GETTER = (stack, world, entity) -> stack.hasTagCompound() && stack.getTagCompound().getBoolean(ACTIVE) ? 1F : 0F;

    public SMPhone(String name) {
		super(name, ItemInit.furniList);
        setMaxStackSize(1);
        this.addPropertyOverride(NAME, GETTER);
    }

	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		if (tags.getBoolean(ACTIVE)) {

			BlockPos pos = new BlockPos(tags.getInteger(PCX), tags.getInteger(PCY), tags.getInteger(PCZ));
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			if (block instanceof BlockNotePC) {
				block.onBlockActivated(world, pos, state, player, hand, EnumFacing.NORTH, 0F, 0F, 0F);
			}

			else {
				tags.removeTag(PCX);
				tags.removeTag(PCY);
				tags.removeTag(PCZ);
				tags.removeTag(ACTIVE);
			}

			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult(EnumActionResult.PASS, stack);
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		TileEntity tile = world.getTileEntity(pos);

		if (tile != null && tile instanceof TileNotePC) {
			NBTTagCompound tags = ItemHelper.getNBT(stack);
			tags.setInteger(PCX, pos.getX());
			tags.setInteger(PCY, pos.getY());
			tags.setInteger(PCZ, pos.getZ());
			tags.setBoolean(ACTIVE, true);
			return EnumActionResult.SUCCESS;
		}

		return EnumActionResult.PASS;
	}

    //ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		List<String> textLine = Arrays.<String>asList(this.getTip("tip.sm_phone.name").split("<br>"));
  		for (String tip : textLine) {
  	  		tooltip.add(I18n.format(TextFormatting.GOLD + tip));
  		}

		NBTTagCompound tags = stack.getTagCompound();

		if (tags != null && tags.getBoolean(ACTIVE)) {

			tooltip.add("");

			int x = tags.getInteger(PCX);
			int y = tags.getInteger(PCY);
			int z = tags.getInteger(PCZ);
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.pos.name") + " : " + x + ", " + y + ", " + z));

			String block = (new ItemStack(world.getBlockState(new BlockPos(x, y, z)).getBlock())).getDisplayName();
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.registerblock.name") + " ： " + block));
		}
  	}
}
