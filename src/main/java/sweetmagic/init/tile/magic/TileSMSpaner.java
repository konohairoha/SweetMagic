package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
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
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.WorldHelper;

public class TileSMSpaner extends TileSMBase {

	public int data = 0;
	public int randTime = 0;
	public EntityPlayer player = null;
	public Entity entity = null;
	public float healthRate = 1F;

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

		if (this.world.isRemote && this.getTime() % 8 == 0 && this.isRangePlayer()) {
			for (int i = 0; i < 8; i++) {
				double d3 = (double) ((float) this.pos.getX() + this.world.rand.nextFloat());
				double d4 = (double) ((float) this.pos.getY() + this.world.rand.nextFloat());
				double d5 = (double) ((float) this.pos.getZ() + this.world.rand.nextFloat());
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
				this.world.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	// モブスポーン
	public void spawnMob () {

		Random rand = this.world.rand;
		int spawnCount = rand.nextInt(3) + 2;

		for (int i = 0; i < spawnCount; i++) {

			boolean isAir = this.getBlock(this.pos.down()) == Blocks.AIR;

			EntityLivingBase entity = this.getEntity();
			double x = rand.nextInt(7) - 3 + this.pos.getX() + 0.5D;
			double y = this.pos.getY() + ( isAir ? -(rand.nextInt(4) + 1) : rand.nextInt(4) + 1 ) + 0.5D;
			double z = rand.nextInt(7) - 3 + this.pos.getZ() + 0.5D;

			if (this.isAir(new BlockPos(x, y, z))) {
				x = this.pos.getX() + 0.5D;
				y = this.pos.getY() + 2 * (isAir ? -1 : 1) + 0.5D;
				z = this.pos.getZ() + 0.5D;
			}

			entity.setPosition(x, y, z);

			if (this.healthRate > 1F) {
				entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(entity.getMaxHealth() * this.healthRate);
				entity.setHealth(entity.getMaxHealth());
			}

			this.setArmor(entity);
			((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(this.pos), (IEntityLivingData) null);
			AnvilChunkLoader.spawnEntity(entity, this.world);
			this.world.playEvent(2004, this.pos, 0);
			((EntityLiving) entity).spawnExplosionParticle();
		}

		this.randTime = rand.nextInt(250) + 250;
		ParticleHelper.spawnBoneMeal(this.world, this.pos, EnumParticleTypes.FLAME);
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

		List<EntityPlayer> entityList = this.getEntityList(EntityPlayer.class, this.getAABB());
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			this.player = player;
			return true;
		}

		return false;
	}

	// 範囲取得
	public AxisAlignedBB getAABB () {
		boolean isAir = this.getBlock(this.pos.down()) == Blocks.AIR;
		return new AxisAlignedBB(this.pos.add(-20, isAir ? -8 : -4, -20), this.pos.add(20, 5, 20));
	}

	// 範囲にモブがいるか
	public boolean checkEntity () {

		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, this.getAABB());
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
		tags.setFloat("healthRate", this.healthRate);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
        this.data = tags.getInteger("data");
        this.randTime = tags.getInteger("randTime");
        this.healthRate = tags.getFloat("healthRate");
	}

	public void setMob () {
		this.data += 1;

		if (this.data > 5) {
			this.data = 0;
		}
	}

	public void setSpaner () {
		this.data = new Random().nextInt(7);
	}
}
