package sweetmagic.plugin;

import java.util.List;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.alstroemeria.AlstroemeriaRecipes;
import sweetmagic.api.recipe.fermenter.FermenterRecipes;
import sweetmagic.api.recipe.flourmill.FlourMillRecipes;
import sweetmagic.api.recipe.freezer.FreezerRecipes;
import sweetmagic.api.recipe.juicemaker.JuiceMakerRecipes;
import sweetmagic.api.recipe.mftable.MFTableRecipes;
import sweetmagic.api.recipe.obmagia.ObMagiaRecipes;
import sweetmagic.api.recipe.oven.OvenRecipes;
import sweetmagic.api.recipe.pan.PanRecipes;
import sweetmagic.api.recipe.pedal.PedalRecipes;
import sweetmagic.api.recipe.pot.PotRecipes;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.tile.gui.GuiJuiceMaker;
import sweetmagic.init.tile.gui.GuiMFTank;
import sweetmagic.plugin.jei.alstroemeria.AlstroemeriaJeiRecipeCategory;
import sweetmagic.plugin.jei.alstroemeria.AlstroemeriaJeiRecipeWrapper;
import sweetmagic.plugin.jei.fermente.FermenteRecipeCategory;
import sweetmagic.plugin.jei.fermente.FermenteRecipeWrapper;
import sweetmagic.plugin.jei.flourmill.MillJeiRecipeCategory;
import sweetmagic.plugin.jei.flourmill.MillJeiRecipeWrapper;
import sweetmagic.plugin.jei.freezer.FreezerJeiRecipeCategory;
import sweetmagic.plugin.jei.freezer.FreezerJeiRecipeWrapper;
import sweetmagic.plugin.jei.juicemaker.JuiceMakerJeiRecipeCategory;
import sweetmagic.plugin.jei.juicemaker.JuiceMakerJeiRecipeWrapper;
import sweetmagic.plugin.jei.mftable.MFTableJeiRecipeCategory;
import sweetmagic.plugin.jei.mftable.MFTableJeiRecipeWrapper;
import sweetmagic.plugin.jei.mftank.MFTankJeiRecipeCategory;
import sweetmagic.plugin.jei.mftank.MFTankJeiRecipeWrapper;
import sweetmagic.plugin.jei.obmagia.ObMagiaRecipeCategory;
import sweetmagic.plugin.jei.obmagia.ObMagiaRecipeWrapper;
import sweetmagic.plugin.jei.oven.OvenJeiRecipeCategory;
import sweetmagic.plugin.jei.oven.OvenJeiRecipeWrapper;
import sweetmagic.plugin.jei.pan.PanJeiRecipeCategory;
import sweetmagic.plugin.jei.pan.PanJeiRecipeWrapper;
import sweetmagic.plugin.jei.pedal.PedalJeiRecipeCategory;
import sweetmagic.plugin.jei.pedal.PedalJeiRecipeWrapper;
import sweetmagic.plugin.jei.pot.PotJeiRecipeCategory;
import sweetmagic.plugin.jei.pot.PotJeiRecipeWrapper;
import sweetmagic.recipe.JeiRecipeMFTank;

@JEIPlugin
public class SMJeiPlugin implements IModPlugin {

	// レシピGUIは静的に持つ
	// レシピレジストリも静的に持つ
	private static IRecipesGui recipesGui;
	private static IRecipeRegistry recipeRegistry;

	// カテゴリーを持っておく
	public AlstroemeriaJeiRecipeCategory alsCategory;
	public PedalJeiRecipeCategory pedalCategory;
	public OvenJeiRecipeCategory ovenCategory;
	public MFTankJeiRecipeCategory mftankCategory;
	public MFTableJeiRecipeCategory tableCategory;
	public ObMagiaRecipeCategory obmagiaCategory;
	public MillJeiRecipeCategory millCategory;
	public PotJeiRecipeCategory potCategory;
	public PanJeiRecipeCategory panCategory;
	public FermenteRecipeCategory fermenteCategory;
	public JuiceMakerJeiRecipeCategory juiceMakerCategory;
	public FreezerJeiRecipeCategory freezCategory;

	public static void showUses(ItemStack stack) {
		if (recipesGui != null && recipeRegistry != null) {
			recipesGui.show(recipeRegistry.createFocus(IFocus.Mode.INPUT, stack));
		}
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistry registry) { }

	@Override
	public void registerIngredients(IModIngredientRegistration registry) { }

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {

		IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

		// トワイライトアルストロメリア
		this.alsCategory = new AlstroemeriaJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.alsCategory);

