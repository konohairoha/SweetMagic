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

	private static final float size = 0.4F;

	@Override
	public void render(TileWandPedal te, double x, double y, double z, float parTick, int stege, float alpha) {

		if ( !te.findPlayer && te.renderTime >= 30) { return; }

    	ItemStack stack = te.getChestItem();
    	if (stack.isEmpty()) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, stack);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileWandPedal te, ItemStack stack) {

		int data = te.getData();
		float size = 1F;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		switch (data) {
		case 0:
			size = 1.125F;
			break;
		case 1:
			size = 0.8F;
			break;
		case 2:
			size = 0.45F;
			break;
		}

        GlStateManager.scale(size, size, size);
        float rot = 0;

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

        GlStateManager.rotate(rot, 0F, 1F, 0F);

        switch (data) {
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

        Item item = stack.getItem();

    	if (item instanceof IWand || item == ItemInit.mf_stuff) {
            GlStateManager.rotate(45, 0F, 0F, 1F);
            RenderUtils.renderItem(render, stack, 0.375F, 0.375F, 0F, 0, 1, 0, 0);
    	}

		else if (item instanceof ItemSword) {
            GlStateManager.rotate(-135, 0F, 0F, 1F);
            RenderUtils.renderItem(render, stack, -0.55F, -0.55F, 0F, 0, 1, 0, 0);
    	}

        else {
            RenderUtils.renderItem(render, stack, 0F, 0.65F, 0F, 0, 1, 0, 0, false);
        }
	}

	public void renderCorkBoard (TileWandPedal te, RenderItem render, ItemStack stack) {

        float posZ = -0.575F;

    	// 3DアイテムならY座標を低く
        if (render.shouldRenderItemIn3D(stack)) {
        	posZ *= -1;
            GlStateManager.rotate(180, 0F, 1F, 0F);
        }

        RenderUtils.renderItem(render, stack, 0F, 0.625F, posZ, 0, 1, 0, 0);
	}

	public void renderShopBoard (TileWandPedal te, RenderItem render, ItemStack stack) {
        GlStateManager.rotate(90, 0F, 1F, 0F);
        RenderUtils.renderItem(render, stack, 0F, 0.9F, 0F, 0, 1, 0, 0);
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
        this.renderItem(render, stack, 0F, posY, 0);

        if (!render.shouldRenderItemIn3D(stack)) {
            for (int i = 0; i < 3; i++) {
                GlStateManager.rotate(90, 0F, 1F, 0F);
                this.renderItem(render, stack, 0F, posY, 0);
            }
        }
	}

    public static void renderItem(RenderItem renderer, ItemStack stack, float x, float y, float z) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
//        GlStateManager.rotate(angle, xr, yr, zr);
//        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        renderer.renderItem(stack, ItemCameraTransforms.TransformType.GUI);
        RenderHelper.disableStandardItemLighting();
//        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }
}
