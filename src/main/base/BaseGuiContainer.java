package sweetmagic.init.base;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.GuiContainerEvent.DrawForeground;
import net.minecraftforge.common.MinecraftForge;

public abstract class BaseGuiContainer extends GuiContainer {

    private Slot hoveredSlot;
    private Slot clickedSlot;
    private boolean isRightMouseClick;
    private ItemStack draggedStack = ItemStack.EMPTY;
    private int touchUpX;
    private int touchUpY;
    private Slot returningStackDestSlot;
    private long returningStackTime;
    private ItemStack returningStack = ItemStack.EMPTY;
    private Slot currentDragTargetSlot;
    private long dragItemDropDelay;
    private int dragSplittingLimit;
    private int dragSplittingButton;
    private boolean ignoreMouseUp;
    private int dragSplittingRemnant;
    private long lastClickTime;
    private Slot lastClickSlot;
    private int lastClickButton;
    private boolean doubleClick;
    private ItemStack shiftClickedSlot = ItemStack.EMPTY;

	private static final String[] ENCODED_SUFFIXES = {
		"K", "M", "G"
	};

	public BaseGuiContainer(Container inventory) {
		super(inventory);
		this.ignoreMouseUp = true;
	}

	public void initGui() {
		super.initGui();
		this.mc.player.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	public void drawScreen(int mouseX, int mouseY, float parTick) {
		int left = this.guiLeft;
		int top = this.guiTop;
		this.drawGuiContainerBackgroundLayer(parTick, mouseX, mouseY);
		GlStateManager.disableRescaleNormal();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();

		for (int x = 0; x < this.buttonList.size(); ++x) {
			((GuiButton) this.buttonList.get(x)).drawButton(this.mc, mouseX, mouseY, parTick);
		}

		for (int y = 0; top < this.labelList.size(); ++y) {
			((GuiLabel) this.labelList.get(y)).drawLabel(this.mc, mouseX, mouseY);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) left, (float) top, 0F);
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.enableRescaleNormal();
		this.hoveredSlot = null;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
		GlStateManager.color(1F, 1F, 1F, 1F);

		for (int i1 = 0; i1 < this.inventorySlots.inventorySlots.size(); ++i1) {
			Slot slot = this.inventorySlots.inventorySlots.get(i1);

			if (slot.isEnabled()) {
				this.drawSlot(slot);
			}

			if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.isEnabled()) {
				this.hoveredSlot = slot;
				GlStateManager.disableLighting();
				GlStateManager.disableDepth();
				int j1 = slot.xPos;
				int k1 = slot.yPos;
				GlStateManager.colorMask(true, true, true, false);
				this.drawGradientRect(j1, k1, j1 + 16, k1 + 16, -2130706433, -2130706433);
				GlStateManager.colorMask(true, true, true, true);
				GlStateManager.enableLighting();
				GlStateManager.enableDepth();
			}
		}

		RenderHelper.disableStandardItemLighting();
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		RenderHelper.enableGUIStandardItemLighting();
        MinecraftForge.EVENT_BUS.post(new DrawForeground(this, mouseX, mouseY));
		InventoryPlayer invplayer = this.mc.player.inventory;
		ItemStack stack = this.draggedStack.isEmpty() ? invplayer.getItemStack() : this.draggedStack;

		if (!stack.isEmpty()) {

			int k2 = this.draggedStack.isEmpty() ? 8 : 16;
			String s = null;

			if (!this.draggedStack.isEmpty() && this.isRightMouseClick) {
				stack = stack.copy();
				stack.setCount(MathHelper.ceil((float) stack.getCount() / 2F));
			}

			else if (this.dragSplitting && this.dragSplittingSlots.size() > 1) {
				stack = stack.copy();
				stack.setCount(this.dragSplittingRemnant);

				if (stack.isEmpty()) {
					s = "" + TextFormatting.YELLOW + "0";
				}
			}

			this.drawItemStack(stack, mouseX - left - 8, mouseY - top - k2, s);
		}

