package sweetmagic.event;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import sweetmagic.init.ItemInit;

public class MobDropEvent {

	@SubscribeEvent
	public void onEvent(LivingDropsEvent event) {

		EntityLivingBase entity = event.getEntityLiving();
		List<EntityItem> itemList = event.getDrops();
		World world = entity.world;
		Random rand = world.rand;
		double x = entity.posX;
		double y = entity.posY;
		double z = entity.posZ;

		//ウィッチが不思議なページを落とす
		if (entity instanceof EntityWitch && rand.nextBoolean()) {
			itemList.add(this.getItem(world, x, y, z, ItemInit.mysterious_page, rand.nextInt(2) + 1));
		}

		// ゾンビなら
		else if (entity instanceof EntityZombie && rand.nextFloat() <= 0.1F) {
			itemList.add(this.getItem(world, x, y, z, ItemInit.eggbag, rand.nextInt(2) + 1));
		}

		// クリーパーなら
		else if (entity instanceof EntityCreeper) {
			itemList.add(this.getItem(world, x, y, z, ItemInit.magicmeal, rand.nextInt(2) + 1));
		}

		// ニワトリなら
		else if (entity instanceof EntityChicken) {
			itemList.add(this.getItem(world, x, y, z, Items.FEATHER, rand.nextInt(3) + 1));
		}
	}

	// EntityItemで返す
	public EntityItem getItem(World world, double x, double y, double z,Item item, int amount) {
		return new EntityItem(world, x, y, z, new ItemStack(item, amount));
	}
}
