package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.tile.inventory.InventoryWoodChest;

public class TileMFSuccessor extends TileMFBase {

	public int maxMagiaFlux = 4000000; 	// 最大MF量を設定
	public int smeltTime = 0;
	public boolean isSmelt = false;

	// 杖スロット
	public final ItemStackHandler wandInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	@Override
	public void update() {

		super.update();

		if (this.isSmelt) {
			this.tickTime++;
		}

		// 1秒に１度だけ更新
		if (this.getTime() % 10 != 0) { return; }

		// 一定時間経てば処理
		if (!this.checkWand()) {
			this.resetData();
			return;
		}

		// コストが0以下なら終了
		int useMF = this.getSuccessorCost();
		if (useMF <= 0) {
			this.resetData();
		}

		// コスト以上のMFがあるなら
		if (this.getMF() >= useMF) {
			this.smeltTime++;
			this.isSmelt = true;
		}

		// コスト以上ないなら終了
		else {
			this.resetData();
			return;
		}

		// 一定時間が経ったらエンチャレベル書き換え
		if (this.smeltTime >= 100) {
			this.successorWnad(useMF);
		}

		if (!this.isSever()) {
			this.spawnParticl();
		}

		this.markDirty();
	}

	// データの初期化
	public void resetData () {
		this.smeltTime = 0;
		this.tickTime = 0;
		this.isSmelt = false;
	}

	// 継承チェック
	public boolean checkWand () {

		// 継承元が杖でないなら終了
		ItemStack original = this.getWandItem(0);
		if (!this.isWand(original)) { return false; }

		// 杖レベルが1以下なら終了
		IWand oriWand = (IWand) original.getItem();
		int oriLevel = oriWand.getLevel(original);
		if (oriLevel <= 1) { return false; }

		// 継承先が杖以外なら終了
		ItemStack successor = this.getWandItem(1);
		if (!this.isWand(successor)) { return false; }

		// 杖レベルが継承元より大きいなら終了
		IWand sucWand = (IWand) successor.getItem();
		int sucLevel = sucWand.getLevel(successor);

		return (oriLevel / 2) > sucLevel;
	}

	// 杖かどうか
	public boolean isWand (ItemStack stack) {
		return !stack.isEmpty() && stack.getItem() instanceof IWand;
	}

	// 継承コストを取得
	public int getSuccessorCost () {

		ItemStack stack = this.getWandItem(0);
		if (!this.isWand(stack)) { return 0; }

		IWand wand = (IWand) stack.getItem();
		int level = wand.getLevel(stack);

		return level * 100000;
	}

	// 杖レベル継承
	public void successorWnad (int useMF) {

		ItemStack original = this.getWandItem(0);	// 継承元
		ItemStack successor = this.getWandItem(1);	// 継承先

		// 継承元のレベルを取得
		IWand oriWand = (IWand) original.getItem();
		int oriLevel = oriWand.getLevel(original);
		int sucLevel = oriLevel / 2;

		// レベル継承
		IWand wand = (IWand) successor.getItem();
		NBTTagCompound tags = wand.getNBT(successor);
		tags.setInteger(IWand.LEVEL, sucLevel);
		this.playSound(this.pos, SMSoundEvent.LEVELUP, 0.5F, 1F);

		// MF消費
		this.setMF(this.getMF() - useMF);
		this.sentClient();
		this.resetData();
	}

	// パーティクルスポーン
	public void spawnParticl () {

		Random rand = this.world.rand;

		for (int i = 0; i < this.smeltTime / 10; i++) {

			float randX = rand.nextFloat() - rand.nextFloat();
			float randY = ( rand.nextFloat() - rand.nextFloat() ) * 0.25F;
			float randZ = rand.nextFloat() - rand.nextFloat();

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 0.65F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;
			float xSpeed = -randX * 0.075F;
			float ySpeed = 0.05F;
			float zSpeed = -randZ * 0.075F;

			Particle effect = ParticleNomal.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed, 123, 255, 71);
			this.getParticle().addEffect(effect);
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("wand", this.wandInv.serializeNBT());
		tags.setInteger("smeltTime", this.smeltTime);
		tags.setBoolean("isSmelt", this.isSmelt);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.wandInv.deserializeNBT(tags.getCompoundTag("wand"));
		this.smeltTime = tags.getInteger("smeltTime");
		this.isSmelt = tags.getBoolean("isSmelt");
	}

	// インベントリの数
	protected int getInvSize() {
		return 2;
	}

	// 杖スロットの取得
	public IItemHandler getWand() {
		return this.wandInv;
	}

	// 杖スロットのアイテムを取得
	public  ItemStack getWandItem(int i) {
		return this.getWand().getStackInSlot(i);
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 25000;
    }

	// 向きの取得
	public EnumFacing getFace () {
		return this.getState(this.pos).getValue(BaseMFFace.FACING);
	}
}
