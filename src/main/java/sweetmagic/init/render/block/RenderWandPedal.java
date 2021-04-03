package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.seed.SMSeed;
import sweetmagic.init.tile.chest.TileWandPedal;
import sweetmagic.util.RenderUtils;

public class RenderWandPedal extends TileEntitySpecialRenderer<TileWandPedal> {

	float size = 0.4F;

	@Override
	public void render(TileWandPedal te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileWandPedal te, double x, double y, double z, float partialTicks) {

    	ItemStack stack = te.getChestItem();
    	if (stack.isEmpty()) { return; }

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, 0, 0);

        switch (te.getData()) {
        case 0:
        	this.renderWandPedal(te, render, stack);
        	break;
        case 1:
        	this.renderCorkBoard(te, render, stack);
        	break;
        case 2:
        	this.renderShopBoard(te, render, stack);
        	break;
        case 3:
        	this.renderFlowerVase(te, render, stack);
        	break;
        }
	}

	public void renderWandPedal (TileWandPedal te, RenderItem render, ItemStack stack) {

        GlStateManager.scale(1.125F, 1.125F, 1.125F);

        float rot = 0;

        // ブロックの向きでアイテムの向きも変える
        switch (te.getFace()) {
        case NORTH:
        	rot = 0F;
        	break;
        case SOUTH:
        	rot = 180F;
        	break;
        case EAST:
        	rot = 270F;
        	break;
        case WEST:
        	rot = 90F;
        	break;
		default:
			break;
        }

        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);

        Item item = stack.getItem();

    	if (item instanceof IWand || item == ItemInit.mf_stuff) {
            GlStateManager.rotate(45, 0F, 0.0F, 1.0F);
            RenderUtils.renderItem(render, stack, 0.375F, 0.375F, 0F, 0, 1, 0, 0);
    	}

    	else if (item instanceof ItemSword){
            GlStateManager.rotate(-135, 0F, 0.0F, 1.0F);
            RenderUtils.renderItem(render, stack, -0.55F, -0.55F, 0F, 0, 1, 0, 0);
    	}

        else {
            RenderUtils.renderItem(render, stack, 0F, 0.65F, 0F, 0, 1, 0, 0);
        }
	}

	public void renderCorkBoard (TileWandPedal te, RenderItem render, ItemStack stack) {

        GlStateManager.scale(0.8F, 0.8F, 0.8F);

        float rot = 0;
        float posZ = -0.5325F;

        // ブロックの向きでアイテムの向きも変える
        switch (te.getFace()) {
        case NORTH:
        	rot = 180F;
        	break;
        case SOUTH:
        	rot = 0F;
        	break;
        case EAST:
        	rot = 90F;
        	break;
        case WEST:
        	rot = 270F;
        	break;
		default:
			break;
        }

    	// 3DアイテムならY座標を低く
        if (render.shouldRenderItemIn3D(stack)) {
        	rot += 180F;
        	posZ *= -1;
        }

        GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);

        RenderUtils.renderItem(render, stack, 0F, 0.6F, posZ, 0, 1, 0, 0);
	}

	public void renderShopBoard (TileWandPedal te, RenderItem render, ItemStack stack) {

        GlStateManager.scale(0.45F, 0.45F, 0.45F);

        float rot = 0;
        float posZ = 0F;

        // ブロックの向きでアイテムの向きも変える
        switch (te.getFace()) {
        case NORTH:
        	rot = 180F;
        	break;
        case SOUTH:
        	rot = 0F;
        	break;
        case EAST:
        	rot = 90F;
        	break;
        case WEST:
        	rot = 270F;
        	break;
		default:
			break;
        }

    	// 3DアイテムならY座標を低く
        if (render.shouldRenderItemIn3D(stack)) {
        	rot += 180F;
//        	posZ *= -1;
        }

        GlStateManager.rotate(rot - 90F, 0.0F, 1.0F, 0.0F);

        RenderUtils.renderItem(render, stack, 0F, 0.9F, posZ, 0, 1, 0, 0);
	}

	public void renderFlowerVase (TileWandPedal te, RenderItem render, ItemStack stack) {

		float size = 0.675F;
		float posY = 1F;
        GlStateManager.scale(size + 0.1, size, size + 0.1);

        // 種なら成長後の見た目に変える
        if (stack.getItem() instanceof SMSeed) {
        	SMSeed smSeed = (SMSeed) stack.getItem();
        	stack = new ItemStack(smSeed.state.getBlock());
        }

        GlStateManager.rotate(45, 0F, 1F, 0F);
        this.renderItem(render, stack, 0F, posY, 0, 0, 0, 0, 0);

        if (!render.shouldRenderItemIn3D(stack)) {
            for (int i = 0; i < 3; i++) {
                GlStateManager.rotate(90, 0F, 1F, 0F);
                this.renderItem(render, stack, 0F, posY, 0, 0, 0, 0, 0);
            }
        }
	}

    public static void renderItem(RenderItem renderer, ItemStack stack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, xr, yr, zr);
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        renderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
