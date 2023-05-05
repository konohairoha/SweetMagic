package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import sweetmagic.init.entity.projectile.EntityCushion;

public class ModelCushion extends ModelBase {

	private final ModelRenderer model;
	private final boolean isBaked;

	public ModelCushion(boolean baked) {
		this.isBaked = baked;
		this.textureWidth = 48;
		this.textureHeight = 16;

		this.model = new ModelRenderer(this, 0, 0);
		this.model.addBox(-8F, -1.125F, -8F, 12, 1, 12);
		this.model.setRotationPoint(0F, 0F, 0F);
		this.model.setTextureSize(48, 16);
	}

	public void render(float scale, EntityCushion entity) {
		this.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, scale);
	}

	@Override
	public void render(Entity entity, float limbSwing, float swingAmount, float ageTick, float headYaw, float headPitch, float scale) {
		this.setRotationAngles(limbSwing, swingAmount, ageTick, headYaw, headPitch, scale, entity);
		this.model.render(scale);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float limbSwing, float swingAmount, float ageTick, float netHeadYaw, float headPitch, float scale, Entity entity) {
		super.setRotationAngles(limbSwing, swingAmount, ageTick, netHeadYaw, headPitch, scale, entity);
	}

	public boolean isBaked() {
		return isBaked;
	}
}