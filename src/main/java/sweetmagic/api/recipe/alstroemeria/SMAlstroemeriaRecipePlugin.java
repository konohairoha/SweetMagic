package sweetmagic.api.recipe.alstroemeria;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.minecraftforge.fml.common.eventhandler.EventPriority;

/**
 * レシピ登録用アノテーション
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SMAlstroemeriaRecipePlugin {

	/** ModId */
	String modid() default "";

	/** 優先度 */
	EventPriority priority() default EventPriority.NORMAL;

}