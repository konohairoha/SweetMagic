package sweetmagic.init.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import sweetmagic.client.particle.ParticleTwilightlight;
import sweetmagic.util.ParticleHelper;

public class EntityMagicItem extends EntityItem {

	private static final DataParameter<Boolean> ISEFFECT = EntityDataManager.<Boolean>createKey(EntityMagicItem.class, DataSerializers.BOOLEAN);

    public EntityMagicItem(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isImmuneToFire = true;
    }

    public EntityMagicItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
        this.isImmuneToFire = true;
        this.setNoDespawn();
    }

    public EntityMagicItem(World world, EntityLivingBase entity, ItemStack stack) {
        super(world, entity.posX, entity.posY, entity.posZ, stack);
        this.isImmuneToFire = true;
        this.setNoDespawn();
    }

    public EntityMagicItem(World world) {
        super(world);
        this.isImmuneToFire = true;
        this.setNoDespawn();
    }

	public boolean attackEntityFrom(DamageSource src, float amount) {
		return false;
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISEFFECT, false);
	}

	public void setEffect (boolean isSpecial) {
		this.dataManager.set(ISEFFECT, isSpecial);
	}

	public boolean getEffect () {
		return this.dataManager.get(ISEFFECT);
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
		if (!this.world.isRemote || this.onGround || !this.getEffect()) { return; }

		this.spawnParticle();
	}

	public void spawnParticle() {
		float f1 = (float) this.posX - 0.25F + this.rand.nextFloat() * 0.5F;
		float f2 = (float) this.posY + 0.5F;
		float f3 = (float) this.posZ - 0.25F + this.rand.nextFloat() * 0.5F;
		ParticleHelper.spawnParticl().addEffect(ParticleTwilightlight.create(this.world, f1, f2, f3, 0, 0, 0));
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tags) {
		super.readEntityFromNBT(tags);
		this.setEffect(tags.getBoolean("isEffect"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tags) {
		super.writeEntityToNBT(tags);
		tags.setBoolean("isEffect", this.getEffect());
	}
}
