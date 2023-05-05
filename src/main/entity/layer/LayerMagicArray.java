package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.PotionInit;

public class LayerMagicArray extends LayerEffectBase<EntityLivingBase> {

	private static final ResourceLocation RUNE_TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin.png");
	private static final int FADEOUT_TIME = 60;

	public LayerMagicArray(RenderLivingBase<?> render) {
		super(render);
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity.isPotionActive(PotionInit.magic_array) && entity.isEntityAlive() && entity.ticksExisted < 100;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
			 GlStateManager.enableBlend();
	         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		     this.renderEffect(scale * 18, entity, parTick);
		}
	}

	private void renderEffect(float scale, EntityLivingBase entity, float parTick) {

		int tick = entity.ticksExisted;

		float rotY = (tick + parTick) / 12F;
		float pi = 180F / (float) Math.PI;
		Minecraft mine = Minecraft.getMinecraft();
		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GlStateManager.pushMatrix();
		float alpha = tick >= FADEOUT_TIME ? 1F - (float) (tick - FADEOUT_TIME) / 10F : 1F;
		GlStateManager.color(1F, 1F, 1F, alpha);
		GlStateManager.translate(0F, 1.5F, 0F);
		scale *= Math.min(3F, tick * 0.2F);
		GlStateManager.scale(scale, -scale, scale);
		GlStateManager.rotate(rotY * pi, 0F, 1F, 0F);

		GlStateManager.rotate(180F, 0F, 0F, 1F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		mine.getTextureManager().bindTexture(RUNE_TEX);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0, -0.5F).tex(0, 0).endVertex();
		buffer.pos(0.5F, 0, -0.5F).tex(1, 0).endVertex();
		buffer.pos(0.5F, 0, 0.5F).tex(1, 1).endVertex();
		buffer.pos(-0.5F, 0, 0.5F).tex(0, 1).endVertex();
		tessellator.draw();

		GlStateManager.popMatrix();

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}
}

