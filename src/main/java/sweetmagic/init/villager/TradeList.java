package sweetmagic.init.villager;

import java.util.Random;

import net.minecraft.item.ItemStack;
import sweetmagic.init.ItemInit;

public class TradeList {

	public static Random rand = new Random();

	// 種
	public static ItemStack getSeed() {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(7)) {
		case 0:
			return new ItemStack(ItemInit.sweetpotato, rand.nextInt(3) + 3);
		case 1:
			return new ItemStack(ItemInit.vannila_pods, rand.nextInt(4) + 4);
		case 2:
			return new ItemStack(ItemInit.strawberry, rand.nextInt(2) + 2);
		case 3:
			return new ItemStack(ItemInit.blueberry, rand.nextInt(3) + 3);
		case 4:
			return new ItemStack(ItemInit.azuki_seed, rand.nextInt(3) + 3);
		case 5:
			return new ItemStack(ItemInit.blueberry, rand.nextInt(2) + 1);
		case 6:
			return new ItemStack(ItemInit.sweetpotato, rand.nextInt(2) + 1);
		}

		return stack;
	}

	// 料理素材
	public static ItemStack getCookMaterial () {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(6)) {
		case 0:
			return new ItemStack(ItemInit.flourpowder, rand.nextInt(3) + 2);
		case 1:
			return new ItemStack(ItemInit.gelatin, rand.nextInt(2) + 1);
		case 2:
			return new ItemStack(ItemInit.salt, rand.nextInt(6) + 6);
		case 3:
			return new ItemStack(ItemInit.olive_oil, rand.nextInt(3) + 2);
		case 4:
			return new ItemStack(ItemInit.milk_pack, rand.nextInt(3) + 2);
		case 5:
			return new ItemStack(ItemInit.cocoamass, rand.nextInt(3) + 2);
		}

		return stack;
	}

	public static ItemStack getInterMaterial () {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(3)) {
		case 0:
			return new ItemStack(ItemInit.custard, rand.nextInt(2) + 1);
		case 1:
			return new ItemStack(ItemInit.toast, rand.nextInt(3) + 1);
		case 2:
			return new ItemStack(ItemInit.cheese, rand.nextInt(3) + 1);
		case 3:
			return new ItemStack(ItemInit.salt_popcorn, rand.nextInt(2) + 1);
		case 4:
			return new ItemStack(ItemInit.caramel_popcorn, rand.nextInt(2) + 1);
		case 5:
			return new ItemStack(ItemInit.lemon_cookie, rand.nextInt(2) + 1);
		case 6:
			return new ItemStack(ItemInit.icebox_cookie, rand.nextInt(2) + 1);
		}

		return stack;
	}

	// 料理
	public static ItemStack cookItem () {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(7)) {
		case 0:
			return new ItemStack(ItemInit.chocolate);
		case 1:
			return new ItemStack(ItemInit.white_chocolate);
		case 2:
			return new ItemStack(ItemInit.mont_blanc);
		case 3:
			return new ItemStack(ItemInit.cake_roll);
		case 4:
			return new ItemStack(ItemInit.eclair);
		case 5:
			return new ItemStack(ItemInit.blueberry_muffin);
		case 6:
			return new ItemStack(ItemInit.chocolate_muffin);
		}

		return stack;
	}

	// 魔法の種
	public static ItemStack getMagicSeed() {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(9)) {
		case 0:
			return new ItemStack(ItemInit.sugarbell_seed, rand.nextInt(4) + 3);
		case 1:
			return new ItemStack(ItemInit.sannyflower_seed, rand.nextInt(4) + 3);
		case 2:
			return new ItemStack(ItemInit.moonblossom_seed, rand.nextInt(4) + 3);
		case 3:
			return new ItemStack(ItemInit.dm_seed, rand.nextInt(4) + 3);
		case 4:
			return new ItemStack(ItemInit.clerodendrum_seed, rand.nextInt(4) + 3);
		case 5:
			return new ItemStack(ItemInit.fire_nasturtium_seed, rand.nextInt(4) + 3);
		case 6:
			return new ItemStack(ItemInit.sticky_stuff_seed, rand.nextInt(4) + 3);
		case 7:
			return new ItemStack(ItemInit.glowflower_seed, rand.nextInt(4) + 3);
		case 8:
			return new ItemStack(ItemInit.cotton_seed, rand.nextInt(4) + 3);
		}

		return stack;
	}

	// 魔法の花弁
	public static ItemStack getMagicPetal() {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(8)) {
		case 0:
			return new ItemStack(ItemInit.sugarbell, rand.nextInt(6) + 4);
		case 1:
			return new ItemStack(ItemInit.sannyflower_petal, rand.nextInt(2) + 1);
		case 2:
			return new ItemStack(ItemInit.moonblossom_petal, rand.nextInt(2) + 1);
		case 3:
			return new ItemStack(ItemInit.dm_flower, rand.nextInt(2) + 1);
		case 4:
			return new ItemStack(ItemInit.clero_petal, rand.nextInt(6) + 4);
		case 5:
			return new ItemStack(ItemInit.fire_nasturtium_petal, rand.nextInt(6) + 4);
		case 6:
			return new ItemStack(ItemInit.sticky_stuff_petal, rand.nextInt(6) + 4);
		case 7:
			return new ItemStack(ItemInit.cotton, rand.nextInt(6) + 4);
		}

		return stack;
	}

	// 魔法の道具
	public static ItemStack getMagicItem() {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(13)) {
		case 0:
			return new ItemStack(ItemInit.blank_page, rand.nextInt(3) + 2);
		case 1:
			return new ItemStack(ItemInit.blank_magic, rand.nextInt(2) + 1);
		case 2:
			return new ItemStack(ItemInit.mysterious_page, rand.nextInt(2) + 1);
		case 3:
			return new ItemStack(ItemInit.magicmeal, rand.nextInt(4) + 3);
		case 4:
			return new ItemStack(ItemInit.card_heal, 1);
		case 5:
			return new ItemStack(ItemInit.card_normal, 1);
		case 6:
			return new ItemStack(ItemInit.alt_axe, 1);
		case 7:
			return new ItemStack(ItemInit.alt_bucket, 1);
		case 8:
			return new ItemStack(ItemInit.alt_hoe, 1);
		case 9:
			return new ItemStack(ItemInit.alt_pick, 1);
		case 10:
			return new ItemStack(ItemInit.alt_shears, 1);
		case 11:
			return new ItemStack(ItemInit.alt_shovel, 1);
		case 12:
			return new ItemStack(ItemInit.alt_sword, 1);
		}

		return stack;
	}

	// 敵ドロップ
	public static ItemStack getMagicDrop() {

		Random rand = new Random();
		ItemStack stack = ItemStack.EMPTY;

		switch (rand.nextInt(7)) {
		case 0:
			return new ItemStack(ItemInit.electronic_orb, rand.nextInt(2) + 1);
		case 1:
			return new ItemStack(ItemInit.poison_bottle, rand.nextInt(2) + 1);
		case 2:
			return new ItemStack(ItemInit.unmeltable_ice, rand.nextInt(2) + 1);
		case 3:
			return new ItemStack(ItemInit.grav_powder, rand.nextInt(2) + 1);
		case 4:
			return new ItemStack(ItemInit.tiny_feather, rand.nextInt(2) + 1);
		case 5:
			return new ItemStack(ItemInit.stray_soul, rand.nextInt(2) + 1);
		case 6:
			return new ItemStack(ItemInit.ender_shard, rand.nextInt(2) + 1);
		}

		return stack;
	}
}
