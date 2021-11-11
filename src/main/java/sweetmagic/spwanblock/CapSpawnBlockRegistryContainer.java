package sweetmagic.spwanblock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import sweetmagic.handlers.CapabilityHandler;

public class CapSpawnBlockRegistryContainer implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound> {

    private SpawnBlockRegsterContainer container = new SpawnBlockRegsterContainer();

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing face) {
		return cap == CapabilityHandler.CONTAINER_REGISTRY;
	}

    @Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing face) {
        return this.hasCapability(cap, face) ? CapabilityHandler.CONTAINER_REGISTRY.cast(this.container) : null;
    }

	@Override
	public NBTTagCompound serializeNBT() {
		return this.container.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		this.container.deserializeNBT(nbt);
	}
}