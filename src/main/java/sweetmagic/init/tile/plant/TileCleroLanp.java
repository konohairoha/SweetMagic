package sweetmagic.init.tile.plant;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.tile.magic.TileSMBase;
import sweetmagic.util.WorldHelper;

public class TileCleroLanp extends TileSMBase {

	public boolean blockFlag = false;	// 条件を満たしたかどうかのフラグ
	public UUID uuId;						// プレイヤーID
	public EntityPlayer player;			// プレイヤー

	@Override
	public void update() {

		if (!this.isActive()) { return; }

		this.tickTime++;

		//1秒ごとに
		if (this.tickTime % 20 == 0) {

			// ブロックのチェックフラグ更新
			this.blockFlag = this.checkBlock(this.world, this.pos);

			// パーティクルすぽーん
			if (this.blockFlag) {
				this.spawnParticles(this.world, this.pos);
			}
		}

		// 4秒ごとにモブにダメージ
		if (this.tickTime >= 40) {

			if (this.blockFlag) {
				if (this.player != null) {
					WorldHelper.attackAOE(this.player);

				// プレイヤーがNULLのとき
				} else {
					this.setPlayer();
				}
			}
			this.tickTime = 0;
		}
	}

	private boolean isActive() {
		return !this.isRedStonePower();
	}

	// レッドストーン信号を受けているかを判断する
	public boolean isRedStonePower() {
		int rs = 0;
		for (EnumFacing dir : EnumFacing.VALUES) {
			int redstoneSide = this.getWorld().getRedstonePower(this.getPos().offset(dir), dir);
			rs = Math.max(rs, redstoneSide);
		}
		return rs > 0;
	}

	// オルタナティブブロックが下に３×３であるかどうか(無かったらflagをfalseに)
	protected boolean checkBlock(World world, BlockPos pos) {

		// 範囲のブロック検索
		for (BlockPos po : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, -1, 1))) {

			// ブロックがオルトブロックか
			if (world.getBlockState(po).getBlock() != BlockInit.alt_block) { return false; }
		}
		return true;
	}

	//下３×３にオルタナティブブロックがあるときのパーティクル
	public void spawnParticles(World worldIn, BlockPos pos) {
		Random rand = this.world.rand;
		for (int i = 0; i < 16; ++i) {
			this.world.spawnParticle(EnumParticleTypes.PORTAL,
					pos.getX() + (rand.nextDouble() - 0.5D) + 0.5D, pos.getY() + rand.nextDouble(),
					pos.getZ() + (rand.nextDouble() - 0.5D) + 0.5D, (rand.nextDouble() - 0.5D) * 2.0D,
					-rand.nextDouble(), (rand.nextDouble() - 0.5D) * 2.0D);
		}
	}

	// 範囲のプレイヤーをセット
	public void setPlayer () {
		List<EntityPlayer> playerList = this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.pos.add(-17, -1, -17), this.pos.add(17, 17, 17)));
		if (playerList.isEmpty()) { return; }

		for (EntityPlayer player : playerList) {
			this.player = player;
			return;
		}
	}

	//*==================================
	//* 以下がnbt保存処理
	//*==================================

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {

		//UUIDがヌル以外
		if (this.uuId != null) {
			tags.setUniqueId("uuid", this.uuId);
		}
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {

		// nbtにデータが入ってるかどうか
		if (tags.hasUniqueId("uuid")) {
			this.uuId = tags.getUniqueId("uuid");

			// えんちちーに変換
			Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(this.uuId);
			if (entity instanceof EntityPlayer) {
				this.player = (EntityPlayer) entity;
			}
		}
	}
}
