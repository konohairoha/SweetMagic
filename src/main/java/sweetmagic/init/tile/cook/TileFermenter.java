package sweetmagic.init.tile.cook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import sweetmagic.init.tile.magic.TileSMBase;

public class TileFermenter extends TileSMBase {

	public boolean isWorking = false;
	public boolean isFinish = false;

	public ItemStack handItem = ItemStack.EMPTY;			// 手に持ってるアイテム
	public List<ItemStack> inPutList = new ArrayList<>();	// 入力アイテム
	public List<ItemStack> outPutList  = new ArrayList<>();// 出力アイテム

	// 初期化
	public void clear () {
		this.isWorking = false;
		this.isFinish = false;
		this.handItem = ItemStack.EMPTY;
		this.inPutList.clear();
		this.outPutList.clear();
	}

	@Override
	public void update() {

		// 動いていなかったら
		if (!this.isWorking) { return; }

		this.tickTime++;

		// 稼働処理
		if (this.tickTime % 10 == 0) {
			this.doWorking();
		}
	}

	// 稼働処理
	public void doWorking () {

		// パーティクルすぽーん
		if (this.world.isRemote) {
			this.spawnParticle();
		}

		if (this.tickTime % 100 == 0) {
			this.isWorking = false;
			this.isFinish = true;
			this.playSound(this.pos, SoundEvents.ENTITY_GENERIC_EXTINGUISH_FIRE, 1F, 1F);

			if (this.world.isRemote) {
				for (int i = 0; i < 8; i++) {
					this.spawnParticle();
				}
			}
		}
	}

	// パーティクルすぽーん
	public void spawnParticle () {

		Random rand = this.world.rand;
		double randDouble = rand.nextDouble();
		double d0 = this.pos.getX() + 0.1D + randDouble * 0.8D;
		double d1 = this.pos.getY() + 1D + randDouble * 0.3D;
		double d2 = this.pos.getZ() + 0.1D + randDouble * 0.8D;
		this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, d0, d1, d2, 0, 0D, 0);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (!this.handItem.isEmpty()) {
			tags.setInteger("tickTime", this.tickTime);
			tags.setBoolean("isWorking", this.isWorking);
			tags.setBoolean("isFinish", this.isFinish);
			tags.setTag("handItem", this.handItem.writeToNBT(new NBTTagCompound()));
		}
		this.saveStackList(tags, this.inPutList, "input");
		this.saveStackList(tags, this.outPutList, "output");
		return tags;

	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		if (this.handItem.isEmpty()) {
			this.tickTime = tags.getInteger("tickTime");
			this.isWorking = tags.getBoolean("isWorking");
			this.isFinish = tags.getBoolean("isFinish");
			this.handItem = new ItemStack(tags.getCompoundTag("handItem"));
		}
		this.inPutList = loadAllItems(tags, "input");
		this.outPutList = loadAllItems(tags, "output");
	}

	public ItemStack getOutItem () {
		for (int i = 0; i < this.outPutList.size(); i++) {
			ItemStack stack = this.outPutList.get(i);
			if (!stack.isEmpty()) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}
}
