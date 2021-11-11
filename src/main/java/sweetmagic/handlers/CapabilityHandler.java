package sweetmagic.handlers;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import sweetmagic.spwanblock.GenericNBTStorage;
import sweetmagic.spwanblock.SpawnBlockRegsterContainer;

public class CapabilityHandler {

    @CapabilityInject(SpawnBlockRegsterContainer.class)
    public static Capability<SpawnBlockRegsterContainer> CONTAINER_REGISTRY = null;

	public static void registerModCaps() {
        CapabilityManager.INSTANCE.register(SpawnBlockRegsterContainer.class, new GenericNBTStorage<>(), SpawnBlockRegsterContainer::new);
    }
}
