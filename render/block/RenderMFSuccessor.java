package sweetmagic.init.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.magic.TileMFSuccessor;
import sweetmagic.util.RenderUtils;

public class RenderMFSuccessor extends TileEntitySpecialRenderer<TileMFSuccessor> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/magia_successor.png");
	private static final ModelBase model = new ModelEnderCrystal(0.0F, false);
	private static final float size = 0.45F;
	private Long worldTime;

	@Override
	public void render(TileMFSuccessor te, double x, double y, double z, float parTick, int stage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		this.worldTime = te.getWorld().getTotalWorldTime();
		this.renderCrystal(te, x, stage, z, parTick);
        float rot = this.worldTime % 720;
		GlStateManager.rotate(rot, 0F, 1F, 0F);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, parTick);
        GlStateManager.popMatrix();
	}

	public void renderCrystal (TileMFSuccessor te, double x, double y, double z, float parTick) {

		float f = this.worldTime + parTick;
		this.bindTexture(TEX);
		float f1 = MathHelper.sin(f * 0.2F) / 4F + 0.75F;
		f1 = f1 * f1 + f1;

		GlStateManager.enableColorMaterial();
		this.model.render((Entity) null, 0.0F, f * 3F, 0.25F, 0.0F, 0.0F, 0.035F);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
	}

	protected void renderItem(TileMFSuccessor te, double x, double y, double z, float parTick) {

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

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		ItemStack sucStack = te.getWandItem(1);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);

		if (!sucStack.isEmpty()) {
			RenderUtils.renderItem(render, sucStack, 0F, 2.5F, 0F, 0F, 1F, 0F, 0F);
		}

		ItemStack oriStack = te.getWandItem(0);
		if (!oriStack.isEmpty()) {
			GlStateManager.rotate(rot, 0F, -1F, 0F);
			GlStateManager.rotate(-90, 1F, 0F, 0F);
			GlStateManager.scale(1.65F, 1.65F, 1.65F);
			RenderUtils.renderItem(render, oriStack, 0F, 0F, 0.625F, 0F, 1F, 0F, 0F);
		}
	}
}
