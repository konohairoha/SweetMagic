package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.event.EntityItemTossEvent;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.key.SMKeybind;

public class SMBookCosmic extends SMBook implements IMFTool {

	public static final String ISPICKUP = "isPickUp";
	private int maxMF = 0;
	private static final ItemStack AETHER = new ItemStack(ItemInit.aether_crystal);
	private static final ItemStack PURE = new ItemStack(ItemInit.pure_crystal);

	public SMBookCosmic(String name, int data) {
		super(name, data);
		this.setMaxMF(data == 0 ? 3000000 : 6000000);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		if (!player.isSneaking()) { return super.onItemRightClick(world, player, hand); }

		ItemStack stack = player.getHeldItem(hand);

		if (!world.isRemote) {

			Item item = this.data == 0 ? ItemInit.aether_crystal : ItemInit.pure_crystal;
			ItemStack crystal = new ItemStack(item);
			int mf = this.getMF(stack);
			int aether_mf = this.getItemMF(crystal);

			// MFが600以上あるなら
			if (mf >= aether_mf) {
				int amount = Math.min(64, mf / aether_mf);
				this.setMF(stack, mf - aether_mf * amount);
				world.spawnEntity(new EntityMagicItem(world, player, new ItemStack(item, amount)));
			}

			// 600未満ならGUIを開く
			else {
				super.onItemRightClick(world, player, hand);
			}
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	//ツールチップの表示
  	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
  		super.addInformation(stack, world, tooltip, flag);
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magicbook_cosmic.name") ));

		if (this.data == 1) {
			tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.magicbook_cosmic_summon.name") ));
		}

		// シフトを押したとき
		if (Keyboard.isKeyDown(42)) {
			tooltip.add("");
			tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magicbook_cosmic_shift.name") ));
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.BACK) + TextFormatting.GREEN + this.getTip("tip.magicbook_cosmic_open.name")));
			tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magicbook_cosmic_pick.name") + this.isPickUp(stack) ));
		}

		else {
			tooltip.add(I18n.format(TextFormatting.RED + this.getTip("tip.shift.name")));
		}
  	}

	// エンチャレベル取得
	public int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

  	// 最大MFを取得
	@Override
  	public int getMaxMF (ItemStack stack) {
		int addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 5) * (this.maxMF / 100);
  		return this.maxMF + addMaxMF;
  	}

	@Override
	public void setMaxMF(int maxMF) {
		this.maxMF = maxMF;
	}

	// アイテムを拾うかのフラグ
	public boolean isPickUp (ItemStack stack) {
		return this.getNBT(stack).getBoolean(ISPICKUP);
	}

	public void setPickUp (ItemStack stack) {
		NBTTagCompound tags = this.getNBT(stack);
		tags.setBoolean(ISPICKUP, !tags.getBoolean(ISPICKUP));
	}

	// エンチャントエフェクト描画
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return false;
    }

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.data == 1 && this.getMF(stack) != this.getMaxMF(stack);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ( (double) this.getMF(stack) / (double) this.getMaxMF(stack) );
	}

	public boolean checkCrystal (Item item) {
		return this.data == 0 ? EntityItemTossEvent.cosmicList.contains(item) : EntityItemTossEvent.scarletList.contains(item);
	}

	// tierの取得
	public int getTier () {
		return this.data + 2;
	}

//	@Override
//	public List<String> magicToolTip(List<String> toolTip) {
//		toolTip.add(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.BACK) + TextFormatting.GOLD + this.getTip("tip.magicbook_acc.name"));
//		return toolTip;
//	}
}
