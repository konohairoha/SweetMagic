package sweetmagic.recipe;

import net.minecraft.block.Block;

public class RecipeRegisterHelper {

	public Block log = null;
	public Block plate = null;

	public Block planks = null;
	public Block stairs = null;
	public Block slab = null;

	public Block stone = null;
	public String ore = null;

	public Block glass = null;
	public Block pane = null;

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
}
