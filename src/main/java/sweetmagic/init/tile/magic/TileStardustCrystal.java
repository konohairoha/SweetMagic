package sweetmagic.init.tile.magic;

import java.util.Random;

import net.minecraft.client.particle.Particle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.client.FMLClientHandler;
import sweetmagic.client.particle.ParticleNomal;

public class TileStardustCrystal extends TileSMBase {

	public int pX;
	public int pY;
	public int pZ;
	public int nowY;
	public boolean isNew = false;

	public void clientUpdate () {

		this.tickTime++;
		if (this.tickTime % 20 != 0) { return; }

		this.tickTime = 0;
		this.spawnParticle();
	}

	public void spawnParticle() {

		Random rand = this.world.rand;
		float posX = this.pos.getX();
		float posY = this.pos.getY();
		float posZ = this.pos.getZ();

		for (int i = 0; i < 6; i++) {
			float f1 = posX + rand.nextFloat();
			float f2 = posY + 0.3F + rand.nextFloat() * 0.5F;
			float f3 = posZ + rand.nextFloat();
			float x = (rand.nextFloat() - rand.nextFloat()) * 0.09F;
			float y = (rand.nextFloat() + rand.nextFloat()) * 0.08F;
			float z = (rand.nextFloat() - rand.nextFloat()) * 0.09F;

			Particle effect = new ParticleNomal.Factory().createParticle(0, this.world, f1, f2, f3, x, y, z);
			FMLClientHandler.instance().getClient().effectRenderer.addEffect(effect);
		}
	}

	@Override
	public NBTTagCompound writeNBT(NBTTagCompound tags) {
		tags.setInteger("pX", this.pX);
		tags.setInteger("pY", this.pY);
		tags.setInteger("pZ", this.pZ);
		tags.setInteger("nowY", this.nowY);
		tags.setBoolean("isNew", this.isNew);
		return tags;
	}

	@Override
	public void readNBT(NBTTagCompound tags) {
		this.pX = tags.getInteger("pX");
		this.pY = tags.getInteger("pY");
		this.pZ = tags.getInteger("pZ");
		this.nowY = tags.getInteger("nowY");
		this.isNew = tags.getBoolean("isNew");
	}
}
