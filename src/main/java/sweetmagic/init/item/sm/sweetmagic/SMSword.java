package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.config.SMConfig;
import sweetmagic.init.ItemInit;

public class SMSword extends ItemSword {

	public double speed;
	public final int data;
	public static final String DODROP = "doDrop";

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

		//盗賊のナイフの処理
		else if (SMConfig.help_knifedrop && this.data == 1 && target.getHealth() <= 0) {

			ItemStack st = ItemStack.EMPTY;
			float random = world.rand.nextFloat() - (float)(EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING,stack) * 0.02F);

			if(random < 0.05F) {

				//ランダムに救済アイテムを出現させる
				switch(world.rand.nextInt(3)) {
				case 0:
					st = new ItemStack(ItemInit.sannyflower_seed);
					break;
				case 1:
					st = new ItemStack(ItemInit.moonblossom_seed);
					break;
				case 2:
					st = new ItemStack(ItemInit.sugarbell_seed);
				}

				world.spawnEntity(new EntityItem(world, target.posX, target.posY, target.posZ, st));
			}
		}

		// 肉切り包丁
		else if (this.data == 2 && attacker instanceof EntityPlayer && !(target instanceof IMob) && !(target instanceof EntityPlayer)) {

			NBTTagCompound tags = target.getEntityData();

			// NBTを持っていたら
			if (!tags.hasKey(DODROP)) {
				tags.setBoolean(DODROP, true);
	    		target.onDeath(DamageSource.causePlayerDamage((EntityPlayer)attacker));
	    		target.isDead = false;
			}
		}

		// 彷徨う魂ドロップイベント
		if (target instanceof IMob && target.getHealth() <= 0) {

			Random rnd = new Random();
  	  		int pro = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);

  	  		// ドロ増を付けるとちょっと確率上がる
			if (rnd.nextInt(100) + 1 >= (90 - pro)) {
				EntityItem drop = new EntityItem(target.world, target.posX, target.posY, target.posZ,
						new ItemStack(ItemInit.stray_soul, target.world.rand.nextInt(2) + 1));
				drop.motionY += 0.15;
				target.world.spawnEntity(drop);
			}
		}
		return true;
	}

    protected void attackAOE(World world, ItemStack stack, EntityPlayer player, float damage, int emcCost) {

		// ダメージを与える範囲
		int charge = 2 + (EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack));
		float factor = 2.5F * charge;
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(factor);
		List<Entity> toAttack = world.getEntitiesWithinAABBExcludingEntity(player, aabb);
		DamageSource src = DamageSource.causePlayerDamage(player);
		src.setDamageBypassesArmor();

		for (Entity entity : toAttack) {
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

  	  		//xx_xx.langファイルから文字を取得する方法
  	  		String text =  new TextComponentTranslation(tip, new Object[0]).getFormattedText();
  			tooltip.add(I18n.format(TextFormatting.GREEN + text ));
  		}
  	}
}
