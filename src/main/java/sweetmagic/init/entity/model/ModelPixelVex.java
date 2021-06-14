package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPixelVex extends ModelBiped {

	protected ModelRenderer leftWing;
	protected ModelRenderer rightWing;

	public ModelPixelVex() {
		this(0.0F);
	}

	public ModelPixelVex(float size) {
		super(size, 0.0F, 64, 64);
		this.bipedLeftLeg.showModel = false;
		this.bipedHeadwear.showModel = false;
		this.bipedRightLeg = new ModelRenderer(this, 32, 0);
		this.bipedRightLeg.addBox(-1.0F, -1.0F, -2.0F, 6, 10, 4, 0.0F);
		this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F, 0.0F);
		this.rightWing = new ModelRenderer(this, 0, 32);
		this.rightWing.addBox(-20.0F, 0.0F, 0.0F, 20, 12, 1);
		this.leftWing = new ModelRenderer(this, 0, 32);
		this.leftWing.mirror = true;
		this.leftWing.addBox(0.0F, 0.0F, 0.0F, 20, 12, 1);
	}

	public void render(Entity entity, float limbSwing, float swingAmount, float ageTick, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, swingAmount, ageTick, netHeadYaw, headPitch, scale);
		this.rightWing.render(scale);
		this.leftWing.render(scale);
	}

	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageTick, float netHeadYaw, float headPitch, float scale, Entity entity) {

		super.setRotationAngles(limbSwing, limbSwingAmount, ageTick, netHeadYaw, headPitch, scale, entity);

		this.bipedRightLeg.rotateAngleX += ((float) Math.PI * 0.1F);
		this.rightWing.rotationPointZ = 2.0F;
		this.leftWing.rotationPointZ = 2.0F;
		this.rightWing.rotationPointY = 1.0F;
		this.leftWing.rotationPointY = 1.0F;
		this.rightWing.rotateAngleY = 0.47123894F + MathHelper.cos(ageTick * 0.8F) * (float) Math.PI * 0.05F;
		this.leftWing.rotateAngleY = -this.rightWing.rotateAngleY;
		this.leftWing.rotateAngleZ = -0.47123894F;
		this.leftWing.rotateAngleX = 0.47123894F;
		this.rightWing.rotateAngleX = 0.47123894F;
		this.rightWing.rotateAngleZ = 0.47123894F;
	}

	public int getModelVersion() {
		return 23;
	}
}
