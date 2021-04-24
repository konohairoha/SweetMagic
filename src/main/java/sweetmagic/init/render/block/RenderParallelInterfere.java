package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBook;
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
import sweetmagic.init.tile.magic.TileParallelInterfere;
import sweetmagic.init.tile.magic.TileStardustWish;

public class RenderParallelInterfere extends TileEntitySpecialRenderer<TileParallelInterfere> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/magicbook.png");
	private static final ResourceLocation TEX_STAR = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/stardustbook.png");
	private static final ResourceLocation RUNE_TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/hexagram_pastelcolor.png");
	private static final ModelBook model = new ModelBook();

	public void render(TileParallelInterfere te, double x, double y, double z, float parTick, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		this.renderBook(te, x, y, z, parTick);
		GlStateManager.popMatrix();
	}

	public void renderBook (TileParallelInterfere te, double x, double y, double z, float parTick) {

		GlStateManager.translate((float) x + 0.5F, (float) y + 0.75F, (float) z + 0.5F);
		float f = (float) te.tickTime + parTick;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.01F + 0.25, 0.0F);
		float f1;

		for (f1 = te.bookRot - te.bookRotPre; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) { ; }

		while (f1 < -(float) Math.PI) {
			f1 += ((float) Math.PI * 2F);
		}

		float f2 = te.bookRotPre + f1 * parTick;
		GlStateManager.rotate(-f2 * (180F / (float) Math.PI), 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(80.0F, 0.0F, 0.0F, 1.0F);
		this.bindTexture(this.getTex(te));
		float f3 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * parTick + 0.25F;
		float f4 = te.pageFlipPrev + (te.pageFlip - te.pageFlipPrev) * parTick + 0.75F;
		f3 = (f3 - (float) MathHelper.fastFloor((double) f3)) * 1.6F - 0.3F;
		f4 = (f4 - (float) MathHelper.fastFloor((double) f4)) * 1.6F - 0.3F;

		if (f3 < 0F) { f3 = 0F; }
		if (f4 < 0F) { f4 = 0F; }
		if (f3 > 1F) { f3 = 1F; }
		if (f4 > 1F) { f4 = 1F; }

		float f5 = te.bookSpreadPrev + (te.bookSpread - te.bookSpreadPrev) * parTick;
		GlStateManager.enableCull();
		this.model.render((Entity) null, f, f3, f4, f5, 0.0F, 0.0625F);

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(100F, 0F, 0F, 1F);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		GlStateManager.translate(0F, 0.15F, 0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(RUNE_TEX);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0F, -0.5F).tex(0, 0).endVertex();
		buffer.pos(0.5F, 0F, -0.5F).tex(1, 0).endVertex();
		buffer.pos(0.5F, 0F, 0.5F).tex(1, 1).endVertex();
		buffer.pos(-0.5F, 0F, 0.5F).tex(0, 1).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
	}

	public ResourceLocation getTex (TileParallelInterfere te) {
		return te instanceof TileStardustWish ? TEX_STAR : TEX;
	}
}
