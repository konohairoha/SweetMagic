package sweetmagic.init.potion;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
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
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class PotionSM extends PotionBase {

	public final boolean isActive;
	public static final UUID PID = UUID.fromString("CE9DBC2A-EE3F-43F5-9DF7-F7F1EE491222");

	public PotionSM (boolean effect, int color, String name, String dir, boolean isActive) {
		super(effect, color, name, dir);

		if (name.equals("babule")) {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name)
					.registerPotionAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, PID.toString(), -0.025D, 2));
		}

		else {
			ForgeRegistries.POTIONS.register(this.setRegistryName(SweetMagicCore.MODID, name));
		}

		this.isActive = isActive;
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {

		// クリエなら終了
		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			return;
		}

		World world = entity.world;

		// リフレッシュ・エフェクト
		if (this == PotionInit.refresh_effect) {
			this.actionReflash(world, entity);
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
		else if (this == PotionInit.gravity && !entity.onGround ) {
			entity.motionY -= 0.1F + amplifier * 0.1F;
		}

		// 泡状態
		if (this == PotionInit.babule) {
			entity.motionY = 0.0814125F;
			entity.motionX *= 0.67;
			entity.motionZ *= 0.67;

			if (amplifier >= 1 && entity.getActivePotionEffect(PotionInit.babule).getDuration() % 20 == 0) {
				entity.attackEntityFrom(DamageSource.FALL, 1F);
			}
		}

		// リジェネ
		if (this == PotionInit.regene && entity.getActivePotionEffect(PotionInit.regene).getDuration() % 60 == 0) {

			// 体力が最大以上なら
			if (entity.getHealth() < entity.getMaxHealth() && !entity.isDead) {
				entity.setHealth(entity.getHealth() + (amplifier + 1));
				ParticleHelper.spawnHeal(entity, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
			}
		}

		// 猛毒状態なら
		if (this == PotionInit.deadly_poison && !entity.isPotionActive(PotionInit.grant_poison)) {

			if (entity.getHealth() > 1F) {
				entity.setHealth(entity.getHealth() - 1);
				entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.NEUTRAL, 1F, 1F);
			}

			if (!(entity instanceof EntityPlayer)) { return; }

			EntityPlayer player = (EntityPlayer) entity;
			player.addExhaustion(amplifier + 3);
		}

		// 燃焼状態
		if (this == PotionInit.flame) {

			if (entity.getHealth() > 1F) {
				entity.setHealth(entity.getHealth() - 1);
				entity.world.playSound(null, new BlockPos(entity), SoundEvents.ENTITY_PLAYER_HURT, SoundCategory.NEUTRAL, 1F, 1F);
			}

			else {
				entity.attackEntityFrom(DamageSource.WITHER, 1F);
			}
		}
	}

	public boolean isReady(int duration, int amplifier) {

		if (this == PotionInit.deadly_poison || this == PotionInit.flame) {
			int j = 32 >> amplifier;
			return j > 0 ? duration % j == 0 : true;
        }

		return this.isActive;
	}

	// デバフ解除
	public void actionReflash (World world, EntityLivingBase entity) {
		for (Potion potion : PotionInit.getDeBuffPotionList()) {
			entity.removePotionEffect(potion);
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

	// えんちちーチェック
	public boolean checkEntity (Entity e) {

		boolean flag = false;

		if (e instanceof EntityArrow) {
			flag = true;
		} else if (e instanceof EntityThrowable) {
			flag = true;
		} else if (e instanceof EntityFireball) {
			flag = true;
		} else if (e instanceof IProjectile) {
			flag = false;
		} else if (e instanceof IMob) {
			flag = true;
		}

		return flag;
	}
}
