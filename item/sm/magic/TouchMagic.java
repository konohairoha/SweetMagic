package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class TouchMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;

	public TouchMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.MOB, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public TouchMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.MOB, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	/**
	 * 0 = ドレインタッチ
	 * 1 = マインドコントロール
	 */

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_absorphealth.name");
			break;
		case 1:
			toolTip.add("tip.magic_mind_control.name");
			break;
		}
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, EntityLivingBase entity, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;

		switch (this.data) {
		case 0:
			// 体力吸収魔法
			flag = this.absorpHealthAction(world, player, entity, stack, tags);
			break;
		case 1:
			// マインドコントロール
			flag = this.maindControlAction(world, player, entity, stack, tags);
			break;
		}

		return flag;
	}

	// 体力吸収魔法
	public boolean absorpHealthAction (World world, EntityPlayer player, EntityLivingBase entity, ItemStack stack, NBTTagCompound tags) {

		// プレイヤーの最大体力
		float maxAbsorp = player.getMaxHealth() * 0.5F;
		float entityHealth = entity.getMaxHealth() * 0.5F;

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		// 吸収する体力の計算
		float absorp = Math.min(entityHealth, Math.min(maxAbsorp, level));

		// 敵と自身の体力増減
		entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (absorp * 1F));
		player.heal(absorp);
		this.playSound(world, entity, SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 0.875F);
		FoodStats stats = player.getFoodStats();
		stats.setFoodLevel((int) (stats.getFoodLevel() * 0.75F));

		return true;
	}

	// マインドコントロール魔法
	public boolean maindControlAction (World world, EntityPlayer player, EntityLivingBase entity, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(entity, PotionInit.mind_control, this.effectTime(level), level);
		FoodStats stats = player.getFoodStats();
		stats.setFoodLevel((int) (stats.getFoodLevel() * 0.75F));
		this.playSound(world, entity, SoundEvents.ENTITY_SHULKER_TELEPORT, 1F, 0.875F);
		return true;
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		EntityLivingBase enttiy = IWand.getWand(stack).getTouchEntity();

		switch (this.data) {
		case 0:
			// 敵モブに触れたときのみ発動
			return enttiy instanceof IMob;
		case 1:
			// 敵モブに触れたときのみ発動
			return enttiy instanceof IMob && enttiy.isNonBoss();
		}

		return true;
	}
}
