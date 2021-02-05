package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

public class LayerBarrier extends LayerEffectBase<EntityLivingBase> {

	public LayerBarrier(RenderLivingBase<?> renderer) {
		super(renderer);
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity.isPotionActive(PotionInit.aether_barrier);
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
			 GlStateManager.enableBlend();
	         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	         GlStateManager.color(1F, 1F, 1F, 1F);
	         this.renderEffect(scale * 12, entity, parTick);
		}
	}

	private void renderEffect(float scale, EntityLivingBase entity, float parTick) {

		float rotY = (entity.ticksExisted + parTick) / 8F;
		float rotX = -0.125F;//MathHelper.sin((entity.ticksExisted + parTick) / 5F) / 4F;
		float rotZ = 0;//MathHelper.cos((entity.ticksExisted + parTick) / 5F) / 4F;

		ItemStack stack = new ItemStack(ItemInit.aether_crystal_shard);
		Minecraft mine = Minecraft.getMinecraft();
		IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(stack, entity.world, entity);

		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1f, 1f, 1f, 1f));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		int count = 4;
		float pi = 180F / (float) Math.PI;

		for (int i = 0; i < count; i++) {

			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0F, -0.5F, 1F);
			mine.getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0f, 0f, 0f, 1f));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}
}
