package sweetmagic.init.tile.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleOrb2;
import sweetmagic.init.BlockInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.block.crop.BlockAlstroemeria;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.entity.monster.EntityZombieKronos;
import sweetmagic.init.tile.magic.TileSMBase;

public class TileAlstroemeria extends TileSMBase {

	private EntityLivingBase entity = null;
	public static boolean flagTwilight = false;
	public boolean isSummon = false;
	public int mobData = 0;
	public int nowTick = 0;

	public void update() {

		if (this.isSummon) {
			this.summonBoss();
		}

		if (!this.isSever()) { return; }

		// Worldの時間は20分、Tick換算で24000となる。
		this.tickTime++;
		if (this.tickTime % 160 != 0) { return; }

		IBlockState state = this.getState(this.pos);
		Block block = state.getBlock();
		if (block != BlockInit.twilight_alstroemeria) { return; }

		int meta = ((BlockAlstroemeria) block).getNowStateMeta(state);
		long worldTime = this.world.getWorldTime() % 24000;

		// 夕方か黄昏の森か黄昏時の明かりがあれば
		flagTwilight = worldTime >= 9500 && worldTime <= 15500 || this.world.provider.getDimension() == 7 || this.getBlock(this.pos.up()) == BlockInit.twilightlight;

		if ( (meta == 0 && flagTwilight) || (meta == 1 && !flagTwilight) ) {
			TileEntity tile = this;
			this.world.setBlockState(this.pos, ((BlockAlstroemeria) block).withStage(this.world, state, flagTwilight ? 1 : 0), 2);

	        if (tile != null){
	            tile.validate();
	            this.world.setTileEntity(this.pos, tile);
	        }
		}

		this.tickTime = 0;
	}

	public void summonBoss () {

		this.nowTick++;

		if (this.nowTick % 10 == 0 && !this.isSever()) {
			this.spwanParticle();
		}

		if (this.nowTick % 200 == 0) {

			this.nowTick = 0;
			this.isSummon = false;
			int x = this.pos.getX();
			int y = this.pos.getY();
			int z = this.pos.getZ();

			if (this.isSever()) {

				if (this.mobData == 1) {
					EntityZombieHora entity = new EntityZombieHora(this.world);
					entity.setLocationAndAngles(x + 0.5D, y + 1.75D, z + 0.5D, 0, 0F);
					entity.pX = this.pos.getX();
					entity.pY = this.pos.getY();
					entity.pZ = this.pos.getZ();
					entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
					this.world.spawnEntity(entity);
				}

				else {

					EntityZombieKronos entity = new EntityZombieKronos(this.world);
					entity.setLocationAndAngles(x + 0.5D, y + 1.75D, z + 0.5D, 0, 0F);
					entity.pX = this.pos.getX();
					entity.pY = this.pos.getY();
					entity.pZ = this.pos.getZ();
					entity.addPotionEffect(new PotionEffect(PotionInit.magic_array, 60, 0));
					entity.addPotionEffect(new PotionEffect(PotionInit.refresh_effect, 99999, 0));
					entity.addPotionEffect(new PotionEffect(PotionInit.resistance_blow, 99999, 9));
					entity.setAcceSize(3);
					entity.setIsSpecial(false);
					this.world.spawnEntity(entity);
				}
				this.playSound(this.pos, SoundEvents.ENTITY_WITHER_SPAWN, 1F, 1F);
			}

			else {

				double d1 = x + this.world.rand.nextFloat();
				double d2 = (double) (y + this.world.rand.nextFloat()) + 5;
				double d3 = z + this.world.rand.nextFloat();
				this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, d1, d2, d3, 0, 0, 0);
			}

		}
	}

	public void spwanParticle () {

		int count = this.nowTick / 10 * this.mobData;
		Random rand = this.world.rand;
		for (int i = 0; i < count; ++i) {

			float randX = (rand.nextFloat() - rand.nextFloat()) * 1.5F;
			float randY = (rand.nextFloat() - rand.nextFloat()) * 1.5F;
			float randZ = (rand.nextFloat() - rand.nextFloat()) * 1.5F;

			float x = this.pos.getX() + 0.5F + randX;
			float y = this.pos.getY() + 1.5F + this.nowTick * 0.008F + randY;
			float z = this.pos.getZ() + 0.5F + randZ;
			float xSpeed = -randX * 0.075F;
			float ySpeed = -randY * 0.075F;
			float zSpeed = -randZ * 0.075F;

			Particle effect = ParticleOrb2.create(this.world, x, y, z, xSpeed, ySpeed, zSpeed, 82, 66, 255);
			this.getParticle().addEffect(effect);
		}
	}

	// えんちちーの取得
	public EntityLivingBase getEntity () {

		// 生成済みならすぐ返す
		if (this.entity != null) { return this.entity; }

		this.entity = this.mobData == 1 ? new EntityZombieHora(this.world) : new EntityZombieKronos(this.world);
		this.entity.ticksExisted = 0;

		return this.entity;
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("nowTick", this.nowTick);
		tags.setBoolean("isSummon", this.isSummon);
		tags.setInteger("mobData", this.mobData);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.nowTick = tags.getInteger("nowTick");
		this.isSummon = tags.getBoolean("isSummon");
		this.mobData = tags.getInteger("mobData");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
