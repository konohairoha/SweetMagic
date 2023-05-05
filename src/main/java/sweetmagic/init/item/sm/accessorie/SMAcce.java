package sweetmagic.init.item.sm.accessorie;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IAcce;
import sweetmagic.api.iitem.IPouch;
import sweetmagic.config.SMConfig;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.eitem.SMAcceType;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.init.tile.inventory.InventoryPouch;
import sweetmagic.util.EventUtil;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.WorldHelper;

public class SMAcce extends SMItem implements IAcce {

	private final int data;
	private final boolean isDup;
	private SMAcceType type;
	private final int tier;
	private final int craftType;

	public SMAcce(String name, SMAcceType type, boolean isDup, int data, int tier) {
		super(name, ItemInit.magicList);
		this.setAcceType(type);
		this.isDup = isDup;
		this.data = data;
		this.tier = tier;
		this.craftType = 0;
		this.setMaxStackSize(1);
	}

	public SMAcce(String name, SMAcceType type, boolean isDup, int data, int tier, int craftType) {
		super(name, ItemInit.magicList);
		this.setAcceType(type);
		this.isDup = isDup;
		this.data = data;
		this.tier = tier;
		this.craftType = craftType;
		this.setMaxStackSize(1);
	}

	/**
	 * 0 = 勇者の腕輪
	 * 1 = ウィッチ・スクロール
	 * 2 = 灼熱の宝玉
	 * 3 = 人魚の衣
	 * 4 = 血吸の指輪
	 * 5 = エメラルドピアス
	 * 6 = フォーチュンリング
	 * 7 = 夜の帳
	 * 8 = 守護のペンダント
	 * 9 = 魔術師のグローブ
	 * 10 = 魔法使いの羽ペン
	 * 11 = アブソープペンダント
	 * 12 = 毒牙
	 * 13 = ペンデュラムネックレス
	 * 14 = 不屈の炎
	 * 15 = フロストチェーン
	 * 16 = ホーリーチャーム
	 * 17 = 風のレリーフ
	 * 18 = エンジェルフリューゲル
	 * 19 = 召喚の手引き書
	 * 20 = 電光のイアリング
	 * 21 = ワイドレンジグローブ
	 * 22 = 機敏な羽根
	 * 23 = 不思議なフォーク
	 * 24 = 拡張の指輪
	 */

	@Override
	public List<String> magicToolTip(List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.warrior_bracelet.name");
			break;
		case 1:
			toolTip.add("tip.witch_scroll.name");
			break;
		case 2:
			toolTip.add("tip.scorching_jewel.name");
			break;
		case 3:
			toolTip.add("tip.mermaid_veil.name");
			break;
		case 4:
			toolTip.add("tip.blood_sucking_ring.name");
			break;
		case 5:
			toolTip.add("tip.emelald_pias.name");
			break;
		case 6:
			toolTip.add("tip.fortune_ring.name");
			break;
		case 7:
			toolTip.add("tip.veil_buff.name");
			break;
		case 8:
			toolTip.add("tip.varrier_pendant.name");
			break;
		case 9:
			toolTip.add("tip.magicians_grobe.name");
			break;
		case 10:
			toolTip.add("tip.magician_quillpen.name");
			break;
		case 11:
			toolTip.add("tip.gravity_pendant.name");
			break;
		case 12:
			toolTip.add("tip.poison_fang.name");
			break;
		case 13:
			toolTip.add("tip.pendulum_necklace.name");
			break;
		case 14:
			toolTip.add("tip.unyielding_fire.name");
			break;
		case 15:
			toolTip.add("tip.frosted_chain.name");
			break;
		case 16:
			toolTip.add("tip.holly_charm.name");
			break;
		case 17:
			toolTip.add("tip.wind_relief.name");
			break;
		case 18:
			toolTip.add("tip.angel_flugel.name");
			break;
		case 19:
			toolTip.add("tip.summon_book.name");
			break;
		case 20:
			toolTip.add("tip.electric_earring.name");
			break;
		case 21:
			toolTip.add("tip.range_glove.name");
			break;
		case 22:
			toolTip.add("tip.prompt_feather.name");
			break;
		case 23:
			toolTip.add("tip.mysterious_fork.name");
			break;
		case 24:
			toolTip.add("tip.extension_ring.name");
			break;
		}

