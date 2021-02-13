package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.item.sm.magic.MagicianBeginnerBook;
import sweetmagic.packet.LeftClickPKT;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.RenderUtils;
import sweetmagic.util.SoundHelper;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = SweetMagicCore.MODID)
public class MagicianBeginnerEvent {

	public static final List<AxisAlignedBB> renderList = new ArrayList<>();
	public static double x, y, z;
	public final static String FACING = "facing";
	public final static String SNEAK = "sneak";
	public final static String X = "posX";
	public final static String Y = "posY";
	public final static String Z = "posZ";
	public final static String SMSTARTER = "smStarter";

    @SubscribeEvent
    public static void onLeftClick(PlayerInteractEvent.LeftClickEmpty event) {

    	// マジシャンズビギナーを持ってないなら終了
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof MagicianBeginnerBook)) { return; }

        // NBTに登録してサーバーに通知
        NBTTagCompound tags = changeFace(player, stack);
    	PacketHandler.sendToServer(new LeftClickPKT(tags.getBoolean(SNEAK), tags.getInteger(FACING), tags.getInteger(X), tags.getInteger(Y), tags.getInteger(Z)));
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {

        if (event.isCanceled()) { return; }

    	// マジシャンズビギナーを持ってないなら終了
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.getHeldItemMainhand();
        Item item = stack.getItem();
        if (!(item instanceof MagicianBeginnerBook) || player.getCooldownTracker().hasCooldown(item)) { return; }

        event.setCanceled(true);
        if (!player.world.isRemote) { return; }

        // NBTに登録してサーバーに通知
		player.getCooldownTracker().setCooldown(stack.getItem(), 5);
        NBTTagCompound tags = changeFace(player, stack);
    	PacketHandler.sendToServer(new LeftClickPKT(tags.getBoolean(SNEAK), tags.getInteger(FACING), tags.getInteger(X), tags.getInteger(Y), tags.getInteger(Z)));
    }

    public static NBTTagCompound changeFace (EntityPlayer player, ItemStack stack) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);

		// 向きのNBTがないなら
		if (!tags.hasKey(FACING)) {
			tags.setInteger(FACING, 90);
		}

		// 向きのNBTがあるなら
		else {
			int angle = tags.getInteger(FACING);
			tags.setInteger(FACING, angle >= 270 ? 0 : angle + 90);
		}

		// クライアント（プレイヤー）へ送りつける
		if (player instanceof EntityPlayerMP) {
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_BUT, 0.5F, 0.33F), (EntityPlayerMP) player);
		}

		return tags;
    }

	@SubscribeEvent
	public static void onOverlay(DrawBlockHighlightEvent event) {

		EntityPlayer player = Minecraft.getMinecraft().player;
		ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
		if (!isMBBook(stack)) { stack = player.getHeldItem(EnumHand.OFF_HAND); }

    	// マジシャンズビギナーを持ってないなら終了
		if (!isMBBook(stack)) { return; }

		World world = player.getEntityWorld();
		float parTick = event.getPartialTicks();

		x = player.lastTickPosX + (player.posX - player.lastTickPosX) * parTick;
		y = player.lastTickPosY + (player.posY - player.lastTickPosY) * parTick;
		z = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * parTick;

		renderGen(world, player, stack, x, y, z, parTick);
	}

	// マジシャンズビギナーかどうか
	public static boolean isMBBook (ItemStack stack) {
		return stack.getItem() instanceof MagicianBeginnerBook;
	}

	public static void renderGen (World world, EntityPlayer player, ItemStack stack, double x, double y, double z, float parTick) {

		RayTraceResult mop = ((MagicianBeginnerBook) stack.getItem()).getHitBlock(player);
		NBTTagCompound tags = ItemHelper.getNBT(stack);
		BlockPos pos = null;

		// 向いてる方向にすでにブロックがある場合
		if (mop != null && mop.typeOfHit == Type.BLOCK) {

			// 固定ならその座標を取得
			if (isSneak(tags)) {
				pos = new BlockPos(tags.getInteger(X), tags.getInteger(Y) + 1, tags.getInteger(Z));
			}

			// 固定してなら現在の向いてる方向
			else {
				pos = mop.getBlockPos().offset(mop.sideHit);
			}
		}

		// ブロックがないかつ固定ならその座標を取得
		else if (isSneak(tags)) {
			pos = new BlockPos(tags.getInteger(X), tags.getInteger(Y) + 1, tags.getInteger(Z));
		}

		// 座標がnull以外なら生成範囲をレンダー
		if (pos != null) {
			addHeldToRenderList(world, stack, pos, stack.getItem());
			RenderUtils.drawCube(renderList);
		}

		// AABBリストの初期化
		renderList.clear();
	}

	// 座標固定かどうか
	public static boolean isSneak (NBTTagCompound tags) {
		return tags.hasKey(SNEAK) && tags.getBoolean(SNEAK);
	}

	// レンダー範囲を向き合わせて取得
	public static void addHeldToRenderList(World world, ItemStack stack, BlockPos pos, Item item) {

		AxisAlignedBB box = null;
		NBTTagCompound tags = ItemHelper.getNBT(stack);

		switch (tags.getInteger(FACING)) {
		case 0:
			box = new AxisAlignedBB(pos, pos.add(-14, 10, -14));
			box = box.offset(-x + 1, -y + 0.075, -z + 1);
			break;
		case 90:
			box = new AxisAlignedBB(pos, pos.add(-14, 10, 14));
			box = box.offset(-x + 1, -y + 0.075, -z );
			break;
		case 180:
			box = new AxisAlignedBB(pos, pos.add(14, 10, 14));
			box = box.offset(-x, -y + 0.075, -z);
			break;
		case 270:
			box = new AxisAlignedBB(pos, pos.add(14, 10, -14));
			box = box.offset(-x, -y + 0.075, -z + 1);
			break;
		}

		renderList.add(box);
	}

    public static PlacementSettings getPlacement(ItemStack capsule) {

//		NBTTagCompound tags = ItemHelper.getNBT(capsule);

        PlacementSettings placementSettings = new PlacementSettings()
                .setMirror(Mirror.valueOf("house"))
                .setRotation(Rotation.NONE)
                .setIgnoreEntities(false)
                .setChunk(null)
                .setReplacedBlock(null)
                .setIgnoreStructureBlock(false);
        return placementSettings;
    }
}
