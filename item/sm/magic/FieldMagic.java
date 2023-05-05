package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.entity.projectile.EntityMagicCycle;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class FieldMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;

	public FieldMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.FIELD, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public FieldMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.FIELD, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	public FieldMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir, SMElement subEle) {
		super(name, SMType.SHOTTER, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
		this.setSubElement(subEle);
	}

	/**
	 * 0 = グラヴィティフィールド
	 * 1 = リフレッシュフィールド
	 * 2 = フレイムノヴァ
	 * 3 = アブソリュートゼロ
	 * 4 = アヴォイド・トルネード
	 */

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_vector_field.name");
			break;
		case 1:
			toolTip.add("tip.magic_reflesh_field.name");
			break;
		case 2:
			toolTip.add("tip.magic_blaze_end.name");
			break;
		case 3:
			toolTip.add("tip.magic_absolute_zero.name");
			break;
		case 4:
			toolTip.add("tip.magic_avoid_tornado.name");
			break;
		}
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

//		NBTTagCompound tags = stack.getTagCompound();

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		EntityMagicCycle entity = null;
		float scale = Math.min(8F, level * 1.5F) ;
		int time = level * 60;

		switch (this.data) {
		case 0:
			entity = new EntityMagicCycle(player, 103, 42, 35, scale * 1.5F, level, time, 0);
			break;
		case 1:
			entity = new EntityMagicCycle(player, 56, 110, 215, scale, level, time, 1);
			break;
		case 2:
			entity = new EntityMagicCycle(player, 255, 120, 96, scale * 2F, level, level * 10, 2);
			break;
		case 3:
			entity = new EntityMagicCycle(player, 107, 141, 255, scale * 2F, level, level * 10, 3);
			break;
		case 4:
			entity = new EntityMagicCycle(player, 79, 226, 160, scale * 0.5F, level, time, 4);
			break;
		}

		if (!world.isRemote) {
			world.spawnEntity(entity);
		}

		this.playSound(world, player, SMSoundEvent.FLASH, 0.75F, 1.25F);

		return true;
	}

	// 幻影オオカミ召喚魔法
	public boolean shadowWolfAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return true;
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return true;
	}
}
