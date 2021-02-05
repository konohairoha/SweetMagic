package sweetmagic.worldgen.dimension;

import javax.annotation.Nonnull;

import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.DimensionInit;

public class WorldProviderSweetMagic extends WorldProvider {

	@Override
	public DimensionType getDimensionType() {
		return DimensionInit.sweetmagic;
	}

	@Override
	@Nonnull
	public String getSaveFolder() {
		return "DIM-SweetMagic";
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return new ChunkGeneratorOverworld(this.world, this.world.getSeed() * 2, true, "");
	}

	@Override
	protected void init() {
		this.doesWaterVaporize = false;
		this.hasSkyLight = true;
		this.biomeProvider = new BiomeProvider(world.getWorldInfo());
//		this.biomeProvider = new SMBiomeProvider(this.world.getWorldInfo());
	}

	// 雲の高さ
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 192F;
	}
}
