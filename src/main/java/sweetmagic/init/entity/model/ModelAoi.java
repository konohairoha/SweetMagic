package sweetmagic.init.entity.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelAoi extends ModelBase {

    public ModelRenderer Head;
    public ModelRenderer Body;
    public ModelRenderer Arms;
    public ModelRenderer rightLeg;
    public ModelRenderer leftLeg;

    public ModelAoi(float scale) {
        this(scale, 0.0F, 64, 64);
    }

    public ModelAoi(float scale, float par2, int width, int height) {

        this.Head = (new ModelRenderer(this)).setTextureSize(width, height);
        this.Head.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.Head.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, scale);

        this.Body = (new ModelRenderer(this)).setTextureSize(width, height);
        this.Body.setRotationPoint(0.0F, 0.0F + par2, 0.0F);
        this.Body.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, scale);
        this.Body.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F);

        this.Arms = (new ModelRenderer(this)).setTextureSize(width, height);
        this.Arms.setRotationPoint(0.0F, 0.0F + par2 + 2.0F, 0.0F);
        this.Arms.setTextureOffset(44, 22).addBox(-8.0F, -3.0F, -1.0F, 4, 12, 4, scale);
        this.Arms.setTextureOffset(44, 22).addBox(4.0F, -3.0F, -1.0F, 4, 12, 4, scale);

        this.rightLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
        this.rightLeg.setRotationPoint(-2.0F, 12.0F + par2, 0.0F);
        this.rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);

        this.leftLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(width, height);
        this.leftLeg.mirror = true;
        this.leftLeg.setRotationPoint(2.0F, 12.0F + par2, 0.0F);
        this.leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, scale);
    }

    @Override
	public void render(Entity entity, float swing, float swingAmount, float age, float headYaw, float headPitch, float scale) {
        this.setRotationAngles(swing, swingAmount, age, headYaw, headPitch, scale, entity);
        this.Head.render(scale);
        this.Body.render(scale);
        this.rightLeg.render(scale);
        this.leftLeg.render(scale);
        this.Arms.render(scale);
    }

    @Override
	public void setRotationAngles(float swing, float swingAmount, float age, float headYaw, float headPitch, float scale, Entity entity) {
        this.Head.rotateAngleY = headYaw * 0.017453292F;
        this.Head.rotateAngleX = headPitch * 0.017453292F;
        this.Arms.rotationPointY = 3.0F;
        this.Arms.rotationPointZ = -1.0F;
        this.rightLeg.rotateAngleX = MathHelper.cos(swing * 0.6662F) * 1.4F * swingAmount * 0.5F;
        this.leftLeg.rotateAngleX = MathHelper.cos(swing * 0.6662F + (float)Math.PI) * 1.4F * swingAmount * 0.5F;
        this.rightLeg.rotateAngleY = 0.0F;
        this.leftLeg.rotateAngleY = 0.0F;
    }
}
