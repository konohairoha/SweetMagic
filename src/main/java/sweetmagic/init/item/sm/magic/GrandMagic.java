package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.entity.monster.EntityShadowGolem;
import sweetmagic.init.entity.monster.EntityShadowWolf;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class GrandMagic extends MFSlotItem {

	private final int data;
	ResourceLocation icon;
	public static final String SUMMONWOLF = "summonWolf";

	public GrandMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.GROUND, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + name + ".png");
    }

	public GrandMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.GROUND, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + dir + ".png");
    }

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	/**
	 * 0 = 幻影オオカミ召喚
	 * 1 = 幻影ゴーレム召喚
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
		}
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;

		switch (this.data) {
		case 0:
			// 幻影オオカミ召喚魔法
			flag = this.shadowWolfAction(world, player, stack, tags);
			break;
		case 1:
			// 幻影ゴーレム召喚魔法
			flag = this.shadowGolemAction(world, player, stack, tags);
			break;
		}

		return flag;
	}

	// 幻影オオカミ召喚魔法
	public boolean shadowWolfAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			BlockPos pos = wand.getWandPos().up();
			EntityShadowWolf entity = new EntityShadowWolf(world);
			entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			entity.setTamed(true);
			entity.setOwnerId(player.getUniqueID());

			entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(4D + level);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10D + level);
			entity.setHealth((float) (10D + level));
			world.spawnEntity(entity);
			world.playSound(null, pos, SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	// 幻影ゴーレム召喚魔法
	public boolean shadowGolemAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (!world.isRemote) {

			// 杖の取得
			IWand wand = IWand.getWand(stack);
			int level = IWand.getLevel(wand, stack);

			BlockPos pos = wand.getWandPos().up();
			EntityShadowGolem entity = new EntityShadowGolem(world);
			entity.setPosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50D + level * 5D);
			entity.setHealth((float) (50D + level * 5D));
			world.spawnEntity(entity);
			world.playSound(null, pos, SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
		}
		return true;
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		switch (this.data) {
		// 幻影オオカミ召喚魔法
		case 0:

			NBTTagCompound data = player.getEntityData();

			// NBTを持ってなかったら
			if (!data.hasKey(SUMMONWOLF)) {
				data.setInteger(SUMMONWOLF, 1);
			}

			// NBTを持っていたら
			else {

				int count = data.getInteger(SUMMONWOLF) + 1;

				// 召喚数が4以上なら失敗
				if (count >= 4) { return false; }

				// 召喚
				data.setInteger(SUMMONWOLF, count);
			}
			return true;
		}
		return true;
	}
}
