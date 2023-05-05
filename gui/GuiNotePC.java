package sweetmagic.init.tile.gui;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.tile.chest.TileNotePC;
import sweetmagic.init.tile.chest.TileNotePC.TrageItem;
import sweetmagic.init.tile.container.ContainerNotePC;
import sweetmagic.packet.PacketNotePCtoSever;

public class GuiNotePC extends GuiContainer {

	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_notepc.png");
	private final TileNotePC tile;
	public EntityPlayer player;

	public GuiNotePC(InventoryPlayer invPlayer, TileNotePC tile) {
		super(new ContainerNotePC(invPlayer, tile));
		this.player = invPlayer.player;
		this.tile = tile;
		this.xSize = 183 + 38;
		this.ySize = 255;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float parTick) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, parTick);

		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;

		Map<Integer, Integer> valueMap = this.tile.getItemValue();
		Map<Integer, TrageItem> itemMenu = this.tile.getItemMenu();

		for (Entry<Integer, TrageItem> map : itemMenu.entrySet()) {
			this.itemRender.renderItemIntoGUI(map.getValue().getStack(), x + 160, y + 52 + map.getKey() * 25);
		}

		for (Entry<Integer, TrageItem> map : itemMenu.entrySet()) {
			int i = map.getKey();
			TrageItem trad = map.getValue();
			int amount = valueMap.get(i);
			String rate = String.format("%,d", trad.getRate() * amount);
			String text = TextFormatting.GOLD + trad.getStack().getDisplayName() + TextFormatting.WHITE + "× " + TextFormatting.GREEN + amount;
			String buy = TextFormatting.GOLD + this.getTip("tip.sm_buying_price.name") + TextFormatting.WHITE + "： " + TextFormatting.GREEN + rate + TextFormatting.GREEN + "SP";
			this.fontRenderer.drawString(I18n.format(text), x + 9, y + 51 + i * 25, 0x404040, true);
			this.fontRenderer.drawString(I18n.format(buy), x + 9, y + 61 + i * 25, 0x404040, true);
		}

		String text = TextFormatting.GOLD + this.getTip("tip.sm_rate.name") + TextFormatting.WHITE + "： " + TextFormatting.GREEN + String.format("%.2f", this.tile.rate);
		this.fontRenderer.drawString(I18n.format(text), x + 43, y + 20, 0x404040, true);

		String text2 = TextFormatting.GOLD + "SP" + TextFormatting.WHITE + "： " + TextFormatting.GREEN + String.format("%,d", this.tile.sp);
		this.fontRenderer.drawString(I18n.format(text2), x + 43, y + 35, 0x404040, true);

		this.fontRenderer.drawString(I18n.format(TextFormatting.GREEN + this.getTip("tip.sm_sall.name")), x + 15, y + 33, 0x404040, true);

		int value = this.tile.getStack().isEmpty() ? 0 : this.tile.getItemSP(this.tile.getStack());
		String tip = TextFormatting.GOLD + this.getTip("tip.sm_selling_price.name") + TextFormatting.WHITE + "： " + TextFormatting.GREEN + String.format("%,d", value) + TextFormatting.GREEN + "SP";
		this.fontRenderer.drawString(I18n.format(tip), x + 43, y + 9, 0x404040, true);

		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);

		// 選択中のページタブを少し長く表示
		this.drawTexturedModalRect(x + 180, y + 25 + (this.tile.getPage() * 23), 184, 156, 38, 22);

		// タブごとのアイテム表示
		for (int i = 0; i < this.tile.getMaxPage(); i++) {
			int page = i + 1;
			int addX = page == this.tile.getPage() ? 8 : 0;
			this.itemRender.renderItemIntoGUI(this.tile.getPageIcon(i + 1), x + 186 + addX, y + 51 + (i * 23));
		}

		int tip_x = x + 183;
		int tip_y = y + 25;

		// タブごとのタブ名表示
		for (int i = 0; i < this.tile.getMaxPage(); i++) {

			int addX = this.tile.getPage() == (i + 1) ? 8 : 0;
			tip_y += 23;

			if (tip_x <= mouseX && mouseX <= tip_x + 26 + addX
					&& tip_y <= mouseY && mouseY <= tip_y + 21) {
				this.drawHoveringText(this.getTip(this.tile.getTip(i + 1)), mouseX + 0, mouseY - 0);
			}
		}

		this.renderHoveredToolTip(mouseX, mouseY);
	}

	public String getTip (String tip) {
		return new TextComponentTranslation(tip).getFormattedText();
	}

	@Override
	public void initGui() {

		super.initGui();
		// X座標、Y座標、カーソル合わせたときの元X座標の差, テクスチャ

		for (int i = 0; i < 5; i++) {
			int addId = i * 5;
			int addY = i * 25;
			this.buttonList.add(new GuiButtonImage(0 + addId, this.guiLeft + 158, this.height / 2 - 76 + addY, 20, 18, 186, 46, 19, TEX));
			this.buttonList.add(new GuiButtonImage(1 + addId, this.guiLeft + 126, this.height / 2 - 76 + addY, 13, 9, 186, 87, 10, TEX));
			this.buttonList.add(new GuiButtonImage(2 + addId, this.guiLeft + 141, this.height / 2 - 76 + addY, 15, 9, 201, 87, 10, TEX));
			this.buttonList.add(new GuiButtonImage(3 + addId, this.guiLeft + 126, this.height / 2 - 66 + addY, 13, 9, 218, 87, 10, TEX));
			this.buttonList.add(new GuiButtonImage(4 + addId, this.guiLeft + 141, this.height / 2 - 66 + addY, 15, 9, 233, 87, 10, TEX));
		}

		this.buttonList.add(new GuiButtonImage(26, this.guiLeft + 11, this.height / 2 - 96, 26, 13, 187, 122, 14, TEX));

		// タブのボタン表示
		for (int i = 0; i < this.tile.getMaxPage(); i++) {
			this.buttonList.add(new GuiButtonImage(26 + (i + 1), this.guiLeft + 183, this.height / 2 - 79 + (i * 23), 26, 22, 196, 156, 23, TEX));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		BlockPos pos = this.tile.getPos();
		int id = button.id;

		Map<Integer, Integer> valueMap = this.tile.getItemValue();

		// アイテムを売却ボタン
		if (id == 26) { }

		// ページ切り替えボタン
		else if (id >= 27) {

			int page = id - 26;

			if (this.tile.getPage() != page) {
				this.tile.setPage(page);
			}
		}

		// 購入ボタン
		else if (id % 5 == 0) { }

		// アイテムボタンなら
		else if (id % 5 != 0) {

			int add = 0;
			switch (id % 5) {
			case 1:
				add += 1;
				break;
			case 2:
				add += 10;
				break;
			case 3:
				add -= 1;
				break;
			case 4:
				add -= 10;
				break;
			}

			// 加減の最低、最大値で設定
			int addValue = Math.min(64, Math.max(1, valueMap.get(id / 5) + add));

			switch(id) {
			case 1:
			case 2:
			case 3:
			case 4:
				this.tile.bagValue = addValue;
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				this.tile.saplingValue = addValue;
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				this.tile.nuggetValue = addValue;
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				this.tile.dropValue = addValue;
				break;
			case 21:
			case 22:
			case 23:
			case 24:
				this.tile.emeraldValue = addValue;
				break;
			}
		}

		// サーバーへ送りつける
		PacketHandler.sendToServer(new PacketNotePCtoSever(pos.getX(), pos.getY(), pos.getZ(), button.id, 0, 0, 0F));
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float parTick, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEX);
		int x = (this.width - this.xSize) / 2;
		int y = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(x, y, 0, 0, this.xSize - 38, this.ySize);

		//こっちではゲージ量を計算する
		if (!this.tile.isEmptySP()) {
			int progress = this.tile.getSPProgressScaled(35);
			this.drawTexturedModalRect(x + 153, y + 44 - progress, 186, 43 - progress, 23, progress);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

		//描画位置を計算
		int tip_x = (this.width - this.xSize) / 2 + 152;
		int tip_y = (this.height - this.ySize) / 2 + 8;

		if (tip_x <= mouseX && mouseX <= tip_x + 24
				&& tip_y <= mouseY && mouseY <= tip_y + 36) {

			//ツールチップでMF量を表示する
			int sp = this.tile.getMaxChargeSP() - this.tile.dateSP;

			//GUIの左上からの位置
			int xAxis = (mouseX - (this.width - this.xSize) / 2);
			int yAxis = (mouseY - (this.height - this.ySize) / 2);

			String text = TextFormatting.GOLD + this.getTip("tip.max_daytrad.name") + "： ";
			this.drawHoveringText(text + TextFormatting.GREEN + String.format("%,d", sp) + TextFormatting.GREEN + "SP", xAxis, yAxis);
		}
	}

//	@Override
//	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
//
////		if (mouseButton != 0) {
////			super.mouseClicked(mouseX, mouseY, mouseButton);
////			return;
////		}
//
//		for (int i = 0; i < this.buttonList.size(); ++i) {
//
//			GuiButton guibutton = this.buttonList.get(i);
//
//			if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
//
//				ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
//				if (MinecraftForge.EVENT_BUS.post(event)) { break; }
//
//				guibutton = event.getButton();
//
//				if (guibutton.id >= 27 && guibutton.id <= this.tile.getMaxPage() + 26) {
//					int page = guibutton.id - 26;
//					if (page == this.tile.getPage()) { break; }
//				}
//
//				this.selectedButton = guibutton;
//				guibutton.playPressSound(this.mc.getSoundHandler());
//				this.actionPerformed(guibutton);
//
//				if (this.equals(this.mc.currentScreen)) {
//					MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this, event.getButton(), this.buttonList));
//				}
//			}
//		}
//	}
}
