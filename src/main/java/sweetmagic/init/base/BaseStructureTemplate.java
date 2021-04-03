package sweetmagic.init.base;

import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponentTemplate;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.gen.structure.template.TemplateManager;
import sweetmagic.SweetMagicCore;

public class BaseStructureTemplate extends StructureComponentTemplate {

	public Rotation rot;
	public Mirror mirror;
	public String templateName;

    public BaseStructureTemplate() { }

    public BaseStructureTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, String templateName) {
        this(manager, pos, rotation, Mirror.NONE, templateName);
    }

    public BaseStructureTemplate(TemplateManager manager, BlockPos pos, Rotation rotation, Mirror mirror, String templateName) {
        super(0);
        this.templatePosition = pos;
        this.rot = rotation;
        this.mirror = mirror;
        this.templateName = templateName;
        this.loadTemplate(manager);
    }

    public void loadTemplate(TemplateManager manager) {
        Template template = manager.getTemplate(null, new ResourceLocation(SweetMagicCore.MODID, this.templateName));
        PlacementSettings set = (new PlacementSettings()).setIgnoreEntities(true).setRotation(this.rot).setMirror(this.mirror);
        this.setup(template, this.templatePosition, set);
    }

    @Override
    public boolean addComponentParts(World world, Random random, StructureBoundingBox box) {
        return super.addComponentParts(world, random, box);
    }


    @Override
    protected void handleDataMarker(String function, BlockPos pos, World world, Random rand, StructureBoundingBox box) {
    }

    @Override
    public void writeStructureToNBT(NBTTagCompound tags) {
        super.writeStructureToNBT(tags);
        tags.setString("Template", this.templateName);
        tags.setString("Rot", this.placeSettings.getRotation().name());
        tags.setString("Mi", this.placeSettings.getMirror().name());
    }

    @Override
    public void readStructureFromNBT(NBTTagCompound tags, TemplateManager manager) {
        super.readStructureFromNBT(tags, manager);
        this.templateName = tags.getString("Template");
        this.rot = Rotation.valueOf(tags.getString("Rot"));
        this.mirror = Mirror.valueOf(tags.getString("Mi"));
        this.loadTemplate(manager);
    }
}
