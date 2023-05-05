package sweetmagic.init.tile.slot;

import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotArmor extends Slot {

    private final Entity entity;
    private final EntityEquipmentSlot slotType;

    public SlotArmor(Entity entity, EntityEquipmentSlot type, IInventory inv, int index, int x, int y) {
        super(inv, index, x, y);
        this.entity = entity;
        this.slotType = type;
    }

    @Override
    public int getSlotStackLimit () {
        return 1;
    }

    @Override
    public boolean isItemValid (ItemStack stack) {
        return !stack.isEmpty() ? stack.getItem().isValidArmor(stack, this.slotType, this.entity) : false;
    }
}
