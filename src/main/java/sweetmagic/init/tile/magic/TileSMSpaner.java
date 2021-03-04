package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.WorldHelper;

public class TileSMSpaner extends TileSMBase {

	public int data = 0;
	public int randTime = 0;
	public EntityPlayer player = null;
	public Entity entity = null;

	@Override
	public void update() {

		if (this.randTime <= 0) {
			this.randTime = this.world.rand.nextInt(250) + 250;
		}

        // モブスポーン
		if (this.getTime() % this.randTime == 0 && !WorldHelper.isPeaceful(this.world)
				&& this.isRangePlayer() && !this.checkEntity() && !this.world.isRemote) {
			this.spawnMob();
		}
	}

	// モブスポーン
	public void spawnMob () {

		Random rand = this.world.rand;
		int spawnCount = rand.nextInt(3) + 2;

		for (int i = 0; i < spawnCount; i++) {

			EntityLivingBase entity = this.getEntity();
			int x = rand.nextInt(7) - 3 + this.pos.getX();
			int y = rand.nextInt(2) + 1 + this.pos.getY();
			int z = rand.nextInt(7) - 3 + this.pos.getZ();
			BlockPos pos = new BlockPos(x, y, z);
			if (this.getBlock(pos) != Blocks.AIR || this.getBlock(pos.up()) != Blocks.AIR) { continue; }

			entity.setPosition(x, y, z);
			this.setArmor(entity);
			((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
			AnvilChunkLoader.spawnEntity(entity, world);
			this.world.playEvent(2004, pos, 0);
			((EntityLiving) entity).spawnExplosionParticle();
		}

		this.randTime = rand.nextInt(250) + 250;
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

		List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getAABB());
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {
			if (!PlayerHelper.isCleative(player)) {
				this.player = player;
				return true;
			}
		}

		return false;
	}

	// 範囲取得
	public AxisAlignedBB getAABB () {
		return new AxisAlignedBB(this.pos.add(-15, -5, -15), this.pos.add(15, 5, 15));
	}

	// 範囲にモブがいるか
	public boolean checkEntity () {

		List<EntityLivingBase> entityList = this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getAABB());
		int count = 0;

		for (EntityLivingBase entity : entityList) {

			if (entity instanceof ISMMob) {
				count++;
			}
		}

		return count > 7;
	}

	// えんちちーの取得
	public EntityLivingBase getEntity () {

		EntityLivingBase entity = null;

		switch (this.data) {
		case 0:
			// スカルフロスト
			entity = new EntitySkullFrost(this.world);
			break;
		case 1:
			// エンダーシャドー
			entity = new EntityEnderShadow(this.world);
			break;
		case 2:
			// ブレイズテンペスト
			entity = new EntityBlazeTempest(this.world);
			break;
		case 3:
			// ファントムゾンビ
			entity = new EntityPhantomZombie(this.world);
			break;
		case 4:
			// ウィッチマスター
			entity = new EntityWitchMadameVerre(this.world);
			break;
		case 5:
			// ウィンディーネ
			entity = new EntityWindineVerre(this.world);
			break;
		case 6:
			// イフリート
			entity = new EntityIfritVerre(this.world);
			break;
		}

		return entity;
	}

	// 防御バフ
	public void setArmor (EntityLivingBase entity) {
		entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1200, 2, true, false));
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		tags.setInteger("data", this.data);
		tags.setInteger("randTime", this.randTime);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
        this.data = tags.getInteger("data");
        this.randTime = tags.getInteger("randTime");
	}

	public void setMob () {
		this.data += 1;

		if (this.data > 5) {
			this.data = 0;
		}
	}
}
