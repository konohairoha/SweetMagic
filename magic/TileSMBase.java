package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;
import net.minecraft.world.World;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.util.ParticleHelper;

public class TileSMBase extends TileEntity implements ITickable, ILockableContainer {

	protected final int dayTime = 24000;
	public int tickTime = 0;	// 稼働してる時間

	@Override
	public void update() {

		// クライアント
		if (!this.isSever()) {
			this.clientUpdate();
		}

		// サーバー
		else {
			this.serverUpdate();
		}
	}

	public void clientUpdate () { }
	public void serverUpdate () { }

	public IBlockState getState (BlockPos pos) {
		return this.world.getBlockState(pos);
	}

	// ブロックの取得
	public Block getBlock (BlockPos pos) {
		return this.getState(pos).getBlock();
	}

	// 向きの取得
	public EnumFacing getFace () {

		IBlockState state = this.getState(this.pos);
		Block block = state.getBlock();
		if ( !(block instanceof BaseFaceBlock)) { return EnumFacing.NORTH; }

		return state.getValue(BaseFaceBlock.FACING);
	}

	// タイルえんちちーの取得
	public TileEntity getTile (BlockPos pos) {
		return this.world.getTileEntity(pos);
	}

	public void playSound (BlockPos pos, SoundEvent sound, float vol, float pit) {
		this.world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	public long getTime () {
		return this.world.getTotalWorldTime();
	}

	/*
	 * ====================================
	 * nbt保存処理開始
	 * ====================================
	 */

	@Override
    public void readFromNBT(NBTTagCompound tags) {
        super.readFromNBT(tags);
        this.readNBT(tags);
        this.tickTime = tags.getInteger("tickTime");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tags) {
        super.writeToNBT(tags);
        this.writeNBT(tags);
        tags.setInteger("tickTime", this.tickTime);
        return tags;
    }

    public NBTTagCompound writeNBT(NBTTagCompound tags) {
		return tags;
	}

	public void readNBT(NBTTagCompound tags) { }

	@Override
	@Nullable
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(this.pos, 0, this.writeToNBT(new NBTTagCompound()));
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		this.readFromNBT(tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void invalidate() {
		super.invalidate();
		this.updateContainingBlockInfo();
	}

	// List<ItemStack>をnbt保存
	public static NBTTagCompound saveStackList (NBTTagCompound tag, List<ItemStack> stackList, String name) {

		// NULLチェックとListの個数を確認
		if (stackList != null && !stackList.isEmpty()) {

			// nbtのリストを作成
			NBTTagList nbtList = new NBTTagList();
			for (ItemStack stack : stackList) {
				if (!stack.isEmpty()) {

					// アイテムスタックごとに保存
	                NBTTagCompound nbt = new NBTTagCompound();
	                stack.writeToNBT(nbt);
//	                nbt.setInteger("ICount", stack.getCount());
	                nbtList.appendTag(nbt);
				}
			}

			// アイテムスタック
			if (!nbtList.hasNoTags()) {
//				System.out.println("SAVE：========  " + nbtList);
				tag.setTag(name, nbtList);
			}
		}

		return tag;
	}

	public boolean isNotAir () {
		return this.getBlock(this.pos) != Blocks.AIR;
	}

	// nbtを呼び出してList<ItemStack>に突っ込む
	public static List<ItemStack> loadAllItems(NBTTagCompound tag, String name) {

		NBTTagList nbtList = tag.getTagList(name, 10);
		List<ItemStack> list = new ArrayList<ItemStack>();

		for (int i = 0; i < nbtList.tagCount(); ++i) {

			NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
			ItemStack stack = new ItemStack(nbt);
//			stack.setCount(nbt.getInteger("ICount"));
			list.add(stack);
		}

//		System.out.println("LOAD：========  " + list);
		return list;
	}

	// 対象の座標が空いてるか
	public boolean isAir (BlockPos pos) {
		IBlockState state = this.getState(pos);
		Block block = state.getBlock();
		return block != Blocks.AIR && block.isFullBlock(state);
	}

	// 赤石制御
	public boolean isActive(BlockPos pos) {
		return this.isRedStonePower(this.world, pos) ? false : true;
	}

	// レッドストーン信号を受けているかを判断する
	public boolean isRedStonePower(World world, BlockPos pos) {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			redstone = Math.max(redstone, world.getRedstonePower(pos.offset(dir), dir));
		}
		return redstone > 0;
	}

	// サーバー側かどうか
	public boolean isSever () {
		return !this.world.isRemote;
	}

	// エンティティの一定距離内か
	public boolean isEntityDistance (Entity entity, double x, double y, double z) {
		return Math.abs(entity.posX - this.pos.getX()) <= x && Math.abs(entity.posY - this.pos.getY()) <= y && Math.abs(entity.posZ - this.pos.getZ()) <= z;
	}

	// EntityItemの取得
	public EntityItem getEntityItem (BlockPos pos, ItemStack stack) {
		return new EntityItem(this.world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack);
	}

	// AxisAlignedBBの取得
	public AxisAlignedBB getAABB (double x, double y, double z) {
		return this.getAABB(this.pos.add(-x, -y, -z), this.pos.add(x, y, z));
	}

	// AxisAlignedBBの取得
	public AxisAlignedBB getAABB (BlockPos pos1, BlockPos pos2) {
		return new AxisAlignedBB(pos1, pos2);
	}

	// 周囲にプレイヤーがいなる場合
	public boolean findRangePlayer () {
		return this.findRangePlayer(32D, 16D, 32D);
	}

	public boolean findRangePlayer (double x, double y, double z) {
		return !this.getEntityList(EntityPlayer.class, this.getAABB(x, y, z)).isEmpty();
	}

	// えんちちーリストを取得
	public <T extends Entity> List<T> getEntityList (Class <? extends T > entity, AxisAlignedBB aabb) {
		return this.world.getEntitiesWithinAABB(entity, aabb);
	}

	// アイテムリストにアイテムを突っ込む
	public void putList (List<ItemStack> stackList, ItemStack stack) {
		if (stack.isEmpty()) { return; }
		stackList.add(stack);
	}

	// 座標のリスト取得
	public Iterable<BlockPos> getPosList (BlockPos from, BlockPos to) {
		return BlockPos.getAllInBox(from, to);
	}

	// エンティティの最大体力設定
	public void setMaxHealth (EntityLivingBase entity, float maxHealth) {
		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(maxHealth);
		entity.setHealth(maxHealth);
	}

	public ParticleManager getParticle () {
		return ParticleHelper.spawnParticl();
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, boolean dropBlock) {

		IBlockState state = this.getState(pos);
		Block block = state.getBlock();
		if (block == Blocks.AIR) { return false; }

		this.world.playEvent(2001, pos, Block.getStateId(state));

		if (dropBlock) {
			block.dropBlockAsItem(this.world, pos, state, 0);
		}

		return this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	public int getDate () {
		return (int) (this.getTime() / this.dayTime);
	}

	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.getTile(this.pos) != this || player == null) {
			return false;
		}

		return Math.sqrt(player.getDistanceSq(this.pos)) < 256D;
	}

	@Override
	public int getSizeInventory() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return null;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return null;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) { }

	@Override
	public int getInventoryStackLimit() {
		return 0;
	}

	@Override
	public void openInventory(EntityPlayer player) { }

	@Override
	public void closeInventory(EntityPlayer player) { }

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return false;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() { }

	@Override
	public String getName() {
		return null;
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer pInv, EntityPlayer player) {
		return null;
	}

	@Override
	public String getGuiID() {
		return null;
	}

	@Override
	public boolean isLocked() {
		return false;
	}

	@Override
	public void setLockCode(LockCode code) {}

	@Override
	public LockCode getLockCode() {
		return null;
	}
}
