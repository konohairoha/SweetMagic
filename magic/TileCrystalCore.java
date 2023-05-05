package sweetmagic.init.tile.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.block.blocks.CrystalCore;
import sweetmagic.init.entity.monster.EntityArchSpider;
import sweetmagic.init.entity.monster.EntityBlazeTempest;
import sweetmagic.init.entity.monster.EntityElectricCube;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.monster.EntityPhantomZombie;
import sweetmagic.init.entity.monster.EntityPixieVex;
import sweetmagic.init.entity.monster.EntitySilderGhast;
import sweetmagic.init.entity.monster.EntitySkullFrost;
import sweetmagic.init.tile.inventory.InventoryWoodChest;
import sweetmagic.init.tile.slot.WrappedItemHandler;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.WorldHelper;

public class TileCrystalCore extends TileSMBase {

	private static final ItemStack PAGE = new ItemStack(ItemInit.mystical_page);
	private int maxPower = 0;
	private EntityPlayer player = null;

	// スロット数
	public int getInvSize () {
		return 1;
	}

	// インベントリ
	public final ItemStackHandler chestInv = new InventoryWoodChest(this, this.getInvSize()) {

        @Override
		public void onContentsChanged(int slot) {
            markDirty();
            IBlockState state = world.getBlockState(pos);
            world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 1 | 2);
        }
    };

	public void serverUpdate() {

		this.tickTime++;
		if (this.tickTime % 50 != 0 || WorldHelper.isPeaceful(this.world) || this.getData() == 0) { return; }

		this.tickTime = 0;

		// プレイヤーが範囲にいないか、範囲にモブがいるなら
        if (!this.isRangePlayer() || this.isRangeEntity()) { return; }

        // モブスポーン
		this.spawnMob();
	}

	// 範囲内にプレイヤーがいるかどうか
	public boolean isRangePlayer () {

        AxisAlignedBB aabb = this.getAABB(this.pos.add(-8, -2, -8), this.pos.add(8, 3, 8));
		List<EntityPlayer> entityList = this.getEntityList(EntityPlayer.class, aabb);
		if (entityList.isEmpty()) { return false; }

		for (EntityPlayer player : entityList) {

			if (player.isCreative() || player.isSpectator()) { continue; }

			PlayerHelper.addPotion(player, PotionInit.breakblock, 270, 1, true);
			this.player = player;
			return true;
		}

		return false;
	}

	public boolean isRangeEntity () {

		AxisAlignedBB aabb = this.getAABB(this.pos.add(-7, -2, -7), this.pos.add(7, 1, 7));
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, aabb);
		if (entityList.isEmpty()) { return false; }

		for (EntityLivingBase entity : entityList) {
			if ( !(entity instanceof IMob) ) { continue; }
			return true;
		}

		return false;
	}

	// モブスポーン
	public void spawnMob () {

		Random rand = this.world.rand;
		int power = this.breakCore();
		this.playSound(this.pos, SMSoundEvent.HORAMAGIC, 0.25F, 1F);

		for (int i = 0; i < 5; i++) {

			EntityLivingBase entity = null;
			int data = rand.nextInt(8);

			switch (data) {
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
				// ピクシィヴェックス
				entity = new EntityPixieVex(this.world);
				break;
			case 7:
				// シルダーガスト
				entity = new EntitySilderGhast(this.world);
				break;
			}

			if (power > 0 && data != 0) {
				float health = entity.getMaxHealth() * (1F + power * 0.5F);
				this.setMaxHealth(entity, health);
			}

			int x = rand.nextInt(7) - 3 + this.pos.getX();
			int y = rand.nextInt(3) + 1 + this.pos.getY();
			int z = rand.nextInt(7) - 3 + this.pos.getZ();

			if (this.isAir(new BlockPos(x, y, z))) {
				x = this.pos.getX();
				y = this.pos.getY() + 1;
				z = this.pos.getZ();
			}

			entity.addPotionEffect(new PotionEffect(PotionInit.aether_barrier, 1600, Math.max(power - 2, 0), true, false));
			entity.setLocationAndAngles(x, y, z, 0, 0F);
			entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
			this.world.spawnEntity(entity);
		}
	}

	public int breakCore () {

		for (BlockPos p : this.getPosList(this.pos.add(-5, 0, -5), this.pos.add(5, 1, 5))) {

			if (this.getBlock(p) != BlockInit.crystal_core) { continue; }

			int power = ((TileCrystalCore) this.getTile(p)).getPower();
			this.maxPower = Math.max(this.maxPower, power);

			for (int i = 0; i < 4; i++)
				this.breakBlock(p.down(i), false);
			return power;
		}

		for (int i = 0; i < 3; i++)
			this.breakBlock(this.pos.down(i), false);
		return this.maxPower + 1;
	}

    // スロットが空かどうか
    public ItemStackHandler getSlot () {
    	return this.chestInv;
    }

    public boolean isSlotEmpty () {
    	return this.getChestItem().isEmpty();
    }

	private final IItemHandlerModifiable output = new WrappedItemHandler(this.chestInv, WrappedItemHandler.WriteMode.OUT);
	private final IItemHandler side = new CombinedInvWrapper(this.chestInv);
	private final CombinedInvWrapper join = new CombinedInvWrapper(this.chestInv);

	@Override
	public boolean hasCapability(@Nonnull Capability<?> cap, EnumFacing side) {
		return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(cap, side);
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> cap, EnumFacing side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (side == null) {
				return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.join);
			} else {
				switch (side) {
				case DOWN:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.output);
				default:
					return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(this.side);
				}
			}
		}

		return super.getCapability(cap, side);
	}

	// インベントリの取得
	public IItemHandler getChest() {
		return this.chestInv;
	}

	// インベントリのアイテムを取得
	public  ItemStack getChestItem() {
		return this.getData() == 0 ? this.getChest().getStackInSlot(0) : PAGE;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setTag("chestInv", this.chestInv.serializeNBT());
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.chestInv.deserializeNBT(tags.getCompoundTag("chestInv"));
	}

	public int getData () {
		return ((CrystalCore) this.getBlock(this.pos)).getData();
	}

	public int getPower () {

		Item item = this.getChestItem().getItem();

		if (item == ItemInit.aether_crystal) {
			return 1;
		}

		else if (item == ItemInit.divine_crystal) {
			return 2;
		}

		else if (item == ItemInit.pure_crystal) {
			return 3;
		}

		else if (item == ItemInit.deus_crystal) {
			return 4;
		}

		else if (item == ItemInit.cosmic_crystal) {
			return 5;
		}

		return 0;
	}
}
