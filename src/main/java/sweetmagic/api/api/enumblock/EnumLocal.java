package sweetmagic.api.enumblock;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public enum EnumLocal implements IStringSerializable {

	TOP("top"),
	CEN("center"),
	BOT("bottom"),
	NOR("normal");

	private final String name;

	EnumLocal(String name) {
		this.name = name;
	}

	public static List<EnumLocal> getLocalList() {
		return Arrays.<EnumLocal> asList(TOP, CEN, BOT, NOR);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public static EnumLocal getLocal(boolean top, boolean bot) {

		EnumLocal local = EnumLocal.NOR;

		if (top) {
			if (bot) {
				local = EnumLocal.CEN;
			}

			else {
				local = EnumLocal.TOP;
			}
		}

		else {
			if (bot) {
				local = EnumLocal.BOT;
			}
		}

		return local;
	}

	public static class PropertyLocal extends PropertyEnum<EnumLocal> {

		public PropertyLocal(String name, Collection<EnumLocal> values) {
			super(name, EnumLocal.class, values);
		}
	}
}
