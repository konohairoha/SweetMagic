package sweetmagic.init.tile.chest;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

public class TileDisplay extends TileNotePC {

	private static final int MAXPAGE = 3;
	private static final int MAXSP = 8000000;
	private static final int MAXCHANGESP = 120000;
	private static final ItemStack MYSTICAL = new ItemStack(ItemInit.mystical_page);
	private static final ItemStack ICE = new ItemStack(ItemInit.unmeltable_ice);

	// 袋
	private int acceRate = 30000;
	private ItemStack acceStack = new ItemStack(ItemInit.accebag);

	// 苗木
	private int bottleRate = 1500;
	private ItemStack bottleStack = new ItemStack(ItemInit.mf_sbottle);

	// ナゲット
	private int ingotRate = 2200;
	private ItemStack ingotStack = new ItemStack(Items.IRON_INGOT);

	// 敵ドロップ
	private int dropAdvanceRate = 40000;
	private ItemStack dropAdvanceStack = new ItemStack(ItemInit.witch_tears);

	// エメラルド
	private int crystalRate = 10000;
	private ItemStack crystalStack = new ItemStack(ItemInit.divine_crystal);

	// ドロップ品
	private int dropRate = 3000;
	private ItemStack iceStack = new ItemStack(ItemInit.unmeltable_ice);
	private ItemStack poisonStack = new ItemStack(ItemInit.poison_bottle);
	private ItemStack orbStack = new ItemStack(ItemInit.electronic_orb);
	private ItemStack soulStack = new ItemStack(ItemInit.stray_soul);
	private ItemStack gravStack = new ItemStack(ItemInit.grav_powder);

	public Map<Integer, TrageItem> getItemMenu () {
		Map<Integer, TrageItem> map = new HashMap<Integer, TrageItem>();
		map.put(0, new TrageItem(this.getBagStack(), this.getBagRate()));
		map.put(1, new TrageItem(this.getSaplingStack(), this.getSaplingRate()));
		map.put(2, new TrageItem(this.getNuggetStack(), this.getNuggetRate()));
		map.put(3, new TrageItem(this.getDropStack(), this.getDropRate()));
		map.put(4, new TrageItem(this.getEmeraldStack(), this.getEmeraldRate()));
		return map;
	}

	// 袋アイテムの取得
	public ItemStack getBagStack() {

		switch (this.getPage()) {
		case 2: return this.acceStack;
		case 3: return this.iceStack;
		}

		return super.getBagStack();
	}

	// ナゲットアイテムの取得
	public ItemStack getNuggetStack() {

		switch (this.getPage()) {
		case 2: return this.ingotStack;
		case 3: return this.poisonStack;
		}

		return super.getNuggetStack();
	}

	// 苗木アイテムの取得
	public ItemStack getSaplingStack() {

		switch (this.getPage()) {
		case 2: return this.bottleStack;
		case 3: return this.orbStack;
		}

		return super.getSaplingStack();
	}

	// 敵ドロップアイテムの取得
	public ItemStack getDropStack() {

		switch (this.getPage()) {
		case 2: return this.dropAdvanceStack;
		case 3: return this.soulStack;
		}

		return super.getDropStack();
	}

	// エメラルドアイテムの取得
	public ItemStack getEmeraldStack() {

		switch (this.getPage()) {
		case 2: return this.crystalStack;
		case 3: return this.gravStack;
		}

		return super.getEmeraldStack();
	}

	// 袋の価格
	@Override
	public int getBagRate() {

		switch (this.getPage()) {
		case 2: return this.acceRate;
		case 3: return this.dropRate;
		}

		return super.getBagRate();
	}

	// 苗木の価格
	public int getSaplingRate() {

		switch (this.getPage()) {
		case 2: return this.bottleRate;
		case 3: return this.dropRate;
		}

		return super.getSaplingRate();
	}

	// ナゲットの価格
	public int getNuggetRate() {

		switch (this.getPage()) {
		case 2: return this.ingotRate;
		case 3: return this.dropRate;
		}

		return super.getNuggetRate();
	}

	// ドロップ品の価格
	public int getDropRate() {

		switch (this.getPage()) {
		case 2: return this.dropAdvanceRate;
		case 3: return this.dropRate;
		}

		return super.getDropRate();
	}

	// エメラルドの価格
	public int getEmeraldRate() {

		switch (this.getPage()) {
		case 2: return this.crystalRate;
		case 3: return this.dropRate;
		}

		return super.getEmeraldRate();
	}

	// 袋の価格設定
	public void setBagRate (int bagRate, int page) {

		switch(page) {
		case 1:
			super.setBagRate(bagRate, 1);
			break;
		case 2:
			this.acceRate = bagRate;
			break;
		case 3:
			this.dropRate = bagRate;
			break;
		}
	}

	// 苗木の価格設定
	public void setSaplingRate (int saplingRate, int page) {

		switch(page) {
		case 1:
			super.setSaplingRate(saplingRate, 1);
			break;
		case 2:
			this.bottleRate = saplingRate;
			break;
		case 3:
			this.dropRate = saplingRate;
			break;
		}
	}

	// ナゲットの価格設定
	public void setNuggetRate (int nuggetRate, int page) {

		switch(page) {
		case 1:
			super.setNuggetRate(nuggetRate, 1);
			break;
		case 2:
			this.ingotRate = nuggetRate;
			break;
		case 3:
			this.dropRate = nuggetRate;
			break;
		}
	}

