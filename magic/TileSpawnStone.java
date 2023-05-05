package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityElshariaCurious;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntityRepairKitt;
import sweetmagic.init.entity.monster.EntitySandryon;
import sweetmagic.init.entity.monster.EntitySilderGhast;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.util.SMUtil;
import sweetmagic.util.WorldHelper;

public class TileSpawnStone extends TileSMBase {

	public int data = 0;
	public boolean isRand = false;
	public boolean isBossSummon = true;
	private EntityPlayer player = null;
	public int isPowerUp = 0;
	public boolean isWCSide = true;

	public void serverUpdate() {

		this.tickTime++;
		if (this.tickTime % 20 != 0 || WorldHelper.isPeaceful(this.world)) { return; }

		this.tickTime = 0;

		// プレイヤーが範囲にいないか
        if (!this.isRangePlayer()) { return; }

        // モブスポーン
		this.spawnMob();
	}

	// モブスポーン
	public void spawnMob () {

		Random rand = this.world.rand;
		Block block = this.getBlock(this.pos.down());
		boolean isBoss = false;

		if (!this.isRand) {

			if (this.isBossSummon && block == BlockInit.ac_ore) {
				this.data = 6;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == Blocks.PACKED_ICE) {
				this.data = 7;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == Blocks.MAGMA) {
				this.data = 8;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp <= 1 && block == Blocks.SKULL) {
				this.data = 9;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.poison_block) {
				this.data = 10;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.cosmic_crystal_ore) {
				this.data = 11;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.cosmos_light_block) {
				this.data = 12;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.twilightlight) {
				this.data = 13;
				isBoss = true;
			}

			else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.magiclight) {
				this.data = 14;
				isBoss = true;
			}

			else if (this.isPowerUp == 0 && this.isWCSide) {
				this.data = rand.nextInt(6);
			}

			else if (this.isWCSide) {
				this.data = rand.nextInt(9);
			}

			else {
				this.data = rand.nextInt(8);
			}
		}

		if (!isBoss) {
			this.summonMob(rand);
		}

		else {
			this.summonBoss(rand);
		}

