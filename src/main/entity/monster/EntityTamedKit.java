package sweetmagic.init.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class EntityTamedKit extends EntityBlackCat {

	public EntityTamedKit(World world) {
		super(world);
        this.setSize(0.6F, 1.5F);
	}

	public String getName() {

		if (this.hasCustomName()) {
			return this.getCustomNameTag();
		}

		else {
			return I18n.translateToLocal("entity.tamedkit.name");
		}
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4625D);
	}

	@Override
	protected float getSoundVolume() {
		return 0.5F;
	}

	@Override
	protected float getSoundPitch() {
		return 0.875F;
	}
}
