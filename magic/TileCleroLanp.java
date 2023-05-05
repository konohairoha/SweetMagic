package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.util.SMDamage;
import sweetmagic.util.WorldHelper;

public class TileCleroLanp extends TileMFBase {

	public UUID uuId;						// プレイヤーID
	public EntityPlayer player;				// プレイヤー
	public boolean isHaveBlock = false;	// ブロックがそろったかどうかフラグ

	private static final List<Block> blockList = Arrays.<Block> asList(
		BlockInit.alt_block, BlockInit.cosmos_light_block
	);

	@Override
	public void update() {

		super.update();
		if (!this.isActive(this.pos) || this.getTime() % 20 != 0 || WorldHelper.isPeaceful(this.world)) { return; }

		// ブロックがセットしていなかったら終了
		this.isHaveBlock = this.checkBlock();
		if (!this.isHaveBlock || !this.isSever()) { return; }

		// プレイヤーがいる場合範囲攻撃
		if (this.getPlayer() != null) {
			this.rangeAttack();
		}
	}

	// オルタナティブブロックが下に３×３であるかどうか(無かったらflagをfalseに)
	protected boolean checkBlock() {

		BlockPos pos = this.pos;

		// 範囲のブロック検索
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, -1, 1))) {

			// ブロックがオルトブロックか
			if (!blockList.contains(this.getBlock(p))) { return false; }
		}
		return true;
	}

	// 範囲のプレイヤーをセット
	public void setPlayer () {
		AxisAlignedBB aabb = this.getAABB(this.pos.add(-17, -1, -17), this.pos.add(17, 17, 17));
		List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, aabb);
		if (playerList.isEmpty()) { return; }

		for (EntityPlayer player : playerList) {
			this.player = player;
			return;
		}
	}

	public EntityPlayer getPlayer () {

		EntityPlayer player = this.player;

		if (player == null) {
			player = this.world.getClosestPlayer(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, 16D, false);
		}

		return player;
	}

	public void rangeAttack() {
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, this.getAABB(16D, 16D, 16D));

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob) || !entity.isNonBoss()) { continue; }

			this.attackDamage(entity, 2F);
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);
		}
	}

	// 攻撃
	public boolean attackDamage (EntityLivingBase entity, float dame) {

		// エンダーマンなら
		if (entity instanceof EntityEnderman) {
			DamageSource src = DamageSource.causePlayerDamage(this.getPlayer());
			return entity.attackEntityFrom(src, dame);
		}

		return entity.attackEntityFrom(this.damageSource(), dame);
	}

	// ダメージソース(誰が攻撃したかをわかるために)
	protected DamageSource damageSource(){
		return SMDamage.MagicDamage(this.getPlayer(), this.getPlayer());
	}

	// エンダーシャドーの分身なら
	public void checkShadow (EntityLivingBase entity) {
		if (entity instanceof EntityEnderShadow) {
			EntityEnderShadow ender = (EntityEnderShadow) entity;
			if (ender.isShadow) {
				ender.setDead();
			}
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
		if (!tags.hasUniqueId("uuid")) { return; }

		// えんちちーに変換
		this.uuId = tags.getUniqueId("uuid");
		if (this.uuId == null) { return; }

		Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(this.uuId);
		if (entity instanceof EntityPlayer) {
			this.player = (EntityPlayer) entity;
		}
	}
}
