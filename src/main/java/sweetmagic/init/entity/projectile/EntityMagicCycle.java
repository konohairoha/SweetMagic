package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import sweetmagic.client.particle.ParticleMagicFrost;
import sweetmagic.client.particle.ParticleOrb;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.SMUtil;

public class EntityMagicCycle extends Entity {

    public static final DataParameter<Integer> NOWTICK = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> MAXTICK = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> DATA = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> RED = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> GREEN = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Integer> BLUE = EntityDataManager.<Integer>createKey(EntityMagicCycle.class, DataSerializers.VARINT);
    public static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntityMagicCycle.class, DataSerializers.FLOAT);

	public EntityLivingBase thrower;
    public UUID throwerId;

	public EntityMagicCycle(World world) {
		super(world);
		this.setSize(1.5F, 0F);
		this.isImmuneToFire = true;
	}

	public EntityMagicCycle(EntityLivingBase entity, int red, int green, int blue, float scale, int level, int maxTick, int data) {
		this(entity.world);
		BlockPos pos = entity.getPosition();
		this.setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		this.setThrower(entity);
		this.throwerId = entity.getUniqueID();
		this.setNowTick(0);
		this.setMaxTick(maxTick);
		this.setLevel(level);
		this.setData(data);
		this.setRed(red);
		this.setGreen(green);
		this.setBlue(blue);
		this.setScale(scale);
	}

	@Override
	protected void entityInit() {
		this.dataManager.register(NOWTICK, 0);
		this.dataManager.register(MAXTICK, 0);
		this.dataManager.register(LEVEL, 0);
		this.dataManager.register(DATA, 0);
		this.dataManager.register(RED, 255);
		this.dataManager.register(GREEN, 255);
		this.dataManager.register(BLUE, 255);
		this.dataManager.register(SCALE, Float.valueOf(1.0F));
	}

	public void setScale (float scale) {
		this.dataManager.set(SCALE, scale);
	}

	public float getScale () {
		return this.dataManager.get(SCALE);
	}

	public void setRed (int red) {
		this.dataManager.set(RED, red);
	}

	public int getRed () {
		return this.dataManager.get(RED);
	}

	public void setGreen (int green) {
		this.dataManager.set(GREEN, green);
	}

	public int getGreen () {
		return this.dataManager.get(GREEN);
	}

	public void setBlue (int blue) {
		this.dataManager.set(BLUE, blue);
	}

	public int getBlue () {
		return this.dataManager.get(BLUE);
	}

	public void setData (int data) {
		this.dataManager.set(DATA, data);
	}

	public int getData () {
		return this.dataManager.get(DATA);
	}

	public void setLevel (int level) {
		this.dataManager.set(LEVEL, level);
	}

	public int getLevel () {
		return this.dataManager.get(LEVEL);
	}

	public void setMaxTick (int tick) {
		this.dataManager.set(MAXTICK, tick);
	}

	public int getMaxTick () {
		return this.dataManager.get(MAXTICK);
	}

	public void setNowTick (int tick) {
		this.dataManager.set(NOWTICK, tick);
	}

	public int getNowTick () {
		return this.dataManager.get(NOWTICK);
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	public void onUpdate() {

		super.onUpdate();

		this.setNowTick(this.getNowTick() + 1);

		int tickTime = this.getNowTick();
		int maxTick = this.getMaxTick();
		int data = this.getData();

		// 一定時間ごとに効果発動
		if (tickTime % 10 == 0) {
			switch (data) {
			case 0:
				this.gravityField();
				break;
			case 1:
				this.refleshFiled();
				break;
			case 2:
				this.blazeEnd();
				break;
			case 3:
				this.absoulteZero();
				break;
			case 4:
				this.avoidTornado();
				break;
			case 5:
				this.increasedCoolTime();
				break;
			case 6:
				break;
			case 7:
				this.incinerationCannon();
				break;
			case 8:
				break;
			}
		}

		if (data == 8) {
			this.frostRain();
		}

		// 一定時間後に消滅
		if (tickTime >= maxTick) {
			this.deadEffect();
			this.setDead();
		}

		// パーティクルスポーン
		if (this.world.isRemote && data != 8) {

			int scale = (int) this.getScale();

			if (data != 2 && data !=3 && data != 4) {

				if (tickTime <= (maxTick - 6)) {
					this.spawnParticle(scale);
				}

				else if (tickTime == (maxTick - 5)) {
					for (int i = 0; i <= 20; i++) {
						this.spawnParticle(scale);
					}
				}
			}

			else {
				switch (data) {
				case 2:
					this.spawnParticleFire(scale);
					break;
				case 3:
					this.spawnParticleIce(scale);
					break;
				case 4:
					this.spawnParticleAvoid(scale);
					break;
				}
			}
		}
	}

	// グラヴィティフィールド
	public void gravityField () {

		boolean attackDamage = this.getNowTick() % 30 == 0;
		float scale = this.getScale();
		List<EntityLivingBase> entityList = this.getEntityList((double) this.getScale(), (double) this.getScale(), (double) scale);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob) || this.getDistance(entity) > scale) { continue; }

			this.addPotion(entity, PotionInit.gravity, 210, 5, true);

			if (attackDamage) {
				this.attackDamage(entity, 1F);
				entity.hurtResistantTime = 0;
				this.checkShadow(entity);
			}
		}
	}

	// リジェネシールド
	public void refleshFiled () {

		boolean isHeal = this.getNowTick() % 30 == 0;
		float scale = this.getScale();
		List<EntityPlayer> entityList = this.getPlayerList((double) this.getScale(), (double) this.getScale(), (double) scale);
		Potion potion = PotionInit.refresh_effect;

		for (EntityPlayer entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistance(entity) > scale) { continue; }

			// 体力回復
			if (isHeal && entity.getHealth() < entity.getMaxHealth()) {
				entity.heal(0.5F);
			}

			// バフのレベルが2以上or10秒以上付与されているなら次へ
			if (entity.isPotionActive(potion) && (SMUtil.potionLevel(entity, potion) >= 1 || SMUtil.potionLevel(entity, potion) > 210)) { continue; }

			this.addPotion(entity, potion, 210, 0, true);
		}
	}

	// ブレイズエンド
	public void blazeEnd () {

		boolean isAttack = this.getNowTick() % 20 == 0;
		int time = 40 * (this.getLevel() + 1);
		float scale = this.getScale();
		List<EntityLivingBase> entityList = this.getEntityList(scale, scale, scale);

		for (EntityLivingBase entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistance(entity) > scale) { continue; }

			if (entity instanceof IMob) {

				if (isAttack) {
					this.attackDamage(entity, 1F);
					entity.hurtResistantTime = 0;
					this.checkShadow(entity);
				}
			}

			else {
				entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, time, 1, true, true));
			}
		}
	}

	public void absoulteZero () {

		boolean isAttack = this.getNowTick() % 20 == 0;
		int time = 40 * (this.getLevel() + 1);
		float scale = this.getScale();
		List<EntityLivingBase> entityList = this.getEntityList(scale, scale, scale);

		for (EntityLivingBase entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistance(entity) > scale) { continue; }

			if (entity instanceof IMob) {

				if (isAttack) {
					this.attackDamage(entity, 1F);
					entity.hurtResistantTime = 0;
					this.checkShadow(entity);
				}
			}

			else {
				entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, time, 1, true, true));
			}
		}
	}

	public void avoidTornado () {

		int time = 610 + ((this.getLevel() + 1) * 600);
		float scale = this.getScale();
		List<EntityPlayer> entityList = this.getPlayerList(scale, scale, scale);

		for (EntityPlayer entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistance(entity) > scale) { continue; }

			if (!entity.onGround && !entity.isPotionActive(PotionInit.breakblock)) {
				entity.motionY += 6F;
				this.playSound(this.world, entity, SMSoundEvent.CYCLON, 0.5F, 1F);
			}

			entity.addPotionEffect(new PotionEffect(PotionInit.cyclone, time, 2, true, true));
			entity.addPotionEffect(new PotionEffect(PotionInit.glider, time, 2, true, true));
		}
	}

	public void increasedCoolTime () {

		float scale = this.getScale();
		List<EntityPlayer> entityList = this.getPlayerList(scale, scale, scale);

		for (EntityPlayer entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistance(entity) > scale) { continue; }

			entity.addPotionEffect(new PotionEffect(PotionInit.increased_cool_time, 211, 0, true, true));
		}
	}

	public void incinerationCannon () {

		int tickTime = this.ticksExisted;
		if ( this.getMaxTick() - tickTime > 200) { return; }

		float range = (200F - Math.max(this.getMaxTick() - tickTime, 150F)) / 50F;
		float scale = this.getScale() * range * range;

		List<EntityPlayer> entityList = this.getPlayerList(scale, 64, scale);

		for (EntityPlayer entity : entityList) {

			// 魔法陣の外なら次へ
			if (this.getDistanceRange(entity) > scale) { continue; }
			this.attackDamage(entity, 10F);
			entity.hurtResistantTime = 0;
		}
	}

	public void frostRain () {

		// (レベル × 補正値) + (レベル + 追加ダメージ) ÷ (レベル ÷ 2) + 追加ダメージ
		float power = this.getLevel() / 2;
		float range = this.getScale();

		if (this.thrower == null) { return; }

		int count = (int) (range / 3);

		for (int i = 0; i < count; i++) {

			double x = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
			double y = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
			double z = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);

			if (!this.world.isRemote) {
				EntityFrostMagic entity = new EntityFrostMagic(this.world, pos.getX(), pos.getY(), pos.getZ());
				entity.setHeadingFromThrower(this.thrower, this.thrower.rotationPitch, this.thrower.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
				entity.shoot(0F, -2F, 0F, 0, 0);
				entity.motionY -= 2F;
				entity.setDamage(power);
				this.world.spawnEntity(entity);
			}

			this.world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.33F, 0.67F);
		}
	}

	// えんちちー死亡時の攻撃
	public void deadEffect () {

		int data = this.getData();
		if (data != 2 && data != 3) { return; }

		int level = this.getLevel();
		PotionEffect effect = new PotionEffect(data == 2 ? PotionInit.flame : PotionInit.frosty, 40 * (level + 1), data - 1);
		float damage = (float) (level) * 6F;
		float scale = this.getScale();
		List<EntityLivingBase> entityList = this.getEntityList(scale, scale, scale);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob)) { continue; }

			this.attackDamage(entity, damage);
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);
			entity.addPotionEffect(effect);
		}
	}

	// パーティクルスポーン
	public void spawnParticle (int scale) {

		int data = this.getData();
		if (data == 6 || data == 7) { return; }

		// rgbの設定
		float rate = 2F;
		int red = this.getRGB(this.getRed(), rate);
		int green = this.getRGB(this.getGreen(), rate);
		int blue = this.getRGB(this.getBlue(), rate);

		for (int x = -scale; x <= scale; x++) {
			for (int z = -scale; z <= scale; z++) {

				// 乱数による出現調整
				if (this.rand.nextFloat() >= 0.0125F) { continue; }

				// 魔法陣外はパーティクルを出さない
				int posX = (int) (this.posX + x);
				int posZ = (int) (this.posZ + z);
				if (this.getDistanceSq(new BlockPos(posX, this.posY, posZ)) > scale * scale) { continue; }

				float f1 = posX - 0.5F + this.rand.nextFloat() * 0.5F;
				float f2 = (float) (this.posY + 0.25F);
				float f3 = posZ - 0.5F + this.rand.nextFloat() * 0.5F;
				float ax = this.getRandFloat(this.rand) * 0.2F;
				float ay = this.getRandFloat(this.rand) * 0.0825F;
				float az = this.getRandFloat(this.rand) * 0.2F;

				Particle effect = ParticleOrb.create(this.world, f1, f2, f3, ax, ay, az, red, green, blue);
				ParticleHelper.spawnParticl().addEffect(effect);
			}
		}
	}

	// パーティクルスポーン
	public void spawnParticleFire (int scale) {

		for (int x = -scale; x <= scale; x++) {
			for (int z = -scale; z <= scale; z++) {

				// 乱数による出現調整
				if (this.rand.nextFloat() >= 0.02F) { continue; }

				// 魔法陣外はパーティクルを出さない
				int posX = (int) (this.posX + x);
				int posZ = (int) (this.posZ + z);
				if (this.getDistanceSq(new BlockPos(posX, this.posY, posZ)) > scale * scale) { continue; }

				float f1 = posX - 0.5F + this.rand.nextFloat() * 0.5F;
				float f2 = (float) (this.posY - 0.25F);
				float f3 = posZ - 0.5F + this.rand.nextFloat() * 0.5F;
				float ax = this.getRandFloat(this.rand) * 0.3F;
				float ay = this.getRandFloat(this.rand) * 0.175F;
				float az = this.getRandFloat(this.rand) * 0.3F;

				this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, ax, ay, az);
			}
		}
	}

	// パーティクルスポーン
	public void spawnParticleIce (int scale) {

		for (int x = -scale; x <= scale; x++) {
			for (int z = -scale; z <= scale; z++) {

				// 乱数による出現調整
				if (this.rand.nextFloat() >= 0.015F) { continue; }

				// 魔法陣外はパーティクルを出さない
				int posX = (int) (this.posX + x);
				int posZ = (int) (this.posZ + z);
				if (this.getDistanceSq(new BlockPos(posX, this.posY, posZ)) > scale * scale) { continue; }

				float f1 = posX - 0.5F + this.rand.nextFloat() * 0.5F;
				float f2 = (float) (this.posY - 0.25F);
				float f3 = posZ - 0.5F + this.rand.nextFloat() * 0.5F;
				float ax = this.getRandFloat(this.rand) * 0.05F;
				float ay = this.rand.nextFloat() * 0.025F;
				float az = this.getRandFloat(this.rand) * 0.05F;

				Particle effect = ParticleMagicFrost.create(this.world, f1, f2, f3, ax, ay, az);
				ParticleHelper.spawnParticl().addEffect(effect);
			}
		}
	}

	// パーティクルスポーン
	public void spawnParticleAvoid (int scale) {

		// rgbの設定
		float rate = 2F;
		int red = this.getRGB(this.getRed(), rate);
		int green = this.getRGB(this.getGreen(), rate);
		int blue = this.getRGB(this.getBlue(), rate);

		for (int x = -scale; x <= scale; x++) {
			for (int z = -scale; z <= scale; z++) {

				// 乱数による出現調整
				if (this.rand.nextFloat() >= 0.075F) { continue; }

				// 魔法陣外はパーティクルを出さない
				int posX = (int) (this.posX + x);
				int posZ = (int) (this.posZ + z);
				if (this.getDistanceSq(new BlockPos(posX, this.posY, posZ)) > scale * scale) { continue; }

				float f1 = posX - 0.5F + this.rand.nextFloat() * 0.5F;
				float f2 = (float) (this.posY - 0.25F);
				float f3 = posZ - 0.5F + this.rand.nextFloat() * 0.5F;
				float ax = this.getRandFloat(this.rand) * 0.2F;
				float ay = this.rand.nextFloat() * 1.375F;
				float az = this.getRandFloat(this.rand) * 0.2F;

				Particle effect = ParticleOrb.create(this.world, f1, f2, f3, ax, ay, az, red, green, blue);
				ParticleHelper.spawnParticl().addEffect(effect);
			}
		}
	}

	// RGBの調整
	public int getRGB (int rgb, float rate) {
		return (int) Math.min(255, rgb * rate);
	}

	// 乱数取得
	public float getRandFloat (Random rand) {
		return this.rand.nextFloat() - this.rand.nextFloat();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}

	public void setDead() {
		super.setDead();
	}

	public float getDistanceRange(Entity entity) {
		float f = (float) (this.posX - entity.posX);
		float f2 = (float) (this.posZ - entity.posZ);
		return MathHelper.sqrt(f * f + f2 * f2);
	}

	// えんちちーリスト取得
	public List<EntityLivingBase> getEntityList (double x, double  y, double  z) {
		return this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getAABB(x, y, z));
	}

	// プレイヤーリスト取得
	public List<EntityPlayer> getPlayerList (double x, double  y, double  z) {
		return this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getAABB(x, y, z));
	}

	// 範囲の取得
	public AxisAlignedBB getAABB (double x, double  y, double  z) {
		return this.getEntityBoundingBox().grow(x, y, z);
	}

	// ポーション付与
	public void addPotion (EntityLivingBase entiy, Potion potion, int time, int level, boolean flag) {
		PlayerHelper.addPotion(entiy, potion, time, level, flag);
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

	// 射撃者の設定
	public void setThrower (EntityLivingBase thrower) {
		this.thrower = thrower;
        this.throwerId = thrower == null ? null : thrower.getUniqueID();
	}

	public EntityLivingBase getThrower () {

		if (this.thrower == null && this.throwerId != null && this.world instanceof WorldServer) {
			Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.throwerId);

			if (entity instanceof EntityLivingBase) {
				this.thrower = (EntityLivingBase) entity;
			}
		}

        return this.thrower;
	}

	// 攻撃
	public boolean attackDamage (EntityLivingBase entity, float dame) {

		// エンダーマンなら
		if (entity instanceof EntityEnderman && this.getThrower() instanceof EntityPlayer) {
			DamageSource src = DamageSource.causePlayerDamage((EntityPlayer) this.getThrower());
			return entity.attackEntityFrom(src, dame);
		}

		return entity.attackEntityFrom(this.damageSource(), dame);
	}

	// ダメージソース(誰が攻撃したかをわかるために)
	protected DamageSource damageSource(){
		return SMDamage.MagicDamage(this, this.getThrower());
	}

	// 音流し
	public void playSound (World world, EntityPlayer player, SoundEvent sound, float vol, float pith) {
        world.playSound(player, player.getPosition(), sound, SoundCategory.PLAYERS, vol, pith);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tags) {
		this.setNowTick(tags.getInteger("nowTick"));
		this.setMaxTick(tags.getInteger("maxTick"));
		this.setLevel(tags.getInteger("level"));
		this.setData(tags.getInteger("data"));
		this.setRed(tags.getInteger("red"));
		this.setGreen(tags.getInteger("green"));
		this.setBlue(tags.getInteger("blue"));
		this.setScale(tags.getFloat("scale"));
		this.throwerId = tags.getUniqueId("throwerId");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tags) {
		tags.setInteger("nowTick", this.getNowTick());
		tags.setInteger("maxTick", this.getMaxTick());
		tags.setInteger("level", this.getLevel());
		tags.setInteger("data", this.getData());
		tags.setInteger("red", this.getRed());
		tags.setInteger("green", this.getGreen());
		tags.setInteger("blue", this.getBlue());
		tags.setFloat("scale", this.getScale());

		if (this.throwerId != null) {
			tags.setUniqueId("throwerId", this.throwerId);
		}
	}
}