		return toolTip;
	}

	public void debuffRemovalTip(List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("effect.weakness");
			toolTip.add("effect.hunger");
			break;
		case 2:
			toolTip.add("sweetmagic.effect.flame");
			break;
		case 3:
			toolTip.add("sweetmagic.effect.babule");
			break;
		case 6:
			toolTip.add("effect.unluck");
			break;
		case 7:
			toolTip.add("effect.blindness");
			break;
		case 11:
			toolTip.add("sweetmagic.effect.gravity");
			break;
		case 12:
			toolTip.add("effect.poison");
			toolTip.add("sweetmagic.effect.deadly_poison");
			break;
		case 22:
			toolTip.add("potion.effect.slowness");
			break;
		}
	}

	// 効果が発動できるかどうか
	public boolean canUseEffect(World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		// 血吸の指輪
		case 4:
			return player.getHealth() > 1F;
		// 守護のペンダント
		case 8:
			return player.getHealth() <= (player.getMaxHealth() * 0.3F)
					&& !player.getCooldownTracker().hasCooldown(stack.getItem());
		// 電光のイアリング
		case 20:
			return player.ticksExisted % 10 != 0 && !player.getCooldownTracker().hasCooldown(stack.getItem());
		case 23:

			int foodLevel = player.getFoodStats().getFoodLevel();
			return foodLevel >= 20 || foodLevel < 5F;
		}

		return true;
	}

	// 常に発動したいならここで
	public void onUpdate(World world, EntityPlayer player, ItemStack stack) {

		// 発動条件を満たさないなら終了
		if (!this.canUseEffect(world, player, stack)) {
			return;
		}

		switch (this.data) {
		case 0:
			// 弱体化状態なら弱体化を除去
			this.checkDebuf(player, MobEffects.WEAKNESS, MobEffects.HUNGER);
			break;
		case 2:
			// 灼熱の宝玉
			this.scorchEffect(world, player, stack);
			break;
		case 3:
			// 人魚の衣
			this.mermaidEffect(world, player, stack);
			break;
		case 6:
			// フォーチュンリング
			this.addPotion(player, MobEffects.LUCK, 201, 1, false);
			this.checkDebuf(player, MobEffects.UNLUCK);
			break;
		case 7:
			// 夜の帳
			this.addPotion(player, MobEffects.NIGHT_VISION, 201, 0, false);
			this.checkDebuf(player, MobEffects.BLINDNESS);
			break;
		case 8:
			// 守護の守り
			this.addPotion(player, MobEffects.REGENERATION, 300, 1, true);
			this.addPotion(player, MobEffects.RESISTANCE, 600, 4, true);
			player.getCooldownTracker().setCooldown(stack.getItem(), 12000);
			break;
		case 11:
			// アブソープペンダント
			this.gravityEffext(world, player, stack);
			break;
		case 12:
			this.checkDebuf(player, PotionInit.deadly_poison, MobEffects.POISON);
			break;
		case 14:
			// 不屈の炎
			if (player.isBurning()) {
				player.extinguish();
			}

			// やけど状態ならやけどを除去
			this.checkDebuf(player, PotionInit.flame);
			break;
		case 15:
			// フロストチェーン
			this.checkDebuf(player, PotionInit.frosty);
			break;
		case 20:
			// 電光のイアリング
			this.electricEarringEffext(world, player, stack);
			break;
		case 21:
			// ワイドレンジグローブ
			this.addPotion(player, PotionInit.range_glove, 201, 0, false);
			break;
		case 22:
			// 機敏な羽根
			this.addPotion(player, PotionInit.prompt_feather, 201, 0, false);
			this.checkDebuf(player, MobEffects.SLOWNESS);
			break;
		case 23:

			int foodLevel = player.getFoodStats().getFoodLevel();

			if (foodLevel >= 20) {
				this.addPotion(player, MobEffects.RESISTANCE, 201, 2, false);
			}

			else {
				this.addPotion(player, PotionInit.frosty, 201, 1, false);
			}

			break;
		}
	}

	// 灼熱の宝玉
	public boolean scorchEffect(World world, EntityPlayer player, ItemStack stack) {

		this.addPotion(player, MobEffects.FIRE_RESISTANCE, 201, 0, false);
		if (player.isBurning()) {
			player.extinguish();
		}

		// やけど状態ならやけどを除去
		this.checkDebuf(player, PotionInit.flame);

		if (player.isInLava()) {

			player.fallDistance = 0F;

			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825F;
			}

			else if (player.isSneaking()) {
				player.motionY -= 0.04825F;
			}

			else {
				player.motionY *= 0.75F;
			}

			if (world.isRemote && player.ticksExisted % 5 == 0) {

				NBTTagCompound tags = player.getEntityData();
				float lastRotYaw = tags.hasKey("lavalastRotYaw") ? tags.getFloat("lavalastRotYaw") : 0F;

				if (lastRotYaw != 0 && (lastRotYaw > player.prevRenderYawOffset + 5F || lastRotYaw + 5F < player.prevRenderYawOffset) ) {
					float down = 1F - Math.min(1F, ( 1.375F * Math.abs(lastRotYaw - player.prevRenderYawOffset) / 180F));
					player.motionX *= down;
					player.motionZ *= down;
				}

				tags.setFloat("lavalastRotYaw", player.prevRenderYawOffset);
			}

			if (player.moveForward > 0) {

				if (Math.abs(player.motionX) <= 2.25F && Math.abs(player.motionZ) <= 2.25F) {
					float moXZ = 1.875F;
					player.motionX *= moXZ;
					player.motionZ *= moXZ;
				}
			}

			else if (player.moveForward < 0) {
				player.motionX *= 0.85F;
				player.motionZ *= 0.85F;
			}
		}

		return true;
	}

	// 人魚の衣
	public boolean mermaidEffect(World world, EntityPlayer player, ItemStack stack) {

		// 泡状態なら泡を除去
		this.checkDebuf(player, PotionInit.babule);

		if (player.isInWater()) {

			player.setAir(300);
			player.fallDistance = 0F;
			this.addPotion(player, MobEffects.NIGHT_VISION, 201, 0, false);

			if (!player.capabilities.isFlying && SweetMagicCore.proxy.isJumpPressed()) {
				player.motionY += 0.04825F;
			}

			else if (player.isSneaking()) {
				player.motionY -= 0.04825F;
			}

			else {
				player.motionY *= 0.75F;
			}

			if (world.isRemote && player.ticksExisted % 5 == 0) {

				NBTTagCompound tags = player.getEntityData();
				float lastRotYaw = tags.hasKey("lavalastRotYaw") ? tags.getFloat("lavalastRotYaw") : 0F;

				if (lastRotYaw != 0 && (lastRotYaw > player.prevRenderYawOffset + 5F || lastRotYaw + 5F < player.prevRenderYawOffset) ) {
					float down = 1F - Math.min(1F, ( 1.375F * Math.abs(lastRotYaw - player.prevRenderYawOffset) / 180F));
					player.motionX *= down;
					player.motionZ *= down;
				}

				tags.setFloat("lavalastRotYaw", player.prevRenderYawOffset);
			}

			if (player.moveForward > 0) {

				if (Math.abs(player.motionX) <= 2.25F && Math.abs(player.motionZ) <= 2.25F) {
					float moXZ = 1.1875F;
					player.motionX *= moXZ;
					player.motionZ *= moXZ;
				}
			}

			else if (player.moveForward < 0) {
				player.motionX *= 0.85F;
				player.motionZ *= 0.85F;
			}

			if (world.isRemote && !SMConfig.isRender && ( player.motionX != 0F || player.motionZ != 0F ) ) {

				Random rand = world.rand;
				float mY = (float) (player.motionY <= 0F ? 1F : player.motionY);

				for (int i = 0; i < 1; ++i) {
					float f1 = (float) (player.posX - 0.5F + rand.nextFloat() + player.motionX * i / 4.0F);
					float f2 = (float) (player.posY + 0.5F + rand.nextFloat() * 0.5F + mY * i / 4F);
					float f3 = (float) (player.posZ - 0.5F + rand.nextFloat() + player.motionZ * i / 4.0D);
					float y = rand.nextFloat() - rand.nextFloat() + 0.75F;
					world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, f1, f2, f3, 0, y, 0);
				}
			}
		}

		return true;
	}

	// 装備品の効果
	public boolean gravityEffext(World world, EntityPlayer player, ItemStack stack) {

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(8);

		// アイテム吸引
		List<EntityItem> itemList = this.getEntityList(EntityItem.class, player, aabb);
		Random rand = world.rand;
		boolean isRender = !SMConfig.isRender;
		NonNullList<ItemStack> invList = player.inventory.mainInventory;

		if (!itemList.isEmpty()) {
			for (EntityItem item : itemList) {
				if (ItemHelper.hasSpace(invList, item.getItem())) {
					WorldHelper.gravitateEntityTowards(world, rand, item, player.posX, player.posY, player.posZ, true,
							isRender);
				}
			}
		}

		// 経験値吸引
		List<EntityXPOrb> expList = this.getEntityList(EntityXPOrb.class, player, aabb);
		if (!expList.isEmpty()) {
			for (EntityXPOrb xpOrb : expList) {
				WorldHelper.gravitateEntityTowards(world, rand, xpOrb, player.posX, player.posY, player.posZ, false,
						false);
			}
		}

		// 重力状態なら重力解除
		this.checkDebuf(player, PotionInit.gravity);

		return true;
	}

	// 装備品の効果
	public boolean electricEarringEffext(World world, EntityPlayer player, ItemStack stack) {

		boolean checkMob = false;
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(5);

		// アイテム吸引
		List<EntityLiving> entityList = this.getEntityList(EntityLiving.class, player, aabb);

		for (EntityLiving entity : entityList) {

			if ( !(entity instanceof IMob) || !entity.isNonBoss() || entity.getMaxHealth() != entity.getHealth()) { continue; }

			// 3秒間敵を動かなくさせる
			EventUtil.tameAIDonmov(entity, 3);
			checkMob = true;
			world.playSound(null, entity.getPosition(), SMSoundEvent.KNOCKBACK, SoundCategory.HOSTILE, 0.75F, 1.5F);
		}

		// モブ見つかったらクールタイム
		if (checkMob) {
			player.getCooldownTracker().setCooldown(stack.getItem(), 3600);
		}

		return true;
	}

	// 装備品の効果
	public boolean acceeffect(World world, EntityPlayer player, ItemStack stack) {

		switch (this.data) {
		case 0:
			this.addPotion(player, MobEffects.STRENGTH, 800, 2, false);
			this.addPotion(player, MobEffects.RESISTANCE, 800, 2, false);
			this.addPotion(player, PotionInit.drop_increase, 1200, 2, false);
			return true;

		case 4:

			// 血吸の指輪
			float health = player.getHealth();

			// クリエじゃなければ体力を減らす
			if (!player.isCreative()) {
				player.setHealth(health - 0.5F);
			}

			return true;
		case 15:

			// フロストチェーン

			// 範囲内のえんちちーを取得
			AxisAlignedBB aabb = player.getEntityBoundingBox().grow(7.5D, 7.5D, 7.5D);
			List<EntityLivingBase> entityList = world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);

			for (EntityLivingBase entity : entityList) {

				if (!(entity instanceof IMob)) { continue; }

				if (entity.isNonBoss() && entity.isPotionActive(PotionInit.refresh_effect)) {
					entity.removePotionEffect(PotionInit.refresh_effect);
				}

				entity.addPotionEffect(new PotionEffect(PotionInit.frosty, 200, 2));
				world.playSound(null, entity.getPosition(), SMSoundEvent.FROST, SoundCategory.NEUTRAL, 0.25F, 1F);
			}

			return true;

		case 16:

			// ホーリーチャーム
			this.addPotion(player, PotionInit.holly_charm, 600, 0, false);
			return true;
		case 17:

			// 風のレリーフ
			this.addPotion(player, PotionInit.wind_relief, 600, 0, false);
			return true;
		}

		return false;
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

		String tipEntity = "";

		switch (this.data) {
		case 0:
			tipEntity = "entity.braveskeleton.name";
			break;
		case 1:
			tipEntity = "entity.witchmadameverre.name";
			break;
		case 2:
			tipEntity = "entity.ifrite.name";
			break;
		case 3:
			tipEntity = "entity.windine.name";
			break;
		case 8:
			tipEntity = "entity.ancientfairy.name";
			break;
		case 10:
			tipEntity = "entity.sandryon.name";
			break;
		case 18:
			tipEntity = "entity.elsharia_curious.name";
			break;
		case 24:
			tipEntity = "entity.zombiekronos.name";
			break;
		}

		if (!tipEntity.equals("")) {
			tipEntity = new TextComponentTranslation(tipEntity).getFormattedText();
			String tip = new TextComponentTranslation("tip.dropboss.name").getFormattedText() + " " + tipEntity;
			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
		}

		if (this.data == 7) {
			String tip = new TextComponentTranslation("tip.veil_darkness.name").getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
		}
	}

	// デバフチェック
	public void checkDebuf(EntityPlayer player, Potion... potion) {
		for (Potion p : potion) {
			if (player.isPotionActive(p)) {
				player.removePotionEffect(p);
			}
		}
	}

	public void spawnParticle (World world, EntityLivingBase entity, EnumParticleTypes particle, int value) {

		if (!(world instanceof WorldServer)) { return; }

		Random rand = world.rand;
		float mY = (float) (entity.motionY <= 0F ? 1F : entity.motionY);

		for (int i = 0; i < value; ++i) {
			float f1 = (float) (entity.posX - 0.5F + rand.nextFloat() + entity.motionX * i / 4.0F);
			float f2 = (float) (entity.posY + 0.5F + rand.nextFloat() * 0.5F + mY * i / 4F);
			float f3 = (float) (entity.posZ - 0.5F + rand.nextFloat() + entity.motionZ * i / 4.0D);
			float y = rand.nextFloat() - rand.nextFloat() + 0.5F;
				((WorldServer) world).spawnParticle(particle, f1, f2, f3, value, 0F, y, 0F, 0, 0);
		}
	}

	@Override
	public SMAcceType getAcceType() {
		return this.type;
	}

	// アクセサリーの設定
	@Override
	public void setAcceType(SMAcceType type) {
		this.type = type;
	}

	// 効果の重複
	@Override
	public boolean isDuplication() {
		return this.isDup;
	}

	// tierの取得
	@Override
	public int getTier() {
		return this.tier;
	}

	// 装備の作成可能か
	@Override
	public String canCraft() {
		switch(this.craftType) {
		case 0: return "smacc_bag";
		case 1: return "smacc_drop";
		case 2: return "smacc_craft";
		case 3: return "smacc_craft_bag";
		case 4: return "smacc_drop_bag";
		}
		return "";
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		player.setActiveHand(hand);
		ItemStack stack = player.getHeldItem(hand);
		ItemStack leg = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
		if (!(leg.getItem() instanceof IPouch)) {
			return new ActionResult(EnumActionResult.PASS, stack);
		}

		// インベントリを取得
		InventoryPouch neo = new InventoryPouch(player);
		IItemHandlerModifiable inv = neo.inventory;

		// インベントリの分だけ回す
		for (int i = 0; i < inv.getSlots(); i++) {

			// アイテムを取得し空かアクセサリー以外なら次へ
			ItemStack st = inv.getStackInSlot(i);
			if (!st.isEmpty()) {
				continue;
			}

			inv.insertItem(i, stack.copy(), false);
			neo.writeBack();
			stack.shrink(1);
			world.playSound(null, player.getPosition(), SMSoundEvent.ROBE, SoundCategory.NEUTRAL, 0.3F, 1F);
			return new ActionResult(EnumActionResult.SUCCESS, stack);
		}

		return new ActionResult(EnumActionResult.PASS, stack);
	}

	public void addPotion (EntityPlayer player, Potion potion, int time, int level, boolean isRender) {
		player.addPotionEffect(new PotionEffect(potion, time, level, true, isRender));
	}
}
