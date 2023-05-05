package sweetmagic.init.render.monster;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.model.ModelPixelVex;
import sweetmagic.init.entity.monster.EntityEvilCrystal;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.util.RenderUtils;

public class RenderEvilCrystal extends RenderLiving<EntityEvilCrystal> {

    private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/models/armor/empty_layer_1.png");
    private static final ResourceLocation BEAM = new ResourceLocation("textures/entity/guardian_beam.png");
	private static final ResourceLocation CORE = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/magia_successor.png");
	private static final ItemStack STACK = new ItemStack(ItemInit.aether_crystal_shard);
	private static final ModelBase model = new ModelEnderCrystal(0.0F, false);
	private static final float size = 0.5F;

	public RenderEvilCrystal(RenderManager render) {
		super(render, new ModelPixelVex(), 0.3F);
		this.shadowSize = 0F;
	}

	protected ResourceLocation getEntityTexture(EntityEvilCrystal entity) {
		return TEX;
	}

	public void doRender(EntityEvilCrystal entity, double x, double y, double z, float entityYaw, float parTick) {

		super.doRender(entity, x, y, z, entityYaw, parTick);

		float f1 = MathHelper.sin(entity.ticksExisted * 0.2F) / 4F + 0.75F;
		f1 = f1 * f1 + f1;

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 0.35F, (float) z);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.renderCrystal(entity, x, y, z, parTick);
		GlStateManager.rotate( entity.ticksExisted % 720, 0F, 1F, 0F);
        GlStateManager.popMatrix();

		if (!entity.isEntityAlive()) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y - 0.4F + f1 * 0.1F, (float) z);
        this.renderItem(entity, x, y, z, parTick);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + 1.35F - f1 * 0.15F, (float) z);
		GlStateManager.rotate(33F, 1F, 0F, 0F);
        this.renderEffect(entity, parTick, 0.5F, false);
		GlStateManager.rotate(-66F, 1F, 0F, 0F);
        this.renderEffect(entity, parTick, 0.5F, true);
        GlStateManager.popMatrix();

		EntityLivingBase target = entity.getAttackTarget();

		if (target != null && entity.canEntityBeSeen(target)) {


			float f = entity.ticksExisted;
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buf = tessellator.getBuffer();
			this.bindTexture(BEAM);
			GlStateManager.glTexParameteri(3553, 10242, 10497);
			GlStateManager.glTexParameteri(3553, 10243, 10497);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.disableBlend();
			GlStateManager.depthMask(true);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
					GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
			float f2 = (float) entity.world.getTotalWorldTime() + parTick;
			float f3 = f2 * 0.5F % 1.0F;
			float f4 = entity.getEyeHeight();
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x, (float) y + f4, (float) z);
			Vec3d vec3d = this.getPosition(target, (double) target.height * 0.5D, parTick);
			Vec3d vec3d1 = this.getPosition(entity, (double) f4, parTick);
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
			double d4 = Math.cos(d1 + 2.356194490192345D) * 0.282D;
			double d5 = Math.sin(d1 + 2.356194490192345D) * 0.282D;
			double d6 = Math.cos(d1 + (Math.PI / 4D)) * 0.282D;
			double d7 = Math.sin(d1 + (Math.PI / 4D)) * 0.282D;
			double d8 = Math.cos(d1 + 3.9269908169872414D) * 0.282D;
			double d9 = Math.sin(d1 + 3.9269908169872414D) * 0.282D;
			double d10 = Math.cos(d1 + 5.497787143782138D) * 0.282D;
			double d11 = Math.sin(d1 + 5.497787143782138D) * 0.282D;
			double d12 = Math.cos(d1 + Math.PI) * 0.2D;
			double d13 = Math.sin(d1 + Math.PI) * 0.2D;
			double d14 = Math.cos(d1 + 0.0D) * 0.2D;
			double d15 = Math.sin(d1 + 0.0D) * 0.2D;
			double d16 = Math.cos(d1 + (Math.PI / 2D)) * 0.2D;
			double d17 = Math.sin(d1 + (Math.PI / 2D)) * 0.2D;
			double d18 = Math.cos(d1 + (Math.PI * 3D / 2D)) * 0.2D;
			double d19 = Math.sin(d1 + (Math.PI * 3D / 2D)) * 0.2D;
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
			if (entity.ticksExisted % 2 == 0) { d24 = 0.5D; }

			buf.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			buf.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
			buf.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
			buf.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
			tessellator.draw();
			GlStateManager.popMatrix();
		}
	}

	// クリスタル描画
	public void renderCrystal (EntityEvilCrystal entity, double x, double y, double z, float parTick) {

		float f = entity.ticksExisted;
		this.bindTexture(CORE);
		float f1 = MathHelper.sin(f * 0.2F) / 4F + 0.75F;
		f1 = f1 * f1 + f1;

		GlStateManager.scale(0.75F, 0.75F, 0.75F);
		GlStateManager.enableColorMaterial();
		this.model.render((Entity) null, 0.0F, f * 3F, f1 * 0.085F, 0.0F, 0.0F, 0.05F);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
	}

	// アイテム描画
	protected void renderItem(EntityEvilCrystal entity, double x, double y, double z, float parTick) {

		int data = entity.getData();
		if (data < 0) { return; }

		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(entity.ticksExisted % 720, 0F, 1F, 0F);
		RenderUtils.renderItem(Minecraft.getMinecraft().getRenderItem(), new ItemStack(entity.getItem()), 0F, 2.5F, 0F, 0F, 1F, 0F, 0F);
	}

	private Vec3d getPosition(EntityLivingBase entity, double par1, float par2) {
		double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double) par2;
		double d1 = par1 + entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double) par2;
		double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double) par2;
		return new Vec3d(d0, d1, d2);
	}

	protected void preRenderCallback(EntityPixieVex entity, float parTick) {
		GlStateManager.scale(1F, 1F, 1F);
	}

	private void renderEffect(EntityEvilCrystal entity, float parTick, float scale, boolean isReverse) {

		float rotY = (entity.ticksExisted + parTick) / 8F;
		float rotX = -0.125F;
		float rotZ = 0;

		Minecraft mine = Minecraft.getMinecraft();
		IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(STACK, entity.world, entity);

		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		int count = 6;
		float pi = 180F / (float) Math.PI;
		int reverse = isReverse ? -1 : 1;

		for (int i = 0; i < count; i++) {

			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (265 / count)), 0F, 1F * reverse, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.translate(0F, -0.5F, 1.25F);
			mine.getRenderItem().renderItem(STACK, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}
}
