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
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.ItemInit;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.magic.TileMFHarvester;
import sweetmagic.util.RenderUtils;

@SideOnly(Side.CLIENT)
public class RenderMFHarvester extends TileEntitySpecialRenderer<TileMFHarvester> {

	private static final ResourceLocation RUNE_TEXTURE = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin3.png");
	private static final ItemStack SICKLE = new ItemStack(ItemInit.alt_sickle);
	private static final float SIZE = 0.5F;
	private static final List<AxisAlignedBB> renderList = new ArrayList<>();

	@Override
	public void render(TileMFHarvester te, double x, double y, double z, float parTick, int state, float alpha) {

		// 描画onの場合
		if (te.isRangeRender) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x, (float) y + 0.005F, (float) z);
			this.renderRange(te, x, y - 0.5, z, parTick);
			GlStateManager.popMatrix();
		}

		// 起動していないまたはプレイヤーがいないなら終了
		if (!te.findPlayer && te.tickTime > 30) { return; }

		// 赤石で止まってない場合
		if (te.isActive) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
			this.renderItem(te, x, y - 0.5, z, parTick);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
			this.renderMahoujin(te, x, y - 0.5, z, parTick);
			GlStateManager.popMatrix();
		}
	}

	protected void renderRange(TileMFHarvester te, double x, double y, double z, float parTick) {

		int range = te.range;
		int putRange = Math.abs(range) + 1;
		BlockPos pos = te.getPos();
		BlockPos startPos = new BlockPos(pos.getX() - range, pos.getY(), pos.getZ() - range);
		BlockPos endPos = new BlockPos(pos.getX() + range, pos.getY() + 2D, pos.getZ() + range).add(1, 0, 1);

		switch (te.getState(pos).getValue(BaseMFFace.FACING)) {
		case NORTH:
			startPos = startPos.south(putRange);
			endPos = endPos.south(putRange);
			break;
		case SOUTH:
			startPos = startPos.north(putRange);
			endPos = endPos.north(putRange);
			break;
		case EAST:
			startPos = startPos.west(putRange);
			endPos = endPos.west(putRange);
			break;
		case WEST:
			startPos = startPos.east(putRange);
			endPos = endPos.east(putRange);
			break;
		}

		this.addAABB(startPos, endPos, pos.getX(), pos.getY(), pos.getZ());
	}

	// アイテム描画
	protected void renderItem(TileMFHarvester te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) * 0.1F) * 0.15F + 0.2F, 0);
		GlStateManager.scale(this.SIZE, this.SIZE, this.SIZE);
		GlStateManager.rotate(rot, 0.0F, 1.0F, 0.0F);
		RenderUtils.renderItem(render, SICKLE, 0, 0.55F, 0, 0, 1, 0, 0);
	}

	// 魔法陣描画
	public void renderMahoujin (TileMFHarvester te, double x, double y, double z, float parTick) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(-rot, 0F, 1F, 0F);
		GlStateManager.translate(0F, -1.6F, 0F);
		GlStateManager.color(0F, 0.882F, 1F, 1F);
		float f = (float) te.getTime() + parTick;
		GlStateManager.translate(0.0F, 0.1F + MathHelper.sin(f * 0.1F) * 0.03F + 0.3, 0.0F);
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buf = tes.getBuffer();
		bindTexture(RUNE_TEXTURE);

		buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buf.pos(-0.5F, 0F, -0.5F).tex(0D, 0D).endVertex();
		buf.pos(0.5F, 0F, -0.5F).tex(1D, 0D).endVertex();
		buf.pos(0.5F, 0F, 0.5F).tex(1D, 1D).endVertex();
		buf.pos(-0.5F, 0F, 0.5F).tex(0D, 1D).endVertex();
		tes.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
	}

	public void addAABB (BlockPos startPos, BlockPos endPos, double x, double y, double z) {
		renderList.add(new AxisAlignedBB(startPos, endPos).offset(-x, -y, -z).grow(-0.005D));
		RenderUtils.drawCube(renderList, 7);
		RenderUtils.drawCube(renderList, 2);
		renderList.clear();
	}

	private void drawAll() {

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.disableTexture2D();
		GlStateManager.disableCull();
		GlStateManager.disableLighting();
		GlStateManager.depthMask(false);
		GlStateManager.color(1F, 1F, 1F, 0.35F);
		Tessellator tess = Tessellator.getInstance();
		BufferBuilder wr = tess.getBuffer();
		wr.begin(7, DefaultVertexFormats.POSITION);

		for (AxisAlignedBB b : renderList) {

			//Top
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();

			//Bottom
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			//Front
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();

			//Back
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();

			//Left
			wr.pos(b.minX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.minX, b.maxY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.minZ).endVertex();
			wr.pos(b.minX, b.minY, b.maxZ).endVertex();

			//Right
			wr.pos(b.maxX, b.maxY, b.maxZ).endVertex();
			wr.pos(b.maxX, b.maxY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.minZ).endVertex();
			wr.pos(b.maxX, b.minY, b.maxZ).endVertex();
		}

		tess.draw();
		GlStateManager.depthMask(true);
		GlStateManager.enableCull();
		GlStateManager.enableLighting();
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}
}
