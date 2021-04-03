package sweetmagic.init.tile.cook;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.freezer.FreezerRecipeInfo;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.block.blocks.BlockFreezer;
import sweetmagic.init.block.blocks.BlockJuiceMaker;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.packet.TileJMPKT;
import sweetmagic.util.ItemHelper;

public class TileFreezer extends TileSMBase {

	// 水量
	private int waterValue = 0;
	private int waterMaxValue = 10000;

	private final ItemStackHandler waterInventory = new StackHandler(this, 1);	// 水投入スロット
	private final ItemStackHandler iceInventory = new StackHandler(this, 2);		// 氷スロット
	private final ItemStackHandler handInventory = new StackHandler(this, 1);		// ハンドスロット
	private final ItemStackHandler inputInventory = new StackHandler(this, 6);	// 入力スロット
	private final ItemStackHandler outputInventory = new StackHandler(this, 4);	// 出力スロット
	private final ItemStackHandler chestInventory = new StackHandler(this, 104);	// チェストスロット

	// 消費水量
	public int costWater = 100;

	// 作成中
	public boolean isCooking = false;
	public int cookTime = 0;

	// 出力アイテム
	public List<ItemStack> inPutList  = new ArrayList<>();
	public List<ItemStack> outPutList  = new ArrayList<>();

	public void clear() {

		// スロットリスト
		List<ItemStack> list = new ArrayList<>();
		list.add(this.getHandItem());
		for (int i = 0; i < 6; i++) {
			list.add(this.getInputItem(i));
		}

		for (ItemStack stack : list) {
			stack.shrink(1);
		}

		// 水の消費
		this.setWaterValue(this.getWaterValue() - this.getCostWater());
	}

	public List<ItemStack> allSlotItem () {

		List<ItemStack> slotList = new ArrayList<>();
		slotList.add(this.getWaterItem());
		slotList.add(this.getIceItem(0));
		slotList.add(this.getIceItem(1));
		slotList.add(this.getHandItem());

		for (int i = 0; i < 6; i++)
			slotList.add(this.getInputItem(i));

		for (int i = 0; i < 4; i++)
			slotList.add(this.getOutPutItem(i));

		return slotList;
	}

	@Override
	public void update() {

		// 下のブロックなら終了
		if (!this.isTop()) { return; }
		this.tickTime++;

		// クッキング中なら時間を足す
		if (this.isCooking) {
			this.cookTime++;

			// 100を超えても調理できないならちょっと減らす
			if (this.cookTime > 100) {
				this.cookTime = 90;
			}
		}

		// 1秒経つ
		if (this.tickTime % 20 == 0) {

			this.tickTime = 0;
			ItemHelper.compactInventory(this.inputInventory);
			ItemHelper.compactInventory(this.outputInventory);
			this.markDirty();

			// 水スロットが空以外のとき
			if (!this.getWaterItem().isEmpty()) {
				this.insetWater();
			}

			// 水が消費量より多かったら氷作成
			if (this.getWaterValue() >= this.getCostWater()) {
				this.genIce();
			}

			// 入力スロットが空じゃないかつ
			if (!this.isCooking && !this.getHandItem().isEmpty() && this.outPutList.size() <= 0) {
				this.checkRecipe();
			}

			// 作成中から5秒経ったら
			else if (this.isCooking && this.cookTime % 100 == 0) {
				this.finishCook();
			}
		}
	}

	// 上のブロックかどうか
	public boolean isTop () {
		Block b = this.getBlock(this.pos);
		if(!(b instanceof BlockFreezer)) { return false; }
		return ((BlockFreezer) b).isTop;
	}

