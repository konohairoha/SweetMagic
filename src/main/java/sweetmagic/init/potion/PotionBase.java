package sweetmagic.init.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;

public class PotionBase extends Potion {

	public final ResourceLocation sprite;

	public PotionBase (boolean effect, int color, String name, String dir) {
		super(effect, color);
		this.setPotionName(SweetMagicCore.MODID + ".effect." + name);
		this.sprite = SweetMagicCore.prefix(dir);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		this.drawModalRectWithCustomSizedTexture(x + 3, y + 3, z, 0, 0, 18, 18, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		Minecraft.getMinecraft().renderEngine.bindTexture(sprite);
		this.drawModalRectWithCustomSizedTexture(x + 6, y + 7, z, 0, 0, 18, 18, 18, 18);
	}

	public void drawModalRectWithCustomSizedTexture(int x, int y, float z, float u, float v, int width,
			int height, float textureWidth, float textureHeight) {
		float f = 1.0F / textureWidth;
		float f1 = 1.0F / textureHeight;
		Tessellator tesl = Tessellator.getInstance();
		BufferBuilder buffer = tesl.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos((double) x, (double) (y + height), (double) z)
				.tex((double) (u * f), (double) ((v + (float) height) * f1)).endVertex();
		buffer.pos((double) (x + width), (double) (y + height), (double) z)
				.tex((double) ((u + (float) width) * f), (double) ((v + (float) height) * f1)).endVertex();
		buffer.pos((double) (x + width), (double) y, (double) z)
				.tex((double) ((u + (float) width) * f), (double) (v * f1)).endVertex();
		buffer.pos((double) x, (double) y, (double) z).tex((double) (u * f), (double) (v * f1)).endVertex();
		tesl.draw();
	}

	public void registerPotion(String name, Potion potion, RegistryEvent.Register<Potion> event){
		event.getRegistry().register(potion.setRegistryName(SweetMagicCore.MODID, name));
	}
}
