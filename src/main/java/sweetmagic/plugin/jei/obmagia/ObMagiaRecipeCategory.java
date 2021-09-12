package sweetmagic.plugin.jei.obmagia;

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

public class ObMagiaRecipeCategory implements IRecipeCategory<ObMagiaRecipeWrapper> {

	public static final String UID = "sweetmagic.obmagia";
	private final IDrawable background;
	private final IDrawable icon;
	private final String localizedName;
	private static final ResourceLocation TEX = new ResourceLocation(SweetMagicCore.MODID, "textures/gui/gui_obmagia.png");

	public ObMagiaRecipeCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(TEX, 0, 0, 175, 151);
		this.icon = helper.createDrawableIngredient(new ItemStack(BlockInit.obmagia_bottom));
		this.localizedName = I18n.format("sweetmagic.jei.obmagia.title");
	}

	@Override
	public String getUid() {
		return "sweetmagic_obmagia";
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
	public void setRecipe(IRecipeLayout layout, ObMagiaRecipeWrapper wrapper, IIngredients ingredients) {

		IGuiItemStackGroup stackFroup = layout.getItemStacks();

		// アイテムの配列順番、Inputかどうか、X座標、Y座標
		stackFroup.init(0, false, 48, 20);
		stackFroup.set(0, wrapper.handList);

		for (int i = 1; i <= 9; i++) {

			int x = 7 + (i - 1) * 18;
			stackFroup.init(i, true, x, 109);

			if ((i - 1) < wrapper.inputList.size()) {
				stackFroup.set(i, wrapper.inputList.get(i - 1));
			}
		}

		// 完成品アイテム ※今後のためFor文で記述してます　ごめん
		for(int i = 10; i <= 10; i++) {
			ItemStack item = wrapper.outputs[i - 10];
			stackFroup.init(10, true, 108, 20);
			stackFroup.set(i, item);
		}
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
	}
}
