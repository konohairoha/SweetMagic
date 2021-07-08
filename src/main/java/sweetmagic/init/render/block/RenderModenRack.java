package sweetmagic.init.render.block;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.BlockModenRack;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.util.RenderUtils;

public class RenderModenRack extends TileEntitySpecialRenderer<TileModenRack> {

	private static final float size = 0.4F;
	private static final Item plate = Item.getItemFromBlock(BlockInit.plate);
	private static final Item woodPlate = Item.getItemFromBlock(BlockInit.wood_plate);
	private static final Item ironPlate = Item.getItemFromBlock(BlockInit.iron_plate);

	@Override
	public void render(TileModenRack te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		Block block = te.getBlock(te.getPos());
		if ( !(block instanceof BlockModenRack)) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

        try {
            this.renderItem(te, x, y - 0.5, z, partialTicks);
        }

		catch (Throwable e) { }

        GlStateManager.popMatrix();
	}

	protected void renderItem(TileModenRack te, double x, double y, double z, float partialTicks) {

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(0, 0, 0);
        int data = te.getRackData();

        if (data != 2 && data != 3) {
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
        case 3:
        	this.renderPlate(te, render, data);
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

            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
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

            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
        }
	}

	// 料理皿
	public void renderPlate (TileModenRack te, RenderItem render, int data) {

    	ItemStack stack = te.getChestItem(0);
    	if (stack.isEmpty()) { return; }

    	float posX = 0;
    	float posY = 0.55F;
    	float posZ = 0;

    	// 3DアイテムならY座標を低く
        if (render.shouldRenderItemIn3D(stack)) {
        	posY -= 0.2F;
        }

        Item item = stack.getItem();

        // スロットのアイテムが皿なら
        if (item == plate || item == woodPlate|| item == ironPlate) {

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

        	if (data == 3) {
            	posY -= 0.55F;
            	posZ += 0.2F;
                GlStateManager.rotate(270, 1F, 0F, 0.0F);
                GlStateManager.rotate(180, 0F, 0F, 1F);
        	}

            RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
        }
	}
}
