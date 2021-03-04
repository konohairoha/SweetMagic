package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class DekaijyuPiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(DekaijyuPiece.DekaijyuTemplate.class, "DKJ");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<DekaijyuTemplate> list, Random rand) {

        list.add(new DekaijyuPiece.DekaijyuTemplate(manager, pos, rot, "dekaijyu"));

    }

    public static class DekaijyuTemplate extends BaseStructureTemplate {

        public DekaijyuTemplate() { }

        public DekaijyuTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public DekaijyuTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
