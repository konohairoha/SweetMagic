package sweetmagic.init.item.sm.magic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IPlantable;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.ISMItem;
import sweetmagic.api.iitem.IWand;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.block.crop.MagiaFlower;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.init.entity.monster.EntityEnderShadow;
import sweetmagic.init.entity.projectile.EntityElectricMagic;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.eitem.SMType;
import sweetmagic.util.EventUtil;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.SMDamage;
import sweetmagic.util.WorldHelper;

public class ChargeMagic extends MFSlotItem {

	private final int data;
	private ResourceLocation icon;
	private final static String CYCLONEINT = "cyclonInt";
	private final static String GRAVITYINT = "gravityInt";

	public ChargeMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf) {
		super(name, SMType.CHARGE, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + name + ".png");
    }

	public ChargeMagic(String name, int meta, SMElement ele, int tier, int coolTime, int mf, String dir) {
		super(name, SMType.CHARGE, ele, tier, coolTime, mf, false);
        this.data = meta;
		this.icon = new ResourceLocation(SweetMagicCore.MODID,"textures/items/" + dir + ".png");
    }

	/**
	 * 0 = 風魔法
	 * 1 = 重力魔法
	 * 2 = 雷魔法
	 * 3 = 単体雷魔法
	 * 4 = クールタイム減少魔法
	 * 5 = 範囲作物成長魔法
	 * 6 = 小範囲作物成長魔法
	 * 7 = 作物広範囲魔法
	 * 8 = 無敵魔法
	 * 9 = 無敵魔法Ⅱ
	 * 10 = 作物成長&回収
	 * 11 = オシリス魔法
	 * 12 = 地震魔法
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
		case 8:
			toolTip.add("tip.magic_aether_shield.name");
			break;
		case 9:
			toolTip.add("tip.magic_aether_shield2.name");
			break;
		case 10:
			toolTip.add("tip.magic_growth_collect.name");
			break;
		case 11:
			toolTip.add("tip.magic_thunderforce.name");
			break;
		case 12:
			toolTip.add("tip.magic_earthquake.name");
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
			// クールタイム減少魔法
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
		case 8:
		case 9:
			// 無敵魔法
			flag = this.aetherShield(world, player, stack, tags);
			break;
		case 10:
			// 作物成長&回収
			flag = this.growthCollect(world, player, stack, tags);
			break;
		case 11:
			// オシリス魔法
			flag = this.thunderForceAction(world, player, stack, tags);
			break;
		case 12:
			// 地震魔法
			flag = this.earthquakeAction(world, player, stack, tags);
			break;
		}

		return flag;
	}

	// 風魔法
	public boolean cyclonAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
        double range = isRange ? 20D : 10D;

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(range);
		WorldHelper.addEffect(world, aabb, PotionInit.cyclone, this.effectTime(level), 0, EnumParticleTypes.SWEEP_ATTACK);

		// 疾風を与える
		this.addPotion(player, PotionInit.cyclone, this.effectTime(level), 1);
		world.playSound(null, player.getPosition(), SMSoundEvent.CYCLON, SoundCategory.NEUTRAL, 3F, 1F);
		player.getEntityData().setInteger(CYCLONEINT, 5);
		return true;
	}

	// 重力魔法
	public boolean gravityAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
        double range = isRange ? 20D : 10D;

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);

		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(range);
		WorldHelper.addEffect(world, aabb, PotionInit.gravity_accele, this.effectTime(level), 0, EnumParticleTypes.FALLING_DUST);

		// 疾風を与える
		this.addPotion(player, PotionInit.gravity_accele, this.effectTime(level), 1);

		// パーティクルを出す
		world.playSound(null, player.getPosition(), SMSoundEvent.GRAVITY, SoundCategory.NEUTRAL, 1.5F, 1F);
		player.getEntityData().setInteger(GRAVITYINT, 5);
		return true;
	}

	// 雷魔法
	public boolean elecAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (world.isRemote) { return false; }

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
		double range = (isRange ? 8D : 20D) + (level * 0.5D);

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, player, range, range, range);

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

		return true;
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
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, player, 7.5D, 3D, 7.5D);

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
		float rate = 1F + Math.min(1F, level * 0.5F);
		CooldownTracker cool = player.getCooldownTracker();
		List<ItemStack> magicList = wand.getMagicList(player, stack);

		for (ItemStack magic : magicList) {

			// クールタイムが付いてないなら次へ
			Item item = magic.getItem();
			if (!cool.hasCooldown(item) || item == this) { continue; }

			// クールタイムの取得
			float coolTime = cool.getCooldown(item, 0F) * ((ISMItem) item).getCoolTime();

			// クールタイムの再設定
			cool.removeCooldown(item);
			cool.setCooldown(item, (int) (coolTime / rate));
		}

		this.playSound(world, player, SMSoundEvent.REVERTIME, 1F, 1F);
		cool.setCooldown(stack.getItem(), 120);

		return true;
	}

	// 作物育成魔法
	public boolean growthAura (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);

		// 杖の取得
		int level = isRange ? 10 : 5;
		this.rangeGrow(world, player.getPosition(), level, 3);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, player.getPosition(), level, 3);
	}

	// 固定範囲作物育成魔法
	public boolean growthEffect (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);

		int level = isRange ? 5 : 1;
		this.rangeGrow(world, player.getPosition(), level, 3);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, player.getPosition(), level, 3);
	}


	// 範囲成長
	public boolean rangeGrow (World world, BlockPos range, int level, int valaue) {

		if(world.isRemote) { return false; }

		boolean isGrow = false;

		for (BlockPos pos : BlockPos.getAllInBoxMutable(range.add(-level, -level, -level), range.add(level, level, level))) {

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


			ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.VILLAGER_HAPPY, valaue);
		}

		return isGrow;
	}

	// 広範囲作物育成魔法
	public boolean growthVerre (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) + (isRange ? 14 : 4);
		this.rangeGrow(world, player.getPosition(), level, 3);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);
		return this.rangeGrow(world, player.getPosition(), level, 3);
	}


	// 無敵魔法
	public boolean aetherShield (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		int maxTime = this.data == 8 ? 300 : 500;
		int time = (int) (maxTime * IWand.getWand(stack).getChargeTick());
		this.addPotion(player, PotionInit.aether_shield, time, this.data == 8 ? 0 : 1);
		this.playSound(world, player, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 1F, 1.175F);
		player.getCooldownTracker().setCooldown(stack.getItem(), time);

		return true;
	}

	// 作物成長&回収
	public boolean growthCollect (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack) +  (isRange ? 18 : 4);
		BlockPos pos = player.getPosition();
		this.rangeGrow(world, pos, level, 1);
		this.rangeGrow(world, pos, level, 1);
		boolean flag = this.rangeGrow(world, pos, level, 1);
		this.playSound(world, player, SMSoundEvent.GROW, 0.425F, 1F);

		if (!world.isRemote) {
			this.plantCollect(world, pos, player, stack, level);
		}

		return flag;
	}

	// オシリス魔法
	public boolean thunderForceAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (world.isRemote) { return false; }

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
		double range = (isRange ? 12D : 24D) + (level * 0.75D);
		double damage = wand.getMagicList(player, stack).size() * this.getPower(level) * 0.25D;

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, player, range, range, range);

		for (EntityLivingBase living : entityList) {

			if (!(living instanceof IMob)) { continue; }

			EntityElectricMagic entity = new EntityElectricMagic(world, player, stack, 2);
			entity.setDamage(entity.getDamage() + damage);
			entity.setHeadingFromThrower(player, player.rotationPitch, player.rotationYaw, 0.0F, 2.5F, 0.0F);	//　弾の初期弾速と弾のばらつき
			entity.shoot(entity.motionX, entity.motionY, entity.motionZ, 0, 0);									// 射撃速度
			entity.motionY -= 1.5D;
			entity.setPosition(living.posX, living.posY + 2, living.posZ);
	        world.spawnEntity(entity);

		}

		return true;
	}


	// 地震魔法
	public boolean earthquakeAction (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		// 杖の取得
		IWand wand = IWand.getWand(stack);
		int level = IWand.getLevel(wand, stack);
        boolean isRange = this.hasAcce(player, ItemInit.extension_ring);
		double range = (isRange ? 8D : 12D) + (level * 0.75D);
		float dame = 6F + level * 0.67F;

		// 範囲内のえんちちーを取得
		List<EntityLivingBase> entityList = this.getEntityList(EntityLivingBase.class, player, range, range, range);

		for (EntityLivingBase living : entityList) {

			if (!(living instanceof IMob)) { continue; }

			this.attackDamage(player, living, dame);
			this.checkShadow(living);
			living.hurtResistantTime = 0;

			this.attackDamage(player, living, dame * 0.5F);
			living.hurtResistantTime = 0;

			float armorValue = (float) living.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue() * 2F;

			if (armorValue > 0F) {
				this.attackDamage(player, living, armorValue);
				living.hurtResistantTime = 0;
			}

			// 地面に付いている場合1秒間停止
			if (living.onGround) {
				if (living instanceof EntityLiving) {
					EventUtil.tameAIDonmov((EntityLiving) living, 1);
				}

				living.motionY += 0.75D;
			}

			// 浮いている敵なら地面に落として追加ダメージ
			else {
				living.motionY -= 2D;
				this.attackDamage(player, living, 6F);
				living.hurtResistantTime = 0;
			}

		}

		this.playSound(world, player, SoundEvents.BLOCK_ANVIL_PLACE, 0.5F, 0.25F);

		if (world instanceof WorldServer) {

			BlockPos pos = player.getPosition();
			for (BlockPos p : BlockPos.getAllInBox(pos.add(-4, -1, -4), pos.add(4, -1, 4))) {

				if (world.getBlockState(p).getBlock() == Blocks.AIR) { continue; }

				ParticleHelper.spawnParticle(world, p.up(), EnumParticleTypes.FALLING_DUST);
			}
		}

		return true;
	}

	public void plantCollect (World world, BlockPos pos, EntityPlayer player, ItemStack stack, int level) {

		Random rand = world.rand;

		// 範囲とstackListの初期化
		List<ItemStack> stackList = new ArrayList();

		// 範囲分回す
		for (BlockPos p : BlockPos.getAllInBoxMutable(pos.add(-level, -level, -level), pos.add(level, level, level))) {

			Item item = null;
			IBlockState plant = null;
			IBlockState state = world.getBlockState(p);
			Block b = state.getBlock();
			List<ItemStack> dropList = new ArrayList<>();

			if (b == Blocks.AIR || b == Blocks.DIRT || b == Blocks.GRASS || b == Blocks.STONE || b == Blocks.PUMPKIN_STEM || b == Blocks.MELON_STEM) { continue; }

			// まずはスイマジ作物なら右クリック処理を呼び出し
			if (b instanceof ISMCrop) {
				if (!((IGrowable) b).canGrow(world, p, state, false)) {
					((ISMCrop) b).getPickPlant(world, player, p, stack);
				}
			}

			// 通常の作物なら
			else if (b instanceof IGrowable) {
				if (!((IGrowable) b).canGrow(world, p, state, false)) {
					dropList = b.getDrops(world, p, state, 0);
					ItemHelper.compactStackList(dropList);
				}
			}

			// リストが空なら終了
			if (dropList.isEmpty()) { continue; }

			// 作物の種の取得
			if (b instanceof BlockBush) {
				List<ItemStack> itemList = b.getDrops(world, p, b.getDefaultState(), 0);
				if (!itemList.isEmpty()) {
					item = itemList.get(0).getItem();
				}
			}

			// ドロップリスト分回す
			for (ItemStack drop : dropList) {

				Item dropItem = drop.getItem();

				// 取得した種を植える
				if (plant == null && item != null && item == dropItem && dropItem instanceof IPlantable) {
					drop.shrink(1);
					plant = ((IPlantable) item).getPlant(world, p);
				}
			}

			// stackListに追加
			ItemHelper.compactStackList(dropList);
			stackList.addAll(dropList);
			this.breakBlock(world, p);
			this.playCropSound(world, rand, p);

			if (plant == null && b == Blocks.WHEAT) {
				plant = Blocks.WHEAT.getDefaultState();
			}

			if (plant != null) {
				world.setBlockState(p, plant, 2);
			}
		}

		// Listが空なら終了
		if (stackList.isEmpty()) { return; }

		// リスト分スポーン
		for (ItemStack s : stackList) {
			world.spawnEntity(new EntityItem(world, player.posX + 0.5D, player.posY, player.posZ + 0.5D, s));
		}
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos) {
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

	// 作物回収時の音
	public void playCropSound (World world, Random rand, BlockPos pos) {
        world.playSound(null, pos, SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
	}

	// 攻撃
	public boolean attackDamage (EntityPlayer player, EntityLivingBase entity, float dame) {

		// エンダーマンなら
		if (entity instanceof EntityEnderman) {
			DamageSource src = DamageSource.causePlayerDamage(player);
			return entity.attackEntityFrom(src, dame);
		}

		return entity.attackEntityFrom(SMDamage.MagicDamage(player, player), dame);
	}

	// エンダーシャドーの分身なら
	public void checkShadow (EntityLivingBase entity) {
		if (entity instanceof EntityEnderShadow) {
			EntityEnderShadow ender = (EntityEnderShadow) entity;
			if (ender.isShadow) {
				ender.setDead();
			}
		}
	}

	@Override
	public boolean canItemMagic (World world, EntityPlayer player, ItemStack stack, NBTTagCompound tags) {

		if (this.data == 8 || this.data == 9) {
			return true;
		}

		IWand wand = IWand.getWand(stack);
		return wand.getChargeTick() >= 1 || wand.isEquipment(player, ItemInit.magician_quillpen);
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
