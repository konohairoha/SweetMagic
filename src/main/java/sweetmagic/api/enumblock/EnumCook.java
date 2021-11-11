package sweetmagic.api.enumblock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public enum EnumCook implements IStringSerializable {

	OFF("off", 0),
	ON("on", 4),
	FIN("fin", 8);

	private final String name;
	private final int meta;

	EnumCook(String name, int meta) {
		this.name = name;
		this.meta = meta;
	}

	public static List<EnumCook> getCookList() {
		return Arrays.<EnumCook> asList(OFF, ON, FIN);
	}

	public static List<EnumCook> getStoveList() {
		return Arrays.<EnumCook> asList(OFF, ON);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public int getMeta () {
		return this.meta;
	}

	public static FaceCookMeta getMeta (int meta) {

		FaceCookMeta fcMeta = null;

		if (meta < 4) {
			fcMeta = new FaceCookMeta(meta , EnumCook.OFF);
		}

		else if (meta < 8) {
			fcMeta = new FaceCookMeta(meta - 4, EnumCook.ON);
		}

		else if (meta < 12) {
			fcMeta = new FaceCookMeta(meta - 8, EnumCook.FIN);
		}

		return fcMeta;
	}

	public static IBlockState transitionCook (IBlockState state, PropertyCook COOK) {

		switch (state.getValue(COOK)) {
		case OFF:
			return state.withProperty(COOK, EnumCook.ON);
		case ON:
			return state.withProperty(COOK, EnumCook.FIN);
		case FIN:
			return state.withProperty(COOK, EnumCook.OFF);
		}

		return state;
	}

	public static IBlockState transitionStove (IBlockState state, PropertyCook COOK) {

		switch (state.getValue(COOK)) {
		case OFF:
			return state.withProperty(COOK, EnumCook.ON);
		case ON:
			return state.withProperty(COOK, EnumCook.OFF);
		}

		return state;
	}

	public boolean isOFF () {
		return this == EnumCook.OFF;
	}

	public boolean isON () {
		return this == EnumCook.ON;
	}

	public boolean isFIN () {
		return this == EnumCook.FIN;
	}

	public static class PropertyCook extends PropertyEnum<EnumCook> {

		public PropertyCook(String name, Collection<EnumCook> values) {
			super(name, EnumCook.class, values);
		}
	}

	public static class FaceCookMeta {

		private final int meta;
		private final EnumCook cook;

		public FaceCookMeta (int meta, EnumCook cook) {
			this.meta = meta;
			this.cook = cook;
		}

		public int  getMeta () {
			return this.meta;
		}

		public EnumCook getCook () {
			return this.cook;
		}
	}
}
