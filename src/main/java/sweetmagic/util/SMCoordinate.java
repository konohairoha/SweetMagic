package sweetmagic.util;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class SMCoordinate {

	public final int x;
	public final int z;
	public final int dim;

	public SMCoordinate(int i, int j, int d) {
		this.x = i;
		this.z = j;
		this.dim = d;
	}

	public Chunk getChunk(World world) {
		return world.getChunkFromChunkCoords(this.x, this.z);
	}

	public boolean sameCood(int i, int j, int d) {
		return i == this.x && j == this.z && this.dim == d;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SMCoordinate) {
			SMCoordinate coord = (SMCoordinate) obj;
			return coord.x == x && coord.z == this.z && coord.dim == this.dim;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.x * 13 + this.z * 953;
	}
}
