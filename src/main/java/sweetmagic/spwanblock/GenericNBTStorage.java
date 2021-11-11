package sweetmagic.spwanblock;

import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

public class GenericNBTStorage<T extends INBTSerializable<NBTTagCompound>> implements Capability.IStorage<T> {

	@Nullable
	@Override
	public NBTBase writeNBT(Capability<T> cap, T instance, EnumFacing side) {
		return instance.serializeNBT();
	}

	@Override
	public void readNBT(Capability<T> cap, T instance, EnumFacing side, NBTBase nbt) {
		if (nbt instanceof NBTTagCompound) {
			instance.deserializeNBT((NBTTagCompound) nbt);
		}
	}
}