		if (!this.returningStack.isEmpty()) {
			float f = (float) (Minecraft.getSystemTime() - this.returningStackTime) / 100F;

			if (f >= 1F) {
				f = 1F;
				this.returningStack = ItemStack.EMPTY;
			}

			int l2 = this.returningStackDestSlot.xPos - this.touchUpX;
			int i3 = this.returningStackDestSlot.yPos - this.touchUpY;
			int l1 = this.touchUpX + (int) ((float) l2 * f);
			int i2 = this.touchUpY + (int) ((float) i3 * f);
			this.drawItemStack(this.returningStack, l1, i2, (String) null);
		}

		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		RenderHelper.enableStandardItemLighting();
	}

	protected void renderHoveredToolTip(int x, int y) {
		if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {
			this.renderToolTip(this.hoveredSlot.getStack(), x, y);
		}
	}

	protected void drawItemStack(ItemStack stack, int x, int y, String altText) {

		GlStateManager.translate(0F, 0F, 32F);
		this.zLevel = 200F;
		this.itemRender.zLevel = 200F;
		FontRenderer font = stack.getItem().getFontRenderer(stack);

		if (font == null) {
			font = this.fontRenderer;
		}

		this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
		this.renderItemOverlayIntoGUI(font, stack, x, y, altText);
		this.zLevel = 0F;
		this.itemRender.zLevel = 0F;
	}

	public void renderItemOverlayIntoGUI(FontRenderer fr, ItemStack stack, int x, int y, @Nullable String text) {

		if (stack.isEmpty()) { return; }

		if (stack.getCount() != 1 || text != null) {

			String stackSize = text == null ? String.valueOf(stack.getCount()) : text;
			int stackLength = ( stackSize.length() - 1) / 3;
			if (stackSize.length() > 3) {
				String encord = ENCODED_SUFFIXES[stackLength - 1];
				stackSize = stackSize.substring(0, stackSize.length() - stackLength * 3) + encord;
			}

			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableBlend();
			fr.drawStringWithShadow(stackSize, (float) (x + 19 - 2 - fr.getStringWidth(stackSize)), (float) (y + 6 + 3), 16777215);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
		}

		Item item = stack.getItem();

		if (item.showDurabilityBar(stack)) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			BufferBuilder buf = Tessellator.getInstance().getBuffer();
			double health = item.getDurabilityForDisplay(stack);
			int rgbfordisplay = item.getRGBDurabilityForDisplay(stack);
			int i = Math.round(13F - (float) health * 13F);
			int j = rgbfordisplay;
			this.draw(buf, x + 2, y + 13, 13, 2, 0, 0, 0, 255);
			this.draw(buf, x + 2, y + 13, i, 1, j >> 16 & 255, j >> 8 & 255, j & 255, 255);
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}

		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayerSP player = mc.player;
		float f3 = player == null ? 0F : player.getCooldownTracker().getCooldown(stack.getItem(), mc.getRenderPartialTicks());

		if (f3 > 0F) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.disableTexture2D();
			this.draw(Tessellator.getInstance().getBuffer(), x, y + MathHelper.floor(16F * (1F - f3)), 16, MathHelper.ceil(16F * f3), 255, 255, 255, 127);
			GlStateManager.enableTexture2D();
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
		}
	}

	private void draw(BufferBuilder render, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		render.begin(7, DefaultVertexFormats.POSITION_COLOR);
		render.pos((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
		render.pos((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
		render.pos((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
		render.pos((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();
	}

	protected void drawSlot(Slot slot) {

		int x = slot.xPos;
		int y = slot.yPos;
		ItemStack stack = slot.getStack();
		boolean flag = false;
		boolean flag1 = slot == this.clickedSlot && !this.draggedStack.isEmpty() && !this.isRightMouseClick;
		ItemStack stack1 = this.mc.player.inventory.getItemStack();
		String s = null;

		if (slot == this.clickedSlot && !this.draggedStack.isEmpty() && this.isRightMouseClick && !stack.isEmpty()) {
			stack = stack.copy();
			stack.setCount(stack.getCount() / 2);
		}

		else if (this.dragSplitting && this.dragSplittingSlots.contains(slot) && !stack1.isEmpty()) {

			if (this.dragSplittingSlots.size() == 1) { return; }

			if (Container.canAddItemToSlot(slot, stack1, true) && this.inventorySlots.canDragIntoSlot(slot)) {
				stack = stack1.copy();
				flag = true;
				Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, stack, slot.getStack().isEmpty() ? 0 : slot.getStack().getCount());
				int k = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

				if (stack.getCount() > k) {
					s = TextFormatting.YELLOW.toString() + k;
					stack.setCount(k);
				}
			}

			else {
				this.dragSplittingSlots.remove(slot);
				this.updateDragSplitting();
			}
		}

		this.zLevel = 100F;
		this.itemRender.zLevel = 100F;

		if (stack.isEmpty() && slot.isEnabled()) {
			TextureAtlasSprite tex = slot.getBackgroundSprite();

			if (tex != null) {
				GlStateManager.disableLighting();
				this.mc.getTextureManager().bindTexture(slot.getBackgroundLocation());
				this.drawTexturedModalRect(x, y, tex, 16, 16);
				GlStateManager.enableLighting();
				flag1 = true;
			}
		}

		if (!flag1) {
			if (flag) {
				drawRect(x, y, x + 16, y + 16, -2130706433);
			}

			GlStateManager.enableDepth();
			this.itemRender.renderItemAndEffectIntoGUI(this.mc.player, stack, x, y);
			this.renderItemOverlayIntoGUI(this.fontRenderer, stack, x, y, s);
		}

		this.itemRender.zLevel = 0F;
		this.zLevel = 0F;
	}

	protected void updateDragSplitting() {

		ItemStack stack = this.mc.player.inventory.getItemStack();

		if (!stack.isEmpty() && this.dragSplitting) {

			if (this.dragSplittingLimit == 2) {
				this.dragSplittingRemnant = stack.getMaxStackSize();
			}

			else {

				this.dragSplittingRemnant = stack.getCount();

				for (Slot slot : this.dragSplittingSlots) {
					ItemStack stack1 = stack.copy();
					ItemStack stack2 = slot.getStack();
					int count = stack2.isEmpty() ? 0 : stack2.getCount();
					Container.computeStackSize(this.dragSplittingSlots, this.dragSplittingLimit, stack1, count);
					int stackSize = Math.min(stack1.getMaxStackSize(), slot.getItemStackLimit(stack1));

					if (stack1.getCount() > stackSize) {
						stack1.setCount(stackSize);
					}

					this.dragSplittingRemnant -= stack1.getCount() - count;
				}
			}
		}
	}

	protected boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		return this.isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {

		if (keyCode == 1 || this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) {
			this.mc.player.closeScreen();
		}

		this.checkHotbarKeys(keyCode);

		if (this.hoveredSlot != null && this.hoveredSlot.getHasStack()) {

			if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(keyCode)) {
				this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, 0, ClickType.CLONE);
			}

			else if (this.mc.gameSettings.keyBindDrop.isActiveAndMatches(keyCode)) {
				this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, isCtrlKeyDown() ? 1 : 0, ClickType.THROW);
			}
		}
	}

	protected boolean checkHotbarKeys(int keyCode) {
		if (this.mc.player.inventory.getItemStack().isEmpty() && this.hoveredSlot != null) {
			for (int i = 0; i < 9; ++i) {
				if (this.mc.gameSettings.keyBindsHotbar[i].isActiveAndMatches(keyCode)) {
					this.handleMouseClick(this.hoveredSlot, this.hoveredSlot.slotNumber, i, ClickType.SWAP);
					return true;
				}
			}
		}

		return false;
	}

	protected void mouseReleased(int mouseX, int mouseY, int state) {

		if (this.selectedButton != null && state == 0) {
			this.selectedButton.mouseReleased(mouseX, mouseY);
			this.selectedButton = null;
		}

		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		int i = this.guiLeft;
		int j = this.guiTop;
		boolean flag = this.hasClickedOutside(mouseX, mouseY, i, j);

		if (slot != null) {
			flag = false;
		}

		int k = -1;

		if (slot != null) {
			k = slot.slotNumber;
		}

		if (flag) {
			k = -999;
		}

		EntityPlayerSP player = this.mc.player;

		if (this.doubleClick && slot != null && state == 0 && this.inventorySlots.canMergeSlot(ItemStack.EMPTY, slot)) {

			if (isShiftKeyDown()) {

				if (!this.shiftClickedSlot.isEmpty()) {

					for (Slot slot2 : this.inventorySlots.inventorySlots) {

						if (slot2 != null && slot2.canTakeStack(player) && slot2.getHasStack() && slot2.isSameInventory(slot)
								&& Container.canAddItemToSlot(slot2, this.shiftClickedSlot, true)) {
							this.handleMouseClick(slot2, slot2.slotNumber, state, ClickType.QUICK_MOVE);
						}
					}
				}
			}

			else {
				this.handleMouseClick(slot, k, state, ClickType.PICKUP_ALL);
			}

			this.doubleClick = false;
			this.lastClickTime = 0L;
		}

		else {

			if (this.dragSplitting && this.dragSplittingButton != state) {
				this.dragSplitting = false;
				this.dragSplittingSlots.clear();
				this.ignoreMouseUp = true;
				return;
			}

			if (this.ignoreMouseUp) {
				this.ignoreMouseUp = false;
				return;
			}

			if (this.clickedSlot != null && this.mc.gameSettings.touchscreen) {

				if (state == 0 || state == 1) {

					if (this.draggedStack.isEmpty() && slot != this.clickedSlot) {
						this.draggedStack = this.clickedSlot.getStack();
					}

					boolean flag2 = Container.canAddItemToSlot(slot, this.draggedStack, false);

					if (k != -1 && !this.draggedStack.isEmpty() && flag2) {

						this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state, ClickType.PICKUP);
						this.handleMouseClick(slot, k, 0, ClickType.PICKUP);

						if (player.inventory.getItemStack().isEmpty()) {
							this.returningStack = ItemStack.EMPTY;
						}

						else {
							this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, state,
									ClickType.PICKUP);
							this.touchUpX = mouseX - i;
							this.touchUpY = mouseY - j;
							this.returningStackDestSlot = this.clickedSlot;
							this.returningStack = this.draggedStack;
							this.returningStackTime = Minecraft.getSystemTime();
						}
					}

					else if (!this.draggedStack.isEmpty()) {
						this.touchUpX = mouseX - i;
						this.touchUpY = mouseY - j;
						this.returningStackDestSlot = this.clickedSlot;
						this.returningStack = this.draggedStack;
						this.returningStackTime = Minecraft.getSystemTime();
					}

					this.draggedStack = ItemStack.EMPTY;
					this.clickedSlot = null;
				}
			}

			else if (this.dragSplitting && !this.dragSplittingSlots.isEmpty()) {

				this.handleMouseClick((Slot) null, -999, Container.getQuickcraftMask(0, this.dragSplittingLimit), ClickType.QUICK_CRAFT);

				for (Slot slot1 : this.dragSplittingSlots) {
					this.handleMouseClick(slot1, slot1.slotNumber,
							Container.getQuickcraftMask(1, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
				}

				this.handleMouseClick((Slot) null, -999, Container.getQuickcraftMask(2, this.dragSplittingLimit), ClickType.QUICK_CRAFT);
			}

			else if (!player.inventory.getItemStack().isEmpty()) {

				if (this.mc.gameSettings.keyBindPickBlock.isActiveAndMatches(state - 100)) {
					this.handleMouseClick(slot, k, state, ClickType.CLONE);
				}

				else {
					boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));

					if (flag1) {
						this.shiftClickedSlot = slot != null && slot.getHasStack() ? slot.getStack().copy()
								: ItemStack.EMPTY;
					}

					this.handleMouseClick(slot, k, state, flag1 ? ClickType.QUICK_MOVE : ClickType.PICKUP);
				}
			}
		}

		if (player.inventory.getItemStack().isEmpty()) {
			this.lastClickTime = 0L;
		}

		this.dragSplitting = false;
	}

	protected Slot getSlotAtPosition(int x, int y) {
		for (int i = 0; i < this.inventorySlots.inventorySlots.size(); ++i) {
			Slot slot = this.inventorySlots.inventorySlots.get(i);

			if (this.isMouseOverSlot(slot, x, y) && slot.isEnabled()) {
				return slot;
			}
		}

		return null;
	}

	public List<String> getItemToolTip(ItemStack stack) {
		List<String> tipList = super.getItemToolTip(stack);

		int stackSize = stack.getCount();
		if (stackSize > 64) {
			String text = new TextComponentTranslation("tip.stacksize.name").getFormattedText();
			tipList.add(text + "： " + String.format("%,d", stackSize));
		}
		return tipList;
	}

	@Nullable
	public Slot getSlotUnderMouse() {
		return this.hoveredSlot;
	}
}
