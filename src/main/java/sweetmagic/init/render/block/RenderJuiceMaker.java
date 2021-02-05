package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import sweetmagic.init.tile.cook.TileJuiceMaker;

public class RenderJuiceMaker extends TileEntitySpecialRenderer<TileJuiceMaker> {

	//マナテクスチャ
	protected TextureAtlasSprite spriteMana = null;

	@Override
	public void render(TileJuiceMaker te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        this.renderFluid(te, x, y, z);
        GlStateManager.popMatrix();
	}

	// 液体描画
	protected void renderFluid(TileJuiceMaker te, double x, double y, double z) {

		if (this.spriteMana == null) {
			this.spriteMana = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:blocks/water_still");
		}

		//MFがない場合は描画しない
		if (te.getWaterValue() == 0) { return; }

        //基準の高さ
        double vertX = 5.5 / 16.0;
        double vertY = 7.75 / 16.0;
        double vertZ = 3 / 16.0;

		switch (te.getFace()) {
		case NORTH:
	        GlStateManager.translate(5.25/16F - 0.001F, 1/16F - 0.001F, 11.25/16F - 0.001F);
			break;
		case SOUTH:
	        GlStateManager.translate(5.25/16F - 0.001F,1/16F - 0.001F,1.75/16F - 0.001F);
	        vertX = 5.5 / 16.0;
	        vertZ = 3 / 16.0;
			break;
		case EAST:
	        GlStateManager.translate( 1.75/16F - 0.001F, 1/16F - 0.001F, 5.25/16F - 0.001F);
	        vertX = 3 / 16.0;
	        vertZ = 5.5 / 16.0;
			break;
		case WEST:
	        GlStateManager.translate( 11.25/16F - 0.001F, 1/16F - 0.001F, 5.25/16F - 0.001F);
	        vertX = 3 / 16.0;
	        vertZ = 5.5 / 16.0;
			break;
		default:
			break;
		}

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

        //スプライトからUVを取得
        float minU = this.spriteMana.getMinU();
        float maxU = this.spriteMana.getMaxU();
        float minV = this.spriteMana.getMinV();
        float maxV = this.spriteMana.getMaxV();

        //高さはさらに計算が必要
        double manaCap = (double)te.getWaterValue() / (double)te.getWaterMaxValue();
        vertY *= manaCap;

        //最低値
        vertY = Math.max(0.001D, vertY);

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

        //前面
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

        //右側
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

        //左側
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

        //背面
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

        //底板
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

        //GL11終了処理
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glFlush();
	}
}
