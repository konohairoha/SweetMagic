package sweetmagic.init.entity.projectile;

import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.PotionInit;

public class EntityPoisonMagic extends EntityBaseMagicShot {

	public EntityPoisonMagic(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
	}

	public EntityPoisonMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityPoisonMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.spawnEntity();
		this.setEntityDead();
	}

	public void spawnEntity () {

		if (!this.world.isRemote) {

			int level = IWand.getWand(this.stack).getLevel(this.stack);

			EntityAreaEffectCloud entity = new EntityAreaEffectCloud(this.world, this.posX, this.posY, this.posZ);
	        entity.setOwner(this.getThrower());
	        entity.setParticle(EnumParticleTypes.SPELL_WITCH);
	        entity.setRadius(1F + level);
			entity.setDuration(200 + (level * 50));
			entity.setRadiusPerTick((7F - entity.getRadius()) / (float) entity.getDuration());
	        entity.addEffect(new PotionEffect(PotionInit.deadly_poison, 201, 1));
	        this.world.spawnEntity(entity);
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
