package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.util.PlayerHelper;

public class SMFood extends ItemFood {

	private Random rand = new Random();
	private final int data;

    public SMFood(int amount, float saturation ,String name, int meta) {
        super(amount, saturation, false);
        setUnlocalizedName(name);
        setRegistryName(name);
        setAlwaysEdible();
        this.data = meta;
        ItemInit.foodList.add(this);
    }

    /**
     *  0 = 効果無し
     *  1 = 食事早いやつ
     *  2 = 火炎耐性
     *  3 = HP + 2
     *  4 = デバフ解除
     *  5 = 体力増強
     *  6 = HP + 20%
     *  7 = 周囲に回復
     *  8 = 猛毒 + 毒 + 鈍足
     *  9 = ウィザー + 盲目 + 鈍足
     */

	//食べた際にポーション効果を付加
	protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
		switch (this.data) {
		case 0:
			break;
		// 食事速いやつ(1秒)
		case 1:
			break;
		// 火炎耐性
		case 2:
			player.addPotionEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 600, 0));
			break;
		case 3:
			player.heal(2F);
			break;
		case 4:
			player.clearActivePotions();
			break;
		// 火炎耐性
		case 5:
			player.addPotionEffect(new PotionEffect(MobEffects.HEALTH_BOOST, 3600, 2));
			break;
		case 6:
			player.heal(player.getMaxHealth() * 0.3F);
			break;
		case 7:
			AxisAlignedBB aabb =  player.getEntityBoundingBox().grow(7.5F, 7.5F, 7.5F);
			List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

			for (EntityLivingBase entity : entityList) {

				if (entity instanceof IMob) { continue; }

				entity.heal(entity.getMaxHealth() * 0.15F);
			}

			break;
		case 8:

			if (PlayerHelper.getLegItem(player) instanceof IPouch) {

				// インベントリを取得
				InventoryPouch neo = new InventoryPouch(player);
				List<ItemStack> stackList = neo.getStackList();

				// インベントリの分だけ回す
				for (ItemStack acce : stackList) {

					// エメラルドピアスを持ってるならダメージ増加
					if (acce.getItem() == ItemInit.veil_darkness) {
						player.addExperienceLevel(1);

						if (player instanceof EntityPlayerMP) {
							AdvancedInit.item_use.trigger((EntityPlayerMP) player, stack);
						}
						return;
					}
				}
			}

			player.removePotionEffect(PotionInit.refresh_effect);
			player.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, 300, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.POISON, 300, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 300, 0));
			break;
		case 9:

			if (PlayerHelper.getLegItem(player) instanceof IPouch) {

				// インベントリを取得
				InventoryPouch neo = new InventoryPouch(player);
				List<ItemStack> stackList = neo.getStackList();

				// インベントリの分だけ回す
				for (ItemStack acce : stackList) {

					// エメラルドピアスを持ってるならダメージ増加
					if (acce.getItem() == ItemInit.veil_darkness) {
						player.addExperienceLevel(1);

						if (player instanceof EntityPlayerMP) {
							AdvancedInit.item_use.trigger((EntityPlayerMP) player, stack);
						}
						return;
					}
				}
			}
			player.removePotionEffect(PotionInit.refresh_effect);
			player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 300, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 300, 0));
			player.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 300, 0));
			break;
		}
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
    	case 2:
    		tipname = "potion.effect.fire_resistance";
    		tipTime = "30";
    		break;
  		case 3:
  			tipname = "tip.heal.name";
  			break;
  		case 4:
    		tipname ="tip.food1.name";
    		break;
    	case 5:
    		tipname = "potion.effect.health_boost";
    		tipTime = "180";
    		break;
  		case 6:
  			tipname = "tip.heal_value.name";
  			break;
  		case 7:
  			tipname = "tip.renge_heal.name";
  			break;
  		case 8:
  			tipname = Keyboard.isKeyDown(42) ? "tip.witch_cake_but.name" : "tip.witch_cake.name";
  			break;
  		case 9:
  			tipname = Keyboard.isKeyDown(42) ? "tip.devil_cake_but.name" : "tip.devil_cake.name";
  			break;
  		}

  		if (!tipname.equals("")) {
  			String name = new TextComponentTranslation(tipname, new Object[0]).getFormattedText();
  			String tip = "";

  			switch (this.data) {
  			case 2:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + name + " (" + tipTime + "sec)";
  				break;
  			case 3:
  			case 6:
  			case 7:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + name;
  				break;
  			case 8:
  			case 9:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + TextFormatting.GOLD + name;
  				break;
  			case 5:
  				tip = new TextComponentTranslation("tip.food.name", new Object[0]).getFormattedText() + " " + name + " (" + tipTime + "sec)";
  				break;
  			}

  			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
  		}
  	}

	//食べ方を飲み物に
	public EnumAction getItemUseAction(ItemStack stack) {
    	return EnumAction.EAT;
    }

	public int getMaxItemUseDuration(ItemStack stack) {

		if (this.data == 1) {
			return 20;
		}

		else if (this.data == 6) {
			return 42;
		}

		else {
			return 32;
		}
    }
}
