package sweetmagic.init.tile.magic;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iblock.IMFBlock;
import sweetmagic.init.item.sm.magic.MFItem;

public class TileMFBase extends TileSMBase implements IMFBlock {

	public int magiaFlux = 0;				// 所有しているMF
	public int maxMagiaFlux = 10000;	// 最大MF量を設定
	public boolean isReceive;		// 受け取る側かどうか
	public int randTime = 0;
	public Set<BlockPos> posList = new HashSet<BlockPos>();	// MFブロックを保存するリスト
	public String POST = "pos";

	public TileMFBase () {
		this(true);
	}

	public TileMFBase (boolean isReceive) {
		this.setReceive(isReceive);
	}

//	@Override
//	public void update() {
//
////		int tick = this.getTickTime();
////		this.setTickTime(++tick);
//
//		// 一定時間経つと送受信をする
//		if (this.getTime() % 20 == 0 && !this.posList.isEmpty()) {
//			this.sendRecivehandler();
////			System.out.println("===========" + this.posList);
//		}
//	}

	@Override
	public void serverUpdate() {

		// 一定時間経つと送受信をする
		if (this.getTime() % 20 == 0 && !this.posList.isEmpty()) {
			this.sendRecivehandler();
		}
	}

	// 燃焼時間を返す
	protected int getItemBurnTime(ItemStack stack) {

		int val = 0;

		// MFアイテムなら
		if (stack.getItem() instanceof MFItem) {
			MFItem item = (MFItem) stack.getItem();
			val = item.getMF();
		}

		// APIアイテムなら
		else if (this.isMFAPIItem(stack)) {
			val = SweetMagicAPI.getMFFromItem(stack);
		}

		return val;
	}

	// MFの取得
	public void setMF (int magiaFlux) {
		this.magiaFlux = Math.max(magiaFlux, 0);
	}

	// MFの取得
	@Override
	public int getMF() {
		return this.magiaFlux;
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	// 座標リストの取得
	@Override
	public Set<BlockPos> getPosList() {
		return this.posList;
	}

	// 座標リストの追加
	@Override
	public void addPosList(BlockPos pos) {
		this.posList.add(pos);
	}

	// 経過時間の設定
	@Override
	public void setTickTime(int tickTime) {
		this.tickTime = tickTime;
	}

	// 経過時間の取得
	@Override
	public int getTickTime() {
		return this.tickTime;
	}

	// 座標の取得
	@Override
	public BlockPos getTilePos () {
		return this.pos;
	}

	// ワールドの取得
	@Override
	public World getTileWorld () {
		return this.world;
	}

	// 受け取り側かどうかの取得
	@Override
	public boolean getReceive() {
		return this.isReceive;
	}

	// 受け取り側かどうかの設定
	@Override
	public void setReceive(boolean isReceive) {
		this.isReceive = isReceive;
	}

	public TileEntity getTile (BlockPos pos) {
		return this.world.getTileEntity(pos);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (!this.posList.isEmpty()) {
			this.savePosList(tags, this.posList, this.POST);
		}
		tags.setInteger("magiaFlux", this.magiaFlux);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		if (tags.hasKey(this.POST)) {
			this.posList = this.loadAllPos(tags, this.POST);
		}
		this.setMF(tags.getInteger("magiaFlux"));
	}

	public List<ItemStack> getList() {
		return null;
	}
}
