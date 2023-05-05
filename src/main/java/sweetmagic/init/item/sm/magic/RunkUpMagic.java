package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class RunkUpMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;
	private SMElement subEle = null;
	private int needEXP = 0;

	public RunkUpMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.AIR, ele, tier, coolTime, mf, true);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public RunkUpMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.AIR, ele, tier, coolTime, mf, true);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	/**
	 * 0 = 範囲回復魔法
	 */

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		String tip = "";

		if (this.data == 5) {
			tip = "tip.magic_creative.name";
		}

		else {
			tip = new TextComponentTranslation("tip.runk-up-magic.name", new Object[0]).getFormattedText() + " " + this.addExp();
		}

		toolTip.add(tip);
		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;

		switch (this.data) {
		case 5:
			// 範囲回復魔法
			flag = this.ruckUpMagic(world, player, stack, tags);
			break;
		}

		return flag;
	}

	// レベルアップ魔法
	public boolean ruckUpMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) - wand.addWandLevel(world, player, stack, this, EnchantInit.wandAddPower);

		if (wand.isNotElement()) {
			level -= 3;
		}

		this.needEXP = wand.needExp(wand.getMaxLevel(), level + 1, stack);
		return true;
	}

	// 追加経験値
	public int addExp () {

		switch(this.data) {
		case 0: return 100;
		case 1: return 750;
		case 2: return 2000;
		case 3: return 5000;
		case 4: return 30000;
		case 5: return this.needEXP;
		}

		return 100;
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) - wand.addWandLevel(world, player, stack, this, EnchantInit.wandAddPower);
		return !wand.isCreativeWand() && level < wand.getMaxLevel();
	}
}
