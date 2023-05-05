package sweetmagic.init.tile.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.client.particle.ParticleOrb;
import sweetmagic.init.block.blocks.BlockMagiaStorage;
import sweetmagic.init.tile.inventory.InventoryMagiaStorage;
import sweetmagic.init.tile.slot.WrappedItemHandler;

public class TileMagiaStorage extends TileWoodChest {

	public boolean findPlayer = false;
	public int renderTick = 0;

	// インベントリ
	public final InventoryMagiaStorage chestInv = new InventoryMagiaStorage(this, this.getInvSize());

	@Override
	public void update() {
		this.tickTime++;
		this.renderTick++;
		super.update();
	}

	@Override
	public void clientUpdate() {

		super.clientUpdate();
		if (this.tickTime % 16 != 0) { return; }

		if (this.tickTime % 32 != 0) {
			this.checkRangePlayer();
		}

		if (!this.findPlayer) { return; }

		List<Rgb> rgbList = new ArrayList<>();

		switch (this.getTier()) {
		case 5:
			rgbList.add(new Rgb(255, 65, 91));
		case 4:
			rgbList.add(new Rgb(116, 185, 255));
		case 3:
			rgbList.add(new Rgb(254, 204, 40));
		case 2:
			rgbList.add(new Rgb(0, 255, 255));
		case 1:
			rgbList.add(new Rgb(0, 128, 255));
		}

		for (Rgb rgb : rgbList) {

			Random rand = new Random();

			float randX = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			float randY = (rand.nextFloat() - rand.nextFloat()) * 0.1F;
			float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.1F;

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 0.5F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;

			Particle effect = ParticleOrb.create(this.world, x, y, z, randX, randY, randZ, rgb.red, rgb.green, rgb.blue);
			this.getParticle().addEffect(effect);
		}
	}

	public void checkRangePlayer () {
		if (!this.isSever()) {
			this.findPlayer = this.findRangePlayer(64D, 24D, 64D);
		}
	}

	public int getTier() {
		return ((BlockMagiaStorage) this.getBlock(this.pos)).getTier() + 1;
	}

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

	// インベントリの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem(int i) {
		return this.getChest().getStackInSlot(i);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {

		// nbtのリストを作成
		NBTTagList nbtList = new NBTTagList();
		for (int i = 0; i < this.getInvSize(); i++) {

			// アイテムスタックごとに保存
            NBTTagCompound nbt = new NBTTagCompound();
			ItemStack stack = this.chestInv.getStackInSlot(i);
            stack.writeToNBT(nbt);
            nbt.setInteger("ICount", stack.getCount());
            nbt.setInteger("Slot", i);
            nbtList.appendTag(nbt);
		}

		// アイテムスタック
		if (!nbtList.hasNoTags()) {
			tags.setTag("chestInv", nbtList);
		}

		tags.setInteger("tickTime", this.tickTime);
		tags.setBoolean("findPlayer", this.findPlayer);

		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);

		NBTTagList nbtList = tags.getTagList("chestInv", Constants.NBT.TAG_COMPOUND);

		for (int i = 0; i < nbtList.tagCount(); ++i) {
			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
			stack.setCount(nbt.getInteger("ICount"));
			int slot = nbt.getInteger("Slot");
			this.chestInv.setStackInSlot(slot, stack);
		}

		this.tickTime = tags.getInteger("tickTime");
		this.findPlayer = tags.getBoolean("findPlayer");
	}

	public class Rgb {

		private final int red;
		private final int green;
		private final int blue;

		public Rgb (int red, int green, int blue) {
			this.red = red;
			this.green = green;
			this.blue = blue;
		}

		public int getRed () {
			return this.red;
		}

		public int getGreen () {
			return this.green;
		}

		public int getBlue () {
			return this.blue;
		}
	}
}
