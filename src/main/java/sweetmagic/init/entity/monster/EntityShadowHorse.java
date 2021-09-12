package sweetmagic.init.entity.monster;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityShadowHorse extends EntityHorse {

	public EntityShadowHorse(World world) {
		super(world);
	}

    public void updateHorseSlots() {
        super.updateHorseSlots();
        this.setHorseArmorStack(this.horseChest.getStackInSlot(1));
    }

    public void setInv (int i, ItemStack stack) {
        this.horseChest.setInventorySlotContents(i, stack);
    }

	@Override
	public void setInWeb() {}

    public void fall(float dis, float dama) { }
}
