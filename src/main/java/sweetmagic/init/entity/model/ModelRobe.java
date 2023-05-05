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
import sweetmagic.init.entity.monster.EntityTamedKit;
import sweetmagic.init.entity.monster.ISMMob;

@SideOnly(Side.CLIENT)
public class ModelRobe extends ModelBiped {

	private ModelRenderer head;
	private ModelRenderer bipedHeadwear;
	private ModelRenderer body;
	private ModelRenderer rightArm;
	private ModelRenderer leftArm;
	private ModelRenderer rightLeg;
	private ModelRenderer leftLeg;
    private ModelRenderer rightEar;
    private ModelRenderer leftEar;
    private ModelRenderer tail;
	private ModelRenderer pouch;
    private final boolean isCat;
    private final boolean isPorch;

	private boolean isSneak = false;
	private boolean isBlocking = false;
	private boolean aimedBow = false;
	private int slot;

	public ModelRobe(int slotId) {
		this(0.35F, slotId);
	}

	public ModelRobe(float scale, int slotId) {
		this(scale, 0.0F, 64, 32, slotId);
	}

	public ModelRobe(float scale, float rote, int height, int width, int slotId) {
		this.isCat = slotId == 10;
		this.isPorch = slotId == 2;
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

		if (this.isCat) {
	        this.rightEar = new ModelRenderer(this, 52, 0);
	        this.rightEar.addBox(-2.5F, -9.0F, -1.0F, 2, 5, 1);
	        this.rightEar.setRotationPoint(0.0F, 16.0F, -1.0F);
	        this.rightEar.mirror = true;
	        this.setRotationOffset(this.rightEar, 0.0F, -0.2617994F, 0.0F);
	        this.leftEar = new ModelRenderer(this, 58, 0);
	        this.leftEar.addBox(0.5F, -9.0F, -1.0F, 2, 5, 1);
	        this.leftEar.setRotationPoint(0.0F, 16.0F, -1.0F);
	        this.leftEar.mirror = true;
	        this.setRotationOffset(this.leftEar, 0.0F, 0.2617994F, 0.0F);
	        this.tail = new ModelRenderer(this, 32, 0);
	        this.tail.addBox(-0.5F, 0.0F, 0.0F, 1, 12, 1);
	        this.tail.rotateAngleX = 0.9F;
	        this.tail.setRotationPoint(0.0F, 15.0F, 8.0F);
		}

		else if (this.isPorch) {
			this.pouch = new ModelRenderer(this, 36, 0);
			this.pouch.addBox(-3.0F, 7.0F, 2.0F, 4, 2, 1, scale);
			this.pouch.setRotationPoint(0.0F, 0.0F + scale, 0.0F);
		}
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

		if (this.isCat) {
			this.rightEar.showModel = true;
			this.leftEar.showModel = true;
			this.tail.showModel = true;
		}

		else if (this.isPorch) {
			this.pouch.showModel = true;
		}

		if (this.isChild) {
			float f = 2.0F;
			GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
			GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
			this.head.render(scale);
			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);
		}

