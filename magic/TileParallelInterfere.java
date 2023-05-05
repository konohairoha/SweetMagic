package sweetmagic.init.tile.magic;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileParallelInterfere extends TileSMBase {

	public int page = 0;
    public float pageFlip;
    public float pageFlipPrev, flipT, flipA, tRot;
    public float bookSpread, bookSpreadPrev, bookRot, bookRotPre;
    public boolean isOpne;
	public boolean findPlayer = false;
	public int renderTick = 0;
    private static final Random rand = new Random();

	public final ItemStackHandler chestInv = new InventoryWoodChest(this, this.getInvSize());		// チェストスロット

	@Override
	public void update() {

		super.update();

		this.bookSpreadPrev = this.bookSpread;
		this.bookRotPre = this.bookRot;
		EntityPlayer player = this.world.getClosestPlayer(this.pos.getX() + 0.5F, this.pos.getY() + 0.5F, this.pos.getZ() + 0.5F, 4D, false);

		this.isOpne = player != null;

		if (this.isOpne) {
			double d0 = player.posX - (double) ((float) this.pos.getX() + 0.5F);
			double d1 = player.posZ - (double) ((float) this.pos.getZ() + 0.5F);
			this.tRot = (float) MathHelper.atan2(d1, d0);
			this.bookSpread += 0.1F;

			if (this.bookSpread < 0.5F || rand.nextInt(40) == 0) {
				float f1 = this.flipT;

				while (true) {
					this.flipT += (float) (rand.nextInt(4) - rand.nextInt(4));
					if (f1 != this.flipT) { break; }
				}
			}

			if (!this.isSever() && this.getTime() % 12 == 0) {
				this.spawnParticl();
			}

			this.playSound();
		}

		else {
			this.tRot += 0.02F;
			this.bookSpread -= 0.1F;
		}

		while (this.bookRot >= (float) Math.PI) {
			this.bookRot -= ((float) Math.PI * 2F);
		}

		while (this.bookRot < -(float) Math.PI) {
			this.bookRot += ((float) Math.PI * 2F);
		}

		while (this.tRot >= (float) Math.PI) {
			this.tRot -= ((float) Math.PI * 2F);
		}

		while (this.tRot < -(float) Math.PI) {
			this.tRot += ((float) Math.PI * 2F);
		}

		float f2;

		for (f2 = this.tRot - this.bookRot; f2 >= (float) Math.PI; f2 -= ((float) Math.PI * 2F)) { ; }

		while (f2 < -(float) Math.PI) {
			f2 += ((float) Math.PI * 2F);
		}

		this.bookRot += f2 * 0.4F;
		this.bookSpread = MathHelper.clamp(this.bookSpread, 0.0F, 1.0F);
		++this.renderTick;
		this.pageFlipPrev = this.pageFlip;
		float f = (this.flipT - this.pageFlip) * 0.4F;
		f = MathHelper.clamp(f, -0.2F, 0.2F);
		this.flipA += (f - this.flipA) * 0.9F;
		this.pageFlip += this.flipA;

		if (this.renderTick > 6000) {
			this.renderTick = 0;
		}
	}

	public void spawnParticl () {

		float f1 = this.pos.getX() + 0.5F;
		float f2 = this.pos.getY() + 1.25F + this.rand.nextFloat() * 0.5F;
		float f3 = this.pos.getZ() + 0.5F;
		float x = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;
		float z = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.15F;

		Particle effect = ParticleNomal.create(this.world, f1, f2, f3, x, 0, z);
		this.getParticle().addEffect(effect);
	}

	@Override
	public void clientUpdate () {

//		this.tickTime++;
		if (this.renderTick % 30 != 0) { return; }

		this.checkRangePlayer();
	}

	public void checkRangePlayer() {
		this.findPlayer = this.findRangePlayer();
	}

	public void playSound () { }

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.IN_OUT);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
		}

		return super.getCapability(cap, side);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chest", this.chestInv.serializeNBT());
		tags.setBoolean("findPlayer", this.findPlayer);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chest"));
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	public int getInvSize () {
		return 234;
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

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
