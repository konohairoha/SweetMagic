package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import sweetmagic.init.tile.magic.TileMFArcaneTable;
import sweetmagic.util.RenderUtils;

public class RenderMFArcaneTable extends TileEntitySpecialRenderer<TileMFArcaneTable> {

	private static final float size = 0.6F;

	@Override
	public void render(TileMFArcaneTable te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y - 0.5, z, partialTicks);
        GlStateManager.popMatrix();
	}

	protected void renderItem(TileMFArcaneTable te, double x, double y, double z, float partialTicks) {

        ItemStack charm = te.getWandItem(0);
        ItemStack heal = te.getWandItem(1);

        // スロットにアイテムないなら終了
        if (charm.isEmpty() && heal.isEmpty()) { return; }

        float face = 0;

        // ブロックの向きでアイテムの向きも変える
        switch (te.getFace()) {
        case NORTH:
        	face = 180F;
        	break;
        case SOUTH:
        	face = 0F;
        	break;
        case EAST:
        	face = 90F;
        	break;
        case WEST:
        	face = 270F;
        	break;
		default:
			break;
        }

        GlStateManager.rotate(face, 0.0F, 1.0F, 0.0F);

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(-90, 1F, 0F, 0F);

        // チャームのスロットにアイテムがあるなら
        if (!charm.isEmpty()) {
            RenderUtils.renderItem(render, charm, -0.3F, 0.2F, 1.4F, 0, 1, 0, 0F);
        }

        // チャームのスロットにアイテムがあるなら
        if (!heal.isEmpty()) {
            RenderUtils.renderItem(render, heal, -0.2F, -0.3F, 1.402F, 0, 1, 0, 0F);
        }
	}
}
