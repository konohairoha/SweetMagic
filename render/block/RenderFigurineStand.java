package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import sweetmagic.init.tile.magic.TileFigurineStand;

public class RenderFigurineStand extends TileEntitySpecialRenderer<TileFigurineStand> {

	private static final float SIZE = 0.425F;

	@Override
	public void render(TileFigurineStand te, double x, double y, double z, float parTick, int stage, float alpha) {

		// エンティティの取得が出来なければ終了
		EntityLivingBase entity = te.getEntity();
		if (entity == null) { return; }

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		this.renderMob(te, entity, x, y, z, parTick);
        GlStateManager.popAttrib();
		GlStateManager.popMatrix();
	}

	public void renderMob(TileFigurineStand te, EntityLivingBase entity, double posX, double posY, double posZ, float parTick) {

		float rot = 0F;
		RenderManager render = Minecraft.getMinecraft().getRenderManager();

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
        }

		float addPosY = te.getSub() ? 0.35F : 0F;
        GlStateManager.rotate(rot, 0F, 1F, 0F);
		GlStateManager.scale(SIZE, SIZE, SIZE);
		entity.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
        GlStateManager.pushAttrib();
		render.renderEntity(entity, 0D, te.getPosY() + addPosY, 0D, 0F, 0F, false);

		// サブエンティティが取得出来なければ終了
		EntityLivingBase sub = te.getSubEntity();
		if (sub == null) { return; }

		sub.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
		render.renderEntity(sub, 0D, te.getPosY(), 0D, 0F, 0F, false);
	}
}
