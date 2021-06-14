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

	public void onUpdate() {

		super.onUpdate();

		if (this.ticksInAir >= 7 && !this.isFix) {
			this.ticksInAir = 0;
			this.tickTime++;
			this.isFix = true;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
			this.createExplo(this.getWandLevel() * 1.25F);
		}

		// 一定時間が経ったら
		if (this.isFix) {

			this.ticksInAir = 0;
			this.tickTime++;
			this.motionX = 0;
			this.motionY = 0;
			this.motionZ = 0;
//			System.out.println("|||||" + this.tickTime);

			if (this.tickTime % 9 == 0) {

//				System.out.println("=======");
				Random rand = this.world.rand;
				float explo = this.getWandLevel() * 0.5F;

				for (int i = 0; i < 4; i++) {
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

        AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-explo, -explo / 2, -explo), pos.add(explo, explo / 2, explo));
		List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		if (list.isEmpty()) { return; }

		boolean isIMob = this.getThrower() instanceof IMob;

		for (EntityLivingBase entity : list ) {

			if ( ( !isIMob && !(entity instanceof IMob) ) || ( isIMob && entity instanceof IMob ) ) { continue; }

			float dame = explo;
			double distance = 2 - entity.getDistance(this.posX, this.posY, this.posZ) / dame;
			dame *= distance * 1.825F;
			this.attackDamage(entity, dame);
			entity.hurtResistantTime = 0;
		}

		this.world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 1.25F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnBoneMeal(this.world, pos, EnumParticleTypes.EXPLOSION_HUGE);
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
		living.hurtResistantTime = 0;
		this.createExplo(this.getWandLevel() * 0.75F);
	}

//	// 死亡時
//	public void setDead() {
//
//		super.setDead();
//
//		int level = this.getWandLevel();
//		double range = 5D + this.getWandLevel();
//		List<EntityLivingBase> entityList = this.getEntityList(range, range * 0.75, range);
//
//		for (EntityLivingBase entity : entityList) {
//
//			if (!(entity instanceof IMob)) { continue; }
//
//			this.attackDamage(entity, (float) (range + this.getDamage()) );
//			entity.hurtResistantTime = 0;
//			this.checkShadow(entity);
//
//			entity.addPotionEffect(new PotionEffect(PotionInit.frosty, 40 * (level + 1), 2));
//			this.playSound(entity, SMSoundEvent.FROST, 0.25F, 1F);
//		}
//
//		this.playSound(SoundEvents.ENTITY_BLAZE_SHOOT, 0.5F, 0.67F);
//	}
}
