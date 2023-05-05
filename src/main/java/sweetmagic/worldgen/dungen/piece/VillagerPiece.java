package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class VillagerPiece {

	private static final int A = 4;

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(VillagerPiece.VillagerTemplate.class, "VIL");
    }

    public static void generateCore(TemplateManager manager, World world, BlockPos pos, Rotation rot, List<VillagerTemplate> list, Random rand, List<BlockPos> posList) {

        list.add(new VillagerPiece.VillagerTemplate(manager, pos, Rotation.NONE, "vil_dai"));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(17, 0, 30), Rotation.NONE, "vil_well"));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(14, 0, -11), Rotation.NONE, "vil_tower"));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(0, 0, 16), Rotation.NONE, getHouse(world)));
        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(23, 0, 16), Rotation.NONE, getHouse(world)));
        posList.add(pos.add(0 + A, 0 + 21, 16 + A));
        posList.add(pos.add(23 + A, 0 + 21, 16 + A));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(3, 0, 52), Rotation.COUNTERCLOCKWISE_90, getHouse(world)));
        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(36, 0, 36), Rotation.CLOCKWISE_90, getHouse(world)));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(3, 0, 72), Rotation.COUNTERCLOCKWISE_90, getHouse(world)));
        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(36, 0, 56), Rotation.CLOCKWISE_90, getHouse(world)));

        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(6, 0, 13), Rotation.COUNTERCLOCKWISE_90, getField(world)));
        list.add(new VillagerPiece.VillagerTemplate(manager, pos.add(24, 0, 13), Rotation.COUNTERCLOCKWISE_90, getField(world)));
    }

    public static String getHouse (World world) {

    	String structure = "";

    	switch (world.rand.nextInt(9)) {
    	case 0:
    		structure = "vil_books";
    		break;
    	case 1:
    		structure = "vil_house_blue";
    		break;
    	case 2:
    		structure = "vil_house_brown";
    		break;
    	case 3:
    		structure = "vil_house_purple";
    		break;
    	case 4:
    		structure = "vil_house_red";
    		break;
    	case 5:
    		structure = "vil_house_yellow";
    		break;
    	case 6:
    		structure = "vil_loghouse";
    		break;
    	case 7:
    		structure = "vil_smith";
    		break;
    	case 8:
    		structure = "vil_bread";
    		break;
    	}

    	return structure;
    }

    public static String getField (World world) {

    	String structure = "";

    	switch (world.rand.nextInt(2)) {
    	case 0:
    		structure = "vil_field_1";
    		break;
    	case 1:
    		structure = "vil_field_2";
    		break;
    	}

    	return structure;
    }

    public static class VillagerTemplate extends BaseStructureTemplate {

        public VillagerTemplate() { }

        public VillagerTemplate(TemplateManager manager, BlockPos pos, Rotation rot, String templateName) {
            this(manager, getPos(pos, rot, templateName), rot, Mirror.NONE, templateName);
        }

        public static BlockPos getPos (BlockPos pos, Rotation rot, String templateName) {

        	if (!templateName.equals("vil_bread")) {
        		return pos;
        	}

        	switch (rot) {
        	case NONE:
            	return pos.north(1);
        	case CLOCKWISE_90:
            	return pos.east(2);
        	case COUNTERCLOCKWISE_90:
            	return pos.west(2);
        	}

        	return pos;
        }

        public VillagerTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
