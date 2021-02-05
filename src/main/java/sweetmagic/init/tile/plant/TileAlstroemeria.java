package sweetmagic.init.tile.plant;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.Particle;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.BlockInit;
import sweetmagic.init.block.crop.BlockAlstroemeria;
import sweetmagic.init.entity.monster.EntityZombieHora;
import sweetmagic.init.tile.magic.TileSMBase;

public class TileAlstroemeria extends TileSMBase {

	public static boolean flagTwilight = false;
	public boolean isSummon = false;
	public int nowTick = 0;

	public void update() {

		if (this.isSummon) {
			this.summonBoss();
		}

		if (this.world.isRemote) { return; }

		this.tickTime++;

		// Worldの時間は20分、Tick換算で24000となる。
		if (this.tickTime % 160 != 0) { return; }

		IBlockState state = this.getState(this.pos);
		Block block = state.getBlock();

		if (block != BlockInit.twilight_alstroemeria) { return; }

		int meta = BlockAlstroemeria.getNowStateMeta(state);
		long worldTime = this.world.getWorldTime() % 24000;

		// 夕方だったら
		flagTwilight = worldTime >= 11000 && worldTime < 14000 || this.world.provider.getDimension() == 7 || this.getBlock(this.pos.up()) == BlockInit.twilightlight;

		if ( (meta == 0 && flagTwilight) || (meta == 1 && !flagTwilight) ) {
			this.world.setBlockState(this.pos, BlockAlstroemeria.withStage(this.world, state, flagTwilight ? 1 : 0), 2);
		}

		this.tickTime = 0;
	}

	public void summonBoss () {

		this.nowTick++;

		if (this.nowTick % 16 == 0 && this.world.isRemote) {
			this.spwanParticle();
		}

		if (this.nowTick % 200 == 0) {

			this.nowTick = 0;
			this.isSummon = false;
			int x = this.pos.getX();
			int y = this.pos.getY();
			int z = this.pos.getZ();

			if (!this.world.isRemote) {

				EntityZombieHora entity = new EntityZombieHora(this.world);
				entity.setLocationAndAngles(x + 0.5D, y + 3D, z + 0.5D, 0, 0F);
				entity.pX = this.pos.getX();
				entity.pY = this.pos.getY();
				entity.pZ = this.pos.getZ();
				this.world.spawnEntity(entity);
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

		Random rand = world.rand;
		for (int i = 0; i < 4; ++i) {

			float f1 = this.pos.getX() + 0.5F;
			float f2 = this.pos.getY() + 1.25F + rand.nextFloat() * 0.5F + this.nowTick * 0.0065F;
			float f3 = this.pos.getZ() + 0.5F;
			float x = (rand.nextFloat() - rand.nextFloat()) * 0.15F;
			float z = (rand.nextFloat() - rand.nextFloat()) * 0.15F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, 0, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}


	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		super.writeNBT(tags);
		tags.setInteger("nowTick", this.nowTick);
		tags.setBoolean("isSummon", this.isSummon);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		super.readNBT(tags);
		this.nowTick = tags.getInteger("nowTick");
		this.isSummon = tags.getBoolean("isSummon");
	}
}
