package sweetmagic.plugin.jei.juicemaker;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.BlockInit;

public class JuiceMakerJeiRecipeCategory implements IRecipeCategory<JuiceMakerJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.juiceMaker";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_juicemaker_jei.png");

	public JuiceMakerJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 104);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.juicemaker_off));
		this.localizedName = I18n.format("sweetmagic.jei.juicemaker.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_juicemaker";
	}

	@Override
	public String getTitle() {
		return this.localizedName;
	}

	@Override
	public String getModName() {
		return SweetMagicCore.MODID;
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, JuiceMakerJeiRecipeWrapper recipeWrapper, IIngredients ingredients) {

		IGuiItemStackGroup group = recipeLayout.getItemStacks();

		// アイテムの配列順番、Inputかどうか、X座標、Y座標
		group.init(0, true, 70, 7);
		group.init(1, true, 133, 8);

		for (int i = 0; i < 3; i++)
			group.init(i + 2, false, 70, 43 + 18 * i);

//		for (int i = 1; i < 4; i++)
//			group.init(5 + i, true, 133, 23 + 18 * i);

		// 手に持ってるアイテム
		group.set(0, recipeWrapper.handList);

		// 完成品
		group.set(1, recipeWrapper.outputs[0]);

		// インプットアイテム
		for(int i = 2; i <= 5; i++) {
			if ((i - 2) < recipeWrapper.inputList.size()) {
				group.set(i, recipeWrapper.inputList.get(i - 2));
			}
		}
	}
}
