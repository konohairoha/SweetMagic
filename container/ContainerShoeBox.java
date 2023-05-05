package sweetmagic.init.tile.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 3)
public class ContainerShoeBox extends BaseContainer {

	protected TileWoodChest chest;

	public ContainerShoeBox(InventoryPlayer invPlayer, TileWoodChest tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileWoodChest) this.tile;

		for (int i = 0; i < 3; i++)
			for (int k = 0; k < 9; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), i + k * 3, 8 + k * 18, 18 + i * 18, s -> true));

		this.initPlayerSlot(invPlayer, 8, 84, 8, 142);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		player.playSound(SoundEvents.BLOCK_SHULKER_BOX_CLOSE, 0.5F, 1.25F);
	}
}
