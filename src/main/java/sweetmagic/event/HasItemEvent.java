package sweetmagic.event;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IWand;
import sweetmagic.client.particle.ParticleMagicLight;
import sweetmagic.config.SMConfig;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.util.ParticleHelper;

public class HasItemEvent {

	public static boolean hasThisItem;
	private int tickTime = 0;

	private static final List<Item> itemList = Arrays.<Item> asList(
		ItemInit.magic_light, ItemInit.magic_starburst, ItemInit.magic_sacredbuster, Item.getItemFromBlock(BlockInit.magiclight)
	);

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderfov(FOVUpdateEvent event) {

		this.tickTime++;
		int renderTick = SMConfig.isRender ? 30 : 65;

		if(this.tickTime % renderTick != 0) { return; }

		this.tickTime = 0;
		EntityPlayer player = FMLClientHandler.instance().getClient().player;
		World world = player.world;
		ItemStack stack = player.getHeldItemMainhand();

		if (stack.isEmpty()) {
			hasThisItem = false;
			return;
		}

		Item item = stack.getItem();

		// 杖の呼び出して選択中のアイテムを取得
		if (item instanceof IWand) {
			IWand wand = (IWand) item;
			item = wand.getSlotItem(player, stack, wand.getNBT(stack)).getItem();
		}

		// 持っているアイテムが光魔法なら
		hasThisItem = itemList.contains(item);

		if (hasThisItem && world.isRemote) {
			this.renderEffect(world, player);
		}
	}

	public void renderEffect (World world, EntityPlayer player) {

		int area = 12;
		BlockPos pPos = player.getPosition();

		for (BlockPos pos : BlockPos.getAllInBox(pPos.add(-area, -area, -area), pPos.add(area, area, area))) {

			Block block = world.getBlockState(pos).getBlock();
			if (block != BlockInit.magiclight) { continue; }

			double d0 = (int) pos.getX() + 0.5D;
			double d1 = (int) pos.getY() + 0.6D;
			double d2 = (int) pos.getZ() + 0.5D;
			ParticleHelper.spawnParticl().addEffect(ParticleMagicLight.create(world, d0, d1, d2, 0D, 0D, 0D));
		}
	}
}
