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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.item.sm.magic.MFStuff;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.RenderUtils;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class MFStuffRenderEvent {

	public static final List<AxisAlignedBB> renderList = new ArrayList<>();
	public static double x, y, z;
	public final static String X = "X";
	public final static String Y = "Y";
	public final static String Z = "Z";

	@SubscribeEvent
	public static void onOverlay(DrawBlockHighlightEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!isStuff(stack)) { stack = player.getHeldItem(EnumHand.OFF_HAND); }

    	// マジシャンズビギナーを持ってないなら終了
		if (!isStuff(stack)) { return; }

		World world = player.getEntityWorld();
		float parTick = event.getPartialTicks();

		x = player.lastTickPosX + (player.posX - player.lastTickPosX) * parTick;
		y = player.lastTickPosY + (player.posY - player.lastTickPosY) * parTick;
		z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * parTick;

		renderGen(world, player, stack, x, y, z, parTick);
	}

	// マジシャンズビギナーかどうか
	public static boolean isStuff (ItemStack stack) {
		return stack.getItem() instanceof MFStuff;
	}

	public static void renderGen (World world, EntityPlayer player, ItemStack stack, double x, double y, double z, float parTick) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);
		if (!tags.hasKey(X)) { renderList.clear(); return; }

		BlockPos pos = new BlockPos(tags.getInteger(X), tags.getInteger(Y) + 1, tags.getInteger(Z));

		// 座標がnull以外なら生成範囲をレンダー
		if (pos != null) {
			addHeldToRenderList(world, stack, pos, stack.getItem());
			RenderUtils.drawCube(renderList);
		}

		// AABBリストの初期化
		renderList.clear();
	}

	// レンダー範囲を向き合わせて取得
	public static void addHeldToRenderList(World world, ItemStack stack, BlockPos pos, Item item) {
		AxisAlignedBB box = new AxisAlignedBB(pos, pos.add(1, -1, 1)).offset(-x, -y + 0.025, -z);
		renderList.add(box);
	}
}
