package sweetmagic.init.base;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.tile.chest.TileWoodChest;
import sweetmagic.init.tile.magic.TileSMSpaner;
import sweetmagic.worldgen.dimension.SMChunkGen;
import sweetmagic.worldgen.dungen.piece.PyramidPiece;

public class BaseStructureStart extends StructureStart {

    public BaseStructureStart() { }

    public BaseStructureStart(World world, SMChunkGen chunk, Random rabd, int chunkX, int chunkZ) {
        super(chunkX, chunkZ);
        this.create(world, chunk, rabd, chunkX, chunkZ);
    }

    public Rotation getRot (Random rand) {

        Rotation rot = Rotation.NONE;

        switch (rand.nextInt(4)) {
        case 0:
        	return Rotation.NONE;
        case 1:
        	return Rotation.CLOCKWISE_90;
        case 2:
        	return Rotation.CLOCKWISE_180;
        case 3:
        	return Rotation.COUNTERCLOCKWISE_90;
        }

        return rot;
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
        int posY = Math.min(Math.min(y1, l), Math.min(i1, j1));

        BlockPos pos = new BlockPos(chunkX * 16 + 8, posY, chunkZ * 16 + 8);
        List<PyramidPiece.PyramidTemplate> list = Lists.<PyramidPiece.PyramidTemplate>newLinkedList();
        PyramidPiece.generateCore(world.getSaveHandler().getStructureTemplateManager(), pos, rot, list, rand);
        this.components.addAll(list);
        this.updateBoundingBox();
    }

	public void setMobSpawner (World world, Random rand, BlockPos pos) {
		world.setBlockToAir(pos);
		world.setBlockState(pos, Blocks.MOB_SPAWNER.getDefaultState(), 3);
		this.setSpawner(world, rand, pos);
	}

	public void setSpawner (World world, Random rand, BlockPos pos) {

		TileEntity tile = world.getTileEntity(pos);
		int rnd = rand.nextInt(2);
		if (tile instanceof TileEntityMobSpawner) {

			MobSpawnerBaseLogic sp = ((TileEntityMobSpawner) tile).getSpawnerBaseLogic();

			switch (rnd) {
			case 0:
				sp.setEntityId(EntityList.getKey(EntitySkeleton.class));
				break;
			case 1:
				sp.setEntityId(EntityList.getKey(EntitySkullFrost.class));
				break;
			case 2:
				sp.setEntityId(EntityList.getKey(EntityPhantomZombie.class));
				break;
			case 3:
				sp.setEntityId(EntityList.getKey(EntityBlazeTempest.class));
				break;
			}
		}
	}

	public void setChest (World world, Random rand, BlockPos pos, List<ItemStack> list, int chance) {

		//宝箱の生成
		TileEntity chest = world.getTileEntity(pos);
		if (chest != null && chest instanceof TileEntityChest) {
			for (int l = 0; l < 27; l++) {
				int r = rand.nextInt(chance);
				if (r < list.size()) {
					ItemStack ret = list.get(r);
					((TileEntityChest) chest).setInventorySlotContents(l, ret);
				}
			}
		}
	}

	public void setLootTable (World world, Random rand, BlockPos pos, ResourceLocation src) {

		TileEntity tile = world.getTileEntity(pos);

		if (tile instanceof TileEntityChest) {
			((TileEntityChest) tile).setLootTable(src, rand.nextLong());
		}
	}

	public void setLootTable (World world, Random rand, BlockPos pos, ResourceLocation src, float chance) {

		if (world.isRemote) { return; }

		TileEntity tile = world.getTileEntity(pos);
		if (!(tile instanceof TileWoodChest)) { return; }

    	TileWoodChest chest = (TileWoodChest) tile;

    	for (int i = 0; i < chest.getInvSize(); i++) {

			if (rand.nextFloat() >= chance || !chest.getChestItem(i).isEmpty()) { continue; }

			// ルートテーブルをリストに入れて取り出してインベントリに入れる
			LootContext.Builder lootcontext = new LootContext.Builder((WorldServer) world);
			LootTable loot =  world.getLootTableManager().getLootTableFromLocation(src);

			List<ItemStack> items =loot.generateLootForPools(rand, lootcontext.build());
			if (items.isEmpty() || items.size() < 0) { continue; }

			ItemStack stack = items.get(rand.nextInt(items.size()));

			chest.chestInv.insertItem(i, stack, false);
    	}
	}

	public void setSMSpaner (World world, Random rand, BlockPos pos) {

		//宝箱の生成
		TileEntity tile = world.getTileEntity(pos);
		if (tile == null || !(tile instanceof TileSMSpaner)) { return; }

		TileSMSpaner spaner = (TileSMSpaner) tile;
		spaner.setSpaner();
	}


    public boolean isSizeableStructure() {
    	return true;
    }
}
