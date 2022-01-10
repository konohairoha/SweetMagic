package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.PotionInit;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SoundHelper;

public class EntityBlastBomb extends EntityBaseMagicShot {

    private static final DataParameter<Integer> DATA = EntityDataManager.<Integer>createKey(EntityBlastBomb.class, DataSerializers.VARINT);

	public EntityBlastBomb(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityBlastBomb(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityBlastBomb(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;
		this.dataManager.set(DATA, data);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(DATA, 0);
	}

	public int getData () {
		return this.dataManager.get(DATA);
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.world.isRemote) { return; }

		if (this.getData() == 0) {
			this.setEntityDead();
			return;
		}

		try {

			if (result.typeOfHit != Type.BLOCK) { return; }

			// ブロック設置処理
			this.createExplo(5.25F);

			// 座標取得の定義
			BlockPos pos = result.getBlockPos().offset(result.sideHit);
			IBlockState state = this.world.getBlockState(pos);
			if (!state.getBlock().isReplaceable(this.world, pos)) { return; }

			this.world.spawnEntity(new EntityCuriousCrystal(this.world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D));
			this.setDead();

		}

		catch (Throwable e) {
			this.createExplo(5.25F);
			this.setDead();
			return;
		}

		this.world.setEntityState(this, (byte) 3);
	}

	//えんちちーの死亡の処理
	protected void setEntityDead() {
		this.createExplo(4F);
		this.setDead();
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		boolean isBlast = this.getData() == 0;

		float x = (float) (-this.motionX );
		float y = (float) (-this.motionY );
		float z = (float) (-this.motionZ );
		x *= isBlast ? 0.5F : 1F;
		y *= isBlast ? 0.5F : 1F;
		z *= isBlast ? 0.5F : 1F;

		EnumParticleTypes par = isBlast ? EnumParticleTypes.EXPLOSION_NORMAL : EnumParticleTypes.SWEEP_ATTACK;

		for (int i = 0; i < 6; ++i) {
			float f1 = (float) (this.posX - 0.5F + this.rand.nextFloat() + this.motionX * i * 0.8);
			float f2 = (float) (this.posY - 0.25F + this.rand.nextFloat() * 0.5 + this.motionY * i);
			float f3 = (float) (this.posZ - 0.5F + this.rand.nextFloat() + this.motionZ * i * 0.8);
			this.world.spawnParticle(par, f1, f2, f3, x, y, z);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {
		this.createExplo(this.data == 0 ?  7.5F : 9F);
	}

	public void createExplo (float explo) {

		List<EntityLivingBase> list = this.getEntityList(EntityLivingBase.class, explo, explo, explo);
		if (list.isEmpty()) { return; }

		int cutTime = this.data == 0 ? 60 : 200;

		for (EntityLivingBase entity : list ) {

			if (entity instanceof IMob) { continue; }

			float dame = explo;
			double distance = 2 - entity.getDistance(this.posX, this.posY, this.posZ) / dame;
			dame *= distance * 1.825F;
			this.attackDamage(entity, dame);
			entity.hurtResistantTime = 0;

			if (entity.isPotionActive(PotionInit.aether_barrier)) {

				PotionEffect effect = entity.getActivePotionEffect(PotionInit.aether_barrier);
				int level = effect.getAmplifier();
				int time = effect.getDuration();

				entity.removePotionEffect(PotionInit.aether_barrier);

				// 10秒削る
				time -= cutTime;

				// 時間が切れたら
				if (time <= 0) {

					// クライアント（プレイヤー）へ送りつける
					if (entity instanceof EntityPlayerMP) {
						PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_BREAK, 1F, 1F), (EntityPlayerMP) entity);
					}
				}

				// 時間が残ってるなら
				else {
					PlayerHelper.addPotion(entity, PotionInit.aether_barrier, time, level, false);
				}
			}
		}

		this.playSound(this, SoundEvents.ENTITY_GENERIC_EXPLODE, 3F, 1F / (this.rand.nextFloat() * 0.2F + 0.9F));
		ParticleHelper.spawnParticle(this.world, new BlockPos(this.posX, this.posY + 0.5, this.posZ), EnumParticleTypes.EXPLOSION_HUGE);
	}
}
