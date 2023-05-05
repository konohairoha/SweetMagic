package sweetmagic.init.render.block;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMFFisher;
import sweetmagic.util.RenderUtils;

public class RenderMFFisher extends TileEntitySpecialRenderer<TileMFFisher> {

	private static final ResourceLocation RUNE_TEXTURE = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin.png");
	private static final ItemStack FISH = new ItemStack(Items.FISHING_ROD);
	private static final ItemStack MACH = new ItemStack(ItemInit.machete);
	private static final ItemStack LAVA = new ItemStack(ItemInit.alt_bucket_lava);
	private static final float SIZE = 0.5F;

	@Override
	public void render(TileMFFisher te, double x, double y, double z, float parTick, int state, float alpha) {

		// 起動していないまたはプレイヤーがいないなら終了
		if (!te.isActive || !te.findPlayer) { return; }

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		this.renderItem(te, x, y - 0.5, z, parTick);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		this.renderMahoujin(te, x, y - 0.5, z, parTick);
		GlStateManager.popMatrix();

	}

	protected void renderItem(TileMFFisher te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.SIZE, this.SIZE, this.SIZE);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
		int data = te.getData();

		// スクイザー以外なら
		if (data != 2) {
			ItemStack stack = ItemStack.EMPTY;

			switch (data) {
			case 0:
				stack = FISH;
				break;
			case 1:
				stack = MACH;
				break;
			case 3:
				stack = LAVA;
				break;
			}

			RenderUtils.renderItem(render, stack, 0, 0.55F, 0, 0, 1, 0, 0);
		}

		// スクイザーなら
		else {
			EntityLivingBase entity = te.getEntity();
			if (entity == null) { return; }

			float size = 0.75F;
			GlStateManager.scale(size, size, size);
			GlStateManager.rotate(90, 0.0F, 1.0F, 0.0F);
			entity.setLocationAndAngles(x, y, z, 0F, 0F);
			Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0D, 0D, 0D, 0F, 0, false);
		}

	}

	public void renderMahoujin (TileMFFisher te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(-rot, 0F, 1F, 0F);
		GlStateManager.translate(0F, -1.6F, 0F);
		float f = (float) te.getTime() + parTick;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.03F + 0.3, 0.0F);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(RUNE_TEXTURE);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0F, -0.5F).tex(0D, 0D).endVertex();
		buffer.pos(0.5F, 0F, -0.5F).tex(1D, 0D).endVertex();
		buffer.pos(0.5F, 0F, 0.5F).tex(1D, 1D).endVertex();
		buffer.pos(-0.5F, 0F, 0.5F).tex(0D, 1D).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
	}
}
