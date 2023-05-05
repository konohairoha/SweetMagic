package sweetmagic.init.tile.magic;

import net.minecraft.nbt.NBTTagCompound;

public class TileWarpBlock extends TileSMBase {

	public int posX;
	public int posY;
	public int posZ;

	public boolean isSetPos () {
		return this.posY != 0;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("posX", this.posX);
		tags.setInteger("posY", this.posY);
		tags.setInteger("posZ", this.posZ);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.posX = tags.getInteger("posX");
		this.posY = tags.getInteger("posY");
		this.posZ = tags.getInteger("posZ");
	}
}
