package sweetmagic.api.iitem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import sweetmagic.util.ItemHelper;

public interface IRangePosTool {

	public final static String STARTX = "sX";
	public final static String STARTY = "sY";
	public final static String STARTZ = "sZ";
	public final static String ENDX = "eX";
	public final static String ENDY = "eY";
	public final static String ENDZ = "eZ";

	default BlockPos getStartPos (NBTTagCompound tags) {
		return new BlockPos(tags.getInteger(STARTX), tags.getInteger(STARTY), tags.getInteger(STARTZ));
	}

	// 終点の座標取得
	default BlockPos getEndPos (NBTTagCompound tags) {
		return new BlockPos(tags.getInteger(ENDX), tags.getInteger(ENDY), tags.getInteger(ENDZ));
	}

	// 座標リセット
	default void resetPos (ItemStack stack) {
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		tags.removeTag(STARTX);
		tags.removeTag(STARTY);
		tags.removeTag(STARTZ);
		tags.removeTag(ENDX);
		tags.removeTag(ENDY);
		tags.removeTag(ENDZ);
	}
}
