package sweetmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.EventUtil;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.SoundHelper;

public class LivingDamageEvent {

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
		if (src.getTrueSource() instanceof EntityLivingBase) {
			newDam = this.fromEntityDamage(event, (EntityLivingBase) src.getTrueSource(), target, src, newDam);
			if (event.isCanceled()) { return; }
		}

		// エーテルバリアーを張っていれば
		if (target.isPotionActive(PotionInit.aether_barrier)) {
			newDam = this.barrierCut(target, newDam);
		}

		ItemStack chest = target.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		Item chestItem = chest.getItem();

		// ローブを着ていたら
		if (chestItem instanceof IRobe) {
			newDam = this.robeDamageCut(src, target, newDam, chest, chestItem);
		}

		event.setAmount(newDam);
	}

	// えんちちーの攻撃
	public float fromEntityDamage (LivingHurtEvent event, EntityLivingBase attacker, EntityLivingBase target, DamageSource src, float newDam) {

		// エメラルドピアスの効果
		if (target instanceof EntityPlayer) {
			newDam = this.emelaldPiasEffect((EntityPlayer) target, newDam);
		}

		// 無敵魔法
		if (target.isPotionActive(PotionInit.aether_shield)) {
			event.setAmount(0);
			event.setCanceled(true);
			return newDam;
		}

		// 未来視レベル2以上なら
		if (target.isPotionActive(PotionInit.timestop) && this.getPotionLevel(target, PotionInit.timestop) >= 1) {

			// 攻撃を無効化したら終了
			if (this.futureVision(src, target, attacker, newDam)) {
				event.setAmount(0F);
				event.setCanceled(true);
				return 0F;
			}
		}

		// 攻撃者が燃焼状態なら攻撃力ダウン
		if (attacker.isPotionActive(PotionInit.flame)) {
			newDam *= 0.75;
		}

		// 火力上昇ならダメージアップ
		if (attacker.isPotionActive(PotionInit.shadow)) {
			newDam = this.lunaticAdd(target, attacker, newDam);
		}

		// 攻撃されたのがえんちちーかつエレキアーマーかつボスでないなら
		if (target instanceof EntityLivingBase && target.isPotionActive(PotionInit.electric_armor) && attacker.isNonBoss()) {
			newDam = this.getElecArmor(target, attacker, newDam);
		}

		// 毒の付与
		if (attacker.isPotionActive(PotionInit.grant_poison) && !target.isPotionActive(PotionInit.grant_poison)) {
			this.addGrantPoison(attacker, target);
		}

		// 未来視が付いてたらスタン
		if (attacker.isPotionActive(PotionInit.timestop) && target instanceof EntityLiving && src instanceof SMDamage) {
			this.activeFutureVision(attacker, (EntityLiving) target);
		}

		// 猛毒が付いてたらダメージ増加
		if (target.isPotionActive(PotionInit.deadly_poison) && !target.isPotionActive(PotionInit.grant_poison)) {
			newDam = this.addPoisonDamage(target, newDam);
		}

		// 回避魔法
		if (target.isPotionActive(PotionInit.cyclone)) {

			// 攻撃向こうかなら終了
			if(this.avoidAtttack(target, this.getPotionLevel(target, PotionInit.cyclone))) {
				event.setAmount(0);
				return 0F;
			}
		}

		return newDam;
	}

	// 回避魔法
	public boolean avoidAtttack (EntityLivingBase entity, int level) {
		return ( 0.01F + level * 0.02F ) >= entity.world.rand.nextFloat();
	}

	// エレキアーマー
	public float getElecArmor (EntityLivingBase entity, EntityLivingBase liv, float dame) {

		// ポーション取得
		PotionEffect effect = entity.getActivePotionEffect(PotionInit.electric_armor);
		int level = effect.getAmplifier() + 1;
		int time = effect.getDuration();

		// 反撃ダメージ
		float counter = dame * (level * 0.2F);
		float health = liv.getHealth();

		// バフ時間の減少
		this.setPotionTime(entity.world, entity, effect.getPotion(), level, time, (int) (dame * 20));

		liv.setHealth( Math.max(1, health - counter) );
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

		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return dame; }

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		IItemHandlerModifiable inv = neo.inventory;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

			Item item = st.getItem();
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
			int level = effect.getAmplifier() + 1;
			int time = effect.getDuration();

			// バフ時間の減少
			this.setPotionTime(target.world, target, effect.getPotion(), level, time, 6000);

			// 8秒間敵を動かなくさせる
			EventUtil.tameAIDonmov(living, 8);
			DamageSource damage = SMDamage.MagicDamage(living, target);
			attacker.attackEntityFrom(damage, newDam);
			return true;
		}

		return false;
	}

	// エーテルバリア
	public float barrierCut(EntityLivingBase liv, float dame) {

		PotionEffect effect = liv.getActivePotionEffect(PotionInit.aether_barrier);
		int level = effect.getAmplifier() + 1;
		int time = effect.getDuration();

		// バフ時間の減少
		this.setPotionTime(liv.world, liv, effect.getPotion(), level, time, (int) (dame * 20));

		return dame / level;
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

	// 未来視レベル0なら敵を動かなくさせる
	public void activeFutureVision (EntityLivingBase attacker, EntityLiving target) {
		if (this.getPotionLevel(attacker, PotionInit.timestop) == 0) {
			EventUtil.tameAIDonmov(target, 1);
		}
	}

	// バフ時間の設定
	public void setPotionTime (World world, EntityLivingBase entity, Potion potion, int level, int time, int decre) {

		entity.removePotionEffect(potion);

		// レギンスの取得
		ItemStack legs = entity.getItemStackFromSlot(EntityEquipmentSlot.LEGS);

		if (legs.getItem() instanceof IPouch && entity instanceof EntityPlayer) {

			// インベントリを取得
			EntityPlayer player = (EntityPlayer) entity;
			InventoryPouch neo = new InventoryPouch(player);
			IItemHandlerModifiable inv = neo.inventory;

			// インベントリの分だけ回す
			for (int i = 0; i < inv.getSlots(); i++) {

				// アイテムを取得し空かアクセサリー以外なら次へ
				ItemStack st = inv.getStackInSlot(i);
				if (st.isEmpty() || !(st.getItem() instanceof IAcce)) { continue; }

				// アクセサリーの取得
				Item item = st.getItem();

				// 魔法使いの羽ペンなら減少時間を半分に
				if (item == ItemInit.magician_quillpen) {
					decre *= 0.25;
					break;
				}
			}
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
}
