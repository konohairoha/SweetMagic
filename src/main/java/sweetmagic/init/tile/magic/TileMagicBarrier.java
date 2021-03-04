package sweetmagic.init.tile.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.PlayerHelper;

public class TileMagicBarrier extends TileSMBase {

//	public int range = 2;

	// ロック状態の取得
	public boolean getRock () {
		return this.getBlock(this.pos) == BlockInit.magicbarrier_on;
	}


	@Override
	public void update() {

		if (this.world.isRemote || !this.getRock()) { return; }

		this.tickTime++;
		if (this.tickTime % 60 != 0) { return; }

		this.tickTime = 0;

		int range = 64;
        AxisAlignedBB aabb = new AxisAlignedBB(this.pos.add(-range, -range / 2, -range), this.pos.add(range, range / 2, range));
		List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		if (entityList.isEmpty()) { return; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			PlayerHelper.addPotion(player, PotionInit.breakblock, 400, 0, true);
		}
	}

//	// 範囲の取得
//	public int getRange() {
//		return this.range;
//	}

//	@Override
//	public NBTTagCompound writeNBT(NBTTagCompound tags) {
//		tags.setInteger("range", this.range);
//		return tags;
//	}

//	@Override
//	public void readNBT(NBTTagCompound tags) {
//		this.range = tags.getInteger("range");
//	}
}
