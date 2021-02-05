package sweetmagic.api.iitem;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
import sweetmagic.handlers.PacketHandler;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.tile.inventory.InventorySMWand;
import sweetmagic.init.tile.magic.TileMFTable;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.packet.TileMFBlockPKT;
import sweetmagic.util.SoundHelper;

public interface IWand {

	// NBT用の変数
	public static final String SLOT = "slot";				// 選択中のスロット
	public static final String SLOTCOUNT = "slotCount";	// スロットの数
	public static final String EXP = "exp";				// 経験値
	public static final String LEVEL = "level";			// レベル
	public static final String MF = "mf";					// MF
	public static final String ELEMENT = "element";		// 属性
	public static final String FAVFLAG = "favoriteFlag";	// お気に入り切り替え用フラグ
	public static final String FAV1 = "favorite_1";		// お気に入り1
	public static final String FAV2 = "favorite_2";		// お気に入り2

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
	 * 				アクション処理　Start
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				アクション登録　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				デフォルトアクション登録　Start
	 * =========================================================
	 */

//	default ActionResult<ItemStack> onItemRightClickWand(World world, EntityPlayer player, EnumHand hand) {
//
//		// アイテムスタックを取得
//		ItemStack stack = player.getHeldItem(hand);
//
//		if (hand == EnumHand.OFF_HAND || stack.isEmpty()) {
//			return new ActionResult(EnumActionResult.PASS, stack);
//		}
//
//		// nbtを取得
//		NBTTagCompound tags = this.getNBT(stack);
//
//		// 選択中のアイテムを取得
//		ItemStack slotItem = this.getSlotItem(player, stack, tags);
//
//		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return new ActionResult(EnumActionResult.PASS, stack); }
//
//		ISMItem item = (ISMItem) slotItem.getItem();
//		this.slotItem = item;
//
//		// 射撃タイプで分別
//		switch (item.getType()) {
//
//		// 射撃タイプ
//		case SHOTTER:
//
//			// 射撃処理
//			this.shotterActived(world, player, stack, slotItem, tags);
//			break;
//
//		// 空中タイプ
//		case AIR:
//
//			// 空中処理
//			this.airActived(world, player, stack, slotItem, tags);
//			break;
//		case CHARGE:
//			player.setActiveHand(hand);
//			break;
//		default:
//			return new ActionResult(EnumActionResult.PASS, stack);
//
//		}
//
//		return new ActionResult(EnumActionResult.SUCCESS, stack);
//	}

	/*
	 * =========================================================
	 * 				デフォルトアクション登録　End
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
	default void mobActived (World world, EntityPlayer player, ItemStack stack, ItemStack slotItem, NBTTagCompound tags) {

		Item item = slotItem.getItem();
		ISMItem smItem = (ISMItem) item;

		// クリエワンド以外で魔法が使えるか事前チェック
		if(!this.magicActionBeforeCheck(player, stack, item, smItem)) { return; }

		// アイテムの処理を実行
		boolean actionFlag = this.onAction(world, player, stack, item, smItem, tags);

		// 魔法アクション後の処理
		this.magicActionAfter(world, player, stack, item, smItem, tags, actionFlag);
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

		NBTTagCompound tags = this.getNBT(stack);	// nbtを取得
		int slotCount = tags.getInteger(SLOTCOUNT);
		int slot = tags.getInteger(SLOT);
		slot = slot >= slotCount - 1 ? 0 : slot + 1;
		tags.setInteger(SLOT, slot);

		// クライアント（プレイヤー）へ送りつける
		PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_NEXT, 1F, 0.33F), (EntityPlayerMP) player);
	}

	// 前のスロットへ
	default void backSlot (World world, EntityPlayer player, ItemStack stack) {

		NBTTagCompound tags = this.getNBT(stack);	// nbtを取得
		int slotCount = tags.getInteger(SLOTCOUNT);
		int slot = tags.getInteger(SLOT);
		slot = slot <= 0 ? slotCount - 1 : slot - 1;
		tags.setInteger(SLOT, slot);

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
	 * 				魔法発動中処理　End
	 * =========================================================
	 */

