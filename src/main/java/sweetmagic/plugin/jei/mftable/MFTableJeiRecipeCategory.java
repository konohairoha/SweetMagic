package sweetmagic.plugin.jei.mftable;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;
import sweetmagic.init.BlockInit;

public class MFTableJeiRecipeCategory implements IRecipeCategory<MFTableJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.mftable";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_mftable.png");

	public MFTableJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 175, 151);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.mftable));
		this.localizedName = I18n.format("sweetmagic.jei.mftable.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_mftable";
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
		return this.background;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, MFTableJeiRecipeWrapper recipeWrapper,
			IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = recipeLayout.getItemStacks();

		//アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, true, 48, 20);

		for (int i = 1; i <= 9; i++) {
			int x = 7 + (i - 2) * 18;
			stackFroup.init(i, true, x, 127);
		}

		stackFroup.init(10, true, 108, 20);

		// 手にもってるアイテム
		stackFroup.set(0, recipeWrapper.handList);

		//レシピアイテム
		for(int i = 2; i <= 19; i++) {
			if ((i - 2) < recipeWrapper.inputList.size()) {
				stackFroup.set(i, recipeWrapper.inputList.get(i - 2));
			}
		}

		//完成品アイテム ※今後のためFor文で記述してます　ごめん
		for(int i = 10; i <= 10; i++) {
			ItemStack item = recipeWrapper.outputs[i-10];
			stackFroup.set(i, item);
		}
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}
}
