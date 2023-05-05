package sweetmagic.init;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;
import sweetmagic.config.SMConfig;
import sweetmagic.worldgen.dimension.WorldProviderSweetMagic;

public class DimensionInit {

	public static int dimID = SMConfig.dimId;
	public static DimensionType sweetmagic;

    public static Map<Integer, String> dimensionProfileMap = new HashMap<>();

	public static void init() {

		sweetmagic = DimensionType.register("SweetMagic Dimension", "_sweet", dimID, WorldProviderSweetMagic.class, false);
		DimensionManager.registerDimension(dimID, sweetmagic);

	}
}
