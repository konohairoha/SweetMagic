package sweetmagic.worldgen.dungen.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import sweetmagic.init.BiomeInit;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMaoGen;
import sweetmagic.init.base.BaseStructureStart;
import sweetmagic.util.SweetState;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.VillagerPiece;

public class MapGenVillager extends BaseMaoGen {

    public MapGenVillager(SMChunkGen provider) {
        super(provider);
        this.distance = 24;
    }

    public String getStructureName() {
        return "villager";
    }

    // バイオームリストの取得
	public List<Biome> getBiomeList() {
		return Arrays.<Biome> asList(BiomeInit.BIGPLATE, BiomeInit.FLUITFOREST);
    }

	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new MapGenVillager.Start(this.world, this.provider, this.rand, chunkX, chunkZ);
    }

    // 生成クラス
    public static class Start extends BaseStructureStart {

    	private static final IBlockState dirt = Blocks.DIRT.getDefaultState();
    	private static final IBlockState glass = Blocks.GRASS.getDefaultState();
    	private List<BlockPos> posList = new ArrayList<>();
    	public int posY;

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
            int y2 = primer.findGroundBlockIdx(7, 7 + j);
            int y3 = primer.findGroundBlockIdx(7 + i, 7);
            int y4 = primer.findGroundBlockIdx(7 + i, 7 + j);
            int posY = this.posY = Math.min(Math.min(y1, y2), Math.min(y3, y4));

            BlockPos pos = new BlockPos(chunkX * 16 + 8, posY + 1, chunkZ * 16 + 8);
            List<VillagerPiece.VillagerTemplate> list = Lists.<VillagerPiece.VillagerTemplate>newLinkedList();
            posList = new ArrayList<>();
            VillagerPiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), world, pos, Rotation.NONE, list, rand, posList);

            this.components.addAll(list);
            this.updateBoundingBox();
        }

        public void generateStructure(World world, Random rand, StructureBoundingBox sbb) {

            super.generateStructure(world, rand, sbb);

            IBlockState punp = this.getPunp(rand);
            IBlockState jack = this.getJack(rand);
            IBlockState melon = this.getMelon(rand);
            IBlockState lapi = this.getLapi(rand);
            IBlockState alt = this.getAlt(rand);
            IBlockState sample = this.getSample(rand);
            IBlockState babu = this.getBabu(rand);

			for (int x = sbb.minX; x <= sbb.maxX; ++x) {
				for (int z = sbb.minZ; z <= sbb.maxZ; ++z) {
					for (int y = sbb.minY; y <= sbb.maxY; ++y) {

						BlockPos pos = new BlockPos(x, y, z);
						IBlockState state = world.getBlockState(pos);
						Block block = state.getBlock();

						if (!world.isRemote && this.checkBlock(block, state)) {
							EntityVillager entity = new EntityVillager(world);
							VillagerRegistry.setRandomProfession(entity, world.rand);
							entity.setGrowingAge(0);
							entity.setLocationAndAngles(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, 0, 0F);
							world.spawnEntity(entity);
						}

						else if (block == Blocks.PUMPKIN) {
	                    	world.setBlockState(pos, punp, 2);
						}

						else if (block == Blocks.LIT_PUMPKIN) {
	                    	world.setBlockState(pos, jack, 2);
						}

						else if (block == Blocks.MELON_BLOCK) {
	                    	world.setBlockState(pos, melon, 2);
						}

						else if (block == Blocks.LAPIS_BLOCK) {
	                    	world.setBlockState(pos, lapi, 2);
						}

						else if (block == BlockInit.alt_block) {
	                    	world.setBlockState(pos, alt, 2);
						}

						else if (block == BlockInit.sample) {
	                    	world.setBlockState(pos, sample, 2);
						}

						else if (block == BlockInit.magic_circle) {
	                    	world.setBlockState(pos, babu, 2);
						}

	                    if (y <= this.posY && block == Blocks.AIR) {
	                    	world.setBlockState(pos, dirt, 2);
	                    }

	                    else if (y == this.posY + 1 && block == Blocks.AIR) {
	                    	world.setBlockState(pos, glass, 2);
	                    }
					}
                }
            }
        }

        public boolean checkBlock (Block block, IBlockState state) {
        	return block instanceof BlockDoor && state.getValue(BlockDoor.HALF) == BlockDoor.EnumDoorHalf.LOWER;
        }

        // パンプキン
        public IBlockState getPunp (Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.sannyflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 1: return BlockInit.moonblossom_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 2: return BlockInit.sugarbell_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 3: return BlockInit.fire_nasturtium_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	}
        	return null;
        }

        // ジャックオランタン
        public IBlockState getJack (Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.glowflower_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 1: return BlockInit.sticky_stuff_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 2: return BlockInit.cotton_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 3: return BlockInit.clerodendrum.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	}
        	return null;
        }

        // スイカ
        public IBlockState getMelon(Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.vannila_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 1: return BlockInit.olive_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 2: return BlockInit.blueberry_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 3: return BlockInit.raspberry_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
        	}
        	return null;
        }

        // ラぴ
        public IBlockState getLapi(Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.corn_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 1: return BlockInit.tomato_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 2: return BlockInit.egg_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 3: return BlockInit.coffee_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	}
        	return null;
        }

        // 泡
        public IBlockState getBabu(Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.rice_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
        	case 1: return BlockInit.cabbage_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 2: return BlockInit.lettuce_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 3: return BlockInit.sweetpotato_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	}
        	return null;
        }

        // サンプル
        public IBlockState getSample(Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.strawberry_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 1: return BlockInit.onion_plant.getDefaultState().withProperty(SweetState.STAGE5, 4);
        	case 2: return BlockInit.soybean_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
        	case 3: return BlockInit.azuki_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
        	}
        	return null;
        }

        // オルタナティブ
        public IBlockState getAlt(Random rand) {
        	switch (rand.nextInt(4)) {
        	case 0: return BlockInit.spinach_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 1: return BlockInit.j_radish_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	case 2: return BlockInit.rice_plant.getDefaultState().withProperty(SweetState.STAGE6, 5);
        	case 3: return BlockInit.strawberry_plant.getDefaultState().withProperty(SweetState.STAGE4, 3);
        	}
        	return null;
        }
    }
}
