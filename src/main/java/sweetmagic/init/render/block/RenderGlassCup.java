package sweetmagic.init.render.block;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.chest.TileGlassCup;
import sweetmagic.init.tile.magic.TilePedalCreate.RGB;

public class RenderGlassCup extends TileEntitySpecialRenderer<TileGlassCup> {

	//マナテクスチャ
	private TextureAtlasSprite sprite = null;

	@Override
	public void render(TileGlassCup te, double x, double y, double z, float parTick, int stage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
        this.renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
	}

	// 液体描画
	protected void renderFluid(TileGlassCup te, double x, double y, double z) {

		//MFがない場合は描画しない
		ItemStack stack = te.getChestItem(0);
		if (stack.isEmpty()) { return; }

		List<Integer> color = this.getRGB(new Random(Item.getIdFromItem(stack.getItem())));
		GlStateManager.color(color.get(0) / 255F, color.get(1) / 255F, color.get(2) / 255F);

		if (this.sprite == null) {
			this.sprite = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("sweetmagic:block/water_still");
		}

        // テクスチャバインド
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		// GL11初期化
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);

        // ライト設定（これをしないと描画したものが暗くなる）
        RenderHelper.disableStandardItemLighting();

        // 描画のオブジェクトを透過する（液体なので透過する必要あり）
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // jsonと同じように16x16で位置を設定調整する
        //  x,y,z
		GlStateManager.translate(
			6.5F / 16F - 0.001F,
			0.5F / 16F - 0.001F,
			6.5F / 16F - 0.001F
		);

        // スプライトからUVを取得
        float minU = this.sprite.getMinU();
        float maxU = this.sprite.getMaxU();
        float minV = this.sprite.getMinV();
        float maxV = this.sprite.getMaxV();

        // 基準の高さ
        double vertX = 3D / 16D;
        double vertY = 10D / 16D;
        double vertZ = 3 / 16D;

        // 高さはさらに計算が必要
        double manaCap = 0.6D;
        vertY *= manaCap;

        // 最低値
        vertY = Math.max(0.001D, vertY);

        // 描画処理
        // 天板
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glEnd();

        // 裏面描画をしない
        GL11.glEnable(GL11.GL_CULL_FACE);

        // 前面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glEnd();

        // 右側
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(vertX, vertY, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glEnd();

        // 左側
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, 0);
        GL11.glEnd();

        // 背面
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, vertY, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, vertY, vertZ);
        GL11.glEnd();

        // 底板
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(minU, minV);
        GL11.glVertex3d(0, 0, 0);
        GL11.glTexCoord2d(maxU, minV);
        GL11.glVertex3d(vertX, 0, 0);
        GL11.glTexCoord2d(maxU, maxV);
        GL11.glVertex3d(vertX, 0, vertZ);
        GL11.glTexCoord2d(minU, maxV);
        GL11.glVertex3d(0, 0, vertZ);
        GL11.glEnd();

        // GL11終了処理
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glFlush();
	}

	public float getColor (Random rand) {
		return rand.nextInt(256) / 255F;
	}

	public List<Integer> getRGB (Random rand) {

		RGB color = null;

		switch (rand.nextInt(7)) {
		case 0:
			color = RGB.RED;
			break;
		case 1:
			color = RGB.B;
			break;
		case 2:
			color = RGB.C;
			break;
		case 3:
			color = RGB.D;
			break;
		case 4:
			color = RGB.E;
			break;
		case 5:
			color = RGB.F;
			break;
		case 6:
			color = RGB.G;
			break;

		}

		return Arrays.asList(color.r, color.g, color.b);

	}
}
