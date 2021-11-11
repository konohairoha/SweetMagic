package sweetmagic.spwanblock;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class SpawnBlockRegsterContainer implements INBTSerializable<NBTTagCompound> {

	private SpwanBlockRegistry spawn = new SpwanBlockRegistry();
	private static final String SM_SPANWBLOCK = "sm_spanwblock";

	public boolean isPosInRange (EntityLivingBase entity) {
		return this.getRegistry().isPosInRange(entity.world, entity.posX, entity.posY, entity.posZ);
	}

	public SpwanBlockRegistry getRegistry () {
		return this.spawn;
	}

	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setTag(SM_SPANWBLOCK, this.spawn.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (nbt.hasKey(SM_SPANWBLOCK)) {
			this.spawn.deserializeNBT(nbt.getTagList(SM_SPANWBLOCK, Constants.NBT.TAG_COMPOUND));
		}
	}
}
