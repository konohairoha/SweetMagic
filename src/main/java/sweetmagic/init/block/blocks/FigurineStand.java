package sweetmagic.init.block.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySkeletonHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.entity.monster.EntityAncientFairy;
import sweetmagic.init.entity.monster.EntityBraveSkeleton;
import sweetmagic.init.entity.monster.EntityElshariaCurious;
import sweetmagic.init.entity.monster.EntityFairySkyDwarfKronos;
import sweetmagic.init.entity.monster.EntityIfritVerre;
import sweetmagic.init.entity.monster.EntityRepairKitt;
import sweetmagic.init.entity.monster.EntitySandryon;
import sweetmagic.init.entity.monster.EntityWindineVerre;
import sweetmagic.init.entity.monster.EntityWitchMadameVerre;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.entity.monster.ISMMob;
import sweetmagic.init.tile.magic.TileFigurineStand;

public class FigurineStand extends BaseFaceBlock {

	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.1875D, 0D, 0.1875D, 0.8125D, 0.625D, 0.8125D);

	public FigurineStand(String name, int data) {
		super(Material.WOOD, name);
		setHardness(0.2F);
        setResistance(1024F);
		setSoundType(SoundType.METAL);
		this.data = data;
		BlockInit.magicList.add(this);
	}

	/**
	 * 0 = ウィッチマスター
	 * 1 = ウィンディーネ
	 * 2 = イフリート
	 * 3 = ブレイブスケルトン
	 * 4 = サンドリヨン
	 * 5 = ホーラ
	 * 6 = エンシェントフェアリー
	 * 7 = キット
	 * 8 = キュリオス
	 * 9 = クロノス
	 */

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFigurineStand();
	}

	// 右クリックの処理
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

//		if (!world.isRemote) {
//			return true;
//		}

		TileFigurineStand tile = (TileFigurineStand) world.getTileEntity(pos);
		EntityLivingBase entity = tile.getEntity();
		ISMMob sm = (ISMMob) entity;
		boolean isSpe = sm.getSpecial();

		if (this.data == 3) {

			EntityLivingBase sub = tile.getSubEntity();

			// 騎乗していなければ馬の生成
			if (sub == null) {
				tile.setSubEntity(this.getSub(world, pos));
			}

			// 騎乗解除して馬を削除
			else {
				tile.setSubEntity(null);
				entity.dismountRidingEntity();
			}
		}

		else  if (this.data == 6) {
			if (world instanceof WorldServer) {
				((WorldServer) world).spawnParticle(EnumParticleTypes.SPELL_WITCH, pos.getX() + 0.5D, pos.getY() + 0.75D, pos.getZ() + 0.5D, 24, -0.1D, 0, -0.1D, 0.025D, 0, 0);
			}
		}

		else if (this.data == 9) {
			tile.setEntity(null);
			tile.setSpecial(!isSpe);
			tile.setEntity(this.getEntity(tile, world));
			tile.setSpecial(isSpe);
			sm = (ISMMob) tile.getEntity();
		}

//		// サーバーならクライアントに通知
//		if (world.isRemote) {
//			System.out.println("=====");
//		}

		sm.setSpecial(!isSpe);
		tile.setSpecial(!isSpe);
		tile.markDirty();
		world.notifyBlockUpdate(pos, state, state, 3);

//		PacketHandler.sendToServer(new FigurineStandPKT(!isSpe, pos));

		return true;
	}

	public EntityLivingBase getEntity (TileFigurineStand tile, World world) {

		EntityLivingBase entity = null;

		switch (this.data) {
		case 0:
			entity = new EntityWitchMadameVerre(world);
			break;
		case 1:
			entity = new EntityWindineVerre(world);
			break;
		case 2:
			entity = new EntityIfritVerre(world);
			break;
		case 3:
			entity = new EntityBraveSkeleton(world);
			entity.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
			entity.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.BOW));
			break;
		case 4:
			entity = new EntitySandryon(world);
			break;
		case 5:
			entity = new EntityZombieHora(world);
			break;
		case 6:
			entity = new EntityAncientFairy(world);
			break;
		case 7:
			entity = new EntityRepairKitt(world);
			break;
		case 8:
			entity = new EntityElshariaCurious(world);
			break;
		case 9:
			entity = tile.getSpecial() ? new EntityFairySkyDwarfKronos(world) : new EntityZombieKronos(world);
			break;
		}

		entity.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(512D);

		return entity;
	}

	public EntitySkeletonHorse getSub (World world, BlockPos pos) {

		EntitySkeletonHorse entity = null;
		TileFigurineStand tile = (TileFigurineStand) world.getTileEntity(pos);

		switch (this.data) {
		case 3:

			EntityLivingBase main = tile.getEntity();
			entity = new EntitySkeletonHorse(world);
			entity.setOwnerUniqueId(main.getUniqueID());
			entity.setTemper(100);
			entity.setHorseTamed(true);
			main.startRiding(entity, true);
			break;
		}

		return entity;
	}

	public double getPosY (World world, BlockPos pos) {

		double posY = 0.075D;

		if (this.data >= 5 && this.data <= 9) {

			EntityLivingBase entity = ((TileFigurineStand) world.getTileEntity(pos)).getEntity();
			ISMMob sm = (ISMMob) entity;

			if ( this.data == 7 && sm.getSpecial() ) {
				posY = 0.75D + MathHelper.sin(entity.ticksExisted * 0.15F) * 0.075D;
			}

			else {
				posY = 0.3D + MathHelper.sin(entity.ticksExisted * ( 0.15F + this.data * 0.01F) ) * 0.075D;
			}
		}

		return posY;
	}
}
