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
public class ModelPouch extends ModelBiped {

	public ModelRenderer head;
	public ModelRenderer bipedHeadwear;
	public ModelRenderer body;
	public ModelRenderer rightArm;
	public ModelRenderer leftArm;
	public ModelRenderer rightLeg;
	public ModelRenderer leftLeg;
	public ModelRenderer pouch;

	public boolean isSneak = false;
	public boolean isBlocking = false;
	public boolean aimedBow = false;
	public int slot;

	public ModelPouch(int b) {
		this(0.35F, b);
	}

	public ModelPouch(float f, int b) {
		this(f, 0.0F, 64, 32, b);
	}

	public ModelPouch(float f1, float f2, int i3, int i4, int s) {
		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;
		this.slot = s;
		this.textureWidth = i3;
		this.textureHeight = i4;
		this.head = new ModelRenderer(this, 0, 0);
		this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, f1);
		this.head.setRotationPoint(0.0F, 0.0F + f2, 0.0F);
		this.body = new ModelRenderer(this, 16, 16);
		this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, f1);
		this.body.setRotationPoint(0.0F, 0.0F + f2, 0.0F);
		this.rightArm = new ModelRenderer(this, 40, 16);
		this.rightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, f1);
		this.rightArm.setRotationPoint(-5.0F, 2.0F + f2, 0.0F);
		this.leftArm = new ModelRenderer(this, 40, 16);
		this.leftArm.mirror = true;
		this.leftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, f1);
		this.leftArm.setRotationPoint(5.0F, 2.0F + f2, 0.0F);
		this.rightLeg = new ModelRenderer(this, 0, 16);
		this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f1);
		this.rightLeg.setRotationPoint(-1.9F, 12.0F + f2, 0.0F);
		this.leftLeg = new ModelRenderer(this, 0, 16);
		this.leftLeg.mirror = true;
		this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, f1);
		this.leftLeg.setRotationPoint(1.9F, 12.0F + f2, 0.0F);
		this.pouch = new ModelRenderer(this, 16, 4);
		this.pouch.addBox(-3.0F, 7.0F, 2.0F, 4, 2, 1, f1);
		this.pouch.setRotationPoint(0.0F, 0.0F + f2, 0.0F);

	}

	@Override
	public void render(Entity ent, float f2, float f3, float f4, float f5, float f6, float f7) {

		this.setRotationAngles(f2, f3, f4, f5, f6, f7, ent);
		GlStateManager.pushMatrix();

		// showModelをここでいじる
		this.head.showModel = false;
		this.body.showModel = false;
		this.rightArm.showModel = false;
		this.leftArm.showModel = false;
		this.rightLeg.showModel = false;
		this.leftLeg.showModel = false;
		this.pouch.showModel = true;

		if (this.isChild) {
			float f = 2.0F;
			GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
			GlStateManager.translate(0.0F, 16.0F * f7, 0.0F);
			this.head.render(f7);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
			GlStateManager.translate(0.0F, 24.0F * f7, 0.0F);
			this.body.render(f7);
			this.rightArm.render(f7);
			this.leftArm.render(f7);
			this.rightLeg.render(f7);
			this.leftLeg.render(f7);
			this.pouch.render(f7);
		} else {

			if (ent.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.head.render(f7);
			this.body.render(f7);
			this.rightArm.render(f7);
			this.leftArm.render(f7);
			this.rightLeg.render(f7);
			this.leftLeg.render(f7);
			this.pouch.render(f7);
		}

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
		this.setAngle(this.pouch, this.bipedBody);

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
			ModelBiped modelbiped = (ModelBiped) model;
			this.leftArmPose = modelbiped.leftArmPose;
			this.rightArmPose = modelbiped.rightArmPose;
			this.isSneak = modelbiped.isSneak;
			this.isChild = modelbiped.isChild;
			this.isRiding = modelbiped.isRiding;
			this.swingProgress = modelbiped.swingProgress;
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
		this.pouch.showModel = visible;
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
