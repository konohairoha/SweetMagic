package sweetmagic.worldgen.dungen.map;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
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
import sweetmagic.init.base.BaseMaoGen;
import sweetmagic.init.base.BaseStructureStart;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.KutiMuraPiece;

public class MapGenKutiMura extends BaseMaoGen {

    public MapGenKutiMura(SMChunkGen provider) {
        super(provider);
        this.distance = 35;
    }

    public String getStructureName() {
        return "kuchihatetamura";
    }

    // バイオームリストの取得
    public List<Biome> getBiomeList () {
    	return Arrays.<Biome>asList(BiomeInit.FLUITFOREST);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new MapGenKutiMura.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

    	public int posY;

        public Start() { }

        public Start(World world, SMChunkGen chunk, Random rabd, int chunkX, int chunkZ) {
            super(world, chunk, rabd, chunkX, chunkZ);
        }

        @Override
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
            int posY = this.posY = Math.min(Math.min(y1, l), Math.min(i1, j1));

            BlockPos pos = new BlockPos(chunkX * 16 + 8, posY, chunkZ * 16 + 8);
            List<KutiMuraPiece.KutiMuraTemplate> list = Lists.<KutiMuraPiece.KutiMuraTemplate>newLinkedList();
            KutiMuraPiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);
            this.components.addAll(list);
            this.updateBoundingBox();
        }

        @Override
        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {
					for (int y = sbb.minY; y <= sbb.maxY; ++y) {

						BlockPos pos = new BlockPos(x, y, z);
	                    Block block = world.getBlockState(pos).getBlock();

	                    if (y < this.posY && block == Blocks.AIR) {
	                    	world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
	                    }

	                    if (block == Blocks.AIR) { continue; }

	                    if (block == BlockInit.treasure_chest) {
	                    	this.setLootTable(world, rand, pos, LootTableInit.PYM, 0.25F);
	                    }

	                    else if (block == Blocks.GOLD_BLOCK) {
	                    	world.setBlockState(pos, Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, EnumFacing.EAST), 2);
	                    	this.setLootTable(world, rand, pos, this.getLootTable(rand));
	                    }

	                    else if (block == BlockInit.smspaner) {
	                    	this.setSMSpaner(world, rand, pos);
	                    }

	                    else if (block == Blocks.PACKED_ICE) {
	                    	world.setBlockState(pos, Blocks.DIRT.getDefaultState(), 2);
	                    }
	                }
				}
			}
		}

        public ResourceLocation getLootTable (Random rand) {

        	ResourceLocation src = null;

        	switch (rand.nextInt(3)) {
        	case 0:
        		src = LootTableInit.SMBOUNUS;
        		break;
        	case 1:
        		src = LootTableList.CHESTS_END_CITY_TREASURE;
        		break;
        	case 2:
        		src = LootTableInit.MOBCHEST;
        		break;
        	}

        	return src;
        }
    }
}
