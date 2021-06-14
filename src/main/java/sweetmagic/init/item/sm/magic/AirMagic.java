package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityFrostRain;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.SMUtil;
import sweetmagic.util.WorldHelper;

public class AirMagic extends MFSlotItem {

	public final int data;
	ResourceLocation icon;

	public AirMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.AIR, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public AirMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.AIR, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	/**
	 * 0 = 範囲回復魔法
	 * 1 = 影魔法
	 * 2 = 回復魔法
	 * 3 = バリア魔法
	 * 4 = 毒付与魔法
	 * 5 = デバフ除去魔法
	 * 6 = 反撃アーマー魔法
	 * 7 = 持続デバフ解除魔法
	 * 8 = 持続デバフ解除+全回復魔法
	 * 9 = 隕石落下魔法
	 * 10 = 氷雨魔法
	 * 11 = ジャンプ力アップ
	 * 12 = 弾のベクトル無効化
	 * 13 = 敵スタン魔法
	 * 14 = 敵スタン + 反撃魔法
	 * 15 = 持続回復 + バリア魔法
	 * 16 = 持続回復Ⅲ + バリア魔法
	 * 17 = 回避魔法1
	 * 18 = 回避魔法2
	 * 19 = 回避魔法3
	 * 20 = 持続デバフ解除 + 全回復魔法 + 衝撃吸収
	 * 21 = レベルアップ用
	 * 22 = 透明化魔法
	 */

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_randheal.name");
			break;
		case 1:
			toolTip.add("tip.magic_shaodw.name");
			break;
		case 2:
			toolTip.add("tip.magic_heal.name");
			break;
		case 3:
			toolTip.add("tip.magic_barrier.name");
			break;
		case 4:
			toolTip.add("tip.magic_poison.name");
			break;
		case 5:
			toolTip.add("tip.magic_effectremover.name");
			break;
		case 6:
			toolTip.add("tip.magic_elecarmor.name");
			break;
		case 7:
			toolTip.add("tip.magic_refresh.name");
			break;
		case 8:
			toolTip.add("tip.magic_healingwish.name");
			break;
		case 9:
			toolTip.add("tip.magic_meteor_fall.name");
			break;
		case 10:
			toolTip.add("tip.magic_frostrain.name");
			break;
		case 11:
			toolTip.add("tip.magic_vector_boost.name");
			break;
		case 12:
			toolTip.add("tip.magic_vector_halten.name");
			break;
		case 13:
			toolTip.add("tip.magic_deusora.name");
			break;
		case 14:
			toolTip.add("tip.magic_futurevision.name");
			break;
		case 15:
			toolTip.add("tip.magic_regene_shield.name");
			break;
		case 16:
			toolTip.add("tip.magic_magia_protection.name");
			break;
		case 17:
		case 18:
		case 19:
			toolTip.add("tip.magic_avoid.name");
			break;
		case 20:
			toolTip.add("tip.magic_healing_hightlow.name");
			break;
		case 21:
			toolTip.add("tip.magic_creative.name");
			break;
		case 22:
			toolTip.add("tip.magic_invisible.name");
			break;
		}

		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;

		switch (this.data) {
		case 0:
			// 範囲回復魔法
			flag = this.rangeHealAction(world, player, stack, tags);
			break;
		case 1:
			// 影魔法
			flag = this.shadowAction(world, player, stack, tags);
			break;
		case 2:
			// 回復魔法
			flag = this.healAction(world, player, stack, tags);
			break;
		case 3:
			// バリア魔法
			flag = this.barrierAction(world, player, stack, tags);
			break;
		case 4:
			// 毒魔法
			flag = this.poisonAction(world, player, stack, tags);
			break;
		case 5:
			// デバフ除去魔法
			flag = this.effectRemoverAction(world, player, stack, tags);
			break;
		case 6:
			// 反撃アーマー魔法
			flag = this.addElecArmor(world, player, stack, tags);
			break;
		case 7:
			// 持続デバフ解除魔法
			flag = this.refreshAction(world, player, stack, tags);
			break;
		case 8:
		case 20:
			// 持続デバフ解除+全回復魔法
			flag = this.healingWish(world, player, stack, tags);
			break;
		case 9:
			// 隕石落下魔法
			flag = this.meteorFall(world, player, stack, tags);
			break;
		case 10:
			// 氷雨魔法
			flag = this.frostRain(world, player, stack, tags);
			break;
		case 11:
			// ジャンプ力アップバフ魔法
			flag = this.jumpBoost(world, player, stack, tags);
			break;
		case 12:
			// 弾ベクトル無効化魔法
			flag = this.vectorHaltenMagic(world, player, stack, tags);
			break;
		case 13:
			// 敵スタン
			flag = this.deusOraMagic(world, player, stack, tags);
			break;
		case 14:
			// 敵スタン + 反撃魔法
			flag = this.futureVisionMagic(world, player, stack, tags);
			break;
		case 15:
		case 16:
			// 持続回復Ⅲ + バリア魔法
			flag = this.regeneBarrierAction(world, player, stack, tags);
			break;
		case 17:
		case 18:
		case 19:
			// 回避魔法
			flag = this.rangeAvoid(world, player, stack, tags);
			break;
		case 21:
			// レベルアップ魔法
			flag = this.creativeAction(world, player, stack, tags);
			break;
		case 22:
			// 透明化魔法
			flag = this.invisibleAction(world, player, stack, tags);
		}

		return flag;
	}

	// 範囲回復
	public boolean rangeHealAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		world.playSound(null, new BlockPos(player), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1);
		WorldHelper.PlayerHeal(world, player.getEntityBoundingBox().grow(7D, 5D, 7D), player, stack, false);
		return false;
	}

	// 影魔法
	public boolean shadowAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		// 幻影を与える
		this.addPotion(player, PotionInit.shadow, this.effectTime(level), level, true);
		this.playSound(world, player, SMSoundEvent.MAGICSTART, 0.75F, 1F);

		long worldTime = world.getWorldTime() % 24000;
		boolean isNight = worldTime >= 12000;
		player.setHealth(isNight ? player.getHealth() * 0.75F : player.getHealth() * 0.5F);
		FoodStats stats = player.getFoodStats();
		stats.setFoodLevel((int) (isNight ? stats.getFoodLevel() * 0.75F : stats.getFoodLevel() * 0.5F));

		return true;
	}

	// 回復魔法
	public boolean healAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		// switch文で回復量を変える予定
		player.setHealth(player.getHealth() + level + 3);

		// デバフ解除
		player.removePotionEffect(MobEffects.HUNGER);
		player.removePotionEffect(MobEffects.WEAKNESS);
		world.playSound(null, new BlockPos(player), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1F);
		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);

		return true;
	}

	// バリア魔法
	public boolean barrierAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		this.addPotion(player, PotionInit.aether_barrier, this.effectTime(level), --level, false);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		return true;
	}

	// 毒付与魔法
	public boolean poisonAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.grant_poison, this.effectTime(level), 0, true);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		return true;
	}

	// デバフ除去魔法
	public boolean effectRemoverAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = (int) (wand.isCreativeWand() ? wand.getCreativePower() : wand.getLevel(stack));
		level += 2;

		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);
		WorldHelper.effectRemoveRange(world, player.getEntityBoundingBox().grow(level, level, level));

		return true;
	}

	// 反撃アーマー魔法
	public boolean addElecArmor (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.electric_armor, this.effectTime(level), --level, true);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		return true;
	}

	// 持続デバフ解除魔法
	public boolean refreshAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		level += 2;

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(level, level, level);
		WorldHelper.addEffect(world, aabb, PotionInit.refresh_effect, (int) (this.effectTime(level) * 1.25), 0, EnumParticleTypes.WATER_SPLASH);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		return true;
	}

	// 持続デバフ解除+全回復魔法
	public boolean healingWish (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		level += 2;

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(level, level, level);
		WorldHelper.PlayerHeal(world, aabb, player, stack, true);
		WorldHelper.addEffect(world, aabb, PotionInit.refresh_effect, (int) (this.effectTime(level) * 1.25), 1, EnumParticleTypes.WATER_SPLASH);
		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);

		if (this.data == 20) {
			this.addPotion(player, MobEffects.ABSORPTION, (int) (this.effectTime(level) * 1.25), 2, true);
			WorldHelper.addEffect(world, aabb, MobEffects.ABSORPTION, this.effectTime(level), 2, null);
		}

		return true;
	}

	// 隕石落下魔法
	public boolean meteorFall (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		// 向き取得
        Vec3d vec3d = player.getLookVec();
        vec3d = vec3d.normalize().scale(17);

		if (!world.isRemote) {
	        EntityMeteorMagic entity = new EntityMeteorMagic(world, player, stack);
			entity.setDamage(entity.getDamage() + this.getPower(level) * 3);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 0, 0);									// 射撃速度
			entity.motionY -= 1.5;
	        BlockPos pos = new BlockPos(player.posX + vec3d.x, player.posY + vec3d.y, player.posZ + vec3d.z);
			entity.setPosition(pos.getX(), pos.getY() + 15, pos.getZ());
			entity.plusTick = -400;
	        world.spawnEntity(entity);
		}
		return true;
	}

	// 氷雨魔法
	public boolean frostRain (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		// 向き取得
        Vec3d vec3d = player.getLookVec();
        vec3d = vec3d.normalize().scale(17);

		if (!world.isRemote) {
			EntityFrostRain entity = new EntityFrostRain(world, player, stack);
			entity.setDamage(entity.getDamage() + this.getPower(level) * 2);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(0, entity.motionY, 0, 0, 0);									// 射撃速度
			entity.motionY -= 16;
			entity.isHitDead = true;
	        BlockPos pos = new BlockPos(player.posX + vec3d.x, player.posY + vec3d.y + 10, player.posZ + vec3d.z);
			entity.setPosition(pos.getX(), pos.getY() + 10, pos.getZ());
	        world.spawnEntity(entity);
		}
		return true;
	}

	// ジャンプ力アップバフ魔法
	public boolean jumpBoost (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, MobEffects.JUMP_BOOST, this.effectTime(level), 1, false);
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, this.effectTime(level), 1, true, false));
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 弾ベクトル無効化魔法
	public boolean vectorHaltenMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.gravity_accele, this.effectTime(level), 0, false);
		this.playSound(world, player, SMSoundEvent.GRAVITY, 1.5F, 1F);

		return true;
	}

	// 敵スタン魔法
	public boolean deusOraMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.timestop, this.effectTime(level), 0, false);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 敵スタン魔法
	public boolean futureVisionMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.timestop, this.effectTime(level), 1, false);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 持続回復 + バリア魔法
	public boolean regeneBarrierAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		int time = this.data == 16 ? (int) (this.effectTime(level) * 1.25) : this.effectTime(level);
		int potionLevel = this.data == 16 ? 3 : 1;

		this.addPotion(player, PotionInit.aether_barrier, time, --level, false);
		this.addPotion(player, PotionInit.regene, this.effectTime(level), potionLevel, false);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		player.getActivePotionEffects();

		return true;
	}

	// 回避魔法
	public boolean rangeAvoid (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		int potionLevel = 0;

		switch (this.data) {
		case 17:
			potionLevel = 0;
			break;
		case 18:
			potionLevel = 1;
			break;
		case 19:
			potionLevel = 2;
			break;
		}

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(7D, 7D, 7D);
		List<EntityPlayer> playerList = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);

		for (EntityPlayer p : playerList) {
			this.addPotion(p, PotionInit.cyclone, this.effectTime(level), potionLevel, false);
			this.playSound(world, player, SMSoundEvent.CYCLON, 0.5F, 1F);
		}

		return true;
	}

	// レベルアップ魔法
	public boolean creativeAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) + 1;
		int needExp = wand.needExp(wand.getMaxLevel(), level, stack);

		tags.setInteger(wand.LEVEL, level);
		tags.setInteger(wand.EXP, needExp);
		this.playSound(world, player, SMSoundEvent.LEVELUP, 0.2F, 1F);

		return true;
	}

	// 透明化魔法
	public boolean invisibleAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(64D, 64D, 64D);
		List<EntityLiving> entityList = world.getEntitiesWithinAABB(EntityLiving.class, aabb);

		for (EntityLiving entity : entityList) {

			if (player != entity.getAttackTarget() || !entity.isNonBoss()) { continue; }

			SMUtil.tameAIAnger(entity, null);
		}

		this.addPotion(player, MobEffects.INVISIBILITY, this.effectTime(level), 0, false);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 魔法が使えるかチェック
	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		switch (this.data) {
		case 9:
	        // 空が見えるなら打てる
	        return world.canSeeSky(new BlockPos(player));
		case 21:
			// 杖の取得
			IWand wand = IWand.getWand(stack);
			return wand.getLevel(stack) < wand.getMaxLevel();
		}
		return true;
	}
}
