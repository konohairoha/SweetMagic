package sweetmagic.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.CapabilityHandler;
import sweetmagic.spwanblock.CapSpawnBlockRegistryContainer;
import sweetmagic.spwanblock.SpawnBlockRegsterContainer;

public class EntitiySpawnEvent {

	@SubscribeEvent
	public void onWorldAttachCapabilityEvent(AttachCapabilitiesEvent<World> event) {
		event.addCapability(new ResourceLocation(SweetMagicCore.MODID, "registry"), new CapSpawnBlockRegistryContainer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onEntityCheckSpawn(LivingSpawnEvent.CheckSpawn event) {

		if (event.getResult() == Event.Result.ALLOW || event.isSpawner()) { return; }

		EntityLivingBase entity = event.getEntityLiving();
		if ( ( !(entity instanceof IMob) && !(entity instanceof EntityBat) ) || !entity.isNonBoss()) { return; }

		SpawnBlockRegsterContainer container = event.getWorld().getCapability(CapabilityHandler.CONTAINER_REGISTRY, null);

		if (container != null && container.isPosInRange(entity)) {
			event.setResult(Event.Result.DENY);
		}
	}
}
