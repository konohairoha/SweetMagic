package sweetmagic.init.entity.projectile;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketChangeGameState;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMDamage;

public class EntityBaseMagicShot extends Entity implements IProjectile {

	protected int xTile;
	protected int yTile;
	protected int zTile;
	protected Block inTile;
	protected int inData;
	public boolean inGround;
	public EntityLivingBase thrower;
	private int ticksInGround;
	protected int ticksInAir;
	protected int knockback;
	protected double damage;
	public boolean isHitDead = false;
	public ItemStack stack = ItemStack.EMPTY;
	public boolean isHit = false;
	public double range = 0;
	public int tickTime = 0;
	public int level = 1;
	public int plusTick = 0;
    public UUID throwerId;
    public int data = 0;

	public EntityBaseMagicShot(World world) {
		super(world);
		this.xTile = -1;
		this.yTile = -1;
		this.zTile = -1;
		this.setSize(1.0F, 1.0F);
	}

	public EntityBaseMagicShot(World world, double x, double y, double z) {
		this(world);
		this.setPosition(x, y, z);
	}

	public EntityBaseMagicShot(World world, EntityLivingBase thrower, ItemStack stack) {
		this(world, thrower.posX, thrower.posY + (double) thrower.getEyeHeight() - 0.10000000149011612D, thrower.posZ);
		this.setThrower(thrower);
		this.throwerId = thrower.getUniqueID();
		if (!stack.isEmpty()) {
			this.stack = stack;
			this.level = IWand.getLevel(IWand.getWand(this.stack), this.stack);
		}
	}

	protected void entityInit() {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
		d0 = Double.isNaN(d0) ? 4D : d0;
		d0 = d0 * 128.0D;
		return distance < d0 * d0;
	}

	public void setHeadingFromThrower(Entity entity, float rotationPitch, float rotationYaw, float pitchOffset, float velocity, float inaccuracy) {
		float f = -MathHelper.sin(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017F);
		float f1 = -MathHelper.sin((rotationPitch + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYaw * 0.017453292F) * MathHelper.cos(rotationPitch * 0.017F);
		this.shoot((double) f, (double) f1, (double) f2, velocity, inaccuracy);
		this.motionX += entity.motionX;
		this.motionZ += entity.motionZ;
	}

	//射撃時のメソッド
	public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
		float f = MathHelper.sqrt(x * x + y * y + z * z);
		x = x / (double) f;
		y = y / (double) f;
		z = z / (double) f;
		x = x + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		y = y + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		z = z + this.rand.nextGaussian() * 0.007499999832361937D * (double) inaccuracy;
		x = x * (double) velocity;
		y = y * (double) velocity;
		z = z * (double) velocity;
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		float f1 = MathHelper.sqrt(x * x + z * z);
		this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
		this.rotationPitch = (float) (MathHelper.atan2(y, (double) f1) * (180D / Math.PI));
		this.prevRotationYaw = this.rotationYaw;
		this.prevRotationPitch = this.rotationPitch;
		this.ticksInGround = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void setVelocity(double x, double y, double z) {
		this.motionX = x;
		this.motionY = y;
		this.motionZ = z;
		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(x * x + z * z);
			this.rotationPitch = (float) (MathHelper.atan2(y, (double) f) * (180D / Math.PI));
			this.rotationYaw = (float) (MathHelper.atan2(x, z) * (180D / Math.PI));
			this.prevRotationPitch = this.rotationPitch;
			this.prevRotationYaw = this.rotationYaw;
		}
	}

