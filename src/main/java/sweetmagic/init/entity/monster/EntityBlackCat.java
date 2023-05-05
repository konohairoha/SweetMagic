package sweetmagic.init.entity.monster;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.item.sm.sweetmagic.SMBook;
import sweetmagic.util.EventUtil;

public class EntityBlackCat extends EntityOcelot implements IItemHandlerModifiable {

	public int tickTime = 0;
	public final IItemHandlerModifiable inv = new ItemStackHandler(104);
	private static final DataParameter<Boolean> ISPROTECT = EntityDataManager.<Boolean>createKey(EntityBlackCat.class, DataSerializers.BOOLEAN);

	public EntityBlackCat(World world) {
		super(world);
		this.isImmuneToFire = true;
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

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
	}

	@Override
	public void onUpdate() {

		super.onUpdate();

		this.tickTime++;

		if (!this.isSitting() && this.tickTime % 10 == 0) {
			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(1.25D);
			List<EntityItem> entityList = this.world.getEntitiesWithinAABB(EntityItem.class, aabb);

			if (!entityList.isEmpty()) {

				for (EntityItem entity : entityList) {

					ItemStack stack = entity.getItem();

					for (int k = 0; k < 104; k++) {

						// スロットのアイテムを取得
						if (!this.insertItem(k, stack, true).isEmpty()) { continue; }

						// アイテムを入れる
						ItemStack insertStack = this.insertItem(k, stack.copy(), false);
						this.writeEntityToNBT(this.getEntityData());

						if (insertStack.isEmpty()) {
							entity.setDead();
							break;
						}

						else {
							stack.shrink(insertStack.getCount());
						}
					}
				}
			}
		}

		if (this.tickTime % 200 == 0) {

			// 周囲のえんちちー取得
			AxisAlignedBB aabb = this.getEntityBoundingBox().grow(16D);
			List<EntityCreeper> entityList = this.world.getEntitiesWithinAABB(EntityCreeper.class, aabb);

			if (!entityList.isEmpty()) {

				for (EntityCreeper entity : entityList) {
					EventUtil.tameAIDonmov(entity, 10);
					entity.attackEntityFrom(DamageSource.MAGIC, 2F);
				}
			}
		}
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
	public void setInWeb() {}

	@Override
    public void fall(float dis, float dama) {}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {

		if (!player.isSneaking()) {
			if (!this.world.isRemote) {
				BlockPos pos = this.getPosition();
				player.openGui(SweetMagicCore.INSTANCE, this.getEntityId(), this.world, pos.getX(), pos.getY(), pos.getZ());
			}
		}

		else {

			if (player.getHeldItem(hand).getItem() instanceof SMBook) {
				this.dropInvItem();
				this.isDead = true;
			}

			return super.processInteract(player, hand);
		}

		return true;
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound tag) {
		super.readEntityFromNBT(tag);
		CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(this.inv, null, tag.getTagList("InventoryCatChest", Constants.NBT.TAG_COMPOUND));
		this.setProtect(tag.getBoolean("isProtect"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tag) {
		super.writeEntityToNBT(tag);
		tag.setTag("InventoryCatChest", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(this.inv, null));
		tag.setBoolean("isProtect", this.getProtect());
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.inv.getStackInSlot(index);
	}

	@Override
	public int getSlots() {
		return this.inv.getSlots();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack ret = this.inv.insertItem(slot, stack, simulate);
		this.writeBack();

		if (!simulate) {
			this.playSound(SoundEvents.ENTITY_ITEM_PICKUP, 0.33F, 1F + (this.world.rand.nextFloat() * 0.6F - 0.3F));
		}
		return ret;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack ret = this.inv.extractItem(slot, amount, simulate);
		this.writeBack();
		return ret;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		this.inv.setStackInSlot(slot, stack);
		this.writeBack();
	}

	public void writeBack() {
		this.writeEntityToNBT(this.getEntityData());
	}

	@Override
	public void onDeath(DamageSource src) {

//		EntityLivingBase entity = this.getOwner();

//		if (entity != null && entity instanceof EntityPlayer) {
//			this.setHealth(1F);
//			this.isDead = false;
//			EntityPlayer player = (EntityPlayer) entity;
//			BlockPos pos = player.getBedLocation(this.world.provider.getDimension());
//	        this.setPositionAndUpdate(pos.getX() + 0.5D, pos.getY() + 1.5D, pos.getZ() + 0.5D);
//		}

//		else {
	        this.dead = true;
	        this.getCombatTracker().reset();
	        this.dropInvItem();
//		}
	}

	public void dropInvItem () {

		if (!this.world.isRemote) {

	        for (int i = 0; i < 104; i++) {

	        	ItemStack stack = this.inv.getStackInSlot(i);
	        	if (stack.isEmpty()) { continue; }

	        	this.world.spawnEntity(new EntityItem(this.world, this.posX, this.posY + 0.5D, this.posZ, stack));
	        }
		}
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	protected boolean canDropLoot() {
		return false;
	}

	@Override
	protected Item getDropItem() {
		return null;
	}

	@Override
	protected ResourceLocation getLootTable() {
		return null;
	}
}
