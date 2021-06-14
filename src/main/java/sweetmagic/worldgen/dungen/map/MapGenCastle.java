package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
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
import sweetmagic.init.base.BaseMaoGen;
import sweetmagic.init.base.BaseStructureStart;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.init.tile.magic.TileSpawnStone;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.CastlePiece;

public class MapGenCastle extends BaseMaoGen {

    public MapGenCastle(SMChunkGen provider) {
        super(provider);
        this.distance = 32;
    }

    public String getStructureName() {
        return "castle";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.FROZENFOREST, BiomeInit.PRISMFOREST);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenCastle.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

    	public int posY;
    	private static final IBlockState DIRT = Blocks.DIRT.getDefaultState();
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
            int posY = this.posY = Math.min(69, Math.min(Math.min(y1, l), Math.min(i1, j1)));

            BlockPos pos = new BlockPos(chunkX * 16 + 8, posY, chunkZ * 16 + 8);
            List<CastlePiece.CastleTemplate> list = Lists.<CastlePiece.CastleTemplate>newLinkedList();
            CastlePiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);

            this.components.addAll(list);
            this.updateBoundingBox();
        }

        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int y = sbb.minY; y <= sbb.maxY; ++y) {
					for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {

						BlockPos pos = new BlockPos(x, y, z);
	                    Block block = world.getBlockState(pos).getBlock();

	                    if (y < this.posY && block == Blocks.AIR) {
	                    	world.setBlockState(pos, DIRT, 2);
	                    }

	                    else if (y == this.posY && block == Blocks.AIR) {
	                    	world.setBlockState(pos, GRASS, 2);
	                    }

	                    if (block == Blocks.AIR) { continue; }

	                    if (block == BlockInit.treasure_chest) {

	                    	Block upBlock = this.getBlock(world, pos.up());
	                    	if (upBlock == Blocks.LAPIS_BLOCK || upBlock == BlockInit.smspaner) {
								this.setLootTable(world, rand, pos, LootTableInit.PYM, 0.2F);

								if (upBlock == Blocks.LAPIS_BLOCK) {
									this.setAir(world, pos.up());
								}
	                    	}

	                    	else if (upBlock == Blocks.IRON_BLOCK) {
								this.setLootTable(world, rand, pos, LootTableInit.IDOCHEST, 0.25F);
								this.setAir(world, pos.up());
	                    	}

	                    	else if (upBlock == Blocks.GOLD_BLOCK) {
								this.setLootTable(world, rand, pos, LootTableInit.MEQCHEST, 0.25F);
								this.setAir(world, pos.up());
	                    	}

	                    	else if (upBlock == Blocks.DIAMOND_BLOCK) {
								this.setLootTable(world, rand, pos, LootTableInit.CASTLECHEST, 0.375F);
								this.setAir(world, pos.up());
	                    	}

	                    	else {
								this.setLootTable(world, rand, pos, LootTableInit.MOBCHEST, 0.25F);
	                    	}

						}

	                    else if (block == BlockInit.smspaner) {

	                    	if (this.getBlock(world, pos.up()) != Blocks.BONE_BLOCK) {
		                    	this.setSMSpaner(world, rand, pos);
	                    	}

	                    	else {
								this.setAir(world, pos.up());
	                    	}

	                    	TileSMSpaner tile = ( TileSMSpaner) world.getTileEntity(pos);
	                    	tile.healthRate = 2F;
	                    }

	                    else if (block == Blocks.CHEST) {
	                    	this.setLootTable(world, rand, pos, this.getLoot(rand));
	                    }

						else if (block == BlockInit.spawn_stone) {

		                    Block underBlock = this.getBlock(world, pos.down());

		                    if (underBlock != Blocks.PACKED_ICE && underBlock != Blocks.MAGMA && underBlock != BlockInit.ac_ore && underBlock != BlockInit.cosmic_crystal_ore) {

								world.setBlockState(pos, BlockInit.spawn_stone.getDefaultState(), 2);
								TileSpawnStone tile = (TileSpawnStone) world.getTileEntity(pos);
								tile.isPowerUp= 1;
								tile.isBossSummon = false;
		                    }
						}

						else if (block == Blocks.DISPENSER) {
							world.setBlockState(pos, Blocks.DISPENSER.getDefaultState().withProperty(BlockDispenser.FACING, EnumFacing.EAST), 2);
							TileEntityDispenser tile = (TileEntityDispenser) world.getTileEntity(pos);
							tile.addItemStack(new ItemStack(Items.LAVA_BUCKET));
							block.getDefaultState().withRotation(Rotation.CLOCKWISE_180);
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
