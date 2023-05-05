package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.api.iitem.IRangePosTool;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.item.sm.magic.AetherHammer;
import sweetmagic.init.item.sm.magic.StarLightWand;
import sweetmagic.packet.StarLightPKT;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.RenderUtils;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class StarLightWandEvent {

	private static final List<AxisAlignedBB> renderList = new ArrayList<>();
	private static double x, y, z;
	private static final String REPLACE = "replace";
	private static final String STARTX = "sX";
	private static final String STARTY = "sY";
	private static final String STARTZ = "sZ";
	private static final String ENDX = "eX";
	private static final String ENDY = "eY";
	private static final String ENDZ = "eZ";

    @SubscribeEvent
    public static void onLeftClickBlockWand(PlayerInteractEvent.LeftClickBlock event) {

        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof StarLightWand) && !(stack.getItem() instanceof AetherHammer)) { return; }

        boolean isHammer = stack.getItem() instanceof AetherHammer;
		BlockPos pos = event.getPos();
		NBTTagCompound tags = ItemHelper.getNBT(stack);

		// 設置モードなら触れた面の座標
		if (!tags.getBoolean(REPLACE) && !isHammer) {
			pos = pos.offset(event.getFace());
		}

		// スニークしてるなら終点を登録
		if (player.isSneaking()) {
			tags.setInteger(ENDX, pos.getX());
			tags.setInteger(ENDY, pos.getY());
			tags.setInteger(ENDZ, pos.getZ());
		}

		// スニークしてないなら始点の登録
		else {
			tags.setInteger(STARTX, pos.getX());
			tags.setInteger(STARTY, pos.getY());
			tags.setInteger(STARTZ, pos.getZ());
		}

		// サーバーに送信
    	PacketHandler.sendToServer(new StarLightPKT(
    			tags.getInteger(STARTX), tags.getInteger(STARTY), tags.getInteger(STARTZ),
    			tags.getInteger(ENDX), tags.getInteger(ENDY), tags.getInteger(ENDZ)
    	));

    	event.setCanceled(true);
    }

	@SubscribeEvent
	public static void onOverlay(DrawBlockHighlightEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!isSLWand(stack)) { stack = player.getHeldItem(EnumHand.OFF_HAND); }

    	// マジシャンズビギナーを持ってないなら終了
		if (!isSLWand(stack)) { return; }

		World world = player.getEntityWorld();
		float parTick = event.getPartialTicks();

		x = player.lastTickPosX + (player.posX - player.lastTickPosX) * parTick;
		y = player.lastTickPosY + (player.posY - player.lastTickPosY) * parTick;
		z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * parTick;

		if (addHeldToRenderList(world, stack, stack.getItem())) {
			RenderUtils.drawCube(renderList, false);
		}

		renderList.clear();
	}

	// スターライトワンドかどうか
	public static boolean isSLWand (ItemStack stack) {
		return stack.getItem() instanceof StarLightWand || stack.getItem() instanceof AetherHammer;
	}

	// レンダー範囲を向き合わせて取得
	public static boolean addHeldToRenderList(World world, ItemStack stack, Item item) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);
		IRangePosTool wand = (IRangePosTool) item;
		BlockPos startPos = null;
		BlockPos endPos = null;

		if (tags.hasKey(STARTX)) {
			startPos = wand.getStartPos(tags);
		}

		else if (tags.hasKey(ENDX)) {
			startPos = endPos = wand.getEndPos(tags);
		}

		if (tags.hasKey(ENDX)) {
			endPos = wand.getEndPos(tags);
		}

		else if  (tags.hasKey(STARTX)) {
			startPos = endPos = wand.getStartPos(tags);
		}

		// 座標取得出来なかったら終了
		if (startPos == null && endPos == null) { return false; }

		// 若干範囲が狭まるのを対策
		int addSX = startPos.getX() > endPos.getX() ? 1 : 0;
		int addSY = startPos.getY() > endPos.getY() ? 1 : 0;
		int addSZ = startPos.getZ() > endPos.getZ() ? 1 : 0;
		int addEX = startPos.getX() < endPos.getX() ? 1 : 0;
		int addEY = startPos.getY() < endPos.getY() ? 1 : 0;
		int addEZ = startPos.getZ() < endPos.getZ() ? 1 : 0;

		// 範囲が狭い時は補正
		if (addSX == 0 && addEX == 0) {
			addEX += 1;
		}

		// 範囲が狭い時は補正
		if (addSY == 0 && addEY == 0) {
			addEY += 1;
		}

		// 範囲が狭い時は補正
		if (addSZ == 0 && addEZ == 0) {
			addEZ += 1;
		}

		renderList.add(new AxisAlignedBB(startPos.add(addSX, addSY, addSZ), endPos.add(addEX, addEY, addEZ)).offset(-x, -y, -z).grow(0.02D));
		return true;
	}
}
