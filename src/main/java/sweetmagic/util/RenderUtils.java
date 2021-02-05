package sweetmagic.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
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
            GlStateManager.rotate(180f, 0f, 1f, 0f);
        }
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        renderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}