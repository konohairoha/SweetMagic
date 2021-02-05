package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;
import sweetmagic.util.PlayerHelper;

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
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		List<EntityLivingBase> list = this.getEntityList(4D, 2.5D, 4D);
		float dame = 0.5F + 0.5F * this.getWandLevel();

		// 風攻撃
		this.cycloneAttack(list, dame);
		this.setEntityDead();

		// 経験値追加処理
		this.addExp();
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

		List<EntityLivingBase> list = this.getEntityList(5.75D, 3.5D, 5.75D);
		float dame = 1.5F + 0.75F * this.getWandLevel();

		// 風攻撃
		this.cycloneAttack(list, dame - 1F);

		// 経験値追加処理
		this.addExp();
	}

	// 風攻撃
	public void cycloneAttack (List<EntityLivingBase> list, float dame) {

		for (EntityLivingBase entity : list ) {

			if (this.isPlayer(entity) || this.isNotPlayer(entity)) { continue; }

			Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);

			if (this.isHit) {
				entity.motionX += r.x / 12;
				entity.motionY += 1.75;
				entity.motionZ += r.z / 12;
			}

			else {
				entity.motionX += r.x * 0.67;
				entity.motionY += r.y * 0.67;
				entity.motionZ += r.z * 0.67;
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
			if (PlayerHelper.isPlayer(this.getThrower())) {
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
