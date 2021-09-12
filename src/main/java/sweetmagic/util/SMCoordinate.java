package sweetmagic.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SMCoordinate {

	public final int x;
	public final int z;
	public final int dim;

	public SMCoordinate(int x, int y, int dim) {
		this.x = x;
		this.z = y;
		this.dim = dim;
	}

	public Chunk getChunk(World world) {
		return world.getChunkFromChunkCoords(this.x, this.z);
	}

	public boolean sameCood(int x, int z, int dim) {
		return x == this.x && z == this.z && this.dim == dim;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SMCoordinate) {
			SMCoordinate coord = (SMCoordinate) obj;
			return coord.x == this.x && coord.z == this.z && coord.dim == this.dim;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.x * 13 + this.z * 953;
	}
}
