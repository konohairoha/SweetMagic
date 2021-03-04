package sweetmagic.worldgen.structures;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.SweetMagicCore;

public class WorldGenStructure extends WorldGenerator implements IStructure {

	public String structureName;

	public WorldGenStructure(String name) {
		this.structureName = name;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos position) {
		this.generateStructure(world, position);
		return true;
	}

	public void generateStructure(World world, BlockPos pos) {

		Template template = this.getTemplate(world, this.structureName);

		if (template != null) {
			IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
			template.addBlocksToWorldChunk(world, pos, settings);
		}
	}

	public Template getTemplate (World world, String name) {
		MinecraftServer mcServer = world.getMinecraftServer();
		TemplateManager manager = worldServer.getStructureTemplateManager();
		ResourceLocation location = new ResourceLocation(SweetMagicCore.MODID, name);
		return manager.get(mcServer, location);
	}
}