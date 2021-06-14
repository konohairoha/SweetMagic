package sweetmagic.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import sweetmagic.init.tile.chest.TileGravityChest;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.container.BookContainer;
import sweetmagic.init.tile.container.ContainerAetherFurnace;
import sweetmagic.init.tile.container.ContainerAetherHopper;
import sweetmagic.init.tile.container.ContainerFreezer;
import sweetmagic.init.tile.container.ContainerGravityChest;
import sweetmagic.init.tile.container.ContainerJuiceMaker;
import sweetmagic.init.tile.container.ContainerKichenChest;
import sweetmagic.init.tile.container.ContainerMFArcaneTable;
import sweetmagic.init.tile.container.ContainerMFChanger;
import sweetmagic.init.tile.container.ContainerMFFisher;
import sweetmagic.init.tile.container.ContainerMFFurnace;
import sweetmagic.init.tile.container.ContainerMFSuccessor;
import sweetmagic.init.tile.container.ContainerMFTable;
import sweetmagic.init.tile.container.ContainerMFTank;
import sweetmagic.init.tile.container.ContainerMagiaWrite;
import sweetmagic.init.tile.container.ContainerModenRack;
import sweetmagic.init.tile.container.ContainerParallelInterfere;
import sweetmagic.init.tile.container.ContainerPouch;
import sweetmagic.init.tile.container.ContainerRattanBasket;
import sweetmagic.init.tile.container.ContainerRobe;
import sweetmagic.init.tile.container.ContainerSMWand;
import sweetmagic.init.tile.container.ContainerStove;
import sweetmagic.init.tile.container.ContainerToolRepair;
import sweetmagic.init.tile.container.ContainerWoodChest;
import sweetmagic.init.tile.cook.TileFreezer;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.gui.GuiAetherFurnace;
import sweetmagic.init.tile.gui.GuiAetherHopper;
import sweetmagic.init.tile.gui.GuiBook;
import sweetmagic.init.tile.gui.GuiFreezer;
import sweetmagic.init.tile.gui.GuiGravityChest;
import sweetmagic.init.tile.gui.GuiGuidBook;
import sweetmagic.init.tile.gui.GuiJuiceMaker;
import sweetmagic.init.tile.gui.GuiKichenChest;
import sweetmagic.init.tile.gui.GuiMFArcaneTable;
import sweetmagic.init.tile.gui.GuiMFChanger;
import sweetmagic.init.tile.gui.GuiMFF;
import sweetmagic.init.tile.gui.GuiMFFisher;
import sweetmagic.init.tile.gui.GuiMFSuccessor;
import sweetmagic.init.tile.gui.GuiMFTable;
import sweetmagic.init.tile.gui.GuiMFTank;
import sweetmagic.init.tile.gui.GuiMagiaWrite;
import sweetmagic.init.tile.gui.GuiModenRack;
import sweetmagic.init.tile.gui.GuiParallelInterfere;
import sweetmagic.init.tile.gui.GuiPouch;
import sweetmagic.init.tile.gui.GuiRattanBasket;
import sweetmagic.init.tile.gui.GuiRobe;
import sweetmagic.init.tile.gui.GuiSMWand;
import sweetmagic.init.tile.gui.GuiStove;
import sweetmagic.init.tile.gui.GuiToolRepair;
import sweetmagic.init.tile.gui.GuiWoodChest;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.inventory.InventoryRobe;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.magic.TileAetherHopper;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFChangerAdvanced;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFFurnace;
import sweetmagic.init.tile.magic.TileMFFurnaceAdvanced;
import sweetmagic.init.tile.magic.TileMFMMTable;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TileStardustWish;
import sweetmagic.init.tile.magic.TileToolRepair;

public class SMGuiHandler implements IGuiHandler {

