package sweetmagic.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.chest.TileRattanBasket;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.container.BookContainer;
import sweetmagic.init.tile.container.ContainerAetherFurnace;
import sweetmagic.init.tile.container.ContainerFreezer;
import sweetmagic.init.tile.container.ContainerJuiceMaker;
import sweetmagic.init.tile.container.ContainerMFChanger;
import sweetmagic.init.tile.container.ContainerMFChangerAdvanced;
import sweetmagic.init.tile.container.ContainerMFFisher;
import sweetmagic.init.tile.container.ContainerMFFurnace;
import sweetmagic.init.tile.container.ContainerMFTable;
import sweetmagic.init.tile.container.ContainerMFTableAdvanced;
import sweetmagic.init.tile.container.ContainerMFTank;
import sweetmagic.init.tile.container.ContainerModenRack;
import sweetmagic.init.tile.container.ContainerParallelInterfere;
import sweetmagic.init.tile.container.ContainerPouch;
import sweetmagic.init.tile.container.ContainerRattanBasket;
import sweetmagic.init.tile.container.ContainerRobe;
import sweetmagic.init.tile.container.ContainerSMWand;
import sweetmagic.init.tile.container.ContainerStove;
import sweetmagic.init.tile.container.ContainerWoodChest;
import sweetmagic.init.tile.cook.TileFreezer;
import sweetmagic.init.tile.cook.TileJuiceMaker;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.init.tile.furnace.TileMFF;
import sweetmagic.init.tile.gui.GuiAetherFurnace;
import sweetmagic.init.tile.gui.GuiBook;
import sweetmagic.init.tile.gui.GuiFreezer;
import sweetmagic.init.tile.gui.GuiGuidBook;
import sweetmagic.init.tile.gui.GuiJuiceMaker;
import sweetmagic.init.tile.gui.GuiMFChanger;
import sweetmagic.init.tile.gui.GuiMFChangerAdvanced;
import sweetmagic.init.tile.gui.GuiMFF;
import sweetmagic.init.tile.gui.GuiMFFisher;
import sweetmagic.init.tile.gui.GuiMFTable;
import sweetmagic.init.tile.gui.GuiMFTableAdvanced;
import sweetmagic.init.tile.gui.GuiMFTank;
import sweetmagic.init.tile.gui.GuiModenRack;
import sweetmagic.init.tile.gui.GuiParallelInterfere;
import sweetmagic.init.tile.gui.GuiPouch;
import sweetmagic.init.tile.gui.GuiRattanBasket;
import sweetmagic.init.tile.gui.GuiRobe;
import sweetmagic.init.tile.gui.GuiSMWand;
import sweetmagic.init.tile.gui.GuiStove;
import sweetmagic.init.tile.gui.GuiWoodChest;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.inventory.InventoryRobe;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.magic.TileAetherFurnace;
import sweetmagic.init.tile.magic.TileMFChanger;
import sweetmagic.init.tile.magic.TileMFChangerAdvanced;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.init.tile.magic.TileMFTableAdvanced;
import sweetmagic.init.tile.magic.TileMFTank;
import sweetmagic.init.tile.magic.TileParallelInterfere;

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

	///サーバ側の処理
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
//		EnumHand hand = ITEM_IDS.contains(ID) ? (x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) : null;
		EnumHand hand = EnumHand.MAIN_HAND;

		//IDで判断する
		if(ID == SMGuiHandler.SMBOOK_GUI) {
			return new BookContainer(player.inventory);
		} else if(ID == SMGuiHandler.MFF_GUI) {
			return new ContainerMFFurnace(player.inventory, (TileMFF) tile);
		} else if(ID == SMGuiHandler.MFTANK_GUI) {
			return new ContainerMFTank(player.inventory, (TileMFTank) tile);
		} else if(ID == SMGuiHandler.MFFISHER_GUI) {
			return new ContainerMFFisher(player.inventory, (TileMFFisher) tile);
		} else if(ID == SMGuiHandler.STOVE_GUI) {
			return new ContainerStove(player.inventory, (TileStove) tile);
		} else if(ID == SMGuiHandler.SMWAND_GUI) {
			return new ContainerSMWand(player.inventory, new InventorySMWand(player.getHeldItem(hand), player));
		} else if(ID == SMGuiHandler.MFTABLE_GUI) {
			return new ContainerMFTable(player.inventory, (TileMFTable) tile);
		} else if(ID == SMGuiHandler.MAKER_GUI) {
			return new ContainerJuiceMaker(player.inventory, (TileJuiceMaker) tile);
		} else if(ID == SMGuiHandler.MFCHANGER_GUI) {
			return new ContainerMFChanger(player.inventory, (TileMFChanger) tile);
		} else if(ID == SMGuiHandler.FREEZER_GUI) {
			return new ContainerFreezer(player.inventory, (TileFreezer) tile);
		} else if(ID == SMGuiHandler.AETHER_GUI) {
			return new ContainerAetherFurnace(player.inventory, (TileAetherFurnace) tile);
		} else if(ID == SMGuiHandler.MFROBE_GUI) {
			return new ContainerRobe(player.inventory, new InventoryRobe(player));
		} else if(ID == SMGuiHandler.MFPOUCH_GUI) {
			return new ContainerPouch(player.inventory, new InventoryPouch(player));
		} else if(ID == SMGuiHandler.PARALLELINTERFERE_GUI) {
			return new ContainerParallelInterfere(player.inventory, (TileParallelInterfere) tile);
		} else if(ID == SMGuiHandler.MFTABLE_ADVANCED_GUI) {
			return new ContainerMFTableAdvanced(player.inventory, (TileMFTableAdvanced) tile);
		} else if(ID == SMGuiHandler.MFCHANGER_ADVANCED_GUI) {
			return new ContainerMFChangerAdvanced(player.inventory, (TileMFChangerAdvanced) tile);
		} else if(ID == SMGuiHandler.MODENRACK_GUI) {
			return new ContainerModenRack(player.inventory, (TileModenRack) tile);
		} else if(ID == SMGuiHandler.RATTAMBASKET) {
			return new ContainerRattanBasket(player.inventory, (TileRattanBasket) tile);
		} else if(ID == SMGuiHandler.WOODCHEST) {
			return new ContainerWoodChest(player.inventory, (TileWoodChest) tile);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		if (!world.isBlockLoaded(new BlockPos(x, y, z))) { return null; }
		TileEntity tile = world.getTileEntity(pos);
//		EnumHand hand = ITEM_IDS.contains(ID) ? (x == 1 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) : null;
		EnumHand hand = EnumHand.MAIN_HAND;

		//IDで判断する
		//TileEntityを取得する
		if(ID == SMGuiHandler.SMBOOK_GUI) {
			return new GuiBook(player.inventory);
		} else if(ID == SMGuiHandler.MFF_GUI) {
			return new GuiMFF(player.inventory, (TileMFF) tile);
		} else if(ID == SMGuiHandler.MFTANK_GUI) {
			return new GuiMFTank(player.inventory, (TileMFTank) tile);
		} else if(ID == SMGuiHandler.MFFISHER_GUI) {
			return new GuiMFFisher(player.inventory, (TileMFFisher) tile);
		} else if(ID == SMGuiHandler.STOVE_GUI) {
			return new GuiStove(player.inventory, (TileStove) tile);
		} else if(ID == SMGuiHandler.SMWAND_GUI) {
			return new GuiSMWand(player.inventory, new InventorySMWand(player.getHeldItem(hand), player));
		} else if(ID == SMGuiHandler.MFTABLE_GUI) {
			return new GuiMFTable(player.inventory, (TileMFTable) tile);
		} else if(ID == SMGuiHandler.MAKER_GUI) {
			return new GuiJuiceMaker(player.inventory, (TileJuiceMaker) tile);
		} else if(ID == SMGuiHandler.MFCHANGER_GUI) {
			return new GuiMFChanger(player.inventory, (TileMFChanger) tile);
		} else if(ID == SMGuiHandler.FREEZER_GUI) {
			return new GuiFreezer(player.inventory, (TileFreezer) tile);
		} else if(ID == SMGuiHandler.AETHER_GUI) {
			return new GuiAetherFurnace(player.inventory, (TileAetherFurnace) tile);
		} else if(ID == SMGuiHandler.MFROBE_GUI) {
			return new GuiRobe(player.inventory, new InventoryRobe(player));
		} else if(ID == SMGuiHandler.MFPOUCH_GUI) {
			return new GuiPouch(player.inventory, new InventoryPouch(player));
		} else if(ID == SMGuiHandler.PARALLELINTERFERE_GUI) {
			return new GuiParallelInterfere(player.inventory, (TileParallelInterfere) tile);
		} else if(ID == SMGuiHandler.MFTABLE_ADVANCED_GUI) {
			return new GuiMFTableAdvanced(player.inventory, (TileMFTableAdvanced) tile);
		} else if(ID == SMGuiHandler.MFCHANGER_ADVANCED_GUI) {
			return new GuiMFChangerAdvanced(player.inventory, (TileMFChangerAdvanced) tile);
		} else if(ID == SMGuiHandler.MODENRACK_GUI) {
			return new GuiModenRack(player.inventory, (TileModenRack) tile);
		} else if (ID == SMGuiHandler.GUIDBOOK) {
			return new GuiGuidBook();
		} else if(ID == SMGuiHandler.RATTAMBASKET) {
			return new GuiRattanBasket(player.inventory, (TileRattanBasket) tile);
		} else if(ID == SMGuiHandler.WOODCHEST) {
			return new GuiWoodChest(player.inventory, (TileWoodChest) tile);
		}
		return null;
	}
}
