package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
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
import sweetmagic.init.tile.magic.TileAetherEnchantTable;
import sweetmagic.init.tile.magic.TileMFAccelerator;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMagiaReIncarnation;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileToolRepair;
import sweetmagic.init.tile.magic.TileWorkbenchStorage;

public class ToolRepairBlock extends BaseMFFace {

	public final int data;
	private static final AxisAlignedBB TOOL = new AxisAlignedBB(0.2D, 0D, 0.2D, 0.8D, 0.65D, 0.8D);
	private static final AxisAlignedBB WRITE = new AxisAlignedBB(0.075D, 0D, 0.075D, 0.925D, 0.875D, 0.925D);
	private static final AxisAlignedBB SUCCESSOR = new AxisAlignedBB(0D, 0D, 0D, 1D, 0.4375D, 1D);
	private static final AxisAlignedBB ENCHANT = new AxisAlignedBB(0.1875D, 0D, 0.1875D, 0.8125D, 1.4375D, 0.8125D);
	private static final AxisAlignedBB REINCAHRM = new AxisAlignedBB(0.0625D, 0D, 0.0625D, 0.9375D, 1D, 0.9375D);

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
     * 5 = 収納付き作業台
     * 6 = エーテルエンチャントテーブル
     * 7 = マギア・リーンカネーション
     * 8 = MFハーヴェスター
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
		case 7:
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
		case 5:
			guiId = SMGuiHandler.WORDKBENCH_GUI;
			break;
		case 6:
			guiId = SMGuiHandler.ENCHATABLE_GUI;
			break;
		case 8:
			guiId = SMGuiHandler.MFHARVESTER_GUI;
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
		case 5:	return new TileWorkbenchStorage();
		case 6:	return new TileAetherEnchantTable();
		case 7:	return new TileMagiaReIncarnation();
		case 8:	return new TileMFHarvester();
		}
		return null;
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (this.data == 0) {
			TileToolRepair tile = (TileToolRepair) world.getTileEntity(pos);
			tile.checkRangePlayer();
		}
	}

	@Override
	public int getMaxMF() {
		switch (this.data) {
		case 0:	  return 500000;
		case 1:	  return 600000;
		case 2:	  return 4000000;
		case 3:	  return 4000000;
		case 4:	  return 100000;
		case 5:	  return 500000;
		case 6:	  return 100000;
		case 7:	  return 25000000;
		case 8:	  return 500000;
		}
		return super.getMaxMF();
	}

	@Override
	public int getTier() {
		switch (this.data) {
		case 0:	  return 2;
		case 1:	  return 3;
		case 2:	  return 3;
		case 3:	  return 3;
		case 4:	  return 3;
		case 5:	  return 2;
		case 6:	  return 1;
		case 7:	  return 3;
		case 8:	  return 3;
		}
		return super.getMaxMF();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {

		switch (this.data) {
		case 0:	return TOOL;
		case 1:	return WRITE;
		case 2:	return SUCCESSOR;
		case 3:	return FULL_BLOCK_AABB;
		case 4:	return SUCCESSOR;
		case 6:	return ENCHANT;
		case 7:	return REINCAHRM;
		}

		return FULL_BLOCK_AABB;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {

		if (this.data == 5 || this.data == 6 || this.data == 8) {
	        return BlockFaceShape.SOLID;
		}

        return BlockFaceShape.UNDEFINED;
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
			break;
		case 5:
			tip = "tip.workbench_storage.name";
			break;
		case 6:
			tip = "tip.aether_enchanttable.name";
			break;
		case 7:
			tip = "tip.magia_reincarnation.name";
			break;
		case 8:
			tip = "tip.mfharvester.name";
			break;
		}

		if (this.data == 0 || this.data == 4 || this.data == 5 || this.data == 6 || this.data == 8) {
			tooltip.add(I18n.format(TextFormatting.RED + this.getTip("tip.sm_redstone.name")));
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip(tip)));
		tooltip.add(I18n.format(""));
		super.addInformation(stack, world, tooltip, advanced);
	}
}
