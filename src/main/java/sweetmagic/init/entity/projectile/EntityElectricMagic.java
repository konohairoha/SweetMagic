package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.util.PlayerHelper;

public class EntityElectricMagic extends EntityBaseMagicShot {

	public EntityElectricMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityElectricMagic(World world, double x, double y, double z) {
		super(world, x, y , z);
	}

	public EntityElectricMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
		this.isHit = true;
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		this.ticksInAir = 0;
		float dame = (float) (this.getDamage() * 0.625);

		List<EntityLivingBase> list = this.getEntityList(2.5D, 2.5D, 2.5D);
		for (EntityLivingBase entity : list) {

			// プレイヤーが攻撃した時
			if (PlayerHelper.isPlayer(this.thrower)) {
				this.attackDamage(entity, dame);
				this.checkShadow(entity);
			}

			// プレイヤー以外が攻撃したとき
			else {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
			}

			// 採掘速度低下中なら弱体化付与
			if (entity.isPotionActive(MobEffects.MINING_FATIGUE)) {
				entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100 * this.level, 0));
			}
		}

		this.tickTime++;

		if (this.tickTime > 8) {
			this.setEntityDead();
		}

		if (this.tickTime == 1) {
			this.playSound(this, SMSoundEvent.ELECTRIC, 0.5F, 1F);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		living.hurtResistantTime = 0;
		float dame = (float) (this.getDamage() * 0.85F);

		for (EntityLivingBase entity : this.getEntityList(3.8D, 3.8D, 3.8D)) {

			// プレイヤーが攻撃した時
			if (PlayerHelper.isPlayer(this.thrower)) {
				entity.attackEntityFrom(DamageSource.causePlayerDamage(this.getPlayer()), dame);
			}

			// プレイヤー以外が攻撃したとき
			else {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
			}

			entity.hurtResistantTime = 0;
		}
	}
}
