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
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileMFAccelerator;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileToolRepair;

public class ToolRepairBlock extends BaseMFFace {

	public final int data;
	public static final AxisAlignedBB TOOL = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.65D, 0.8D);
	public static final AxisAlignedBB WRITE = new AxisAlignedBB(0.075D, 0D, 0.075D, 0.925D, 0.875D, 0.925D);
	public static final AxisAlignedBB SUCCESSOR = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.4375D, 1D);

    public ToolRepairBlock(String name, int data) {
		super(name);
		this.data = data;
		if (data == 4) {
			setLightLevel(1F);
		}
		BlockInit.magicList.add(this);
    }

    /**
     * 0 = ツールリペアラー
     * 1 = マギア・リライト
     * 2 = マギア・サクセサー
     * 3 = アルカナ・テーブル
     * 4 = マギア・アクセラレータ
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
		case 2:
			guiId = SMGuiHandler.SUCCESSOR_GUI;
			break;
		case 3:
			guiId = SMGuiHandler.ARCANETABLE_GUI;
			break;
		case 4:
			TileMFAccelerator tile = (TileMFAccelerator) world.getTileEntity(pos);
			TextComponentTranslation tip = new TextComponentTranslation("tip.mf_amount.name");
			player.sendMessage(tip.appendText(String.format("%,d", tile.getMF())));
			break;
		}

		if (guiId == 0) { return; }
		this.openGui(world, player, pos, guiId);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		switch (this.data) {
		case 0:	return new TileToolRepair();
		case 1:	return new TileMagiaWrite();
		case 2:	return new TileMFSuccessor();
		case 3:	return new TileMFArcaneTable();
		case 4:	return new TileMFAccelerator();
		}
		return null;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:	return TOOL;
		case 1:	return WRITE;
		case 2:	return SUCCESSOR;
		case 3:	return FULL_BLOCK_AABB;
		case 4:	return SUCCESSOR;
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
		case 2:
			tip = "tip.magia_successor.name";
			break;
		case 3:
			tip = "tip.arcane_table.name";
			break;
		case 4:
			tip = "tip.magia_accelerator.name";
			tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.sm_redstone.name")));
			break;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip(tip)));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
