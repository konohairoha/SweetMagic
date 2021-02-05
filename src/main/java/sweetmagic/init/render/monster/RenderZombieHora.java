package sweetmagic.init.render.monster;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.model.ModelHora;
import sweetmagic.init.entity.monster.EntityZombieHora;

@SideOnly(Side.CLIENT)
public class RenderZombieHora extends RenderLiving<EntityZombieHora> {

	public static final ResourceLocation TEXTURES = new ResourceLocation("sweetmagic:textures/entity/hora.png");
    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

	public RenderZombieHora(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelHora(), 0.3F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this) {
            @Override
			protected void initArmor() {
                this.modelLeggings = new ModelHora(0.5F, true);
                this.modelArmor = new ModelHora(1.0F, true);
            }
        });
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityZombieHora entity) {
		return TEXTURES;
	}

	@Override
	public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

	public boolean shouldRender(EntityZombieHora liv, ICamera camera, double camX, double camY, double camZ) {

		if (super.shouldRender(liv, camera, camX, camY, camZ)) { return true; }

		EntityLivingBase entity1 = liv.suportEntity1;
		EntityLivingBase entity2 = liv.suportEntity2;

		if (entity1 != null) {
			boolean flag = this.shouldRender(liv, entity1, camera, camX, camY, camZ);
			if (flag) { return true;}
		}

		if (entity2 != null) {
			boolean flag = this.shouldRender(liv, entity2, camera, camX, camY, camZ);
			if (flag) { return true;}
		}

		return false;
	}

	public boolean shouldRender(EntityZombieHora liv, EntityLivingBase entity, ICamera camera, double camX, double camY, double camZ) {

		Vec3d vec3d = this.getPosition(entity, (double) entity.height * 0.5D, 1.0F);
		Vec3d vec3d1 = this.getPosition(liv, (double) liv.getEyeHeight(), 1.0F);

		if (camera.isBoundingBoxInFrustum( new AxisAlignedBB(vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y, vec3d.z))) {
			return true;
		}

		return false;
	}

	private Vec3d getPosition(EntityLivingBase entity, double par1, float par2) {
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) par2;
		double d1 = par1 + entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) par2;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) par2;
		return new Vec3d(d0, d1, d2);
	}

	public void doRender(EntityZombieHora liv, double x, double y, double z, float entityYaw, float partialTicks) {

		super.doRender(liv, x, y, z, entityYaw, partialTicks);

		EntityLivingBase entity1 = liv.suportEntity1;
		EntityLivingBase entity2 = liv.suportEntity2;

		if (entity1 != null) {
			this.doRender(liv, entity1, x, y, z, entityYaw, partialTicks);
		}

		if (entity2 != null) {
			this.doRender(liv, entity2, x, y, z, entityYaw, partialTicks);
		}
	}

	public void doRender(EntityZombieHora liv, EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks) {
		float f = 1F;
		Tessellator tesla = Tessellator.getInstance();
		BufferBuilder buf = tesla.getBuffer();
		this.bindTexture(GUARDIAN_BEAM_TEXTURE);
		GlStateManager.glTexParameteri(3553, 10242, 10497);
		GlStateManager.glTexParameteri(3553, 10243, 10497);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		float f2 = (float) liv.world.getTotalWorldTime() + partialTicks;
		float f3 = f2 * 0.5F % 1.0F;
		float f4 = liv.getEyeHeight();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + f4, (float) z);
		Vec3d vec3d = this.getPosition(entity, (double) entity.height * 0.5D, partialTicks);
		Vec3d vec3d1 = this.getPosition(liv, (double) f4, partialTicks);
		Vec3d vec3d2 = vec3d.subtract(vec3d1);
		double d0 = vec3d2.lengthVector() + 1.0D;
		vec3d2 = vec3d2.normalize();
		float f5 = (float) Math.acos(vec3d2.y);
		float f6 = (float) Math.atan2(vec3d2.z, vec3d2.x);
		GlStateManager.rotate((((float) Math.PI / 2F) + -f6) * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(f5 * (180F / (float) Math.PI), 1.0F, 0.0F, 0.0F);
		double d1 = (double) f2 * 0.05D * -1.5D;
		buf.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		float f7 = f * f;
		int j = 64 + (int) (f7 * 191.0F);
		int k = 32 + (int) (f7 * 191.0F);
		int l = 128 - (int) (f7 * 64.0F);
		double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * 0.282D;
		double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * 0.282D;
		double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
		double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
		double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * 0.282D;
		double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * 0.282D;
		double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * 0.282D;
		double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * 0.282D;
		double d12 = 0.0D + Math.cos(d1 + Math.PI) * 0.2D;
		double d13 = 0.0D + Math.sin(d1 + Math.PI) * 0.2D;
		double d14 = 0.0D + Math.cos(d1 + 0.0D) * 0.2D;
		double d15 = 0.0D + Math.sin(d1 + 0.0D) * 0.2D;
		double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
		double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
		double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
		double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
		double d22 = (double) (-1.0F + f3);
		double d23 = d0 * 2.5D + d22;
		buf.pos(d12, d0, d13).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
		buf.pos(d12, 0.0D, d13).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
		buf.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
		buf.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
		buf.pos(d16, d0, d17).tex(0.4999D, d23).color(j, k, l, 255).endVertex();
		buf.pos(d16, 0.0D, d17).tex(0.4999D, d22).color(j, k, l, 255).endVertex();
		buf.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
		buf.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
		double d24 = 0.0D;

		if (liv.ticksExisted % 2 == 0) {
			d24 = 0.5D;
		}

		buf.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
		buf.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
		buf.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
		buf.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
		tesla.draw();
		GlStateManager.popMatrix();
	}
}