	public static final int SMBOOK_GUI = 1;
	public static final int MFF_GUI = 2;
	public static final int MFTANK_GUI = 3;
	public static final int MFFISHER_GUI = 4;
	public static final int STOVE_GUI = 5;
	public static final int SMWAND_GUI = 6;
	public static final int MFTABLE_GUI = 7;
	public static final int MAKER_GUI = 8;
	public static final int MFCHANGER_GUI = 9;
	public static final int FREEZER_GUI = 10;
	public static final int AETHER_GUI = 11;
	public static final int MFROBE_GUI = 12;
	public static final int PARALLELINTERFERE_GUI = 13;
	public static final int MFTABLE_ADVANCED_GUI = 14;
	public static final int MFCHANGER_ADVANCED_GUI = 15;
	public static final int MODENRACK_GUI = 16;
	public static final int MFPOUCH_GUI = 17;
	public static final int GUIDBOOK = 18;
	public static final int RATTAMBASKET = 19;
	public static final int WOODCHEST = 20;
	public static final int TOOLREPAIR = 21;
	public static final int GRAVITYCHEST = 22;
	public static final int MAGIAWRITE = 23;
	public static final int AETHERHOPPER = 24;
	public static final int MMTABLE = 25;
	public static final int STARDUSTWISH = 26;
	public static final int MFF_ADVANCED_GUI = 27;
	public static final int KICHEN_CHEST_GUI = 28;
	public static final int SUCCESSOR_GUI = 29;
	public static final int ARCANETABLE_GUI = 30;

	///サーバ側の処理
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		EnumHand hand = EnumHand.MAIN_HAND;
		InventoryPlayer inv = player.inventory;

		//IDで判断する
		switch (ID) {
		case SMBOOK_GUI:
			return new BookContainer(inv);
		case MFF_GUI:
			return new ContainerMFFurnace(inv, (TileMFFurnace) tile);
		case MFTANK_GUI:
			return new ContainerMFTank(inv, (TileMFTank) tile);
		case MFFISHER_GUI:
			return new ContainerMFFisher(inv, (TileMFFisher) tile);
		case STOVE_GUI:
			return new ContainerStove(inv, (TileStove) tile);
		case SMWAND_GUI:
			return new ContainerSMWand(inv, new InventorySMWand(player.getHeldItem(hand), player));
		case MFTABLE_GUI:
			return new ContainerMFTable(inv, (TileMFTable) tile);
		case MAKER_GUI:
			return new ContainerJuiceMaker(inv, (TileJuiceMaker) tile);
		case MFCHANGER_GUI:
			return new ContainerMFChanger(inv, (TileMFChanger) tile);
		case FREEZER_GUI:
			return new ContainerFreezer(inv, (TileFreezer) tile);
		case AETHER_GUI:
			return new ContainerAetherFurnace(inv, (TileAetherFurnace) tile);
		case MFROBE_GUI:
			return new ContainerRobe(inv, new InventoryRobe(player));
		case MFPOUCH_GUI:
			return new ContainerPouch(inv, new InventoryPouch(player));
		case PARALLELINTERFERE_GUI:
			return new ContainerParallelInterfere(inv, (TileParallelInterfere) tile);
		case MFTABLE_ADVANCED_GUI:
			return new ContainerMFTable(inv, (TileMFTableAdvanced) tile);
		case MFCHANGER_ADVANCED_GUI:
			return new ContainerMFChanger(inv, (TileMFChangerAdvanced) tile);
		case MODENRACK_GUI:
			return new ContainerModenRack(inv, (TileModenRack) tile);
		case RATTAMBASKET:
			return new ContainerRattanBasket(inv, (TileRattanBasket) tile);
		case WOODCHEST:
			return new ContainerWoodChest(inv, (TileWoodChest) tile);
		case TOOLREPAIR:
			return new ContainerToolRepair(inv, (TileToolRepair) tile);
		case GRAVITYCHEST:
			return new ContainerGravityChest(inv, (TileGravityChest) tile);
		case MAGIAWRITE:
			return new ContainerMagiaWrite(inv, (TileMagiaWrite) tile);
		case AETHERHOPPER:
			return new ContainerAetherHopper(inv, (TileAetherHopper) tile);
		case MMTABLE:
			return new ContainerMFTable(inv, (TileMFMMTable) tile);
		case STARDUSTWISH:
			return new ContainerParallelInterfere(inv, (TileStardustWish) tile);
		case MFF_ADVANCED_GUI:
			return new ContainerMFFurnace(inv, (TileMFFurnaceAdvanced) tile);
		case KICHEN_CHEST_GUI:
			return new ContainerKichenChest(inv, (TileWoodChest) tile);
		case SUCCESSOR_GUI:
			return new ContainerMFSuccessor(inv, (TileMFSuccessor) tile);
		case ARCANETABLE_GUI:
			return new ContainerMFArcaneTable(inv, (TileMFArcaneTable) tile);
		}

		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

