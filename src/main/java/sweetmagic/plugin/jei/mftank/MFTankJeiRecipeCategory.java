package sweetmagic.plugin.jei.mftank;

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

public class MFTankJeiRecipeCategory implements IRecipeCategory<MFTankJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.mftank";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_jeitank.png");

	public MFTankJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 107);

		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.mftank));
		this.localizedName = I18n.format("sweetmagic.jei.mftank.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_mftank";
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
	public void setRecipe(IRecipeLayout recipeLayout, MFTankJeiRecipeWrapper recipeWrapper,
			IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = recipeLayout.getItemStacks();

		//アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, true, 48, 20);
		stackFroup.init(1, false, 108, 20);
		//レシピを引っ張り出す
		stackFroup.set(ingredients);
	}
}
