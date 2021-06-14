package sweetmagic.worldgen.dimension;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.DimensionInit;

public class WorldProviderSweetMagic extends WorldProvider {

    private IChunkGenerator chunkGenerator;

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
        return this.chunkGenerator;
	}

	@Override
	protected void init() {
		super.init();
        this.chunkGenerator = new SMChunkGen(this.world, this.getSeed(), true, this.world.getWorldInfo().getGeneratorOptions());
        this.biomeProvider = new SMBiomeProvider(this.world.getWorldInfo());
	}

	// 雲の高さ
	@Override
	@SideOnly(Side.CLIENT)
	public float getCloudHeight() {
		return 192F;
	}

	public WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos) {
		return WorldSleepResult.ALLOW;
	}
}
