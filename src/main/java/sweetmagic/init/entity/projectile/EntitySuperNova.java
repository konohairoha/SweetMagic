package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.util.ParticleHelper;

public class EntitySuperNova extends EntityExplosionMagic {

	public boolean isFix = false;

	public EntitySuperNova(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntitySuperNova(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntitySuperNova(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack, 3);
		this.data = data;
		this.exp = 1F;
		this.plusTick = -200;
		this.isHit = true;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		if (this.motionX != 0 || this.motionZ != 0) {
			super.spawnParticle();
		}
	}

	public void onUpdate() {

		super.onUpdate();
		if (this.world.isRemote) { return; }

		if (this.ticksInAir >= 9 && !this.isFix) {
			this.ticksInAir = 0;
			this.tickTime++;
			this.isFix = true;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			this.createExplo(this.getWandLevel() * 1F);
		}

		// 一定時間が経ったら
		if (this.isFix) {

			this.ticksInAir = 0;
			this.tickTime++;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;

			if (this.tickTime % 9 == 0) {

				Random rand = this.world.rand;
				float explo = this.getWandLevel() * 0.35F;

				for (int i = 0; i < 3; i++) {
					int pX = (int) (this.posX + rand.nextInt(24) - rand.nextInt(24));
					int pZ = (int) (this.posZ + rand.nextInt(24) - rand.nextInt(24));
					this.createExplo(explo, new BlockPos(pX, this.posY, pZ));
				}
			}

			if (this.tickTime >= 45) {
				this.setEntityDead();
			}
		}
	}

	public void createExplo (float explo, BlockPos pos) {

        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-explo, -explo, -explo), pos.add(explo, explo, explo));
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		boolean isIMob = this.getThrower() instanceof IMob;

		for (EntityLivingBase entity : list ) {

			if ( ( !isIMob && !(entity instanceof IMob) ) || ( isIMob && entity instanceof IMob ) ) { continue; }

			float dame = explo;
			double distance = 2 - entity.getDistance(this.posX, this.posY, this.posZ) / dame;
			dame *= distance * 1.825F;
			this.attackDamage(entity, dame);
			entity.hurtResistantTime = 0;
		}

		this.world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.75F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnBoneMeal(this.world, pos, EnumParticleTypes.EXPLOSION_HUGE);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.tickTime == 0) {
			this.setPosition(this.posX, this.posY + 1.25D, this.posZ);
		}

		if (!this.world.isRemote && !this.isFix) {
			this.ticksInAir = 0;
			this.tickTime++;
			this.isFix = true;
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {
		living.hurtResistantTime = 0;
		this.createExplo(this.getWandLevel() * 0.75F);
	}
}
