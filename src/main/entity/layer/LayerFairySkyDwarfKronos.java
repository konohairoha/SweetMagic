package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.monster.EntityFairySkyDwarfKronos;
import sweetmagic.init.entity.monster.ISMMob;

public class LayerFairySkyDwarfKronos implements LayerRenderer<EntityLivingBase> {

	protected final RenderLivingBase<?> render;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/fairy_wing.png");
	private static final ResourceLocation CICLE = new ResourceLocation(SweetMagicCore.MODID, "textures/block/kogen.png");
	private static final ItemStack WAND = new ItemStack(ItemInit.kitt_gadget);

	public LayerFairySkyDwarfKronos(RenderLivingBase<?> render) {
		this.render = render;
	}

	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount, float parTick, float age, float netHeadYaw, float headPitch, float scale) {

		if (!(entity instanceof EntityFairySkyDwarfKronos)) { return; }

		this.renderWing(entity, amount, parTick);
		this.renderGajet(entity, WAND, EnumHandSide.RIGHT);
		this.renderCircle(entity, parTick);
	}

	public void renderWing (EntityLivingBase entity, float amount, float parTick) {

		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);


		GlStateManager.rotate(amount, 0F, 1F, 0F);
		GlStateManager.rotate(180, 1F, 0F, 0F);
		GlStateManager.translate(0, -1.6F, 0);

		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();

		GlStateManager.pushMatrix();

		GlStateManager.translate(0.1F, 0.4F, -0.15F);
		GlStateManager.rotate(20 + 20 * MathHelper.sin((entity.ticksExisted + parTick) * 0.3F), 0, 1, 0);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();

		tes.draw();

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();

		tes.draw();

		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();

		GlStateManager.translate(-0.1, 0.4, -0.15);
		GlStateManager.rotate(-200 - 20 * MathHelper.sin((entity.ticksExisted + parTick) * 0.3F), 0, 1, 0);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();

		tes.draw();

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buf.pos(0, 2, 0).tex(0, 0).endVertex();
		buf.pos(0, 0, 0).tex(0, 1).endVertex();
		buf.pos(2, 0, 0).tex(1, 1).endVertex();
		buf.pos(2, 2, 0).tex(1, 0).endVertex();

		tes.draw();

		GlStateManager.popMatrix();

		GlStateManager.enableLighting();
		GlStateManager.disableBlend();

		GlStateManager.popMatrix();
	}

	// 魔法陣描画
	private void renderCircle(EntityLivingBase entity, float parTick) {

		ISMMob sm = (ISMMob) entity;
		GlStateManager.rotate(180F, 0F, 0F, 1F);

		if (sm.getSpecial()) {
			GlStateManager.scale(1.5D, 1.5D, 1.5D);
			GlStateManager.translate(0, -0.35F, 0);

			float pi = 180F / (float) Math.PI;
			float rotY = (entity.ticksExisted + parTick) / 6F;
			GlStateManager.rotate(-rotY / 2 * pi + (360 / 6), 0F, 1F, 0F);
		}

		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		Minecraft.getMinecraft().getTextureManager().bindTexture(CICLE);
		GlStateManager.translate(0, 1.075F, 0);
		float scale = 1.375F;
		GlStateManager.scale(scale, scale, scale);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-0.5F, 0, -0.5F).tex(0, 0).endVertex();
		buf.pos(0.5F, 0, -0.5F).tex(1, 0).endVertex();
		buf.pos(0.5F, 0, 0.5F).tex(1, 1).endVertex();
		buf.pos(-0.5F, 0, 0.5F).tex(0, 1).endVertex();
		tes.draw();
	}

	// 杖を手に持たせる描画
	private void renderGajet(EntityLivingBase entity, ItemStack stack, EnumHandSide side) {
		GlStateManager.pushMatrix();
		boolean flag = side == EnumHandSide.LEFT;
		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, side);
		GlStateManager.rotate(flag ? -90F : 90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.scale(1.15F, 1F, 1.15F);
		GlStateManager.translate(flag ? 0.14F : -0.15F, -0.75F, 0.5F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, flag);
		GlStateManager.popMatrix();
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}
}
