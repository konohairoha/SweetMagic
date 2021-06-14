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
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

public class LayerRegen extends LayerEffectBase<EntityLivingBase> {

	private static final ItemStack STACK = new ItemStack(ItemInit.cosmic_crystal_shard);

	public LayerRegen(RenderLivingBase<?> renderer) {
		super(renderer);
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity.isPotionActive(PotionInit.regene);
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
	         this.renderEffect(scale * 8, entity, parTick);
		}
	}

	private void renderEffect(float scale, EntityLivingBase entity, float parTick) {

		float rotY = (entity.ticksExisted + parTick) / 8F;
		float rotX = -0.125F;
		float rotZ = 0;

		Minecraft mine = Minecraft.getMinecraft();
		IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(STACK, entity.world, entity);

		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		int count = 6;
		float pi = 180F / (float) Math.PI;

		for (int i = 0; i < count; i++) {

			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0F, MathHelper.sin((entity.ticksExisted + parTick) * 0.25F + i) * 0.15F + 1.35F, 1F);
			mine.getRenderItem().renderItem(STACK, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}
}
