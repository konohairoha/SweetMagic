package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IChoker;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.api.iitem.IHarness;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.tile.slot.SlotPredicates;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.key.SMKeybind;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class ToolTipEvent {

	@SubscribeEvent
	public static void tTipEvent(ItemTooltipEvent event) {

		ItemStack stack = event.getItemStack();
		if (stack.isEmpty()) { return; }

		List<String> tooltip = event.getToolTip();
		Item itemStack = stack.getItem();

		// 杖に入れる魔法アイテムなら
		if (itemStack instanceof ISMItem) {

			// シフトを押したとき
			if (Keyboard.isKeyDown(42)) {

				ISMItem item = (ISMItem) stack.getItem();

				String tipEle = new TextComponentTranslation(enumString(item.getElement().name()), new Object[0]).getFormattedText();
				String tipType = new TextComponentTranslation(enumString(item.getType().name()), new Object[0]).getFormattedText();

				if (item.getSubElement() != null) {
					tipEle += "/" + new TextComponentTranslation(enumString(item.getSubElement().name()), new Object[0]).getFormattedText();
				}

				String texEle = new TextComponentTranslation(getTipName("smelement"), new Object[0]).getFormattedText() + " ： " + tipEle;
				String texType = new TextComponentTranslation(getTipName("smtype"), new Object[0]).getFormattedText() +  " ： " + tipType;

				String tipCoolTime = getTip("tip.cooltime.name");
				String tipTier= getTip("tip.tier.name");
				String tiprequireMF = getTip("tip.requiremf.name");

				tooltip.add(I18n.format(TextFormatting.GREEN + texType));
				tooltip.add(I18n.format(TextFormatting.GREEN + texEle));
				tooltip.add(I18n.format(TextFormatting.GREEN + tipTier + " ： " + item.getTier()));
				tooltip.add(I18n.format(TextFormatting.GREEN + tiprequireMF + " ： " + String.format("%,d", item.getUseMF())));
				tooltip.add(I18n.format(TextFormatting.GREEN + tipCoolTime + " ： " + item.getCoolTime()));

				// Stringのリストを作成
				List<String> list = new ArrayList<>();
				List<String> toolTip = item.magicToolTip(list);
				if (toolTip.isEmpty()) { return; }

				String effect = getTip("tip.effect.name");

				// リストの分だけ回す
				for (String name : toolTip) {
					String listTip = new TextComponentTranslation(name, new Object[0]).getFormattedText();
					tooltip.add(I18n.format(TextFormatting.GREEN + effect + TextFormatting.GOLD + listTip));
				}

				item.magicToolTip(list);
			}

			// シフトを押してないとき
			else {

				getShiftTip(tooltip);
			}

		}

		// 杖のとき
		else if (itemStack instanceof IWand) {

			IWand wand = (IWand) itemStack;

			// キー操作
			String open = getTip("tip.open.name");
			String next = getTip("tip.next.name");
			String back = getTip("tip.back.name");
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.OPEN) + open));
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.NEXT) + next));
			tooltip.add(I18n.format(TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.BACK) + back));

			// tier
	  		String textt = new TextComponentTranslation("tip.tier.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GREEN + textt + " : " + wand.getTier() ));

			// クリエワンドなら終了
			if (wand.isCreativeWand()) { return; }

			String tipLv = getTip("tip.level.name");
			String tipExp = getTip("tip.experience.name");
			String tipMF = getTip("tip.mf.name");

			// 次のレベル
			int lv = wand.getLevel(stack);
			int nextLevel = ++lv;

			// レベルアップに必要な経験値
			int needExp = wand.needExp(wand.getMaxLevel(), nextLevel, stack);

			tooltip.add(I18n.format(TextFormatting.GREEN + tipExp + " : " + wand.getLevel(stack)));
			tooltip.add(I18n.format(TextFormatting.GREEN + tipMF + " : " + String.format("%,d", wand.getMF(stack))));
			tooltip.add(I18n.format(TextFormatting.GREEN + tipLv + " : " + String.format("%,d", needExp)));

			// 属性杖なら属性表示
			if (wand.isNotElement()) {
				String tipEle = new TextComponentTranslation(enumString(wand.getWandElement().toString()), new Object[0]).getFormattedText();
				String texEle = new TextComponentTranslation(getTipName("smelement"), new Object[0]).getFormattedText() + " ： " + tipEle;
				tooltip.add(I18n.format(TextFormatting.GREEN + texEle));
			}

			// シフトを押したとき
			if (Keyboard.isKeyDown(42)) {
				String tipScroll = getTip("tip.mousescroll.name");
				String tipShift = getTip("tip.shiftleft.name");
				tooltip.add(I18n.format(TextFormatting.GOLD + tipScroll));
				tooltip.add(I18n.format(TextFormatting.GOLD + tipShift));
			}

			else {
				getShiftTip(tooltip);
			}
		}

		// MF関連アイテムのとき
		else if (itemStack instanceof IMFTool) {


			IMFTool wand = (IMFTool) itemStack;
			String tipMF = getTip("tip.mf.name");
			tooltip.add(I18n.format(TextFormatting.GREEN + tipMF + " : " + String.format("%,d", wand.getMF(stack))));

			// ローブ
			if (itemStack instanceof IRobe) {

				IRobe robe = (IRobe) itemStack;

				// キー操作
				String open = getTip("tip.open.name");
				String amor = getTip("tip.robe_armor.name");
				tooltip.add(I18n.format(amor + TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.OPEN) + open));

				String dame = getTip("tip.smmagic_damagecut.name");

				// Math.floorを使わないと端数が出る
				String cut =  Math.floor( ( 1 - robe.getMagicDamageCut() ) * 100) + "";
				tooltip.add(I18n.format(TextFormatting.GREEN + dame + " ： " + cut) + "%");

			}

			else if (itemStack instanceof IHarness) {
				tooltip.add(I18n.format(TextFormatting.GOLD + getTip("tip.angel_harness.name")));
				tooltip.add(I18n.format(TextFormatting.GOLD + getTip("tip.angel_harness_charge.name")));
				tooltip.add(I18n.format(TextFormatting.GOLD + getTip("tip.angel_harness_dus.name")));
			}

			else if (itemStack instanceof IChoker) {
				tooltip.add(I18n.format(TextFormatting.GOLD + getTip("tip.aether_choker.name")));
			}
		}

		// 属性アイテムのとき
		else if (itemStack instanceof IElementItem && !(itemStack instanceof ISMItem)) {

			IElementItem ele = (IElementItem) itemStack;

			String tipEle = getTip(enumString(ele.getElement().name()));
			String texEle = getTip(getTipName("smelement")) + " ： " + tipEle;

			tooltip.add(I18n.format(TextFormatting.GREEN + texEle));
		}

		// ポーチ
		else if (itemStack instanceof IPouch) {

			// キー操作
			String open = getTip("tip.open.name");
			String amor = getTip("tip.robe_armor.name");
			tooltip.add(I18n.format(amor + TextFormatting.RED + ClientKeyHelper.getKeyName(SMKeybind.POUCH) + open));
		}

		// 装備品
		else if (itemStack instanceof IAcce) {

			IAcce acce = (IAcce) itemStack;

			// Stringのリストを作成
			List<String> list = new ArrayList<>();
			tooltip.add(I18n.format(TextFormatting.RED + new TextComponentTranslation("tip.smacc.name", new Object[0]).getFormattedText()));

			String tipEle = getTip(enumString(acce.getAcceType().name()));
			String texEle = getTip(getTipName("smacce")) + " ： " + tipEle;

			tooltip.add(I18n.format(TextFormatting.GREEN + texEle));
			tooltip.add(I18n.format(TextFormatting.GREEN + getTip(getTipName("isduplication")) + " ： " + acce.isDuplication()));

			List<String> toolTip = acce.magicToolTip(list);
			if (toolTip.isEmpty()) { return; }

			String effect = getTip("tip.effect.name");

			// リストの分だけ回す
			for (String name : toolTip) {
				String listTip = new TextComponentTranslation(name, new Object[0]).getFormattedText();
				tooltip.add(I18n.format(TextFormatting.GREEN + effect + " " + TextFormatting.GOLD + listTip));
			}
		}

		// MFアイテムなら
		if (SlotPredicates.hasMFItem(stack)) {
			int itemMF = SweetMagicAPI.getMFFromItem(stack);
			String mf = String.format("%,d", itemMF);
			String stackMF = String.format("%,d", itemMF * stack.getCount());
			tooltip.add(I18n.format(TextFormatting.GREEN + mf + "MF"));
			tooltip.add(I18n.format(TextFormatting.GREEN + "Stack：" + stackMF + "MF"));
		}
	}

	// 翻訳に変換
	public static String getTip (String tip) {
		return new TextComponentTranslation(tip, new Object[0]).getFormattedText();
	}

	// シフト押したときのツールチップ
	public static void getShiftTip (List<String> tooltip) {
		String tipShift= getTip("tip.shift.name");
		tooltip.add(I18n.format(TextFormatting.RED + tipShift));
	}

	public static String enumString (String name) {
		return getTipName(name.toLowerCase());
	}

	// ツールチップに変換
	public static String getTipName (String name) {
		name = "tip." + name + ".name";
		return name;
	}
}