		// 想像の台座
		this.pedalCategory = new PedalJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.pedalCategory);

		// MFタンク
		this.mftankCategory = new MFTankJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.mftankCategory);

		// MFテーブル
		this.tableCategory = new MFTableJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.tableCategory);

		// オブマギア
		this.obmagiaCategory = new ObMagiaRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.obmagiaCategory);

		// 製粉機
		this.millCategory = new MillJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.millCategory);

		// オーブン
		this.ovenCategory = new OvenJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.ovenCategory);

		// 鍋
		this.potCategory = new PotJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.potCategory);

		// フライパン
		this.panCategory = new PanJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.panCategory);

		// 発酵機
		this.fermenteCategory = new FermenteRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.fermenteCategory);

		// ジュースメイカー
		this.juiceMakerCategory = new JuiceMakerJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.juiceMakerCategory);

		// 冷蔵庫
		this.freezCategory = new FreezerJeiRecipeCategory(guiHelper);
		registry.addRecipeCategories(this.freezCategory);
	}

	// レシピ定義、JEIアイテム関連をいじる
	@Override
	public void register(IModRegistry registry) {

		// トワイライトアルストロメリア
		registry.handleRecipes(AlstroemeriaRecipes.class, AlstroemeriaJeiRecipeWrapper::new, this.alsCategory.getUid());
		registry.addRecipes(SweetMagicAPI.alsRecipe, this.alsCategory.getUid());
		registry.addRecipeCatalyst(new ItemStack(BlockInit.twilight_alstroemeria), this.alsCategory.getUid());

		// 創造の台座
		registry.handleRecipes(PedalRecipes.class, PedalJeiRecipeWrapper::new, this.pedalCategory.getUid());
		registry.addRecipes(SweetMagicAPI.pedalRecipe, this.pedalCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.pedalList, this.pedalCategory.getUid());

		// 魔法流の机
		registry.handleRecipes(MFTableRecipes.class, MFTableJeiRecipeWrapper::new, this.tableCategory.getUid());
		registry.addRecipes(SweetMagicAPI.mfTableRecipe, this.tableCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.mftableList, this.tableCategory.getUid());

		// 魔法流の貯蔵庫
		registry.handleRecipes(JeiRecipeMFTank.class, MFTankJeiRecipeWrapper::new, this.mftankCategory.getUid());
		registry.addRecipes(JeiRecipeMFTank.recipes, this.mftankCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.mftankList, this.mftankCategory.getUid());
		registry.addRecipeClickArea(GuiMFTank.class, 80, 35, 21, 14, new String[] { "sweetmagic_mftank" });

		// オブ・マギア
		registry.handleRecipes(ObMagiaRecipes.class, ObMagiaRecipeWrapper::new, this.obmagiaCategory.getUid());
		registry.addRecipes(SweetMagicAPI.magiaRecipe, this.obmagiaCategory.getUid());
		registry.addRecipeCatalyst(new ItemStack(BlockInit.obmagia_bottom), this.obmagiaCategory.getUid());

		// オーブン
		registry.handleRecipes(OvenRecipes.class, OvenJeiRecipeWrapper::new, this.ovenCategory.getUid());
		registry.addRecipes(SweetMagicAPI.ovenRecipe, this.ovenCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.ovenList, this.ovenCategory.getUid());

		// ジュースメイカー
		registry.handleRecipes(JuiceMakerRecipes.class, JuiceMakerJeiRecipeWrapper::new, this.juiceMakerCategory.getUid());
		registry.addRecipes(SweetMagicAPI.juiceRecipe, this.juiceMakerCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.juiceList, this.juiceMakerCategory.getUid());
		registry.addRecipeClickArea(GuiJuiceMaker.class, 95, 36, 21, 13, new String[] { "sweetmagic_juicemaker" });

		// 冷蔵庫
		registry.handleRecipes(FreezerRecipes.class, FreezerJeiRecipeWrapper::new, this.freezCategory.getUid());
		registry.addRecipes(SweetMagicAPI.freezRecipe, this.freezCategory.getUid());
		registry.addRecipeCatalyst(new ItemStack(BlockInit.freezer_bottom), this.freezCategory.getUid());

		// 製粉機
		registry.handleRecipes(FlourMillRecipes.class, MillJeiRecipeWrapper::new, this.millCategory.getUid());
		registry.addRecipes(SweetMagicAPI.millRecipe, this.millCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.millList, this.millCategory.getUid());

		// 鍋
		registry.handleRecipes(PotRecipes.class, PotJeiRecipeWrapper::new, this.potCategory.getUid());
		registry.addRecipes(SweetMagicAPI.potRecipe, this.potCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.potList, this.potCategory.getUid());

		// フライパン
		registry.handleRecipes(PanRecipes.class, PanJeiRecipeWrapper::new, this.panCategory.getUid());
		registry.addRecipes(SweetMagicAPI.panRecipe, this.panCategory.getUid());
		this.addRecpeCatalyst(registry, BlockInit.frypanList, this.panCategory.getUid());

		// 発酵機
		registry.handleRecipes(FermenterRecipes.class, FermenteRecipeWrapper::new, this.fermenteCategory.getUid());
		registry.addRecipes(SweetMagicAPI.fermenterRecipe, this.fermenteCategory.getUid());
		registry.addRecipeCatalyst(new ItemStack(BlockInit.matured_bottle), this.fermenteCategory.getUid());

		// ItemStackをArrayListに入れてブラックリストに入れていく。
		this.setBlackListItemStack(registry.getJeiHelpers().getIngredientBlacklist());
	}

	public void addRecpeCatalyst (IModRegistry registry, List<Block> blockList, String uuId) {

		if (blockList.isEmpty()) { return; }

		for (Block block : blockList) {
			registry.addRecipeCatalyst(new ItemStack(block), uuId);
		}
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime runtime) {
		this.recipesGui = runtime.getRecipesGui();
		this.recipeRegistry = runtime.getRecipeRegistry();
	}

	private void setBlackListItemStack(IIngredientBlacklist blacklist) {

		for (Item item : ItemInit.noTabList) {
			blacklist.addIngredientToBlacklist(new ItemStack(item));
		}

		for (Block block : BlockInit.noTabList) {
			blacklist.addIngredientToBlacklist(new ItemStack(block));
		}
	}
}