	public void onUpdate() {

		super.onUpdate();

		if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
			float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
			this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f) * (180D / Math.PI));
			this.prevRotationYaw = this.rotationYaw;
			this.prevRotationPitch = this.rotationPitch;
		}

		BlockPos pos = new BlockPos(this.xTile, this.yTile, this.zTile);
		IBlockState state = this.world.getBlockState(pos);

		if (state.getMaterial() != Material.AIR) {
			AxisAlignedBB aabb = state.getCollisionBoundingBox(this.world, pos);
			if (aabb != Block.NULL_AABB
					&& aabb.offset(pos).contains(new Vec3d(this.posX, this.posY, this.posZ))) {
				this.inGround = true;
			}
		}

		if (this.inGround) {
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult result = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);
			this.inGround(result);
		}

		else {
			++this.ticksInAir;
			Vec3d vec3d1 = new Vec3d(this.posX, this.posY, this.posZ);
			Vec3d vec3d = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
			RayTraceResult result = this.world.rayTraceBlocks(vec3d1, vec3d, false, true, false);

			if (result != null) {
				vec3d = new Vec3d(result.hitVec.x, result.hitVec.y, result.hitVec.z);
			}

			Entity entity = this.findEntityOnPath(vec3d1, vec3d);
			if (entity != null) {
				result = new RayTraceResult(entity);
			}

			if (result != null && result.entityHit != null
					&& result.entityHit instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) result.entityHit;
				if (this.getThrower() instanceof EntityPlayer
						&& !((EntityPlayer) this.getThrower()).canAttackPlayer(entityplayer)) {
					result = null;
				}
			}

			if (result != null && !ForgeEventFactory.onProjectileImpact(this, result)) {
				this.onHit(result);
			}

			this.posX += this.motionX;
			this.posY += this.motionY;
			this.posZ += this.motionZ;
			float f4 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

			for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f4)
					* (180D / Math.PI)); this.rotationPitch
							- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
				;
			}

			while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
				this.prevRotationPitch += 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
				this.prevRotationYaw -= 360.0F;
			}

			while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
				this.prevRotationYaw += 360.0F;
			}

			this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
			this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
			float f1 = 0.99F;

			if (this.isInWater()) {
				f1 = this.inWaterSpeed(result);
			}

			// パーティクルをスポーンするメソッド
			if (this.world.isRemote) {
				this.spawnParticle();
			}

			if (this.isWet()) {
				this.extinguish();
			}

			this.motionX *= (double) f1;
			this.motionY *= (double) f1;
			this.motionZ *= (double) f1;
			this.isGravity();
			this.setPosition(this.posX, this.posY, this.posZ);
			this.doBlockCollisions();
		}

		if (this.ticksInAir >= 30 + this.plusTickAir()) {
			this.setEntityDead();
		}
	}

	// ダメージソース(誰が攻撃したかをわかるために)
	protected DamageSource damageSource(){
		return SMDamage.MagicDamage(this, this.getThrower());
	}

	protected void onHit(RayTraceResult result) {

		Entity entity = result.entityHit;

		if (entity != null) {

			if (entity instanceof EntityShadowGolem) {
				EntityShadowGolem golem = (EntityShadowGolem) entity;
				if (golem.isRading()) { return; }
			}

			DamageSource damage = this.damageSource();

			// エンダーマン以外
			if (!(entity instanceof EntityEnderman) && entity.attackEntityFrom(damage, (float) this.damage)) {

				if (entity instanceof EntityLivingBase) {

					EntityLivingBase target = (EntityLivingBase) entity;

					if (this.knockback > 0) {
						float f1 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
						if (f1 > 0.0F) {
							target.addVelocity(
									this.motionX * (double) this.knockback * 0.6D / (double) f1, 0.1D,
									this.motionZ * (double) this.knockback * 0.6D / (double) f1);
						}
					}

					if (this.getThrower() instanceof EntityLivingBase) {
						EnchantmentHelper.applyThornEnchantments(target, this.getThrower());
						EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase) this.getThrower(), target);
					}

					this.entityHit(target);
					target.hurtResistantTime = 0;

					if (this.getThrower() != null && target != this.getThrower()
							&& target instanceof EntityPlayer && this.getThrower() instanceof EntityPlayerMP) {
						((EntityPlayerMP) this.getThrower()).connection.sendPacket(new SPacketChangeGameState(6, 0.0F));
					}
				}

				if (!this.isHitDead && !this.isHit) {
					this.setEntityDead();
				}
			}

			else {

				if (this.getThrower() instanceof EntityPlayer && entity instanceof EntityLivingBase) {

					DamageSource src = DamageSource.causePlayerDamage((EntityPlayer) this.getThrower());

					if (entity.attackEntityFrom(src, (float) this.damage)) {
						this.setEntityDead();
						this.entityHit((EntityLivingBase) entity);
						entity.hurtResistantTime = 0;
					}
				}

				else if (!this.isHit) {
					this.setEntityDead();
				}
			}
		}

		else {

			BlockPos pos = result.getBlockPos();
			this.xTile = pos.getX();
			this.yTile = pos.getY();
			this.zTile = pos.getZ();
			IBlockState state = this.world.getBlockState(pos);
			this.inTile = state.getBlock();
			this.inData = this.inTile.getMetaFromState(state);
			this.motionX = (double) ((float) (result.hitVec.x - this.posX));
			this.motionY = (double) ((float) (result.hitVec.y - this.posY));
			this.motionZ = (double) ((float) (result.hitVec.z - this.posZ));
			float f2 = MathHelper.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
			this.posX -= this.motionX / (double) f2 * 0.05000000074505806D;
			this.posY -= this.motionY / (double) f2 * 0.05000000074505806D;
			this.posZ -= this.motionZ / (double) f2 * 0.05000000074505806D;
			this.inGround = true;

			if (state.getMaterial() != Material.AIR) {
				this.inTile.onEntityCollidedWithBlock(this.world, pos, state, this);
			}
		}
	}

	// 攻撃
	public boolean attackDamage (EntityLivingBase entity, float dame) {

		// エンダーマンなら
		if (entity instanceof EntityEnderman && this.getThrower() instanceof EntityPlayer) {
			DamageSource src = DamageSource.causePlayerDamage((EntityPlayer) this.getThrower());
			return entity.attackEntityFrom(src, dame);
		}

		DamageSource damage = this.damageSource();
		return entity.attackEntityFrom(damage, dame);
	}

	// 地面についたときの処理
	protected void inGround(RayTraceResult result) {
		this.setEntityDead();
	}

	//えんちちーの死亡の処理
	protected void setEntityDead() {
		this.setDead();
	}

	// 重力加速度及び空中時のonUpdateの追加
	protected void isGravity() {
	}

	// 水中での速度減衰
	protected float inWaterSpeed(RayTraceResult raytraceresult) {
		return 0.9F;
	}

	// 自然消滅までの時間 30tick + this.plusTickAir(増やしたい場合は-10とか付ければおっけー)
	protected int plusTickAir() {
		return 0 - this.plusTick;
	}

	// パーティクルスポーン
	protected void spawnParticle () {}

	public static void registerFixesThrowable(DataFixer fixer, String name){}

	// えんちちーに当たった時の処理
	protected void entityHit(EntityLivingBase living){}

	@Override
	public void move(MoverType type, double x, double y, double z) {
		super.move(type, x, y, z);
		if (this.inGround) {
			this.xTile = MathHelper.floor(this.posX);
			this.yTile = MathHelper.floor(this.posY);
			this.zTile = MathHelper.floor(this.posZ);
		}
	}

	@Override
	protected boolean canTriggerWalking() {
		return false;
	}

	@Override
	public boolean canBeAttackedWithItem() {
		return false;
	}

	@Nullable
	protected Entity findEntityOnPath(Vec3d start, Vec3d end) {

		Entity entity = null;
		List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this,
				this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
		double d0 = 0.0D;
		Entity entity1;
		double d1;

		if (this.world.isRemote) { return entity; }

		for (int k = 0; k < list.size(); ++k) {

			entity1 = (Entity) list.get(k);

			if (entity1.canBeCollidedWith() && (entity1 != this.getThrower() || this.ticksInAir >= 5)) {
				AxisAlignedBB aabb = entity1.getEntityBoundingBox().grow(0.30000001192092896D);
				RayTraceResult result = aabb.calculateIntercept(start, end);

				if (result == null) { continue; }

				d1 = start.distanceTo(result.hitVec);
				if (d1 < d0 || d0 == 0.0D) {
					entity = entity1;
					d0 = d1;
				}
			}
		}
		return entity;
	}

	// 射撃者の設定
	public void setThrower (EntityLivingBase thrower) {
		this.thrower = thrower;
        this.throwerId = thrower == null ? null : thrower.getUniqueID();
	}

	public EntityLivingBase getThrower () {

		if (this.thrower == null && this.throwerId != null && this.world instanceof WorldServer) {
			Entity entity = ((WorldServer) this.world).getEntityFromUuid(this.throwerId);

			if (entity instanceof EntityLivingBase) {
				this.thrower = (EntityLivingBase) entity;
			}
		}

        return this.thrower;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tags) {
		tags.setInteger("xTile", this.xTile);
		tags.setInteger("yTile", this.yTile);
		tags.setInteger("zTile", this.zTile);
		tags.setShort("life", (short) this.ticksInGround);
		ResourceLocation resource = Block.REGISTRY.getNameForObject(this.inTile);
		tags.setString("inTile", resource == null ? "" : resource.toString());
		tags.setByte("inData", (byte) this.inData);
		tags.setByte("inGround", (byte) (this.inGround ? 1 : 0));
		tags.setDouble("damage", this.damage);
		tags.setBoolean("isHitDead", this.isHitDead);
		tags.setBoolean("isHit", this.isHit);
		tags.setTag("Item", this.stack.writeToNBT(new NBTTagCompound()));
		tags.setInteger("level", this.level);
		tags.setInteger("plusTick", this.plusTick);
		tags.setInteger("data", this.data);
//		System.out.println("====ID" + (this.throwerId != null));
		if (this.throwerId != null) {
			tags.setUniqueId("throwerId", this.throwerId);
		}
//		System.out.println("====TAG" + tags.hasKey("throwerId"));
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tags) {
		this.xTile = tags.getInteger("xTile");
		this.yTile = tags.getInteger("yTile");
		this.zTile = tags.getInteger("zTile");
		this.ticksInGround = tags.getShort("life");
		if (tags.hasKey("inTile", 8)) {
			this.inTile = Block.getBlockFromName(tags.getString("inTile"));
		} else {
			this.inTile = Block.getBlockById(tags.getByte("inTile") & 255);
		}
		this.inData = tags.getByte("inData") & 255;
		this.inGround = tags.getByte("inGround") == 1;
		if (tags.hasKey("damage", 99)) {
			this.damage = tags.getDouble("damage");
		}
		this.isHitDead = tags.getBoolean("isHitDead");
		this.isHit = tags.getBoolean("isHit");
		this.stack = this.read(tags.getCompoundTag("Item"));
		this.level = tags.getInteger("level");
		this.plusTick = tags.getInteger("plusTick");
		this.data = tags.getInteger("data");
		this.throwerId = tags.getUniqueId("throwerId");
	}

	public ItemStack read(NBTTagCompound tags) {
		try {
			return new ItemStack(tags);
		} catch (RuntimeException run) {
			return ItemStack.EMPTY;
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entity) {}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public double getDamage() {

		double k = 0;

		if (this.getThrower() != null) {

			if (this.thrower.isPotionActive(MobEffects.STRENGTH)) {
				PotionEffect potion = this.getThrower().getActivePotionEffect(MobEffects.STRENGTH);
				int i = potion.getAmplifier();
				k = k + 2 + (i * 2);
			}

			if (this.thrower.isPotionActive(MobEffects.WEAKNESS)) {
				PotionEffect potion = this.getThrower().getActivePotionEffect(MobEffects.WEAKNESS);
				int i = potion.getAmplifier();
				k = k - (2 + (i * 2));
			}
		}
		return this.damage + k;
	}

	// 経験値アップ処理
	public void addExp () {

		if (!this.world.isRemote && !this.stack.isEmpty() && this.getThrower() != null) {

			IWand wand = (IWand) this.stack.getItem();

			if (!wand.isCreativeWand()) {

				// 経験値の計算
				int addExp = this.getExp(IWand.getSMItem((EntityPlayer) this.getThrower(), this.stack));

				// 経験値の追加
				wand.levelUpCheck(this.world, (EntityPlayer) this.getThrower(), this.stack, addExp);
			}
		}
	}

	// 経験値の取得
	public int getExp (ISMItem smItem) {
		int addExp = (int) smItem.getUseMF() / 10;
		 return addExp = addExp <= 0 ? 0 : addExp;
	}

	// 杖レベルの取得
	public int getWandLevel () {
		return this.stack.isEmpty() ? 3 : IWand.getLevel(IWand.getWand(this.stack), this.stack);
	}

	public void setKnockbackStrength(int knockback) {
		this.knockback = knockback;
	}

	@SideOnly(Side.CLIENT)
	protected void generateRandomParticles(){}

	// 音を流す
	public void playSound (Entity entity, SoundEvent sound, float vol, float pit) {
		this.world.playSound(null, new BlockPos(entity), sound, SoundCategory.NEUTRAL, vol, pit);
	}

	// えんちちーリスト取得
	public List<EntityLivingBase> getEntityList (double x, double  y, double  z) {
		return this.world.getEntitiesWithinAABB(EntityLivingBase.class, this.getAABB(x, y, z));
	}

	// プレイヤーかどうか
	public boolean isPlayer (EntityLivingBase entity) {
		return PlayerHelper.isThowerPlayer(entity, this.getThrower());
	}

	// プレイヤー以外かどうか
	public boolean isNotPlayer (EntityLivingBase entity) {
		return PlayerHelper.isNotThowerPlayer(entity, this.getThrower());
	}

	// 範囲の取得
	public AxisAlignedBB getAABB (double x, double  y, double  z) {
		return this.getEntityBoundingBox().grow(x, y, z);
	}

	// プレイヤーがの取得
	public EntityPlayer getPlayer () {
		return (EntityPlayer) this.getThrower();
	}

	// エンダーシャドーの分身なら
	public void checkShadow (EntityLivingBase entity) {
		if (entity instanceof EntityEnderShadow) {
			EntityEnderShadow ender = (EntityEnderShadow) entity;
			if (ender.isShadow) {
				ender.setDead();
			}
		}
	}

	public int getTick () {
		return this.ticksInAir;
	}
}
