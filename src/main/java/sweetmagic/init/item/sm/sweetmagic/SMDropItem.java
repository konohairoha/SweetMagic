package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMDropItem extends SMItem {

	public final String name;

	public SMDropItem (String name) {
		super(name, ItemInit.magicList);
		this.name = name;
	}

	//ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

		String tipEntity = "";

  		switch (this.name) {
  		case "stray_soul":
			tipEntity = new TextComponentTranslation("entity.endershadow.name", new Object[0]).getFormattedText();
  			break;
  		case "mysterious_page":
			tipEntity = new TextComponentTranslation("entity.Witch.name", new Object[0]).getFormattedText();
  			break;
  		case "electronic_orb":
			tipEntity = new TextComponentTranslation("entity.electriccube.name", new Object[0]).getFormattedText();
  			break;
  		case "poison_bottle":
			tipEntity = new TextComponentTranslation("entity.archspider.name", new Object[0]).getFormattedText();
  			break;
  		case "unmeltable_ice":
			tipEntity = new TextComponentTranslation("entity.skullfrost.name", new Object[0]).getFormattedText();
  			break;
  		case "grav_powder":
			tipEntity = new TextComponentTranslation("entity.creepercalamity.name", new Object[0]).getFormattedText();
  			break;
  		case "tiny_feather":
			tipEntity = new TextComponentTranslation("entity.blazetempest.name", new Object[0]).getFormattedText();
  			break;
  		case "witch_tears":
			tipEntity = new TextComponentTranslation("entity.witchmadameverre.name", new Object[0]).getFormattedText();
  			break;
  		}

  		if (!tipEntity.equals("")) {
  	  		String tip = new TextComponentTranslation("tip.drop.name", new Object[0]).getFormattedText() + " " + tipEntity;
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}

}
