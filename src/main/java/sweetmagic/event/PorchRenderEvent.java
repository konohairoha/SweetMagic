package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.config.SMConfig;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.util.DrawHeleper;

@SideOnly(Side.CLIENT)
public class PorchRenderEvent {

	// GUIの取得
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/items/frame_2.png");
	private static final ResourceLocation WANDTEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_usergage.png");
	private int tickTime = 0;

	// レンダーイベントの呼び出し
	@SubscribeEvent
	public void onWandRenderEvent(RenderGameOverlayEvent.Text event) {

		// GUIを表示しないなら終了
		if (!SMConfig.isPorchRenderGUI) { return; }

		// プレイヤーを取得してスペクターモードなら終了
		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.player;
		if (player.isSpectator()) { return; }

		// ItemStackを取得
		ItemStack stack = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		Item item = stack.getItem();
		if (stack.isEmpty() || !(item instanceof IPouch)) { return; }

		// 画面サイズの高さを取得
		int height = event.getResolution().getScaledHeight();

		// 画面サイズの高さを取得
		int weight = event.getResolution().getScaledWidth();

		// ポーチの中身を取得し、空なら終了
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();
		if (stackList.isEmpty()) { return; }

		// 装備品リストとクールタイム取得
		List<ItemStack> acceList = new ArrayList<>();
		CooldownTracker coolDown = player.getCooldownTracker();

		for (ItemStack acceStack : stackList) {

			// クールタイムがないなら次へ
			Item acce = acceStack.getItem();
			if (!coolDown.hasCooldown(acce)) { continue; }

			acceList.add(acceStack);
		}

		if (acceList.isEmpty()) { return; }

		// 画面にレンダー出来るようにセットアップ
		mc.entityRenderer.setupOverlayRendering();

		// 杖を左寄せ描画フラグ
		boolean isLeftSide = SMConfig.isLeftSide;

		/*
		 * =========================================================
		 * 				上記必須情報
		 * =========================================================
		 */


		// レンダーの開始
		this.renderStart();

		for (int i = 0; i < acceList.size(); i++) {

			// GUIを描画
			this.renderGUI(mc, height, weight, isLeftSide, i);

			// 装備品の描画
			this.renderAcceItem(mc, player, height, weight, acceList.get(i), coolDown, isLeftSide, i);

			if (i >= 5) { break; }
		}

		// レンダーの終了
		this.renderEnd();

	}

	// レンダーの開始
	public void renderStart () {
		GL11.glPushMatrix();
        RenderHelper.disableStandardItemLighting();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
		GlStateManager.color(1, 1, 1);

		float size = 0.5F;
		GlStateManager.scale(size, size, size);
	}

	// 枠を描画
	public void renderGUI (Minecraft mc, int height, int weight, boolean isLeftSide, int addX) {

		// テクスチャのサイズ
		int texSize = 32;

		// テクスチャの指定
		mc.getTextureManager().bindTexture(TEX);
		int posX = (isLeftSide ? weight + 200 : 20) + 32 * addX;
		DrawHeleper.drawTextured(posX, (height - 20) * 2, 0, 0, texSize, texSize, texSize, texSize);
	}

	// 装備品の描画
	public void renderAcceItem(Minecraft mc, EntityPlayer player, int height, int weight, ItemStack stack, CooldownTracker coolDown, boolean isLeftSide, int addX) {

		GlStateManager.scale(2F, 2F, 2F);

		// テクスチャの指定
		int posX = (isLeftSide ? (weight + 200) / 2 : 10) + 16 * addX;
		mc.getRenderItem().renderItemIntoGUI(stack, posX, (height - 20));

		// ゲージの計算
		float coolTime = coolDown.getCooldown(stack.getItem(), 0);
		int progress = Math.min(28, (int) (28 * coolTime));

		// テクスチャの指定
		int texSize = 256;
		mc.getTextureManager().bindTexture(WANDTEX);
		GlStateManager.scale(0.5F, 0.5F, 0.5F);

		posX = (isLeftSide ? weight + 202 : 22) + 32 * addX;
		DrawHeleper.drawTextured(posX, (height - 5) * 2 - progress, 5, 73, 28, progress, texSize, texSize, false, false);
	}

	// レンダー終了しないと描画がおかしくなる
	public void renderEnd () {
		GL11.glPopMatrix();
	}
}
