package sweetmagic.init.entity.projectile;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleEntityMagicLight;
import sweetmagic.init.BlockInit;
import sweetmagic.util.ParticleHelper;

public class EntityLightMagic extends EntityBaseMagicShot {

	public EntityLightMagic(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityLightMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityLightMagic(World world, EntityLivingBase thrower, ItemStack stack) {
		super(world, thrower, stack);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.world.isRemote) { return; }

		try {

			int lightLevel = 0;
			if (result.typeOfHit != Type.BLOCK) { return; }

			// 座標取得の定義
			BlockPos pos = result.getBlockPos().offset(result.sideHit);
			IBlockState state = this.world.getBlockState(pos);
			Material material = state.getMaterial();
			this.setEntityDead();

			if (material == Material.WATER || material == Material.LAVA) { return; }
			if (!state.getBlock().isReplaceable(this.world, pos)) { return; }

			// 光レベルの取得
			lightLevel = this.world.getLightFromNeighbors(pos);

			// 杖と魔法アイテムの取得
			IWand wand = IWand.getWand(this.stack);

			if (!wand.isCreativeWand()) {

				// 習得経験値初期化
				int addExp = 0;
				if (lightLevel >= 10) {
					addExp = 1;
				} else if (lightLevel >= 7) {
					addExp = 2;
				} else if (lightLevel >= 4) {
					addExp = 3;
				} else {
					addExp = 4;
				}

				// 経験値の追加
				wand.levelUpCheck(this.world, (EntityPlayer) this.getThrower(), this.stack, addExp);
			}

			// ブロック設置処理
			this.world.setBlockState(pos, BlockInit.magiclight.getDefaultState(), 2);

		} catch (Throwable e) {
			this.setEntityDead();
			return;
		}

		this.world.setEntityState(this, (byte) 3);
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		float x = (float) (-this.motionX / 80);
		float y = (float) (-this.motionY / 80);
		float z = (float) (-this.motionZ / 80);

		for (int i = 0; i < 6; i++) {

			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i / 4.0F);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i / 4.0D);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i / 4.0D);
			Particle effect = new ParticleEntityMagicLight.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
			ParticleHelper.spawnParticl().addEffect(effect);;
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = IWand.getWand(this.stack).getLevel(this.stack);

		if (living instanceof EntityPlayer) {
			living.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 200 * level, 0));
		} else {
			living.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200 * level, 0));
		}

		// 経験値追加処理
		this.addExp();
	}
}
