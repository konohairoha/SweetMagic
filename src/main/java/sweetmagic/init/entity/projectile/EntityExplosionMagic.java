package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleMagicDig;
import sweetmagic.init.ItemInit;
import sweetmagic.util.ParticleHelper;

public class EntityExplosionMagic extends EntityBaseMagicShot {

	public float exp = 0F;

	public EntityExplosionMagic(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityExplosionMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityExplosionMagic(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;

		if (thrower instanceof EntityPlayer) {
			this.isRange = this.hasAcce((EntityPlayer) thrower, ItemInit.extension_ring);
		}

		switch (this.data) {
		case 0:
			this.exp = 0.15F;
			break;
		case 1:
			this.exp = 0.5F;
			break;
		case 2:
			this.exp = 0.75F;
			break;
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		// 経験値追加処理
		this.addExp();
		float power = this.exp * (this.data == 2 ? 0.925F : 1F);
		this.explosion(this.getWandLevel() * power);
		this.setEntityDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		Particle effect = ParticleMagicDig.create(this.world, this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ);
		this.getParticle().addEffect(effect);
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		// 経験値追加処理
		this.addExp();
		float power = this.exp * (this.data == 2 ? 1.5F : 2F);
		this.explosion(this.getWandLevel() * power);
	}

	// 爆発
	public void explosion (float explo) {

		if (this.data == 2) {
			this.createExplo(explo);
		}

		else {
			this.world.createExplosion(this.getThrower(), this.posX, this.posY, this.posZ, explo, this.data == 0);
		}
	}

	public void createExplo (float explo) {

		explo = this.isRange ? explo * 1.33F : explo;

		List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, explo, explo, explo);
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

		this.playSound(this, SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnParticle(this.world, new BlockPos(this.posX, this.posY + 0.5, this.posZ), EnumParticleTypes.EXPLOSION_HUGE);
	}
}
