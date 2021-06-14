package sweetmagic.init.entity.layer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.monster.EntitySandryon;

public class LayerWandSandryon extends LayerEffectBase<EntityLivingBase> {

	private static final ResourceLocation RUNE_TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/hexagram_pastelcolor.png");

	private static final ItemStack[] WAND = new ItemStack[] {
			new ItemStack(ItemInit.deuscrystal_wand_r),
			new ItemStack(ItemInit.deuscrystal_wand_b),
			new ItemStack(ItemInit.deuscrystal_wand_g),
			new ItemStack(ItemInit.deuscrystal_wand_y),
			new ItemStack(ItemInit.deuscrystal_wand_p)
	};

	private static final ItemStack[] SACRED = new ItemStack[] {
			new ItemStack(ItemInit.sacred_meteor_wand),
			new ItemStack(ItemInit.sacred_meteor_wand),
			new ItemStack(ItemInit.sacred_meteor_wand),
			new ItemStack(ItemInit.sacred_meteor_wand),
			new ItemStack(ItemInit.sacred_meteor_wand),
			new ItemStack(ItemInit.sacred_meteor_wand)
	};

	public LayerWandSandryon(RenderLivingBase<?> render) {
		super(render);
	}

	@Override
	public ResourceLocation getTexture(EntityLivingBase entity, float parTick) {
		return null;
	}

	@Override
	public boolean shouldRender(EntityLivingBase entity, float parTick) {
		return entity instanceof EntitySandryon && this.isMagic((EntitySandryon) entity);
	}

	// 魔法発動中か
	public boolean isMagic (EntitySandryon entity) {
		return entity.isWand() || entity.isInfiniteWand();
	}

	@Override
	public void doRenderLayer(EntityLivingBase entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
			 GlStateManager.enableBlend();
	         GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	         GlStateManager.color(1F, 1F, 1F, 1F);
		     this.renderEffect(scale * 18, (EntitySandryon) entity, parTick);
		}
	}

	private void renderEffect(float scale, EntitySandryon entity, float parTick) {

		float rotY = (entity.ticksExisted + parTick) / 6F;
		float rotX = -0.125F;
		float rotZ = 0;

		Minecraft mine = Minecraft.getMinecraft();
		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1F, 1F, 1F, 1F));
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		int count = entity.isInfiniteWand() ? 6 : Math.min(entity.getWandSize(), WAND.length);
		float pi = 180F / (float) Math.PI;
		ItemStack[] wandArray = entity.isInfiniteWand() ? SACRED : WAND;

		for (int i = 0; i < count; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
            GlStateManager.rotate(-45, 0F, 0.0F, 1.0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0F, -0.5F, 1F);
			IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(wandArray[i], entity.world, entity);
			mine.getRenderItem().renderItem(wandArray[i], ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
			GlStateManager.popMatrix();
		}

		if (entity.isInfiniteWand()) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(0F, 1.25F, 0F);
			scale *= 3F;
			GlStateManager.scale(scale, -scale, scale);
			this.renderWand(scale, entity, parTick);
			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}

	private void renderWand(float scale, EntityLivingBase entity, float parTick) {
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		Minecraft.getMinecraft().getTextureManager().bindTexture(RUNE_TEX);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0, -0.5F).tex(0, 0).endVertex();
		buffer.pos(0.5F, 0, -0.5F).tex(1, 0).endVertex();
		buffer.pos(0.5F, 0, 0.5F).tex(1, 1).endVertex();
		buffer.pos(-0.5F, 0, 0.5F).tex(0, 1).endVertex();
		tessellator.draw();
	}
}
