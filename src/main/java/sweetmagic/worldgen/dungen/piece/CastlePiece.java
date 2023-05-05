package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class CastlePiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(CastlePiece.CastleTemplate.class, "CSTL");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<CastleTemplate> list, Random random) {
        list.add(new CastlePiece.CastleTemplate(manager, pos.up(), rot, "cas_main"));
        list.add(new CastlePiece.CastleTemplate(manager, pos.add(51, 1, 30), rot, "cas_sub"));
        list.add(new CastlePiece.CastleTemplate(manager, pos.add(13, 53, 28), rot, "cas_top"));
        list.add(new CastlePiece.CastleTemplate(manager, pos.add(8, 26, -1), rot, "cas_dai"));
        list.add(new CastlePiece.CastleTemplate(manager, pos.add(8, 26, 79), rot, "cas_dai"));
    }

    public static class CastleTemplate extends BaseStructureTemplate {

        public CastleTemplate() { }

        public CastleTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public CastleTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
