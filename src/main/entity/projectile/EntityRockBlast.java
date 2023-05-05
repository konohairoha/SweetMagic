package sweetmagic.init.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.entity.monster.EntityPixieVex;

public class EntityRockBlast extends EntityBaseMagicShot {

	private static final int STONE = Block.getStateId(Blocks.STONE.getDefaultState());
	private static final DataParameter<Integer> DATA = EntityDataManager.<Integer>createKey(EntityPixieVex.class, DataSerializers.VARINT);

	public EntityRockBlast(World world) {
		super(world);
		this.setSize(0.75F, 0.75F);
	}

	public EntityRockBlast(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityRockBlast(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;
		this.setData(data);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(DATA, Integer.valueOf((int) 0));
	}

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {
		this.setEntityDead();
		this.world.playEvent(2001, this.getPosition(), STONE);
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {

		boolean isPlayer = this.getData() != 2;

		for(int k = 0; k <= 4; k++) {

			float f1 = (float) this.posX - 0.5F + this.getRandFloat(1F);
			float f2 = (float) (this.posY + this.getRandFloat(1.5F));
			float f3 = (float) this.posZ - 0.5F + this.getRandFloat(1F);
			Particle particle = null;

			if (isPlayer) {
				particle = ParticleNomal.create(this.world, f1, f2, f3, 0, 0, 0);
			}

			else {
				particle = ParticleNomal.create(this.world, f1, f2, f3, 0, 0, 0, 48);
			}

			this.getParticle().addEffect(particle);
		}
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();
		int data = this.getData();

		// 敵モブなら
		if (!this.isPlayerThrower) {

			float rate = (data == 0) ? 1.5F : 2.25F;
			double range = (data == 0) ? 2.5D : 3.75D;
			int time = 40 * (level + 1);

			// えんちちーリストの取得
			List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, range, range, range);

			// 範囲のモブに攻撃力ダウン + 防御力分のダメージ
			for (EntityLivingBase target : entityList) {
				float dame = (float) (target.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue() * rate);
				this.attackDamage(target, dame);
				target.hurtResistantTime = 0;

				if (data == 0) {
					target.addPotionEffect(new PotionEffect(MobEffects.LEVITATION, time, 1));
				}
			}
		}

		// 味方なら
		else {

			int value = 1 + data * 3;
			float damage = (float) (this.getDamage() * 0.35F + living.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue() * 1.5F);

			for (int i = 0; i < value ; i++) {
				this.attackDamage(living, damage);
				living.hurtResistantTime = 0;
			}
		}

		living.hurtResistantTime = 0;
		this.world.playEvent(2001, this.getPosition(), STONE);
	}

	public int getData () {
		return this.dataManager.get(DATA);
	}

	public void setData (int data) {
		this.dataManager.set(DATA, data);
	}
}
