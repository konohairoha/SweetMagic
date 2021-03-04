package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
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
import sweetmagic.init.base.BaseMaoGen;
import sweetmagic.init.base.BaseStructureStart;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.PyramidPiece;

public class MapGenPyramid extends BaseMaoGen {

    public MapGenPyramid(SMChunkGen provider) {
        super(provider);
    }

    public String getStructureName() {
        return "pyramid_main";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.COCONUTBEACH);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenPyramid.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

        public Start() { }

        public Start(World world, SMChunkGen chunk, Random rabd, int chunkX, int chunkZ) {
            super(world, chunk, rabd, chunkX, chunkZ);
        }

        public void create(World world, SMChunkGen chunk, Random rand, int chunkX, int chunkZ) {

            Rotation rot = Rotation.NONE;
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
            List<PyramidPiece.PyramidTemplate> list = Lists.<PyramidPiece.PyramidTemplate>newLinkedList();
            PyramidPiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);

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

	                    Block block = world.getBlockState(pos).getBlock();

	                    if (block == Blocks.DIAMOND_BLOCK) {
	                    	this.setMobSpawner(world, rand, pos);
	                    }

	                    else if (block == Blocks.GOLD_BLOCK) {
	                    	world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST), 2);
	                    	this.setLootTable(world, rand, pos, LootTableList.CHESTS_END_CITY_TREASURE);
	                    }

	                    else if (block == Blocks.LAPIS_BLOCK) {
	                    	world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.WEST), 2);
	                    	this.setLootTable(world, rand, pos, LootTableInit.PYM);
	                    }

	                    else if (block == Blocks.IRON_BLOCK) {
	                    	world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST), 2);
	                    	this.setLootTable(world, rand, pos, LootTableList.CHESTS_WOODLAND_MANSION);
	                    }

	                    else if (block == BlockInit.alt_block) {
	                    	world.setBlockState(pos, Blocks.TRAPPED_CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.SOUTH), 2);
	                    	this.setLootTable(world, rand, pos, LootTableList.CHESTS_SIMPLE_DUNGEON);
	                    }
	                }
				}
            }
        }
    }
}
