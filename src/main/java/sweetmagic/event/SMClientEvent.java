package sweetmagic.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.config.SMConfig;
import sweetmagic.init.potion.RenderEffect;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = SweetMagicCore.MODID, value = Side.CLIENT)
public class SMClientEvent {

	@SubscribeEvent
	public static void renderLivingPost(RenderLivingEvent.Post<EntityLivingBase> event) {
		for (RenderEffect effect : RenderEffect.VALUES) {
			if (effect.shouldRender(event.getEntity(), false)) {
				effect.render(event.getEntity(), event.getRenderer(), event.getX(), event.getY(), event.getZ(), event.getPartialRenderTick(), false);
			}
		}
	}

	@SubscribeEvent
	public static void renderWings(RenderPlayerEvent.Post event) {

		if (SMConfig.isRender) { return; }

		// 寝ているなら終了
		EntityPlayer player = event.getEntityPlayer();
		if (player == null || player.isPlayerSleeping() || player.isElytraFlying() || player.isSpectator()) { return; }

		// 杖をインベントリに入ってないか、メインハンドオフハンドに杖を持っていたら終了
		ItemStack stack = getStack(player);
		if (stack.isEmpty() || !checkWand(stack)) { return; }

		float parTick = event.getPartialRenderTick();
		Minecraft mine = Minecraft.getMinecraft();
		IBakedModel model = mine.getRenderItem().getItemModelWithOverrides(stack, player.world, player);
		mine.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();

		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float f = setRot(player.prevRenderYawOffset, player.renderYawOffset, parTick);
		GlStateManager.rotate(180F - f, 0.0F, 1.0F, 0.0F);

		// スニーク
		if (player.isSneaking()) {
			GlStateManager.translate(0F, -0.15F, 0.692F);
			GlStateManager.rotate(-30F, 1F, 0F, 0F);
		}

		((IWand) stack.getItem()).renderWand(0.85F, player, parTick);
		mine.getRenderItem().renderItem(stack, ForgeHooksClient.handleCameraTransforms(model, TransformType.NONE, false));
		GlStateManager.popMatrix();
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0F, 0F, 0F, 1F));
	}

	// インベントリ内をチェック
	private static ItemStack getStack(EntityPlayer player) {

		ItemStack stack = ItemStack.EMPTY;

		// メインハンドに杖を持ってるなら終了
		if (checkWand(player.getHeldItemMainhand())) {
			return stack;
		}

		// オフハンドに杖を持ってるなら終了
		else if (checkWand(player.getHeldItemOffhand())) {
			return stack;
		}

		// プレイヤーのInventoryの取得
		for (ItemStack invStack : player.inventory.mainInventory) {
			if (!invStack.isEmpty() && checkWand(invStack)) { return invStack; }
		}

		return stack;
	}

	// 杖かどうか
	private static boolean checkWand (ItemStack stack) {
		return stack.getItem() instanceof IWand;
	}

	private static float setRot(float prevYawOffset, float yawOffset, float parTick) {

		float f = yawOffset - prevYawOffset;

		while (f < -180.0F) { f += 360.0F; }
		while (f >= 180.0F) { f -= 360.0F; }

		return prevYawOffset + parTick * f;
	}
}
