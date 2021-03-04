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
import sweetmagic.init.BlockInit;

public enum RenderEffect {

	ICE {

		@Override
		public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
			return !firstPerson && entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(PotionFrost.MODIFIER_UUID) != null;
		}

		@Override
		public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer, double x, double y, double z, float parTick, boolean firstPerson) {

			int entityID = entity.getEntityId();

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			this.rand.setSeed(entityID * entityID * 3121 + entityID * 45238971);

			int numCubes = (int) (entity.height / 0.5F * 2);

			for (int i = 0; i < numCubes; i++) {

				double randDouble = this.rand.nextGaussian() * 0.2F;
				float randFloat = this.rand.nextFloat() * 360F;

				GlStateManager.pushMatrix();
				float dx = (float) (x + randDouble * entity.width);
				float dy = (float) (y + randDouble * entity.height) + entity.height / 2F;
				float dz = (float) (z + randDouble * entity.width);
				GlStateManager.translate(dx, dy, dz);

				float randFloat2 = this.rand.nextFloat() * 0.85F;
				GlStateManager.scale(randFloat2, randFloat2, randFloat2);

				GlStateManager.rotate(randFloat, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(randFloat, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(randFloat, 0.0F, 0.0F, 1.0F);
				Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(Blocks.PACKED_ICE.getDefaultState(), 1);
				GlStateManager.popMatrix();
			}
			GlStateManager.disableBlend();
		}
	},

	BABULE {

		@Override
		public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
			return !firstPerson && entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifier(PotionSM.PID) != null;
		}

		@Override
		public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer, double x, double y, double z, float parTick, boolean firstPerson) {

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			GlStateManager.pushMatrix();
			float dx = (float) (x - entity.width ) - 0.5F;
			float dz = (float) (z + entity.width) + 0.5F;
			GlStateManager.translate(dx, y + entity.height * 0.01D, dz);
			float size = entity.height * 1.25F;
			GlStateManager.scale(size, size, size);

			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlockBrightness(BlockInit.magic_circle.getDefaultState(), 1);
			GlStateManager.popMatrix();
			GlStateManager.disableBlend();
		}
	};

	public static final RenderEffect[] VALUES = values();
	public final Random rand = new Random();

	public boolean shouldRender(EntityLivingBase entity, boolean firstPerson) {
		return false;
	}

	public void render(EntityLivingBase entity, RenderLivingBase<? extends EntityLivingBase> renderer, double x, double y, double z, float parTick, boolean firstPerson) { }
}
