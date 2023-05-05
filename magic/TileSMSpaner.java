package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntitySilderGhast;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.WorldHelper;

public class TileSMSpaner extends TileSMBase {

	public int data = 0;
	private int randTime = 0;
	private EntityPlayer player = null;
	private EntityLivingBase entity = null;
	public float healthRate = 1F;
	public boolean isWCSide = true;
	public boolean findPlayer = false;

	@Override
	public void update() {

		if (this.randTime <= 0) {
			this.randTime = this.setRandTime();
		}

        // モブスポーン
		if (this.getTime() % this.randTime == 0 && !WorldHelper.isPeaceful(this.world)
				&& this.isRangePlayer() && !this.checkEntity() && this.isSever()) {
			this.spawnMob();
		}

		if (!this.isSever() && this.getTime() % 9 == 0 && this.isRangePlayer()) {

			Random rand = this.world.rand;

			for (int i = 0; i < 8; i++) {
				double d3 = (double) ((float) this.pos.getX() + rand.nextFloat());
				double d4 = (double) ((float) this.pos.getY() + rand.nextFloat());
				double d5 = (double) ((float) this.pos.getZ() + rand.nextFloat());
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d3, d4, d5, 0.0D, 0.0D, 0.0D);
				this.world.spawnParticle(EnumParticleTypes.FLAME, d3, d4, d5, 0.0D, 0.0D, 0.0D);
			}
		}

		super.update();
	}

	public void clientUpdate() {
		if (this.entity != null) {
			this.entity.ticksExisted++;
		}

		this.tickTime++;
		if (this.tickTime % 30 != 0) { return; }

		this.checkRangePlayer();
	}

	public void checkRangePlayer() {
		this.findPlayer = this.findRangePlayer();
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
				this.setMaxHealth(entity, entity.getMaxHealth() * this.healthRate);
			}

			this.setArmor(entity);
			((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(this.pos), (IEntityLivingData) null);
			AnvilChunkLoader.spawnEntity(entity, this.world);
			this.world.playEvent(2004, this.pos, 0);
			((EntityLiving) entity).spawnExplosionParticle();
		}

		this.randTime = this.setRandTime();
		ParticleHelper.spawnParticle(this.world, this.pos, EnumParticleTypes.FLAME);
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

		List<EntityPlayer> entityList = this.getEntityList(EntityPlayer.class, this.getAABB());
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative() || player.isSpectator()) { continue; }

			this.player = player;
			return true;
		}

		return false;
	}

	// 範囲取得
	public AxisAlignedBB getAABB () {
		boolean isAir = this.getBlock(this.pos.down()) == Blocks.AIR;
		int under = isAir ? -8 : -4;
		int top = isAir ? 1 : 5;
		int side = isAir ? 12 : 20;
		return new AxisAlignedBB(this.pos.add(-side, under, -side), this.pos.add(side, top, side));
	}

	// 範囲にモブがいるか
	public boolean checkEntity () {

		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, this.getAABB());
		int count = 0;

		for (EntityLivingBase entity : entityList) {
			if (entity instanceof ISMMob) { count++; }
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
			if (this.isWCSide) {
				entity = new EntityWitchMadameVerre(this.world);
			}

			// ピクシィヴェックス
			else {
				entity = new EntityPixieVex(this.world);
			}
			break;
		case 5:
			// ウィンディーネ
			if (this.isWCSide) {
				entity = new EntityWindineVerre(this.world);
			}

			// シルダーガスト
			else {
				entity = new EntitySilderGhast(this.world);
			}
			break;
		case 6:
			// イフリート
			entity = new EntityIfritVerre(this.world);
			break;
		case 7:
			// ピクシィベックス
			entity = new EntityPixieVex(this.world);
			break;
		case 8:
			// シルダーガスト
			entity = new EntitySilderGhast(this.world);
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
		tags.setBoolean("isWCSide", this.isWCSide);
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
        this.data = tags.getInteger("data");
        this.randTime = tags.getInteger("randTime");
        this.healthRate = tags.getFloat("healthRate");
        this.isWCSide = tags.getBoolean("isWCSide");
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	public void setSpaner () {
		Random rand = new Random();
		this.data = this.isWCSide ? rand.nextInt(7) : rand.nextInt(6);
	}

	public int setRandTime () {
		boolean isAir = this.getBlock(this.pos.down()) == Blocks.AIR;
		int randTick = isAir ? 175 : 250;
		return this.world.rand.nextInt(randTick) + 250;
	}

	public EntityLivingBase getRenderEntity () {

		if (this.entity == null) {
			this.setRenderEntity();
			this.entity.ticksExisted++;
		}

		return this.entity;
	}

	public void setRenderEntity () {
		this.entity = this.getEntity();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
