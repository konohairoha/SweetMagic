package sweetmagic.init.tile.chest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.init.item.sm.seed.SMSeed;
import sweetmagic.init.item.sm.seed.SMSeedFood;
import sweetmagic.init.item.sm.sweetmagic.SMDrink;
import sweetmagic.init.item.sm.sweetmagic.SMFood;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.packet.PacketNotePCtoClient;
import sweetmagic.packet.PacketNotePCtoSever;
import sweetmagic.util.ItemHelper;

public class TileNotePC extends TileSMBase {

	public int sp = 0;
	private static final int MAXSP = 1000000;
	public float rate = 1.0F;
	public int dateSP = 0;
	private static final int MAXCHANGESP = 60000;
	private int tickTime = 0;
	private int page = 1;
	private int maxPage = 1;

	// 袋
	private int bagRand = 0;
	public int bagValue = 1;
	private int bagRate = 100;
	private ItemStack bagStack = new ItemStack(ItemInit.seedbag);

	// 苗木
	private int saplingRand = 0;
	public int saplingValue = 1;
	private int saplingRate = 50;
	private ItemStack saplingStack = new ItemStack(BlockInit.chestnut_sapling);

	// ナゲット
	public int nuggetRand = 0;
	public int nuggetValue = 1;
	private int nuggetRate = 250;
	private ItemStack nuggetStack = new ItemStack(Items.IRON_NUGGET);

	// 敵ドロップ
	public int dropRand = 0;
	public int dropValue = 1;
	private int dropRate = 2000;
	private ItemStack dropStack = new ItemStack(ItemInit.unmeltable_ice);

	// エメラルド
	public int crystalRand = 0;
	public int emeraldValue = 1;
	private int emeraldRate = 10000;
	private ItemStack emeraldStack = new ItemStack(Items.EMERALD);

	private static final ItemStack EMERALD = new ItemStack(Items.EMERALD);


	// 袋アイテムの取得
	public ItemStack getBagStack () {
		return this.bagStack;
	}

	// ナゲットアイテムの取得
	public ItemStack getNuggetStack () {
		return this.nuggetStack;
	}

	// 苗木アイテムの取得
	public ItemStack getSaplingStack () {
		return this.saplingStack;
	}

	// 敵ドロップアイテムの取得
	public ItemStack getDropStack () {
		return this.dropStack;
	}

	// エメラルドアイテムの取得
	public ItemStack getEmeraldStack () {
		return this.emeraldStack;
	}

	// 袋の価格
	public int getBagRate() {
		return this.bagRate;
	}

	// 苗木の価格
	public int getSaplingRate() {
		return this.saplingRate;
	}

	// ナゲットの価格
	public int getNuggetRate() {
		return this.nuggetRate;
	}

	// ドロップ品の価格
	public int getDropRate() {
		return this.dropRate;
	}

	// エメラルドの価格
	public int getEmeraldRate() {
		return this.emeraldRate;
	}

	// 袋のアイテム設定
	public void setBagStack (ItemStack stack, int page) {
		this.bagStack = stack;
	}

	// 苗木のアイテム設定
	public void setSaplingStack (ItemStack stack, int page) {
		this.saplingStack = stack;
	}

	// ナゲットのアイテム設定
	public void setNuggetStack (ItemStack stack, int page) {
		this.nuggetStack = stack;
	}

	// ドロップのアイテム設定
	public void setDropStack (ItemStack stack, int page) {
		this.dropStack = stack;
	}

	// エメラルドのアイテム設定
	public void setCrystalStack (ItemStack stack, int page) {
		this.emeraldStack = stack;
	}

	// 袋の価格設定
	public void setBagRate (int bagRate, int page) {
		this.bagRate = bagRate;
	}

	// 苗木の価格設定
	public void setSaplingRate (int saplingRate, int page) {
		this.saplingRate = saplingRate;
	}

	// ナゲットの価格設定
	public void setNuggetRate (int nuggetRate, int page) {
		this.nuggetRate = nuggetRate;
	}

	// ドロップ品の価格設定
	public void setDropRate (int dropRate, int page) {
		this.dropRate = dropRate;
	}

	// エメラルドの価格設定
	public void setEmeraldRate (int emeraldRate, int page) {
		this.emeraldRate = emeraldRate;
	}

	public Map<Integer, TrageItem> getItemMenu () {
		Map<Integer, TrageItem> map = new HashMap<Integer, TrageItem>();
		map.put(0, new TrageItem(this.getBagStack(), this.getBagRate()));
		map.put(1, new TrageItem(this.getSaplingStack(), this.getSaplingRate()));
		map.put(2, new TrageItem(this.getNuggetStack(), this.getNuggetRate()));
		map.put(3, new TrageItem(this.getDropStack(), this.getDropRate()));
		map.put(4, new TrageItem(this.getEmeraldStack(), this.getEmeraldRate()));
		return map;
	}

