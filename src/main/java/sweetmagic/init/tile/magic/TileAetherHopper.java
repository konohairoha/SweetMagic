package sweetmagic.init.tile.magic;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.magic.AetherHopper;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileAetherHopper extends TileMFBase {

	public int maxMagiaFlux = 200000;	// 最大MF量を設定

	// スロット数
	public int getInvSize () {
		return 18;
	}

	// インベントリ
	public final StackHandler chestInv = new StackHandler(this, this.getInvSize());
	public final StackHandler cleroInv = new StackHandler(this, 1);

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler side = new CombinedInvWrapper(this.chestInv);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.chestInv);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.join);
			} else {
				switch (side) {
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	@Override
	public void serverUpdate() {

		super.serverUpdate();

		if (!this.isActive(this.world, this.pos) || this.getTime() % 40 != 0) { return; }

		// ホッパーからアイテムをチェストに入れる
		this.suctionItem();

		// アイテム吸い込み
		this.extractItem();

		// MFが空出なければ実行
		if (!this.isMfEmpty()) {
			this.setHopperItem();
		}
	}

	// ホッパーからアイテムをチェストに入れる
	public void extractItem () {

		// 向きが取れないなら終了
		EnumFacing face = this.getFace();
		if (face == null) { return; }

		// タイルえんちちーを持たないかインベントリを持たないなら終了
		TileEntity tile = this.world.getTileEntity(this.pos.offset(face));
		if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) { return; }

		// ホッパーの向きで突っ込めるかチェック
		IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
		if (target == null) { return; }

		// ホッパーのインベントリ分回す
		for (int invSlot = 0; invSlot < this.getInvSize(); invSlot++) {

			// スロットが空なら次へ
			ItemStack stack = this.getChestItem(invSlot);
			if (stack.isEmpty()) { continue; }

			// 投入先のインベントリに入れる
            for (int targetSlot = 0; targetSlot < target.getSlots(); targetSlot++) {
            	ItemStack insertStack = ItemHandlerHelper.insertItemStacked(target, stack.copy(), false);
            	stack.setCount(insertStack.getCount());
            }
		}
	}

	// アイテム吸い込み
	public void suctionItem () {

		// 向きを取得してその向きにチェストがあるかのチェック
		EnumFacing face = this.getFace() == EnumFacing.UP ? EnumFacing.DOWN : EnumFacing.UP;
		TileEntity tile = this.world.getTileEntity(this.pos.offset(face));
		if (tile == null || !tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite())) { return; }

		// ホッパーの向きで突っ込めるかチェック
		IItemHandler target = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, face.getOpposite());
		if (target == null) { return; }

		for (int targetSlot = 0; targetSlot < target.getSlots(); targetSlot++) {

			// スロットが空なら次へ
			ItemStack stack = target.getStackInSlot(targetSlot);
			if (stack.isEmpty()) { continue; }

			// ホッパーにアイテムを入れる
        	ItemStack insertStack = ItemHandlerHelper.insertItemStacked(this.chestInv, stack.copy(), false);
        	stack.setCount(insertStack.getCount());
		}
	}

	// 別のホッパーへ送り付ける
	public void setHopperItem () {

		int useMF = 0;

		// クレロスロットが空かNBTを持ってないなら終了
		ItemStack clero = this.getCleroItem();
		if (clero.isEmpty() || !clero.hasTagCompound()) { return; }

		NBTTagCompound tags = clero.getTagCompound();
		if (tags == null || !tags.hasKey("X")) { return; }

		// クレロが登録してる座標を取得してホッパー以外なら終了
		BlockPos targetPos = new BlockPos(tags.getInteger("X"), tags.getInteger("Y"), tags.getInteger("Z"));
		Block block = this.getBlock(targetPos);
		if (block != BlockInit.aether_hopper) { return; }

		TileAetherHopper tile = (TileAetherHopper) this.world.getTileEntity(targetPos);

		// ホッパーのインベントリ分回す
		for (int invSlot = 0; invSlot < this.getInvSize(); invSlot++) {

			// スロットが空なら次へ
			ItemStack stack = this.getChestItem(invSlot);
			if (stack.isEmpty()) { continue; }

			// ホッパーにアイテムを入れる
        	int cost = stack.getCount();
        	ItemStack insertStack = ItemHandlerHelper.insertItemStacked(tile.chestInv, stack.copy(), false);
        	stack.setCount(insertStack.getCount());

        	// コスト分加算
        	useMF += this.getCostMF(cost, insertStack, 2);
		}

		// MFを消費するなら
		if (useMF > 0) {
			this.setMF(this.getMF() - useMF);
			this.sentClient();
		}
	}

	// 消費MFの取得
	public int getCostMF (int cost, ItemStack stack, int rate) {
		return stack.isEmpty() ? cost * rate : (cost - stack.getCount() ) * rate;
	}

	// インベントリの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem(int i) {
		return this.getChest().getStackInSlot(i);
	}

	// クレロのインベントリの取得
	public IItemHandler getClero() {
		return this.cleroInv;
	}

	// クレロのインベントリのアイテムを取得
	public  ItemStack getCleroItem() {
		return this.getClero().getStackInSlot(0);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chestInv", this.chestInv.serializeNBT());
		tags.setTag("cleroInv", this.cleroInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
		this.cleroInv.deserializeNBT(tags.getCompoundTag("cleroInv"));
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 5000;
    }

	// 向きの取得
	public EnumFacing getFace() {

		EnumFacing face = (EnumFacing) this.getState(this.pos).getValue(AetherHopper.FACING);

		switch (face) {
		case NORTH:
			return EnumFacing.SOUTH;
		case SOUTH:
			return EnumFacing.NORTH;
		case WEST:
			return EnumFacing.EAST;
		case EAST:
			return EnumFacing.WEST;
		case UP:
			return EnumFacing.DOWN;
		case DOWN:
			return EnumFacing.UP;
		}

		return face;
	}
}
