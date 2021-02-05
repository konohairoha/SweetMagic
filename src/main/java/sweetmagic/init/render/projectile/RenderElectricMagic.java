package sweetmagic.init.render.projectile;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;

@SideOnly(Side.CLIENT)
public class RenderElectricMagic  extends RenderLightMagic {

	public static final ResourceLocation TEX = new ResourceLocation("textures/entity/beacon_beam.png");

	public RenderElectricMagic(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	public void renderAnimetion (EntityBaseMagicShot entity, double x, double y, double z, float entityYaw, float parTick) {

		if (!entity.inGround) { return; }

        GlStateManager.pushMatrix();
		GlStateManager.alphaFunc(516, 0.1F);
		this.bindTexture(TEX);
		GlStateManager.disableFog();
		this.renderBeamSegment(x, y, z, parTick, 1F, entity.world.getTotalWorldTime(), -2, 24, EnumDyeColor.YELLOW.getColorComponentValues(), 0.15D, 0.25D);
		GlStateManager.enableFog();
        GlStateManager.popMatrix();
	}

	public void renderBeamSegment(double x, double y, double z, float parTick, double scale,
			float time, int yOffset, int height, float[] colors, double beamRadius, double glowRadius) {

		int i = yOffset + height;
		GlStateManager.glTexParameteri(3553, 10242, 10497);
		GlStateManager.glTexParameteri(3553, 10243, 10497);
		GlStateManager.disableLighting();
		GlStateManager.disableCull();
		GlStateManager.disableBlend();
		GlStateManager.depthMask(true);
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buil = tessellator.getBuffer();
		double d0 = time + parTick;
		double d1 = height < 0 ? d0 : -d0;
		double d2 = MathHelper.frac(d1 * 0.2D - (double) MathHelper.floor(d1 * 0.1D));
		float f = colors[0];
		float f1 = colors[1];
		float f2 = colors[2];
		double d3 = d0 * 0.025D * -1.5D;
		double d4 = 0.5D + Math.cos(d3 + 2.356194490192345D) * beamRadius;
		double d5 = 0.5D + Math.sin(d3 + 2.356194490192345D) * beamRadius;
		double d6 = 0.5D + Math.cos(d3 + (Math.PI / 4D)) * beamRadius;
		double d7 = 0.5D + Math.sin(d3 + (Math.PI / 4D)) * beamRadius;
		double d8 = 0.5D + Math.cos(d3 + 3.9269908169872414D) * beamRadius;
		double d9 = 0.5D + Math.sin(d3 + 3.9269908169872414D) * beamRadius;
		double d10 = 0.5D + Math.cos(d3 + 5.497787143782138D) * beamRadius;
		double d11 = 0.5D + Math.sin(d3 + 5.497787143782138D) * beamRadius;
		double d13 = 1.0D;
		double d14 = -1.0D + d2;
		double d15 = (double) height * scale * (0.5D / beamRadius) + d14;
		buil.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buil.pos(x + d4, y + (double) i, z + d5).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d4, y + (double) yOffset, z + d5).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d6, y + (double) yOffset, z + d7).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d6, y + (double) i, z + d7).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d10, y + (double) i, z + d11).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d10, y + (double) yOffset, z + d11).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d8, y + (double) yOffset, z + d9).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d8, y + (double) i, z + d9).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d6, y + (double) i, z + d7).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d6, y + (double) yOffset, z + d7).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d10, y + (double) yOffset, z + d11).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d10, y + (double) i, z + d11).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d8, y + (double) i, z + d9).tex(1.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d8, y + (double) yOffset, z + d9).tex(1.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d4, y + (double) yOffset, z + d5).tex(0.0D, d14).color(f, f1, f2, 1.0F).endVertex();
		buil.pos(x + d4, y + (double) i, z + d5).tex(0.0D, d15).color(f, f1, f2, 1.0F).endVertex();
		tessellator.draw();
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);
		GlStateManager.depthMask(false);
		d3 = 0.5D - glowRadius;
		d4 = 0.5D - glowRadius;
		d5 = 0.5D + glowRadius;
		d6 = 0.5D - glowRadius;
		d7 = 0.5D - glowRadius;
		d8 = 0.5D + glowRadius;
		d9 = 0.5D + glowRadius;
		d10 = 0.5D + glowRadius;
		d11 = 0.0D;
		d13 = -1.0D + d2;
		d14 = (double) height * scale + d13;
		buil.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
		buil.pos(x + d3, y + (double) i, z + d4).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d3, y + (double) yOffset, z + d4).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d5, y + (double) yOffset, z + d6).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d5, y + (double) i, z + d6).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d9, y + (double) i, z + d10).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d9, y + (double) yOffset, z + d10).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d7, y + (double) yOffset, z + d8).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d7, y + (double) i, z + d8).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d5, y + (double) i, z + d6).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d5, y + (double) yOffset, z + d6).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d9, y + (double) yOffset, z + d10).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d9, y + (double) i, z + d10).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d7, y + (double) i, z + d8).tex(1.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d7, y + (double) yOffset, z + d8).tex(1.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d3, y + (double) yOffset, z + d4).tex(0.0D, d13).color(f, f1, f2, 0.125F).endVertex();
		buil.pos(x + d3, y + (double) i, z + d4).tex(0.0D, d14).color(f, f1, f2, 0.125F).endVertex();
		tessellator.draw();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.depthMask(true);
	}
}
