package sweetmagic.init.tile.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileSMBase extends TileEntity implements ITickable {

	public int tickTime = 0;	// 稼働してる時間

	@Override
	public void update() {

		// クライアント
		if (this.world.isRemote) {
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
		NBTTagCompound tags = new NBTTagCompound();
		this.writeToNBT(tags);
		return new SPacketUpdateTileEntity(this.pos, -50, tags);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		return this.writeToNBT(new NBTTagCompound());
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

	public boolean isActive(World world, BlockPos pos) {
		return this.isRedStonePower(world, pos) ? false : true;
	}

	// レッドストーン信号を受けているかを判断する
	public boolean isRedStonePower(World world, BlockPos pos) {
		int redstone = 0;
		for(EnumFacing dir : EnumFacing.VALUES) {
			redstone = Math.max(redstone, world.getRedstonePower(pos.offset(dir), dir));
		}
		return redstone > 0;
	}
}
