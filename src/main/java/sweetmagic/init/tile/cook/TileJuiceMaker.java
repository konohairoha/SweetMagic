package sweetmagic.init.tile.cook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipeInfo;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.blocks.BlockJuiceMaker;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.util.ItemHelper;

public class TileJuiceMaker extends TileSMBase {

	// 水量
	private int waterValue = 0;
	private int waterMaxValue = 2000;

	private final ItemStackHandler waterInventory = new StackHandler(this, 1);	// 水投入スロット
	private final ItemStackHandler handInventory = new StackHandler(this, 1);		// ハンドスロット
	private final ItemStackHandler inputInventory = new StackHandler(this, 3);	// 入力スロット
	private final ItemStackHandler outputInventory = new StackHandler(this, 4);	// 出力スロット

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
		List<ItemStack> list = Arrays.<ItemStack> asList(
			this.getHandItem(), this.getInputItem(0), this.getInputItem(1), this.getInputItem(2)
		);

		for (ItemStack stack : list) {
			stack.shrink(1);
		}

		// 水の消費
		this.setWaterValue(this.getWaterValue() - this.getCostWater());
	}

	@Override
	public void update() {

		this.tickTime++;

		// クッキング中なら時間を足す
		if (this.isCooking) {

			this.cookTime++;

			if (this.cookTime % 10 == 0) {
				this.playSound(this.pos, SMSoundEvent.JMON, 0.1F, 1F);
			}
		}

		// 1秒経つ
		if (this.tickTime % 20 == 0) {

			this.tickTime = 0;
			ItemHelper.compactInventory(this.inputInventory);
			ItemHelper.compactInventory(this.outputInventory);
			this.markDirty();

			if (!this.getWaterItem().isEmpty()) {
				this.insetWater();
			}

			// 入力スロットが空じゃないかつ水が消費量より多かったら
			if (!this.isCooking && !this.getHandItem().isEmpty() && !this.getInputItem(2).isEmpty()
					&& this.getWaterValue() >= this.getCostWater() && this.outPutList.size() <= 0) {
				this.checkRecipe();
			}

			// 作成中から5秒経ったら
			else if (this.isCooking && this.cookTime >= 100) {
				this.finishCook();
			}
		}
	}

	// 水投入処理
	public void insetWater () {

		// 水が最大に達していたら終了
		if (this.isMaxWaterValue()) { return; }

		// アイテムがバケツなら終了
		ItemStack stack = this.getWaterItem();
		Item item = stack.getItem();
		if (item == Items.BUCKET || item == ItemInit.alt_bucket) { return; }

		// 水バケツならバケツに置き換え
		else if (item == Items.WATER_BUCKET) {
			if (this.getWaterValue() > 1000) { return; }
			this.setWaterValue(this.getWaterValue() + 1000);
			stack.shrink(1);
			ItemHandlerHelper.insertItemStacked(this.waterInventory, new ItemStack(Items.BUCKET), false);
		}

		// 水入りオルタナティブバケツなら
		else if (item == ItemInit.alt_bucket_water) {

			if (this.getWaterValue() > 1000) { return; }

			// バケツの投入投入
			stack = ((SMBucket) item).useBucket(stack);
			this.setWaterValue(this.getWaterValue() + 1000);

			// バケツの中身が空になったら入れ替える
			if (stack.getItem() == ItemInit.alt_bucket) {
				stack.shrink(1);
				ItemHandlerHelper.insertItemStacked(this.waterInventory, new ItemStack(ItemInit.alt_bucket), false);
			}
		}

		// 水カップなら
		else {
			if (this.getWaterValue() > this.getWaterMaxValue() - 125) { return; }
			this.setWaterValue(this.getWaterValue() + 125);
			ItemStack water = this.getWaterItem();
			water.shrink(1);
		}
	}

	// レシピ確認
	public void checkRecipe () {

		ItemStack stack = this.getHandItem();

		// 投入リスト
		List<ItemStack> inputs = new ArrayList<ItemStack>();
		for (int i = 0; i < 3; i++) {
			inputs.add(this.getInputItem(i));
		}

		//手持ちアイテムからレシピと一致するかを検索して失敗なら終了
		JuiceMakerRecipeInfo recipeInfo = SweetMagicAPI.getJuiceMakerRecipeInfo(stack, inputs);
		if (!recipeInfo.canComplete) { return; }

		//入れるアイテム、完成品はItemStackリストに突っ込む
		ItemStack handitem = stack.copy();
		List<ItemStack> results = new ArrayList<ItemStack>();

		//減らしたい個数を取得
		int iShrink = recipeInfo.getHandList().get(0).getCount();
		handitem.setCount(iShrink);

		// 取り出したアイテムをリストにセットする
		for (ItemStack s : inputs) {
			ItemStack copy = s.copy();
			copy.setCount(1);
			this.inPutList.add(copy);
		}

		ItemStack copy = this.getHandItem().copy();
		copy.setCount(iShrink);
		this.inPutList.add(copy);
		this.getHandItem().shrink(iShrink);

		for (Object[] recipe : recipeInfo.getinputs()) {

			// ItemStackの取得して個数設定 + リスト追加
			ItemStack send = ((ItemStack) recipe[1]).copy();
			send.setCount((int) recipe[2]);

			for (ItemStack s : inputs) {

				Item item = s.getItem();
				int sendCount = send.getCount();
				if (send.getItem() != item || sendCount > s.getCount()) { continue; }

				if (item == ItemInit.alt_bucket_water) {
					ItemStack copyStack = ((SMBucket) item).useBucket(s).copy();
					s.shrink(sendCount);
					ItemHandlerHelper.insertItemStacked(this.inputInventory, copyStack, false);
				}

				s.shrink(sendCount);
				break;
			}
		}

		// 水を消費させる
		this.setWaterValue(this.getWaterValue() - this.getCostWater());

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
		BlockJuiceMaker.setState(this.world, this.pos);
		this.world.playSound(null, this.pos, SMSoundEvent.JMON, SoundCategory.AMBIENT, 0.1F, 1F);
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
		this.playSound(this.pos, SMSoundEvent.JMFIN, 0.2F, 1F);
		this.markDirty();
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

		if (this.isCooking && this.cookTime > 0) {
			return Math.min(value, (int) (value * ( (float) this.cookTime ) * 0.01F ) );
		}

		return 0;
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

	// 水スロットのアイテムを取得
	public  ItemStack getWaterItem() {
		return this.getWater().getStackInSlot(0);
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

	// 風車の向きを返す
	public EnumFacing getFace() {
		return (EnumFacing) this.getState(this.pos).getValue(BlockJuiceMaker.FACING);
	}

	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("waterValue", this.getWaterValue());
		tags.setInteger("cookTime", this.cookTime);
		tags.setBoolean("isCook", this.isCooking);
		this.saveStackList(tags, this.inPutList, "input");
		this.saveStackList(tags, this.outPutList, "output");
		tags.setTag("Water", this.waterInventory.serializeNBT());
		tags.setTag("Hand", this.handInventory.serializeNBT());
		tags.setTag("Input", this.inputInventory.serializeNBT());
		tags.setTag("Output", this.outputInventory.serializeNBT());
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
		this.handInventory.deserializeNBT(tags.getCompoundTag("Hand"));
		this.inputInventory.deserializeNBT(tags.getCompoundTag("Input"));
		this.outputInventory.deserializeNBT(tags.getCompoundTag("Output"));
	}
}
