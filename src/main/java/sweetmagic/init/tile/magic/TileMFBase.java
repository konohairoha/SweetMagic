package sweetmagic.init.tile.magic;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iblock.IMFBlock;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.item.sm.magic.MFItem;
import sweetmagic.packet.TileMFBlockPKT;

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

	@Override
	public void clientUpdate() {

		if (this.getTime() % 80 == 0 && !this.posList.isEmpty() && this.canMFChange()) {

			for (BlockPos pos : this.posList) {

				TileEntity tile = this.getTile(pos);
				if (!(tile instanceof TileMFBase) || !this.checkDistance(pos) || ((TileMFBase) tile).isMfEmpty()) { continue; }

				Random rand = this.world.rand;
				float pX = this.pos.getX() - pos.getX();
				float pY = this.pos.getY() - pos.getY();
				float pZ = this.pos.getZ() - pos.getZ();

				for (int i = 0; i < 4; i++) {

					float randX = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
					float randY = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
					float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.5F;

					float x = pos.getX() + 0.5F + randX;
					float y = pos.getY() + 0.525F + randY;
					float z = pos.getZ() + 0.5F + randZ;
					float xSpeed = pX * 0.1175F;
					float ySpeed = pY * 0.1175F;
					float zSpeed = pZ * 0.1175F;

					Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, x, y, z, xSpeed, ySpeed, zSpeed);
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
				}
			}
		}
	}

	@Override
	public void serverUpdate() {

		// 一定時間経つと送受信をする
		if (this.getTime() % 20 == 0 && !this.posList.isEmpty()) {
			this.sendRecivehandler();
		}
	}

	// 距離のチェック
	public boolean checkDistance (BlockPos pos) {
		double dis = 15D;
		double pX = Math.abs(this.pos.getX() - pos.getX());
		double pY = Math.abs(this.pos.getY() - pos.getY());
		double pZ = Math.abs(this.pos.getZ() - pos.getZ());
		return pX <= dis && pY <= dis && pZ <= dis;
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

	public TileMFBase getMFTile (BlockPos pos) {
		return (TileMFBase) this.getTile(pos);
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

	public void sentClient () {
		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));
	}
}
