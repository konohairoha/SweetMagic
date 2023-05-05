package sweetmagic.init.entity.monster;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.item.sm.sweetmagic.SMBook;

public class EntityShadowHorse extends EntityHorse {

	private static final DataParameter<Boolean> ISPROTECT = EntityDataManager.<Boolean>createKey(EntityShadowHorse.class, DataSerializers.BOOLEAN);

	public EntityShadowHorse(World world) {
		super(world);
	}

	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(ISPROTECT, false);
	}

	public void setProtect(boolean isProtect) {
		this.dataManager.set(ISPROTECT, isProtect);
	}

	public boolean getProtect() {
		return this.dataManager.get(ISPROTECT);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		this.setProtect(tag.getBoolean("isProtect"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setBoolean("isProtect", this.getProtect());
	}

	public boolean attackEntityFrom(DamageSource src, float amount) {

		if (this.getProtect()) {
			return false;
		}

    	if (src.getImmediateSource() instanceof ISMMob) {
    		amount *= 0.5F;
		}

    	else if (src.getImmediateSource() instanceof EntityBaseMagicShot || src == DamageSource.MAGIC) {
    		amount *= 0.25F;
    	}

		return super.attackEntityFrom(src, amount);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {

		if (player.isSneaking()) {
			if (player.getHeldItem(hand).getItem() instanceof SMBook) {
				this.isDead = true;
			}
		}

		return super.processInteract(player, hand);
	}

    public void updateHorseSlots() {
        super.updateHorseSlots();
        this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
    }

    public void setInv (int i, ItemStack stack) {
        this.horseChest.setInventorySlotContents(i, stack);
    }

	@Override
	public void setInWeb() {}

    public void fall(float dis, float dama) {}
}
