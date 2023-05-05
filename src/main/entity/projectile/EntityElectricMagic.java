package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.util.EventUtil;
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
		this.data = 0;
		this.isHit = true;
	}

	public EntityElectricMagic(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;
		this.isHit = true;
	}

	public EntityElectricMagic(World world, EntityLivingBase thrower, int data) {
		super(world, thrower, ItemStack.EMPTY);
		this.data = data;
		this.isHit = true;
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		this.ticksInAir = 0;
		float dame = (float) (this.getDamage() * 0.65D);

		if (this.data == 0) {

			boolean isPlayer = PlayerHelper.isPlayer(this.thrower);

			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, 2D, 2D, 2D);
			for (EntityLivingBase entity : list) {

				// プレイヤーが攻撃した時
				if (isPlayer && entity instanceof IMob ) {
					this.attackDamage(entity, dame);
					this.checkShadow(entity);
				}

				// プレイヤー以外が攻撃したとき
				else if (!isPlayer && !(entity instanceof IMob) ) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
				}

				// 採掘速度低下中なら弱体化付与
				if (entity.isPotionActive(MobEffects.MINING_FATIGUE)) {
					entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100 * this.level, 0));
				}
			}
		}

		else if (this.data == 1) {

			List<EntityPlayer> list = this.getEntityList(EntityPlayer.class, 2D, 2D, 2D);

			for (EntityPlayer entity : list) {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
				entity.getCooldownTracker().setCooldown(entity.getHeldItemMainhand().getItem(), 100);
			}
		}

		else if (this.data == 2) {

			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, 3D, 3D, 3D);
			for (EntityLivingBase entity : list) {

				if(!(entity instanceof IMob) ) { continue; }

				this.attackDamage(entity, dame);
				this.checkShadow(entity);

				// 1秒間敵を動かなくさせる
				if (entity.isNonBoss() && entity instanceof EntityLiving) {
					EventUtil.tameAIDonmov((EntityLiving) entity, 1);
				}

				entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 20 * (this.level + 1), 1));
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
		float dame = (float) (this.getDamage() * 0.875F);

		if (this.data == 0) {

			boolean isPlayer = PlayerHelper.isPlayer(this.thrower);
			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, 3.5D, 3.5D, 3.5D);

			for (EntityLivingBase entity : list) {

				// プレイヤーが攻撃した時
				if (isPlayer && entity instanceof IMob ) {
					this.attackDamage(entity, dame);
					this.checkShadow(entity);
				}

				// プレイヤー以外が攻撃したとき
				else if (!isPlayer && !(entity instanceof IMob) ) {
					entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
				}
			}
		}

		else if (this.data == 1) {

			List<EntityPlayer> list = this.getEntityList(EntityPlayer.class, 3.5D, 3.5D, 3.5D);

			for (EntityPlayer entity : list) {
				entity.attackEntityFrom(DamageSource.causeMobDamage(this.thrower), dame);
				entity.getCooldownTracker().setCooldown(entity.getHeldItemMainhand().getItem(), 35);
			}
		}

		else if (this.data == 2) {

			List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, 4.5D, 4.5D, 4.5D);
			for (EntityLivingBase entity : list) {

				if(!(entity instanceof IMob) ) { continue; }

				this.attackDamage(entity, dame);
				this.checkShadow(entity);

				// 2秒間敵を動かなくさせる
				if (entity.isNonBoss() && entity instanceof EntityLiving) {
					EventUtil.tameAIDonmov((EntityLiving) entity, 2);
				}

				entity.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 60 * (this.level + 1), 2));
			}
		}
	}
}
