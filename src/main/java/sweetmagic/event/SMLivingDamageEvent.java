package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.item.sm.armor.MagiciansPouch;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.EventUtil;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.SoundHelper;

public class SMLivingDamageEvent {

	// ダメージを受けたときのイベント
	@SubscribeEvent
	public void onHurt(LivingHurtEvent event) {

		float newDam = event.getAmount();
		if (event.getEntityLiving() == null || newDam <= 0) { return; }

		// 攻撃されたのがえんちちー
		EntityLivingBase target = event.getEntityLiving();

		// ダメージの取得
		DamageSource src = event.getSource();

		// えんちちーによる攻撃なら
		if ( src.getTrueSource() instanceof EntityLivingBase || ( src.getTrueSource() == null && src == DamageSource.MAGIC )) {

			EntityLivingBase entity = src.getTrueSource() != null ? (EntityLivingBase) src.getTrueSource() : null;

			newDam = this.fromEntityDamage(event, entity, target, src, newDam);
			if (event.isCanceled()) { return; }
		}

		// ダメージが0を超えていたら
		if (newDam > 0) {

			// エーテルバリアーを張っていれば
			if (target.isPotionActive(PotionInit.aether_barrier)) {
				newDam = this.barrierCut(target, newDam);
			}

			ItemStack chest = this.getArmor(target, EntityEquipmentSlot.CHEST);
			Item chestItem = chest.getItem();

			// ローブを着ていたら
			if (chestItem instanceof IRobe) {
				newDam = this.robeDamageCut(src, target, newDam, chest, chestItem);
			}

			// 血吸によるHP回復
			if (target.getHealth() <= newDam && src.getTrueSource() instanceof EntityLivingBase && src.getImmediateSource() instanceof EntityBaseMagicShot) {

				EntityLivingBase attacker = (EntityLivingBase) src.getTrueSource();
				ItemStack porch = this.getArmor(attacker, EntityEquipmentSlot.LEGS);
				Item porchItem = porch.getItem();

				if (porchItem instanceof IPouch) {
					this.targetKillEffect(attacker, porch, porchItem);
				}
			}
		}

		event.setAmount(newDam);
	}

