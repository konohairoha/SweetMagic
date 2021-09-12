package sweetmagic.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.ItemInit;

public class SMHarvestEvent {

	@SubscribeEvent
	public void onMining(HarvestDropsEvent event) {

		if (event.getHarvester() == null || event.getWorld().isRemote) { return; }

		EntityPlayer player = event.getHarvester();
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) { return; }

		int maxDama = stack.getMaxDamage();
		Item item = stack.getItem();

		if (item instanceof ItemSpade && maxDama >= 250) {

			// 幸運レベルを取得
			int level = event.getFortuneLevel() + 1;
			float f = event.getWorld().rand.nextFloat();

			IBlockState state = event.getState();
			Block block = state.getBlock();
			List<ItemStack> dropList = event.getDrops();

			if (block == Blocks.CLAY) {

				// f = ドロップ率（数字が小さいほど確率が低い）
				if (f < 0.02F * level) {
					dropList.add(new ItemStack(ItemInit.sticky_stuff_seed));
				}

			}

			else if (block == Blocks.DIRT && block.getMetaFromState(state) == 2) {

				if (f < 0.2F * level) {
					dropList.add(new ItemStack(ItemInit.sweetpotato));
				}
			}
		}

		// オルタナティブピッケル
		else if (item == ItemInit.alt_pick && !player.isSneaking()) {

			World world = player.world;
			IBlockState state = event.getState();
			BlockPos pos = event.getPos();

			for (ItemStack s : this.getBlockDrops(world, player, state, state.getBlock(), pos) ) {
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s));
			}

			// ドロップアイテムのクリア
			event.getDrops().clear();
		}
	}

	@SubscribeEvent
	public void onBreak (BreakEvent event) {

		if (event.getPlayer() == null || event.getWorld().isRemote) { return; }

		EntityPlayer player = event.getPlayer();
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.isEmpty()) { return; }

		Item item = stack.getItem();

		// オルタナティブピッケル
		if (item == ItemInit.alt_pick && !player.isSneaking()) {

			World world = player.world;
			BlockPos pos = event.getPos();
			IBlockState state = event.getState();
			Block block = state.getBlock();

			if (block.canSilkHarvest(world, pos, state, player)) {
				event.setExpToDrop(0);
			}
		}
	}

	public List<ItemStack> getBlockDrops(World world, EntityPlayer player, IBlockState state, Block block, BlockPos pos) {
		if (block.canSilkHarvest(world, pos, state, player)) {
//			return Lists.newArrayList(new ItemStack(block, 1, block.getMetaFromState(state)));
			List<ItemStack> stackList = new ArrayList<>();
			stackList.add(this.getSilkTouchDrop(state, block));
			return stackList;
		}
		return block.getDrops(world, pos, state, 0);
	}

	public ItemStack getSilkTouchDrop(IBlockState state, Block block) {
		Item item = Item.getItemFromBlock(block);
		int i = item.getHasSubtypes() ? block.getMetaFromState(state) : 0;
		return new ItemStack(item, 1, i);
	}
}
