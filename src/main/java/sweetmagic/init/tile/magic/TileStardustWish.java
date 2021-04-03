package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;
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
import sweetmagic.init.tile.magic.TilePedalCreate.RGB;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.init.tile.slot.StackHandler;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileStardustWish extends TileParallelInterfere {

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

	public void spawnParticl () {

//		float posX = this.pos.getX() + 0.5F;
//		float posY = this.pos.getY() + 0.5F;
//		float posZ = this.pos.getZ() + 0.5F;
		Random rand = this.world.rand;

//		for(int k = 0; k <= 1; k++) {
//
//			float f4 = (float) posX - 0.5F + rand.nextFloat();
//			float f5 = posY + 0.75F + rand.nextFloat() * 0.75F;
//			float f6 = (float) posZ - 0.5F + rand.nextFloat();
//			List<Integer> color = this.getRGB(rand);
//			Particle particle = new ParticleLay.Factory().createParticle(0, this.world, f4, f5, f6, 0, 0, 0, color.get(0), color.get(1), color.get(2));
//			FMLClientHandler.instance().getClient().effectRenderer.addEffect(particle);
//		}

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

	public List<Integer> getRGB (Random rand) {

		RGB color = null;

		switch (rand.nextInt(7)) {
		case 0:
			color = RGB.RED;
			break;
		case 1:
			color = RGB.B;
			break;
		case 2:
			color = RGB.C;
			break;
		case 3:
			color = RGB.D;
			break;
		case 4:
			color = RGB.E;
			break;
		case 5:
			color = RGB.F;
			break;
		case 6:
			color = RGB.G;
			break;

		}

		return Arrays.asList(color.r, color.g, color.b);

	}

	public final IItemHandlerModifiable autoInput = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.IN) {

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			return SlotPredicates.SMELTABLE.test(stack) ? super.insertItem(slot, stack, simulate) : stack;
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