	// 水投入処理
	public void insetWater () {

		// 水が最大に達していたら終了
		if (this.isMaxWaterValue()) { return; }

		ItemStack stack = this.getWaterItem();
		Item item = stack.getItem();

		// アイテムがバケツなら終了
		if (item == Items.BUCKET) { return; }

		// 水バケツならバケツに置き換え
		else if (item == Items.WATER_BUCKET) {
			if (this.getWaterValue() > 1000) { return; }
			this.setWaterValue(this.getWaterValue() + 1000);
			ItemStack water = this.getWaterItem();
			water.shrink(1);
			ItemHandlerHelper.insertItemStacked(this.waterInventory, new ItemStack(Items.BUCKET), false);
		}

		// 水カップなら
		else {
			if (this.getWaterValue() > this.getWaterMaxValue() - 125) { return; }
			this.setWaterValue(this.getWaterValue() + 125);
			ItemStack water = this.getWaterItem();
			water.shrink(1);
		}

	}

	// 氷作成
	public void genIce () {

		ItemStack stack = new ItemStack(Blocks.ICE);
		ItemStack stack1 = ItemHandlerHelper.insertItemStacked(this.iceInventory, stack, true);
		if (!stack1.isEmpty()) { return; }

		ItemHandlerHelper.insertItemStacked(this.iceInventory, stack, false);
		ItemHelper.compactInventory(this.iceInventory);

		// 水を消費させる
		this.setWaterValue(this.getWaterValue() - this.getCostWater());
	}

	// レシピ確認
	public void checkRecipe () {

		ItemStack stack = this.getHandItem();

		// 投入リスト
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for (int i = 0; i < 6; i++) {
			inputs.add(this.getInputItem(i));
		}

		//手持ちアイテムからレシピと一致するかを検索
		FreezerRecipeInfo recipeInfo = SweetMagicAPI.getFreezRecipeInfo(stack, inputs);

		// canComplete = Falseの場合レシピ処理をしない
		if (!recipeInfo.canComplete) { return; }

		//入れるアイテム、完成品はItemStackリストに突っ込む
		ItemStack handitem = stack.copy();
		List<ItemStack> results = new ArrayList<ItemStack>();

		//減らしたい個数を取得
		int iShrink = 1;
		handitem.setCount(iShrink);

		// 取り出したアイテムをリストにセットする
		for (ItemStack s : inputs) {
			ItemStack copy = s.copy();
			copy.setCount(1);
			this.inPutList.add(copy);
		}

		ItemStack copy = this.getHandItem().copy();
		copy.setCount(1);
		this.inPutList.add(copy);

		this.getHandItem().shrink(1);
		for (ItemStack s : inputs) {
			s.shrink(1);
		}

		// Output
		for (Object st : recipeInfo.getOutputItems()) {
			ItemStack ret = (ItemStack) st;
			ItemStack send = ret.copy();
			send.setCount(iShrink * send.getCount());
			results.add(send);
		}

		// リストをまとめる
		ItemHelper.compactItemListNoStacksize(results);

		// 作成開始
		this.isCooking = true;
		this.outPutList = results;
		this.cookTime = 0;
		this.playSound(this.pos, SMSoundEvent.JMON, 0.1F, 1F);
	}

	// 作成終了
	public void finishCook () {

		// リストの数だけ回す
		for (int i = 0; i < this.outPutList.size(); i++) {

			// 出力インベントの取得
			ItemStack outStack = this.getOutPutItem(3);
			ItemStack stack = this.outPutList.get(i);
			if (!outStack.isEmpty() && !outStack.isItemEqual(stack)) {
				this.outPutList.removeIf(ItemStack::isEmpty);
				return;
			}

			ItemHandlerHelper.insertItemStacked(this.outputInventory, stack, false);
			ItemHelper.compactInventory(this.outputInventory);
			this.outPutList.set(i, ItemStack.EMPTY);
		}

		this.inPutList.clear();
		this.outPutList.clear();
		this.cookTime = 0;
		this.isCooking = false;
		BlockJuiceMaker.setState(this.world, this.pos);

		// クライアントに送信
		PacketHandler.sendToClient(new TileJMPKT(this.pos, this.tickTime, this.cookTime, this.isCooking));
	}

