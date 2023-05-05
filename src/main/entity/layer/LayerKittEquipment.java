package sweetmagic.init.entity.layer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import sweetmagic.init.entity.monster.ISMMob;

public class LayerKittEquipment implements LayerRenderer<EntityLivingBase> {

	protected final RenderLivingBase<?> render;

	public LayerKittEquipment(RenderLivingBase<?> render) {
		this.render = render;
	}

	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount, float parTick, float age, float netHeadYaw, float headPitch, float scale) {

		if (entity.isNonBoss() || !(entity instanceof ISMMob)) { return; }

		ISMMob mob = (ISMMob) entity;
		ItemStack stack = mob.getWandHand();
		if (stack.isEmpty()) { return; }

		GlStateManager.pushMatrix();

		switch (mob.getArmorType()) {
		case 0:
			this.renderHammer(entity, stack);
			break;
		case 1:
			this.renderLifle(entity, stack);
			break;
		case 2:
			this.renderGajet(entity, stack, EnumHandSide.RIGHT);
			this.renderGajet(entity, stack, EnumHandSide.LEFT);
			this.renderJet(entity, mob.getBackPack());
			break;
		}
		GlStateManager.popMatrix();
	}

	// 杖を手に持たせる描画
	private void renderHammer(EntityLivingBase entity, ItemStack stack) {

		GlStateManager.pushMatrix();
		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, EnumHandSide.RIGHT);
		GlStateManager.translate(0.4F, 0.72F, 0.2F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, false);
		GlStateManager.popMatrix();
	}

	// 杖を手に持たせる描画
	private void renderLifle(EntityLivingBase entity, ItemStack stack) {

		GlStateManager.pushMatrix();
		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, EnumHandSide.RIGHT);
		GlStateManager.scale(2F, 2F, 2F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);
		GlStateManager.rotate(-9F, 0F, 1F, 0F);
		GlStateManager.translate(0.175F, 0.12F, -0.30F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, false);
		GlStateManager.popMatrix();
	}

	// 杖を手に持たせる描画
	private void renderGajet(EntityLivingBase entity, ItemStack stack, EnumHandSide side) {

		GlStateManager.pushMatrix();
		boolean flag = side == EnumHandSide.LEFT;
		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, side);
		GlStateManager.rotate(flag ? -90F : 90F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
		GlStateManager.scale(1.15F, 1F, 1.15F);
		GlStateManager.translate(flag ? -0.235F : 0.2F, -0.75F, 0.5F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, flag);
		GlStateManager.popMatrix();
	}

	// 杖を手に持たせる描画
	private void renderJet(EntityLivingBase entity, ItemStack stack) {

		if (stack.isEmpty()) { return; }

		GlStateManager.pushMatrix();
//		boolean flag = side == EnumHandSide.LEFT;
//		((ModelBiped) this.render.getMainModel()).postRenderArm(0F, EnumHandSide.RIGHT);
		GlStateManager.rotate(180F, 0F, 1F, 0F);
		GlStateManager.rotate(180F, 1F, 0F, 0F);
//		GlStateManager.scale(1.15F, 1F, 1.15F);
		GlStateManager.translate(0F, -0.29F, 0.45F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, TransformType.THIRD_PERSON_RIGHT_HAND, false);
		GlStateManager.popMatrix();
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
