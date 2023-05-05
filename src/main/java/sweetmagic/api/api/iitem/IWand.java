package sweetmagic.api.iitem;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.AdvancedInit;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.armor.MagiciansPouch;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.SoundHelper;

public interface IWand extends IMFTool {

	// NBT用の変数
	public static final String SLOT = "slot";				// 選択中のスロット
	public static final String SLOTCOUNT = "slotCount";	// スロットの数
	public static final String EXP = "exp";				// 経験値
	public static final String LEVEL = "level";			// レベル
	public static final String ELEMENT = "element";		// 属性

	/*
	 * =========================================================
	 * 				アクション登録　Start
	 * =========================================================
	 */

	// 右クリックの処理
	ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand);

	// モブに右クリック
	boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand);

	// 地面右クリック
	EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing,float x, float y, float z);

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft);

	/*
	 * =========================================================
	 * 				アクション登録　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				アクション処理　Start
	 * =========================================================
	 */


	// 射撃処理
	default void shotterActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるかチェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
	}

	// 空中処理
	default void airActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるかチェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
	}

	// モブ処理
	default void mobActived (World world, EntityPlayer player, EntityLivingBase entity, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるか事前チェック
		this.setTouchEntity(entity);
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, entity, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
		this.setTouchEntity(null);
	}

	// 地面行動
	default void groundActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるか事前チェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
	}

	// 溜め行動
	default void chargeActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるかチェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
	}

	/*
	 * =========================================================
	 * 				アクション処理　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				キー処理　Start
	 * =========================================================
	 */

	// 次のスロットへ
	default void nextSlot (World world, EntityPlayer player, ItemStack stack) {

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);
		ItemStack slotItem = ItemStack.EMPTY;
		int slotCount = tags.getInteger(SLOTCOUNT);
		int maxCount = 0;

		while (slotItem.isEmpty() && slotCount >= maxCount) {
			int slot = tags.getInteger(SLOT);
			slot = slot >= slotCount - 1 ? 0 : slot + 1;
			tags.setInteger(SLOT, slot);
			slotItem = this.getSlotItem(player, stack, tags);
			maxCount++;
		}

		// クライアント（プレイヤー）へ送りつける
		PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_NEXT, 1F, 0.33F), (EntityPlayerMP) player);
	}

	// 前のスロットへ
	default void backSlot (World world, EntityPlayer player, ItemStack stack) {

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);
		ItemStack slotItem = ItemStack.EMPTY;
		int slotCount = tags.getInteger(SLOTCOUNT);
		int maxCount = 0;

		while (slotItem.isEmpty() && slotCount >= maxCount) {
			int slot = tags.getInteger(SLOT);
			slot = slot <= 0 ? slotCount - 1 : slot - 1;
			tags.setInteger(SLOT, slot);
			slotItem = this.getSlotItem(player, stack, tags);
			maxCount++;
		}

		// クライアント（プレイヤー）へ送りつける
		PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_NEXT, 1F, 0.33F), (EntityPlayerMP) player);
	}

	// GUIを開く
	default void openGui(World world, EntityPlayer player, ItemStack stack) {

		// nbtを取得
		this.getNBT(stack);

		if (!world.isRemote) {
			player.openGui(SweetMagicCore.INSTANCE, SMGuiHandler.SMWAND_GUI, world, 0, -1, -1);
			// クライアント（プレイヤー）へ送りつける
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_PAGE, 1F, 0.33F), (EntityPlayerMP) player);
		}
	}

	/*
	 * =========================================================
	 * 				キー処理　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				魔法発動前処理　Start
	 * =========================================================
	 */

	// クリエワンド以外で魔法が使えるかチェック
	default boolean magicActionBeforeCheck (EntityPlayer player, ItemStack stack, Item item, ISMItem smItem) {

		// MFが消費量を超えてかつtierを満たしてるかどうか
		if (this.isCreativeWand()) {
			return true;
		}

		// MFが足りてるか
		if (!this.canUseMagic(stack, smItem.getUseMF())) {
			return false;
		}

		// tierが杖のほうが大きいか
		if (this.checkOverTier(smItem)) {
			return false;
		}

		// クールタイムがあるなら
		if (player.getCooldownTracker().hasCooldown(item)) {
			return false;
		}

		// 魔法の発動条件を満たしていないなら
		if (!smItem.canItemMagic(player.world, player, stack, this.getNBT(stack))) {
			return false;
		}

		return true;
	}

	// 魔法が必要MFを超えているかどうか
	default boolean canUseMagic (ItemStack stack, int mf) {
		return this.getNBT(stack).getInteger(MF) >= mf;
	}

	// tierが足りているか
	default boolean checkOverTier (ISMItem item) {
		return item.getTier() > this.getTier();
	}

	/*
	 * =========================================================
	 * 				魔法発動前処理　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				魔法発動中処理　Start
	 * =========================================================
	 */

	// 魔法アクション中の処理
	default boolean onAction (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.addWandLevel(world, player, stack, smItem, EnchantInit.wandAddPower);

		tags.setInteger(LEVEL, (level + enchaLevel));
		flag = smItem.onItemAction(world, player, stack, item);
		AdvancedInit.active_magic.triggerFor(player);

		// レベルを戻す
		tags.setInteger(LEVEL, level);

		return flag;
	}

	// 魔法アクション中の処理
	default boolean onAction (World world, EntityPlayer player, EntityLivingBase entity, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.addWandLevel(world, player, stack, smItem, EnchantInit.wandAddPower);

		tags.setInteger(LEVEL, (level + enchaLevel));
		flag = smItem.onItemAction(world, player, entity, stack, item);
		AdvancedInit.active_magic.triggerFor(player);

		// レベルを戻す
		tags.setInteger(LEVEL, level);

		return flag;
	}

	/*
	 * =========================================================
	 * 				魔法発動中処理　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				魔法発動後処理　Start
	 * =========================================================
	 */

	// 魔法アクション後の処理
	default void magicActionAfter (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags, boolean actionFlag) {

		// クリエワンド以外なら
		if (!this.isCreativeWand()) {

			// クールタイムの設定
			player.getCooldownTracker().setCooldown(item, this.getCoolTime(player, stack, smItem.getCoolTime()));

			// 使用した魔法分だけ消費
			this.setMF(stack, this.setMF(player, stack, smItem));

			// アイテムを消費するかどうか
			if (smItem.isShirink() && !player.isCreative()) {
				this.shrinkItem(player, stack, (Item) smItem);
			}

			// actionFlagがtrueならレベルアップチェック
			if (actionFlag && !world.isRemote) {
				this.levelUpCheck(world, player, stack, this.getAddExp(player, smItem));
			}
		}
	}

	//クリエワンドかどうか
	default boolean isCreativeWand () {
		return this.getTier() >= 6;
	}

	// MFを設定
	default int setMF (EntityPlayer player, ItemStack stack, ISMItem smItem) {
		return this.getMF(stack) - this.getCostMF(player, stack, smItem.getUseMF());
	}

	// 消費MF量取得
	default int getCostMF (EntityPlayer player, ItemStack stack, int useMF) {

		int costDown = 0;

		// エンチャ分のMF消費の減少
		costDown += this.getEnchantLevel(EnchantInit.mfCostDown, stack) * 7;

		// アクセサリー分のMF消費量の増減
		costDown += this.acceCostDown(player);

		// バフによるMF消費量の減少
		costDown += player.isPotionActive(PotionInit.mf_down) ? 10 : 0;

		// MF消費量が99%より大きくならないように
		costDown = Math.min(99, costDown);

		if (costDown > 0) {
			useMF *= (100 - costDown) / 100F;
		}

		// 属性一致によるボーナス
		return useMF *= this.getElementMatchDown();
	}

	// クールタイムの取得
	default int getCoolTime (EntityPlayer player, ItemStack stack, int coolTime) {

		if (player.isPotionActive(PotionInit.causality_prediction)) {
			player.removePotionEffect(PotionInit.causality_prediction);
			return 0;
		}

		int coolDown = 0;

		// エンチャ分のクールタイムを減らす
		coolDown += this.getEnchantLevel(EnchantInit.mfCoolTimeDown, stack) * 7;

		// 装備品分
		coolDown += this.acceCoolTime(player);

		// クールタイムが99%より大きくならないように
		coolDown = Math.min(99, coolDown);

		// クールタイム減少が0より大きいなら
		if (coolDown > 0) {
			coolTime *= (100 - coolDown) / 100F;
		}

		// 属性一致によるボーナス
		return coolTime *= this.getElementMatchDown();
	}

	// エンチャレベル取得
	default int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

	// 追加杖レベル
	default int addWandLevel (World world, EntityPlayer player, ItemStack stack, ISMItem smItem, Enchantment enchant) {

		// 射撃魔法以外ならエンチャレベルだけを返す
		int enchaLevel = Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), SMConfig.maxLevel);
		if (smItem.getType() != SMType.SHOTTER) { return enchaLevel; }

		int addPower = 0;
		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return enchaLevel; }

		boolean isFlame = this.isElementEqual(smItem, SMElement.FLAME);
		boolean isFrost = this.isElementEqual(smItem, SMElement.FROST);
		boolean isCyclone = this.isElementEqual(smItem, SMElement.CYCLON);
		boolean isShine = this.isElementEqual(smItem, SMElement.SHINE);

		// インベントリを取得
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();

		// インベントリの分だけ回す
		for (ItemStack st : stackList) {

			// アクセサリの効果が使えるか
			Item item = st.getItem();
			IAcce acce = (IAcce) item;
			if (!acce.canUseEffect(world, player, st)) { continue; }

			// 血吸なら10%を返す
			if (item == ItemInit.blood_sucking_ring) {
				addPower++;
				acce.acceeffect(world, player, st);
			}

			else if (item == ItemInit.unyielding_fire && isFlame) {
				addPower += 15;
				isFlame = false;
			}

			else if (item == ItemInit.frosted_chain && isFrost) {
				acce.acceeffect(world, player, st);
				isFrost = false;
			}

			else if (item == ItemInit.wind_relief && isCyclone) {
				acce.acceeffect(world, player, st);
				isCyclone = false;
			}

			else if (item == ItemInit.wind_relief && isShine) {
				acce.acceeffect(world, player, st);
				isShine = false;
			}
		}

		return addPower + enchaLevel;
	}

	// 属性一致ボーナスの取得
	default float getElementMatchDown () {

		// 属性一致ボーナス取得
		float downTime = this.getBounusValue();

		// クールタイム減少が0以外なら
		if (downTime != 0) {
			return (100F - downTime) * 0.01F;
		}

		return 1F;
	}

	// クールタイム減少時間の値
	default float getBounusValue () {
		return 0;
	}

	// 装備品のクールタイム取得
	default int acceCoolTime (EntityPlayer player) {
		return MagiciansPouch.hasAcce(player, ItemInit.witch_scroll) ? 10 : 0;
	}

	// 装備品のMF消費取得
	default int acceCostDown (EntityPlayer player) {

		int costValue = 0;
		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return costValue; }

		// インベントリを取得
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();

		// インベントリの分だけ回す
		for (ItemStack stack : stackList) {

			Item item = stack.getItem();

			// 魔術師のグローブなら5%増加を返す
			if (item == ItemInit.magicians_grobe) {
				costValue -= 5;
			}
		}

		return costValue;
	}

	// 装備品の経験値追加
	default int getAddExp (EntityPlayer player, ISMItem smItem) {

		int exp = Math.max((int) smItem.getUseMF() / 10, 0) + smItem.addExp();

		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return exp; }

		float addPower = 1F;

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		List<ItemStack> stackList = neo.getStackList();

		// インベントリの分だけ回す
		for (ItemStack stack : stackList) {

			if (stack.getItem() == ItemInit.magicians_grobe) {
				addPower += 0.2F;
			}
		}

		return exp *= addPower;
	}

	// レベルアップできるかどうか
	default void levelUpCheck (World world, EntityPlayer player, ItemStack stack, int addExp) {

		// クリエワンドならレベルアップ処理を行わない
		if(this.isCreativeWand()) { return; }

		NBTTagCompound tags = this.getNBT(stack);
		int level = tags.getInteger(LEVEL);	// レベル
		int exp = tags.getInteger(EXP);		// 経験値
		int maxLevel = this.getMaxLevel();		// 最大レベル
		int nextLevel = 1 + level;				// 次のレベル

		// 最大レベルに達してたら終了
		if (level >= maxLevel) {
			this.checkAdavanced(player, stack);
			return;
		}

		// レベルアップ後に持ち越し用の経験値と必要経験値の取得
		int keepExp = 0;
		int nowNeedExp = this.needExp(maxLevel, nextLevel, stack);

		// 要求経験値を超えた場合に次へ持ち越し
		if (addExp > nowNeedExp) {
			keepExp = addExp - nowNeedExp;
			tags.setInteger(EXP, exp + nowNeedExp);
		}

		else {
			tags.setInteger(EXP, exp += addExp);
		}

		// レベルアップに必要な経験値
		int needExp = this.needExp(maxLevel, nextLevel, stack);

		// 必要経験値を満たしていないなら終了
		if (needExp > 0) { return; }

		int upLevel = ++level;
		tags.setInteger(LEVEL, upLevel);
		tags.setInteger(EXP, needExp);

		this.checkAdavanced(player, stack);

		// クライアント（プレイヤー）へ送りつける
		if (!world.isRemote) {
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_LEVELUP, 1F, 0.2F), (EntityPlayerMP) player);
		}

		// 余った分を再度レベルアップに回す
		if (keepExp > 0) {
			this.levelUpCheck(world, player, stack, keepExp);
		}
	}

	default void checkAdavanced(EntityPlayer player, ItemStack stack) {

		// 杖レベル取得
		int level = this.getNBT(stack).getInteger(LEVEL);
		AdvancedInit.apprentice_magician.triggerLevel(player, 10, level);
		AdvancedInit.intermediate_magician.triggerLevel(player, 20, level);
		AdvancedInit.advanced_magician.triggerLevel(player, 30, level);
		AdvancedInit.maestro_magician.triggerLevel(player, 40, level);
	}

	// 最大レベルの取得
	default int getMaxLevel () {
		return 50;
	}

	// 必要経験値を取得
	default int needExp (int maxLevel, int nextLevel, ItemStack stack) {

		// 必要経験値量 - 取得済みの経験値
		int needExp = this.getNeedExp(nextLevel) - this.getExpValue(stack);
		return (nextLevel - 1) >= maxLevel ? 0 : needExp;
	}

	// 必要経験値を取得
	default int getNeedExp (int nextLevel) {

		int level = nextLevel - 1;								// 今のレベルを取得
		int tierLevel = level % 8 == 0 ? level - 1 : level;	// レベル8用に別の変数に
		int tier = (int) (tierLevel / 8);						// レベル8ごとに振り分け用
		int value = level - ( tier * 8 );						// 0～7に振り分け

		value = value != 0 ? value : 8;			// 0なら8に上げる
		tier = level == 8 ? tier - 1 : tier;	// レベル8ならtierを一つ落とす

		// 経験値の取得
		int exp = value * 120;

		// レベル9以降なら一桁増やす
		if (tier > 0) {
			for (int i = 0; i < tier; i++) {
				exp *= 10;
			}
		}

		return Math.min(exp, 600000);
	}

	/*
	 * =========================================================
	 * 				魔法発動後処理　Start
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				NBT用メソッド　Start
	 * =========================================================
	 */

	// nbt初期化用
	default NBTTagCompound getNBT (ItemStack stack) {

		NBTTagCompound tags = stack.getTagCompound();

		// 初期化
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
			tags = stack.getTagCompound();
		}

		// 選択スロットを持っているかどうか
		if (!tags.hasKey(SLOT)) {
			tags.setInteger(SLOT, 0);
		}

		// スロットを持っているかどうか
		if (!tags.hasKey(SLOTCOUNT)) {
			tags.setInteger(SLOTCOUNT, this.getSlot());
		}

		// 経験値を持っているかどうか
		if (!tags.hasKey(EXP)) {
			tags.setInteger(EXP, 0);
		}

		// レベルを持っているかどうか
		if (!tags.hasKey(LEVEL)) {
			tags.setInteger(LEVEL, 1);
		}

		// MFを持っているかどうか
		if (!tags.hasKey(MF)) {
			tags.setInteger(MF, 0);
		}

		// 属性を持っていないなら
		if (!tags.hasKey(ELEMENT)) {
			tags.setString(ELEMENT, "empty");
		}

		return tags;
	}

  	// レベルを設定
	default int getLevel(ItemStack stack) {
		return this.getNBT(stack).getInteger(LEVEL);
	}

	// 経験値を取得
	default int getExpValue(ItemStack stack) {
    	return this.getNBT(stack).getInteger(EXP);
    }

  	// 経験値を設定
	default void setExpValue (ItemStack stack, int expValue) {
  		this.getNBT(stack).setInteger(EXP, expValue);
  	}

	// スロットの取得
	default int getSlot (ItemStack stack) {
		return this.getNBT(stack).getInteger(SLOTCOUNT);
	}

	// 属性の取得
	default String getElement (ItemStack stack) {
		return this.getNBT(stack).getString(ELEMENT);
	}

	// 属性の設定
	default void setElement (ItemStack stack, String ele) {
		this.getNBT(stack).setString(ELEMENT, ele);
	}

	/*
	 * =========================================================
	 * 				NBT用メソッド　End
	 * =========================================================
	 */

	// 選択しているアイテムを取得
	default ItemStack getSlotItem (EntityPlayer player, ItemStack stack, NBTTagCompound tags, int slot) {
		InventorySMWand inv = new InventorySMWand(stack, player);
		return inv.inventory.getStackInSlot(slot);
	}

	// 選択しているアイテムを取得
	default ItemStack getSlotItem (EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 選択しているスロットを取得
		int slot = tags.getInteger(SLOT);

		// インベントリ取得
		InventorySMWand inv = new InventorySMWand(stack, player);
		return inv.inventory.getStackInSlot(slot);
	}

	// スロット内のアイテムを減らす処理
	default void shrinkItem (EntityPlayer player, ItemStack stack, Item slotItem) {

		// インベントリ取得
		InventorySMWand inv = new InventorySMWand(stack, player);

		// スロットの最大数を取得
		int slotCount = inv.getSlots();

		// インベントリの数だけ回して一致したら減らす
		for (int i = 0; i < slotCount; i++) {

			ItemStack st = inv.getStackInSlot(i);

			if (st.getItem() == slotItem) {
				st.shrink(1);
				inv.writeBack();
				break;
			}
		}
	}

	// クリエパワーを取得
	default float getCreativePower () {
		return 60F;
	}

	//右クリックでチャージした量で射程を伸ばす
	default float getArrowVelocity(int charge, float maxTick) {
		float f = (float) charge / 20F;
		f = (f * f + f * 2F) / 3F;
		return Math.min(f, maxTick);
	}

	// デフォルトの座標設定
	default BlockPos getWandPos () {
		return new BlockPos(0, 0, 0);
	}

	// 属性が空以外かつtier5以上なら
	default boolean isNotElement () {
		return this.getWandElement() != null && this.getTier() >= 5;
	}

	// 魔法と杖の属性一致確認
	default boolean isElementEqual (ISMItem smItem) {
		SMElement wandElemet = this.getWandElement();
		return smItem.getElement() == wandElemet || ( smItem.getSubElement() != null && smItem.getSubElement() == wandElemet );
	}

	// 魔法と杖の属性一致確認
	default boolean isElementEqual (ISMItem smItem, SMElement ele) {
		return smItem.getElement() == ele || ( smItem.getSubElement() != null && smItem.getSubElement() == ele );
	}

	// 装備してるかチェック
	default boolean isEquipment (EntityPlayer player, Item item) {

		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) { return false; }

		// インベントリを取得
		List<ItemStack> stackList = new InventoryPouch(player).getStackList();

		// インベントリの分だけ回す
		for (ItemStack stack : stackList) {

			// アイテム一致してるならtrueを返す
			if (stack.getItem() == item) { return true; }
		}

		return false;
	}

	default List<ItemStack> getMagicList (EntityPlayer player, ItemStack stack) {

		// インベントリ取得
		InventorySMWand inv = new InventorySMWand(stack, player);
		int slotCount = this.getNBT(stack).getInteger(SLOTCOUNT);
		List<ItemStack> stackList = new ArrayList<>();

		for (int i = 0; i < slotCount; i++) {

			// 選択中のアイテムを取得
			ItemStack magic = inv.inventory.getStackInSlot(i);
			if (magic.isEmpty()) { continue; }

			stackList.add(magic);
		}

		return stackList;
	}

	// 次のスロットへ
	default ItemStack getNextStack (World world, EntityPlayer player, ItemStack stack) {

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);
		ItemStack slotItem = ItemStack.EMPTY;
		int slotCount = tags.getInteger(SLOTCOUNT);
		int maxCount = 0;
		int slot = tags.getInteger(SLOT);

		while (slotItem.isEmpty() && slotCount >= maxCount) {
			slot = slot >= slotCount - 1 ? 0 : slot + 1;
			slotItem = this.getSlotItem(player, stack, tags, slot);
			maxCount++;
		}

		return slotItem;
	}

	// 前のスロットへ
	default ItemStack getBackStack (World world, EntityPlayer player, ItemStack stack) {

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);
		ItemStack slotItem = ItemStack.EMPTY;
		int slotCount = tags.getInteger(SLOTCOUNT);
		int maxCount = 0;
		int slot = tags.getInteger(SLOT);

		while (slotItem.isEmpty() && slotCount >= maxCount) {
			slot = slot <= 0 ? slotCount - 1 : slot - 1;
			slotItem = this.getSlotItem(player, stack, tags, slot);
			maxCount++;
		}

		return slotItem;
	}
	/*
	 * =========================================================
	 * 				定義用メソッド　Start
	 * =========================================================
	 */

	// tierを取得
	int getTier ();

	// tierを設定
	void setTier(int tier);

	// 最大MFを取得
	int getMaxMF (ItemStack stack);

	// 最大MFを設定
	void setMaxMF (int maxMF);

	// レベルを設定
	void setLevel(ItemStack stack, int level);

	// スロット数の取得
	int getSlot ();

	// スロット数の設定
	void setSlot(int slot);

	// 溜め時間の受け取り
	float getChargeTick();

	// えんちちーの受け取り
	EntityLivingBase getTouchEntity();

	// えんちちーの設定
	void setTouchEntity(EntityLivingBase entity);

	// 溜め時間の設定
	void setChargeTick(float chargeTick);

	// 杖の属性
	default SMElement getWandElement () {
		return null;
	}

	/*
	 * =========================================================
	 * 				定義用メソッド　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				汎用メソッド　Start
	 * =========================================================
	 */

	// 杖の取得
	public static IWand getWand (ItemStack stack) {
		return (IWand) stack.getItem();
	}

	// レベルの取得
	public static int getLevel (IWand wand, ItemStack stack) {
		return (int) (wand.isCreativeWand() ? wand.getCreativePower() : wand.getLevel(stack));
	}

	// 選択中のアイテム取得
	public static ISMItem getSMItem (EntityPlayer player, ItemStack stack) {

		// 選択しているスロットを取得
		int slot = IWand.getWand(stack).getNBT(stack).getInteger(SLOT);

		// 選択中のアイテムを取得
		return (ISMItem) new InventorySMWand(stack, player).inventory.getStackInSlot(slot).getItem();
	}

	/*
	 * =========================================================
	 * 				汎用メソッド　End
	 * =========================================================
	 */

	/*
	 * =========================================================
	 * 				レンダーメソッド　Start
	 * =========================================================
	 */

	// 杖のレンダー時の設定
	default void renderWand (float scale, EntityLivingBase entity, float parTick) {
		GlStateManager.translate(-0.025F, 0.785F, 0.15F);
		GlStateManager.rotate(0, 1F, 0F, 0F);
		GlStateManager.rotate(0, 0F, 1F, 0F);
		GlStateManager.rotate(135, 0F, 0F, 1F);
		GlStateManager.scale(scale, scale, scale);
	}

	/*
	 * =========================================================
	 * 				レンダーメソッド　Start
	 * =========================================================
	 */

}
