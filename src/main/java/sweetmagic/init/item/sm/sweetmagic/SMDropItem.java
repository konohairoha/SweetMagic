package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMDropItem extends SMItem {

	private final String name;

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
			tipEntity = "entity.endershadow.name";
  			break;
  		case "mysterious_page":
			tipEntity = "entity.Witch.name";
  			break;
  		case "electronic_orb":
			tipEntity = "entity.electriccube.name";
  			break;
  		case "poison_bottle":
			tipEntity = "entity.archspider.name";
  			break;
  		case "unmeltable_ice":
			tipEntity = "entity.skullfrost.name";
  			break;
  		case "grav_powder":
			tipEntity = "entity.creepercalamity.name";
  			break;
  		case "tiny_feather":
			tipEntity = "entity.blazetempest.name";
  			break;
  		case "witch_tears":
			tipEntity = "entity.witchmadameverre.name";
  			break;
  		}

  		if (!tipEntity.equals("")) {
  	  		String tip = this.getTip("tip.drop.name") + " " + this.getTip(tipEntity);
  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}
}
