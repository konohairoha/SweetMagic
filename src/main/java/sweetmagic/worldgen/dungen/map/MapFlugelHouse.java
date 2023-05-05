package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.FlugelHousePiece;

public class MapFlugelHouse extends BaseMapGen {

    public MapFlugelHouse(SMChunkGen provider) {
        super(provider);
        this.distance = 37;
    }

    public String getStructureName() {
        return "flugel_house";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.FLUITTOWERFOREST);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapFlugelHouse.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

    	private int posY;
    	private static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
    	private static final IBlockState GRASS = Blocks.GRASS.getDefaultState();
    	private static final IBlockState AIR = Blocks.AIR.getDefaultState();

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
            int posY = this.posY = Math.min(69, Math.min(Math.min(y1, l), Math.min(i1, j1)));

            BlockPos pos = new BlockPos(chunkX * 16 + 8, posY, chunkZ * 16 + 8);
            List<FlugelHousePiece.FlugelHouseTemplate> list = Lists.<FlugelHousePiece.FlugelHouseTemplate>newLinkedList();
            FlugelHousePiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);

            this.components.addAll(list);
            this.updateBoundingBox();
        }

        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int y = sbb.minY; y <= sbb.maxY; ++y) {
					for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {

						BlockPos pos = new BlockPos(x, y, z);
						IBlockState state = world.getBlockState(pos);
	                    Block block = state.getBlock();

	                    if (y < this.posY && ( block == Blocks.AIR || !block.isFullBlock(state) )) {
	                    	world.setBlockState(pos, DIRT, 2);
	                    }

	                    else if (y == this.posY && block == Blocks.AIR) {
	                    	world.setBlockState(pos, GRASS, 2);
	                    }

	                    if (block == Blocks.AIR) { continue; }

	                    if (block == Blocks.GRASS_PATH) {
	                    	world.setBlockState(pos, GRASS, 2);
	                    }

	                    // スイマジスポナー
	                    else if (block == BlockInit.smspaner) {

	                    	Block under = world.getBlockState(pos.down()).getBlock();
	                    	TileSMSpaner spaner = (TileSMSpaner) world.getTileEntity(pos);
	                    	spaner.isWCSide = false;
	                    	spaner.setSpaner();

	                    	if (under == Blocks.IRON_BLOCK) {
	                    		spaner.healthRate = 1.33F;
	                    		this.setAir(world, pos.down());
	                    	}

	                    	else if (under == Blocks.GOLD_BLOCK) {
	                    		spaner.healthRate = 1.67F;
	                    		this.setAir(world, pos.down());
	                    	}

	                    	else if (under == Blocks.DIAMOND_BLOCK) {
	                    		spaner.healthRate = 2F;
	                    		this.setAir(world, pos.down());
	                    	}

	                    	else if (under == BlockInit.cosmos_light_block) {
	                    		spaner.data = 8;
	                    		this.setAir(world, pos.down());
	                    	}
	                    }

	                    // スポーン岩
	                    else if (block == BlockInit.spawn_stone) {

	                    	Block under = this.getBlock(world, pos.down());
							TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);

	                    	if (under == BlockInit.flagstone) {
								tile.isBossSummon = false;
								tile.isWCSide = false;
	                    	}

	                    	else if (under == BlockInit.antique_brick_0l) {
								tile.isBossSummon = false;
								tile.isWCSide = false;
	                    	}

	                    	else if (under == BlockInit.whiteline_brick_b) {
								tile.isPowerUp= 1;
								tile.isBossSummon = false;
								tile.isWCSide = false;
	                    	}
	                    }

	                    // チェスト系なら
	                    else if (block instanceof BlockWoodChest) {

	                    	if (block == BlockInit.estor_woodchest) {
	                    		this.setChest(world, block, state, pos);
								this.setLootTable(world, rand, pos, LootTableInit.MOBCHEST, 0.3F);
	                    	}

	                    	else if (block == BlockInit.rattan_chest_y) {
	                    		this.setChest(world, block, state, pos);
								this.setLootTable(world, rand, pos, LootTableInit.IDOCHEST, 0.25F);
		                    }

	                    	else if (block == BlockInit.rattan_chest_b) {
	                    		this.setChest(world, block, state, pos);
								this.setLootTable(world, rand, pos, LootTableInit.SKYLANDOLD, 0.2F);
		                    }

	                    	else if (block == BlockInit.cafe_kitchen_table) {
	                    		this.setChest(world, block, state, pos);
								this.setLootTable(world, rand, pos, LootTableInit.SKYLANDOLD, 0.275F);
		                    }

	                    	else if (block == BlockInit.cafe_kitchen_sink) {
	                    		this.setChest(world, block, state, pos);
								this.setLootTable(world, rand, pos, LootTableInit.FLUGELCHEST, 0.35F);
		                    }

	                    	else if (block == BlockInit.treasure_chest) {
								this.setLootTable(world, rand, pos, LootTableInit.MEQCHEST, 0.2F);
		                    }
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
