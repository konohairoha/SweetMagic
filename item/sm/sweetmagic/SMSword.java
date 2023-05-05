package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMSword extends ItemSword {

	private double speed;
	private final int data;
	private static final String DODROP = "doDrop";

	public SMSword(String name, int harvestLevel, int maxUses, float efficiency, float damage, int enchantability, Double atack, int data) {
		super(EnumHelper.addToolMaterial(name, harvestLevel, maxUses, efficiency, damage, enchantability));
		setUnlocalizedName(name);
		setRegistryName(name);
		this.speed = atack;
		this.data = data;
        ItemInit.itemList.add(this);
	}

	/**
	 * 0 = オルタナティブソード
	 * 1 = 盗賊のナイフ
	 * 2 = 肉切り包丁
	 */

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			map.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
			map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(),
					new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D + this.speed, 0));
		}
		return map;
	}

	//敵に攻撃したら周囲に範囲ダメージ
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		stack.damageItem(1, attacker);
		World world = target.world;

		if(this.data == 0 && !world.isRemote) {
			EntityPlayer player = (EntityPlayer) attacker;
			target.hurtResistantTime = 0;
	    	this.attackAOE(world, stack, player, 6 + EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack), 0);	//与えるダメージ
		}
		return true;
	}

    protected void attackAOE(World world, ItemStack stack, EntityPlayer player, float damage, int emcCost) {

		// ダメージを与える範囲
		int charge = 2 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack));
		float factor = 2.5F * charge;
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(factor);
		List<EntityLivingBase> toAttack = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		DamageSource src = DamageSource.causePlayerDamage(player);
		src.setDamageBypassesArmor();

		for (EntityLivingBase entity : toAttack) {
			if (entity instanceof IMob) {
				entity.attackEntityFrom(src, damage);
				entity.hurtResistantTime = 0;
			}
		}
    }


	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

  		String tip = "";

  		// オルタナティブソード
  		if (this.data == 0) {
  			tip = "tip.alt_sword.name";
  		}

  		// マチェット
  		else if (this.data == 2) {
  			tip = "tip.machete.name";
  		}

  		if (!tip.equals("")) {
  	  		String text = new TextComponentTranslation(tip, new Object[0]).getFormattedText();
  			tooltip.add(I18n.format(TextFormatting.GREEN + text ));
  		}
  	}
}
