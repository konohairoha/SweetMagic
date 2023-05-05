package sweetmagic.handlers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.sweetmagic.SMBucket;
import sweetmagic.packet.SMBucketPKT;
import sweetmagic.util.ItemHelper;

public class FluidBucketHandler implements IFluidHandlerItem, ICapabilityProvider {

	private final ItemStack stack;
	private final SMBucket bucket;
	private FluidStack fluid;

	public FluidBucketHandler(ItemStack stack, Fluid fluid) {
		this.stack = stack;
		this.bucket = (SMBucket) stack.getItem();
		this.fluid = new FluidStack(fluid, Fluid.BUCKET_VOLUME);
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return new FluidTankProperties[] { new FluidTankProperties(this.fluid, Fluid.BUCKET_VOLUME) };
	}

	protected void setFluid(@Nullable FluidStack fluidStack, int amount) {

		try {
			PacketHandler.sendToServer(new SMBucketPKT(amount));
		}

		catch (Throwable e) { }
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {

		if (resource == null || resource.amount < Fluid.BUCKET_VOLUME || this.bucket != ItemInit.alt_bucket_water) { return 0; }

		NBTTagCompound tags = ItemHelper.getNBT(this.stack);
		int fillAmount = this.bucket.isAmountEmpty(tags) ? Fluid.BUCKET_VOLUME : -Fluid.BUCKET_VOLUME;

		if (doFill) {
			this.setFluid(resource, this.bucket.getAmount(tags) + (this.bucket.isAmountEmpty(tags) ? 1 : -1));
		}

		return fillAmount;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {

		if (resource == null || resource.amount < Fluid.BUCKET_VOLUME || this.bucket != ItemInit.alt_bucket_water) { return null; }

		FluidStack fluidStack = new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);

		if (fluidStack != null && fluidStack.isFluidEqual(resource)) {

			if (doDrain) {
				this.setFluid((FluidStack) null, this.bucket.getAmount(ItemHelper.getNBT(this.stack)) - 1);
			}
			return fluidStack;
		}

		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {

		if (this.fluid == null || maxDrain < Fluid.BUCKET_VOLUME || this.bucket != ItemInit.alt_bucket_water) { return null; }

		if (doDrain) {
			this.setFluid((FluidStack) null, this.bucket.getAmount(ItemHelper.getNBT(this.stack)) - 1);
		}

		return new FluidStack(FluidRegistry.WATER, Fluid.BUCKET_VOLUME);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, @Nullable EnumFacing face) {
		return cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> cap, @Nullable EnumFacing face) {
		return cap == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY ? (T) this : null;
	}

	@Override
	public ItemStack getContainer() {
		return this.stack;
	}
}
