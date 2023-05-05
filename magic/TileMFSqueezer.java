package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.packet.TileMFBlockPKT;

public class TileMFSqueezer extends TileMFFisher {

	public int needMF = 100;
	public final int randTick = 2000;
	public final int shortTick = 200;
	private EntityLivingBase entity;

	public EntityLivingBase getEntity () {

		if (this.entity == null) {
			this.entity = new EntityCow(this.world);
			this.entity.ticksExisted = 0;
		}

		return entity;
	}

	public void clientUpdate () {
		if (this.entity != null) {
			this.entity.ticksExisted++;
		}
	}

	// 必要MF
	public int getNeedMF() {
		return this.needMF;
	}

	public boolean checkMF () {
		return true;
	}

	// 次の処理時間の初期化
	public int clearRandTime () {
		int time = this.getMF() >= this.getNeedMF() ? this.shortTick : this.randTick;
		return this.world.rand.nextInt(time);
	}

	// 燃焼処理
	public void smeltItem() {

		super.smeltItem();

		if (this.randTime >= 1000) {
			this.randTime = this.clearRandTime();
		}
	}

	public boolean checkTime () {
		return this.tickTime >= (this.randTick + (this.getMF() >= this.getNeedMF() ? this.shortTick : this.randTick));
	}

	// 釣りのルートテーブルから引き出す
	public boolean onFishing () {

		boolean isUseMF = this.getMF() >= this.getNeedMF();
		Random rand = world.rand;

		if (isUseMF) {
			this.setMF(this.getMF() - this.needMF);
		}

		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, this.getMF(), this.getTilePos()));

		if (this.isSever()) {
			ItemHandlerHelper.insertItemStacked(this.outputInv, new ItemStack(ItemInit.milk_pack, this.world.rand.nextInt(10) + 6), false);
		}

		this.playSound(this.pos, SoundEvents.ENTITY_COW_MILK, 0.33F, 1F);
		this.randTime = rand.nextInt(isUseMF ? this.shortTick : this.randTick);
		return false;
	}
}
