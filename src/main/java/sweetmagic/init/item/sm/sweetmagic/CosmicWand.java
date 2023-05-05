package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.item.sm.eitem.SMElement;

public class CosmicWand extends SMWand {

	public float downTime = 0;
	public final SMElement element;

	public CosmicWand (String name, int tier, int maxMF, int slot, SMElement element) {
		super(name, tier, maxMF, slot);
		this.element = element;
	}

	// 魔法アクション中の処理
	@Override
	public boolean onAction (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.addWandLevel(world, player, stack, smItem, EnchantInit.wandAddPower);

		// 杖と魔法の属性一致してるなら
		if (this.isElementEqual(smItem)) {
			enchaLevel += 2;
		}

		tags.setInteger(LEVEL, (level + this.getTier() - 2 + enchaLevel));
		flag = smItem.onItemAction(world, player, stack, item);

		// レベルを戻す
		tags.setInteger(LEVEL, level);

		return flag;
	}

	// 魔法アクション後の処理
	@Override
	public void magicActionAfter (World world, EntityPlayer player,ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags, boolean actionFlag) {

		// クリエワンド以外なら
		if (!this.isCreativeWand()) {

			// 杖と魔法の属性一致してるなら
			this.downTime = this.isNotElement() && this.isElementEqual(smItem) ? 10F : 0F;

			float bounus = 1F + (float) (this.getEnchantLevel(EnchantInit.elementBonus, stack)) * 0.2F;
			this.downTime *= Math.min(3F, bounus);

			// クールタイム
			player.getCooldownTracker().setCooldown(item, this.getCoolTime(player, stack, smItem.getCoolTime()));

			// 使用した魔法分だけ消費
			this.setMF(stack, this.setMF(player, stack, smItem));

			// アイテムを消費するかどうか
			if (smItem.isShirink()) {
				this.shrinkItem(player, stack, (Item) smItem);
			}

			// actionFlagがtrueならレベルアップチェック
			if (actionFlag && !world.isRemote) {
				this.levelUpCheck(world, player, stack, this.getAddExp(player, smItem));
			}
		}
	}

	// クールタイム減少時間の値
	@Override
	public float getBounusValue () {
		return this.downTime;
	}

	// 杖の属性
	@Override
	public SMElement getWandElement () {
		return this.element;
	}
}
