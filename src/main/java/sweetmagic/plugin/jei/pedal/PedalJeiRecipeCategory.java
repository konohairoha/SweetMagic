package sweetmagic.plugin.jei.pedal;

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

public class PedalJeiRecipeCategory implements IRecipeCategory<PedalJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.pedal";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_pedal.png");

	public PedalJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 134);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.pedestal_creat));
		this.localizedName = I18n.format("sweetmagic.jei.pedal.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_pedal";
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
	public void setRecipe(IRecipeLayout recipeLayout, PedalJeiRecipeWrapper recipeWrapper, IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = recipeLayout.getItemStacks();

		//アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, true, 48, 45);

		for (int i = 1; i <= 9; i++) {
			int x = 7 + (i - 1) * 18;
			stackFroup.init(i, true, x, 109);
		}

		stackFroup.init(10, false, 108, 45);

		//手にもってるアイテム
		stackFroup.set(0, recipeWrapper.handList);

		for (int i = 1; i <= 9; i++) {
			if ((i - 1) < recipeWrapper.inputList.size()) {
				stackFroup.set(i, recipeWrapper.inputList.get(i - 1));
			}
		}

		//完成品アイテム ※今後のためFor文で記述してます　ごめん
		for(int i = 10; i <= 10; i++) {
			ItemStack item = recipeWrapper.outputs[i-10];
			stackFroup.set(i, item);
		}
	}

	@Override
	public void drawExtras(Minecraft minecraft) {}
}
