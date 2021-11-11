package sweetmagic.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;

public class SweetState {

	// 作物のプロパティ
	public static final PropertyInteger STAGE2 = PropertyInteger.create("stage", 0, 1);
	public static final PropertyInteger STAGE3 = PropertyInteger.create("stage", 0, 2);
	public static final PropertyInteger STAGE4 = PropertyInteger.create("stage", 0, 3);
	public static final PropertyInteger STAGE5 = PropertyInteger.create("stage", 0, 4);
	public static final PropertyInteger STAGE6 = PropertyInteger.create("stage", 0, 5);
	public static final PropertyInteger STAGE7 = PropertyInteger.create("stage", 0, 6);
	public static final PropertyInteger STAGE8 = PropertyInteger.create("stage", 0, 7);

	public static int getInt(IBlockState state, PropertyInteger prop) {
		return state != null && hasProperty(state, prop) ? state.getValue(prop) : -1;
	}

	public static IBlockState setInt(IBlockState state, PropertyInteger prop, int i) {
		return state != null && hasProperty(state, prop) ? state.withProperty(prop, i) : null;
	}

	public static boolean hasProperty(IBlockState state, IProperty prop) {
		return state.getProperties().containsKey(prop);
	}
}
