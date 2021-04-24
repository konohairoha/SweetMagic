package sweetmagic.init.entity.projectile;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleMagicDig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.util.EventUtil;
import sweetmagic.util.PlayerHelper;

public class EntityDigMagic extends EntityBaseMagicShot {

	public EntityDigMagic(World world) {
		super(world);
		this.setSize(0.5F, 0.5F);
	}

	public EntityDigMagic(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public EntityDigMagic(World world, EntityLivingBase thrower, ItemStack stack, int data) {
		super(world, thrower, stack);
		this.data = data;
	}

	/**
	 * 0 = tier1
	 * 1 = tier2
	 * 2 = tier3
	 * 3 = tier4
	 */

	// 地面についたときの処理
	@Override
	protected void inGround(RayTraceResult result) {

		if (this.world.isRemote || !PlayerHelper.isPlayer(this.thrower) || this.getThrower().isPotionActive(PotionInit.breakblock)) { return; }

		try {

			if (result.typeOfHit != Type.BLOCK) { return; }

			// 座標取得の定義
			BlockPos pos = result.getBlockPos();
			IBlockState state = this.world.getBlockState(pos);
			Material material = state.getMaterial();
			boolean nearDrop = IWand.getWand(this.stack).getLevel(this.stack) >= 3;

			// 1ブロック破壊
			if (this.data == 0) {

				// 破壊可能なブロックかどうか
				if (this.canBreakBlock(state.getBlock(), material)) { return; }
				this.breakBlock(pos, nearDrop);
			}

			// 範囲破壊
			else if (this.data == 1) {

				int area = 1, xa = 0, za = 0, ya = 0;

	            switch (result.sideHit) {
	                case UP:
	                	ya = -1;
	                	break;
	                case DOWN:
	                	ya = 1;
	                	break;
	                case NORTH:
	                	za = 1;
	                	break;
	                case SOUTH:
	                	za = -1;
	                	break;
	                case EAST:
	                	xa = -1;
	                	break;
	                case WEST:
	                	xa = 1;
	                	break;
	            }

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-area + xa, -area + ya, -area + za), pos.add(area + xa, area + ya, area + za))) {
					IBlockState target = world.getBlockState(p);
					Material mate2 = state.getMaterial();
					if (this.canBreakBlock(target.getBlock(), mate2)) { continue; }
					this.breakBlock(p, nearDrop);
				}
			}

			// 連続範囲破壊
			else if (this.data == 2 || this.data == 3) {

				int xa = 0, za = 0, ya = 0;
				int areaX = 0, areaZ = 0, areaY = 0;

				int range = this.data == 3 ? 2 : 1;
				int inside = this.data == 3 ? 15 : 9;

	            switch (result.sideHit) {
	                case UP:
	                	xa = range;
	                	ya = inside;
	                	areaY = -inside;
	                	za = range;
	                	break;
	                case DOWN:
	                	xa = range;
	                	ya = -inside;
	                	areaY = inside;
	                	za = range;
	                	break;
	                case NORTH:
	                	xa = range;
	                	ya = range;
	                	za = -inside;
	                	areaZ = inside;
	                	break;
	                case SOUTH:
	                	xa = range;
	                	ya = range;
	                	za = inside;
	                	areaZ = -inside;
	                	break;
	                case EAST:
	                	xa = inside;
	                	areaX = -inside;
	                	ya = range;
	                	za = range;
	                	break;
	                case WEST:
	                	xa = -inside;
	                	areaX = inside;
	                	ya = range;
	                	za = range;
	                	break;
	            }

				for (BlockPos p : BlockPos.getAllInBox(pos.add(-xa, -ya, -za), pos.add(xa + areaX, ya + areaY, za + areaZ))) {

					IBlockState target = this.world.getBlockState(p);
					Block block = target.getBlock();

					//空気ブロックとたいるえんちちーなら何もしない
					if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

					Material mate2 = state.getMaterial();
					if (this.canBreakBlock(target.getBlock(), mate2)) { continue; }
					this.breakBlock(p, nearDrop);
				}

			}

			// 経験値追加処理
			this.addExp();

		} catch (Throwable e) {
			this.setEntityDead();
			return;
		}

		this.world.setEntityState(this, (byte) 3);
		this.setEntityDead();
	}

	// 破壊可能なブロックかどうか
	public boolean canBreakBlock (Block block, Material material) {
		return material == Material.WATER || material == Material.LAVA || block == Blocks.BEDROCK || block == BlockInit.spawn_stone;
	}

	// パーティクルスポーン
	@Override
	protected void spawnParticle() {
		Particle effect = new ParticleMagicDig.Factory().createParticle(0,this. world, this.posX, this.posY, this.posZ, this.posX, this.posY, this.posZ);
		FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
	}

	// えんちちーに当たった時の処理
	@Override
	protected void entityHit(EntityLivingBase living) {

		int level = this.getWandLevel();

		if (this.isPlayer(living)) {
			living.addPotionEffect(new PotionEffect(MobEffects.HASTE, 100 * level, 0));
		}

		else {
			living.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 100 * level, 0));
		}

		// 弱体化がついてたら
		if (living.isPotionActive(MobEffects.WEAKNESS) && living instanceof EntityLiving) {
			living.removePotionEffect(MobEffects.WEAKNESS);
			EventUtil.tameAIDonmov((EntityLiving)living, 3); // 敵を動かなくさせる
		}
	}

	// 自然消滅までの時間 30tick + this.plusTickAir(増やしたい場合は-10とか付ければおっけー)
	protected int plusTickAir() {
		return -10;
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, boolean nearDrop) {

        IBlockState state = this.world.getBlockState(pos);
        Block block = state.getBlock();
		if (block == Blocks.AIR) { return false; }

		this.world.playEvent(2001, pos, Block.getStateId(state));
		BlockPos pos2 = nearDrop ? new BlockPos(this.thrower) : pos;

		if (this.data == 2) {
			if (!this.world.isRemote) {
				for (ItemStack stack : Lists.newArrayList(new ItemStack(block, 1, block.getMetaFromState(state)))) {
					this.world.spawnEntity(new EntityItem(this.world, pos2.getX(), pos2.getY(), pos2.getZ(), stack));
				}
			}
		}

		else {
	        block.dropBlockAsItem(this.world, pos2, state, this.data);
		}

        return this.world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }
}
