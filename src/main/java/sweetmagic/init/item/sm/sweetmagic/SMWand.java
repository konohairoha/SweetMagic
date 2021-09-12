package sweetmagic.init.item.sm.sweetmagic;

import com.google.common.collect.Multimap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.enchant.EnchantWand;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMUtil;

public class SMWand extends SMItem implements IWand {

	// 変数
	public int tier;
	public int maxMF;
	public int slot;
	public int level;
	public ISMItem slotItem;
	public float chargeTick = 0;
	public EntityLivingBase entity;
	public BlockPos wandPos = new BlockPos(0, 0, 0);

	public SMWand (String name, int tier, int maxMF, int slot) {
		super(name, ItemInit.magicList);
		this.setTier(tier);
		this.setMaxMF(maxMF);
		this.setSlot(slot);
		this.setMaxStackSize(1);
		this.setMaxDamage(9999);
	}

	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			String dameName = SharedMonsterAttributes.ATTACK_DAMAGE.getName();
			String weapon = "modifier";
			double dama = (double) this.tier * 0.5D;
			map.removeAll(dameName);
			map.put(dameName, new AttributeModifier(ATTACK_SPEED_MODIFIER, weapon, dama, 0));
			map.put(EntityPlayer.REACH_DISTANCE.getName(), new AttributeModifier(SMUtil.TOOL_REACH, weapon, dama * 2, 0));
		}
        return map;
    }

	// 敵に攻撃したらノックバック
	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {

		// 吹き飛ばし耐性付いてるならノックバックしない
		if (target.isPotionActive(PotionInit.resistance_blow)) { return true; }

		// 魔法火力アップ分ノックバック距離増加
        Vec3d r = attacker.getLookVec().normalize().scale(4);
		double d = Math.min(0.1 + (double) this.getEnchantLevel(EnchantInit.wandAddPower, stack) * 0.075D, 0.5D);
		target.motionX += r.x * d;
		target.motionZ += r.z * d;
		ParticleHelper.spawnBoneMeal(target.world, target.getPosition().up(), EnumParticleTypes.CRIT);
		return true;
	}

	/*
	 * =========================================================
	 * 				アクション登録　Start
	 * =========================================================
	 */

	// 右クリック
	@Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// アイテムスタックを取得
		ItemStack stack = player.getHeldItem(hand);

		if (hand == EnumHand.OFF_HAND || stack.isEmpty()) {
			return new ActionResult(EnumActionResult.PASS, stack);
		}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return new ActionResult(EnumActionResult.PASS, stack); }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃タイプで分別
		switch (item.getType()) {

		// 射撃タイプ
		case SHOTTER:

			// 射撃処理
			this.shotterActived(world, player, stack, slotItem, tags);
			break;

		// 空中タイプ
		case AIR:

			// 空中処理
			this.airActived(world, player, stack, slotItem, tags);
			break;
		case CHARGE:
			player.setActiveHand(hand);
			break;
		default:
			return new ActionResult(EnumActionResult.PASS, stack);

		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	// モブに右クリック
	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		ItemStack st = player.getHeldItemMainhand();

		if (st.isEmpty()) { return false; }

		NBTTagCompound tags = this.getNBT(stack);	// nbtを取得

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return false; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		// 射撃タイプで分別
		switch (item.getType()) {

		// モブタイプ
		case MOB:
			this.mobActived(player.world, player, entity, stack, slotItem, tags);
			break;
		default:
			return false;
		}
		return true;
	}

	// 地面右クリック
	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		if (player.getHeldItemMainhand().isEmpty()) { return EnumActionResult.PASS;}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return EnumActionResult.PASS; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;
		this.wandPos = pos;

		// 射撃タイプで分別
		switch (item.getType()) {

		// 地面タイプ
		case GROUND:
			this.groundActived(world, player, stack, slotItem, tags);
			break;
		default:
			return EnumActionResult.PASS;
		}

		return EnumActionResult.SUCCESS;
	}

	//右クリックチャージをやめたときに矢を消費せずに矢を射る
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase living, int timeLeft) {

		EntityPlayer player = (EntityPlayer) living;

		// アイテムスタックを取得
		ItemStack st = player.getHeldItemMainhand();
		if (st.isEmpty()) { return;}

		// nbtを取得
		NBTTagCompound tags = this.getNBT(stack);

		// 選択中のアイテムを取得
		ItemStack slotItem = this.getSlotItem(player, stack, tags);

		if (slotItem.isEmpty() || !(slotItem.getItem() instanceof ISMItem)) { return; }

		ISMItem item = (ISMItem) slotItem.getItem();
		this.slotItem = item;

		int i = this.getMaxItemUseDuration(stack) - timeLeft;
		this.setChargeTick(this.getArrowVelocity(i, 1F));

		// 射撃タイプで分別
		switch (item.getType()) {

		// 地面タイプ
		case CHARGE:
			this.chargeActived(world, player, stack, slotItem, tags);
			break;
		default:
			return;
		}
	}

	/*
	 * =========================================================
	 * 				アクション登録　End
	 * =========================================================
	 */


	/*
	 * =========================================================
	 * 				以下インターフェース必要メソッド
	 * =========================================================
	 */


	// tierの取得
	@Override
	public int getTier () {
		return this.tier;
	}

	// tierの設定
	@Override
	public void setTier (int tier) {
		this.tier = tier;
	}

	// レベルを取得
	@Override
	public void setLevel(ItemStack stack, int level) {
		this.level = level;
	}

  	// 最大MFを取得
	@Override
  	public int getMaxMF (ItemStack stack) {
		int addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 5) * (this.maxMF / 100);
  		return this.maxMF + addMaxMF;
  	}

  	// 最大MFを設定
  	public void setMaxMF (int maxMF) {
  		this.maxMF = maxMF;
  	}

  	// スロット数の取得
	@Override
	public int getSlot() {
		return this.slot;
	}

	// スロット数の設定
	public void setSlot(int slot) {
		this.slot = slot;
	}

	// 溜める時間の取得
	@Override
	public float getChargeTick() {
		return this.chargeTick;
	}

	// 溜める時間の設定
	@Override
	public void setChargeTick(float chargeTick) {
		this.chargeTick = chargeTick;
	}

	// えんちちーの取得
	@Override
	public EntityLivingBase getTouchEntity() {
		return this.entity == null ? null : this.entity;
	}

	// えんちちーの設定
	@Override
	public void setTouchEntity(EntityLivingBase entity) {
		this.entity = entity;
	}

	// 最大１分間出来るように
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {

		int chargeTime = 0;
		if (this.slotItem != null && this.slotItem.getType() == SMType.CHARGE) {
			chargeTime = 72000;
		}
		return chargeTime;
	}

	// 右クリックをした際の挙動を弓に
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		if (this.slotItem != null && this.slotItem.getType() == SMType.CHARGE) {
			return EnumAction.BOW;
		}
		return EnumAction.NONE;
	}

	// エンチャントエフェクト描画
	@Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
	    return this.getLevel(stack) >= this.getMaxLevel();
    }

	@Override
	public BlockPos getWandPos () {
		return this.wandPos;
	}

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) {
		return;
	}

	//壊すブロックの採掘速度を変更
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return super.getDestroySpeed(stack, state) * (0.6F + this.tier * 0.2F);
	}

	//全てのブロック（マテリアル）を破壊可能に
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}

	@Override
	public int getItemEnchantability() {
		return 5;
	}

	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchant) {
		return enchant.type == EnchantWand.type;
	}

	public boolean canEnchantItem(Item item) {
		return true;
	}

	// 常時稼働メソッド
    @Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int off, boolean main) {

    	// プレイヤーじゃないなら
    	if (!(entity instanceof EntityPlayer)) { return; }

    	// メインハンドで持ってるアイテムを取得
		EntityPlayer player = (EntityPlayer) entity;
		ItemStack item = player.getHeldItemMainhand();
    	if (item == null || item.isEmpty() || !(item.getItem() instanceof IWand)) { return; }

    	// 杖が選択してる魔法の取得
    	IWand wand = (IWand) item.getItem();
    	ItemStack magic = this.getSlotItem(player, stack, wand.getNBT(stack));
    	if (magic == null || magic.isEmpty()) { return; }

    	// onUpdateメソッドの呼び出し
    	ISMItem smItem = (ISMItem) magic.getItem();
    	smItem.onUpdate(world, player, item);
    }
}
