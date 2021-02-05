package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.WorldHelper;

public class TileSpawnStone extends TileSMBase {

	public int data = 0;
	public boolean isRand = false;
	public EntityPlayer player = null;

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

		if (!this.isRand) {
			this.data = rand.nextInt(7);
		}

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
			}

			// ユニークモンスターなら
			if (i == 3) {
				entity.setFire(0);
				entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
				entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
				entity.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(36D);
				entity.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(6D);
				entity.setHealth(60F);
				entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 600, 3, true, false));
				entity.addPotionEffect(new PotionEffect(PotionInit.shadow, 600, 4, true, false));

				// エレキキューブは対象外
				if (this.data != 0) {
					entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(8D);
				}
			}

			int x = rand.nextInt(7) - 3 + this.pos.getX();
			int y = rand.nextInt(3) + 1 + this.pos.getY();
			int z = rand.nextInt(7) - 3 + this.pos.getZ();
			entity.setLocationAndAngles(x, y, z, 0, 0F);
			this.world.spawnEntity(entity);
		}

		this.breakBlock(this.pos, this.world, false);
		this.playSound(this.pos, SMSoundEvent.HORAMAGIC, 1F, 1F);
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("data", this.data);
		tags.setBoolean("isRand", this.isRand);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.data = tags.getInteger("data");
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
