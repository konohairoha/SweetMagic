package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.magic.TileCrystalCore;
import sweetmagic.util.RenderUtils;

public class RenderCrystalCore extends TileEntitySpecialRenderer<TileCrystalCore> {

	private static final ResourceLocation CORE = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/crystal_core.png");
	private static final ResourceLocation MAGIA = new ResourceLocation(SweetMagicCore.MODID, "textures/entity/magia_core.png");
	private static final ModelBase model = new ModelEnderCrystal(0.0F, false);
	private static final float size = 0.65F;
	private Long worldTime;

	@Override
	public void render(TileCrystalCore te, double x, double y, double z, float parTick, int stage, float alpha) {

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y - 0.8F, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, parTick);
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y - 0.25F, (float) z + 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		this.worldTime = te.getTime();
		this.renderCrystal(te, x, stage, z, parTick);
		GlStateManager.rotate( this.worldTime % 720, 0F, 1F, 0F);
        GlStateManager.popMatrix();

	}

	// クリスタル描画
	public void renderCrystal (TileCrystalCore te, double x, double y, double z, float parTick) {

		float f = this.worldTime + parTick;
		this.bindTexture(te.getData() == 0 ? CORE : MAGIA);
		float f1 = MathHelper.sin(f * 0.2F) / 4F + 0.75F;
		f1 = f1 * f1 + f1;

		GlStateManager.enableColorMaterial();
		this.model.render((Entity) null, 0.0F, f * 3F, 0.25F, 0.0F, 0.0F, 0.05F);
		GlStateManager.disableOutlineMode();
		GlStateManager.disableColorMaterial();
	}

	// アイテム描画
	protected void renderItem(TileCrystalCore te, double x, double y, double z, float parTick) {

		// スタック描画
		ItemStack sucStack = te.getChestItem();
		if (sucStack.isEmpty()) { return; }

		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(te.getTime() % 720, 0F, 1F, 0F);
		RenderUtils.renderItem(Minecraft.getMinecraft().getRenderItem(), sucStack, 0F, 2.5F, 0F, 0F, 1F, 0F, 0F);
	}
}
