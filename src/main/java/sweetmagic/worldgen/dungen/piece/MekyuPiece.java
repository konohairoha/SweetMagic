package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class MekyuPiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(MekyuPiece.MekyuTemplate.class, "PYR");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<MekyuTemplate> list, Random random) {
        list.add(new MekyuPiece.MekyuTemplate(manager, pos.down(41), rot, "mekyu_main"));
        list.add(new MekyuPiece.MekyuTemplate(manager, pos.add(78, 0, 40), rot, "mekyu_tree"));
    }

    public static class MekyuTemplate extends BaseStructureTemplate {

        public MekyuTemplate() { }

        public MekyuTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public MekyuTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
