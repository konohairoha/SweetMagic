package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelRobeOnly extends ModelBiped {

	private ModelRenderer head;
	private ModelRenderer bipedHeadwear;
	private ModelRenderer body;
	private ModelRenderer rightArm;
	private ModelRenderer leftArm;
	private ModelRenderer rightLeg;
	private ModelRenderer leftLeg;

	private boolean isSneak = false;
	private boolean isBlocking = false;
	private boolean aimedBow = false;
	private int slot;

	public ModelRobeOnly(int slotId) {
		this(0.35F, slotId);
	}

	public ModelRobeOnly(float scale, int slotId) {
		this(scale, 0.0F, 64, 32, slotId);
	}

	public ModelRobeOnly(float scale, float rote, int height, int width, int slotId) {
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.slot = slotId;
		this.textureWidth = height;
		this.textureHeight = width;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);
		this.head.setRotationPoint(0.0F, 0.0F + rote, 0.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
		this.body.setRotationPoint(0.0F, 0.0F + rote, 0.0F);
		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.rightArm.setRotationPoint(-5.0F, 2.0F + rote, 0.0F);
		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, scale);
		this.leftArm.setRotationPoint(5.0F, 2.0F + rote, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.rightLeg.setRotationPoint(-1.9F, 12.0F + rote, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
		this.leftLeg.setRotationPoint(1.9F, 12.0F + rote, 0.0F);
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
		this.head.showModel = true;
		this.body.showModel = true;
		this.rightArm.showModel = true;
		this.leftArm.showModel = true;
		this.rightLeg.showModel = true;
		this.leftLeg.showModel = true;

		if (entity.isSneaking()) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		this.head.render(scale);
		this.body.render(scale);
		this.rightArm.render(scale);
		this.leftArm.render(scale);
		this.rightLeg.render(scale);
		this.leftLeg.render(scale);

		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float swingAmount, float ageTick, float netHeadYaw, float pitch, float scale, Entity entity) {

		super.setRotationAngles(limbSwing, swingAmount, ageTick, netHeadYaw, pitch, scale, entity);

		this.setAngle(this.head, this.bipedHead);
		this.setAngle(this.body, this.bipedBody);
		this.setAngle(this.rightArm, this.bipedRightArm);
		this.setAngle(this.leftArm, this.bipedLeftArm);
		this.setAngle(this.rightLeg, this.bipedRightLeg);
		this.setAngle(this.leftLeg, this.bipedLeftLeg);

		if (this.swingProgress > 0.0F) {
			EnumHandSide side = this.getMainHand(entity);
			ModelRenderer arm = this.getArmForSide(side);
			float f1 = this.swingProgress;
			f1 = 1.0F - this.swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1.0F - f1;
			float f2 = MathHelper.sin(f1 * (float) Math.PI);
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
			arm.rotateAngleX = (float) (arm.rotateAngleX - (f2 * 1.2D + f3));
			arm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			arm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}
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

	@Override
	public void setVisible(boolean visible) {
		this.head.showModel = visible;
		this.body.showModel = visible;
		this.rightArm.showModel = visible;
		this.leftArm.showModel = visible;
		this.rightLeg.showModel = visible;
		this.leftLeg.showModel = visible;
	}

	@Override
	public void postRenderArm(float scale, EnumHandSide side) {
		this.getArmForSide(side).postRender(scale);
	}

	@Override
	protected ModelRenderer getArmForSide(EnumHandSide side) {
		return side == EnumHandSide.LEFT ? this.leftArm : this.rightArm;
	}

	@Override
	protected EnumHandSide getMainHand(Entity entity) {
		return entity instanceof EntityLivingBase ? ((EntityLivingBase) entity).getPrimaryHand() : EnumHandSide.RIGHT;
	}
}
