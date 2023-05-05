package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPorch extends ModelBiped {

	private ModelRenderer pouch;

	public ModelPorch(int slotId) {
		this(0.35F, slotId);
	}

	public ModelPorch(float scale, int slotId) {
		this(scale, 0.0F, 64, 32, slotId);
	}

	public ModelPorch(float scale, float rote, int height, int width, int slotId) {
		this.textureWidth = height;
		this.textureHeight = width;
		this.pouch = new ModelRenderer(this, 36, 0);
		this.pouch.addBox(-3.0F, 7.0F, 2.0F, 4, 2, 1, scale);
		this.pouch.setRotationPoint(0.0F, 0.0F + scale, 0.0F);
	}

	private void setRotationOffset(ModelRenderer renderer, float x, float y, float z) {
		renderer.rotateAngleX = x;
		renderer.rotateAngleY = y;
		renderer.rotateAngleZ = z;
	}

	@Override
	public void render(Entity entity, float limbSwing, float swingAmount, float ageTick, float netHeadYaw, float pitch, float scale) {

		this.setRotationAngles(limbSwing, swingAmount, ageTick, netHeadYaw, pitch, scale, entity);
		GlStateManager.pushMatrix();

		// showModelをここでいじる
		this.pouch.showModel = true;

		if (entity.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		this.pouch.render(scale);

		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float swingAmount, float ageTick, float netHeadYaw, float pitch, float scale, Entity entity) {
		super.setRotationAngles(limbSwing, swingAmount, ageTick, netHeadYaw, pitch, scale, entity);
		this.setAngle(this.pouch, this.bipedBody);
	}

	protected void setAngle(ModelRenderer m1, ModelRenderer m2) {
		m1.rotationPointX = m2.rotationPointX;
		m1.rotationPointY = m2.rotationPointY;
		m1.rotationPointZ = m2.rotationPointZ;

		m1.rotateAngleX = m2.rotateAngleX;
		m1.rotateAngleY = m2.rotateAngleY;
		m1.rotateAngleZ = m2.rotateAngleZ;
	}

	@Override
	public void setModelAttributes(ModelBase model) {

		super.setModelAttributes(model);

		if (model instanceof ModelBiped) {
			ModelBiped biped = (ModelBiped) model;
			this.leftArmPose = biped.leftArmPose;
			this.rightArmPose = biped.rightArmPose;
			this.isSneak = biped.isSneak;
			this.isChild = biped.isChild;
			this.isRiding = biped.isRiding;
			this.swingProgress = biped.swingProgress;
		}
	}
}
