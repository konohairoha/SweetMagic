package sweetmagic.worldgen.gen;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.SweetMagicCore;
import sweetmagic.worldgen.structures.IStructure;

public class WorldGenHouse extends WorldGenerator implements IStructure {

	public String structureName;
	public EnumFacing face;

	public WorldGenHouse(String name, EnumFacing face) {
		this.structureName = name;
		this.face = face;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		return this.generateStructure(world, pos);
	}

	public boolean generateStructure(World world, BlockPos p1) {

        int x = 0;
        int z = 0;
        BlockPos pos = p1;
        PlacementSettings place =  new PlacementSettings();

        // 位置調整
        // 北（標準）
        switch (this.face) {
        case NORTH:
        	place.setRotation(Rotation.CLOCKWISE_180);
            pos = p1.south(x).east(z);
        	break;
        case SOUTH:
        	place.setRotation(Rotation.NONE);
            pos = p1.south(x).east(z);
        	break;
        case EAST:
        	place.setRotation(Rotation.COUNTERCLOCKWISE_90);
            pos = p1.north(z).east(x);
        	break;
        case WEST:
        	place.setRotation(Rotation.CLOCKWISE_90);
            pos = p1.south(z).west(x);
        	break;
       	default:
        }

		MinecraftServer mcServer = world.getMinecraftServer();
		TemplateManager manager = mcServer.getWorld(0).getStructureTemplateManager();
		ResourceLocation location = new ResourceLocation(SweetMagicCore.MODID, this.structureName);
		Template template = manager.get(mcServer, location);

		if (template != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			template.addBlocksToWorldChunk(world, pos, place);
			return true;
		}

		return false;
	}
}
