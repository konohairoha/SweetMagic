package sweetmagic.init.tile.cook;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.block.blocks.BlockFlourMill;
import sweetmagic.init.block.blocks.BlockOven;
import sweetmagic.init.tile.magic.TileSMBase;

public class TileFlourMill extends TileSMBase {

	public boolean tickSet = true;
	public ItemStack handItem = ItemStack.EMPTY;			// 手に持ってるアイテム
	public List<ItemStack> inPutList = new ArrayList<>();	// 入力アイテム
	public List<ItemStack> outPutList  = new ArrayList<>();// 出力アイテム
	int time = 21;

	// 初期化
	public void clear () {
		this.tickTime = 1;
		this.tickSet = false;
	}

	public void clearItem() {
		this.handItem = ItemStack.EMPTY;
		this.inPutList.clear();
		this.outPutList.clear();
	}

	@Override
	public void update() {

		this.tickTime++;
		if (this.tickTime % this.time != 0) { return; }

		IBlockState state = this.getState(this.pos);
		EnumCook cook = this.getCook(state);
		if (cook == null || !cook.isON()) { return; }

		Block block = this.getBlock(this.pos);

		//オーブン
		if (this.isOven(block)) {
			this.ovenSetBlock(block);
		}

		//製粉機
		else if (this.isFlourMill(block)) {
			this.flourmillSetBlock(state);
		}

		if (this.tickTime % 105 == 0) {
			this.tickTime = 0;
		}
	}

	// オーブン
	public void ovenSetBlock(Block block) {

		if(this.tickSet) {
			this.clear();
			this.time = 10;
		}

		if(this.tickTime % 10 == 0 && this.tickTime < 100) {
			this.playSound(this.pos, SMSoundEvent.OVEN_ON, 0.5F, 1F);
		}

		//5秒たつとBlockStateを上書き
		if (this.tickTime >= 100 && this.tickTime % 100 == 0 && !this.tickSet) {

			this.clear();
			this.time = 21;

			//BlockStateを置き換え
			if (!this.world.isRemote) {
				BlockOven.setState(this.world, this.pos);
				this.playSound(this.pos, SMSoundEvent.OVEN_FIN, 1F, 1F);
			}
		}
	}

	// 製粉機
	public void flourmillSetBlock(IBlockState state) {

		if(this.tickSet) {
			this.clear();
			this.playSound(this.pos, SMSoundEvent.MACHIN, 0.4F, 1F);
		}

		if(this.tickTime % 21 == 0 && this.tickTime < 100) {
			this.playSound(this.pos, SMSoundEvent.MACHIN, 0.4F, 1F);
		}

		// 5秒たつとBlockStateを上書き
		if (this.tickTime % 105 == 0 && !this.tickSet) {
			this.clear();

			// BlockStateを置き換え
			if (!this.world.isRemote) {
				this.playSound(this.pos, SMSoundEvent.DROP, 1F, 1F);
				BlockFlourMill.setState(this.world, this.pos);
			}
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		if (!this.handItem.isEmpty()) {
			tags.setBoolean("tickSet", this.tickSet);
			tags.setTag("handItem", this.handItem.writeToNBT(new NBTTagCompound()));
		}
		this.saveStackList(tags, this.inPutList, "input");
		this.saveStackList(tags, this.outPutList, "output");
		return tags;

	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		if (this.handItem.isEmpty()) {
			this.tickSet = tags.getBoolean("tickSet");
			this.handItem = new ItemStack(tags.getCompoundTag("handItem"));
		}
		this.inPutList = loadAllItems(tags, "input");
		this.outPutList = loadAllItems(tags, "output");
	}
	public boolean isFlourMill (Block block) {
		return block instanceof BlockFlourMill;
	}

	public boolean isOven (Block block) {
		return block instanceof BlockOven;
	}

	public EnumCook getCook (IBlockState state) {

		Block block = state.getBlock();

		if (this.isFlourMill(block)) {
			return ((BlockFlourMill) block).getCook(state);
		}

		else if (this.isOven(block)) {
			return ((BlockOven) block).getCook(state);
		}

		return null;
	}
}
