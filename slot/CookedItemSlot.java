package sweetmagic.init.tile.slot;

import java.util.function.Predicate;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.armor.MagiciansPouch;

public class CookedItemSlot extends ValidatedSlot {

	public CookedItemSlot(IItemHandler item, int index, int x, int y, Predicate<ItemStack> validator) {
		super(item, index, x, y, validator);
	}

	@Override
	public ItemStack onTake(EntityPlayer player, ItemStack stack) {
		this.getExp(player, stack);
		return super.onTake(player, stack);
	}

	public void getExp (EntityPlayer player, ItemStack stack) {

		Item item = stack.getItem();
		if ( !(item instanceof ItemFood) || !MagiciansPouch.hasAcce(player, ItemInit.mysterious_fork)) { return; }

		ItemFood food = (ItemFood) item;
		float amount = Math.max(food.getHealAmount(stack), 1F) * Math.max(food.getSaturationModifier(stack), 0.5F) * stack.getCount();
		int xp = (int) (Math.max(1, amount));

		World world = player.world;

		if (!world.isRemote) {
	    	EntityXPOrb entity = new EntityXPOrb(world, player.posX, player.posY, player.posZ, xp);
	    	world.spawnEntity(entity);
		}
	}
}
