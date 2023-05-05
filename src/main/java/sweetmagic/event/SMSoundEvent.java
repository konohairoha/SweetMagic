package sweetmagic.event;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import sweetmagic.SweetMagicCore;

@Mod.EventBusSubscriber(modid = SweetMagicCore.MODID)
public class SMSoundEvent {

	public static final SoundEvent PAGE = createEvent("page");
	public static final SoundEvent MACHIN = createEvent("machin");
	public static final SoundEvent DROP = createEvent("drop");
	public static final SoundEvent OVEN_ON = createEvent("oven_on");
	public static final SoundEvent OVEN_FIN = createEvent("oven_fin");
	public static final SoundEvent LEVELUP = createEvent("levelup");
	public static final SoundEvent HEAL = createEvent("heal");
	public static final SoundEvent STOVE_OFF = createEvent("stove_off");
	public static final SoundEvent STOVE_ON = createEvent("stove_on");
	public static final SoundEvent POT = createEvent("pot");
	public static final SoundEvent FRYPAN = createEvent("frypan");
	public static final SoundEvent CHANGETIME = createEvent("changetime");
	public static final SoundEvent NEXT = createEvent("next");
	public static final SoundEvent CYCLON = createEvent("cyclon");
	public static final SoundEvent ELECTRIC = createEvent("electric");
	public static final SoundEvent MAGICSTART = createEvent("magicstart");
	public static final SoundEvent FROST = createEvent("frost");
	public static final SoundEvent GRAVITY = createEvent("gravity");
	public static final SoundEvent JMON = createEvent("jm_on");
	public static final SoundEvent JMFIN = createEvent("jm_fin");
	public static final SoundEvent HORAMAGIC = createEvent("horamagic");
	public static final SoundEvent REVERTIME = createEvent("revertime");
	public static final SoundEvent ROBE = createEvent("robe");
	public static final SoundEvent WRITE = createEvent("write");
	public static final SoundEvent GROW = createEvent("grow");
	public static final SoundEvent MAGICCRAFT = createEvent("magic_craft");
	public static final SoundEvent BABULE = createEvent("babule");
	public static final SoundEvent STARDUST = createEvent("stardust");
	public static final SoundEvent AVOID = createEvent("avoid");
	public static final SoundEvent EC_AMBIENT = createEvent("ec_ambient");
	public static final SoundEvent EC_DEATH = createEvent("ec_death");
	public static final SoundEvent EC_HURT = createEvent("ec_hurt");
	public static final SoundEvent FLASH = createEvent("flash");
	public static final SoundEvent STORAGE = createEvent("storage");
	public static final SoundEvent KNOCKBACK = createEvent("knockback");

	private static SoundEvent createEvent(String sound) {
		ResourceLocation name = new ResourceLocation(SweetMagicCore.MODID, sound);
		return new SoundEvent(name).setRegistryName(name);
	}

	@SubscribeEvent
	public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {

		IForgeRegistry registry = event.getRegistry();

		registry.register(PAGE);
		registry.register(MACHIN);
		registry.register(DROP);
		registry.register(OVEN_ON);
		registry.register(OVEN_FIN);
		registry.register(LEVELUP);
		registry.register(HEAL);
		registry.register(STOVE_OFF);
		registry.register(STOVE_ON);
		registry.register(POT);
		registry.register(FRYPAN);
		registry.register(CHANGETIME);
		registry.register(NEXT);
		registry.register(CYCLON);
		registry.register(ELECTRIC);
		registry.register(MAGICSTART);
		registry.register(FROST);
		registry.register(GRAVITY);
		registry.register(JMON);
		registry.register(JMFIN);
		registry.register(REVERTIME);
		registry.register(HORAMAGIC);
		registry.register(ROBE);
		registry.register(WRITE);
		registry.register(GROW);
		registry.register(MAGICCRAFT);
		registry.register(BABULE);
		registry.register(STARDUST);
		registry.register(AVOID);
		registry.register(FLASH);
		registry.register(STORAGE);
		registry.register(KNOCKBACK);
	}

	public SMSoundEvent() { }
}
