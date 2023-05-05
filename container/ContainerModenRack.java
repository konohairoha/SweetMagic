package sweetmagic.init.tile.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.ValidatedSlot;

public class ContainerModenRack extends BaseContainer {

	protected TileModenRack chest;
	private int data;

	public ContainerModenRack(InventoryPlayer invPlayer, TileModenRack tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileModenRack) this.tile;
		this.data = this.chest.getRackData();

		try {
			this.addSlot();
		}

		catch (Throwable e) {
			invPlayer.closeInventory(invPlayer.player);
		}

		this.initPlayerSlot(invPlayer, 7, 50, 7, 108);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}

	public void addSlot () {

		// データ値で分岐
		switch (this.data) {
		case 0:
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 9; k++)
					this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 9, 7 + 18 * k, 8 + 18 * i, s -> true));
			break;
		case 1:
			for (int i = 0; i < 3; i++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), i, 61 + 18 * i, 25, s -> true));
			break;
		case 2:
		case 3:
		case 4:
		case 5:
			this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), 0, 79, 25, s -> true));
			break;
		case 6:
		case 7:
		case 8:
		case 9:
			for (int i = 0; i < 2; i++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), i, 79, 11 + 18 * i, s -> true));
			break;
		case 10:
			for (int k = 0; k < 6; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k, 34 + 18 * k, 17, SlotPredicates.ISPLATE));
			break;
		case 11:
			for (int i = 0; i < 4; i++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), i, 52 + 18 * i, 17, s -> true));
			break;
		case 12:
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 3; k++)
					this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 3, 61 + 18 * k, 9 + 18 * i, s -> true));
			break;
		case 13:
		case 15:
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 4; k++)
					this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 4, 52 + 18 * k, 9 + 18 * i, s -> true));
			break;
		case 14:
			for (int i = 0; i < 2; i++)
				for (int k = 0; k < 2; k++)
					this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 2, 70 + 18 * k, 9 + 18 * i, s -> true));
			break;
		}
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (this.data == 6) {
			World world = player.world;
			world.playSound(null, player.getPosition(), SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
		}
	}
}
