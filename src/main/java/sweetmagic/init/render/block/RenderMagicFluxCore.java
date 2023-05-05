package sweetmagic.init.render.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.magic.TileMagiaFluxCore;

public class RenderMagicFluxCore extends TileEntitySpecialRenderer<TileMagiaFluxCore> {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/magiaflux_core.png");
	private static final ModelBase model = new ModelEnderCrystal(0.0F, false);

	@Override
	public void render(TileMagiaFluxCore te, double x, double y, double z, float parTick, int stage, float alpha) {

		if (!te.findPlayer && te.tickTime >= 40) { return; }

        GlStateManager.pushMatrix();

        BlockPos pos = te.getPos().down();
        IBlockState under = te.getState(pos);
		Block block = under.getBlock();

		double addY = 0D;

		if (block != Blocks.AIR && !block.isFullBlock(under)) {
			addY = -1.675D + block.getBoundingBox(under, te.getWorld(), pos).maxY;
		}

        GlStateManager.translate((float) x + 0.5F, (float) y + 0.25F + addY, (float) z + 0.5F);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		this.renderCrystal(te, x, y, z, parTick);
        GlStateManager.popMatrix();
	}

	public void renderCrystal (TileMagiaFluxCore te, double x, double y, double z, float parTick) {

		float f = te.getWorld().getTotalWorldTime() + parTick;
		this.bindTexture(TEX);

		GlStateManager.enableColorMaterial();
		this.model.render((Entity) null, 0F, f * 3F, 0F, 0F, 0F, 0.02875F);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
	}
}
