package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.util.RenderUtils;

public class RenderModenRack extends TileEntitySpecialRenderer<TileModenRack> {

	float size = 0.4F;

	@Override
	public void render(TileModenRack te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileModenRack te, double x, double y, double z, float partialTicks) {

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, 0, 0);

        int data = te.getRackData();

        if (data != 2) {
            GlStateManager.scale(this.size, this.size, this.size);
        }

        else {
            GlStateManager.scale(0.5F, 0.5F, 0.5F);
        }

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

        switch (data) {
        case 0:
        	this.renderRack(te, render);
        	break;
        case 1:
        	this.renderWallRack(te, render);
        	break;
        case 2:
        	this.renderPlate(te, render);
        	break;
        }
	}

	// モダンラック
	public void renderRack (TileModenRack te, RenderItem render) {

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

        	ItemStack stack = te.getChestItem(i);
        	if (stack.isEmpty()) { continue; }

        	float posX = -0.85F + 0.85F * (i % 3);
        	float posZ = -0.85F + 0.85F * (i / 3);

        	// 2段目なら調整
        	posZ += i >= 9 ? -2.6F : 0;
        	float posY = i >= 9 ? 1.65F : 2.9F;

        	// 3DアイテムならY座標を低く
            if (render.shouldRenderItemIn3D(stack)) {
            	posY -= 0.15F;
            }

            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0f);
        }
	}

	// モダンウォールラック
	public void renderWallRack (TileModenRack te, RenderItem render) {

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

        	ItemStack stack = te.getChestItem(i);
        	if (stack.isEmpty()) { continue; }

        	float posX = -0.85F + 0.85F * (i % 3);
        	float posY = 2.9F;
        	float posZ = 0;

        	// 3DアイテムならY座標を低く
            if (render.shouldRenderItemIn3D(stack)) {
            	posY -= 0.15F;
            }

            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0f);
        }
	}

	// 料理皿
	public void renderPlate (TileModenRack te, RenderItem render) {

    	ItemStack stack = te.getChestItem(0);
    	if (stack.isEmpty()) { return; }

    	float posX = 0;
    	float posY = 0.55F;
    	float posZ = 0;

    	// 3DアイテムならY座標を低く
        if (render.shouldRenderItemIn3D(stack)) {
        	posY -= 0.2F;
        }

        // スロットのアイテムが皿なら
        if (stack.getItem() == Item.getItemFromBlock(BlockInit.plate)) {

            GlStateManager.scale(4F, 4F, 4F);
            int amount = Math.min(9, stack.getCount());
            float pY = -0.05375F;

            // 皿の個数分回す
            for (int i = 0; i < amount; i++) {
            	float y = pY + i * 0.05F;
                RenderUtils.renderItem(render, stack, posX, posY + y, posZ, 0, 1, 0, 0);
            }
        }

        else {
            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
        }

	}
}
