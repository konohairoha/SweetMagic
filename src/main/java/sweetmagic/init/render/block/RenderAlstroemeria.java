package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.plant.TileAlstroemeria;
import sweetmagic.util.RenderUtils;

public class RenderAlstroemeria extends TileEntitySpecialRenderer<TileAlstroemeria> {

	private static final float size = 0.5F;
	private static final ItemStack hand = new ItemStack(ItemInit.veil_darkness);
	private static final ItemStack book = new ItemStack(ItemInit.magic_book_scarlet);

	@Override
	public void render(TileAlstroemeria te, double x, double y, double z, float parTick, int stage, float alpha) {

		if (!te.isSummon) { return; }
        GlStateManager.pushMatrix();
        GlStateManager.enableLighting();
        GlStateManager.color(1F, 1F, 1F, 1F);
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, parTick);
        GlStateManager.popMatrix();

        if (te.nowTick >= 100) {

    		// エンティティの取得が出来なければ終了
    		EntityLivingBase entity = te.getEntity();
    		if (entity == null) { return; }

    		GlStateManager.pushMatrix();
    		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
    		this.renderMob(te, entity, x, y, z, parTick);
            GlStateManager.popAttrib();
    		GlStateManager.popMatrix();
        }
	}

	// アイテムのレンダー
	protected void renderItem(TileAlstroemeria te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        float posY = 2.55F + te.nowTick * 0.01225F;
		RenderUtils.renderItem(render, te.mobData == 1 ? this.hand : this.book, 0, posY, 0, 0, 1, 0, 0);
	}

	public void renderMob(TileAlstroemeria te, EntityLivingBase entity, double posX, double posY, double posZ, float parTick) {

        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		entity.setLocationAndAngles(posX, posY, posZ, 0F, 0F);
        GlStateManager.pushAttrib();
		GlStateManager.color(1F, 1F, 1F, (te.nowTick - 100) * 0.01F);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0D, 1.75D, 0D, 0F, 0F, false);
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
	}
}
