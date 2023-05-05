package sweetmagic.init.tile.magic;

import net.minecraft.nbt.NBTTagCompound;

public class TileMagiaFluxCore extends TileSMBase {

	public boolean findPlayer = false;

	public void clientUpdate () {

		this.tickTime++;
		if (this.tickTime % 40 != 0) { return; }

		this.checkRangePlayer();
	}

	public void checkRangePlayer() {
		this.findPlayer = this.findRangePlayer(48D, 24D, 48D);
	}

	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.findPlayer = tags.getBoolean("findPlayer");
	}
}
