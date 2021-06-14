package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class PyramidPiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(PyramidPiece.PyramidTemplate.class, "MQ");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<PyramidTemplate> list, Random random) {
        list.add(new PyramidPiece.PyramidTemplate(manager, pos.down(36), rot, "pyramid_main"));
        list.add(new PyramidPiece.PyramidTemplate(manager, pos.add(17, -36, 70), rot, "pyramid_sub"));
        list.add(new PyramidPiece.PyramidTemplate(manager, pos.add(24, 0, 12), rot, "pyramid_top"));
    }

    public static class PyramidTemplate extends BaseStructureTemplate {

        public PyramidTemplate() { }

        public PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public PyramidTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