	// えんちちーの攻撃
	public float fromEntityDamage (LivingHurtEvent event, EntityLivingBase attacker, EntityLivingBase target, DamageSource src, float newDam) {

		// 無敵魔法
		if (target.isPotionActive(PotionInit.aether_shield)) {
			event.setAmount(0);
			event.setCanceled(true);
			return newDam;
		}

		// 魔法陣
		if (target.isPotionActive(PotionInit.magic_array) && target.ticksExisted <= 60) {
			target.playSound(SoundEvents.ENTITY_BLAZE_HURT, 0.75F, 1F);
			event.setCanceled(true);
			return newDam;
		}

		// 未来視なら
		if (target.isPotionActive(PotionInit.timestop) && attacker != null) {

			// 攻撃を無効化したら終了
			if (this.futureVision(src, target, attacker, newDam)) {
				event.setAmount(0F);
				event.setCanceled(true);
				return 0F;
			}
		}

		// エメラルドピアスの効果
		if (target instanceof EntityPlayer) {
			newDam = this.emelaldPiasEffect((EntityPlayer) target, newDam);
		}

		// 攻撃者が燃焼状態なら攻撃力ダウン
		if (attacker != null && attacker.isPotionActive(PotionInit.flame)) {
			newDam *= 0.75;
		}

		// 火力上昇ならダメージアップ
		if (attacker != null && attacker.isPotionActive(PotionInit.shadow)) {
			newDam = this.lunaticAdd(target, attacker, newDam);
		}

		// 攻撃されたのがえんちちーかつエレキアーマーかつボスでないなら
		if (target instanceof EntityLivingBase && target.isPotionActive(PotionInit.electric_armor) && attacker != null && attacker.isNonBoss()) {
			newDam = this.getElecArmor(target, attacker, newDam);
		}

		// 毒の付与
		if (attacker != null && attacker.isPotionActive(PotionInit.grant_poison) && !target.isPotionActive(PotionInit.grant_poison)) {
			this.addGrantPoison(attacker, target);
		}

		// 猛毒が付いてたらダメージ増加
		if (target.isPotionActive(PotionInit.deadly_poison) && !target.isPotionActive(PotionInit.grant_poison)) {
			newDam = this.addPoisonDamage(target, newDam);
		}

		// 防御力ダウンが付与されているとダメージ増加
		if (target.isPotionActive(PotionInit.armor_break)) {
			newDam *= 1.F + this.getPotionLevel(target, PotionInit.armor_break) * 0.1F;
		}

		// 回避処理
		newDam = this.avoidDamage(target, newDam);

		// 回避出来たら音を鳴らす
		if (newDam <= 0F && target instanceof EntityPlayer) {

			if (this.avoidEffct((EntityPlayer) target) && target instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_AVOID, 0.125F, 1.25F), (EntityPlayerMP) target);
			}
		}

		return newDam;
	}

	// エレキアーマー
	public float getElecArmor (EntityLivingBase target, EntityLivingBase attacker, float dame) {

		// ポーション取得
		PotionEffect effect = target.getActivePotionEffect(PotionInit.electric_armor);
		int level = effect.getAmplifier();

		// 反撃ダメージ
		float counter = dame * (level * 0.2F);
		float health = attacker.getHealth();

		// バフ時間の減少
		this.setPotionTime(target.world, target, effect.getPotion(), level, effect.getDuration(), (int) (dame * 20));

		attacker.setHealth( Math.max(1, health - counter) );
		dame -= 2;

		return dame;
	}

	// ルナティックバフ
	public float lunaticAdd(EntityLivingBase target, EntityLivingBase attacker, float dame) {
		float rate = Math.min(2.5F, 1F + this.getPotionLevel(attacker, PotionInit.shadow) * 0.1F);
		if (!target.isNonBoss()) { Math.max(1.5F, rate); }
		return dame *= rate;
	}

	// エメラルドピアス
	public float emelaldPiasEffect (EntityPlayer player, float dame) {

		if (!(PlayerHelper.getLegItem(player) instanceof IPouch)) { return dame; }

		// インベントリを取得
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();

		// インベントリの分だけ回す
		for (ItemStack stack : stackList) {

			// アクセサリーの取得
			Item item = stack.getItem();
			IAcce acce = (IAcce) item;

			// エメラルドピアスを持ってるならダメージ増加
			if (item == ItemInit.emelald_pias) {

				dame += 0.5F;

				// 重複不可なら終了
				if (!acce.isDuplication()) { return dame; }
			}
		}

		return dame;
	}

	// 未来視による反撃
	public boolean futureVision (DamageSource src, EntityLivingBase target, EntityLivingBase attacker, float newDam) {

		if (attacker instanceof EntityLiving) {

			EntityLiving living = ( EntityLiving) attacker;

			// 未来視を5分減らす
			PotionEffect effect = target.getActivePotionEffect(PotionInit.timestop);
			int level = effect.getAmplifier();
			boolean isFV = level == 0;

			// バフ時間の減少
			this.setPotionTime(target.world, target, effect.getPotion(), level, effect.getDuration(), 6000);

			// 8秒間敵を動かなくさせる
			EventUtil.tameAIDonmov(living, isFV ? 8 : 10);
			DamageSource damage = SMDamage.MagicDamage(living, target);
			attacker.attackEntityFrom(damage, newDam);

			// ボスが攻撃してきたら因果律予測を付与
			if (!isFV && !living.isNonBoss()) {
				PlayerHelper.addPotion(target, PotionInit.causality_prediction, 600, 0, false);
			}

			return true;
		}

		return false;
	}

	// エーテルバリア
	public float barrierCut(EntityLivingBase target, float dame) {

		PotionEffect effect = target.getActivePotionEffect(PotionInit.aether_barrier);
		int level = effect.getAmplifier();

		// バフ時間の減少
		this.setPotionTime(target.world, target, effect.getPotion(), level, effect.getDuration(), (int) (dame * 20));

		return dame / (level + 1);
	}

	// 猛毒追加ダメージ
	public float addPoisonDamage (EntityLivingBase living, float dame) {
		return dame += this.getPotionLevel(living, PotionInit.deadly_poison) + 2;
	}

	// ローブダメージ軽減
	public float robeDamageCut (DamageSource src, EntityLivingBase target, float dame, ItemStack chest, Item chestItem) {

		IRobe robe = (IRobe) chestItem;
		Entity attacker = src.getTrueSource();
		boolean canDMCut = true;

		if (chestItem instanceof IMFTool) {
			IMFTool tool = (IMFTool) chestItem;
			canDMCut = !tool.isEmpty(chest);

			if (canDMCut) {
				tool.setMF(chest, (int) (tool.getMF(chest) - dame));
			}
		}

		// SMMobの攻撃ならダメージカット
		if (attacker instanceof ISMMob && canDMCut) {
			dame *= robe.getSMMobDamageCut();
		}

		// プレイヤーによる攻撃ならダメージを固定
		else if (attacker instanceof EntityPlayer && canDMCut) {
			dame = 0.1F;
		}

		// 魔法ダメージならダメージカット
		if ( (src.getImmediateSource() instanceof EntityBaseMagicShot || src == DamageSource.MAGIC ) && canDMCut) {
			dame *= robe.getMagicDamageCut();
		}

		return dame;
	}

	// 毒の付与
	public void addGrantPoison (EntityLivingBase attacker, EntityLivingBase target) {
		if ( !(attacker instanceof EntityPlayer && target instanceof EntityPlayer) ) {
			int level = this.getPotionLevel(attacker, PotionInit.grant_poison) + 1;
			target.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, 600, level));
		}
	}

	// 敵を倒したら
	public void targetKillEffect (EntityLivingBase entity, ItemStack porch, Item porchItem) {

		if (entity.getHealth() >= 0.25D && entity instanceof EntityPlayer) {

			// インベントリを取得
			float healHealth = 0F;
			EntityPlayer player = (EntityPlayer) entity;
			List<ItemStack> stackList = new InventoryPouch(player).getStackList();

			// インベントリの分だけ回す
			for (ItemStack stack : stackList) {

				// アクセサリーの取得
				Item item = stack.getItem();

				// 血吸の指輪なら体力回復
				if (item == ItemInit.blood_sucking_ring) {
					healHealth += 0.25F;
				}

				// 勇者の腕輪なら
				else if (item == ItemInit.warrior_bracelet) {
					((IAcce) item).acceeffect(player.world, player, stack);
				}
			}

			entity.heal(healHealth);
		}
	}

	public float avoidDamage (EntityLivingBase entity, float damage) {

		// 風のレリーフなら回避
		if (entity.isPotionActive(PotionInit.wind_relief)) {
			entity.removeActivePotionEffect(PotionInit.wind_relief);
			return 0F;
		}

		float chance = 0F;

		// アヴォイドなら確立回避
		if (entity.isPotionActive(PotionInit.cyclone)) {
			chance += ( 0.05F + this.getPotionLevel(entity, PotionInit.cyclone) * 0.05F );
		}

		// 幸運なら確立回避
		if (entity.isPotionActive(MobEffects.LUCK)) {
			chance += ( 0.01F + this.getPotionLevel(entity, MobEffects.LUCK) * 0.01F );
		}

		// 因果律予測なら確立上昇
		if (entity.isPotionActive(PotionInit.causality_prediction)) {
			chance += 0.1F;
		}

		// レギンスの取得
		ItemStack legs = this.getArmor(entity, EntityEquipmentSlot.LEGS);

		if (legs.getItem() instanceof IPouch && entity instanceof EntityPlayer) {

			List<Item> blackList = new ArrayList<>();

			// インベントリを取得
			List<ItemStack> stackList = new InventoryPouch((EntityPlayer) entity).getStackList();

			// インベントリの分だけ回す
			for (ItemStack stack : stackList) {

				Item item = stack.getItem();
				if (blackList.contains(item)) { continue; }

				blackList.add(item);

				// 毒の牙なら確立回避
				if (item == ItemInit.poison_fang) {
					chance += ( 0.05F + 0.1F * ( 1F - Math.max(0.5F, entity.getHealth() / entity.getHealth()) ) );
					break;
				}

				// エンジェルフリューゲルの確立追加
				else if (item == ItemInit.angel_flugel) {
					chance += 0.03F;
					break;
				}
			}
		}

		return chance >= entity.world.rand.nextFloat() ? 0 : damage;
	}

	public boolean avoidEffct (EntityPlayer entity) {

		if (MagiciansPouch.hasAcce(entity, ItemInit.angel_flugel)) {
			PlayerHelper.addPotion(entity, MobEffects.REGENERATION, 60, 0, false);
			PlayerHelper.addPotion(entity, MobEffects.STRENGTH, 400, 3, false);
			PlayerHelper.addPotion(entity, PotionInit.mf_down, 400, 1, false);
			return true;
		}

		return false;
	}

	// バフ時間の設定
	public void setPotionTime (World world, EntityLivingBase entity, Potion potion, int level, int time, int decre) {

		entity.removePotionEffect(potion);

		if (entity instanceof EntityPlayer) {
			boolean hasAcce = MagiciansPouch.hasAcce((EntityPlayer) entity, ItemInit.magician_quillpen);
			decre *= hasAcce ? 0.25F : 1F;;
		}

		time -= decre;

		// 時間が切れたら
		if (time <= 0) {

			// クライアント（プレイヤー）へ送りつける
			if (entity instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_BREAK, 1F, 1F), (EntityPlayerMP) entity);
			}
		}

		// 時間が残ってるなら
		else {
			PlayerHelper.addPotion(entity, potion, time, level, false);
		}
	}

	// ポーションレベル取得
	public int getPotionLevel (EntityLivingBase entity, Potion potion) {
		return entity.getActivePotionEffect(potion).getAmplifier();
	}

	public ItemStack getArmor (EntityLivingBase entity, EntityEquipmentSlot slot) {
		return entity.getItemStackFromSlot(slot);
	}
}
