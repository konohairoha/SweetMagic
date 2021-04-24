package sweetmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;

public class SMDamage extends DamageSource {

	public static Entity shot;

	public static SMDamage flameDamage = new SMDamage("flame").setBypassesArmor();
	public static SMDamage flostDamage = new SMDamage("flost").setBypassesArmor();
	public static SMDamage exploDamage = new SMDamage("explo").setBypassesArmor();

	public SMDamage(String name) {
		super(name);
	}

	public static DamageSource MagicDamage(Entity par1, Entity par2) {
		shot = par1;
		return (new EntityDamageSourceIndirect("magic", par1, par2)).setProjectile();
	}

	private SMDamage setBypassesArmor() {
		this.setDamageBypassesArmor();
		return this;
	}
}
