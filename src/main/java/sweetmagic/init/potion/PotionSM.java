package sweetmagic.init.potion;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.SMUtil;
import sweetmagic.util.WorldHelper;

public class PotionSM extends PotionBase {

	private float lastRotYaw = 0;
	public final boolean isActive;
	public static final UUID SPPEDID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE491222");
	public static final UUID ARMORID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE730213");

	public PotionSM (boolean effect, int color, String name, String dir, boolean isActive) {
		super(effect, color, name, dir);

		if (name.equals("babule")) {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
					.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, SPPEDID.toString(), -0.025D, 2));
		}

		else if (name.equals("range_glove")) {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
					.registerPotionAttributeModifier(EntityPlayer.REACH_DISTANCE, SMUtil.TOOL_REACH.toString(), 12D, 0));
		}

		else if (name.equals("prompt_feather")) {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
					.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, SPPEDID.toString(), 0.5676D, 2));
		}

		else if (name.equals("armor_break")) {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
					.registerPotionAttributeModifier(SharedMonsterAttributes.ARMOR, ARMORID.toString(), -4D, 0));
		}

		else {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name));
		}

		this.isActive = isActive;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

		// クリエなら終了
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) { return; }

		World world = entity.world;

		// リフレッシュ・エフェクト
		if (this == PotionInit.refresh_effect) {

			try {
				this.actionReflash(world, entity);
			}

			catch (Throwable e) { }

		}

		// スロー
		if (this == PotionInit.slow && !entity.isPotionActive(PotionInit.timestop)) {
			this.actionSlow(world, entity);
		}

		// サイクロン
		if (this == PotionInit.cyclone) {
			this.actionCyclon(world, entity);
		}

		// 重力加速
		if (this == PotionInit.gravity_accele) {
			this.actionGravityAccele(world, entity, amplifier);
		}

		// 重力
		else if (this == PotionInit.gravity) {

			if (!entity.onGround) {
				entity.motionY -= 0.1F + amplifier * 0.1F;
			}

			if(entity.getActivePotionEffect(PotionInit.gravity).getAmplifier() >= amplifier) {
				entity.motionX *= 0.75F;
				entity.motionZ *= 0.75F;
			}

		}

		// マインドコントロール
		else if (this == PotionInit.mind_control) {
			this.mindContorol(world, entity, amplifier);
		}

		// 泡状態
		else if (this == PotionInit.babule) {

			entity.motionY = 0.0814125F;
			entity.motionX *= 0.67;
			entity.motionZ *= 0.67;

			int time = entity.getActivePotionEffect(PotionInit.babule).getDuration();

			if (time % 20 == 0) {

				entity.playSound(SMSoundEvent.BABULE, 1F, (world.rand.nextFloat() * 0.3F) + 1F);

				if (amplifier >= 1) {
					entity.attackEntityFrom(DamageSource.FALL, 1F);
					entity.hurtResistantTime = 0;
				}
			}

			if (amplifier >= 3) {

				if (time % 100 == 0) {
					WorldHelper.magicExplo(world, entity, 5F, amplifier + 2F);
				}

				else if (time == 1) {
					WorldHelper.magicExplo(world, entity, 3F, amplifier + 1F);
				}
			}
		}

		// リジェネ
		else if (this == PotionInit.regene && entity.getActivePotionEffect(PotionInit.regene).getDuration() % 60 == 0) {

			// 体力が最大以上なら
			if (entity.getHealth() < entity.getMaxHealth() && entity.isEntityAlive()) {
				entity.setHealth(entity.getHealth() + (amplifier + 1));
				ParticleHelper.spawnHeal(entity, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			}
		}

		// 猛毒状態なら
		else if (this == PotionInit.deadly_poison && !entity.isPotionActive(PotionInit.grant_poison)) {

			if (entity.getHealth() > 1F) {
				entity.setHealth(entity.getHealth() - 1);
				world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.NEUTRAL, 1F, 1F);
			}

			if (!(entity instanceof EntityPlayer)) { return; }

			EntityPlayer player = (EntityPlayer) entity;
			player.addExhaustion(amplifier + 3);
		}

		// 燃焼状態
		else if (this == PotionInit.flame) {
			DamageSource src = SMDamage.flameDamage;
			entity.attackEntityFrom(src, 1F);
			entity.hurtResistantTime = 0;
		}

		// エーテルシールド
		else if (this == PotionInit.aether_shield && entity.getHealth() < entity.getMaxHealth()) {
			entity.setHealth(entity.getHealth() + 1.5F);
		}

		// 魔法陣なら
		else if (this == PotionInit.magic_array && world.isRemote && entity.ticksExisted <= 60) {

			Random rand = world.rand;
			if (rand.nextFloat() > 0.8F) { return; }

			float f1 = (float) (entity.posX - 0.5F + entity.motionX * 0.25F + this.getRandFloat(rand, 1.5F));
			float f2 = (float) (entity.posY - 0.25F + rand.nextFloat() * 0.5 + entity.motionY * 0.25F);
			float f3 = (float) (entity.posZ - 0.5F + entity.motionZ * 0.25F + this.getRandFloat(rand, 1.5F));
			float x = this.getRandFloat(rand, 0.2F);
			float y = (rand.nextFloat() + rand.nextFloat()) * 0.0825F;
			float z = this.getRandFloat(rand, 0.2F);

			Particle effect = ParticleNomal.create(world, f1, f2, f3, x, y, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	public float getRandFloat (Random rand, float rate) {
		return (rand.nextFloat() - rand.nextFloat()) * rate;
	}

	public boolean isReady(int duration, int amplifier) {

		if (this == PotionInit.deadly_poison || this == PotionInit.flame) {
			int j = 32 >> amplifier;
			return j > 0 ? duration % j == 0 : true;
        }

		if (this == PotionInit.aether_shield && amplifier > 0 && duration % 20 == 0) {
			return true;
		}

		return this.isActive;
	}

	// デバフ解除
	public void actionReflash (World world, EntityLivingBase entity) {

		for (PotionEffect effect : entity.getActivePotionEffects()) {

			// デバフなら
			Potion potion = effect.getPotion();
			if (potion.isBadEffect()) {
				entity.removePotionEffect(potion);
			}
		}
	}

	// スロー
	public void actionSlow (World world, EntityLivingBase entity) {
		PotionEffect effect = entity.getActivePotionEffect(PotionInit.slow);
		float power = 1F - (effect.getAmplifier() + 1) * 0.1F;
		entity.motionX *= power;
		entity.motionY *= power;
		entity.motionZ *= power;
	}

	// サイクロン
	public void actionCyclon (World world, EntityLivingBase entity) {

		if (entity.moveForward > 0 && entity.motionX * entity.motionX + entity.motionY * entity.motionY + entity.motionZ * entity.motionZ < 0.35) {
			entity.motionX *= 1.0125;
			entity.motionZ *= 1.0125;
		}

		int level = entity.getActivePotionEffect(PotionInit.cyclone).getAmplifier();
		if (world.isRemote || level == 0) { return; }

		List<Entity> list = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(4, 3, 4));

		for (Entity e : list) {

			if (e == null || !this.checkEntity(e)) { continue; }

			Vec3d p = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d t = new Vec3d(e.posX, e.posY, e.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);
			e.motionX += r.x * 0.1;
			e.motionY += r.y * 0.1;
			e.motionZ += r.z * 0.1;
		}

		if (entity.ticksExisted % 70 != 0) { return; }

		// パーティクルを出す
		ParticleHelper.spawnHeal(entity, EnumParticleTypes.SWEEP_ATTACK, 8, 1, 4);
	}

	// 重量加速
	public void actionGravityAccele (World world, EntityLivingBase entity, int amplifier) {

		entity.fallDistance = 0;
		int level = amplifier;
		if (world.isRemote) { return; }

		if (level == 0) {

			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(1.5F, 2F, 1.5F));

			for (Entity e : list) {

				if (e == null || !this.checkEntity(e)) { continue; }

				e.motionX = 0;
				e.motionZ = 0;
				e.motionY -= 0.5F;
			}
		}

		else {

			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(4F, 3F, 4F));

			for (Entity e : list) {

				if (e == null || !this.checkEntity(e)) { continue; }

				NBTTagCompound tags = e.getEntityData();
				if (tags.getBoolean("isCounter")) { continue; }

				e.motionX = -e.motionX;
				e.motionZ = -e.motionZ;
				e.motionY += 0.35F;
				tags.setBoolean("isCounter", true);
			}
		}
	}

	public void mindContorol (World world, EntityLivingBase entity, int amplifier) {

		// 敵モブ以外は終了
		if (!(entity instanceof EntityLiving)) { return; }

		// 攻撃対象が敵モブなら終了
		EntityLiving living = (EntityLiving) entity;
		EntityLivingBase target = living.getAttackTarget();
		if (target instanceof IMob && target.isEntityAlive()) { return; }

		// 範囲のえんちちーの取得と攻撃対象
		EntityLivingBase attack = null;
		float distance = 24F;
		List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, entity.getEntityBoundingBox().grow(24F, 8F, 24F));

		for (EntityLivingBase e : entityList) {

			// エンティティが同じなら終了
			if (e == entity || !(e instanceof IMob) || e.isPotionActive(PotionInit.mind_control) || !e.canEntityBeSeen(entity)) { continue; }

			// 距離の比較
			if (e.getDistance(entity) < distance) {
				distance = e.getDistance(entity);
				attack = e;
			}
		}

		// 攻撃対象を見つけたら
		if (attack != null) {
			SMUtil.tameAIAnger(living, attack);
		}
	}

	// えんちちーチェック
	public boolean checkEntity (Entity entity) {

		boolean flag = false;

		if (entity instanceof EntityArrow) {
			flag = true;
		} else if (entity instanceof EntityThrowable) {
			flag = true;
		} else if (entity instanceof EntityFireball) {
			flag = true;
		} else if (entity instanceof IProjectile) {
			flag = false;
		} else if (entity instanceof IMob) {
			flag = true;
		}

		if (flag && entity instanceof EntityBaseMagicShot) {
			flag = false;
		}

		return flag;
	}
}
