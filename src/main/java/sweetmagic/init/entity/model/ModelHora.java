package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.AbstractIllager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelHora extends ModelBiped {

	public ModelHora() {
		this(0.0F, false);
	}

	public ModelHora(float size, boolean par1) {
		super(size, 0.0F, 64, par1 ? 32 : 64);
	}

	public void setRotationAngles(float limbSwing, float swingAmount, float ageTick, float headYaw, float headPitch, float scale, Entity entity) {

		super.setRotationAngles(limbSwing, swingAmount, ageTick, headYaw, headPitch, scale, entity);
		boolean flag = entity instanceof EntityZombie && ((EntityZombie) entity).isArmsRaised();
		float f = MathHelper.sin(this.swingProgress * (float) Math.PI);
		float f1 = MathHelper.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightArm.rotateAngleY = -(0.1F - f * 0.6F);
		this.bipedLeftArm.rotateAngleY = 0.1F - f * 0.6F;
		float f2 = -(float) Math.PI / (flag ? 1.5F : 2.25F);
		this.bipedRightArm.rotateAngleX = f2;
		this.bipedLeftArm.rotateAngleX = f2;
		this.bipedRightArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		this.bipedLeftArm.rotateAngleX += f * 1.2F - f1 * 0.4F;
		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageTick * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageTick * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageTick * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageTick * 0.067F) * 0.05F;

        AbstractIllager.IllagerArmPose armpose = ((AbstractIllager)entity).getArmPose();

		if (armpose == AbstractIllager.IllagerArmPose.ATTACKING) {
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI);
			float f4 = MathHelper
					.sin((1.0F - (1.0F - this.swingProgress) * (1.0F - this.swingProgress)) * (float) Math.PI);
			this.bipedRightArm.rotateAngleZ = 0.0F;
			this.bipedLeftArm.rotateAngleZ = 0.0F;
			this.bipedRightArm.rotateAngleY = 0.15707964F;
			this.bipedLeftArm.rotateAngleY = -0.15707964F;

			if (((EntityLivingBase) entity).getPrimaryHand() == EnumHandSide.RIGHT) {
				this.bipedRightArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageTick * 0.09F) * 0.15F;
				this.bipedLeftArm.rotateAngleX = -0.0F + MathHelper.cos(ageTick * 0.19F) * 0.5F;
				this.bipedRightArm.rotateAngleX += f3 * 2.2F - f4 * 0.4F;
				this.bipedLeftArm.rotateAngleX += f3 * 1.2F - f4 * 0.4F;
			}

			else {
				this.bipedRightArm.rotateAngleX = -0.0F + MathHelper.cos(ageTick * 0.19F) * 0.5F;
				this.bipedLeftArm.rotateAngleX = -1.8849558F + MathHelper.cos(ageTick * 0.09F) * 0.15F;
				this.bipedRightArm.rotateAngleX += f3 * 1.2F - f4 * 0.4F;
				this.bipedLeftArm.rotateAngleX += f3 * 2.2F - f4 * 0.4F;
			}

			this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageTick * 0.09F) * 0.05F + 0.05F;
			this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageTick * 0.09F) * 0.05F + 0.05F;
			this.bipedRightArm.rotateAngleX += MathHelper.sin(ageTick * 0.067F) * 0.05F;
			this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageTick * 0.067F) * 0.05F;
		}

		else if (armpose == AbstractIllager.IllagerArmPose.SPELLCASTING) {
			this.bipedRightArm.rotationPointZ = 0.0F;
			this.bipedRightArm.rotationPointX = -5.0F;
			this.bipedLeftArm.rotationPointZ = 0.0F;
			this.bipedLeftArm.rotationPointX = 5.0F;
			this.bipedRightArm.rotateAngleX = MathHelper.cos(ageTick * 0.6662F) * 0.25F;
			this.bipedLeftArm.rotateAngleX = MathHelper.cos(ageTick * 0.6662F) * 0.25F;
			this.bipedRightArm.rotateAngleZ = 2.3561945F;
			this.bipedLeftArm.rotateAngleZ = -2.3561945F;
			this.bipedRightArm.rotateAngleY = 0.0F;
			this.bipedLeftArm.rotateAngleY = 0.0F;
		}

		else if (armpose == AbstractIllager.IllagerArmPose.BOW_AND_ARROW) {
			this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -0.9424779F + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleY = this.bipedHead.rotateAngleY - 0.4F;
			this.bipedLeftArm.rotateAngleZ = ((float) Math.PI / 2F);
		}
	}
}
