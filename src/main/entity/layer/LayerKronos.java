package sweetmagic.init.entity.layer;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBook;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.SweetMagicCore;
import sweetmagic.client.particle.ParticleOrb;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.util.ParticleHelper;

public class LayerKronos extends LayerEffectBase<EntityLivingBase> {

	private static final ResourceLocation TEX_STAR = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/stardustbook.png");
	private static final ModelBook BOOK = new ModelBook();
    public float pageFlip;
    public float pageFlipPrev, flipT, flipA, tRot;
    public float bookSpread, bookSpreadPrev, bookRot, bookRotPre;

	private static final List<ItemStack> itemList = Arrays.<ItemStack> asList(
		new ItemStack(ItemInit.mermaid_veil), new ItemStack(ItemInit.scorching_jewel), new ItemStack(ItemInit.witch_scroll)
	);

	public LayerKronos(RenderLivingBase<?> render) {
		super(render);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float amount, float parTick, float age, float netHeadYaw, float headPitch, float scale) {

		if (!this.shouldRender(entity, parTick)) { return; }

		EntityZombieKronos kronos = (EntityZombieKronos) entity;

		GlStateManager.color(1F, 1F, 1F, 1F);

		if (!kronos.isHalfHelth()) {
			this.renderAcceItem(kronos, parTick, scale * 10F);
		}

		else if (kronos.isQuarterHelth() && kronos.getIsSpecial()) {
			this.renderBook(kronos, parTick, scale);
		}
	}

	public void renderAcceItem (EntityZombieKronos entity, float parTick, float scale) {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float rotY = (entity.ticksExisted + parTick) / 9F;
		float rotX = -0.125F;
		float rotZ = 0;

		Minecraft mine = Minecraft.getMinecraft();
		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int count = entity.getAcceSize();
		float pi = 180F / (float) Math.PI;

		for (int i = 0; i < count; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
            GlStateManager.rotate(-45, 0F, 0F, 1F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0F, 2F, 0F);
			ItemStack stack = this.itemList.get(i);
			RenderItem render = mine.getRenderItem();
			IBakedModel model = render.getItemModelWithOverrides(stack, entity.world, entity);
			render.renderItem(stack, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}

	public void renderBook (EntityZombieKronos entity, float parTick, float scale) {

		GlStateManager.pushMatrix();

		this.setBookPrev(entity);
		float f = (float) entity.ticksExisted + parTick;
		GlStateManager.translate(0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F + -1.4, 0F);
		float f1;

		for (f1 = this.bookRot - this.bookRotPre; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) { ; }

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = this.bookRotPre + f1 * parTick;
		GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0F, 1F, 0F);
		GlStateManager.rotate(130F, 0F, 0F, 1F);
		GlStateManager.rotate(230F, 0F, 1F, 0F);
		GlStateManager.rotate(40F, 1F, 0F, 0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(this.getTex());
		float f3 = this.pageFlipPrev + (this.pageFlip - this.pageFlipPrev) * parTick + 0.25F;
		float f4 = this.pageFlipPrev + (this.pageFlip - this.pageFlipPrev) * parTick + 0.75F;
		f3 = (f3 - (float) MathHelper.fastFloor((double) f3)) * 1.6F - 0.3F;
		f4 = (f4 - (float) MathHelper.fastFloor((double) f4)) * 1.6F - 0.3F;

		if (f3 < 0F) { f3 = 0F; }
		if (f4 < 0F) { f4 = 0F; }
		if (f3 > 1F) { f3 = 1F; }
		if (f4 > 1F) { f4 = 1F; }

		float f5 = this.bookSpreadPrev + (this.bookSpread - this.bookSpreadPrev) * parTick;
		GlStateManager.enableCull();
		this.BOOK.render((Entity) null, f, f3, f4, f5, 0F, 0.0625F);

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		if (entity.getIsSpecial()) {

			World world = entity.world;
			Random rand = world.rand;

			if (rand.nextInt(3) != 0) { return; }

//			for (int i = 0; i < 4; ++i) {

			float randX = (rand.nextFloat() - rand.nextFloat()) * 2.75F;
			float randY = (rand.nextFloat() - rand.nextFloat()) * 2.75F;
			float randZ = (rand.nextFloat() - rand.nextFloat()) * 2.75F;

			float x = (float) (entity.posX + randX);
			float y = (float) (entity.posY + 2.75F + randY);
			float z = (float) (entity.posZ + randZ);
			float pX = (float) (entity.posX - x);
			float pY = (float) (entity.posY - y + 2.75F);
			float pZ = (float) (entity.posZ - z);
			float xSpeed = pX * 0.1175F;
			float ySpeed = pY * 0.1175F;
			float zSpeed = pZ * 0.1175F;

			Particle effect = ParticleOrb.create(world, x, y, z, xSpeed, ySpeed, zSpeed, rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
			ParticleHelper.spawnParticl().addEffect(effect);
		}
	}

	public void setBookPrev (EntityZombieKronos entity) {

		this.bookSpreadPrev = this.bookSpread;
		this.bookRotPre = this.bookRot;

		if (entity.getIsSpecial()) {
			double d0 = entity.posX - (double) ((float) entity.posX + 0.5F);
			double d1 = entity.posZ - (double) ((float) entity.posZ + 0.5F);
			this.tRot = (float) MathHelper.atan2(d1, d0);
			this.bookSpread += 0.1F;

			Random rand = entity.world.rand;

			if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = this.flipT;

				while (true) {
					this.flipT += (float) (rand.nextInt(4) - rand.nextInt(4));
					if (f1 != this.flipT) { break; }
				}
			}
		}

		else {
			this.tRot += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while (this.bookRot >= (float) Math.PI) {
			this.bookRot -= ((float) Math.PI * 2F);
		}

		while (this.bookRot < -(float) Math.PI) {
			this.bookRot += ((float) Math.PI * 2F);
		}

		while (this.tRot >= (float) Math.PI) {
			this.tRot -= ((float) Math.PI * 2F);
		}

		while (this.tRot < -(float) Math.PI) {
			this.tRot += ((float) Math.PI * 2F);
		}

		float f2;

		for (f2 = this.tRot - this.bookRot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) { ; }

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		this.bookRot += f2 * 0.4F;
		this.bookSpread = MathHelper.clamp(this.bookSpread, 0F, 1F);
		this.pageFlipPrev = this.pageFlip;
		float f = (this.flipT - this.pageFlip) * 0.4F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		this.flipA += (f - this.flipA) * 0.9F;
		this.pageFlip += this.flipA;
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity instanceof EntityZombieKronos;
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	public ResourceLocation getTex () {
		return TEX_STAR;
	}
}
