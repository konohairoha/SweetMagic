package sweetmagic.init.tile.container;

import invtweaks.api.container.ChestContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import sweetmagic.init.base.BaseContainer;
import sweetmagic.init.tile.magic.TileGravityChest;
import sweetmagic.init.tile.slot.ValidatedSlot;

@ChestContainer(rowSize = 13)
public class ContainerGravityChest extends BaseContainer {

	protected TileGravityChest chest;

	public ContainerGravityChest(InventoryPlayer invPlayer, TileGravityChest tile) {
		super(invPlayer, tile);
	}

	protected void initSlots(InventoryPlayer invPlayer) {
		this.chest = (TileGravityChest) this.tile;
		for (int i = 0; i < 8; i++)
			for (int k = 0; k < 13; k++)
				this.addSlotToContainer(new ValidatedSlot(this.chest.getChest(), k + i * 13, 5 + k * 18, 13 + i * 18, s -> true));

		this.initPlayerSlot(invPlayer, 41, 159, 41, 218);
	}

	@Override
	protected int getSlotSize() {
		return this.chest.getInvSize();
	}

	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		player.playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 0.5F, player.world.rand.nextFloat() * 0.1F + 0.9F);
	}
}
