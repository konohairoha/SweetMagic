package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.ISMMob;

public class EntityBlazeCyclone extends EntityFireball {

	public EntityBlazeCyclone(World worldIn) {
		super(worldIn);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntityBlazeCyclone(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
		super(worldIn, shooter, accelX, accelY, accelZ);
		this.setSize(0.3125F, 0.3125F);
	}

	public EntityBlazeCyclone(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
		super(worldIn, x, y, z, accelX, accelY, accelZ);
		this.setSize(0.3125F, 0.3125F);
	}

	public static void registerFixesSmallFireball(DataFixer fixer) {
		EntityBlazeCyclone.registerFixesFireball(fixer, "SmallFireball");
	}

	protected boolean isFireballFiery() {
		return false;
	}

	public void onUpdate() {
		super.onUpdate();

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

	protected void onImpact(RayTraceResult result) {

		float dame = 2F;
		List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, this.getEntityBoundingBox().grow(7.5D, 2D, 7.5D));

		// 射撃したのがブレイズテンペストなら
		if (this.shootingEntity instanceof EntityBlazeTempest) {
			EntityBlazeTempest tempest = (EntityBlazeTempest) this.shootingEntity;

			// テンペストかしていたら火力アップ
			if (tempest.isTempest()) { dame *= 2F; }
		}

		for (Entity entity : list ) {

			// テンペストとこの射撃えんちちーなら次へ
			if (entity instanceof ISMMob || entity instanceof EntityBlazeCyclone) { continue; }

			if (entity instanceof EntityLivingBase) {

				EntityLivingBase living = (EntityLivingBase) entity;

				// 疾風が付いていたら飛ばさない
				if (living.isPotionActive(PotionInit.cyclone)) {
					continue;
				}
			}

			// 向き取得
			Vec3d p = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d t = new Vec3d(entity.posX, entity.posY, entity.posZ);
			Vec3d r = new Vec3d(t.x - p.x, t.y - p.y, t.z - p.z);

			// えんちちーを飛ばす
			entity.motionX += r.x / 2 ;
			entity.motionY += r.y / 2;
			entity.motionZ += r.z / 2;

			// 生きているえんちちーならダメージを与える
			if (!(entity instanceof IMob) && entity instanceof EntityLivingBase) {
				entity.attackEntityFrom(DamageSource.MAGIC, dame);
			}
		}

		this.setDead();
	}

	public boolean canBeCollidedWith() {
		return false;
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		return false;
	}
}
