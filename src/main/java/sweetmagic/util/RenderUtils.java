package sweetmagic.util;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import sweetmagic.init.base.BaseFaceBlock;

public class RenderUtils {

	@Deprecated
	public static float getFacingAngle(IBlockState state) {
		return getFacingAngle(state.getValue(BaseFaceBlock.FACING));
	}

	public static float getFacingAngle(EnumFacing face) {
        float angle;
        switch (face) {
            case NORTH:
                angle = 0;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = 90;
                break;
            case EAST:
            default:
                angle = -90;
                break;
        }
        return angle;
    }

    public static void renderItem(RenderItem renderer, ItemStack stack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!renderer.shouldRenderItemIn3D(stack)) {
            GlStateManager.rotate(180F, 0F, 1F, 0F);
        }
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        renderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

    public static void renderItem(RenderItem renderer, ItemStack stack, float x, float y, float z, float angle, float xr, float yr, float zr, boolean flag) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        renderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        GlStateManager.popMatrix();
    }

	// レンダー
	public static void drawCube(List<AxisAlignedBB> renderList, boolean isRed) {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		if (isRed) {
			GlStateManager.color(0F, 0.082F, 0.62F, 1F);
		}

		else {
			GlStateManager.color(1F, 1F, 0F, 1F);
		}

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
        GL11.glLineWidth(isRed ? 10F : 5F);
		wr.begin(2, DefaultVertexFormats.POSITION);

		for (AxisAlignedBB b : renderList) {

	        wr.pos(b.minX, b.minY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.minY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.minY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.minY, b.minZ).endVertex();

	        wr.pos(b.minX, b.maxY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.maxY, b.minZ).endVertex();

	        wr.pos(b.minX, b.minY, b.minZ).endVertex();
	        wr.pos(b.minX, b.maxY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.minY, b.minZ).endVertex();
	        wr.pos(b.maxX, b.maxY, b.minZ).endVertex();

	        wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
	        wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.minY, b.maxZ).endVertex();
	        wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
		}

		tess.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
	}

	// レンダー
	public static void drawCube(List<AxisAlignedBB> renderList, int type) {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);

		if (type == 2) {
	        GL11.glLineWidth(6F);
			GlStateManager.color(1F, 0.4667F, 0.2196F, 0.5F);
		}

		else {
	        GL11.glLineWidth(5F);
			GlStateManager.color(1F, 0.69F, 0.549F, 0.325F);
		}

		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(type, DefaultVertexFormats.POSITION);

		for (AxisAlignedBB b : renderList) {

			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();

			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();

			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();

			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
		}

		tess.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.disableBlend();
	}
}
