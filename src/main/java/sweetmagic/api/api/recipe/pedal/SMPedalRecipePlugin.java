package sweetmagic.api.recipe.pedal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SMPedalRecipePlugin {

	/** ModId */
	String modid() default "";

	/** 優先度 */
	EventPriority priority() default EventPriority.NORMAL;
}
