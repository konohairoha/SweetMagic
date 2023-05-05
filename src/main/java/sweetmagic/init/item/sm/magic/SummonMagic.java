package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityBlackCat;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowHorse;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.entity.monster.EntityTamedKit;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class SummonMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;
    protected static final IAttribute JUMP_STRENGTH = (new RangedAttribute((IAttribute)null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);

	public SummonMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.SUMMON, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public SummonMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.SUMMON, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	/**
	 * 0 = 幻影オオカミ召喚
	 * 1 = 幻影ゴーレム召喚
	 * 2 = 幻影馬召喚
	 * 3 = ねこ召喚
	 */

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_shadowwolf.name");
			break;
		case 1:
			toolTip.add("tip.magic_shadowgolem.name");
			break;
		case 2:
			toolTip.add("tip.magic_shadowhorse.name");
			break;
		case 3:
			toolTip.add("tip.magic_blackcat.name");
			break;
		}
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;
        Vec3d vec3d = player.getLookVec().normalize().scale(2);
        boolean isProtect = this.hasAcce(player, ItemInit.summon_book);

		switch (this.data) {
		case 0:
			// 幻影オオカミ召喚魔法
			flag = this.shadowWolfAction(world, player, vec3d, stack, tags, isProtect);
			break;
		case 1:
			// 幻影ゴーレム召喚魔法
			flag = this.shadowGolemAction(world, player, vec3d, stack, tags, isProtect);
			break;
		case 2:
			// 幻影馬召喚魔法
			flag = this.shadowHorseAction(world, player, vec3d, stack, tags, isProtect);
			break;
		case 3:
			// 黒猫魔法
			flag = this.blackCatAction(world, player, vec3d, stack, tags, isProtect);
			break;
		}

		return flag;
	}

	// 幻影オオカミ召喚魔法
	public boolean shadowWolfAction (World world, EntityPlayer player, Vec3d vec3d, ItemStack stack, NBTTagCompound tags, boolean isProtect) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			EntityShadowWolf entity = new EntityShadowWolf(world);
			entity.setPosition(player.posX + vec3d.x, player.posY + 0.5D, player.posZ + vec3d.z);
			entity.setTamed(true);
			entity.setOwnerId(player.getUniqueID());

			entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D + level);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D + level);
			entity.setHealth((float) (10D + level));
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			entity.setProtect(isProtect);
			world.spawnEntity(entity);
			world.playSound(null, player.getPosition(), SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	// 幻影ゴーレム召喚魔法
	public boolean shadowGolemAction (World world, EntityPlayer player, Vec3d vec3d, ItemStack stack, NBTTagCompound tags, boolean isProtect) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			EntityShadowGolem entity = new EntityShadowGolem(world);
			entity.setPosition(player.posX + vec3d.x, player.posY + 0.5D, player.posZ + vec3d.z);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D + level * 5D);
			entity.setHealth((float) (50D + level * 5D));
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			entity.setProtect(isProtect);
			world.spawnEntity(entity);
			world.playSound(null, player.getPosition(), SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	// 幻影馬召喚魔法
	public boolean shadowHorseAction (World world, EntityPlayer player, Vec3d vec3d, ItemStack stack, NBTTagCompound tags, boolean isProtect) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			EntityShadowHorse entity = new EntityShadowHorse(world);
			entity.setPosition(player.posX + vec3d.x, player.posY + 0.5D, player.posZ + vec3d.z);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20D + level * 2.5D);
			entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
			entity.getEntityAttribute(JUMP_STRENGTH).setBaseValue(2.5D);
			entity.setHealth((float) (20D + level * 2.5D));
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			entity.setGrowingAge(0);
			entity.setTemper(100);
			entity.setHorseTamed(true);
			entity.setHorseArmorStack(new ItemStack(Items.SADDLE));
			entity.setInv(0, new ItemStack(Items.SADDLE));
			entity.updateHorseSlots();
			entity.setProtect(isProtect);
			world.spawnEntity(entity);
			world.playSound(null, player.getPosition(), SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	// 黒猫召喚魔法
	public boolean blackCatAction (World world, EntityPlayer player, Vec3d vec3d, ItemStack stack, NBTTagCompound tags, boolean isProtect) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			EntityBlackCat entity = player.isSneaking() ? new EntityTamedKit(world) : new EntityBlackCat(world);
			entity.setPosition(player.posX + vec3d.x, player.posY + 0.5D, player.posZ + vec3d.z);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D + level * 5D);
			entity.setHealth((float) (50D + level * 5D));
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			entity.setProtect(isProtect);
			entity.setTamed(true);
			entity.setOwnerId(player.getUniqueID());

			world.spawnEntity(entity);
			world.playSound(null, player.getPosition(), SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return true;
	}
}
