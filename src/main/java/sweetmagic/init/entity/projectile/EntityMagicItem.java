package sweetmagic.init.entity.projectile;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class EntityMagicItem extends EntityItem {

    public EntityMagicItem(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.isImmuneToFire = true;
    }

    public EntityMagicItem(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
        this.isImmuneToFire = true;
    }

    public EntityMagicItem(World world) {
        super(world);
        this.isImmuneToFire = true;
    }

	public boolean attackEntityFrom(DamageSource src, float amount) {
		return false;
	}
}
