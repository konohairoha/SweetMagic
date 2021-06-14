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

public class LayerManHandItem implements LayerRenderer<EntityLivingBase> {
	protected final RenderLivingBase<?> render;

	public LayerManHandItem(RenderLivingBase<?> render) {
		this.render = render;
	}

	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount, float parTick, float age, float netHeadYaw, float headPitch, float scale) {

		boolean flag = entity.getPrimaryHand() == EnumHandSide.RIGHT;
		ItemStack stack = flag ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
		ItemStack stack1 = flag ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();

		if (!stack.isEmpty() || !stack1.isEmpty()) {
			GlStateManager.pushMatrix();

			if (this.render.getMainModel().isChild) {
				GlStateManager.translate(0.0F, 0.75F, 0.0F);
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}

			this.renderHeldItem(entity, stack1, TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
			this.renderHeldItem(entity, stack, TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
			GlStateManager.popMatrix();
		}
	}

	private void renderHeldItem(EntityLivingBase entity, ItemStack stack, TransformType par1, EnumHandSide side) {

		if (stack.isEmpty()) { return; }

		GlStateManager.pushMatrix();

		if (entity.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		this.translateToHand(side);
		GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1F, 0.0F);
		boolean flag = side == EnumHandSide.LEFT;
		GlStateManager.translate((float) (flag ? -1 : 1) / 16.0F, 0.125F, -1.65F);
		Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, par1, flag);
		GlStateManager.popMatrix();
	}

	protected void translateToHand(EnumHandSide hand) {
		((ModelBiped) this.render.getMainModel()).postRenderArm(0.0625F, hand);
	}

	public boolean shouldCombineTextures() {
		return false;
	}
}
