package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;

public class EntityFlameNova extends EntityFireMagic {

	public EntityFlameNova(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityFlameNova(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityFlameNova(World world, EntityLivingBase thrower, ItemStack stack) {
		this(world, thrower.posX, thrower.posY + (double) thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);
		this.thrower = thrower;
		this.stack = stack;
		this.isFire = false;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		for (int i = 0; i < 6; i++) {
			super.spawnParticle();
		}
	}

	@Override
	protected void entityHit(EntityLivingBase living) {

		super.entityHit(living);

		this.playSound(living, SoundEvents.ENTITY_GENERIC_EXPLODE, 0.45F, 1F);
		int level = this.getWandLevel();
		double range = 5D;

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, range, range, range);

		for (EntityLivingBase entity : entityList) {

			if (!this.checkThrower(entity)) { continue; }

			if (this.isPlayerThrower) {
				entity.setFire(3 * (level + 1));
			}

			entity.addPotionEffect(new PotionEffect(PotionInit.flame, 40 * (level + 1), 1));
			this.checkShadow(entity);
		}
	}

	protected void onHit(RayTraceResult result) {

		if (this.world.isRemote) {

			Random rand = this.world.rand;

			for (int i = 0; i < 16; i++) {
				this.world.spawnParticle(EnumParticleTypes.FLAME,
						this.posX + rand.nextFloat() - 0.5F,
						this.posY + rand.nextFloat() - 0.5F,
						this.posZ + rand.nextFloat() - 0.5F,
						(rand.nextDouble() - 0.5D) * 1.0D, rand.nextDouble() / 4D, (rand.nextDouble() - 0.5D) * 1.0D);
			}
		}
		super.onHit(result);
	}
}
