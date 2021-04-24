package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.entity.projectile.EntityBabuleMagic;
import sweetmagic.init.entity.projectile.EntityBaseMagicShot;
import sweetmagic.init.entity.projectile.EntityCyclonMagic;
import sweetmagic.init.entity.projectile.EntityDigMagic;
import sweetmagic.init.entity.projectile.EntityExplosionMagic;
import sweetmagic.init.entity.projectile.EntityFireMagic;
import sweetmagic.init.entity.projectile.EntityFlameNova;
import sweetmagic.init.entity.projectile.EntityFrostMagic;
import sweetmagic.init.entity.projectile.EntityGravityMagic;
import sweetmagic.init.entity.projectile.EntityLightMagic;
import sweetmagic.init.entity.projectile.EntityPoisonMagic;
import sweetmagic.init.entity.projectile.EntityShinigFlare;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;

public class ShotMagic extends MFSlotItem {

	public final int data;
	ResourceLocation icon;
	public SMElement subEle = null;

	public ShotMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.SHOTTER, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + name + ".png");
    }

	public ShotMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.SHOTTER, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + dir + ".png");
	}

	public ShotMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir, SMElement subEle) {
		super(name, SMType.SHOTTER, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + dir + ".png");
		this.setSubElement(subEle);
	}

	/**
	 * 0 = 炎魔法
	 * 1 = 氷魔法
	 * 2 = 光魔法
	 * 3 = 採掘魔法
	 * 4 = たまごっち魔法
	 * 5 = 風射撃魔法
	 * 6 = 重力射撃魔法
	 * 7 = 多段光魔法
	 * 8 = 範囲採掘魔法
	 * 9 = 貫通氷魔法
	 * 10 = 火炎玉魔法
	 * 11 = 毒射撃魔法
	 * 12 = 3連風魔法
	 * 13 = 重力波魔法
	 * 14 = シルクタッチ範囲採掘魔法
	 * 15 = 小爆発魔法
	 * 16 = 散弾光魔法
	 * 17 = 魔法爆発
	 * 18 = 暴風魔法
	 * 19 = 大重力魔法
	 * 20 = 泡魔法
	 * 21 = 泡窒息魔法
	 * 22 = 範囲猛毒魔法
	 * 23 = 5範囲dig魔法
	 * 24 = 泡窒息リジェネ解除魔法
	 * 25 = 炎/光魔法
	 * 26 = 泡/爆発魔法
	 */

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_fire.name");
			break;
		case 1:
			toolTip.add("tip.fro.name");
			break;
		case 2:
			toolTip.add("tip.magic_light.name");
			break;
		case 3:
			toolTip.add("tip.magic_dig.name");
			break;
		case 4:
			toolTip.add("tip.magic_tamagotti.name");
			break;
		case 5:
			toolTip.add("tip.magic_tornado.name");
			break;
		case 6:
			toolTip.add("tip.magic_attractive.name");
			break;
		case 7:
			toolTip.add("tip.magic_starburst.name");
			break;
		case 8:
			toolTip.add("tip.magic_rangebreaker.name");
			break;
		case 9:
			toolTip.add("tip.magic_frostspear.name");
			break;
		case 10:
			toolTip.add("tip.magic_flamenova.name");
			break;
		case 11:
			toolTip.add("tip.magic_rangepoison.name");
			break;
		case 12:
			toolTip.add("tip.magic_storm.name");
			break;
		case 13:
			toolTip.add("tip.magic_gravitywave.name");
			break;
		case 14:
			toolTip.add("tip.magic_mining_magia.name");
			break;
		case 15:
			toolTip.add("tip.magic_tamagotti.name");
			break;
		case 16:
			toolTip.add("tip.magic_sacredbuster.name");
			break;
		case 17:
			toolTip.add("tip.magic_magia_destroy.name");
			break;
		case 18:
			toolTip.add("tip.magic_cyclon.name");
			break;
		case 19:
			toolTip.add("tip.magic_gravity_break.name");
			break;
		case 20:
			toolTip.add("tip.magic_bubleprison.name");
			break;
		case 21:
			toolTip.add("tip.magic_scumefang.name");
			break;
		case 22:
			toolTip.add("tip.magic_deadly_poison.name");
			break;
		case 23:
			toolTip.add("tip.magic_earth_destruction.name");
			break;
		case 24:
			toolTip.add("tip.magic_foamy_hell.name");
			break;
		case 25:
			toolTip.add("tip.magic_shining_flare.name");
			break;
		case 26:
			toolTip.add("tip.magic_bleb_burst.name");
			break;
		}

		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		IWand wand = IWand.getWand(stack);

		if (!world.isRemote) {

			// レベル取得
			float level = wand.isCreativeWand() ? wand.getCreativePower() : wand.getLevel(stack);

			// (レベル × 補正値) + (レベル + 追加ダメージ) ÷ (レベル ÷ 2) + 追加ダメージ
			float power = this.getPower(level);

			EntityBaseMagicShot entity = null;
			boolean isDamage = true;		// レベルごとにダメージを増やす
			boolean isFix = false;		// ダメージを3で固定
			boolean tripleShot = false;	// 3弾射撃

			switch (this.data) {
			case 0:
				entity = new EntityFireMagic(world, player, stack);
				break;
			case 1:
				entity = new EntityFrostMagic(world, player, stack, false);
				break;
			case 2:
				entity = new EntityLightMagic(world, player, stack);
				isFix = true;
				break;
			case 3:
				entity = new EntityDigMagic(world, player, stack, 0);
				isDamage = false;
				break;
			case 4:
				entity = new EntityExplosionMagic(world, player, stack, 1);
				break;
			case 5:
				entity = new EntityCyclonMagic(world, player, stack);
				break;
			case 6:
				entity = new EntityGravityMagic(world, player, stack);
				break;
			case 7:
				entity = new EntityLightMagic(world, player, stack);
				isFix = true;
				tripleShot = true;
				break;
			case 8:
				entity = new EntityDigMagic(world, player, stack, 1);
				isDamage = false;
				break;
			case 9:
				entity = new EntityFrostMagic(world, player, stack, true);
				entity.setDamage(3);
				break;
			case 10:
				entity = new EntityFlameNova(world, player, stack);
				entity.setDamage(3);
				break;
			case 11:
				entity = new EntityPoisonMagic(world, player, stack, 0);
				isDamage = false;
				break;
			case 12:
				entity = new EntityCyclonMagic(world, player, stack);
				entity.setDamage(3);
				entity.isHit = true;
				tripleShot = true;
				break;
			case 13:
				entity = new EntityGravityMagic(world, player, stack);
				isFix = true;
				entity.range = 6D;
				entity.isHitDead = true;
				break;
			case 14:
				entity = new EntityDigMagic(world, player, stack, 2);
				isDamage = false;
				break;
			case 15:
				entity = new EntityExplosionMagic(world, player, stack, 0);
				break;
			case 16:
				entity = new EntityLightMagic(world, player, stack);
				isFix = true;
				entity.plusTick = -100;
				break;
			case 17:
				entity = new EntityExplosionMagic(world, player, stack, 2);
				break;
			case 18:
				entity = new EntityCyclonMagic(world, player, stack);
				entity.setDamage((entity.getDamage() + power ) / 2 );
				entity.isHit = true;
				break;
			case 19:
				entity = new EntityGravityMagic(world, player, stack);
				entity.range = 12D;
				entity.isHitDead = true;
				break;
			case 20:
				entity = new EntityBabuleMagic(world, player, stack, 0);
				break;
			case 21:
				entity = new EntityBabuleMagic(world, player, stack, 1);
				break;
			case 22:
				entity = new EntityPoisonMagic(world, player, stack, 1);
				break;
			case 23:
				entity = new EntityDigMagic(world, player, stack, 3);
				break;
			case 24:
				entity = new EntityBabuleMagic(world, player, stack, 2);
				break;
			case 25:
				entity = new EntityShinigFlare(world, player, stack);
				entity.setDamage(5);
				break;
			case 26:
				entity = new EntityBabuleMagic(world, player, stack, 3);
				entity.isHitDead = true;
				entity.setDamage(3);
				break;
			}

			// ダメージを与える場合
			if (isDamage) {
				entity.setDamage(entity.getDamage() + power);
			}

			// ダメージを与えない（必ず1は付けること）
			else {
				entity.setDamage(1);
			}

			// ダメージ固定なら
			if (isFix) {
				entity.setDamage(3);
			}

			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 1F + Math.min(power * 0.5F, 4F), 0);	// 射撃速度
			world.spawnEntity(entity);

			if (tripleShot) {

				// 向きの取得
				Vec3d vec = this.lookVector(player.rotationYaw, player.rotationPitch);
				Vec3d left = vec.crossProduct(new Vec3d(0, 1, 0));
				Vec3d right = vec.crossProduct(new Vec3d(0, -1, 0));

				this.spawnArrow(world, player, stack, power, left.normalize());
				this.spawnArrow(world, player, stack, power, right.normalize());
			}

			else if (this.data == 16 || this.data == 18) {
				this.shotSacred(world, player, stack, power);
			}
		}

		world.playSound(null, new BlockPos(player), SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.67F);

		// falseを返してえんちちーでレベルアップ処理を呼び出す
		return false;
	}

	// 向きの取得
	public Vec3d lookVector(float rotYaw, float rotPitch) {
		return new Vec3d(
				Math.sin(rotYaw) * Math.cos(rotPitch),
				Math.sin(rotPitch),
				Math.cos(rotYaw) * Math.cos(rotPitch));
	}

	// 左右の矢の射撃
	public void spawnArrow(World world, EntityPlayer player, ItemStack stack, float power, Vec3d vec) {

		if (!world.isRemote) {

			EntityBaseMagicShot entity = null;

			switch (this.data) {
			case 7:
				entity = new EntityLightMagic(world, player, stack);
				entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 1F + Math.min(power * 0.5F, 4F), 16);	// 射撃速度
				entity.setDamage(3);
				break;
			case 12:
				entity = new EntityCyclonMagic(world, player, stack);
				entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 1F + Math.min(power * 0.5F, 4F), 2);	// 射撃速度
				entity.setDamage(entity.getDamage() + power);
				break;
			}

			entity.posX += vec.x;
			entity.posY += vec.y;
			entity.posZ += vec.z;
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			world.spawnEntity(entity);
		}
	}


	// 左右の矢の射撃
	public void shotSacred(World world, EntityPlayer player, ItemStack stack, float power) {

		for (int i = 0; i <= 9; i++) {

			EntityBaseMagicShot entity = null;

			switch (this.data) {
			case 16:
				entity = new EntityLightMagic(world, player, stack);
				entity.setDamage(3);
				break;
			case 18:
				entity = new EntityCyclonMagic(world, player, stack);
				entity.setDamage( (entity.getDamage() + power ) * 0.25D );
				entity.isHit = true;
				break;
			}

			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 2.5F, 2.5F, 2.5F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 1F + Math.min(power * 0.5F, 4F), 20);	// 射撃速度
			world.spawnEntity(entity);
			entity.plusTick = -100;
		}
	}

	// サブ属性の取得
	@Override
	public SMElement getSubElement () {
		return this.subEle;
	}

	// サブ属性の設定
	@Override
	public void setSubElement (SMElement ele) {
		this.subEle = ele;
	}
}
