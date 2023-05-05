package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.storage.loot.LootTableList;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.LootTableInit;
import sweetmagic.init.base.BaseMapGen;
import sweetmagic.init.base.BaseStructureStart;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.WitchHousePiece;

public class MapWitchHouse extends BaseMapGen {

    public MapWitchHouse(SMChunkGen provider) {
        super(provider);
        this.distance = 38;
    }

    public String getStructureName() {
        return "witchhouse";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.ESTORFOREST, BiomeInit.FLOWERGARDEN);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapWitchHouse.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

        public Start() { }

        public Start(World world, SMChunkGen chunk, Random rabd, int chunkX, int chunkZ) {
            super(world, chunk, rabd, chunkX, chunkZ);
        }

        public void create(World world, SMChunkGen chunk, Random rand, int chunkX, int chunkZ) {

            ChunkPrimer primer = new ChunkPrimer();
            chunk.setChunkGen(chunkX, chunkZ, primer);
            int i = 5;
            int j = 5;

            int y1 = primer.findGroundBlockIdx(7, 7);
            int l = primer.findGroundBlockIdx(7, 7 + j);
            int i1 = primer.findGroundBlockIdx(7 + i, 7);
            int j1 = primer.findGroundBlockIdx(7 + i, 7 + j);
            int posY = Math.min(69, Math.min(Math.min(y1, l), Math.min(i1, j1)));

			BlockPos pos = new BlockPos(chunkX * 16 + 8, posY, chunkZ * 16 + 8);
			List<WitchHousePiece.WitchHouseTemplate> list = Lists.<WitchHousePiece.WitchHouseTemplate> newLinkedList();
            WitchHousePiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, Rotation.NONE, list, rand);

            this.components.addAll(list);
            this.updateBoundingBox();
        }

        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int y = sbb.minY; y <= sbb.maxY; ++y) {
					for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {

						BlockPos pos = new BlockPos(x, y, z);
	                    if (world.isAirBlock(pos) || !this.boundingBox.isVecInside(pos)) { continue; }

	                    Block block = this.getBlock(world, pos);

	                    if (block == BlockInit.treasure_chest) {
							this.setLootTable(world, rand, pos, this.getLoot(rand), 0.375F);
	                    }

	                    else if (block == BlockInit.smspaner) {
	                    	this.setSMSpaner(world, rand, pos, true);
	                    }

	                    else if (block == BlockInit.spawn_stone) {
							TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
							tile.isWCSide = true;
	                    }
	                }
				}
            }
        }

        // ルートテーブルの取得
        public ResourceLocation getLoot (Random rand) {

        	switch (rand.nextInt(4)) {
        	case 0:
        		return LootTableInit.MOBCHEST;
        	case 1:
        		return LootTableList.CHESTS_END_CITY_TREASURE;
        	case 2:
        		return LootTableInit.IDOCHEST;
        	case 3:
        		return LootTableInit.PYM;
        	}

    		return LootTableInit.MOBCHEST;
        }
    }
}