	// 魔法アクション中の処理
	default boolean onAction (World world, EntityPlayer player, ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags) {

		boolean flag = false;

		// レベルの取得
		int level = this.getLevel(stack);
		int enchaLevel = this.getEnchantLevel(EnchantInit.wandAddPower, stack);

		tags.setInteger(LEVEL, (level + enchaLevel));
		flag = smItem.onItemAction(world, player, stack, item);

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
	default void magicActionAfter (World world, EntityPlayer player,ItemStack stack, Item item, ISMItem smItem, NBTTagCompound tags, boolean actionFlag) {

		// クリエワンド以外なら
		if (!this.isCreativeWand()) {

			// クールタイム
			player.getCooldownTracker().setCooldown(item, this.getCoolTime(stack, smItem.getCoolTime()));

			// 使用した魔法分だけ消費
			this.setMF(stack, this.setMF(stack, smItem));

			// アイテムを消費するかどうか
			if (smItem.isShirink()) {
				this.shrinkItem(player, stack, (Item) smItem);
			}

			// actionFlagがtrueならレベルアップチェック
			if (actionFlag && !world.isRemote) {
				this.levelUpCheck(world, player, stack, Math.max((int) smItem.getUseMF() / 10, 0));
			}
		}
	}

	//クリエワンドかどうか
	default boolean isCreativeWand () {
		return this.getTier() >= 6;
	}

	// MFを設定
	default int setMF (ItemStack stack, ISMItem smItem) {
		int mf = this.getMF(stack) - this.getCostMF(stack, smItem.getUseMF());
		return mf;
	}

	// 消費MF量取得
	default int getCostMF (ItemStack stack, int useMF) {

		int costDown = this.getEnchantLevel(EnchantInit.mfCostDown, stack) * 7;

		if (costDown > 0) {
			useMF *= (100 - costDown) / 100F;
		}

		// クール時間の減少
		useMF *= this.addCoolTimeDown();

		return useMF;
	}

	// クールタイムの取得
	default int getCoolTime (ItemStack stack, int coolTime) {

		int coolDown = this.getEnchantLevel(EnchantInit.mfCoolTimeDown, stack) * 7;

		// クールタイム減少が0より大きいなら
		if (coolDown > 0) {
			coolTime *= (100 - coolDown) / 100F;
		}

		// クール時間の減少
		coolTime *= this.addCoolTimeDown();

		return coolTime;
	}

	default int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

	// downTimeの大きさでクールタイムを減らす
	default float addCoolTimeDown () {

		float coolTimeDown = 1F;
		int downTime = this.getCoolTimeDown();

		if (downTime > 0) {
			coolTimeDown = (100 - downTime) / 100F;
		}

		return coolTimeDown;
	}

	// クールタイム減少時間の値
	default int getCoolTimeDown () {
		return 0;
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
		if (level >= maxLevel) { return; }

		tags.setInteger(EXP, exp += addExp);

		// レベルアップに必要な経験値
		int needExp = this.needExp(maxLevel, nextLevel, stack);

		// 必要経験値を満たしていないなら終了
		if (needExp > 0) { return; }

		int upLevel = ++level;
		tags.setInteger(LEVEL, upLevel);
		tags.setInteger(EXP, needExp);

		// クライアント（プレイヤー）へ送りつける
		if (!world.isRemote) {
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_LEVELUP, 1F, 0.2F), (EntityPlayerMP) player);
		}
	}

	// 最大レベルの取得
	default int getMaxLevel () {
		int maxLevel = 25;
		return maxLevel;
	}

	// 必要経験値を取得
	default int needExp (int maxLevel, int nextLevel, ItemStack stack) {

		// 必要経験値量 - 取得済みの経験値
		int needExp = this.getNeedExp(nextLevel) - this.getExpValue(stack);
		return this.getLevel(stack) >= maxLevel ? 0 : needExp;
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

		return exp;
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

		// お気に入り切り替えフラグ
		if (!tags.hasKey(FAVFLAG)) {
			tags.setBoolean(FAVFLAG, false);
		}

		// お気に入り1
		if (!tags.hasKey(FAV1)) {
			tags.setInteger(FAV1, 0);
		}

		// お気に入り2
		if (!tags.hasKey(FAV2)) {
			tags.setInteger(FAV2, 0);
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

  	// MFを取得
	default int getMF (ItemStack stack) {
    	return this.getNBT(stack).getInteger(MF);
  	}

  	// MFを設定
	default void setMF (ItemStack stack, int expValue) {
  		this.getNBT(stack).setInteger(MF, expValue);
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

	// ゲージ計算取得用
	default int getMfProgressScaled(ItemStack stack, int value) {
		return Math.min(value, (int) (value * this.getMF(stack) / this.getMaxMF(stack)));
	}

	// 選択しているアイテムを取得
	default ItemStack getSlotItem (EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 選択しているスロットを取得
		int slot = tags.getInteger(SLOT);

		// インベントリ取得
		InventorySMWand inv = new InventorySMWand(stack, player);

		// 選択中のアイテムを取得
		ItemStack slotItem = inv.inventory.getStackInSlot(slot);

		return slotItem;
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

	// MFが最大かどうか
	default boolean isMaxMF (ItemStack stack) {
		return this.getMF(stack) >= this.getMaxMF(stack);
	}

	// MFブロックからMFを入れるときの処理
	default void insetMF (ItemStack stack, TileMFTable tile) {

		int mf = this.getMF(stack);
		int useMF = tile.useMF > tile.getMF() ? tile.getMF() : tile.useMF;
		int sumMF = mf + useMF;

		// 合計MFが最大値より少ない場合
		if (sumMF <= this.getMaxMF(stack)) {
			this.setMF(stack, sumMF);
			tile.setMF(tile.getMF() - useMF);
		}

		// 合計MFが最大値を超える場合
		else {

			int insertMF = this.getMaxMF(stack) - mf;
			this.setMF(stack, mf + insertMF);
			tile.setMF(tile.getMF() - insertMF);
		}

		PacketHandler.sendToClient(new TileMFBlockPKT (0, 0, tile.getMF(), tile.getTilePos()));
	}

	default void insetMF (ItemStack stack, int insertMF) {

		int mf = this.getMF(stack);
		int sumMF = insertMF + mf;

		// 合計MFが最大値より少ない場合
		if (sumMF <= this.getMaxMF(stack)) {
			this.setMF(stack, sumMF);
		}

		// 合計MFが最大値を超える場合
		else {

			int setMF = this.getMaxMF(stack) - mf;
			this.setMF(stack, mf + setMF);
		}
	}

	// クリエパワーを取得
	default float getCreativePower () {
		return 25F;
	}

	//右クリックでチャージした量で射程を伸ばす
	default float getArrowVelocity(int charge, float maxTick) {
		float f = (float) charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		return Math.min(f, maxTick);
	}

	// デフォルトの座標設定
	default BlockPos getWandPos () {
		return new BlockPos(0, 0, 0);
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

	// 溜め時間の設定
	void setChargeTick(float chargeTick);

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
		GlStateManager.rotate(-6, 1F, 0F, 0F);
		GlStateManager.rotate(4, 0F, 1F, 0F);
		GlStateManager.rotate(40, 0F, 0F, 1F);
		GlStateManager.scale(scale, -scale, scale);
		GlStateManager.translate(0.5F, -0.75F, 0.34F);
	}

	/*
	 * =========================================================
	 * 				レンダーメソッド　Start
	 * =========================================================
	 */


}
