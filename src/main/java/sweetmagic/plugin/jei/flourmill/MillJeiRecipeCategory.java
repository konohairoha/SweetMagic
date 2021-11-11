package sweetmagic.plugin.jei.flourmill;

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

public class MillJeiRecipeCategory implements IRecipeCategory<MillJeiRecipeWrapper> {

	public static final String UID = "sweetmagic.mill";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_mill.png");

	public MillJeiRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 176, 134);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.flourmill_off));
		this.localizedName = I18n.format("sweetmagic.jei.mill.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_mill";
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
	public void setRecipe(IRecipeLayout layout, MillJeiRecipeWrapper wrapper, IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = layout.getItemStacks();

		// アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, false, 108, 20);

		for (int i = 1; i <= 9; i++) {
			int x = 7 + (i - 1) * 18;
			stackFroup.init(i, true, x, 109);
		}

		stackFroup.init(10, true, 48, 20);
		stackFroup.set(10, wrapper.inputs);

		// 完成品
		for(int i = 0; i <= 9; i++) {

			if (i + 1 > wrapper.outputs.size()) { break; }

			ItemStack item = wrapper.outputs.get(i);
			stackFroup.set(i, item);
		}
	}
}
