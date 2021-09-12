package sweetmagic.init.tile.magic;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileStardustWish extends TileParallelInterfere {

	public int randTime = 0;

	// チェストスロット
	public final ItemStackHandler chestInv = new StackHandler(this, this.getInvSize());

	public int getInvSize () {
		return 936;
	}

	// チェストスロットの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// チェストスロットのアイテムを取得
	public  ItemStack getChestItem(int slot) {
		return this.getChest().getStackInSlot(slot);
	}

	// ページの習得
	public int getPage () {
		return this.page;
	}

	// ページの設定
	public void setPage (int page) {
		this.page = page;
	}

	public void playSound () {
		if (this.randTime <= 0) {
			this.randTime = 20 + this.world.rand.nextInt(40) + 1;
		}

		if (this.getTime() % this.randTime != 0) { return; }

		this.randTime = 20 + this.world.rand.nextInt(40) + 1;
		this.playSound(this.pos, SMSoundEvent.STARDUST, 0.3F, 1F + this.world.rand.nextFloat() * 0.15F);
	}

	public void spawnParticl () {

		Random rand = this.world.rand;

		for(int k = 0; k <= 2; k++) {
			float randX = rand.nextFloat() - rand.nextFloat();
			float randY = rand.nextFloat();
			float randZ = rand.nextFloat() - rand.nextFloat();

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 1.0F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;
			float xSpeed = -randX * 0.075F;
			float ySpeed = -randY * 0.075F;
			float zSpeed = -randZ * 0.075F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, x, y, z, xSpeed, ySpeed, zSpeed);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	public final IItemHandlerModifiable autoInput = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.ALLITEM.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
		}
	};

	public final IItemHandlerModifiable autoOutput = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.OUT);
	public final IItemHandler autoSide = new CombinedInvWrapper(this.chestInv);
	public final CombinedInvWrapper join = new CombinedInvWrapper(this.autoInput, this.autoOutput);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.join);
			}

			switch (side) {
			case UP:
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoInput);
			case DOWN:
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoOutput);
			default:
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.autoSide);
			}
		}

		return super.getCapability(cap, side);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chest", this.chestInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chest"));
	}
}
