package sweetmagic.util;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class DrawHeleper {

	public static void drawTexturedRect(int x, int y, int width, int height) {
		drawTexturedRect(x, y, 0, 0, width, height, width, height);
	}

	public static void drawTexturedRect(int x, int y, int u, int v, int width, int height, int texWidth, int texHeight) {
		DrawHeleper.drawTexturedFlippedRect(x, y, u, v, width, height, texWidth, texHeight, false, false);
	}

	public static void drawTexturedFlippedRect(int x, int y, int u, int v, int width, int height, int texWidth, int texHeight, boolean flipX, boolean flipY) {

		float f = 1F / (float)texWidth;
		float f1 = 1F / (float)texHeight;

		int u1 = flipX ? u + width : u;
		int u2 = flipX ? u : u + width;
		int v1 = flipY ? v + height : v;
		int v2 = flipY ? v : v + height;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

		buffer.pos((double) (x), 		(double)(y + height), 0).tex((double)((float)(u1) * f), (double)((float)(v2) * f1)).endVertex();
		buffer.pos((double)(x + width), (double)(y + height), 0).tex((double)((float)(u2) * f), (double)((float)(v2) * f1)).endVertex();
		buffer.pos((double)(x + width), (double)(y), 		  0).tex((double)((float)(u2) * f), (double)((float)(v1) * f1)).endVertex();
		buffer.pos((double)(x), 		(double)(y), 		  0).tex((double)((float)(u1) * f), (double)((float)(v1) * f1)).endVertex();

		tessellator.draw();
	}

	public static void drawScaledStringToWidth(FontRenderer font, String text, float x, float y, float scale,
			int colour, float width, boolean centre, boolean alignR) {

		float textWidth = font.getStringWidth(text) * scale;
		float textHeight = font.FONT_HEIGHT * scale;

		if (textWidth > width) {
			scale *= width / textWidth;
		} else if (alignR) {
			x += width - textWidth;
		}

		if (centre) {
			y += (font.FONT_HEIGHT - textHeight) / 2;
		}

		DrawHeleper.drawScaledTranslucentString(font, text, x, y, scale, colour);
	}

	public static void drawScaledTranslucentString(FontRenderer font, String text, float x, float y, float scale, int colour) {

		GlStateManager.pushMatrix();
		GlStateManager.enableBlend();
		GlStateManager.scale(scale, scale, scale);
		x /= scale;
		y /= scale;
		font.drawStringWithShadow(text, x, y, colour);
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
	}
}
