package sweetmagic.key;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.ImmutableBiMap;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

@SideOnly(Side.CLIENT)
public class ClientKeyHelper {

	public static ImmutableBiMap<KeyBinding, SMKeybind> mcToPe;
	private static ImmutableBiMap<SMKeybind, KeyBinding> peToMc;

	public static void registerMCBindings() {
		ImmutableBiMap.Builder<KeyBinding, SMKeybind> builder = ImmutableBiMap.builder();
		for (SMKeybind k : SMKeybind.values()) {
			KeyBinding mcK = new KeyBinding(k.keyName, k.defaultKeyCode, SweetMagicCore.MODID);
			builder.put(mcK, k);
			ClientRegistry.registerKeyBinding(mcK);
		}
		mcToPe = builder.build();
        peToMc = mcToPe.inverse();
	}

	public static String getKeyName(SMKeybind k) {
		return getName(peToMc.get(k));
	}

	public static String getName(KeyBinding k) {
		int keyCode = k.getKeyCode();
		if (keyCode > Keyboard.getKeyCount() || keyCode < 0) {
			return k.getDisplayName();
		}
		return Keyboard.getKeyName(keyCode);
	}
}
