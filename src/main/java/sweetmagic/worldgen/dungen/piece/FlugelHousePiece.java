package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class FlugelHousePiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(FlugelHousePiece.FlugelHouseTemplate.class, "FH");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<FlugelHouseTemplate> list, Random random) {
        list.add(new FlugelHousePiece.FlugelHouseTemplate(manager, pos.add(33, 0, 0), rot, "vil_dai"));
        list.add(new FlugelHousePiece.FlugelHouseTemplate(manager, pos, rot, "flugel_house"));
    }

    public static class FlugelHouseTemplate extends BaseStructureTemplate {

        public FlugelHouseTemplate() { }

        public FlugelHouseTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public FlugelHouseTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
