package sweetmagic.plugin.jei.freezer;

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

public class FreezerJeiRecipeCategory implements IRecipeCategory<FreezerJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.freezer";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_freezer_jei.png");

	public FreezerJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 106);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.freezer_bottom));
		this.localizedName = I18n.format("sweetmagic.jei.freezer.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_freezer";
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
	public void setRecipe(IRecipeLayout recipeLayout, FreezerJeiRecipeWrapper recipeWrapper, IIngredients ingredients) {

		IGuiItemStackGroup group = recipeLayout.getItemStacks();

		// アイテムの配列順番、Inputかどうか、X座標、Y座標
		group.init(0, true, 61, 10);
		group.init(1, true, 133, 7);

		for (int i = 0; i < 2; i++)
			for (int k = 0; k < 3; k++)
				group.init(k + i * 3 + 2, false, 52 + 18 * i, 43 + 18 * k);

//		for (int i = 0; i < 4; i++)
//			group.init(8 + i, true, 133, 23 + 18 * i);

		// 手に持ってるアイテム
		group.set(0, recipeWrapper.handList);

		// 完成品
		group.set(1, recipeWrapper.outputs[0]);

		// インプットアイテム
		for(int i = 2; i <= 8; i++) {
			if ((i - 2) < recipeWrapper.inputList.size()) {
				group.set(i, recipeWrapper.inputList.get(i - 2));
			}
		}
	}
}
