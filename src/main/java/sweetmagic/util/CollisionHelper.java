package sweetmagic.util;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public class CollisionHelper {

	public static AxisAlignedBB getBlockAABB(EnumFacing face, FaceAABB aabb) {
		double[] fixeAABB = fixRot(face, aabb.x1, aabb.z1, aabb.x2, aabb.z2);
		return new AxisAlignedBB(fixeAABB[0], aabb.y1, fixeAABB[1], fixeAABB[2], aabb.y2, fixeAABB[3]);
	}

	public static AxisAlignedBB getBlockAABB(EnumFacing face, double x1, double y1, double z1, double x2, double y2, double z2) {
		double[] bounds = fixRot(face, x1, z1, x2, z2);
		return new AxisAlignedBB(bounds[0], y1, bounds[1], bounds[2], y2, bounds[3]);
	}

	private static double[] fixRot(EnumFacing face, double var1, double var2, double var3, double var4) {
		switch (face) {
		case WEST:
			double temp1 = var1;
			var1 = 1F - var3;
			double temp2 = var2;
			var2 = 1F - var4;
			var3 = 1F - temp1;
			var4 = 1F - temp2;
			break;
		case NORTH:
			double temp3 = var1;
			var1 = var2;
			var2 = 1F - var3;
			var3 = var4;
			var4 = 1F - temp3;
			break;
		case SOUTH:
			double temp4 = var1;
			var1 = 1F - var4;
			double temp5 = var2;
			var2 = temp4;
			double temp6 = var3;
			var3 = 1F - temp5;
			var4 = temp6;
			break;
		default:
			break;
		}
		return new double[] { var1, var2, var3, var4 };
	}
}
