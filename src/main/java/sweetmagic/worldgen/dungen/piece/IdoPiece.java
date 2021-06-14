package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class IdoPiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(IdoPiece.IdoTemplate.class, "IDO");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<IdoTemplate> list, Random random) {
        list.add(new IdoPiece.IdoTemplate(manager, pos.down(21), rot, "ido"));
        list.add(new IdoPiece.IdoTemplate(manager, pos.add(1, -35, 2), rot, "ido_under"));
        list.add(new IdoPiece.IdoTemplate(manager, pos.add(31, -22, 23), rot, "ido_miti"));
    }

    public static class IdoTemplate extends BaseStructureTemplate {

        public IdoTemplate() { }

        public IdoTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public IdoTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
