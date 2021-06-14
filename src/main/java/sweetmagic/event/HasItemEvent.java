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
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

public class HasItemEvent {

	public static boolean hasThisItem;
	private int tickTime = 0;

	private static final List<Item> itemList = Arrays.<Item> asList(
			ItemInit.magic_light, ItemInit.magic_starburst, ItemInit.magic_sacredbuster, Item.getItemFromBlock(BlockInit.magiclight));

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderfov(FOVUpdateEvent event) {

		this.tickTime++;
		if(this.tickTime % 65 != 0) { return; }

		this.tickTime = 0;
		EntityPlayer player = FMLClientHandler.instance().getClient().player;
		World world = player.world;
		ItemStack stack = player.getHeldItemMainhand();

		if (stack.isEmpty()) {
			hasThisItem = false;
			return;
		}

		Item item = stack.getItem();

		if (item instanceof IWand) {

			// 杖の呼び出し
			IWand wand = (IWand) item;

			// 選択中のアイテムを取得
			item = wand.getSlotItem(player, stack, wand.getNBT(stack)).getItem();
		}

		// 持っているアイテムが光魔法なら
		hasThisItem = itemList.contains(item);

		if (hasThisItem && world.isRemote) {
			this.renderEffect(world, player);
		}
	}

	public void renderEffect (World world, EntityPlayer player) {

		BlockPos pPos = player.getPosition();

		for (int x = -12; x < 12; x++) {
			for (int z = -12; z < 12; z++) {
				for (int y = -12; y < 12; y++) {

					BlockPos pos = new BlockPos((int) pPos.getX() + x, (int) pPos.getY() + y, (int) pPos.getZ() + z);
					Block block = world.getBlockState(pos).getBlock();
					if (block != BlockInit.magiclight) { continue; }

					double d0 = (int) pos.getX() + 0.5D;
					double d1 = (int) pos.getY() + 0.6D;
					double d2 = (int) pos.getZ() + 0.5D;
					FMLClientHandler.instance().getClient().effectRenderer.addEffect(new ParticleMagicLight.Factory().createParticle(0, world, d0, d1, d2, 0D, 0D, 0D));
				}
			}
		}
	}
}
