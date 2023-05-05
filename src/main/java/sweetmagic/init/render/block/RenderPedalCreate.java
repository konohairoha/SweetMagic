package sweetmagic.init.render.block;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.tile.magic.TilePedalCreate;
import sweetmagic.util.RenderUtils;

public class RenderPedalCreate extends TileEntitySpecialRenderer<TilePedalCreate> {

	private static final ResourceLocation TEX1 = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin_w.png");
	private static final ResourceLocation TEX2 = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin3.png");
	private static final List<AxisAlignedBB> renderList = new ArrayList<>();
	private static final float size = 0.45F;
	private static final float mainSize = 0.45F;

	@Override
	public void render(TilePedalCreate te, double x, double y, double z, float parTick, int stage, float alpha) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		float rotY = (worldTime + parTick) / 90F;
		List<ItemStack> stackList = new ArrayList<>();

		try {
			stackList = te.getList();
		}

		catch (Throwable e) { }

		if (!te.isHaveBlock) {

			if (te.getTime() % 10 == 0) {
				te.checkBlock();
			}

			this.renderHasBlock(te, x, y, z);
		}

		if (stackList.isEmpty()) { return; }

		int count = stackList.size() - 1;
		float pi = 180F / (float) Math.PI;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		int nowTick = te.nowTick * ( !te.quickCraft() ? 1 : 2 );
		float posY = 2.45F + nowTick * 0.015F;

		for (int i = 0; i < count; i++) {

			ItemStack stack = stackList.get(i + 1);
			if (stack.isEmpty()) { continue; }

			GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
	        GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 10F) * 0.15F + 0.2F, 0);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)) + nowTick * 6.75F, 0F, 1F, 0F);
			GlStateManager.scale(size, size, size);
			GlStateManager.translate(1F - (0.0055 * nowTick) , 0F, 0F);
			RenderUtils.renderItem(render, stack, 0F, posY, 0F, 0F, 0F, 0F, 0F, false);
			GlStateManager.popMatrix();
		}

		ItemStack stack = te.getHandItem();
		if (stack.isEmpty()) { return; }

		rot = worldTime % 360;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(size, size, size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0F, posY, 0F, 0F, 1F, 0F, 0F, false);
        GlStateManager.popMatrix();

        if (te.getData() >= 2) {
        	this.renderMahoujin(te, x, y, z, parTick);
        }
	}

	public void renderHasBlock (TilePedalCreate te, double x, double y, double z) {

		// プレイヤーがいないなら描画しない
		if (!te.findPlayer) { return; }

		if (!te.isCrystal) {

			GlStateManager.pushMatrix();

			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			GlStateManager.translate(x, y - 0.95F, z + 1F);
			GlStateManager.color(1F, 1F, 1F, 0.5F);
			GlStateManager.disableDepth();

			Minecraft mc = Minecraft.getMinecraft();

			//ブロック描画
			mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

			//描画
			mc.getBlockRendererDispatcher().renderBlockBrightness(te.getCrystalAlpha().getDefaultState(), 1F);

			GlStateManager.enableDepth();
			GlStateManager.popMatrix();
		}

		if (!te.isEncha) {

			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);

			BlockPos pos = te.getPos();
			this.addAABB(pos.north(2), pos.getX(), pos.getY(), pos.getZ());
			this.addAABB(pos.south(2), pos.getX(), pos.getY(), pos.getZ());
			this.addAABB(pos.west(2), pos.getX(), pos.getY(), pos.getZ());
			this.addAABB(pos.east(2), pos.getX(), pos.getY(), pos.getZ());

			GlStateManager.popMatrix();
		}
	}

	public void addAABB (BlockPos pos, double x, double y, double z) {
		renderList.add(new AxisAlignedBB(pos, pos.add(1, 1, 1)).offset(-x, -y, -z).grow(-0.005D));
		RenderUtils.drawCube(renderList, true);
		renderList.clear();
	}

	public void renderMahoujin (TilePedalCreate te, double x, double y, double z, float parTick) {

		boolean isStar = te.getData() == 3;
		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.pushMatrix();
		GlStateManager.translate(x + 0.5F, y + (isStar ? 1.65F : 1.4F), z + 0.5F);
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(-rot, 0F, 1F, 0F);
		GlStateManager.color(0F, 0.882F, 1F, 1F);
		float f = (float) te.getTime() + parTick;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.03F + 0.3, 0.0F);

		float maxSize = isStar ? 2F : 1.25F;
		float addValue = isStar ? 0.01625F : 0.0125F;

		float size = Math.min(maxSize, 0.25F + (te.nowTick * addValue));
		GlStateManager.scale(size, size, size);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder buffer = tessellator.getBuffer();
		bindTexture(te.getData() == 2 ? TEX1 : TEX2);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0F, -0.5F).tex(0D, 0D).endVertex();
		buffer.pos(0.5F, 0F, -0.5F).tex(1D, 0D).endVertex();
		buffer.pos(0.5F, 0F, 0.5F).tex(1D, 1D).endVertex();
		buffer.pos(-0.5F, 0F, 0.5F).tex(0D, 1D).endVertex();
		tessellator.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();
	}
}
