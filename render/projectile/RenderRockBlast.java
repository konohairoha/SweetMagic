package sweetmagic.init.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;

@SideOnly(Side.CLIENT)
public class RenderRockBlast extends RenderLightMagic {

	public RenderRockBlast(RenderManager render) {
		super(render);
	}

	public void renderAnimetion (EntityBaseMagicShot entity, double x, double y, double z, float entityYaw, float parTick) {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		float dx = (float) (x - 0.5F);
		float dy = (float) (y + 0.5F);
		float dz = (float) (z + 0.5F);
        GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.translate(dx, dy, dz);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.STONE.getDefaultState(), 1);
		GlStateManager.popMatrix();
		GlStateManager.disableBlend();
	}
}
