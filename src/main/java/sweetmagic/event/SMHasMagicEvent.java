package sweetmagic.event;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleOrb;
import sweetmagic.init.ItemInit;
import sweetmagic.util.ParticleHelper;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class SMHasMagicEvent {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/block/mahoujin.png");

	@SubscribeEvent
	public static void drawMahoujin(DrawBlockHighlightEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		if (player.isSpectator()) { return; }

		// メインハンドに杖を持っていないなら終了
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (stack.isEmpty() || !checkWand(stack)){ return; }

		// 選択中のアイテムを取得
		Item item = stack.getItem();
		IWand wand = (IWand) item;
		ItemStack slotStack = wand.getSlotItem(player, stack, wand.getNBT(stack));
		if (slotStack.isEmpty()) { return; }

		Item slotItem = slotStack.getItem();
		if (slotItem != ItemInit.magic_meteor_fall && slotItem != ItemInit.magic_frostrain) { return; }

		// 向き取得
		World world = player.getEntityWorld();
        Vec3d vec3d = player.getLookVec().normalize().scale(10);
		int parTick = player.ticksExisted;
		float rot = parTick % 720;

		float x = (float) (player.posX + vec3d.x);
		float z = (float) (player.posZ + vec3d.z);

		BlockPos pos = new BlockPos(x, player.posY, z);
		float y = 0;

		while(true) {

			if (world.getBlockState(pos.down()).getBlock() != Blocks.AIR || pos.getY() <= 1) { break; }

			pos = pos.down();
			y--;
		}

		while(true) {

			if (world.getBlockState(pos).getBlock() == Blocks.AIR || pos.getY() >= 255) { break; }

			pos = pos.up();
			y++;
		}

		GlStateManager.pushMatrix();
		GlStateManager.translate(vec3d.x, y + 0.15D, vec3d.z);
		GlStateManager.disableLighting();
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GlStateManager.rotate(180F, 0F, 0F, 1F);
		GlStateManager.rotate(-rot, 0F, 1F, 0F);
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEX);
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder buffer = tes.getBuffer();

		float size = 3.25F;
		GlStateManager.scale(size, size, size);

		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(-0.5F, 0F, -0.5F).tex(0D, 0D).endVertex();
		buffer.pos(0.5F, 0F, -0.5F).tex(1D, 0D).endVertex();
		buffer.pos(0.5F, 0F, 0.5F).tex(1D, 1D).endVertex();
		buffer.pos(-0.5F, 0F, 0.5F).tex(0D, 1D).endVertex();
		tes.draw();

		GlStateManager.disableBlend();
		GlStateManager.enableLighting();
		GlStateManager.popMatrix();

		Random rand = world.rand;

		for (int pX = -3; pX <= 3; pX++) {
			for (int pZ = -3; pZ <= 3; pZ++) {

				// 乱数による出現調整
				if (rand.nextFloat() >= 0.075F) { continue; }

				// 魔法陣外はパーティクルを出さない
				int posX = (int) (x + pX);
				int posZ = (int) (z + pZ);
				if (pos.distanceSq(posX, pos.getY(), posZ) > 4) { continue; }

				float f1 = posX - 0.25F + rand.nextFloat() * 0.5F;
				float f2 = pos.getY() - 0.25F;
				float f3 = posZ - 0.25F + rand.nextFloat() * 0.5F;
				float ax = getRandFloat(rand) * 0.2F;
				float ay = rand.nextFloat() * 0.5F;
				float az = getRandFloat(rand) * 0.2F;

				Particle effect = ParticleOrb.create(world, f1, f2, f3, ax, ay, az, 99, 169, 255);
				ParticleHelper.spawnParticl().addEffect(effect);
			}
		}
	}

	// 乱数取得
	public static float getRandFloat (Random rand) {
		return rand.nextFloat() - rand.nextFloat();
	}

	// 杖かどうか
	private static boolean checkWand (ItemStack stack) {
		return stack.getItem() instanceof IWand;
	}
}
