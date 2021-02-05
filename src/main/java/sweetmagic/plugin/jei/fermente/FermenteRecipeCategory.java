package sweetmagic.plugin.jei.fermente;

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

public class FermenteRecipeCategory implements IRecipeCategory<FermenteRecipeWrapper> {

	public static final String UID = "sweetmagic.fermente";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_bin.png");

	public FermenteRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 134);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.matured_bottle));
		this.localizedName = I18n.format("sweetmagic.jei.fermente.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_fermente";
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
	public void setRecipe(IRecipeLayout recipeLayout, FermenteRecipeWrapper recipeWrapper,
			IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = recipeLayout.getItemStacks();

		//アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, true, 48, 20);
		stackFroup.init(1, false, 108, 20);

		for (int i = 2; i <= 19; i++) {
			int x = 7 + (i - 2) * 18;
			stackFroup.init(i, true, x, 109);
		}

		//手に持ってるアイテム
		stackFroup.set(0, recipeWrapper.handList);
		//完成品
		stackFroup.set(1, recipeWrapper.outputs[0]);

		//レシピアイテム
		for(int i = 2; i <= 19; i++) {
			if ((i - 2) < recipeWrapper.inputList.size()) {
				stackFroup.set(i, recipeWrapper.inputList.get(i - 2));
			}
		}
	}
}
