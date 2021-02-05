package sweetmagic.event;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.util.ParticleHelper;

public class LivingPotionEvent {

	public int tickTime = 0;

	@SubscribeEvent
	public void livingEvent (LivingEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		if (entity == null) { return; }

		this.tickTime++;
		World world = entity.getEntityWorld();

		// サイクロン
		if (entity.isPotionActive(PotionInit.cyclone)) {
			this.actionCyclon(world, entity);
		}

		// 重力加速
		if (entity.isPotionActive(PotionInit.gravity_accele)) {
			this.actionGravityAccele(world, entity);
		}

		// 重力
		else if(!entity.onGround && entity.isPotionActive(PotionInit.gravity)) {
			float neg = 0.1F + entity.getActivePotionEffect(PotionInit.gravity).getAmplifier() * 0.15F;
			entity.motionY -= neg;
		}

		// スロー
		if (entity.isPotionActive(PotionInit.slow) && !entity.isPotionActive(PotionInit.timestop)) {
			this.actionSlow(world, entity);
		}

		// デバフ解除
		if (this.tickTime % 20 == 0 /*&& !world.isRemote*/ && entity.isPotionActive(PotionInit.refresh_effect)) {
			this.actionReflash(world, entity);
		}

//		// 盲目ならタゲをnullに書き換え
//		if (this.tickTime % 20 == 0 && entity.isPotionActive(MobEffects.BLINDNESS) && entity instanceof EntityLiving) {
//			SMUtil.tameAIAnger((EntityLiving) entity, null);
//		}

		// 飛躍上昇なら
		if (entity.isPotionActive(MobEffects.JUMP_BOOST)) {
			entity.fallDistance = 0;
		}

		if (this.tickTime % 140 == 0) {
			this.tickTime = 0;
		}
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

		if (this.tickTime % 70 != 0) { return; }

		// パーティクルを出す
		ParticleHelper.spawnHeal(entity, EnumParticleTypes.SWEEP_ATTACK, 8, 1, 4);
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

	// 重量加速
	public void actionGravityAccele (World world, EntityLivingBase entity) {

		entity.fallDistance = 0;

		int level = entity.getActivePotionEffect(PotionInit.gravity_accele).getAmplifier();
//		if (level == 0) { return; }

//		if (PlayerHelper.isPlayer(entity)) {
//
//			EntityPlayer player = (EntityPlayer) entity;
//
//			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
//				player.motionY += 0.0045;
//			}
//		}

		if (world.isRemote) { return; }

		if (level == 0) {

//			float entityRange = (float) (entity.posX + entity.posY + entity.posZ);
			List<Entity> list = world.getEntitiesWithinAABB(Entity.class, entity.getEntityBoundingBox().grow(1.5F, 2F, 1.5F));

			for (Entity e : list) {

				if (e == null || !this.checkEntity(e)) { continue; }

				e.motionX = 0;
				e.motionZ = 0;
				e.motionY -= 0.5F;

//				float targetRange = (float) (e.posX + e.posY + e.posZ);
//				float range = entityRange - targetRange;
//				range = range < 0 ? range * -1 : range;
//
//				e.motionX *= this.getGravityPowet(range, 0.8F);
//				e.motionY -= this.getGravityPowet(range, 0.1F);
//				e.motionZ *= this.getGravityPowet(range, 0.8F);
			}

			if (this.tickTime % 70 != 0) { return; }

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

	public float getGravityPowet (float range, float power) {
		return range > 16 ? 0F : 1F - power * ( (16 - range) / 16);
	}

	// スロー
	public void actionSlow (World world, EntityLivingBase entity) {

		PotionEffect effect = entity.getActivePotionEffect(PotionInit.slow);
		float power = 1F - (effect.getAmplifier() + 1) * 0.1F;
		entity.motionX *= power;
		entity.motionY *= power;
		entity.motionZ *= power;
	}

	// デバフ解除
	public void actionReflash (World world, EntityLivingBase entity) {
		for (Potion potion : PotionInit.getDeBuffPotionList()) {
			entity.removePotionEffect(potion);
		}
	}

	public boolean checkEntityAll (Entity entity) {
		return entity instanceof EntityPlayer || entity instanceof EntityXPOrb || entity instanceof EntityBaseMagicShot;
	}
}
