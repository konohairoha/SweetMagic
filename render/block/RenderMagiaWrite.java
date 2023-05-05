package sweetmagic.init.render.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.magic.TileMagiaWrite;
import sweetmagic.util.RenderUtils;

public class RenderMagiaWrite extends TileEntitySpecialRenderer<TileMagiaWrite> {

	private static final float size = 0.35F;
	private static final ItemStack stack = new ItemStack(ItemInit.aether_crystal_shard);
	private static final IBlockState MAGIA = BlockInit.magiaflux_block.getDefaultState();

	@Override
	public void render(TileMagiaWrite te, double x, double y, double z, float parTick, int stage, float alpha) {

		ItemStack stack = te.getToolItem();
		if (te.getToolItem().isEmpty()) { return; }

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        this.renderItem(te, x, y + 0.5, z, parTick, stack);
        GlStateManager.popMatrix();

        if (te.getData() == 0) {
            this.renderEffect(te, x, y, z, parTick);
        }

        else if (te.getData() == 1 && te.isSmelt && te.tickTime > 0 && te.smeltTime > 0 && te.getEnchantCost() > 0) {
        	this.spawnParticl(te);
        }
	}

	// スロットのアイテム描画
	protected void renderItem(TileMagiaWrite te, double x, double y, double z, float parTick, ItemStack stack) {

		Long worldTime = te.getWorld().getTotalWorldTime();
		float rot = worldTime % 720;
		float addY = te.getData() == 0 ? 1.6F : 3.5F;

		RenderItem render = Minecraft.getMinecraft().getRenderItem();
		GlStateManager.translate(0, MathHelper.sin((worldTime + parTick) / 10F) * 0.1F + 0.2F, 0);
		GlStateManager.scale(this.size, this.size, this.size);
		GlStateManager.rotate(rot, 0F, 1F, 0F);
		RenderUtils.renderItem(render, stack, 0, addY, 0, 0, 1, 0, 0F);
	}

	public void renderEffect (TileMagiaWrite te, double x, double y, double z, float parTick) {

		if (te.isSmelt && te.tickTime > 0 && te.getEnchantCost() > 0) {

			float posY = 0.5F + 0.001375F * te.tickTime;
	        GlStateManager.pushMatrix();
	        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);

	        this.renderAether(te, 0.3375F, posY, 0.3375F, parTick);
	        this.renderAether(te, -0.3375F, posY, 0.3375F, parTick);
	        this.renderAether(te, 0.3375F, posY, -0.3375F, parTick);
	        this.renderAether(te, -0.3375F, posY, -0.3375F, parTick);

	        GlStateManager.popMatrix();
		}
	}

	// 柱の周りのかけらの描画
	public void renderAether (TileMagiaWrite te, float x, float y, float z, float parTick) {

		float rotY = (te.getTime() + parTick + te.tickTime) / 10F;
		float rotX = -0.125F;
		float rotZ = 0;
		float scale = 0.25F;
		int count = 4;
		float pi = 180F / (float) Math.PI;
		RenderItem render = Minecraft.getMinecraft().getRenderItem();

		for (int i = 0; i < count; i++) {
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(rotZ * pi, 0F, 0F, 1F);
			GlStateManager.rotate(rotY * pi + (i * (360 / count)), 0F, 1F, 0F);
			GlStateManager.rotate(rotX * pi, 1F, 0F, 0F);
			GlStateManager.scale(scale, -scale, scale);
			GlStateManager.translate(0.75F, 0F, 0F);
			RenderUtils.renderItem(render, this.stack, 0, 1.6F, 0, 0, 0, 0, 0);
			GlStateManager.popMatrix();
		}
	}

	public void spawnParticl (TileMagiaWrite te) {

		int time = (int) (16F * ( ((float) te.smeltTime) / 50F));
		if (time <= 0 || te.tickTime % (64 / time) != 0) { return; }

		float scale = 0.25F;
		this.spawnParticl(te, -scale, -scale);
		this.spawnParticl(te, -scale, scale);
		this.spawnParticl(te, scale, -scale);
		this.spawnParticl(te, scale, scale);
	}

	public void spawnParticl (TileMagiaWrite te, float x, float z) {

		float pX = te.getPos().getX() + x + 0.5F;
		float pZ = te.getPos().getZ() + z + 0.5F;
		Particle effect = ParticleNomal.create(te.getWorld(), pX, te.getPos().getY() + 1F, pZ, -x * 0.1F, 0.5F * 0.1F, -z * 0.1F);
		te.getParticle().addEffect(effect);
	}
}
