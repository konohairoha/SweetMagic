package sweetmagic.init.base;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import sweetmagic.init.BiomeInit;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.map.MapGenPyramid;

public class BaseMapGen extends MapGenStructure {

    public int distance;
    public final SMChunkGen provider;

    public BaseMapGen(SMChunkGen provider) {
        this.distance = 32;
        this.provider = provider;
    }

    // 生成物の名前
    public String getStructureName() {
        return "pyramid_main";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.COCONUTBEACH, BiomeInit.COCONUTBEACHHILL);
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {

        int x = chunkX;
        int z = chunkZ;

        if (chunkX < 0) {
            chunkX -= this.distance - 1;
        }

        if (chunkZ < 0) {
            chunkZ -= this.distance - 1;
        }

        int pX = chunkX / this.distance;
        int pZ = chunkZ / this.distance;
        Random rand = this.world.setRandomSeed(pX, pZ, 14267312);
        pX = pX * this.distance;
        pZ = pZ * this.distance;
        pX = pX + rand.nextInt(this.distance - 8);
        pZ = pZ + rand.nextInt(this.distance - 8);

        if (x == pX && z == pZ) {

            boolean flag = this.world.getBiomeProvider().areBiomesViable(x * 16 + 8, z * 16 + 8, 0, this.getBiomeList());
            if (flag) { return true; }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World world, BlockPos pos, boolean find) {
        this.world = world;
        return findNearestStructurePosBySpacing(world, this, pos, this.distance, 8, 10387312, false, 100, find);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenPyramid.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }
}
