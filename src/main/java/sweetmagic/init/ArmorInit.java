package sweetmagic.init;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import sweetmagic.event.SMSoundEvent;

public class ArmorInit {

	public static ArmorMaterial magicians_robe = EnumHelper
			.addArmorMaterial("magicians_robe", "sweetmagic:magicians_robe", 40, new int[] { 3, 6, 7, 3 }, 35, SMSoundEvent.ROBE, 1);

	public static ArmorMaterial wizard_robe = EnumHelper
			.addArmorMaterial("wizard_robe", "sweetmagic:wizard_robe", 40, new int[] { 3, 6, 7, 3 }, 35, SMSoundEvent.ROBE, 1);

	public static ArmorMaterial magicians_pouch = EnumHelper
			.addArmorMaterial("magicians_pouch", "sweetmagic:magicians_pouch", 40, new int[] { 3, 6, 10, 3 }, 35, SMSoundEvent.ROBE, 1);

	/**
	 *  .addArmorMaterial(name, textureName, durability, reductionAmounts, enchantability, soundOnEquip, toughness)
	 * name "Material名"
	 * textureName テクスチャの名前 "Modid:pngファイル名の_layer前まで"
	 * durability 防具の耐久度 革5 鉄15 金7 ダイヤモンド33
	 * reductionAmounts 各部の防御ポイント 革{ 1, 2, 3, 1 }、鉄{ 2, 5, 6, 2 }、金{ 1, 3, 5, 2 }、ダイヤモンド{ 3, 6, 8, 3 }
	 * enchantability 数値が大きいほど良いエンチャントが付き易い 革15、鉄9、金25、ダイヤモンド10
	 * soundOnEquip 効果音
	 * toughness タフネス、ダメージカット率をより上げる デフォルト0 ダイヤモンドのみ2
	 */
}
