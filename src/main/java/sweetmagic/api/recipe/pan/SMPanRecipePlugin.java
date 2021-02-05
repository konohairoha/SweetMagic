package sweetmagic.api.recipe.pan;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SMPanRecipePlugin {

	String modid() default ""; // ModId
	EventPriority priority() default EventPriority.NORMAL; // 優先度
}
