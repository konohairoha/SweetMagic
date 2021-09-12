package sweetmagic.event;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import sweetmagic.SweetMagicCore;
import sweetmagic.config.SMConfig;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.block.blocks.FlowerBuscket;
import sweetmagic.init.item.sm.magic.MagicianBeginnerBook;
import sweetmagic.packet.LeftClickPKT;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.RenderUtils;
import sweetmagic.util.SMUtil;
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

	private static final List<Block> itemList = Arrays.<Block> asList(
			Blocks.AIR, BlockInit.goldcrest, BlockInit.table_modernlamp_on, BlockInit.modenlanp
	);

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

		// レンダーを軽減するなら
		if (!SMConfig.isRender) {

			// 座標がnull以外なら生成範囲をレンダー
			if (pos != null) {
				addHeldToRenderList(world, stack, pos, stack.getItem());
				RenderUtils.drawCube(renderList);
			}

			renderList.clear();
		}

		// レンダーを軽減しないなら
		else {

			if (pos == null) { return; }

			Template template = getTemplateToJar(new ResourceLocation(SweetMagicCore.MODID, "house"));
			List<Template.BlockInfo> list = (List<Template.BlockInfo>) SMUtil.callPrivateField(Template.class, template, "field_186270_a", "blocks");
			if (list == null || list.isEmpty()) { return; }

			PlacementSettings place = new PlacementSettings();
			Minecraft mine = Minecraft.getMinecraft();
			BlockRendererDispatcher render = mine.getBlockRendererDispatcher();
			TextureManager tex = mine.getTextureManager();
			RenderManager mane = mine.getRenderManager();
			EnumFacing face = EnumFacing.SOUTH;
			int pX = 0;
			int pZ = 0;

			switch (tags.getInteger(FACING)) {
			case 0:
				place.setRotation(Rotation.NONE);
				pos = pos.south(pX).east(pZ);
				break;
			case 90:
				place.setRotation(Rotation.COUNTERCLOCKWISE_90);
				pos = pos.north(pZ).east(pX);
				face = EnumFacing.EAST;
				break;
			case 180:
				place.setRotation(Rotation.CLOCKWISE_180);
				pos = pos.south(pX).east(pZ);
				face = EnumFacing.NORTH;
				break;
			case 270:
				place.setRotation(Rotation.CLOCKWISE_90);
				pos = pos.south(pZ).west(pX);
				face = EnumFacing.WEST;
				break;
			}

			for (Template.BlockInfo info : list) {

				IBlockState state = info.blockState;
				Block block = state.getBlock();
				if ( itemList.contains(block) || block instanceof BlockBush || block instanceof FlowerBuscket) { continue; }

				if (state.getRenderType() != EnumBlockRenderType.INVISIBLE) {

					if (block instanceof BlockDoor) {
						state = state.withProperty(BlockDoor.FACING, face);
					}

					else if (block instanceof BlockStairs) {
						state = state.withProperty(BlockStairs.FACING, place.getRotation().rotate(state.getValue(BlockStairs.FACING)));
					}

					else if (block instanceof BlockTrapDoor) {
						state = state.withProperty(BlockTrapDoor.FACING, place.getRotation().rotate(state.getValue(BlockTrapDoor.FACING)));
					}

					else if (block instanceof BaseFaceBlock) {
						state = state.withProperty(BaseFaceBlock.FACING, place.getRotation().rotate(state.getValue(BaseFaceBlock.FACING)));
					}

					GlStateManager.pushMatrix();

					GlStateManager.translate( -mane.viewerPosX, -mane.viewerPosY + 0.1F, -mane.viewerPosZ );
					GlStateManager.color(1F, 1F, 1F, 1F);
					GlStateManager.depthMask(false);
					RenderHelper.disableStandardItemLighting();
					GlStateManager.enableLighting();
					GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					tex.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
					GlStateManager.shadeModel(Minecraft.isAmbientOcclusionEnabled() ? GL11.GL_SMOOTH : GL11.GL_FLAT);

					Tessellator tes = Tessellator.getInstance();
					BufferBuilder buf = tes.getBuffer();
					buf.begin(7, DefaultVertexFormats.BLOCK);
					BlockPos p = Template.transformedBlockPos(place, info.pos);
					render.renderBlock(state, pos.add(p.getX(), p.getY() - 1D, p.getZ()), world, buf);

					GlStateManager.disableLighting();
					RenderHelper.enableStandardItemLighting();
					GlStateManager.depthMask(true);

					tes.draw();
					GlStateManager.popMatrix();
				}

				ForgeHooksClient.setRenderLayer(null);
			}
		}
	}

	public static Template getTemplateToJar(ResourceLocation rl) {

		//ファイル読み込み
		InputStream input = FMLCommonHandler.instance().getClass().getClassLoader()
				.getResourceAsStream("assets/" + rl.getResourceDomain() + "/structures/" + rl.getResourcePath() + ".nbt");

		NBTTagCompound tags = null;

		try {
			tags = CompressedStreamTools.readCompressed(input);
		} catch (IOException e) { }

		//Template生成
		Template template = new Template();
		template.read(tags);

		return template;
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
			box = new AxisAlignedBB(pos, pos.add(14, 10, 14));
			box = box.offset(-x, -y + 0.075, -z);
			break;
		case 90:
			box = new AxisAlignedBB(pos, pos.add(14, 10, -14));
			box = box.offset(-x, -y + 0.075, -z + 1);
			break;
		case 180:
			box = new AxisAlignedBB(pos, pos.add(-14, 10, -14));
			box = box.offset(-x + 1, -y + 0.075, -z + 1);
			break;
		case 270:
			box = new AxisAlignedBB(pos, pos.add(-14, 10, 14));
			box = box.offset(-x + 1, -y + 0.075, -z );
			break;
		}

		renderList.add(box);
	}
}
