package sweetmagic.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityFrostRain extends EntityFrostMagic {

	public EntityFrostRain(World world) {
		super(world);
		this.setSize(0.25F, 0.25F);
		this.isHit = true;
		this.plusTick = -200;
	}

	public EntityFrostRain(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityFrostRain(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack, false);
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() { }

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		this.ticksInAir = 0;
		this.tickTime++;

		// (レベル × 補正値) + (レベル + 追加ダメージ) ÷ (レベル ÷ 2) + 追加ダメージ
		float power = this.getPower(this.level) / 2;
		int range = 8;

		for (int i = 0; i < 2; i++) {

			double x = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
			double y = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
			double z = (this.rand.nextDouble() * range) - this.rand.nextDouble() * range;
	        BlockPos pos = new BlockPos(this.posX + x, this.posY + y, this.posZ + z);

			if (!this.world.isRemote) {
				EntityFrostMagic entity = new EntityFrostMagic(this.world, pos.getX(), pos.getY() + 20, pos.getZ());
				entity.shoot(0, entity.motionY - 0.33F, 0, 0, 0);
				entity.motionY -= 1.5;
				entity.setDamage(this.getDamage() + power);
				this.world.spawnEntity(entity);
			}

			this.world.playSound(null, pos, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.33F, 0.67F);
		}

		if (this.tickTime > 160) {
			this.setEntityDead();
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) { }

	// 火力取得
	public float getPower (float level) {
		return (level * 0.5F) + (level + 1) / (level / 2) + 1;
	}
}
