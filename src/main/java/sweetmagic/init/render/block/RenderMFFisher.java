package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.util.RenderUtils;

public class RenderMFFisher extends TileEntitySpecialRenderer<TileMFFisher> {

	private static final ResourceLocation RUNE_TEXTURE = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin.png");
	float size = 0.5F;

	@Override
	public void render(TileMFFisher te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderMahoujin(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();

	}

	protected void renderItem(TileMFFisher te, double x, double y, double z, float partialTicks) {

		Long worldTime = te.getWorld().getTotalWorldTime();
        float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, MathHelper.sin((worldTime + partialTicks) / 10.0F) * 0.15F + 0.2F, 0);
        GlStateManager.scale(this.size, this.size, this.size);
        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);

        ItemStack stack = new ItemStack(te.isFisher() ? Items.FISHING_ROD : ItemInit.machete);
        RenderUtils.renderItem(render, stack, 0, 0.55F, 0, 0, 1, 0, 0);
	}

	public void renderMahoujin (TileMFFisher te, double x, double y, double z, float partialTicks) {

		Long worldTime = te.getWorld().getTotalWorldTime();
        float rot = worldTime % 720;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(-rot, 0F, 1F, 0F);
		GlStateManager.translate(0F, -1.6F, 0F);
		float f = (float) te.getTime() + partialTicks;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.03F + 0.3, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(RUNE_TEXTURE);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5f, 0, -0.5f).tex(0, 0).endVertex();
		buffer.pos(0.5f, 0, -0.5f).tex(1, 0).endVertex();
		buffer.pos(0.5f, 0, 0.5f).tex(1, 1).endVertex();
		buffer.pos(-0.5f, 0, 0.5f).tex(0, 1).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
	}
}
