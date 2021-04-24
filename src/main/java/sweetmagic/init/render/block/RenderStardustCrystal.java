package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.magic.TileStardustCrystal;

public class RenderStardustCrystal extends TileEntitySpecialRenderer<TileStardustCrystal> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/stardust_crystal.png");
	private static final ResourceLocation RUNE_TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/hexagram_pastelcolor.png");
	private static final ModelBase model = new ModelEnderCrystal(0.0F, false);
	public Long worldTime;

	@Override
	public void render(TileStardustCrystal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		this.worldTime = te.getWorld().getTotalWorldTime();
		this.renderCrystal(te, x, destroyStage, z, partialTicks);
        float rot = this.worldTime % 720;
		GlStateManager.rotate(rot, 0F, 1F, 0F);
        this.renderItem(te, x, -0.35, z);
        GlStateManager.popMatrix();
	}

	public void renderCrystal (TileStardustCrystal te, double x, double y, double z, float partialTicks) {

		float f = this.worldTime + partialTicks;
		this.bindTexture(TEX);
		float f1 = MathHelper.sin(f * 0.2F) / 4F + 0.75F;
		f1 = f1 * f1 + f1;

		GlStateManager.enableColorMaterial();
		this.model.render((Entity) null, 0.0F, f * 3F, f1 * 0.2F, 0.0F, 0.0F, 0.035F);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
	}

	protected void renderItem(TileStardustCrystal te, double x, double y, double z) {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.translate(0F, (float) y, 0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(RUNE_TEX);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0, -0.5F).tex(0, 0).endVertex();
		buffer.pos(0.5F, 0, -0.5F).tex(1, 0).endVertex();
		buffer.pos(0.5F, 0, 0.5F).tex(1, 1).endVertex();
		buffer.pos(-0.5F, 0, 0.5F).tex(0, 1).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
	}
}
