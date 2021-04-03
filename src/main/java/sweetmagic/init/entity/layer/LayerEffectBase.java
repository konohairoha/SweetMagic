package sweetmagic.init.entity.layer;

import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class LayerEffectBase <T extends EntityLivingBase> implements LayerRenderer<T> {

	public final RenderLivingBase<?> renderer;
	public final int texWidth;
	public final int texHeight;

	public LayerEffectBase(RenderLivingBase<?> renderer, int texWidth, int texHeight) {
		this.texWidth = texWidth;
		this.texHeight = texHeight;
		this.renderer = renderer;
	}

	public LayerEffectBase(RenderLivingBase<?> renderer) {
		this(renderer, 16, 16);
	}

	public abstract boolean shouldRender(T entity, float parTick);
	public abstract ResourceLocation getTexture(T entity, float parTick);

	public ModelBase getModel(T entity, float parTick) {
		return this.renderer.getMainModel();
	}

	public boolean renderSecondLayer(T entity, float parTick) {
		return false;
	}

	protected void applyTextureSpaceTransformations(T entity, float parTick) {
	}

	@Override
	public void doRenderLayer(T entity, float limbSwing, float swingAmount, float parTick, float ageTick, float netHeadYaw, float headPitch, float scale) {
		if (this.shouldRender(entity, parTick)) {
			this.renderer.bindTexture(this.getTexture(entity, parTick));
			this.renderEntityModel(entity, limbSwing, swingAmount, parTick, ageTick, netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	}

	protected static int getBlockBrightnessForEntity(Entity entity) {

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(MathHelper.floor(entity.posX), 0, MathHelper.floor(entity.posZ));
		if (!entity.world.isBlockLoaded(pos)) { return 0; }

		pos.setY(MathHelper.floor(entity.posY + (double) entity.getEyeHeight()));
		return entity.world.getCombinedLight(pos, 0);
	}

	public void renderEntityModel(T entity, float limbSwing, float swingAmount, float parTick, float ageTick, float headYaw, float headPitch, float scale) {

		ModelBase model = this.getModel(entity, parTick);
		boolean flag = entity.isInvisible();
		GlStateManager.depthMask(!flag);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		float f = (float) (entity.ticksExisted + parTick) * 0.67F;
		GlStateManager.translate(f * 0.01F, f * 0.01F, 0.0F);
		GlStateManager.matrixMode(5888);
		GlStateManager.enableBlend();
		GlStateManager.color(0.5F, 0.5F, 0.5F, 1.0F);
		GlStateManager.disableLighting();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
		model.setModelAttributes(this.renderer.getMainModel());
		Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
		model.render(entity, limbSwing, swingAmount, ageTick, headYaw, headPitch, scale);
		Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
		GlStateManager.matrixMode(5890);
		GlStateManager.loadIdentity();
		GlStateManager.matrixMode(5888);
		GlStateManager.enableLighting();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(flag);
	}

	public static <T extends EntityLivingBase> void initialiseLayers(Class<T> entity, Function<RenderLivingBase<T>, LayerRenderer<? extends T>> layer) {

		for (Class<? extends Entity> c : Minecraft.getMinecraft().getRenderManager().entityRenderMap.keySet()) {

			if (entity.isAssignableFrom(c)) {
				Render<T> renderer = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(c);

				if (renderer instanceof RenderLivingBase<?>) {
					((RenderLivingBase<T>) renderer).addLayer(layer.apply((RenderLivingBase<T>) renderer));
				}
			}
		}

		if (entity.isAssignableFrom(EntityPlayer.class)) {
			for (RenderPlayer renderer : Minecraft.getMinecraft().getRenderManager().getSkinMap().values()) {
				renderer.addLayer(layer.apply((RenderLivingBase<T>) renderer));
			}
		}
	}

	public static void initialiseLayers(Function<RenderLivingBase<EntityLivingBase>, LayerRenderer<? extends EntityLivingBase>> layer) {
		initialiseLayers(EntityLivingBase.class, layer);
	}
}
