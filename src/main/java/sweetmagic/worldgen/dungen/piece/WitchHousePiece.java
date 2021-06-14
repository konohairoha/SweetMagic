package sweetmagic.worldgen.dungen.piece;

import java.util.List;
import java.util.Random;

import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.init.base.BaseStructureTemplate;

public class WitchHousePiece {

    public static void registerIdoPiece() {
        MapGenStructureIO.registerStructureComponent(WitchHousePiece.WitchHouseTemplate.class, "WH");
    }

    public static void generateCore(TemplateManager manager, BlockPos pos, Rotation rot, List<WitchHouseTemplate> list, Random random) {
        list.add(new WitchHousePiece.WitchHouseTemplate(manager, pos, rot, "witchhouse_main"));
        list.add(new WitchHousePiece.WitchHouseTemplate(manager, pos.add(24, -8, 13), rot, "witchhouse_under"));
    }

    public static class WitchHouseTemplate extends BaseStructureTemplate {

        public WitchHouseTemplate() { }

        public WitchHouseTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
            this(manager, pos, rotation, Mirror.NONE, templateName);
        }

        public WitchHouseTemplate(TemplateManager manager, BlockPos pos, Rotation rot, Mirror mirror, String templateName) {
            super(manager, pos, rot, mirror, templateName);
            this.templatePosition = pos;
            this.rot = rot;
            this.mirror = mirror;
            this.templateName = templateName;
            this.loadTemplate(manager);
        }
    }
}
