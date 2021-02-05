package sweetmagic.init;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.client.particle.ParticleIceCrystal;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class ParticleInit {

	public static final List<String> texList = new ArrayList<String>();

	public static void init () {
		texList.add(ParticleIceCrystal.ORB_TEX);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void textureStitch(TextureStitchEvent.Pre event) {
    	ParticleInit.init();
		TextureMap texMap = event.getMap();
		for (String src : texList) {
			texMap.registerSprite(new ResourceLocation(src));
		}
	}
}
