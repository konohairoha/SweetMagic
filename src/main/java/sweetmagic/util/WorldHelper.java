package sweetmagic.util;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import sweetmagic.api.iitem.IWand;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.ISMMob;

public class WorldHelper {

	// プレイヤーを取得
	public static void attackEntity(World world, AxisAlignedBB aabb) {

		if (world.isRemote) { return; }

		List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		if (list.isEmpty()) { return; }

		for (EntityPlayer ent : list){
			attackAOE(ent);
			return;
		}
	}

	public static void attackAOE(EntityPlayer player) {

		World world = player.world;
		int charge = 5;		//ダメージを与える範囲
		float factor = 2.5F * charge;
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(factor);
		List<EntityLiving> toAttack = world.getEntitiesWithinAABB(EntityLiving.class, aabb);
		if (toAttack.isEmpty()) { return; }

		DamageSource src = DamageSource.causePlayerDamage(player);
		src.setDamageBypassesArmor();
		Random rand = new Random();

		for (EntityLiving entity : toAttack) {

			if (!(entity instanceof IMob)) { continue; }

			entity.attackEntityFrom(src, 6);
			if (entity.getHealth() <= 0 && rand.nextInt(100) == 0) {
				EntityItem drop = new EntityItem(world, entity.posX, entity.posY, entity.posZ, new ItemStack(ItemInit.stray_soul, rand.nextInt(2) + 1));
				drop.motionY += 0.15;
				world.spawnEntity(drop);
			}
		}
    }

	//範囲内のえんちちーを回復
	public static void PlayerHeal(World world, AxisAlignedBB aabb, EntityPlayer player, ItemStack stack, boolean isFullHeal) {

		// 杖と魔法アイテムの取得
		IWand wand = IWand.getWand(stack);

		// 追加経験値
		int addExp = 0;
		List<EntityLivingBase> list = getEntityList(world, aabb);

		for (EntityLivingBase ent : list) {

			if (ent instanceof IMob || ent.getHealth() >= ent.getMaxHealth()) { continue; }

			if (!isFullHeal) {

				// switch文で回復量を変える予定
				ent.setHealth(ent.getHealth() + (wand.getLevel(stack) * 2) + 4);

				// デバフ解除
				ent.removePotionEffect(MobEffects.HUNGER);
				ent.removePotionEffect(MobEffects.WEAKNESS);
				addExp++;
			}

			else {

				// 全回復
				ent.setHealth(ent.getMaxHealth());
			}

			// パーティクルスポーン
			ParticleHelper.spawnHeal(ent, EnumParticleTypes.VILLAGER_HAPPY, 16, 1, 4);
		}


		if (addExp > 0 && !wand.isCreativeWand() && isFullHeal) {

			addExp *= 2;

			// 経験値の追加
			wand.levelUpCheck(world, player, stack, addExp);
		}
	}

	//範囲内のえんちちーデバフ解除
	public static void effectRemoveRange(World world, AxisAlignedBB aabb) {

		List<EntityLivingBase> list = getEntityList(world, aabb);

		for (EntityLivingBase ent : list) {

			if (ent instanceof IMob) { continue; }

			// デバフを除去
			for (Potion potion : PotionInit.getDeBuffPotionList()) {
				ent.removePotionEffect(potion);
			}

			// パーティクルスポーン
			ParticleHelper.spawnHeal(ent, EnumParticleTypes.WATER_SPLASH, 16, 1, 4);
		}
	}

	//範囲内のえんちちーデバフ解除
	public static void addEffect(World world, AxisAlignedBB aabb, Potion potion, int time, int level, EnumParticleTypes particle) {

		List<EntityLivingBase> list = getEntityList(world, aabb);

		for (EntityLivingBase ent : list) {

			if (ent instanceof IMob || ent.isPotionActive(potion)) { continue; }

			ent.addPotionEffect(new PotionEffect(potion, time, level, true, false));

			// パーティクルスポーン
			ParticleHelper.spawnHeal(ent, particle, 16, 1, 4);
		}
	}

	//プレイや吸い寄せ
	public static void suctionPlayer(World world, AxisAlignedBB aabb, double x, double y, double z, EntityLivingBase entity, int tickTime) {

		List<EntityLivingBase> list = getEntityList(world, aabb);

		for (EntityLivingBase ent : list) {

			// カラミティまたは重力加速があるなら次へ
			if (ent instanceof ISMMob || ent.isPotionActive(PotionInit.gravity_accele)) { continue; }

			double dX = x - ent.posX;
			double dY = y - ent.posY;
			double dZ = z - ent.posZ;
			double dist = Math.sqrt(dX * dX + dY * dY + dZ * dZ);
			double vel = 1D - dist / 15D;

			if (vel > 0.0D) {
				vel *= dist * vel;
				ent.motionX += dX / vel * 0.0775;
				ent.motionY += dY / vel * 0.135;
				ent.motionZ += dZ / vel * 0.0775;
			}
		}
	}

	// エンティティリストの取得
	public static List<EntityLivingBase> getEntityList (World world, AxisAlignedBB aabb) {
		return world.getEntitiesWithinAABB(EntityLivingBase.class, aabb);
	}

	public static boolean setAir(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		world.playEvent(2001, pos, Block.getStateId(state));
		return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	public static List<ItemStack> getBlockDrops(World world, EntityPlayer player, IBlockState state, Block block, BlockPos pos, boolean canSilk, int fortune){
		if (canSilk && block.canSilkHarvest(world, pos, state, player)){
			return Lists.newArrayList(new ItemStack(block, 1, block.getMetaFromState(state)));
		}
		return block.getDrops(world, pos, state, fortune);
	}

	public static void createLootDrop(List<ItemStack> drops, World world, double x, double y, double z) {

		ItemHelper.compactItemListNoStacksize(drops);
		if (drops.isEmpty()) { return; }

		for (ItemStack drop : drops) {
			EntityItem ent = new EntityItem(world, x, y, z);
			ent.setItem(drop);
			world.spawnEntity(ent);
		}
	}

	// ピースフルか
	public static boolean isPeaceful (World world) {
		return world.getDifficulty() == EnumDifficulty.PEACEFUL;
	}

	// スパーフラットかどうか
	public static boolean isFlat (World world) {
		return world.getWorldInfo().getTerrainType() == WorldType.FLAT;
	}

	// ディメンションID取得
	public static int getDimId (World world) {
		return world.provider.getDimension();
	}

	// スイートマジックのディメンションか
	public static boolean isSMDim (World world) {
		return getDimId(world) == DimensionInit.dimID;
	}

	// バイオーム取得
	public static Biome getBiome (World world, BlockPos pos) {
		return world.getBiomeForCoordsBody(pos);
	}
}
