package sweetmagic.init.item.sm.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
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
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.projectile.EntityMagicCycle;
import sweetmagic.init.entity.projectile.EntityMeteorMagic;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.WorldHelper;

public class AirMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;
	private SMElement subEle = null;

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

	public AirMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir, SMElement subEle) {
		super(name, SMType.AIR, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
		this.setSubElement(subEle);
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
	 * 13 = バフ延長魔法
	 * 14 = 敵スタン + 反撃魔法
	 * 15 = 持続回復 + バリア魔法
	 * 16 = 持続回復Ⅲ + バリア魔法
	 * 17 = 回避魔法1
	 * 18 = 回避魔法2
	 * 19 = 回避魔法3
	 * 20 = 持続デバフ解除 + 全回復魔法 + 衝撃吸収
	 * 21 =
	 * 22 = 透明化魔法
	 * 23 = 採掘速アップ魔法
	 * 24 = 拡散バリア魔法
	 * 25 = ディメンション・デバフ解除
	 * 26 = 因果律予測
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
		case 22:
			toolTip.add("tip.magic_invisible.name");
			break;
		case 23:
			toolTip.add("tip.magic_mining_enchant.name");
			break;
		case 24:
			toolTip.add("tip.magic_expand_barrier.name");
			break;
		case 25:
			toolTip.add("tip.magic_refresh_gift.name");
			break;
		case 26:
			toolTip.add("tip.magic_causality_prediction.name");
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
			// バフ延長魔法
			flag = this.deusOraMagic(world, player, stack, tags);
			break;
		case 14:
		case 26:
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
		case 22:
			// 透明化魔法
			flag = this.invisibleAction(world, player, stack, tags);
			break;
		case 23:
			// 採掘速度アップ魔法
			flag = this.miningAction(world, player, stack, tags);
			break;
		case 24:
			// 拡散バリア魔法
			flag = this.expandBarrierAction(world, player, stack, tags);
			break;
		case 25:
			flag = this.refreshGiftAction(world, player, stack, tags);
		}

		return flag;
	}

	// 範囲回復
	public boolean rangeHealAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
        double range = isRange ? 13.5D : 7D;

		world.playSound(null, player.getPosition(), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1);
		WorldHelper.PlayerHeal(world, player.getEntityBoundingBox().grow(range), player, stack, false);
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

		// 最大体力 * ( 基礎回復量 + 補正値 * ( 杖レベル - 1 ) )
		float healValue = player.getMaxHealth() * (0.25F + 0.01F * (level - 1));
		player.setHealth(player.getHealth() + healValue);

		// デバフ解除
		player.removePotionEffect(MobEffects.HUNGER);
		player.removePotionEffect(MobEffects.WEAKNESS);
		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);

		return true;
	}

	// バリア魔法
	public boolean barrierAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		System.out.println("=======level" + level);
		System.out.println("=======Time" + this.effectTime(level));

		this.addPotion(player, PotionInit.aether_barrier, this.effectTime(level), --level, true);
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

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = (int) (wand.isCreativeWand() ? wand.getCreativePower() : wand.getLevel(stack));
		level += isRange ? 12 : 2;

		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);
		WorldHelper.effectRemoveRange(world, player.getEntityBoundingBox().grow(level));

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
        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) + (isRange ? 12 : 2);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(level);
		WorldHelper.PlayerHeal(world, aabb, player, stack, true);
		int time = (int) (this.effectTime(level) * (isRange ? 1.33F : 1));

		WorldHelper.addEffect(world, aabb, PotionInit.refresh_effect, (int) (time * 1.25F), 1, EnumParticleTypes.WATER_SPLASH);
		this.playSound(world, player, SMSoundEvent.HEAL, 0.175F, 1F);

		if (this.data == 20) {
			this.addPotion(player, MobEffects.ABSORPTION, (int) (time * 1.25F), 2, true);
			WorldHelper.addEffect(world, aabb, MobEffects.ABSORPTION, time, 2, null);
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
        vec3d = vec3d.normalize().scale(10);
        int y = 16;
        BlockPos pos = new BlockPos(player.posX + vec3d.x, player.posY + y, player.posZ + vec3d.z);

		while(true) {

			if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR || pos.getY() <= 1) {
				y--;
				break;
			}

			pos = pos.down();
			y--;
		}

		if (!world.isRemote) {
	        EntityMeteorMagic entity = new EntityMeteorMagic(world, player, stack);
			entity.setDamage(entity.getDamage() + this.getPower(level) * 3);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 0, 0);									// 射撃速度
			entity.motionY -= 2D;

			entity.setPosition(player.posX + vec3d.x, player.posY + y, player.posZ + vec3d.z);
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
        vec3d = vec3d.normalize().scale(10);
        int y = 11;
        BlockPos pos = new BlockPos(player.posX + vec3d.x, player.posY + y, player.posZ + vec3d.z);

		while(true) {

			if (world.getBlockState(pos.down()).getBlock() == Blocks.AIR || pos.getY() <= 1) {
				break;
			}

			pos = pos.down();
			y--;
		}

		if (!world.isRemote) {
			float scale = level * 0.5F;
			EntityMagicCycle entity = new EntityMagicCycle(player, 103, 42, 35, scale, level, 200, 8);
			entity.setPosition(player.posX + vec3d.x, player.posY + y, player.posZ + vec3d.z);
	        world.spawnEntity(entity);
		}
		return true;
	}

	// ジャンプ力アップバフ魔法
	public boolean jumpBoost (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, MobEffects.JUMP_BOOST, this.effectTime(level), 1, true);
		player.addPotionEffect(new PotionEffect(MobEffects.JUMP_BOOST, this.effectTime(level), 1, true, false));
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 弾ベクトル無効化魔法
	public boolean vectorHaltenMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.gravity_accele, this.effectTime(level), 0, true);
		this.playSound(world, player, SMSoundEvent.GRAVITY, 1.5F, 1F);

		return true;
	}

	// 敵スタン魔法
	public boolean deusOraMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		boolean isCreativeWand = wand.isCreativeWand();
		int mf = wand.getMF(stack);
		int useMF = 0;

		List<PotionEffect> pList = new ArrayList<>();
		pList.addAll(player.getActivePotionEffects());
		if (pList.isEmpty()) { return false; }

		try {

			for (PotionEffect effect : pList) {

				Potion potion = effect.getPotion();

				// バフなら時間延長
				if (!potion.isBadEffect()) {

					int pLevel = effect.getAmplifier();
					int pTime = (int) ( effect.getDuration() * (1 + Math.min(0.5F, level * 0.025F)) );
					if (!isCreativeWand && mf < useMF + pTime) { break; }

					useMF += pTime;
					player.removePotionEffect(potion);
					PlayerHelper.addPotion(player, potion, pTime, pLevel, false);
				}
			}

			wand.setMF(stack, mf - useMF);
			this.playSound(world, player, SMSoundEvent.REVERTIME, 1F, 1F);
		}

		catch (Throwable e) { }

		return true;
	}

	// 敵スタン魔法
	public boolean futureVisionMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		this.addPotion(player, PotionInit.timestop, this.effectTime(level), this.data == 26 ? 1 : 0, true);
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

		this.addPotion(player, PotionInit.aether_barrier, time, --level, true);
		this.addPotion(player, PotionInit.regene, this.effectTime(level), potionLevel, true);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		player.getActivePotionEffects();

		return true;
	}

	// 回避魔法
	public boolean rangeAvoid (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
        double range = isRange ? 15D : 7D;

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

		List<EntityPlayer> playerList = this.getEntityList(EntityPlayer.class, player, range, range, range);

		for (EntityPlayer p : playerList) {
			this.addPotion(p, PotionInit.cyclone, this.effectTime(level), potionLevel, true);
			this.playSound(world, player, SMSoundEvent.CYCLON, 0.5F, 1F);
		}

		return true;
	}

	// 透明化魔法
	public boolean invisibleAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		List<EntityLiving> entityList = this.getEntityList(EntityLiving.class, player, 64D, 64D, 64D);

		for (EntityLiving entity : entityList) {

			if (player != entity.getAttackTarget() || !entity.isNonBoss()) { continue; }

			SMUtil.tameAIAnger(entity, null);
		}

		this.addPotion(player, MobEffects.INVISIBILITY, this.effectTime(level), 0, true);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 採掘速度アップ魔法
	public boolean miningAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		this.addPotion(player, MobEffects.HASTE, this.effectTime(level), 2, true);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);

		return true;
	}

	// 拡散バリア魔法
	public boolean expandBarrierAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
        double range = isRange ? 15D : 7.5D;

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		int time = (int) (this.effectTime(level) * (isRange ? 1F : 0.67F));

		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, player, range, range, range);

		for (EntityLivingBase entity : entityList) {

			if (entity instanceof IMob) { continue; }

			this.addPotion(entity, PotionInit.aether_barrier, time, --level, true);
			this.addPotion(entity, PotionInit.regene, time, 2, true);
			this.playSound(world, entity, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		}

		return true;
	}

	// 持続デバフ解除魔法
	public boolean refreshGiftAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		level += 2;

		// ワールド内のプレイヤー取得
		List<EntityPlayer> playerList = world.playerEntities;

		for (EntityPlayer entity : playerList) {
			this.addPotion(entity, PotionInit.refresh_effect, (int) (this.effectTime(level) * 1.5), 0, true);
			ParticleHelper.spawnHeal(entity, EnumParticleTypes.WATER_SPLASH, 16, 1, 4);
		}

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(level, level, level);
		List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

		for (EntityLivingBase entity : entityList) {

			if (!(entity instanceof EntityZombie) && !entity.getName().equals("lich")) { continue; }

			entity.heal(level + 3);
			entity.addPotionEffect(new PotionEffect(MobEffects.INSTANT_HEALTH, level * 2, 0, true, false));
			world.playSound(null, entity.getPosition(), SMSoundEvent.HEAL, SoundCategory.NEUTRAL, 0.175F, 1);
		}


		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		return true;
	}

	// 魔法が使えるかチェック
	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return true;
	}


	// サブ属性の取得
	@Override
	public SMElement getSubElement () {
		return this.subEle;
	}

	// サブ属性の設定
	@Override
	public void setSubElement (SMElement ele) {
		this.subEle = ele;
	}
}
