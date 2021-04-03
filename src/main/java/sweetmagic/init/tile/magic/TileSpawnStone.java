package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.world.World;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.util.WorldHelper;

public class TileSpawnStone extends TileSMBase {

	public int data = 0;
	public boolean isRand = false;
	public boolean isBossSummon = true;
	public EntityPlayer player = null;
	public int isPowerUp = 0;

	public void update() {

		this.tickTime++;
		if (this.tickTime % 20 != 0 || this.world.isRemote || WorldHelper.isPeaceful(this.world)) { return; }

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
			} else if (this.isBossSummon && this.isPowerUp == 0 && block == Blocks.PACKED_ICE) {
				this.data = 7;
				isBoss = true;
			} else if (this.isBossSummon && this.isPowerUp == 0 && block == Blocks.MAGMA) {
				this.data = 8;
				isBoss = true;
			} else if (this.isBossSummon && this.isPowerUp == 0 && block == Blocks.SKULL) {
				this.data = 9;
				isBoss = true;
			} else if (this.isBossSummon && this.isPowerUp == 0 && block == BlockInit.poison_block) {
				this.data = 10;
				isBoss = true;
			} else if (this.isPowerUp == 0) {
				this.data = rand.nextInt(6);
			} else {
				this.data = rand.nextInt(9);
			}
		}

		if (!isBoss) {
			this.summonMob(rand);
		}

		else {
			this.summonBoss(rand);
		}

		this.breakBlock(this.pos, this.world, false);
		this.playSound(this.pos, SMSoundEvent.HORAMAGIC, 1F, 1F);
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
				entity = new EntityWitchMadameVerre(this.world);
				break;
			case 7:
				// ウィンディーネ
				entity = new EntityWindineVerre(this.world);
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
				entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getHealth(60D));
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
				entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36D);
				entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6D);
				entity.setHealth(entity.getMaxHealth());
				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1200, 3, true, false));
				entity.addPotionEffect(new PotionEffect(PotionInit.shadow, 1200, 4, true, false));

				// エレキキューブは対象外
				if (this.data != 0) {
					entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
				}
			}

			else if (this.isPowerUp >= 0) {
				entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.getHealth(entity.getMaxHealth()));
				entity.setHealth(entity.getMaxHealth());
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
			entity.addPotionEffect(new PotionEffect(PotionInit.regene, 99999, 3, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 1, true, false));
			isBuff = true;
			break;
		case 8:
			// イフリート
			entity = new EntityIfritVerre(this.world);
			entity.addPotionEffect(new PotionEffect(PotionInit.regene, 2400, 2, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 2400, 1, true, false));
			isBuff = true;
			((EntityIfritVerre) entity).tpPos = this.pos;
			break;
		case 9:
			// ブレイブスケルトン
			entity = new EntityBraveSkeleton(this.world);
			this.world.setBlockState(this.pos.down(), Blocks.AIR.getDefaultState(), 2);
			break;
		case 10:
			// エンシェントフェアリー
			entity = new EntityAncientFairy(this.world);
			this.world.setBlockState(this.pos.down(), Blocks.AIR.getDefaultState(), 2);

			Random vRand = this.world.rand;

			for (int i = 0; i < 4; i++) {
				EntityPixieVex vex = new EntityPixieVex(this.world);
				double x = this.getPos().getX() + vRand.nextDouble() * 4 - vRand.nextDouble() * 4;
				double z = this.getPos().getZ() + vRand.nextDouble() * 4 - vRand.nextDouble() * 4;

				vex.setPosition(x, this.getPos().getY() + vRand.nextDouble() * 2, z);
				vex.data = vRand.nextInt(3);
				vex.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 99999, 4, true, false));
				this.world.spawnEntity(vex);
			}

			break;
		}

		int x = rand.nextInt(7) - 3 + this.pos.getX();
		int y = rand.nextInt(3) + 1 + this.pos.getY();
		int z = rand.nextInt(7) - 3 + this.pos.getZ();
		BlockPos pos = new BlockPos(x, y, z);
		entity.setLocationAndAngles(x, y, z, 0, 0F);
		SMUtil.tameAIAnger((EntityLiving) entity, this.player);

		// バフあ
		if (isBuff) {
			entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(256D);
			entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.33D);
			entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64D);
			entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10D);
			entity.setHealth(256F);

			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 4800, 3, true, false));
			entity.addPotionEffect(new PotionEffect(PotionInit.shadow, 4800, 4, true, false));
			this.world.spawnEntity(entity);
		}

		else {
			((EntityLiving) entity).onInitialSpawn(this.world.getDifficultyForLocation(pos), (IEntityLivingData) null);
			AnvilChunkLoader.spawnEntity(entity, world);
			this.world.playEvent(2004, pos, 0);
		}
	}

	public double getHealth (double health) {
		return health * ( 1 + this.isPowerUp * 0.5F);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("data", this.data);
		tags.setInteger("isPowerUp", this.isPowerUp);
		tags.setBoolean("isRand", this.isRand);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.data = tags.getInteger("data");
		this.isPowerUp = tags.getInteger("isPowerUp");
		this.isRand = tags.getBoolean("isRand");
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

        AxisAlignedBB aabb = new AxisAlignedBB(this.pos.add(-7, -1, -7), this.pos.add(7, 3, 7));
		List<EntityPlayer> entityList = this.world.getEntitiesWithinAABB(EntityPlayer.class, aabb);
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {
			if (!PlayerHelper.isCleative(player)) {
				this.player = player;
				return true;
			}
		}

		return false;
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, World world, boolean dropBlock) {

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block.isAir(state, world, pos)) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));

		if (dropBlock) {
			block.dropBlockAsItem(world, pos, state, 0);
		}

		return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}
}