	public Map<Integer, Integer> getItemValue () {
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		map.put(0, this.bagValue);
		map.put(1, this.saplingValue);
		map.put(2, this.nuggetValue);
		map.put(3, this.dropValue);
		map.put(4, this.emeraldValue);
		return map;
	}

	public void update () {

		long time = this.getTime();

		if (time % 40 == 0 && !this.isSever() && !this.isActive(this.pos)) {

			// サーバーへ送りつける
			if (!this.getStack().isEmpty()) {
				PacketHandler.sendToServer(new PacketNotePCtoSever(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 26, 0, 0, 0F));
			}
		}

		// 日にちチェック
		if ( (this.rate == 1F || time % 24000 == 0) && this.isSever() ) {

			// レート設定
			this.rate = 0.75F + this.world.rand.nextFloat() * 0.75F;
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, 0, 100, this.rate));

			// 今日のSP変換分
			this.dateSP = 0;

			// サーバーへ送りつける
			this.bagRand = this.getRand(3);
			this.setBagStack(this.bagRand);
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, this.bagRand, 0, 0F));

			this.saplingRand = this.getRand(8);
			this.setSaplingStack(this.saplingRand);
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, this.saplingRand, 1, 0F));

			this.nuggetRand = this.getRand(3);
			this.setNuggetStack(this.nuggetRand);
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, this.nuggetRand, 2, 0F));

			this.dropRand = this.getRand(5);
			this.setDropStack(this.dropRand);
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, this.dropRand, 3, 0F));

			this.crystalRand = this.getRand(7);
			this.setCrystalStack(this.crystalRand);
			PacketHandler.sendToClient(new PacketNotePCtoClient(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 100, this.crystalRand, 4, 0F));
		}
	}

	public long getTime () {
		return this.world.getTotalWorldTime();
	}

	public int getRand (int i) {
		Random rand = new Random();
		return rand.nextInt(i);
	}

	public int insertSP (ItemStack stack) {

		int insertSP = this.getItemSP(stack);
		int canInsertSP = this.getMaxSP() - this.sp;

		if (this.sp <= canInsertSP) {

			if (this.isMaxTradSp(insertSP)) {
				stack.shrink(stack.getCount());
				return this.insertSP(insertSP);
			}

			return this.getCanInsertSize(this.getMaxChargeSP() - this.dateSP, insertSP, stack);
		}

		return this.getCanInsertSize(canInsertSP, insertSP, stack);
	}

	// spの加算
	public int insertSP (int insertSP) {
		this.sp = Math.min(this.getMaxSP(), this.sp + insertSP);
		this.dateSP += insertSP;
		return this.sp;
	}

	// spが最大かどうか
	public boolean isMaxSP () {
		return this.sp >= this.getMaxSP();
	}

	// spが空かどうか
	public boolean isEmptySP () {
		return this.sp <= 0;
	}

	public int getMaxSP () {
		return this.MAXSP;
	}

	public int getMaxChargeSP () {
		return this.MAXCHANGESP;
	}

	public void setPage (int page) {
		this.page = page;
	}

	public int getPage () {
		return this.page;
	}

	public int getMaxPage() {
		return this.maxPage;
	}

	public int getCanInsertSize (int maxInsertSP, int insertSP, ItemStack stack) {

		int singleSP = insertSP / stack.getCount();
		int count = maxInsertSP / singleSP;

		ItemStack copy = stack.copy();
		copy.setCount(count);
		stack.shrink(count);
		return this.insertSP(this.getItemSP(copy));
	}

	public boolean isMaxTradSp (int insertSP) {
		return this.getMaxChargeSP() >= this.dateSP + insertSP;
	}

	public int putButton (EntityPlayer player, int id) {

		Map<Integer, Integer> valueMap = this.getItemValue();

		// アイテム売却ボタン
		if (id == 26) {
			if (!this.getStack().isEmpty()) {
				this.insertSP(this.getStack());
			}
		}

		else if (id >= 27) {
			int page = id - 26;

			if (this.getPage() != page) {
				this.setPage(page);
			}
		}

		// アイテム購入ボタンなら
		else if (id % 5 == 0) {

			TrageItem tra = this.getMenu(id);
			int value = valueMap.get(id / 5);
			int amount = tra.rate * value;

			// 支払い金額より多く持っているなら
			if (this.sp >= amount) {
				ItemStack stack = tra.getStack();
				stack.setCount(value);
				this.sp -= amount;

				if (this.isSever()) {
					this.world.spawnEntity(new EntityItem(this.world, player.posX + 0.5D, player.posY, player.posZ + 0.5D, stack));
				}
			}
		}

		else {

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
				this.bagValue = addValue;
				break;
			case 6:
			case 7:
			case 8:
			case 9:
				this.saplingValue = addValue;
				break;
			case 11:
			case 12:
			case 13:
			case 14:
				this.nuggetValue = addValue;
				break;
			case 16:
			case 17:
			case 18:
			case 19:
				this.dropValue = addValue;
				break;
			case 21:
			case 22:
			case 23:
			case 24:
				this.emeraldValue = addValue;
				break;
			}

			return addValue;
		}

		return 0;
	}

	// メニューの取得
	public TrageItem getMenu (int id) {
		return this.getItemMenu().get(id / 5);
	}

	// SPゲージの描画量を計算するためのメソッド
	public int getSPProgressScaled(int value) {
		return Math.min(value, (int) (value * (float) (this.dateSP) / (float) (this.getMaxChargeSP())));
    }

	// 袋の自動設定
	public void setBagStack (int i) {

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			stack = new ItemStack(ItemInit.seedbag);
			break;
		case 1:
			stack = new ItemStack(ItemInit.eggbag);
			break;
		case 2:
			stack = new ItemStack(ItemInit.flowerpack);
			break;
		}

		this.setBagStack(stack, 1);
	}

	// ナゲットの自動設定
	public void setNuggetStack (int i) {

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			this.setNuggetRate(250, 1);
			stack = new ItemStack(Items.IRON_NUGGET);
			break;
		case 1:
			this.setNuggetRate(500, 1);
			stack = new ItemStack(Items.GOLD_NUGGET);
			break;
		case 2:
			this.setNuggetRate(100, 1);
			stack = new ItemStack(ItemInit.aether_crystal_shard);
			break;
		}

		this.setNuggetStack(stack, 1);
	}

	// 苗木の自動設定
	public void setSaplingStack (int i) {

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			stack = new ItemStack(BlockInit.banana_sapling);
			break;
		case 1:
			stack = new ItemStack(BlockInit.chestnut_sapling);
			break;
		case 2:
			stack = new ItemStack(BlockInit.coconut_sapling);
			break;
		case 3:
			stack = new ItemStack(BlockInit.estor_sapling);
			break;
		case 4:
			stack = new ItemStack(BlockInit.lemon_sapling);
			break;
		case 5:
			stack = new ItemStack(BlockInit.orange_sapling);
			break;
		case 6:
			stack = new ItemStack(BlockInit.peach_sapling);
			break;
		case 7:
			stack = new ItemStack(BlockInit.prism_sapling);
			break;
		}

		this.setSaplingStack(stack, 1);
	}

	// 敵ドロップの自動設定
	public void setDropStack (int i) {

		ItemStack stack = ItemStack.EMPTY;

		switch (i) {
		case 0:
			stack = new ItemStack(ItemInit.unmeltable_ice);
			break;
		case 1:
			stack = new ItemStack(ItemInit.poison_bottle);
			break;
		case 2:
			stack = new ItemStack(ItemInit.electronic_orb);
			break;
		case 3:
			stack = new ItemStack(ItemInit.stray_soul);
			break;
		case 4:
			stack = new ItemStack(ItemInit.grav_powder);
			break;
		}

		this.setDropStack(stack, 1);
	}

	// 敵ドロップの自動設定
	public void setCrystalStack (int i) {

		ItemStack stack = ItemStack.EMPTY;

		switch(i) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			stack = new ItemStack(Items.EMERALD);
			break;
		case 6:
			stack = new ItemStack(ItemInit.divine_crystal);
			break;
		}

		this.setCrystalStack(stack, 1);
	}

	// スロット数
	public int getInvSize () {
		return 1;
	}

	// インベントリ
	public final ItemStackHandler chestInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

    // スロットが空かどうか
    public ItemStackHandler getChest () {
    	return this.chestInv;
    }

    // ItemStackを取得
    public ItemStack getStack () {
    	return this.getChest().getStackInSlot(0);
    }

    public int getItemSP (ItemStack stack) {

    	int value = stack.getCount();
    	int sp = value;
    	Item item = stack.getItem();

    	// 苗木
    	if (ItemHelper.checkOreName(stack, "treeSapling")) {
    		return (int) (20 * value * this.rate);
    	}

    	// 種
    	else if (ItemHelper.checkOreName(stack, "listAllseed") || item instanceof SMSeed || item instanceof SMSeedFood) {
    		return (int) (35 * value * this.rate);
    	}

    	// スイマジの食べ物
    	else if (item instanceof SMFood || item instanceof SMDrink) {
    		ItemFood food  = (ItemFood) stack.getItem();
    		int amount = (int) (food.getHealAmount(stack) * food.getHealAmount(stack) * 15F);
    		float saturation = Math.max(food.getSaturationModifier(stack) * food.getSaturationModifier(stack), 0.25F);
    		int mfValue = (int) (amount * (saturation + 0.1));
    		return (int) (mfValue * value * this.rate);
    	}

    	// 食べ物
    	else if (item instanceof ItemFood || ItemInit.foodList.contains(item)) {

    		if (ItemInit.foodList.contains(item)) {
        		return (int) (30 * value * this.rate);
    		}

    		ItemFood food  = (ItemFood) stack.getItem();
    		int amount = (int) (food.getHealAmount(stack) * food.getHealAmount(stack) * 5F);
    		int mfValue = (int) (amount * 0.25F);
    		return (int) (mfValue * value * this.rate);
    	}

    	// エメラルド
    	else if (item == Items.EMERALD) {
    		return (int) (1000 * value * this.rate);
    	}

    	else if (SlotPredicates.isMFItem(stack)) {
    		return (int) (this.getItemMF(stack, item) / 10 * value * this.rate);
    	}

    	else if (item instanceof IAcce) {
    		IAcce acce = (IAcce) item;
    		return (int) ( (5000 + 12500 * ( acce.getTier() - 1 ) ) * value * this.rate);
    	}

    	return sp;
    }

	// 吸い込むアイテムのMF量
	public int getItemMF (ItemStack stack, Item item) {

		int mf = 0;

		// MFアイテムなら
		if (item instanceof MFItem) {
			MFItem mfItem = (MFItem) item;
			mf = mfItem.getMF();
		}

		// APIに登録されたアイテムなら
		else if (this.isMFAPIItem(stack)) {
			mf = SweetMagicAPI.getMFFromItem(stack);
		}
		return mf;
	}

    // APIアイテムかどうかの判定
	public boolean isMFAPIItem (ItemStack stack) {
    	return SlotPredicates.hasMFItem(stack);
    }

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.IN);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
		}

		return super.getCapability(cap, side);
	}

    public class TrageItem {

    	private final ItemStack stack;
    	private final int rate;

    	public TrageItem (Item item, int rate) {
    		this.stack = new ItemStack(item);
    		this.rate = rate;
    	}

    	public TrageItem (ItemStack stack, int rate) {
    		this.stack = stack;
    		this.rate = rate;
    	}

    	public ItemStack getStack () {
    		return this.stack;
    	}

    	public int getRate () {
    		return this.rate;
    	}
    }

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chestInv", this.chestInv.serializeNBT());
		tags.setInteger("sp", this.sp);
		tags.setInteger("dateSP", this.dateSP);
		tags.setFloat("rate", this.rate);
		tags.setInteger("bagValue", this.bagValue);
		tags.setInteger("saplingValue", this.saplingValue);
		tags.setInteger("nuggetValue", this.nuggetValue);
		tags.setInteger("dropValue", this.dropValue);
		tags.setInteger("emeraldValue", this.emeraldValue);
		tags.setInteger("tickTime", this.tickTime);
		tags.setInteger("page", this.getPage());
		tags.setInteger("bagRand", this.bagRand);
		tags.setInteger("saplingRand", this.saplingRand);
		tags.setInteger("nuggetRand", this.nuggetRand);
		tags.setInteger("dropRand", this.dropRand);
		tags.setInteger("crystalRand", this.crystalRand);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
		this.sp = tags.getInteger("sp");
		this.dateSP = tags.getInteger("dateSP");
		this.rate = tags.getFloat("rate");
		this.bagValue = tags.getInteger("bagValue");
		this.saplingValue = tags.getInteger("saplingValue");
		this.nuggetValue = tags.getInteger("nuggetValue");
		this.dropValue = tags.getInteger("dropValue");
		this.emeraldValue = tags.getInteger("emeraldValue");
		this.tickTime = tags.getInteger("tickTime");

		if (tags.hasKey("page")) {
			this.setPage(tags.getInteger("page"));
		}

		this.bagRand = tags.getInteger("bagRand");
		this.saplingRand = tags.getInteger("saplingRand");
		this.nuggetRand = tags.getInteger("nuggetRand");
		this.dropRand = tags.getInteger("dropRand");
		this.crystalRand = tags.getInteger("crystalRand");

		this.setBagStack(this.bagRand);
		this.setSaplingStack(this.saplingRand);
		this.setNuggetStack(this.nuggetRand);
		this.setDropStack(this.dropRand);
		this.setCrystalStack(this.crystalRand);
	}

	public ItemStack getPageIcon (int page) {
		return EMERALD;
	}

	public String getTip (int page) {
		return "tip.notepc_page" + page + ".name";
	}
}
