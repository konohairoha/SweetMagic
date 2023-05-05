package sweetmagic.init.render.projectile;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;

@SideOnly(Side.CLIENT)
public class RenderMeteorMagic extends RenderLightMagic {

	private static final IBlockState MAGMA = Blocks.MAGMA.getDefaultState();

	public RenderMeteorMagic(RenderManager render) {
		super(render);
	}

	public void renderAnimetion (EntityBaseMagicShot entity, double x, double y, double z, float entityYaw, float parTick) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) (y - 0.75F), (float) z);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.scale(1F, 1F, 1F);
		Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(MAGMA, 1);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
