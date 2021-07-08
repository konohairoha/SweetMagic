package sweetmagic.api.recipe.obmagia;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

@SMObMagiaRecipePlugin(priority = EventPriority.LOW)
public class ObMagiaRecipePlugin implements IObMagiaRecipePlugin {

	@Override
	public void registerObMagiaRecipe(ObMagiaRecipes recipe) {

		//レシピ登録方法…ItemStack(hand)、ItemStack[](Input)、ItemStack[](Output)

		// dig魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(Items.ARROW, 4),
			new ItemStack[] { new ItemStack(Items.IRON_SHOVEL), new ItemStack(Items.IRON_PICKAXE), new ItemStack(Items.IRON_AXE),
			new ItemStack(ItemInit.sugarbell, 2), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_dig) }
		));

		// 範囲dig魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_dig),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2),
			new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.DIAMOND_AXE), new ItemStack(ItemInit.sugarbell, 16)},
			new ItemStack[] { new ItemStack(ItemInit.magic_rangebreaker) }
		));

		// シルクタッチ範囲dig魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_rangebreaker),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal),
			new ItemStack(Items.DIAMOND_SHOVEL), new ItemStack(Items.DIAMOND_PICKAXE), new ItemStack(Items.DIAMOND_AXE),
			new ItemStack(ItemInit.alt_shovel), new ItemStack(ItemInit.alt_pick), new ItemStack(ItemInit.alt_axe), new ItemStack(ItemInit.sugarbell, 32)},
			new ItemStack[] { new ItemStack(ItemInit.magic_mining_magia) }
		));

		// 5範囲dig魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_mining_magia),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.sugarbell, 48)},
			new ItemStack[] { new ItemStack(ItemInit.magic_earth_destruction) }
		));

		// 回復魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.sannyflower_petal, 2),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.sugarbell, 2)
					, new ItemStack(ItemInit.magicmeal, 3), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_regene) }
		));

		// 範囲回復魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_regene),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.dm_flower, 8)
					, new ItemStack(ItemInit.sugarbell, 16), new ItemStack(ItemInit.sannyflower_petal, 16) },
			new ItemStack[] { new ItemStack(ItemInit.magic_heal) }
		));

		// デバフ解除+全回復魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_heal),
			new ItemStack[] { new ItemStack(ItemInit.magic_refresh), new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_healingwish) }
		));

		// デバフ解除 + 全回復魔法 + 衝撃吸収
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_healingwish),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.dm_flower, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_healing_hightlow) }
		));

		// 光魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.prizmium),
			new ItemStack[] { new ItemStack(BlockInit.glow_light, 2), new ItemStack(ItemInit.sugarbell, 2),
			new ItemStack(ItemInit.magicmeal, 3), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_light) }
		));

		// 多段光魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_light),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.sugarbell, 16),
			new ItemStack(ItemInit.mysterious_page, 2), new ItemStack(BlockInit.glow_light, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_starburst) }
		));

		// 散弾光魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_starburst),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.sugarbell, 32),
			new ItemStack(BlockInit.glow_light, 8), new ItemStack(ItemInit.mysterious_page, 4), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_sacredbuster) }
		));

		// 光/炎魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_sacredbuster),
			new ItemStack[] { new ItemStack(ItemInit.magic_flamenova), new ItemStack(ItemInit.cosmic_crystal_shard, 4),
			new ItemStack(ItemInit.prizmium, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_shining_flare) }
		));

		// 炎魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.fire_nasturtium_petal, 4),
			new ItemStack[] { new ItemStack(Items.BLAZE_POWDER, 3), new ItemStack(ItemInit.sugarbell, 2)
			, new ItemStack(ItemInit.sannyflower_petal, 3), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_fire) }
		));

		// 火炎爆発魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_fire, 1),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.fire_nasturtium_petal, 16)
					, new ItemStack(ItemInit.sugarbell, 16), new ItemStack(ItemInit.mysterious_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_flamenova) }
		));

		// 隕石落下魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_flamenova),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.fire_nasturtium_petal, 32)
					, new ItemStack(ItemInit.sugarbell, 32), new ItemStack(ItemInit.mysterious_page, 4), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_meteor_fall) }
		));

		// ブレイズエンド魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_meteor_fall),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.fire_nasturtium_petal, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_blaze_end) }
		));

		// 氷魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.unmeltable_ice, 3),
			new ItemStack[] { new ItemStack(Blocks.ICE, 2), new ItemStack(ItemInit.sugarbell, 2), new ItemStack(ItemInit.moonblossom_petal, 2),
			new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_frost) }
		));

		// 貫通氷魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_frost),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.unmeltable_ice, 8), new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_frostspear) }
		));

		// 氷雨魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_frostspear),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.unmeltable_ice, 16)
					, new ItemStack(ItemInit.sugarbell, 32), new ItemStack(ItemInit.mysterious_page, 5), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_frostrain) }
		));

		// 絶対零度魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_frostrain),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.unmeltable_ice, 32), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_absolute_zero) }
		));

		// 風射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.tiny_feather, 4),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.sugarbell, 3), new ItemStack(ItemInit.moonblossom_petal, 2),
			new ItemStack(ItemInit.sannyflower_petal, 2), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_tornado) }
		));

		// 3連風射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_tornado),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.tiny_feather, 8), new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_storm) }
		));

		// 風魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_storm),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.tiny_feather, 16), new ItemStack(ItemInit.mysterious_page, 5), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_cyclon) }
		));

		// 回避1魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.tiny_feather, 4),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.clero_petal, 4),
					new ItemStack(ItemInit.mystical_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_avoid) }
		));

		// 回避2魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_avoid),
			new ItemStack[] { new ItemStack(ItemInit.tiny_feather, 8), new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_avoid_2) }
		));

		// 回避3魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_avoid_2),
			new ItemStack[] { new ItemStack(ItemInit.tiny_feather, 16), new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_avoid_3) }
		));

		// 時間低速化魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.clero_petal, 8),
			new ItemStack[] { new ItemStack(ItemInit.sannyflower_petal, 16), new ItemStack(ItemInit.moonblossom_petal, 16), new ItemStack(ItemInit.mystical_page),
			new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_slowtime) }
		));

		// 敵スタン魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_slowtime),
			new ItemStack[] { new ItemStack(ItemInit.clero_petal, 16), new ItemStack(ItemInit.sannyflower_petal, 32), new ItemStack(ItemInit.moonblossom_petal, 32),
					new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_deusora) }
		));

		// 未来視魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_deusora),
			new ItemStack[] { new ItemStack(ItemInit.clero_petal, 32), new ItemStack(ItemInit.sannyflower_petal, 64), new ItemStack(ItemInit.moonblossom_petal, 64),
					new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_futurevision) }
		));

		// 不思議なページ
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.blank_page),
			new ItemStack[] { new ItemStack(ItemInit.sannyflower_petal, 2), new ItemStack(ItemInit.moonblossom_petal, 2), new ItemStack(ItemInit.sticky_stuff_petal)},
			new ItemStack[] { new ItemStack(ItemInit.mysterious_page) }
		));

		// 神秘的なページ
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.mysterious_page),
			new ItemStack[] { new ItemStack(ItemInit.clero_petal, 2), new ItemStack(ItemInit.magicmeal, 8), new ItemStack(ItemInit.witch_tears)},
			new ItemStack[] { new ItemStack(ItemInit.mystical_page) }
		));

		// 空の魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(Blocks.COBBLESTONE, 8),
			new ItemStack[] { new ItemStack(ItemInit.blank_page)},
			new ItemStack[] { new ItemStack(ItemInit.blank_magic) }
		));

		// プリズミウム
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(Items.GLOWSTONE_DUST, 4),
			new ItemStack[] { new ItemStack(Items.IRON_NUGGET, 2), new ItemStack(Items.GOLD_NUGGET, 2), new ItemStack(ItemInit.clero_petal, 4)},
			new ItemStack[] { new ItemStack(ItemInit.prizmium) }
		));

		// 重力射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.grav_powder, 3),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.GUNPOWDER, 2),
			new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_ballast) }
		));

		// 重力波魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_ballast),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.grav_powder, 8), new ItemStack(ItemInit.sugarbell, 16)
					, new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_gravitywave) }
		));

		// 重力魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_gravitywave),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.grav_powder, 16), new ItemStack(ItemInit.sugarbell, 32)
					, new ItemStack(ItemInit.mysterious_page, 6), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_gravity_break) }
		));

		// ブラックホール魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_gravity_break),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.grav_powder, 32), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_blackhole) }
		));

		// ジャンプ力アップ魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(Items.FEATHER, 4),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.grav_powder, 3),
			new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_vector_boost) }
		));

		// 弾ベクトル無効化魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_vector_boost),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 4), new ItemStack(ItemInit.grav_powder, 8),
					new ItemStack(ItemInit.clero_petal, 16), new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_vector_halten) }
		));

		// 幻影オオカミ召喚魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.stray_soul, 3),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.ender_shard, 4),
			new ItemStack(ItemInit.moonblossom_petal, 4), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_shadowwolf) }
		));

		// 幻影ゴーレム魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_shadowwolf),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.stray_soul, 8), new ItemStack(ItemInit.ender_shard, 16),
			new ItemStack(ItemInit.mysterious_page, 4)},
			new ItemStack[] { new ItemStack(ItemInit.magic_shadowgolem) }
		));

		// 体力吸収魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.stray_soul, 2),
			new ItemStack[] { new ItemStack(ItemInit.magicmeal, 4), new ItemStack(Items.ENDER_PEARL, 2), new ItemStack(ItemInit.blank_magic, 2), new ItemStack(ItemInit.mysterious_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_absorphealth) }
		));

		// マインドコントロール魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_absorphealth),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(Items.ENDER_PEARL, 4), new ItemStack(ItemInit.ender_shard, 16) },
			new ItemStack[] { new ItemStack(ItemInit.magic_mind_control) }
		));

		// 攻撃力アップ魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_mind_control),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 1), new ItemStack(ItemInit.stray_soul, 16), new ItemStack(ItemInit.ender_shard, 32),
			new ItemStack(ItemInit.mysterious_page, 6), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_shadow) }
		));

		// 反射魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.electronic_orb, 4),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(Items.SLIME_BALL, 4), new ItemStack(ItemInit.sugarbell, 4),
			new ItemStack(ItemInit.moonblossom_petal, 4), new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_elecarmor) }
		));

		// 単体雷魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_elecarmor),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 3), new ItemStack(ItemInit.electronic_orb, 8), new ItemStack(ItemInit.sugarbell, 16),
			new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_lightningbolt) }
		));

		// 雷魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_lightningbolt),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 1), new ItemStack(ItemInit.electronic_orb, 16), new ItemStack(ItemInit.sugarbell, 32),
			new ItemStack(ItemInit.moonblossom_petal, 32), new ItemStack(ItemInit.mysterious_page, 6), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_thunder) }
		));

		// バリア魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(Items.SHIELD),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 4), new ItemStack(Items.IRON_INGOT, 4), new ItemStack(ItemInit.sugarbell, 4),
					new ItemStack(ItemInit.mysterious_page, 2), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_barrier) }
		));

		// バリア + 持続回復魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_barrier),
			new ItemStack[] { new ItemStack(ItemInit.magic_heal), new ItemStack(ItemInit.dm_flower, 16), new ItemStack(ItemInit.prizmium, 16) },
			new ItemStack[] { new ItemStack(ItemInit.magic_regene_shield) }
		));

		// バリア強化 + 持続回復魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_regene_shield),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.dm_flower, 32), new ItemStack(ItemInit.prizmium, 32)
					, new ItemStack(ItemInit.mystical_page, 3)},
			new ItemStack[] { new ItemStack(ItemInit.magic_magia_protection) }
		));

		// 透明魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_barrier),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 3), new ItemStack(ItemInit.mysterious_page, 2), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_invisible) }
		));

		// 無敵魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_invisible),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.witch_tears, 2), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_aether_shield) }
		));

		// 無敵魔法2
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_aether_shield),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.witch_tears, 4), new ItemStack(ItemInit.mystical_page, 5) },
			new ItemStack[] { new ItemStack(ItemInit.magic_aether_shield2) }
		));

		// 泡射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.dm_flower, 8),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 4), new ItemStack(ItemInit.magicmeal, 16), new ItemStack(ItemInit.mystical_page),
					new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_bubleprison) }
		));

		// 泡窒息射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_bubleprison),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.dm_flower, 24), new ItemStack(ItemInit.mystical_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_scumefang) }
		));

		// 泡窒息リジェネ解除射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_scumefang),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal), new ItemStack(ItemInit.dm_flower, 32), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_foamy_hell) }
		));

		// 泡爆発撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_foamy_hell),
			new ItemStack[] { new ItemStack(ItemInit.magic_blast), new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.dm_flower, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_bleb_burst) }
		));

		// 毒射撃魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.poison_bottle, 3),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 4), new ItemStack(ItemInit.sugarbell, 4), new ItemStack(ItemInit.moonblossom_petal, 4),
					new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_rangepoison) }
		));

		// 毒魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_rangepoison),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.poison_bottle, 8), new ItemStack(ItemInit.sugarbell, 16),
					new ItemStack(ItemInit.mysterious_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_poison) }
		));

		// 範囲猛毒魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_poison),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 1), new ItemStack(ItemInit.poison_bottle, 32), new ItemStack(ItemInit.sugarbell, 24),
					new ItemStack(ItemInit.mystical_page, 3), new ItemStack(ItemInit.blank_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_deadly_poison) }
		));

		// デバフ解除魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.dm_flower, 4),
			new ItemStack[] { new ItemStack(ItemInit.sugarbell, 3), new ItemStack(ItemInit.sticky_stuff_petal, 4)
					, new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic) },
			new ItemStack[] { new ItemStack(ItemInit.magic_effectremover) }
		));

		// 範囲デバフ解除魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_effectremover),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 3), new ItemStack(ItemInit.dm_flower, 16) , new ItemStack(ItemInit.mysterious_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_refresh) }
		));

		// 小範囲作物育成魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.sugarbell, 4),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 2), new ItemStack(ItemInit.mysterious_page) , new ItemStack(ItemInit.blank_magic)},
			new ItemStack[] { new ItemStack(ItemInit.magic_growth_effect) }
		));

		// 作物育成魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_growth_effect),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 2), new ItemStack(ItemInit.sugarbell, 16) , new ItemStack(ItemInit.sannyflower_petal, 16)
					 , new ItemStack(ItemInit.mysterious_page, 2) },
			new ItemStack[] { new ItemStack(ItemInit.magic_growth_aura) }
		));

		// 作物広範囲育成魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_growth_aura),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.sugarbell, 32), new ItemStack(ItemInit.mysterious_page, 6), new ItemStack(ItemInit.mystical_page, 6) },
			new ItemStack[] { new ItemStack(ItemInit.magic_growth_verre) }
		));

		// 小爆発魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magicmeal, 16),
			new ItemStack[] { new ItemStack(ItemInit.aether_crystal, 3), new ItemStack(ItemInit.sugarbell, 4), new ItemStack(ItemInit.clero_petal, 4) ,
					new ItemStack(ItemInit.mysterious_page), new ItemStack(ItemInit.blank_magic)},
			new ItemStack[] { new ItemStack(ItemInit.magic_burst) }
		));

		// 爆発魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_burst),
			new ItemStack[] { new ItemStack(ItemInit.divine_crystal, 3), new ItemStack(ItemInit.sugarbell, 16), new ItemStack(ItemInit.clero_petal, 16)
					, new ItemStack(ItemInit.magicmeal, 32)},
			new ItemStack[] { new ItemStack(ItemInit.magic_blast) }
		));

		// 魔法爆発魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_blast),
			new ItemStack[] { new ItemStack(ItemInit.pure_crystal, 2), new ItemStack(ItemInit.sugarbell, 32), new ItemStack(ItemInit.clero_petal, 32)
					, new ItemStack(ItemInit.magicmeal, 64), new ItemStack(ItemInit.mystical_page, 3) },
			new ItemStack[] { new ItemStack(ItemInit.magic_magia_destroy) }
		));

		// 連鎖魔法爆発魔法
		recipe.addRecipe(new ObMagiaRecipes(
			new ItemStack(ItemInit.magic_magia_destroy),
			new ItemStack[] { new ItemStack(ItemInit.cosmic_crystal_shard, 4), new ItemStack(ItemInit.magicmeal, 64), new ItemStack(ItemInit.mystical_page, 4) },
			new ItemStack[] { new ItemStack(ItemInit.magic_supernova) }
		));

		// モブスポーンがfalseなら
		if (!SMConfig.spawn_mob) {

			// 彷徨う魂
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(ItemInit.ender_shard, 8),
				new ItemStack[] { new ItemStack(ItemInit.moonblossom_petal, 8) },
				new ItemStack[] { new ItemStack(ItemInit.stray_soul) }
			));

			// エレクトリックオーブ
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Items.SLIME_BALL, 4),
				new ItemStack[] { new ItemStack(ItemInit.sannyflower_petal, 8) },
				new ItemStack[] { new ItemStack(ItemInit.electronic_orb) }
			));

			// 毒入りビン
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Items.SPIDER_EYE),
				new ItemStack[] { new ItemStack(ItemInit.b_mf_bottle) },
				new ItemStack[] { new ItemStack(ItemInit.poison_bottle) }
			));

			// 解けない氷
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Blocks.ICE, 4),
				new ItemStack[] { new ItemStack(Items.SNOWBALL, 16) },
				new ItemStack[] { new ItemStack(ItemInit.unmeltable_ice) }
			));

			// グラビィティーパウダー
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Items.GUNPOWDER, 4),
				new ItemStack[] { new ItemStack(ItemInit.sugarbell, 8) },
				new ItemStack[] { new ItemStack(ItemInit.grav_powder) }
			));

			// 小さな羽根
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Items.FEATHER, 4),
				new ItemStack[] { new ItemStack(ItemInit.sticky_stuff_petal, 8) },
				new ItemStack[] { new ItemStack(ItemInit.tiny_feather) }
			));

			// 魔女の涙
			recipe.addRecipe(new ObMagiaRecipes(
				new ItemStack(Items.GHAST_TEAR),
				new ItemStack[] { new ItemStack(ItemInit.magicmeal, 16) },
				new ItemStack[] { new ItemStack(ItemInit.witch_tears) }
			));
		}
	}
}
