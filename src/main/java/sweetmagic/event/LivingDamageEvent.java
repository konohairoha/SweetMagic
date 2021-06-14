package sweetmagic.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
		boolean cancelFlag = false;

		// エメラルドピアスの効果
		if (target instanceof EntityPlayer) {
			newDam = this.emelaldPiasEffect((EntityPlayer) target, newDam);
		}

		// えんちちーによる攻撃なら
		if (src.getTrueSource() instanceof EntityLivingBase) {

			EntityLivingBase attacker = (EntityLivingBase) src.getTrueSource();

			// 無敵魔法
			if (target.isPotionActive(PotionInit.aether_shield)) {
				event.setAmount(0);
				event.setCanceled(true);
				return;
			}

			// 回避魔法
			if (target.isPotionActive(PotionInit.cyclone)) {

				this.avoidAtttack(event, target, target.getActivePotionEffect(PotionInit.cyclone).getAmplifier() + 1);

				// 攻撃を無効化されたら終了
				if (event.isCanceled()) { return; }
			}


			// 未来視レベル2以上なら
			if (target.isPotionActive(PotionInit.timestop) && target.getActivePotionEffect(PotionInit.timestop).getAmplifier() >= 1) {

				this.futureVision(event, src, target, attacker, newDam);

				// 攻撃を無効化されたら終了
				if (event.isCanceled()) { return; }
			}

			// 攻撃者が燃焼状態なら攻撃力ダウン
			if (attacker.isPotionActive(PotionInit.flame)) {
				newDam *= 0.75;
			}

			// 火力上昇ならダメージアップ
			if (attacker.isPotionActive(PotionInit.shadow)) {
				newDam = this.lunaticAdd(attacker, newDam);
			}

			// 攻撃されたのがえんちちーかつエレキアーマーかつボスでないなら
			if (target instanceof EntityLivingBase && target.isPotionActive(PotionInit.electric_armor) && attacker.isNonBoss()) {
				newDam = this.getElecArmor(target, attacker, newDam);
			}
		}

		// エーテルバリアーを張っていれば
		if (target.isPotionActive(PotionInit.aether_barrier)) {
			newDam = this.barrierCut(target, newDam);
		}

		ItemStack chest = target.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
		Item chestItem = chest.getItem();

		// ローブを着ていたら
		if (chestItem instanceof IRobe) {
			newDam = this.robwDamageCut(src, target, newDam, chest, chestItem);
		}

		// それ以外が攻撃した場合
		if (!(target instanceof EntityPlayer)) {

			if (!(src.getTrueSource() instanceof EntityLivingBase)) { return; }

			EntityLivingBase entity = (EntityLivingBase) src.getTrueSource();

			// 毒の付与
			if (entity.isPotionActive(PotionInit.grant_poison)) {
				this.addPoison(entity, target);
			}

			// 未来視が付いてたらスタン
			if (entity.isPotionActive(PotionInit.timestop) && target instanceof EntityLiving && src == DamageSource.MAGIC) {
				this.activeFutureVision(entity, (EntityLiving) target);
			}

			// 猛毒が付いてたらダメージ増加
			if (target.isPotionActive(PotionInit.deadly_poison) && !target.isPotionActive(PotionInit.grant_poison)) {
				newDam = this.addPoisonDamage(target, newDam);
			}
		}

		// ダメージが0以下なら攻撃無効
		if (newDam <= 0 && target instanceof EntityPlayer) {
			cancelFlag = true;
		}

		// ダメージ無効フラグがtrueなら
		if (cancelFlag) {
			this.cancelDamage(event, (EntityPlayer) target);
		}

		else {
			event.setAmount(newDam);
		}
	}

	// 回避魔法
	public void avoidAtttack (LivingHurtEvent event, EntityLivingBase entity, int level) {

		float chance = 0F;

		switch (level) {
		case 1:
			chance = 0.01F;
			break;
		case 2:
			chance = 0.03F;
			break;
		case 3:
			chance = 0.05F;
			break;
		}

		if (chance < entity.world.rand.nextFloat()) { return; }

		event.setCanceled(true);
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
	public float lunaticAdd(EntityLivingBase liv, float dame) {
		PotionEffect effect = liv.getActivePotionEffect(PotionInit.shadow);
		float level = Math.min(2.5F, 1F + (effect.getAmplifier() + 1) * 0.1F);
		dame *= level;
		return dame;
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

	public float futureVision (LivingHurtEvent event, DamageSource src, EntityLivingBase target, EntityLivingBase attacker, float newDam) {

		if (src.getTrueSource() instanceof EntityLiving) {

			EntityLiving living = ( EntityLiving) attacker;

			// 未来視を5分減らす
			PotionEffect effect = target.getActivePotionEffect(PotionInit.timestop);
			int level = effect.getAmplifier() + 1;
			int time = effect.getDuration();

			// バフ時間の減少
			this.setPotionTime(target.world, target, effect.getPotion(), level, time, 6000);

			// 敵を動かなくさせる
			EventUtil.tameAIDonmov(living, 6);
			DamageSource damage = SMDamage.MagicDamage(living, target);
			attacker.attackEntityFrom(damage, newDam);

			// ダメージを向こうにしてイベントを無効化
			newDam = 0F;
			event.setAmount(0F);
			event.setCanceled(true);
		}
		return newDam;
	}

	// エーテルバリア
	public float barrierCut(EntityLivingBase liv, float dame) {

		PotionEffect effect = liv.getActivePotionEffect(PotionInit.aether_barrier);
		int level = effect.getAmplifier() + 1;
		int time = effect.getDuration();

		// バフ時間の減少
		this.setPotionTime(liv.world, liv, effect.getPotion(), level, time, (int) (dame * 20));
		dame = dame / level;

		return dame;
	}

	// ダメージ無効処理
	public void cancelDamage(LivingHurtEvent event, EntityPlayer player) {

		// ダメージを向こうにしてイベントを無効化
		event.setAmount(0F);
		event.setCanceled(true);
	}

	// 猛毒追加ダメージ
	public float addPoisonDamage (EntityLivingBase living, float dame) {
		return dame += living.getActivePotionEffect(PotionInit.deadly_poison).getAmplifier() + 2;
	}

	// ローブダメージ軽減
	public float robwDamageCut (DamageSource src, EntityLivingBase living, float dame, ItemStack chest, Item chestItem) {

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
	public void addPoison (EntityLivingBase entity, EntityLivingBase living) {
		if (!living.isPotionActive(PotionInit.grant_poison)) {
			int level = entity.getActivePotionEffect(PotionInit.grant_poison).getAmplifier() + 1;
			NBTTagCompound tags = living.getEntityData();
			level = tags.getBoolean("isCyclone") ? level : ++level;
			living.addPotionEffect(new PotionEffect(PotionInit.deadly_poison, 600, level));
			tags.setBoolean("isCyclone", false);
		}
	}

	// 未来視レベル1
	public void activeFutureVision (EntityLivingBase player, EntityLiving target) {

		// ポーションレベル取得
		int level = player.getActivePotionEffect(PotionInit.timestop).getAmplifier() + 1;

		// レベル0なら
		if (level == 1) {

			// 敵を動かなくさせる
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
}
