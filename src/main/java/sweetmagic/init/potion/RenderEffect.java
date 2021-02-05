package sweetmagic.init.potion;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;

public enum RenderEffect {

	ICE {
		private final Random random = new Random();

		@Override
		public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
			return !firstPerson && entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(PotionFrost.MODIFIER_UUID) != null;
		}

		@Override
		public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer,
		                   double x, double y, double z, float partialTicks, boolean firstPerson) {

			int entityID = entity.getEntityId();

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			this.random.setSeed(entityID * entityID * 3121 + entityID * 45238971);

			int numCubes = (int) (entity.height / 0.5F * 2);

			for (int i = 0; i < numCubes; i++) {

				double randDouble = this.random.nextGaussian() * 0.2F;
				float randFloat = this.random.nextFloat() * 360F;

				GlStateManager.pushMatrix();
				float dx = (float) (x + randDouble * entity.width);
				float dy = (float) (y + randDouble * entity.height) + entity.height / 2F;
				float dz = (float) (z + randDouble * entity.width);
				GlStateManager.translate(dx, dy, dz);

				float randFloat2 = this.random.nextFloat() * 0.85F;
				GlStateManager.scale(randFloat2, randFloat2, randFloat2);

				GlStateManager.rotate(randFloat, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(randFloat, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(randFloat, 0.0F, 0.0F, 1.0F);
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.PACKED_ICE.getDefaultState(), 1);
				GlStateManager.popMatrix();
			}
			GlStateManager.disableBlend();
		}
	};

	public static final RenderEffect[] VALUES = values();

	public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
		return false;
	}

	public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer, double x, double y, double z, float partialTicks, boolean firstPerson) { }
}
