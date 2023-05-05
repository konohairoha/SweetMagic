package sweetmagic.init.villager;

import java.util.Random;

import net.minecraft.item.ItemStack;
import sweetmagic.init.ItemInit;

public class TradeList {

	// 種
	public static ItemStack getSeed() {

		Random rand = new Random();

		switch (rand.nextInt(5)) {
		case 0:
			return new ItemStack(ItemInit.sweetpotato, rand.nextInt(32) + 8);
		case 1:
			return new ItemStack(ItemInit.vannila_pods, rand.nextInt(32) + 8);
		case 2:
			return new ItemStack(ItemInit.strawberry, rand.nextInt(32) + 8);
		case 3:
			return new ItemStack(ItemInit.blueberry, rand.nextInt(32) + 8);
		case 4:
			return new ItemStack(ItemInit.seedbag, rand.nextInt(16) + 4);
		}

		return ItemStack.EMPTY;
	}

	// 料理素材
	public static ItemStack getCookMaterial () {

		Random rand = new Random();

		switch (rand.nextInt(7)) {
		case 0:
			return new ItemStack(ItemInit.flourpowder, rand.nextInt(3) + 2);
		case 1:
			return new ItemStack(ItemInit.gelatin, rand.nextInt(2) + 1);
		case 2:
			return new ItemStack(ItemInit.breadcrumbs, rand.nextInt(6) + 6);
		case 3:
			return new ItemStack(ItemInit.olive_oil, rand.nextInt(3) + 2);
		case 4:
			return new ItemStack(ItemInit.vannila_essence, rand.nextInt(3) + 2);
		case 5:
			return new ItemStack(ItemInit.cocoamass, rand.nextInt(3) + 2);
		case 6:
			return new ItemStack(ItemInit.fruit_wine, rand.nextInt(3) + 2);
		}

		return ItemStack.EMPTY;
	}

	public static ItemStack getInterMaterial () {

		Random rand = new Random();

		switch (rand.nextInt(7)) {
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

		return ItemStack.EMPTY;
	}

	// 料理
	public static ItemStack cookItem () {

		Random rand = new Random();

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

		return ItemStack.EMPTY;
	}

	// 魔法の種
	public static ItemStack getMagicSeed() {

		Random rand = new Random();

		switch (rand.nextInt(6)) {
		case 0:
			return new ItemStack(ItemInit.sticky_stuff_seed, rand.nextInt(16) + 8);
		case 1:
			return new ItemStack(ItemInit.sannyflower_seed, rand.nextInt(16) + 8);
		case 2:
			return new ItemStack(ItemInit.moonblossom_seed, rand.nextInt(16) + 8);
		case 3:
			return new ItemStack(ItemInit.dm_seed, rand.nextInt(16) + 8);
		case 4:
			return new ItemStack(ItemInit.clerodendrum_seed, rand.nextInt(16) + 8);
		case 5:
			return new ItemStack(ItemInit.fire_nasturtium_seed, rand.nextInt(16) + 8);
		}

		return ItemStack.EMPTY;
	}

	// 魔法の花弁
	public static ItemStack getMagicPetal() {

		Random rand = new Random();

		switch (rand.nextInt(5)) {
		case 0:
			return new ItemStack(ItemInit.fire_nasturtium_petal, rand.nextInt(12) + 6);
		case 1:
			return new ItemStack(ItemInit.sannyflower_petal, rand.nextInt(8) + 4);
		case 2:
			return new ItemStack(ItemInit.moonblossom_petal, rand.nextInt(8) + 4);
		case 3:
			return new ItemStack(ItemInit.dm_flower, rand.nextInt(12) + 6);
		case 4:
			return new ItemStack(ItemInit.clero_petal, rand.nextInt(12) + 6);
		}

		return ItemStack.EMPTY;
	}

	// 魔法の道具
	public static ItemStack getMagicItem() {

		Random rand = new Random();

		switch (rand.nextInt(8)) {
		case 0:
			return new ItemStack(ItemInit.blank_page, rand.nextInt(16) + 4);
		case 1:
			return new ItemStack(ItemInit.blank_magic, rand.nextInt(16) + 4);
		case 2:
			return new ItemStack(ItemInit.mysterious_page, rand.nextInt(8) + 2);
		case 3:
			return new ItemStack(ItemInit.magicmeal, rand.nextInt(20) + 12);
		case 4:
			return new ItemStack(ItemInit.alt_axe, 1);
		case 5:
			return new ItemStack(ItemInit.alt_pick, 1);
		case 6:
			return new ItemStack(ItemInit.alt_shovel, 1);
		case 7:
			return new ItemStack(ItemInit.alt_sword, 1);
		}

		return ItemStack.EMPTY;
	}

	// 敵ドロップ
	public static ItemStack getMagicDrop() {

		Random rand = new Random();

		switch (rand.nextInt(7)) {
		case 0:
			return new ItemStack(ItemInit.electronic_orb, rand.nextInt(6) + 2);
		case 1:
			return new ItemStack(ItemInit.poison_bottle, rand.nextInt(6) + 2);
		case 2:
			return new ItemStack(ItemInit.unmeltable_ice, rand.nextInt(6) + 2);
		case 3:
			return new ItemStack(ItemInit.grav_powder, rand.nextInt(6) + 2);
		case 4:
			return new ItemStack(ItemInit.tiny_feather, rand.nextInt(6) + 2);
		case 5:
			return new ItemStack(ItemInit.stray_soul, rand.nextInt(6) + 2);
		case 6:
			return new ItemStack(ItemInit.ender_shard, rand.nextInt(6) + 2);
		}

		return ItemStack.EMPTY;
	}

	// レアアイテム
	public static ItemStack getRateItem () {

		Random rand = new Random();

		switch (rand.nextInt(5)) {
		case 0:
			return new ItemStack(ItemInit.witch_tears, rand.nextInt(4) + 2);
		case 1:
			return new ItemStack(ItemInit.mystical_page, 1);
		case 2:
			return new ItemStack(ItemInit.magic_divine_force, rand.nextInt(12) + 6);
		case 3:
			return new ItemStack(ItemInit.magic_pure_force, rand.nextInt(4) + 2);
		case 4:
			return new ItemStack(ItemInit.magic_deus_force, rand.nextInt(2) + 1);
		}

		return ItemStack.EMPTY;
	}

	// サニムン花弁
	public static ItemStack getSanyMoonItem () {

		Random rand = new Random();

		switch (rand.nextInt(2)) {
		case 0:
			return new ItemStack(ItemInit.sannyflower_petal, 2);
		case 1:
			return new ItemStack(ItemInit.moonblossom_petal, 2);
		}

		return ItemStack.EMPTY;
	}
}
