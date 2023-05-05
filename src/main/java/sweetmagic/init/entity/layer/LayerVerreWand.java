package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.monster.ISMMob;

public class LayerVerreWand implements LayerRenderer<EntityLivingBase> {

	protected final RenderLivingBase<?> render;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin.png");

	public LayerVerreWand(RenderLivingBase<?> render) {
		this.render = render;
	}

	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount, float parTick, float age, float netHeadYaw, float headPitch, float scale) {

		if (!(entity instanceof ISMMob)) { return; }

		if (entity.isNonBoss()) {

			if (!entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty()) {
				this.renderHeadItem(entity, entity.getItemStackFromSlot(EntityEquipmentSlot.HEAD), TransformType.HEAD);
			}

			return;
		}

		ISMMob mob = (ISMMob) entity;
		ItemStack stack = mob.getWandHand();
		if (stack.isEmpty()) { return; }

		GlStateManager.pushMatrix();

		// 必殺技中は描画しない
		if (!mob.getArm().isSpecial()) {
			this.renderHeldItem(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND);
		}

		// 必殺技中のレンダー
		else {
			this.renderEffect(scale * 18, entity, stack, parTick);
		}
		GlStateManager.popMatrix();
	}

	// 杖を手に持たせる描画
	private void renderHeldItem(EntityLivingBase entity, ItemStack stack, TransformType type) {

		GlStateManager.pushMatrix();
		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, EnumHandSide.RIGHT);
		GlStateManager.rotate(-90F, 1F, 0F, 0F);
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.translate(0.4F, 0.2F, -0.75F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, type, false);
		GlStateManager.popMatrix();
	}

	// 頭のアイテム描画
	private void renderHeadItem(EntityLivingBase entity, ItemStack stack, TransformType type) {

		GlStateManager.pushMatrix();
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.translate(0F, 0.4F, 0F);

		float scale = 0.75F;
		GlStateManager.scale(scale, scale, scale);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, type, false);
		GlStateManager.popMatrix();
	}

	// 必殺技中のレンダー
	private void renderEffect(float scale, EntityLivingBase entity, ItemStack stack, float parTick) {

		float rotY = (entity.ticksExisted + parTick) / 6F;
		float rotX = -0.125F;
		float rotZ = 0;

		Minecraft mine = Minecraft.getMinecraft();
		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int count = 6;
		float pi = 180F / (float) Math.PI;

		for (int i = 0; i < count; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * 0.75F * pi + (i * (360F / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
            GlStateManager.rotate(-45, 0F, 0.0F, 1.0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0F, -0.5F, 1F);
			IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(stack, entity.world, entity);
			mine.getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 1.25F, 0F);
		scale *= 2.5F;
		GlStateManager.scale(scale, -scale, scale);
		GlStateManager.rotate(-rotY  / 2 * pi + (360 / count), 0F, 1F, 0F);
		this.renderWand(scale, entity, parTick);
		GlStateManager.popMatrix();

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}

	// 魔法陣描画
	private void renderWand(float scale, EntityLivingBase entity, float parTick) {
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEX);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0, -0.5F).tex(0, 0).endVertex();
		buffer.pos(0.5F, 0, -0.5F).tex(1, 0).endVertex();
		buffer.pos(0.5F, 0, 0.5F).tex(1, 1).endVertex();
		buffer.pos(-0.5F, 0, 0.5F).tex(0, 1).endVertex();
		tessellator.draw();
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
