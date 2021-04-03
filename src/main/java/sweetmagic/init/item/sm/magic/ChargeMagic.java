package sweetmagic.init.item.sm.magic;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.PotionInit;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.WorldHelper;

public class ChargeMagic extends MFSlotItem {

	public final int data;
	ResourceLocation icon;
	public final static String CYCLONEINT = "cyclonInt";
	public final static String GRAVITYINT = "gravityInt";

	public ChargeMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.CHARGE, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + name + ".png");
    }

	public ChargeMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.CHARGE, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation("sweetmagic","textures/items/" + dir + ".png");
    }

	/**
	 * 0 = 風魔法
	 * 1 = 重力魔法
	 * 2 = 雷魔法
	 * 3 = 単体雷魔法
	 * 4 = 時間低速化魔法
	 * 5 = 範囲作物成長魔法
	 * 6 = 小範囲作物成長魔法
	 * 7 = 作物広範囲魔法
	 */

	// テクスチャのリソースを取得
	public ResourceLocation getResource () {
		return this.icon;
	}

	// ツールチップ
	public List<String> magicToolTip (List<String> toolTip) {

		switch (this.data) {
		case 0:
			toolTip.add("tip.magic_cyclone.name");
			break;
		case 1:
			toolTip.add("tip.magic_gravity.name");
			break;
		case 2:
			toolTip.add("tip.magic_elec.name");
			break;
		case 3:
			toolTip.add("tip.magic_lightningbolt.name");
			break;
		case 4:
			toolTip.add("tip.magic_slowtime.name");
			break;
		case 5:
			toolTip.add("tip.magic_growthaura.name");
			break;
		case 6:
			toolTip.add("tip.magic_growth_effect.name");
			break;
		case 7:
			toolTip.add("tip.magic_growth_verre.name");
			break;
		}

		return toolTip;
	}

	@Override
	public boolean onItemAction(World world, EntityPlayer player, ItemStack stack, Item slotItem) {

		NBTTagCompound tags = stack.getTagCompound();
		boolean flag = true;

		switch (this.data) {
		case 0:
			// 風魔法
			flag = this.cyclonAction(world, player, stack, tags);
			break;
		case 1:
			// 重力魔法
			flag = this.gravityAction(world, player, stack, tags);
			break;
		case 2:
			// 雷魔法
			flag = this.elecAction(world, player, stack, tags);
			break;
		case 3:
			// 単体雷魔法
			flag = this.singleElecAction(world, player, stack, tags);
			break;
		case 4:
			// 時間低速化魔法
			flag = this.slowBuff(world, player, stack, tags);
			break;
		case 5:
			// 範囲作物成長魔法
			flag = this.growthAura(world, player, stack, tags);
			break;
		case 6:
			// 小範囲作物成長魔法
			flag = this.growthEffect(world, player, stack, tags);
			break;
		case 7:
			// 作物広範囲魔法
			flag = this.growthVerre(world, player, stack, tags);
			break;
		}

		return flag;
	}

	// 風魔法
	public boolean cyclonAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(10, 5, 10);
		WorldHelper.addEffect(world, aabb, PotionInit.cyclone, this.effectTime(level), 0, EnumParticleTypes.SWEEP_ATTACK);

		// 疾風を与える
		this.addPotion(player, PotionInit.cyclone, this.effectTime(level), 1);
		world.playSound(null, new BlockPos(player), SMSoundEvent.CYCLON, SoundCategory.NEUTRAL, 3F, 1F);
		player.getEntityData().setInteger(CYCLONEINT, 5);
		return true;
	}

	// 重力魔法
	public boolean gravityAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(10, 5, 10);
		WorldHelper.addEffect(world, aabb, PotionInit.gravity_accele, this.effectTime(level), 0, EnumParticleTypes.FALLING_DUST);

		// 疾風を与える
		this.addPotion(player, PotionInit.gravity_accele, this.effectTime(level), 1);

		// パーティクルを出す
		world.playSound(null, new BlockPos(player), SMSoundEvent.GRAVITY, SoundCategory.NEUTRAL, 1.5F, 1F);
		player.getEntityData().setInteger(GRAVITYINT, 5);
		return true;
	}

	// 雷魔法
	public boolean elecAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (world.isRemote) { return false; }

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		double range = 8D + (level * 0.5D);

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(world, player, range, range, range);

		for (EntityLivingBase living : entityList) {

			if (!(living instanceof IMob)) { continue; }

			EntityElectricMagic entity = new EntityElectricMagic(world, player, stack);
			entity.setDamage(entity.getDamage() + this.getPower(level) * 2);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 0, 0);									// 射撃速度
			entity.motionY -= 1.5;
			entity.setPosition(living.posX - 0.5, living.posY + 2, living.posZ - 0.5);
	        world.spawnEntity(entity);

		}

		return false;
	}


	// 単体雷魔法
	public boolean singleElecAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (world.isRemote) { return false; }

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		EntityElectricMagic entity = new EntityElectricMagic(world, player, stack);
		entity.setDamage(entity.getDamage() + this.getPower(level) * 1.5);
		entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
		entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 0, 0);									// 射撃速度
		entity.motionY -= 1.5;

		// 向き取得
        Vec3d vec3d = player.getLookVec();
        vec3d = vec3d.normalize().scale(10);

        // 座標取得
        BlockPos pos = new BlockPos(player.posX + vec3d.x, player.posY + vec3d.y, player.posZ + vec3d.z);
		List<EntityLivingBase> entityList = this.getEntityList(world, pos.add(-7.5, -3, -7.5), pos.add(7.5, 3, 7.5));

		// えんちちー見つからんかった
		if (entityList.isEmpty()) {
			entity.setPosition(pos.getX(), pos.getY() + 2, pos.getZ());
		}

		// えんちちー見つかった
		else {

			for (EntityLivingBase e : entityList) {
				if (!(e instanceof IMob)) { continue; }
				entity.setPosition(e.posX - 0.5, e.posY + 2, e.posZ - 0.5);
				break;
			}
		}

        world.spawnEntity(entity);

		return false;
	}

	// 時間低速化魔法
	public boolean slowBuff (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
		double range = 8D + (level * 0.5D);

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(world, player, range, range, range);

		for (EntityLivingBase living : entityList) {

			if (living instanceof EntityPlayer) { continue; }
			living.addPotionEffect(new PotionEffect(PotionInit.slow, this.effectTime(level), level, true, false));
		}

		this.playSound(world, player, SMSoundEvent.REVERTIME, 1F, 1F);

		return true;
	}

	// 作物育成魔法
	public boolean growthAura (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		int level = 5;
		this.rangeGrow(world, new BlockPos(player), level);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, new BlockPos(player), level);
	}

	// 固定範囲作物育成魔法
	public boolean growthEffect (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		int level = 1;
		this.rangeGrow(world, new BlockPos(player), level);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, new BlockPos(player), level);
	}


	// 範囲成長
	public boolean rangeGrow (World world, BlockPos range, int level) {

		if(world.isRemote) { return false; }

		boolean isGrow = false;

		for (BlockPos pos : BlockPos.getAllInBoxMutable(range.add(-level, -2, -level), range.add(level, 2, level))) {

			//ブロックを取得するための定義
			IBlockState state = world.getBlockState(pos);
			Block crop = state.getBlock();

			if (!(crop instanceof IGrowable) || crop instanceof BlockGrass
					|| crop instanceof BlockDoublePlant|| crop instanceof BlockTallGrass
					|| crop instanceof MagiaFlower) { continue; }

			IGrowable grow = (IGrowable) crop;
			if (!grow.canGrow(world, pos, state, false)) { continue; }

			for (int i = 0; i < 8; i++) {
				if (!grow.canUseBonemeal(world, world.rand, pos, state)) { break; }

				grow.grow(world, world.rand, pos.toImmutable(), state);
			}

			isGrow = true;
			ParticleHelper.spawnBoneMeal(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
		}

		return isGrow;
	}

	// 広範囲作物育成魔法
	public boolean growthVerre (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) + 4;
		this.rangeGrow(world, new BlockPos(player), level);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, new BlockPos(player), level);
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {
		return IWand.getWand(stack).getChargeTick() >= 1;
	}

	public void onUpdate (World world, EntityPlayer player, ItemStack stack) {

		if (this.data != 2 && this.data != 3) { return; }
		if (!player.isHandActive()) { return; }

		if (player.motionY <= 0) {
			player.motionY *= 0.4;
			player.fallDistance = 0;
		}
	}
}
