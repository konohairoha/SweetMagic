package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
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
import sweetmagic.init.block.blocks.BlockWoodChest;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.MekyuPiece;

public class MapGenMekyu extends BaseMapGen {

    public MapGenMekyu(SMChunkGen provider) {
        super(provider);
        this.distance = 32;
    }

    public String getStructureName() {
        return "mekyu";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.PRISMFOREST);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenMekyu.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

    	private static final IBlockState GRASS = Blocks.GRASS.getDefaultState();

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
            List<MekyuPiece.MekyuTemplate> list = Lists.<MekyuPiece.MekyuTemplate>newLinkedList();
            MekyuPiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);

            this.components.addAll(list);
            this.updateBoundingBox();
        }

        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {
					for (int y = sbb.minY; y <= sbb.maxY; ++y) {

						BlockPos pos = new BlockPos(x, y, z);
	                    Block block = this.getBlock(world, pos);
	                    if (block == Blocks.AIR) { continue; }

	                    if (block == Blocks.DIAMOND_BLOCK) {
	                    	world.setBlockState(pos, BlockInit.treasure_chest.getDefaultState().withProperty(BlockWoodChest.FACING, EnumFacing.WEST), 2);
	                    	this.setLootTable(world, rand, pos, LootTableInit.MEQCHEST, 0.425F);
	                    }

						else if (block == BlockInit.treasure_chest) {
							this.setLootTable(world, rand, pos, LootTableInit.PYM, 0.25F);
						}

	                    else if (block == Blocks.GOLD_BLOCK) {
	                    	world.setBlockState(pos, BlockInit.smspaner.getDefaultState(), 2);
	                    	this.setSMSpaner(world, rand, pos, true);
	                    }

	                    else if (block == Blocks.CHEST) {
	                    	this.setLootTable(world, rand, pos, this.getLoot(rand));
	                    }

	                    else if (block == Blocks.TRAPPED_CHEST) {
	                    	this.setLootTable(world, rand, pos, LootTableInit.PYM);
	                    }

	                    else if (block == BlockInit.cosmos_light_block) {
	                    	world.setBlockState(pos, BlockInit.treasure_chest.getDefaultState().withProperty(BlockWoodChest.FACING, EnumFacing.SOUTH), 2);
	                    	this.setLootTable(world, rand, pos, LootTableInit.FLUGELCHEST, 0.525F);
	                    }

						else if (block == BlockInit.spawn_stone) {

		                    Block under = this.getBlock(world, pos.down());

		                    if (under == BlockInit.longtile_brick_r || under == BlockInit.longtile_brick_p) {

								world.setBlockState(pos, BlockInit.spawn_stone.getDefaultState(), 2);

								TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
								tile.isPowerUp= 1;
								tile.isBossSummon = false;
								tile.isWCSide = true;
		                    }

		                    else if (under == Blocks.SKULL) {
								TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
								tile.isPowerUp= 1;
		                    }

		                    else {
								TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
								tile.isWCSide = true;
		                    }
						}

						else if (block == Blocks.DIRT && world.getBlockState(pos.up()).getBlock() == Blocks.AIR) {
	                    	world.setBlockState(pos, GRASS, 2);
						}
	                }
				}
            }
        }

        // ルートテーブルの取得
        public ResourceLocation getLoot (Random rand) {
        	return rand.nextBoolean() ? LootTableInit.MOBCHEST : LootTableList.CHESTS_END_CITY_TREASURE;
        }
    }
}
