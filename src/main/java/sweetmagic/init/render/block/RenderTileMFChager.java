package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import sweetmagic.init.tile.magic.TileMFChanger;

public class RenderTileMFChager extends TileEntitySpecialRenderer<TileMFChanger> {

	// MFテクスチャ
	protected TextureAtlasSprite sprite_1 = null;
	protected TextureAtlasSprite sprite_2 = null;
	protected TextureAtlasSprite sprite_3 = null;

	@Override
	public void render(TileMFChanger te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
        this.renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
	}

	// 液体描画
	protected void renderFluid(TileMFChanger te, double x, double y, double z) {

		int get = te.getMF();

		if (get >= 10000) {
			this.sprite_3 = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("sweetmagic:block/smstone_top_3");
			this.sprite_2 = null;
			this.sprite_1 = null;
		}

		else if (get >= 7000) {
			this.sprite_2 = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("sweetmagic:block/smstone_top_2");
			this.sprite_3 = null;
			this.sprite_1 = null;
		}

		else if (get >= 3000) {
			this.sprite_1 = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("sweetmagic:block/smstone_top_1");
			this.sprite_3 = null;
			this.sprite_2 = null;
		}

		//MFがない場合は描画しない
		if (get < 3000) { return; }

        //テクスチャバインド
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		//GL11初期化
		GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDisable(GL11.GL_CULL_FACE);

        //ライト設定（これをしないと描画したものが暗くなる）
        RenderHelper.disableStandardItemLighting();

        //描画のオブジェクトを透過する（液体なので透過する必要あり）
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        //jsonと同じように16x16で位置を設定調整する
        // x,y,z
        GlStateManager.translate(0, 0, 0);

        float minU = 0;
        float maxU = 0;
        float minV = 0;
        float maxV = 0;

        //スプライトからUVを取得
        if (this.sprite_3 != null) {
            minU = this.sprite_3.getMinU();
            maxU = this.sprite_3.getMaxU();
            minV = this.sprite_3.getMinV();
            maxV = this.sprite_3.getMaxV();
        }

        else if (this.sprite_1 != null) {
            minU = this.sprite_1.getMinU();
            maxU = this.sprite_1.getMaxU();
            minV = this.sprite_1.getMinV();
            maxV = this.sprite_1.getMaxV();
        }

        else if (this.sprite_2 != null) {
            minU = this.sprite_2.getMinU();
            maxU = this.sprite_2.getMaxU();
            minV = this.sprite_2.getMinV();
            maxV = this.sprite_2.getMaxV();
        }

        else {
        	return;
        }

        //基準の高さ
        double vertX = 1D;
        double vertY = 0.50015D;
        double vertZ = 1D;

        //描画処理
        //天板
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

        //裏面描画をしない
        GL11.glEnable(GL11.GL_CULL_FACE);

        //GL11終了処理
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glFlush();
	}
}
