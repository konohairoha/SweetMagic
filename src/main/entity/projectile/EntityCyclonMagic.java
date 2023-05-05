package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

public class EntityCyclonMagic extends EntityBaseMagicShot {

	public EntityCyclonMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityCyclonMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityCyclonMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);

		if (thrower instanceof EntityPlayer) {
			this.isRange = this.hasAcce((EntityPlayer) thrower, ItemInit.extension_ring);
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		double range = this.isRange ? 6.25D : 4D;

		// 風攻撃
		List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, range, range, range);
		this.cycloneAttack(list, 0.5F + 0.5F * this.getWandLevel());
		this.setEntityDead();

		// 経験値追加処理
		if (this.isPlayerThrower) {
			this.addExp();
		}
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX );
		float y = (float) (-this.motionY );
		float z = (float) (-this.motionZ );

		for (int i = 0; i < 6; ++i) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i * 0.8);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i * 0.8);
			this.world.spawnParticle(EnumParticleTypes.SWEEP_ATTACK, f1, f2, f3, x, y, z);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		double range = this.isRange ? 8.5D : 5.75D;

		// 風攻撃
		List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, range, range, range);
		this.cycloneAttack(list, 0.5F + (this.isRange ? 1F : 0.75F) * this.getWandLevel());

		// 経験値追加処理
		if (this.isPlayerThrower) {
			this.addExp();
		}
	}

	// 風攻撃
	public void cycloneAttack (List<EntityLivingBase> list, float dame) {

		for (EntityLivingBase entity : list ) {

			if (( !(this.getThrower() instanceof IMob) && this.getThrower() == entity ) ||
			entity.isPotionActive(PotionInit.resistance_blow)) { continue; }

			Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);

			if (this.isHit) {
				entity.motionX += r.x / 12;
				entity.motionZ += r.z / 12;

				if (entity.isNonBoss()) {
					entity.motionY += 1.75D;
				}
			}

			else {
				entity.motionX += r.x * 0.67D;
				entity.motionZ += r.z * 0.67D;

				if (entity.isNonBoss()) {
					entity.motionY += r.y * 0.67D;
				}
			}

			// 毒状態なら
			if (entity.isPotionActive(PotionInit.deadly_poison)) {

				// ポーション時間の取得
				float poisonDama = entity.getActivePotionEffect(PotionInit.deadly_poison).getDuration() / 80 + 1;
				entity.removePotionEffect(PotionInit.deadly_poison);
				float health = entity.getHealth();

				// 防御無視ダメージ
				entity.setHealth(health <= poisonDama ? 1 : health - poisonDama);
			}

			// 射撃者がプレイヤーなら
			if (this.isPlayerThrower) {
				this.attackDamage(entity, dame);
				entity.hurtResistantTime = 0;
				this.checkShadow(entity);
			}

			// それ以外
			else {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), dame);
				entity.hurtResistantTime = 0;
			}

			entity.getEntityData().setBoolean("isCyclone", true);
		}

		this.setDead();
	}

	protected void onHit(RayTraceResult result) {

		if (this.world.isRemote) {

			Random rand = this.world.rand;

			for (int i = 0; i < 16; i++) {
				this.world.spawnParticle(EnumParticleTypes.CLOUD,
						this.posX + rand.nextFloat() - 0.5F,
						this.posY + rand.nextFloat() - 0.5F,
						this.posZ + rand.nextFloat() - 0.5F,
						(rand.nextDouble() - 0.5D) * 1.0D, rand.nextDouble() / 4D, (rand.nextDouble() - 0.5D) * 1.0D);
			}
		}

		super.onHit(result);
	}
}
