package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileToolRepair;

public class ToolRepairBlock extends BaseMFFace {

	public final int data;

    public ToolRepairBlock(String name, int data) {
		super(name);
		this.data = data;
		BlockInit.magicList.add(this);
    }

    /**
     * 0 = ツールリペアラー
     * 1 = マギア・リライト
     */

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }

		int guiId = 0;

		switch (this.data) {
		case 0:
			guiId = SMGuiHandler.TOOLREPAIR;
			break;
		case 1:
			guiId = SMGuiHandler.MAGIAWRITE;
			break;
		}

		player.openGui(SweetMagicCore.INSTANCE, guiId, world, pos.getX(), pos.getY(), pos.getZ());
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:
			return new TileToolRepair();
		case 1:
			return new TileMagiaWrite();
		}
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:
			return new AxisAlignedBB(0.2, 0, 0.2, 0.8, 0.65, 0.8);
		case 1:
			return new AxisAlignedBB(0.075, 0, 0.075, 0.925, 0.875, 0.925);
		}

		return FULL_BLOCK_AABB;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		String tip = "";

		switch (this.data) {
		case 0:
			tip = "tip.tool_repair.name";
			break;
		case 1:
			tip = "tip.magia_rewrite.name";
			break;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + new TextComponentTranslation(tip, new Object[0]).getFormattedText()));
	}
}
