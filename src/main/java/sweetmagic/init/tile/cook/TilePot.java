package sweetmagic.init.tile.cook;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.block.blocks.BlockFryPan;
import sweetmagic.init.block.blocks.BlockPot;
import sweetmagic.init.tile.magic.TileSMBase;

public class TilePot extends TileSMBase {

	public boolean tickSet = true;
	public boolean startFlag = false;
	public ItemStack handItem = ItemStack.EMPTY;			// 手に持ってるアイテム
	public List<ItemStack> inPutList = new ArrayList<>();	// 入力アイテム
	public List<ItemStack> outPutList = new ArrayList<>(); // 出力アイテム

	// 初期化
	public void clear () {
		this.tickTime = 1;
		this.tickSet = false;
	}

	public void clearItem() {
		this.startFlag = false;
		this.handItem = ItemStack.EMPTY;
		this.inPutList.clear();
		this.outPutList.clear();
	}

	@Override
	public void update() {

		if (!this.startFlag) { return; }

		this.tickTime++;
		IBlockState state = this.getState(this.pos);
		EnumCook cook = this.getCook(state);
		if (cook == null || !cook.isON()) { return; }

		Block block = state.getBlock();

		// 鍋
		if (this.isPot(block)) {
			this.potSetBlock(state);
		}

		// フライパン
		else if (this.isFryPan(block)) {
			this.frypanSetBlock(state);
		}
	}

	// 鍋
	public void potSetBlock(IBlockState state) {

		if(this.tickSet) {
			this.clear();
		}

		if (this.tickTime % 10 == 0) {
			this.playSound(this.pos, SMSoundEvent.POT, 0.4F, 1F);
		}

		// 5秒たつとBlockStateを上書き
		if (this.tickTime % 101 == 0) {
			this.tickTime = 0;

			// BlockStateを置き換え
			if (!this.world.isRemote) {
				BlockPot.setState(this.world, this.pos);
				this.playSound(this.pos, SMSoundEvent.STOVE_OFF, 1F, 1F);
				this.tickSet = true;
			}
		}
	}

	// フライパン
	public void frypanSetBlock(IBlockState state) {

		if(this.tickSet) {
			this.clear();
			this.playSound(this.pos, SMSoundEvent.FRYPAN, 0.4F, 1F);
		}

		if (this.tickTime % 10 == 0) {
			this.playSound(this.pos, SMSoundEvent.FRYPAN, 0.4F, 1F);
		}

		if (this.tickTime % 15 == 0 && !this.isSever()) {

			Random rand = this.world.rand;

			for (int i = 0; i < 4; i++) {
				float f1 = this.pos.getX() + 0.1F + rand.nextFloat() * 0.8F;
				float f2 = this.pos.getY() + 0.15F + rand.nextFloat() * 0.375F;
				float f3 = this.pos.getZ() + 0.1F + rand.nextFloat() * 0.8F;
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f1, f2, f3, 0, 0, 0);
			}
		}

		//5秒たつとBlockStateを上書き
		if (this.tickTime % 101 == 0) {
			this.tickTime = 0;

			//BlockStateを置き換え
			if (this.isSever()) {
				this.playSound(this.pos, SMSoundEvent.STOVE_OFF, 1F, 1F);
				BlockFryPan.setState(this.world, this.pos);
				this.tickSet = true;
			}
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (!this.handItem.isEmpty()) {
			tags.setBoolean("startFlag", this.startFlag);
			tags.setTag("handItem", this.handItem.writeToNBT(new NBTTagCompound()));
		}
		this.saveStackList(tags, this.inPutList, "input");
		this.saveStackList(tags, this.outPutList, "output");
		return tags;

	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		if (this.handItem.isEmpty()) {
			this.startFlag = tags.getBoolean("startFlag");
			this.handItem = new ItemStack(tags.getCompoundTag("handItem"));
		}
		this.inPutList = loadAllItems(tags, "input");
		this.outPutList = loadAllItems(tags, "output");
	}

	public boolean isPot (Block block) {
		return block instanceof BlockPot;
	}

	public boolean isFryPan (Block block) {
		return block instanceof BlockFryPan;
	}

	public EnumCook getCook (IBlockState state) {

		Block block = state.getBlock();

		if (this.isPot(block)) {
			return ((BlockPot) block).getCook(state);
		}

		else if (this.isFryPan(block)) {
			return ((BlockFryPan) block).getCook(state);
		}

		return null;
	}
}