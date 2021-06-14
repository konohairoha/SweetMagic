package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;
import sweetmagic.util.SMDamage;

public class EntityBlazeEndMagic extends EntityFireMagic {

	public boolean isFix = false;

	public EntityBlazeEndMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.isHit = true;
		this.plusTick = -200;
	}

	public EntityBlazeEndMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBlazeEndMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
		this.isHitDead = true;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float moX = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F: this.motionX);
		float moY = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F : this.motionY);
		float moZ = (float) (this.isFix ? ( this.getRandFloat() - this.getRandFloat() ) * 1.25F : this.motionZ);

		float x = -moX / 12F;
		float y = -moY / 12F;
		float z = -moZ / 12F;

		for (int i = 0; i < 8; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + moX * i * 0.25F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5F + moY * i * 0.25F);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + moZ * i * 0.25F);
			this.world.spawnParticle(EnumParticleTypes.FLAME, f1, f2, f3, x, y, z);
		}
	}

	public float getRandFloat () {
		return this.rand.nextFloat() * 1.5F;
	}

	public void onUpdate() {

		super.onUpdate();

		if (this.ticksInAir >= 5 && !this.isFix) {
			this.ticksInAir = 0;
			this.tickTime++;
			this.isFix = true;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
		}

		// 一定時間が経ったら
		if (this.isFix) {

			this.ticksInAir = 0;
			this.tickTime++;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;

			DamageSource src = SMDamage.flameDamage;
			double range = 5D + this.getWandLevel() * 0.5D;
			List<EntityLivingBase> entityList = this.getEntityList(range, range * 0.75, range);

			for (EntityLivingBase entity : entityList) {

				if (entity instanceof IMob) {

					if (this.ticksExisted % 20 == 0) {
						entity.attackEntityFrom(src, 1F);
						entity.hurtResistantTime = 0;
						this.checkShadow(entity);
					}
				}

				else if (!this.world.isRemote) {
					entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 40 * (this.getWandLevel() + 1), 1, true, true));
				}
			}

			if (this.tickTime > 160) {
				this.setEntityDead();
			}
		}

	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.tickTime == 0) {
			this.setPosition(this.posX, this.posY + 1.25D, this.posZ);
		}

		this.ticksInAir = 0;
		this.tickTime++;
		this.isFix = true;
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();
		int addTime = living.isBurning() ? 2 : 0;

		living.addPotionEffect(new PotionEffect(PotionInit.flame, 40 * (level + 1 + addTime), this.isHitDead ? 3 : 2));
		living.hurtResistantTime = 0;

	}

	// 死亡時
	@Override
	public void setDead() {

		super.setDead();

		int level = this.getWandLevel();
		double range = 5D + this.getWandLevel();
		List<EntityLivingBase> entityList = this.getEntityList(range, range * 0.75, range);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof IMob)) { continue; }

			this.attackDamage(entity, (float) (range + this.getDamage()) );
			entity.hurtResistantTime = 0;
			this.checkShadow(entity);

			entity.addPotionEffect(new PotionEffect(PotionInit.flame, 40 * (level + 1), 2));
		}

		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
	}
}
