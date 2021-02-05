package sweetmagic.event;

import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.api.iitem.IRobe;
import sweetmagic.config.SMConfig;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.util.EventUtil;
import sweetmagic.util.SMDamage;

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

		// えんちちーによる攻撃なら
		if (src.getTrueSource() instanceof EntityLivingBase) {

			EntityLivingBase attacker = (EntityLivingBase) src.getTrueSource();

			// 未来視レベル2以上なら
			if (target.isPotionActive(PotionInit.timestop) && target.getActivePotionEffect(PotionInit.timestop).getAmplifier() >= 1) {

				if (src.getTrueSource() instanceof EntityLiving) {

					EntityLiving living = ( EntityLiving) attacker;
					target.removePotionEffect(PotionInit.timestop);

					// 敵を動かなくさせる
					EventUtil.tameAIDonmov(living, 6);
					DamageSource damage = SMDamage.MagicDamage(living, target);
					attacker.attackEntityFrom(damage, newDam);

					// ダメージを向こうにしてイベントを無効化
					event.setAmount(0F);
					event.setCanceled(true);
				}
			}

			// 燃焼なら
			if (attacker.isPotionActive(PotionInit.flame)) {
				newDam *= 0.75;
			}

			// 火力上昇なら
			if (attacker.isPotionActive(PotionInit.shadow)) {
				newDam = this.lunaticAdd(attacker, newDam);
			}

			// 攻撃されたのがプレイヤーの場合
			if (target instanceof EntityLivingBase && target.isPotionActive(PotionInit.electric_armor)) {
				newDam = this.getElecArmor(target, attacker, newDam);
			}
		}


		// エーテルバリアーを張っていれば
		if (target.isPotionActive(PotionInit.aether_barrier)) {
			newDam = this.barrierCut(target, newDam);
		}

		// ローブを着ていたら
		if (target.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof IRobe) {

			IRobe robe = (IRobe) target.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem();
			Entity attacker = src.getTrueSource();

			// SMMobの攻撃ならダメージカット
			if (attacker instanceof ISMMob) {
				newDam *= robe.getSMMobDamageCut();
			}

			// プレイヤーによる攻撃ならダメージを固定
			else if (attacker instanceof EntityPlayer) {
				newDam = 0.13F;
			}

			// 魔法ダメージならダメージカット
			if (src.getImmediateSource() instanceof EntityBaseMagicShot || src == DamageSource.MAGIC) {
				newDam *= robe.getMagicDamageCut();
			}
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

			// 猛毒追加ダメージ
			newDam = this.addPoisonDamage(target, newDam);
		}

		// ダメージが0以下なら攻撃無効
		if (newDam <= 0 && target instanceof EntityPlayer) {
			cancelFlag = true;
		}

		// ダメージ無効フラグがtrueなら
		if (cancelFlag) {
			this.cancelDamage(event, (EntityPlayer) target);
		} else {
			event.setAmount(newDam);
		}

		// モブドロップ
		this.addDrop(event, target, src);
	}

	// エレキアーマー
	public float getElecArmor (EntityLivingBase entity, EntityLivingBase liv, float dame) {

		// ポーション取得
		PotionEffect effect = entity.getActivePotionEffect(PotionInit.electric_armor);
		int level = effect.getAmplifier() + 1;

		// 反撃ダメージ
		float counter = dame * (level / 5);
		float health = liv.getHealth();

		liv.setHealth( (health - counter) <= 0 ? 1 : health - counter );
		dame -= 2;

		return dame;
	}

	// ルナティックバフ
	public float lunaticAdd(EntityLivingBase liv, float dame) {
		PotionEffect effect = liv.getActivePotionEffect(PotionInit.shadow);
		float level = 1F + (effect.getAmplifier() + 1) * 0.1F;
		dame *= level;
		return dame;
	}

	// エーテルバリア
	public float barrierCut(EntityLivingBase liv, float dame) {
		PotionEffect effect = liv.getActivePotionEffect(PotionInit.aether_barrier);
		int level = effect.getAmplifier() + 1;
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

		// 猛毒が付いてたらダメージ増加
		if (living.isPotionActive(PotionInit.deadly_poison) && !living.isPotionActive(PotionInit.grant_poison)) {

			// ポーションの取得
			PotionEffect effect = living.getActivePotionEffect(PotionInit.deadly_poison);
			dame += effect.getAmplifier() + 2;
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

	public void addDrop (LivingHurtEvent event, EntityLivingBase living, DamageSource src) {

		if (living == null || living.world.isRemote) { return; }

		Entity entity = src.getTrueSource(); 	// ダメージを与えたEntityの種類
		float dam = event.getAmount();						// ダメージ量
		if (entity == null || !(entity instanceof EntityPlayer)) { return; }

		World world = living.world;
		Random rand = world.rand;
		double x = living.posX;
		double y = living.posY;
		double z = living.posZ;

		if(SMConfig.mobdrop_crystal && rand.nextInt(8) == 0) {

			//与えたダメージ量がのこり体力をオーバーしていれば
			if (this.attackDeath(dam, living)) {
				this.spawnItem(world, x, y, z, ItemInit.aether_crystal, rand.nextInt(2) + 1);
			}
		}

		//ウィッチが不思議なページを落とす instanceof EntityPlayerを入れるとプレイヤー本人によるキルかどうかを判断可能　見た感じ射撃(EntityArrowとか)も可能？
		if (living instanceof EntityWitch && rand.nextBoolean()) {
			if (this.attackDeath(dam, living)) {
				this.spawnItem(world, x, y, z, ItemInit.mysterious_page, rand.nextInt(2) + 1);
			}
		}

		// スカルフレイムを倒すとファイアナスタチウムを落とす
		else if (living.getName().equals("スカル・フレイム") || living.getName().equals("skullflame")) {
			if (!rand.nextBoolean()) { return; }
			if (this.attackDeath(dam, living)) {
				this.spawnItem(world, x, y, z, ItemInit.fire_nasturtium_petal, rand.nextInt(2) + 1);
			}
		}

		// ニワトリなら
		else if (living instanceof EntityChicken) {
			if (this.attackDeath(dam, living)) {
				this.spawnItem(world, x, y, z, Items.FEATHER, rand.nextInt(2) + 1);
			}
		}
	}

	// 与えたダメージ量がのこり体力をオーバーしていれば
	public boolean attackDeath (float damage, EntityLivingBase living) {
		return damage >= living.getHealth();
	}

	// アイテムスポーン
	public void spawnItem (World world, double x, double y, double z, Item item, int count) {
		world.spawnEntity(new EntityItem(world, x, y, z, new ItemStack(item, count)));
	}

}
