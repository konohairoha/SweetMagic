package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.ParticleHelper;

public class EntityPoisonMagic extends EntityBaseMagicShot {

	public EntityPoisonMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityPoisonMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityPoisonMagic(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;

		if (thrower instanceof EntityPlayer) {
			this.isRange = this.hasAcce((EntityPlayer) thrower, ItemInit.extension_ring);
		}
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.spawnEntity();
		this.setEntityDead();
	}

	public void spawnEntity () {

		if (!this.world.isRemote) {

			int level = this.getWandLevel();

			// tier1
			if (this.data == 0) {

				EntityAreaEffectCloud entity = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
		        entity.setOwner(this.getThrower());
		        entity.setParticle(EnumParticleTypes.SPELL_WITCH);
		        entity.setRadius(1F + level);
				entity.setDuration(200 + (level * 50));
				entity.setRadiusPerTick((7F - entity.getRadius()) / (float) entity.getDuration());
		        entity.addEffect(new PotionEffect(PotionInit.deadly_poison, 201, 1));
		        this.world.spawnEntity(entity);
			}

			// tier3
			else if (this.data == 1) {

				float range = (2 + level * 0.75F) * (this.isRange ? 1.33F : 1F);
				float dame = level;
				List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, range, range, range);
				if (list.isEmpty()) { return; }

				for (EntityLivingBase entity : list ) {

					if (!(entity instanceof IMob)) { continue; }

					this.attackDamage(entity, dame);
					entity.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, 40 * (level + 1), 2));
					entity.hurtResistantTime = 0;

					BlockPos pos = new BlockPos(entity);

					ParticleHelper.spawnParticle(this.world, pos, EnumParticleTypes.SPELL_WITCH);
					ParticleHelper.spawnParticle(this.world, pos.up(), EnumParticleTypes.SPELL_WITCH);
				}
			}

			// tier3
			else if (this.data == 2) {

				float range = (5 + level * 0.875F) * (this.isRange ? 1.4F : 1F);
				float dame = (float) (level + this.getDamage() * 0.25F);
				List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, range, range, range);
				if (list.isEmpty()) { return; }

				for (EntityLivingBase entity : list ) {

					if (!(entity instanceof IMob)) { continue; }

					boolean isHit = this.attackDamage(entity, dame);

					if (entity.isEntityAlive()) {
						entity.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, 40 * (level + 1), 3));
					}

					entity.hurtResistantTime = 0;
					BlockPos pos = new BlockPos(entity);

					ParticleHelper.spawnParticle(this.world, pos, EnumParticleTypes.SPELL_WITCH);
					ParticleHelper.spawnParticle(this.world, pos.up(), EnumParticleTypes.SPELL_WITCH);

					if (isHit) {

						// リフレッシュエフェクトが付いてる場合は強制体力削り
						if (entity.isPotionActive(PotionInit.refresh_effect) && entity.isEntityAlive()) {
							entity.setHealth(Math.max(1F, entity.getHealth() - entity.getMaxHealth() * 0.05F));
						}

						// 防御力ダウン付与
						else {
							entity.addPotionEffect(new PotionEffect(PotionInit.armor_break, 40 * (level + 1), 3));
						}
					}

					// 攻撃が通らなかった場合強制体力半減
					else {
						entity.setHealth(Math.max(1F, entity.getHealth() - entity.getMaxHealth() * 0.5F));
					}
				}
			}
		}

		// 経験値追加処理
		this.addExp();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX / 16);
		float y = (float) (-this.motionY / 16);
		float z = (float) (-this.motionZ / 16);

		for (int i = 0; i < 4; i++) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			this.world.spawnParticle(EnumParticleTypes.SPELL_WITCH, f1, f2, f3, x, y, z);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {
		this.spawnEntity();
	}
}