		else {

			if (entity.isSneaking()) {
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			}

			this.head.render(scale);
			this.body.render(scale);
			this.rightArm.render(scale);
			this.leftArm.render(scale);
			this.rightLeg.render(scale);
			this.leftLeg.render(scale);

			if (this.isCat) {
				this.rightEar.render(scale);
				this.leftEar.render(scale);
				this.tail.render(scale);
			}

			else if (this.isPorch) {
				this.pouch.render(scale);
			}
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

		if (this.isCat) {
			this.setAngle(this.rightEar, this.bipedHead);
			this.setAngle(this.leftEar, this.bipedHead);
			this.setAngle(this.tail, this.bipedBody);
			this.rightEar.rotationPointY = -4F;
			this.leftEar.rotationPointY = -4F;
			this.tail.rotateAngleX = 6.95F;
			this.tail.rotationPointY = 10F;
			this.tail.rotationPointZ = 1.65F;

			float headAngleX = this.head.rotateAngleX * 1.5F;
			float ageCos1 = MathHelper.cos(ageTick * 0.11F) * 0.045F;
			float ageCos2 = MathHelper.cos(ageTick * 0.06875F);
			this.rightEar.rotateAngleX = -ageCos1 + headAngleX;
			this.leftEar.rotateAngleX = ageCos1 + headAngleX;
			this.tail.rotateAngleZ = -ageCos2 * 0.35F;

			if (this.isSneak) {
				this.rightEar.rotationPointY += 1F;
				this.leftEar.rotationPointY += 1F;
				this.tail.rotationPointZ += 4F;
				this.tail.rotationPointY -= 2F;
			}

			if (entity instanceof EntityTamedKit) {

				if (((EntityTamedKit) entity).isSitting()) {
					this.leftLeg.rotateAngleX -= 1.5F;
					this.rightLeg.rotateAngleX -= 1.5F;
					this.leftLeg.rotateAngleY -= 0.25F;
					this.rightLeg.rotateAngleY += 0.25F;
					this.leftLeg.offsetY = 0.5F;
					this.rightLeg.offsetY = 0.5F;
					this.body.offsetY = 0.45F;
					this.leftArm.offsetY = 0.45F;
					this.rightArm.offsetY = 0.45F;
					this.head.offsetY = 0.45F;
					this.rightEar.offsetY = 0.45F;
					this.leftEar.offsetY = 0.45F;
					this.tail.offsetY = 0.45F;
				}

				else {
					this.leftLeg.offsetY = 0F;
					this.rightLeg.offsetY = 0F;
					this.body.offsetY = 0F;
					this.leftArm.offsetY = 0F;
					this.rightArm.offsetY = 0F;
					this.head.offsetY = 0F;
					this.rightEar.offsetY = 0F;
					this.leftEar.offsetY = 0F;
					this.tail.offsetY = 0F;
				}
			}
		}

		else if (this.isPorch) {
			this.setAngle(this.pouch, this.bipedBody);
		}

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

		if (entity instanceof ISMMob) {

			ISMMob mob = (ISMMob) entity;
			this.head.rotationPointY -= 0.675F;

			switch (mob.getArm()) {
			case SPECIAL_MAGIC:
	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -6.0F;
	            this.leftArm.rotationPointZ = 0.0F;
	            this.leftArm.rotationPointX = 6.0F;
	            this.rightArm.rotationPointY = 3.5F;
	            this.leftArm.rotationPointY = 3.5F;
	            this.rightArm.rotateAngleX = MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.leftArm.rotateAngleX = MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.rightArm.rotateAngleZ = 1.25F;
	            this.leftArm.rotateAngleZ = -1.25F;
	            this.rightArm.rotateAngleY = 0F;
	            this.leftArm.rotateAngleY = 0F;
	            break;
			case RIGHT_MAGIC:
	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -7F;
	            this.leftArm.rotationPointZ = 0.0F;
	            this.leftArm.rotationPointX = 5.5F;
	            this.rightArm.rotationPointY = 3.5F;
	            this.leftArm.rotationPointY = 3.5F;
	            this.rightArm.rotateAngleX = MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.leftArm.rotateAngleX = MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.rightArm.rotateAngleZ = 2.675F;
	            this.leftArm.rotateAngleZ = -0.25F;
	            this.rightArm.rotateAngleY = 0F;
	            this.leftArm.rotateAngleY = 0F;
				break;
			case RIGHT_SHOT:
	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -7F;
	            this.leftArm.rotationPointZ = 0.0F;
	            this.leftArm.rotationPointX = 5.5F;
	            this.rightArm.rotationPointY = 3.5F;
	            this.leftArm.rotationPointY = 3.5F;
	            this.rightArm.rotateAngleX = -8.5F + MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.leftArm.rotateAngleX = MathHelper.cos(ageTick * 0.25F) * 0.05F;
	            this.rightArm.rotateAngleZ = 3.375F;
	            this.leftArm.rotateAngleZ = -0.25F;
	            this.rightArm.rotateAngleY = 0F;
	            this.leftArm.rotateAngleY = 0F;
				break;
			case RIGHT_SET:
	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -7F;
	            this.leftArm.rotationPointZ = -1.0F;
	            this.leftArm.rotationPointX = 7F;
	            this.rightArm.rotationPointY = 3.7F;
	            this.leftArm.rotationPointY = 3.7F;
	            this.rightArm.rotateAngleX = -7.85F + MathHelper.cos(ageTick * 0.05F) * 0.05F;
	            this.leftArm.rotateAngleX = -7.85F + MathHelper.cos(ageTick * 0.05F) * 0.05F;
	            this.rightArm.rotateAngleZ = 3.1F;
	            this.leftArm.rotateAngleZ = -3.1F;
	            this.rightArm.rotateAngleY = 0.15F;
	            this.leftArm.rotateAngleY = -0.5F;
				break;
			case BOTH_SET:
	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -7F;
	            this.leftArm.rotationPointZ = -1.0F;
	            this.leftArm.rotationPointX = 7F;
	            this.rightArm.rotationPointY = 3.7F;
	            this.leftArm.rotationPointY = 3.7F;
	            this.rightArm.rotateAngleX = -7.85F + MathHelper.cos(ageTick * 0.05F) * 0.05F;
	            this.leftArm.rotateAngleX = -7.85F + MathHelper.cos(ageTick * 0.05F) * 0.05F;
	            this.rightArm.rotateAngleZ = 3.1F;
	            this.leftArm.rotateAngleZ = -3.1F;
	            this.rightArm.rotateAngleY = 0F;
	            this.leftArm.rotateAngleY = 0F;
				break;
			case RIGHT_SWING:

				float swing = mob.swingAmout();

	            this.rightArm.rotationPointZ = 0.0F;
	            this.rightArm.rotationPointX = -7F;
	            this.leftArm.rotationPointZ = 0.0F;
	            this.leftArm.rotationPointX = 7.5F;
	            this.rightArm.rotationPointY = 3.7F;
	            this.rightArm.rotateAngleX = -8.5F - swing + MathHelper.cos(ageTick * 0.05F) * 0.05F;
	            this.leftArm.rotateAngleX = 3.135F;
	            this.rightArm.rotateAngleZ = 3.1F;
	            this.leftArm.rotateAngleZ = -3.15F;
	            this.rightArm.rotateAngleY = 0F;
	            this.leftArm.rotateAngleY = 0F;
				break;
			}
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