	// ドロップ品の価格設定
	public void setDropRate (int dropRate, int page) {

		switch(page) {
		case 1:
			super.setDropRate(dropRate, 1);
			break;
		case 2:
			this.dropAdvanceRate = dropRate;
			break;
		case 3:
			this.dropRate = dropRate;
			break;
		}
	}

	// エメラルドの価格設定
	public void setEmeraldRate (int emeraldRate, int page) {

		switch(page) {
		case 1:
			super.setEmeraldRate(emeraldRate, 1);
			break;
		case 2:
			this.crystalRate = emeraldRate;
			break;
		case 3:
			this.dropRate = emeraldRate;
			break;
		}
	}

	// 袋のアイテム設定
	public void setBagStack (ItemStack stack, int page) {

		switch(page) {
		case 1:
			super.setBagStack(stack, page);
			break;
		case 2:
			this.acceStack = stack;
			break;
		case 3:
			this.iceStack = stack;
			break;
		}
	}

	// 苗木のアイテム設定
	public void setSaplingStack (ItemStack stack, int page) {

		switch(page) {
		case 1:
			super.setSaplingStack(stack, page);
			break;
		case 2:
			this.bottleStack = stack;
			break;
		case 3:
			this.poisonStack = stack;
			break;
		}
	}

	// ナゲットのアイテム設定
	public void setNuggetStack (ItemStack stack, int page) {

		switch(page) {
		case 1:
			super.setNuggetStack(stack, page);
			break;
		case 2:
			this.ingotStack = stack;
			break;
		case 3:
			this.orbStack = stack;
			break;
		}
	}

	// ドロップのアイテム設定
	public void setDropStack (ItemStack stack, int page) {

		switch(page) {
		case 1:
			super.setDropStack(stack, page);
			break;
		case 2:
			this.dropAdvanceStack = stack;
			break;
		case 3:
			this.soulStack = stack;
			break;
		}
	}

	// エメラルドのアイテム設定
	public void setCrystalStack (ItemStack stack, int page) {

		switch(page) {
		case 1:
			super.setCrystalStack(stack, page);
			break;
		case 2:
			this.crystalStack = stack;
			break;
		case 3:
			this.gravStack = stack;
			break;
		}
	}

	// 袋の自動設定
	public void setBagStack (int i) {

		super.setBagStack(i);

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			this.setBagRate(30000, 2);
			stack = new ItemStack(ItemInit.accebag);
			break;
		case 1:
			this.setBagRate(72000, 2);
			stack = new ItemStack(ItemInit.cosmos_light_ingot);
			break;
		case 2:
			this.setBagRate(100000, 2);
			stack = new ItemStack(BlockInit.magiaflux_core);
			break;
		}

		this.setBagStack(stack, 2);
	}

	// ナゲットの自動設定
	public void setNuggetStack (int i) {

		super.setNuggetStack(i);

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			this.setNuggetRate(1800, 2);
			stack = new ItemStack(ItemInit.alternative_ingot);
			break;
		case 1:
			this.setNuggetRate(2200, 2);
			stack = new ItemStack(Items.IRON_INGOT);
			break;
		case 2:
			this.setNuggetRate(4400, 2);
			stack = new ItemStack(Items.GOLD_INGOT);
			break;
		}

		this.setNuggetStack(stack, 2);
	}

	// 苗木の自動設定
	public void setSaplingStack (int i) {

		super.setSaplingStack(i);

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
		case 1:
		case 2:
			this.setSaplingRate(1500, 2);
			stack = new ItemStack(ItemInit.mf_sbottle);
			break;
		case 3:
		case 4:
		case 5:
			this.setSaplingRate(14000, 2);
			stack = new ItemStack(ItemInit.mf_bottle);
			break;
		case 6:
		case 7:
			this.setSaplingRate(140000, 2);
			stack = new ItemStack(ItemInit.mf_magiabottle);
			break;
		}

		this.setSaplingStack(stack, 2);
	}

	// 敵ドロップの自動設定
	public void setDropStack (int i) {

		super.setDropStack(i);

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
		case 1:
			this.setDropRate(2000, 2);
			stack = new ItemStack(ItemInit.mysterious_page);
			break;
		case 2:
			this.setDropRate(10000, 2);
			stack = new ItemStack(ItemInit.mystical_page);
			break;
		case 3:
			this.setDropRate(10000, 2);
			stack = new ItemStack(ItemInit.witch_tears);
			break;
		case 4:
			this.setDropRate(2000, 2);
			stack = new ItemStack(ItemInit.prizmium);
			break;
		}

		this.setDropStack(stack, 2);
	}

	// クリスタルの自動設定
	public void setCrystalStack (int i) {

		super.setCrystalStack(i);

		ItemStack stack = ItemStack.EMPTY;

		switch(i) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			this.setEmeraldRate(10000, 2);
			stack = new ItemStack(ItemInit.divine_crystal);
			break;
		case 6:
			this.setEmeraldRate(120000, 2);
			stack = new ItemStack(ItemInit.pure_crystal);
			break;
		}

		this.setCrystalStack(stack, 2);
	}

	public int getMaxPage() {
		return MAXPAGE;
	}

	public int getMaxSP () {
		return this.MAXSP;
	}

	public int getMaxChargeSP () {
		return this.MAXCHANGESP;
	}

	public ItemStack getPageIcon (int page) {

		switch(page) {
		case 2: return MYSTICAL;
		case 3: return ICE;
		}

		return super.getPageIcon(page);
	}
}
