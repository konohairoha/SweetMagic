package sweetmagic.util;

import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;

public class SweetState {

	// int系
	public static final PropertyInteger TYPE16 = PropertyInteger.create("type", 0, 15);
	public static final PropertyInteger TYPE8 = PropertyInteger.create("type", 0, 7);
	public static final PropertyInteger TYPE4 = PropertyInteger.create("type", 0, 3);

	// boolean
	public static final PropertyBool FLAG = PropertyBool.create("flag");
	public static final PropertyBool POWERED = PropertyBool.create("powered");

	// crop base BlockState
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

	public static boolean getBool(IBlockState state, PropertyBool prop) {
		return state != null && hasProperty(state, prop) ? state.getValue(prop) : false;
	}

	public static IBlockState setInt(IBlockState state, PropertyInteger prop, int i) {
		return state != null && hasProperty(state, prop) ? state.withProperty(prop, i) : null;
	}

	public static IBlockState setBool(IBlockState state, PropertyBool prop, boolean i) {
		return state != null && hasProperty(state, prop) ? state.withProperty(prop, i) : null;
	}

	public static boolean hasProperty(IBlockState state, IProperty prop) {
		return state.getProperties().containsKey(prop);
	}
}
