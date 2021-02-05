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

	int downTime = 0;

	public CosmicWand (String name, int tier, int maxMF, int slot) {
		super(name, tier, maxMF, slot);
	}

	// 魔法アクション中の処理
	@Override
	public boolean onAction (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.getEnchantLevel(EnchantInit.wandAddPower, stack);

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

			// 光属性なら
			if (smItem.getElement() == SMElement.getElement(this.getElement(stack))) {
				this.downTime = (this.getTier() - 2) * 10;
			}

			// それ以外の属性なら
			else {
				this.downTime = 0;
			}

			// クールタイム
			player.getCooldownTracker().setCooldown(item, this.getCoolTime(stack, smItem.getCoolTime()));

			// 使用した魔法分だけ消費
			this.setMF(stack, this.setMF(stack, smItem));

			// アイテムを消費するかどうか
			if (smItem.isShirink()) {
				this.shrinkItem(player, stack, (Item) smItem);
			}

			// actionFlagがtrueなら経験値を増やす
			if (actionFlag) {
				int addExp = (int) smItem.getUseMF() / 10 ;

				addExp = addExp <= 0 ? 0 : addExp;

				// レベルアップできるかどうか
				this.levelUpCheck(world, player, stack, addExp);
			}
		}
	}

	// クールタイム減少時間の値
	public int getCoolTimeDown () {
		return this.downTime;
	}
}
