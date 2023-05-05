package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.base.BaseMFFace;
import sweetmagic.init.block.blocks.FruitLeaves;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.util.ItemHelper;

public class TileMFHarvester extends TileMFBase {

	public int needMF = 100;
	public int chargeMF = 10000;
	public int maxMagiaFlux = 500000;
	public boolean isActive = false;
	public boolean findPlayer = false;

	public boolean isRangeRender = false;
	public int range = 4 ;
	public int pX = -this.range;
	public int pZ = -this.range;

	// 必要MF
	public int getNeedMF() {
		return this.needMF;
	}

	// 送信するMF量
	@Override
	public int getUseMF () {
		return this.chargeMF;
	}

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

	public final ItemStackHandler outputInv = new InventoryWoodChest(this, this.getInvSize());
	private final IItemHandlerModifiable output = new WrappedItemHandler(this.outputInv, WrappedItemHandler.WriteMode.OUT);

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

	// 初期化
	public void resetInfo () {
		this.pX = -this.range;
		this.pZ = -this.range;
	}

	@Override
	public void update() {

		super.update();

		this.tickTime++;
		if (this.tickTime % 3 != 0 || this.isMfEmpty()) { return; }

		// クライアント側処理
		if (this.tickTime % 30 == 0 && !this.isSever()) {
			this.findPlayer = this.findRangePlayer();
		}

		this.isActive = this.isActive(this.pos);
		if (!this.isActive) { return; }

		if (this.getMF() >= this.getNeedMF() && !this.checkInv()) {
			this.actionPlant();
		}
	}

	// 作物回収
	public void actionPlant () {

		// 対象範囲の取得
		BlockPos targetPos = new BlockPos(this.pos.getX() + this.pX, this.pos.getY(), this.pos.getZ() + this.pZ);
		int putRange = Math.abs(this.range) + 1;

		switch (this.getState(this.pos).getValue(BaseMFFace.FACING)) {
		case NORTH:
			targetPos = targetPos.south(putRange);
			break;
		case SOUTH:
			targetPos = targetPos.north(putRange);
			break;
		case EAST:
			targetPos = targetPos.west(putRange);
			break;
		case WEST:
			targetPos = targetPos.east(putRange);
			break;
		}

		IBlockState plantBaseState = this.world.getBlockState(targetPos);
		Block plantBase = plantBaseState.getBlock();

		IBlockState plantUpState = this.world.getBlockState(targetPos.up());
		Block plantUp = plantUpState.getBlock();

		// ブロックより一つ下が作物ならそれを回収
		if (this.checkPlant(plantUp, targetPos.up())) {
			this.inputPlant(plantUpState, plantUp, targetPos.up());
		}

		// 作物かどうか
		if (this.checkPlant(plantBase, targetPos)) {
			this.inputPlant(plantBaseState, plantBase, targetPos);
		}

		// X軸を加算
		++this.pX;

		// X軸が範囲を超えたらリセットしてZ軸を加算
		if (this.pX > this.range) {
			this.pX = -this.range;
			++this.pZ;
		}

		// X軸が範囲を超えたらX軸とZ軸をリセット
		if (this.pZ > this.range) {
			this.pX = -this.range;
			this.pZ = -this.range;
		}
	}

	// 作物かどうか
	public boolean checkPlant (Block block, BlockPos pos) {

		IBlockState state = this.world.getBlockState(pos);
		boolean flag = true;

		if (block instanceof FruitLeaves) {
			FruitLeaves leave = (FruitLeaves) block;
			return leave.getNowStateMeta(state) >= 2;
		}

		else if (block instanceof IGrowable) {
			flag = ((IGrowable) block).canGrow(this.world, pos, state, false);
		}

		if (block instanceof IShearable) {
			flag = !((IShearable) block).isShearable(new ItemStack(Items.SHEARS), this.world, pos);
		}

		if (block == Blocks.PUMPKIN || block == Blocks.MELON_BLOCK) {
			flag = false;
		}

		if (block == Blocks.REEDS) {
			Block under = this.world.getBlockState(pos.down()).getBlock();
			if (under == Blocks.REEDS) {
				flag = false;
			}
		}

		return !flag;
	}

	// アイテムを入れる
	public void inputPlant (IBlockState state, Block block, BlockPos pos) {

		List<ItemStack> stackList = new ArrayList<>();

		if (block instanceof ISMCrop) {

			Random rand = this.world.rand;

			ISMCrop smCrop = (ISMCrop) block;
			stackList.add(smCrop.rightClickStack(this.world, state, pos));
			smCrop.playCropSound(this.world, rand, pos, 0.2F);

			float pX = this.pos.getX() - pos.getX();
			float pY = this.pos.getY() - pos.getY();
			float pZ = this.pos.getZ() - pos.getZ();

			float randX = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
			float randY = (rand.nextFloat() - rand.nextFloat()) * 0.5F;
			float randZ = (rand.nextFloat() - rand.nextFloat()) * 0.5F;

			float x = pos.getX() + 0.5F + randX;
			float y = pos.getY() + 0.525F + randY;
			float z = pos.getZ() + 0.5F + randZ;
			float xSpeed = pX * 0.1175F;
			float ySpeed = pY * 0.1175F;
			float zSpeed = pZ * 0.1175F;

			if (!this.isSever()) {
				Particle effect = ParticleNomal.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed);
				this.getParticle().addEffect(effect);
			}
		}

		else {

			if (this.isSever()) {
				stackList = block.getDrops(this.world, pos, this.world.getBlockState(pos), 0);
				this.breakBlock(this.world, pos);
				this.world.setBlockState(pos, block.getDefaultState(), 2);
			}
		}

		if (this.isSever()) {
			for (ItemStack stack : stackList) {
				ItemHandlerHelper.insertItemStacked(this.getOutput(), stack, false);
			}

			this.setMF(this.getMF() - this.getNeedMF());
			this.sentClient();
		}
	}

	// 最終スロットが空じゃなかったらfalseを返す
	public boolean checkInv () {
		ItemHelper.compactInventory(this.outputInv);
		return !this.getOutputItem(this.getInvSize() - 1).isEmpty();
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
		world.playEvent(2001, pos, Block.getStateId(state));
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }


	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("Output", this.outputInv.serializeNBT());
		tags.setBoolean("isActive", this.isActive);
		tags.setBoolean("findPlayer", this.findPlayer);
		tags.setInteger("range", this.range);
		tags.setBoolean("isRangeRender", this.isRangeRender);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.outputInv.deserializeNBT(tags.getCompoundTag("Output"));
		this.isActive = tags.getBoolean("isActive");
		this.findPlayer = tags.getBoolean("findPlayer");
		this.range = tags.getInteger("range");
		this.isRangeRender = tags.getBoolean("isRangeRender");
	}

	// インベントリの数
	public int getInvSize() {
		return 27;
	}

	public IItemHandler getOutput() {
		return this.outputInv;
	}

	public ItemStack getOutputItem(int i) {
		return this.getOutput().getStackInSlot(i);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