		this.breakBlock(this.pos, false);
		this.playSound(this.pos, SMSoundEvent.HORAMAGIC, 0.3F, 1F);
	}

	// モブ召喚
	public void summonMob (Random rand) {
		for (int i = 0; i < 4; i++) {

			EntityLivingBase entity = null;

			switch (this.data) {
			case 0:
				// エレキキューブ
				entity = new EntityElectricCube(this.world);
				((EntityElectricCube) entity).setSlimeSize(1 << i, true);
				break;
			case 1:
				// アークスパイダー
				entity = new EntityArchSpider(this.world);
				break;
			case 2:
				// ブレイズテンペスト
				entity = new EntityBlazeTempest(this.world);
				break;
			case 3:
				// ファントムゾンビ
				entity = new EntityPhantomZombie(this.world);
				break;
			case 4:
				// エンダーシャドー
				entity = new EntityEnderShadow(this.world);
				((EntityEnderShadow) entity).canSpawnShadow = false;

				if (i != 3) {
					entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
					entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
				}

				else {
					entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.DIAMOND_SWORD));
					entity.setHeldItem(EnumHand.OFF_HAND, new ItemStack(Items.DIAMOND_SWORD));
					entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
					entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
					entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
					entity.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
				}

				break;
			case 5:
				// スカルフロスト
				entity = new EntitySkullFrost(this.world);
				entity.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));

				if (i == 3) {
					entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
					entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 600, 3, true, false));
				}
				break;
			case 6:

				// ウィッチマスター
				if (this.isWCSide) {
					entity = new EntityWitchMadameVerre(this.world);
				}

				// ピクシィヴェックス
				else {
					entity = new EntityPixieVex(this.world);
				}

				break;
			case 7:

				// ウィンディーネ
				if (this.isWCSide) {
					entity = new EntityWindineVerre(this.world);
				}

				// シルダーガスト
				else {
					entity = new EntitySilderGhast(this.world);
				}

				break;
			case 8:
				// イフリート
				entity = new EntityIfritVerre(this.world);
				break;
			}

			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1200, 1, true, false));

			// ユニークモンスターなら
			if (i == 3) {

				entity.setFire(0);
				this.setMaxHealth(entity, this.getHealth(this.getHealth(60F)));
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
				entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36D);
				entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6D);
				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1200, 3, true, false));
				entity.addPotionEffect(new PotionEffect(PotionInit.shadow, 1200, 4, true, false));

				// エレキキューブは対象外
				if (this.data != 0 && (this.isWCSide && this.data != 7)) {
					entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
				}
			}

			else if (this.isPowerUp >= 0) {
				this.setMaxHealth(entity, this.getHealth(entity.getMaxHealth()));
				entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);
			}


			int x = rand.nextInt(7) - 3 + this.pos.getX();
			int y = rand.nextInt(3) + 1 + this.pos.getY();
			int z = rand.nextInt(7) - 3 + this.pos.getZ();

			if (this.isAir(new BlockPos(x, y, z))) {
				x = this.pos.getX();
				y = this.pos.getY() + 1;
				z = this.pos.getZ();
			}

			entity.setLocationAndAngles(x, y, z, 0, 0F);
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			this.world.spawnEntity(entity);
		}
	}

	// ボス召喚
	public void summonBoss (Random rand) {

		EntityLivingBase entity = null;
		boolean isBuff = false;

		switch (this.data) {
		case 6:
			// ウィッチマスター
			entity = new EntityWitchMadameVerre(this.world);
			entity.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 2400, 3, true, false));
			isBuff = true;
			break;
		case 7:
			// ウィンディーネ
			entity = new EntityWindineVerre(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.regene, 99999, 0, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1, true, false));
			isBuff = true;
			break;
		case 8:
			// イフリート
			entity = new EntityIfritVerre(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.regene, 2400, 0, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1, true, false));
			isBuff = true;
			((EntityIfritVerre) entity).tpPos = this.pos;
			break;
		case 9:
			// ブレイブスケルトン
			EntityBraveSkeleton brave = new EntityBraveSkeleton(this.world);

			if (this.isPowerUp == 1) {
				brave.setPowerd(true);
				brave.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);
				brave.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
				brave.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(30D);
				brave.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(20D);
				brave.setHealth(brave.getMaxHealth());
				brave.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 99999, 1, true, false));
				brave.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1, true, false));
				brave.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 1, true, false));
			}

			entity = brave;
			this.breakBlock(this.pos.down(), false);
			break;
		case 10:
			// エンシェントフェアリー
			entity = new EntityAncientFairy(this.world);
			this.breakBlock(this.pos.down(), false);

			Random vRand = this.world.rand;

			for (int i = 0; i < 4; i++) {
				EntityPixieVex vex = new EntityPixieVex(this.world);
				double x = this.getPos().getX() + vRand.nextDouble() * 4 - vRand.nextDouble() * 4;
				double z = this.getPos().getZ() + vRand.nextDouble() * 4 - vRand.nextDouble() * 4;

				vex.setPosition(x, this.getPos().getY() + vRand.nextDouble() * 2, z);
				vex.setData(vRand.nextInt(3));
				vex.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 99999, 4, true, false));
				vex.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
				this.world.spawnEntity(vex);
			}

			break;
		case 11:
			// サンドリヨン
			entity = new EntitySandryon(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1, true, false));
			break;
		case 12:
			// リペアきっと
			entity = new EntityRepairKitt(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0, true, false));
			this.breakBlock(this.pos.down(), false);
			break;
		case 13:
			// ホーラ
			entity = new EntityZombieHora(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0, true, false));
			this.breakBlock(this.pos.down(), false);
			break;
		case 14:
			// エルシャリアキュリオス
			entity = new EntityElshariaCurious(this.world);
			this.breakBlock(this.pos.down(), false);
			break;
		}

		int x = rand.nextInt(7) - 3 + this.pos.getX();
		int y = rand.nextInt(3) + 1 + this.pos.getY();
		int z = rand.nextInt(7) - 3 + this.pos.getZ();
		BlockPos pos = new BlockPos(x, y, z);
		entity.setLocationAndAngles(x, y, z, 0, 0F);
		SMUtil.tameAIAnger((EntityLiving) entity, this.player);
		entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));

		// バフ
		if (isBuff) {
			this.setMaxHealth(entity, 256F);
			entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
			entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
			entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);

			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 2400, 2, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.shadow, 4800, 4, true, false));
			this.world.spawnEntity(entity);
		}

		else {
			((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
			AnvilChunkLoader.spawnEntity(entity, world);
			this.world.playEvent(2004, pos, 0);
		}

		int count = 0;

		for (BlockPos p : this.getPosList(this.pos.add(-5, 0, -5), this.pos.add(5, 2, 5))) {

			if (this.getBlock(p) != BlockInit.crystal_core) { continue; }

			for (int i = 0; i < 4; i++)
				this.breakBlock(p.down(i), false);

			count++;
		}

		if (count > 0) {
			this.setMaxHealth(entity, entity.getMaxHealth() * (1F + 0.125F * count));
		}
	}

	public float getHealth (float health) {
		return health * ( 1 + this.isPowerUp * 0.5F);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("data", this.data);
		tags.setInteger("isPowerUp", this.isPowerUp);
		tags.setBoolean("isRand", this.isRand);
		tags.setBoolean("isWCSide", this.isWCSide);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.data = tags.getInteger("data");
		this.isPowerUp = tags.getInteger("isPowerUp");
		this.isRand = tags.getBoolean("isRand");
		this.isWCSide = tags.getBoolean("isWCSide");
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

        AxisAlignedBB aabb = this.getAABB(this.pos.add(-7, -1, -7), this.pos.add(7, 3, 7));
		List<EntityPlayer> entityList = this.getEntityList(EntityPlayer.class, aabb);
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative()) { continue; }

			this.player = player;
			return true;
		}

		return false;
	}
}
