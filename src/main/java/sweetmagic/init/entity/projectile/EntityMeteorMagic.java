package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.init.PotionInit;

public class EntityMeteorMagic extends EntityFireMagic {

	private static final int MAGMA = Block.getStateId(Blocks.MAGMA.getDefaultState());

	public EntityMeteorMagic(World world) {
		super(world);
		this.setSize(1.2F, 4F);
	}

	public EntityMeteorMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityMeteorMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		this(world, thrower.posX, thrower.posY + (double) thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);
		this.thrower = thrower;
		this.stack = stack;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() { }

	public void onUpdate() {
		super.onUpdate();
		this.setFire(1);
	}

	@Override
	protected void entityHit(EntityLivingBase living) {

		super.entityHit(living);
		this.playSound(living, SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F);
		double range = 7.5D + this.getWandLevel() * 0.5D;
		float dame = (float) this.getDamage() * 1.5F;

		this.attackDamage(living, (float) this.getDamage() * 2F);
		this.checkShadow(living);
		living.hurtResistantTime = 0;

		// 範囲攻撃
		this.rangeAttack(range, dame);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		this.world.playEvent(2001, this.getPosition(), MAGMA);
		this.playSound(this, SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F);
		double range = 4D + this.getWandLevel() * 0.35D;
		float dame = (float) this.getDamage() * 1.125F;

		// 範囲攻撃
		this.rangeAttack(range, dame);
		super.inGround(result);
	}

	protected void onHit(RayTraceResult result) {

		if (this.world.isRemote) {

			Random rand = this.world.rand;

			for (int i = 0; i < 16; i++) {
				this.world.spawnParticle(EnumParticleTypes.FLAME,
						this.posX + rand.nextFloat() - 0.5F,
						this.posY,
						this.posZ + rand.nextFloat() - 0.5F,
						(rand.nextDouble() - 0.5D) * 1.0D, rand.nextDouble() / 4D, (rand.nextDouble() - 0.5D) * 1.0D);
			}

			for (int x = -2; x < 2; x++) {
				for (int z = -2; z < 2; z++) {
					this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,
							this.posX + rand.nextFloat() - 0.5F + x,
							this.posY,
							this.posZ + rand.nextFloat() - 0.5F + z,
							(rand.nextDouble() - 0.5D) * 1.0D, rand.nextDouble() / 4D, (rand.nextDouble() - 0.5D) * 1.0D);
				}
			}

			this.world.spawnParticle(EnumParticleTypes.CLOUD,
					this.posX + rand.nextFloat() - 0.5F,
					this.posY,
					this.posZ + rand.nextFloat() - 0.5F,
					(rand.nextDouble() - 0.5D) * 1.0D, rand.nextDouble() / 4D, (rand.nextDouble() - 0.5D) * 1.0D);
		}

		// 経験値追加処理
		this.addExp();
		super.onHit(result);
	}

	// 範囲攻撃
	public void rangeAttack (double range, float dame) {

		range = this.isPlayerThrower ? range : range * 0.75D;

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, range, range, range);

		for (EntityLivingBase entity : entityList) {

			if (!this.checkThrower(entity)) { continue; }

			// プレイヤーが攻撃した時
			if (this.isPlayerThrower) {
				this.attackDamage(entity, dame);
				this.checkShadow(entity);
				entity.hurtResistantTime = 0;
			}

			// プレイヤー以外が攻撃したとき
			else {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.getThrower()), dame);
			}

			entity.setFire(8 * (this.level + 1));
			entity.addPotionEffect(new PotionEffect(PotionInit.flame, 50 * (this.level + 1), 2));
			entity.hurtResistantTime = 0;
		}
	}
}