		BlockPos pos = new BlockPos(x, y, z);
		if (!world.isBlockLoaded(pos)) { return null; }

		TileEntity tile = world.getTileEntity(pos);
		EnumHand hand = EnumHand.MAIN_HAND;
		InventoryPlayer inv = player.inventory;

		//IDで判断する
		switch (ID) {
		case SMBOOK_GUI:
			return new GuiBook(inv);
		case MFF_GUI:
			return new GuiMFF(inv, (TileMFFurnace) tile);
		case MFTANK_GUI:
			return new GuiMFTank(inv, (TileMFTank) tile);
		case MFFISHER_GUI:
			return new GuiMFFisher(inv, (TileMFFisher) tile);
		case STOVE_GUI:
			return new GuiStove(inv, (TileStove) tile);
		case SMWAND_GUI:
			return new GuiSMWand(inv, new InventorySMWand(player.getHeldItem(hand), player));
		case MFTABLE_GUI:
			return new GuiMFTable(inv, (TileMFTable) tile);
		case MAKER_GUI:
			return new GuiJuiceMaker(inv, (TileJuiceMaker) tile);
		case MFCHANGER_GUI:
			return new GuiMFChanger(inv, (TileMFChanger) tile);
		case FREEZER_GUI:
			return new GuiFreezer(inv, (TileFreezer) tile);
		case AETHER_GUI:
			return new GuiAetherFurnace(inv, (TileAetherFurnace) tile);
		case MFROBE_GUI:
			return new GuiRobe(inv, new InventoryRobe(player));
		case MFPOUCH_GUI:
			return new GuiPouch(inv, new InventoryPouch(player));
		case PARALLELINTERFERE_GUI:
			return new GuiParallelInterfere(inv, (TileParallelInterfere) tile);
		case MFTABLE_ADVANCED_GUI:
			return new GuiMFTable(inv, (TileMFTableAdvanced) tile);
		case MFCHANGER_ADVANCED_GUI:
			return new GuiMFChanger(inv, (TileMFChangerAdvanced) tile);
		case MODENRACK_GUI:
			return new GuiModenRack(inv, (TileModenRack) tile);
		case GUIDBOOK:
			return new GuiGuidBook();
		case RATTAMBASKET:
			return new GuiRattanBasket(inv, (TileRattanBasket) tile);
		case WOODCHEST:
			return new GuiWoodChest(inv, (TileWoodChest) tile);
		case TOOLREPAIR:
			return new GuiToolRepair(inv, (TileToolRepair) tile);
		case GRAVITYCHEST:
			return new GuiGravityChest(inv, (TileGravityChest) tile);
		case MAGIAWRITE:
			return new GuiMagiaWrite(inv, (TileMagiaWrite) tile);
		case AETHERHOPPER:
			return new GuiAetherHopper(inv, (TileAetherHopper) tile);
		case MMTABLE:
			return new GuiMFTable(inv, (TileMFMMTable) tile);
		case STARDUSTWISH:
			return new GuiParallelInterfere(inv, (TileStardustWish) tile);
		case MFF_ADVANCED_GUI:
			return new GuiMFF(inv, (TileMFFurnaceAdvanced) tile);
		case KICHEN_CHEST_GUI:
			return new GuiKichenChest(inv, (TileWoodChest) tile);
		case SUCCESSOR_GUI:
			return new GuiMFSuccessor(inv, (TileMFSuccessor) tile);
		case ARCANETABLE_GUI:
			return new GuiMFArcaneTable(inv, (TileMFArcaneTable) tile);
		}

		return null;
	}
}
