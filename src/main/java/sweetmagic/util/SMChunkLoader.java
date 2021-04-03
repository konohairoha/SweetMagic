package sweetmagic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import sweetmagic.SweetMagicCore;

public class SMChunkLoader implements LoadingCallback {

	// インスタンス
	private static SMChunkLoader instance;

	// 稼働中のチケット
	protected static final HashMap<SMCoordinate, Ticket> ticketList = new HashMap<SMCoordinate, Ticket>();

	// 発行済みの全チケット
	protected static final ArrayList<Ticket> allTicket = new ArrayList<Ticket>();

	private SMChunkLoader() {}

	public static SMChunkLoader getInstance() {
		if (instance == null) {
			instance = new SMChunkLoader();
		}
		return instance;
	}

	public void preInit() {
		ForgeChunkManager.setForcedChunkLoadingCallback(SweetMagicCore.INSTANCE, instance);
	}

	@Override
	public void ticketsLoaded(List<Ticket> ticketList, World world) {

		for (Ticket ticket : ticketList) {

			if (!this.isBlockTicket(ticket)) { return; }

			int x = ticket.getModData().getInteger("x");
			int y = ticket.getModData().getInteger("y");
			int z = ticket.getModData().getInteger("z");

			int i = ticket.getModData().getInteger("i");
			int j = ticket.getModData().getInteger("j");
			int d = ticket.getModData().getInteger("d");

			if (this.getBlock(ticket, world) instanceof IChunkBlock) {

				IChunkBlock block = (IChunkBlock) this.getBlock(ticket, world);

				if (block.canLoad(world, x, y, z)) {
					setBlockTicket(world, x, y, z, i, j, d);
				}

				else {
					deleteBlockTicket(world, x, y, z, i, j, d);
				}
			}

			else {
				deleteBlockTicket(world, x, y, z, i, j, d);
			}
		}
	}

	// 指定した座礁のブロックをChunkLoaderとして起動する
	public static boolean setBlockTicket(World world, int x, int y, int z, int i, int j, int d) {

		SMCoordinate cood = new SMCoordinate(i, j, d);
		Ticket ticket = ForgeChunkManager.requestTicket(SweetMagicCore.INSTANCE, world, Type.NORMAL);
		if (ticket == null) { return false; }

		setBlockType(ticket);
		setBlock(ticket, x, y, z, i, j, d);
		ticketList.put(cood, ticket);
		ForgeChunkManager.forceChunk(ticket, world.getChunkFromChunkCoords(i, j).getPos());
		return true;
	}

	// 指定した座礁のChunkLoaderを停止する
	public static void deleteBlockTicket(World world, int x, int y, int z, int i, int j, int d) {

		SMCoordinate cood = new SMCoordinate(i, j, d);
		if (!ticketList.containsKey(cood)) { return; }

		if (!ForgeChunkManager.getPersistentChunksFor(ticketList.get(cood).world).isEmpty()) {
			ForgeChunkManager.unforceChunk(ticketList.get(cood), world.getChunkFromChunkCoords(i, j).getPos());
			ForgeChunkManager.releaseTicket(ticketList.get(cood));
		}

		ticketList.remove(cood);
	}

	public static void setBlockType(Ticket ticket) {
		ticket.getModData().setString("type", "dcblock");
	}

	public boolean isBlockTicket(Ticket ticket) {
		return ticket.getModData().getString("type").equals("dcblock");
	}

	public static void setBlock(Ticket ticket, int x, int y, int z, int i, int j, int d) {
		ticket.getModData().setInteger("x", x);
		ticket.getModData().setInteger("y", y);
		ticket.getModData().setInteger("z", z);
		ticket.getModData().setInteger("i", i);
		ticket.getModData().setInteger("j", j);
		ticket.getModData().setInteger("d", d);
	}

	public Block getBlock(Ticket ticket, World world) {
		BlockPos pos = new BlockPos(ticket.getModData().getInteger("x"), ticket.getModData().getInteger("y"), ticket.getModData().getInteger("z"));
		return world.getBlockState(pos).getBlock();
	}

	// Ticketの使い回し
	private static Ticket getTicketFromList(SMCoordinate cood) {

		if (allTicket.isEmpty()) { return null; }

		for (Ticket ticket : allTicket) {
			int i = ticket.getModData().getInteger("i");
			int j = ticket.getModData().getInteger("j");
			int d = ticket.getModData().getInteger("d");

			SMCoordinate cood1 = new SMCoordinate(i, j, d);

			if (cood1.equals(cood)) { return ticket; }
		}

		return null;
	}

	// worldがロードされた時に呼ばれる。trueを返すとChunkLoaderが始まる
	public interface IChunkBlock {

		public boolean canLoad(World world, int x, int y, int z);
	}
}
