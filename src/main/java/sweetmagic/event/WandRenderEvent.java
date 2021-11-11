package sweetmagic.event;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.config.SMConfig;
import sweetmagic.util.DrawHeleper;

@SideOnly(Side.CLIENT)
public class WandRenderEvent {

	// GUIの取得
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_usergage.png");

	// レンダーイベントの呼び出し
	@SubscribeEvent
	public void onBulletRenderEvent(RenderGameOverlayEvent.Text event) {

		Minecraft mc = FMLClientHandler.instance().getClient();
		EntityPlayer player = mc.player;					// プレイヤーを取得
		ItemStack stack = player.getHeldItemMainhand();		// ItemStackを取得
		Item item = stack.getItem();						// Itemを取得
		if (stack.isEmpty() || !(item instanceof IWand)) { return; }

		// 画面サイズの高さを取得
		int height = event.getResolution().getScaledHeight();

		// 画面サイズの高さを取得
		int weight = event.getResolution().getScaledWidth();

		// 選択中のアイテムを取得
		IWand wand = (IWand) item;
		ItemStack slotStack = wand.getSlotItem(player, stack, wand.getNBT(stack));
		ISMItem smItem = null;
		Item slotItem = slotStack.getItem();

		if (!slotStack.isEmpty()) {

			if (!(slotStack.getItem() instanceof ISMItem)) { return; }

			// スロットのアイテムを取得
			smItem = (ISMItem) slotStack.getItem();
		}

		// 画面にレンダー出来るようにセットアップ
		mc.entityRenderer.setupOverlayRendering();
		boolean isLeftSide = SMConfig.isLeftSide;


		/*
		 * =========================================================
		 * 				上記必須情報
		 * =========================================================
		 */


		// レンダーの開始
		this.renderStart();

		// GUIを描画
		this.renderGUI(mc, height, weight, isLeftSide);

		// ゲージの計算
		int progress = wand.getMfProgressScaled(stack, 45);

		// GUIのゲージを描画
		this.renderProgress(mc, height, weight, isLeftSide, progress);

		// 選択してるスロットが空なら終了
		if (!slotStack.isEmpty()) {

			// 選択中のスロットのアイテムを描画
			this.renderSlotItem(mc, height, weight, smItem, isLeftSide);

			// クールタイムを持っていたら
			if (player.getCooldownTracker().hasCooldown(slotItem)) {
				this.renderCoolTime(mc, player, height, weight, slotItem, isLeftSide);
			}
		}

		// 杖のMFをテキスト描画
		this.renderMFText(mc, height, weight, stack, wand, isLeftSide);

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
	}

	// GUIを描画
	public void renderGUI (Minecraft mc, int height, int weight, boolean isLeftSide) {

		// テクスチャのサイズ
		int texSize = 256;

		// テクスチャの指定
		mc.getTextureManager().bindTexture(TEX);
		int posX = isLeftSide ? 10 : weight - 100;
		DrawHeleper.drawTexturedRect(posX, height - 48, 0, 0, texSize, texSize, texSize, texSize);
	}

	// GUIのゲージを描画
	public void renderProgress (Minecraft mc, int height, int weight, boolean isLeftSide, int progress) {

		// テクスチャのサイズ
		int texSize = 256;

		// テクスチャの指定
		mc.getTextureManager().bindTexture(TEX);
		int posX = isLeftSide ? 55 : weight - 55;
		DrawHeleper.drawTexturedFlippedRect(posX, height - 27, 0, 300, progress, 60, texSize, texSize, false, false);
	}

	// 選択中のスロットのアイテムを描画
	public void renderSlotItem(Minecraft mc, int height, int weight, ISMItem smItem, boolean isLeftSide) {

		// テクスチャのサイズ
		int texSize = 32;

		// テクスチャの指定
		mc.getTextureManager().bindTexture(smItem.getResource());
		int posX = isLeftSide ? 15 : weight - 95;
		DrawHeleper.drawTexturedRect(posX, height - 44, 0, 0, texSize, texSize, texSize, texSize);

		// 消費アイテムなら終了
		if (smItem.isShirink()) { return; }

		// tierの取得
		int tier = smItem.getTier();
		String name = "";

		switch (tier) {
		case 1:
			name = "frame_1";
			break;
		case 2:
			name = "frame_2";
			break;
		case 3:
			name = "frame_3";
			break;
		case 4:
			name = "frame_4";
			break;
		}

		// テクスチャの指定
		mc.getTextureManager().bindTexture(new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png"));
		DrawHeleper.drawTexturedRect(posX, height - 44, 0, 0, texSize, texSize, texSize, texSize);
	}

	// 選択中のスロットのクールタイム描画
	public void renderCoolTime(Minecraft mc, EntityPlayer player, int height, int weight, Item item, boolean isLeftSide) {

		// テクスチャのサイズ
		int texSize = 256;

		// ゲージの計算
		int progress = (int) (32 * player.getCooldownTracker().getCooldown(item, 0));

		// テクスチャの指定
		mc.getTextureManager().bindTexture(TEX);
		int posX = isLeftSide ? 15 : weight - 95;
		DrawHeleper.drawTexturedFlippedRect(posX, height - 12 - progress, 5, 73, 32, progress, texSize, texSize, false, false);
	}

	// 杖のMFをテキスト描画
	public void renderMFText (Minecraft mc, int height, int weight, ItemStack stack, IWand wand, boolean isLeftSide) {
		FontRenderer font = mc.fontRenderer;
		String text = String.format("%,d", wand.getMF(stack)) + "MF";
		int posX = isLeftSide ? 72 : weight - 40;
		DrawHeleper.drawScaledStringToWidth(font, text, posX, height - 33, 1, 0xffffff, weight, true, false);
	}

	// レンダー終了しないと描画がおかしくなる
	public void renderEnd () {
		GL11.glPopMatrix();
	}
}