	// ゲージ計算用
	public int getMfProgressScaled(int value) {
		if (!this.isEmptyWater()) {
			return (int) (value * this.getWaterValue() / this.getWaterMaxValue());
		}
		return 0;
    }

	// 調理ゲージ計算用
	public int getCookProgress (int value) {
		return this.cookTime >= 100 ? value : (int) (value * this.cookTime / 100);
	}

	// 水が空なら
	public boolean isEmptyWater () {
		return this.getWaterValue() <= 0;
	}


	// 水が最大量かどうか
	public boolean isMaxWaterValue () {
		return this.getWaterValue() >= this.getWaterMaxValue();
	}

	// 水量を取得
	public int getWaterValue () {
		return this.waterValue;
	}

	// 水量の設定
	public void setWaterValue (int value) {
		this.waterValue = value;
		if (this.isMaxWaterValue()) { this.waterValue = this.getWaterMaxValue(); }
	}

	// 水量の最大値取得
	public int getWaterMaxValue () {
		return this.waterMaxValue;
	}

	public int getCostWater () {
		return this.costWater;
	}

	// 水スロットの取得
	public IItemHandler getWater() {
		return this.waterInventory;
	}

	// 水スロットの取得
	public IItemHandler getIce() {
		return this.iceInventory;
	}

	// ハンドスロットの取得
	public IItemHandler getHand() {
		return this.handInventory;
	}

	// 入力スロットの取得
	public IItemHandler getInput() {
		return this.inputInventory;
	}

	// 出力スロットの取得
	public IItemHandler getOutput() {
		return this.outputInventory;
	}

	// チェストスロットの取得
	public IItemHandler getChest() {
		return this.chestInventory;
	}

	// 水スロットのアイテムを取得
	public  ItemStack getWaterItem() {
		return this.getWater().getStackInSlot(0);
	}

	// 氷スロットのアイテムを取得
	public  ItemStack getIceItem(int i) {
		return this.getIce().getStackInSlot(i);
	}

	// ハンドスロットのアイテムを取得
	public  ItemStack getHandItem() {
		return this.getHand().getStackInSlot(0);
	}

	// 入力スロットのアイテムを取得
	public  ItemStack getInputItem(int slot) {
		return this.getInput().getStackInSlot(slot);
	}

	// 出力スロットのアイテムを取得
	public  ItemStack getOutPutItem(int slot) {
		return this.getOutput().getStackInSlot(slot);
	}

	// チェストスロットのアイテムを取得
	public  ItemStack getChestItem(int slot) {
		return this.getChest().getStackInSlot(slot);
	}

	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("waterValue", this.getWaterValue());
		tags.setInteger("cookTime", this.cookTime);
		tags.setBoolean("isCook", this.isCooking);
		this.saveStackList(tags, this.inPutList, "input");
		this.saveStackList(tags, this.outPutList, "output");
		tags.setTag("Water", this.waterInventory.serializeNBT());
		tags.setTag("Ice", this.iceInventory.serializeNBT());
		tags.setTag("Hand", this.handInventory.serializeNBT());
		tags.setTag("Input", this.inputInventory.serializeNBT());
		tags.setTag("Output", this.outputInventory.serializeNBT());
		tags.setTag("Chest", this.chestInventory.serializeNBT());
		return tags;
	}

	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.isCooking = tags.getBoolean("isCook");
		this.cookTime = tags.getInteger("cookTime");
		this.setWaterValue(tags.getInteger("waterValue"));
		this.inPutList = loadAllItems(tags, "input");
		this.outPutList = loadAllItems(tags, "output");
		this.waterInventory.deserializeNBT(tags.getCompoundTag("Water"));
		this.iceInventory.deserializeNBT(tags.getCompoundTag("Ice"));
		this.handInventory.deserializeNBT(tags.getCompoundTag("Hand"));
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
		this.outputInventory.deserializeNBT(tags.getCompoundTag("Output"));
		this.chestInventory.deserializeNBT(tags.getCompoundTag("Chest"));
	}
}
