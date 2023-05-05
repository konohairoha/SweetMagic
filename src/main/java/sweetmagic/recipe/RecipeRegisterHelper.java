package sweetmagic.recipe;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.Ingredient;

public class RecipeRegisterHelper {

	private Block log = null;
	private Block plate = null;

	private Block planks = null;
	private Block stairs = null;
	private Block slab = null;

	private Block stone = null;
	private String ore = null;

	private Block glass = null;
	private Block pane = null;

	private String dye = null;

	private String brick = null;
	private Block brick_0 = null;
	private Block brick_1 = null;
	private Block brick_2 = null;

	private Block brick_3 = null;
	private Block brick_4 = null;
	private Block brick_5 = null;
	private Block brick_6 = null;

	private Ingredient ing = null;

	public RecipeRegisterHelper (Block planks, Block stairs, Block slab) {
		this.planks = planks;
		this.stairs = stairs;
		this.slab = slab;
	}

	public RecipeRegisterHelper (Block log, Block planks, Block plate, boolean flag) {
		this.log = log;
		this.planks = planks;
		this.plate = plate;
	}

	public RecipeRegisterHelper (Block stone, String ore) {
		this.stone = stone;
		this.ore = ore;
	}

	public RecipeRegisterHelper (Block glass, Block pane) {
		this.glass = glass;
		this.pane = pane;
	}

	public RecipeRegisterHelper (Block planks, String dye, boolean flag) {
		this.planks = planks;
		this.dye = dye;
	}

	public RecipeRegisterHelper (String brick, Block brick_0, Block brick_1, Block brick_2, Block stairs, Block slab) {
		this.brick = brick;
		this.brick_0 = brick_0;
		this.brick_1 = brick_1;
		this.brick_2 = brick_2;
		this.stairs = stairs;
		this.slab = slab;
	}

	public RecipeRegisterHelper (Block log, Block planks, Block brick_0, Block brick_1, Block brick_2, Block brick_3) {
		this.log = log;
		this.planks = planks;
		this.brick_0 = brick_0;
		this.brick_1 = brick_1;
		this.brick_2 = brick_2;
		this.brick_3 = brick_3;
	}

	public RecipeRegisterHelper (Ingredient ing, Block brick_0, Block brick_1, Block brick_2, Block brick_3, Block brick_4, Block brick_5, Block brick_6) {
		this.ing = ing;
		this.brick_0 = brick_0;
		this.brick_1 = brick_1;
		this.brick_2 = brick_2;
		this.brick_3 = brick_3;
		this.brick_4 = brick_4;
		this.brick_5 = brick_5;
		this.brick_6 = brick_6;
	}

	public Block getPlanks () {
		return this.planks;
	}

	public Block getStairs () {
		return this.stairs;
	}

	public Block getSlab () {
		return this.slab;
	}

	public Block getLog () {
		return this.log;
	}

	public Block getPlate () {
		return this.plate;
	}

	public Block getStone () {
		return this.stone;
	}

	public String getOre () {
		return this.ore;
	}

	public Block getGlass () {
		return this.glass;
	}

	public Block getPane () {
		return this.pane;
	}

	public String getDye () {
		return this.dye;
	}

	public String getBrick () {
		return this.brick;
	}

	public Block getBrick0 () {
		return this.brick_0;
	}

	public Block getBrick1 () {
		return this.brick_1;
	}

	public Block getBrick2 () {
		return this.brick_2;
	}

	public Block getBrick3 () {
		return this.brick_3;
	}

	public Block getBrick4 () {
		return this.brick_4;
	}

	public Block getBrick5 () {
		return this.brick_5;
	}

	public Block getBrick6 () {
		return this.brick_6;
	}

	public Ingredient getIng () {
		return this.ing;
	}
}
