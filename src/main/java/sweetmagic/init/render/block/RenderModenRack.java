package sweetmagic.init.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.blocks.BlockModenWallRack;
import sweetmagic.init.tile.chest.TileModenRack;
import sweetmagic.util.RenderUtils;

public class RenderModenRack extends TileEntitySpecialRenderer<TileModenRack> {

	private static final float size = 0.4F;
	private static final Item plate = Item.getItemFromBlock(BlockInit.plate);
	private static final Item woodPlate = Item.getItemFromBlock(BlockInit.wood_plate);
	private static final Item ironPlate = Item.getItemFromBlock(BlockInit.iron_plate);
	private static final Item tray = Item.getItemFromBlock(BlockInit.tray);
	private static final Item tray_wood = Item.getItemFromBlock(BlockInit.tray_wood);
	private static final String BOLD = TextFormatting.BOLD.toString();
	private static final String WHITE = TextFormatting.WHITE.toString();

	@Override
	public void render(TileModenRack te, double x, double y, double z, float parTick, int stage, float alpha) {

		if ( !te.findPlayer && te.renderTime >= 30) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        int data = 0;
        EnumFacing face = EnumFacing.NORTH;

		try {
			data = te.getRackData();
			face = this.renderItem(te, data);
		}

		catch (Throwable e) { }

        GlStateManager.popMatrix();

		if (data == 4 || data == 5) {
			this.renderItemName(te, face, data, (float) x, (float) y, (float) z);
		}
	}

	protected EnumFacing renderItem(TileModenRack te, int data) {

        float rot = 0;
        float size = (data != 2 && data != 3) ? this.size : 0.5F;
        EnumFacing face = te.getFace();

		// ブロックの向きでアイテムの向きも変える
		switch (face) {
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
		}

        GlStateManager.scale(size, size, size);
        GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

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
		case 4:
		case 5:
			this.renderBreadHolder(te, render, data);
			break;
		case 6:
			this.renderShowcase(te, render);
			break;
		case 7:
			this.renderBottleRack(te, render);
			break;
		case 8:
			this.renderCeilingShelf(te, render, data);
			break;
		case 9:
			this.rendeRangeHoodrack(te, render);
			break;
		case 10:
			this.rendePlateShaped(te, render);
			break;
		case 11:
			this.renderTray(te, render);
			break;
		case 12:
			this.renderIronShelf(te, render);
			break;
		case 13:
			this.renderFruitCrate(te, render);
			break;
		case 14:
			this.renderWoodenBox(te, render);
			break;
		case 15:
			this.renderFruitCrateBox(te, render);
			break;
		}

        return face;
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

