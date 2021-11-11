package sweetmagic.spwanblock;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import sweetmagic.config.SMConfig;
import sweetmagic.init.tile.magic.TileMagiaLight;

public class SpwanBlockRegistry implements INBTSerializable<NBTTagList> {

    private final List<BlockPos> posList = new ArrayList<>();

	public final void register(BlockPos pos) {
		if (!this.posList.contains(pos)) {
			this.posList.add(pos);
		}
	}

	public final void unregister(BlockPos pos) {
		this.posList.remove(pos);
	}

	public boolean isPosInRange(World world, double posX, double posY, double posZ) {

		int range = this.getRange();

		for (BlockPos pos : this.posList) {
			if (this.checkDistance(posX, posZ, pos, range) && this.checkHasMF(world, pos)) { return true; }
		}

		return false;
	}

	public boolean checkHasMF (World world, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		if (tile == null || !(tile instanceof TileMagiaLight)) { return false; }

		TileMagiaLight mf = (TileMagiaLight) tile;
		return mf.getMF() >= mf.getShrinkMF() && mf.isActive(world, pos);
	}

	public boolean checkDistance (double posX, double posZ, BlockPos pos, int range) {
		return Math.abs(posX - pos.getX()) <= range && Math.abs(posZ - pos.getZ()) <= range;
	}

	@Override
	public NBTTagList serializeNBT() {

		NBTTagList tagList = new NBTTagList();

		for (BlockPos loc : this.posList) {
			tagList.appendTag(NBTUtil.createPosTag(loc));
		}

		return tagList;
	}

	@Override
	public void deserializeNBT(NBTTagList list) {
		for (int i = 0; i < list.tagCount(); i++) {
			this.register(NBTUtil.getPosFromTag(list.getCompoundTagAt(i)));
		}
	}

	public BlockPos[] getEntries() {
		return this.posList.toArray(new BlockPos[0]);
	}

	public int getRange () {
		return SMConfig.magiaLightRange;
	}
}
