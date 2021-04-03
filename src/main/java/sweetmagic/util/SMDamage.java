package sweetmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class SMDamage extends DamageSource {

	public static Entity shot;

	public SMDamage(String name) {
		super("SweetMagicDamage");
	}

	public static DamageSource MagicDamage(Entity par1, Entity par2) {
		shot = par1;
		return (new EntityDamageSourceIndirect("magic", par1, par2)).setProjectile();
	}
}