		IBlockState state = te.getState(te.getPos());
		BlockModenWallRack block = (BlockModenWallRack) state.getBlock();
		float toal = !state.getValue(block.TOP) ? 0F : -1.55F;

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = -0.85F + 0.85F * (i % 3);
			float posY = 2.9F + toal;

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, 0F, 0, 1, 0, 0);
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
		if (item == plate || item == woodPlate || item == ironPlate) {

			GlStateManager.scale(4F, 4F, 4F);
			int amount = Math.min(9, stack.getCount());
			float pY = -0.05375F;

			// 皿の個数分回す
			for (int i = 0; i < amount; i++) {
				float y = pY + i * 0.05F;
				RenderUtils.renderItem(render, stack, posX, posY + y, posZ, 0, 1, 0, 0, false);
			}
		}

		else {

			if (data == 3) {
				posY -= 0.55F;
				posZ += 0.2F;
				GlStateManager.rotate(270, 1F, 0F, 0F);
				GlStateManager.rotate(180, 0F, 0F, 1F);
			}

			RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
		}
	}

	// パン置き
	public void renderBreadHolder (TileModenRack te, RenderItem render, int data) {

    	ItemStack stack = te.getChestItem(0);
    	if (stack.isEmpty()) { return; }

		float posY = data == 5 ? 0.435F : 0.75F;
		float addZ = data == 4 ? 0.35F : 0F;

		// 3DアイテムならY座標を低く
		if (render.shouldRenderItemIn3D(stack)) {
			posY -= 0.2F;
		}

		for (int posX = 0; posX <= 1; posX++) {
			for (int posZ = 0; posZ <= 1; posZ++) {
				RenderUtils.renderItem(render, stack, posX - 0.5F, posY, posZ - 0.5F - addZ, 0, 1, 0, 0, false);
			}
		}
	}

	// ショーケース
	public void renderShowcase (TileModenRack te, RenderItem render) {

		float posY = 1.65F;

		for (int i = 0; i <= te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.2F;
			}

			for (int posX = 0; posX <= 1; posX++) {
				for (int posZ = 0; posZ <= 1; posZ++) {
					RenderUtils.renderItem(render, stack, posX - 0.5F, posY - i * 1F, posZ - 0.25F, 0, 1, 0, 0, false);
				}
			}
		}
	}

	// ボトルラック
	public void renderBottleRack (TileModenRack te, RenderItem render) {

		float posY = 1.8F;

		for (int i = 0; i <= te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.2F;
			}

			for (int posX = 0; posX <= 2; posX++) {
				RenderUtils.renderItem(render, stack, posX * 0.75F - 0.75F, posY - i * 1.2F, 0.8F, 0, 1, 0, 0);
			}
		}
	}

	// 天井だな
	public void renderCeilingShelf (TileModenRack te, RenderItem render, int data) {

		float posY = 0.655F;
		float size = 3.5F;
		GlStateManager.scale(size, size, size);
		GlStateManager.rotate(270F, 0F, 1F, 0F);

		for (int i = 0; i <= te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			// 皿以外なら
			if (!stack.getItem().getUnlocalizedName().contains("plate")) {
				RenderUtils.renderItem(render, stack, 0F, posY - i * 0.36F, 0F, 0, 1, 0, 0, false);
			}

			// 皿の場合
			else {
				int amount = Math.min(stack.getCount(), 6);
				for (int k = 0; k < amount; k++) {
					RenderUtils.renderItem(render, stack, 0F, posY - i * 0.36F + k * 0.048F, 0F, 0, 1, 0, 0, false);
				}
			}
		}
	}

	// レンジフードラック
	public void rendeRangeHoodrack (TileModenRack te, RenderItem render) {

		float posY = 0F;
		float posX = 0F;
		float size = 2.75F;
		GlStateManager.scale(size, size, size);
		GlStateManager.rotate(270F, 0F, 0F, 1F);
		GlStateManager.rotate(90F, 1F, 0F, 0F);

		for (int i = 0; i <= te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			Item item = stack.getItem();
			String itemName = item.getUnlocalizedName();

			if (itemName.contains("frypan")) {
				posY = 0.64F;
				posX = -0.1F;
			}

			else if (itemName.contains("single_pot")) {
				posY = 0.59F;
				posX = -0F;
			}

			else {
				posY = 0.56F;
				posX = -0.1F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, i * 0.4F - 0.2F, 0, 1, 0, 0, false);
		}
	}

	// レンジフードラック
	public void rendePlateShaped (TileModenRack te, RenderItem render) {

		float posY = 0F;
		float posX = 0F;
		float size = 4F;
		GlStateManager.scale(size, size, size);
		GlStateManager.rotate(90F, 0F, 0F, 1F);

		for (int i = 0; i <= te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			RenderUtils.renderItem(render, stack, posX + 0.227F, posY + 0.42F - 0.0775F * i, 0, 0, 1, 0, 0, false);
		}
	}

	// トレー
	public void renderTray (TileModenRack te, RenderItem render) {

		ItemStack trayStack = te.getChestItem(0);

		if (!trayStack.isEmpty() && (trayStack.getItem() == tray || trayStack.getItem() == tray_wood)) {
			GlStateManager.scale(5F, 5F, 5F);
			int amount = Math.min(7, trayStack.getCount());
			float pY = 0.3125F;

			// 皿の個数分回す
			for (int i = 0; i < amount; i++) {
				float y = pY + 0.0625F * i;
				RenderUtils.renderItem(render, trayStack, 0F, y, 0F, 0, 1, 0, 0);
			}

			return;
		}

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = -0.5F + 1F * (i % 2);
			float posZ = -0.45F + ( i > 1 ? 0.8F : 0F);
			float posY = 0.6F;

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
		}
	}

	// モダンウォールラック
	public void renderIronShelf (TileModenRack te, RenderItem render) {

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = -0.85F + 0.85F * i;
			float posY = i >= 3 ? 2.9F : 1.7F;

			if (i >= 3) {
				posX = -0.85F + (i - 3) * 0.85F;
			}

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, 0.625F, 0, 1, 0, 0);
		}
	}

	// 果物箱
	public void renderFruitCrate (TileModenRack te, RenderItem render) {

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = i < 4 ? 0.5F : -0.5F;
			float posY = 0.85F + 0.2F * i + (i >= 4 ? - 0.8F : 0F);
			float posZ = -1F + 0.4F * i + (i >= 4 ? - 1.6F : 0F);

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
		}
	}

	// ツール入れ木箱
	public void renderWoodenBox (TileModenRack te, RenderItem render) {

        GlStateManager.rotate(225F, 0F, 0F, 1F);
		float size = 2.5F;
        GlStateManager.scale(size, size, size);

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = -0.55F;
			float posY = -0.5F;
			float posZ = -0.3F + i * 0.2F;

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
		}
	}

	// 果物箱(横)
	public void renderFruitCrateBox (TileModenRack te, RenderItem render) {

        // インベントリ分描画
        for (int i = 0; i < te.getInvSize(); i++) {

			ItemStack stack = te.getChestItem(i);
			if (stack.isEmpty()) { continue; }

			float posX = i < 4 ? 0.4F : -0.4F;
			float posY = 0.5F;
			float posZ = -0.625F + 0.4F * i + (i >= 4 ? - 1.6F : 0F);

			// 3DアイテムならY座標を低く
			if (render.shouldRenderItemIn3D(stack)) {
				posY -= 0.15F;
			}

			RenderUtils.renderItem(render, stack, posX, posY, posZ, 0, 1, 0, 0);
		}
	}

	public void renderItemName (TileModenRack te, EnumFacing face, int data, float x, float y, float z ) {

		ItemStack stack = te.getChestItem(0);
		if (stack.isEmpty()) { return; }

		float rot = 0;

		// ブロックの向きでアイテムの向きも変える
		switch (face) {
		case NORTH:
			rot = 180F;
			x += 0.5F;
			z += data == 5 ? 0.04F : 0.95F;
			break;
		case SOUTH:
			rot = 0F;
			x += 0.5F;
			z += data == 5 ? 0.9625F : 0.05F;
			break;
		case EAST:
			rot = 90F;
			x += data == 5 ? 0.9625F : 0.05F;
			z += 0.5F;
			break;
		case WEST:
			rot = 270F;
			x += data == 5 ? 0.04F : 0.95F;
			z += 0.5F;
			break;
		}

		String color = BOLD;

		if (data == 5) {
			y -= 0.6F;
			color = WHITE;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y + 0.725F, (float) z);
		GlStateManager.rotate(rot, 0F, 1F, 0F);

		FontRenderer font = this.getFontRenderer();
		GlStateManager.scale(0.005F, -0.0075F, 0.0075F);
		GlStateManager.glNormal3f(0F, 0F, -0.015F);
		GlStateManager.depthMask(false);

		String name = color + stack.getDisplayName();
		font.drawString(name, -font.getStringWidth(name) / 2, 0, 0x000000);

		GlStateManager.depthMask(true);
		//		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}
}
