package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;

public class SMDrink extends ItemFood {

	private final int data;

    public SMDrink(int amount, float saturation ,String name, int meta) {
        super(amount, saturation, false);
        setUnlocalizedName(name);
        setRegistryName(name);
        setAlwaysEdible();
        this.data = meta;
        ItemInit.foodList.add(this);
    }

    /**
     * 0 = 効果なし
     * 1 = デバフ解除
     * 2 = MF消費ダウン
     */

	//食べた際にポーション効果を付加
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {

		switch (this.data) {
		case 0:
			break;
		case 1:
			// ポーション効果を消す
			player.clearActivePotions();
			break;
		case 2:
			// MF消費ダウン
			player.addPotionEffect(new PotionEffect(PotionInit.mf_down, 1200, 0));
			break;
		}
	}

    // 食べる時間
	public int getMaxItemUseDuration(ItemStack stack) {
		return 24;
	}

	// アクションの仕方
    public EnumAction getItemUseAction(ItemStack stack){
		return EnumAction.DRINK;
    }

    //ツールチップの表示
  	@Override
  	@SideOnly(Side.CLIENT)
  	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

  		//xx_xx.langファイルから文字を取得する方法
		String tipname = "";
		String tipTime = "";

		switch (this.data) {
    	case 0:
    		break;
    	case 1:
    		tipname = "tip.food1.name";
    		break;
    	case 2:
    		tipname = "sweetmagic.effect.mf_down";
    		tipTime = "60";
  		}

		if (!tipname.equals("")) {

  			String name = new TextComponentTranslation(tipname, new Object[0]).getFormattedText();
  			String tip = "";

  			switch (this.data) {
  			case 1:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + name;
  				break;
  			case 2:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + name + " (" + tipTime + "sec)";
  				break;
  			}

  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
		}
  	}
}
