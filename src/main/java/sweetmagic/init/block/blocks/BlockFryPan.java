package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.pan.PanRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockFryPan extends BaseFaceBlock {

	public static boolean keepInventory = false;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.2D, 0.2D, 0.1D, 0.8D, 0D, 0.8D);
	private final int data;
	private final CookingType type;

	public BlockFryPan(String name, float light, CookingType type, int data, List<Block> list) {
		super(Material.IRON, name);
		setHardness(0.1F);
		setResistance(1024F);
		setSoundType(SoundType.STONE);
		this.setLightLevel(light);
		disableStats();
		this.type = type;
		this.data = data;
		list.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TilePot();//TileEntityは処理自体ほぼ同じなため製粉機を指定
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

        if (this.type != CookingType.FIN) {

            // 下のブロックがコンロではないなら終了
            Block uBlock = world.getBlockState(pos.down()).getBlock();
            if (!(uBlock instanceof BlockStove)) { return false;}

    		TileStove stove = (TileStove) world.getTileEntity(pos.down());

    		// 必要燃焼時間を超えていないまたはスロットに燃焼アイテムがないなら終了
    		if (!stove.canCook() && !stove.canSmelt()) { return false; }
        }

		TilePot pot = (TilePot) world.getTileEntity(pos);

		// プレイヤーのInventoryの取得
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;

		// 未起動
		if (this.type == CookingType.PRE) {

			// 手持ちアイテムからレシピと一致するかを検索
			PanRecipeInfo recipeInfo = SweetMagicAPI.getPanRecipeInfo(stack, pInv);

			//入れるアイテム、完成品はItemStackリストに突っ込む
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();
			ItemStack copy = stack.copy();

			// クラフト失敗
			if (!recipeInfo.canComplete) {

				ItemStack smeltResult = FurnaceRecipes.instance().getSmeltingResult(stack).copy();

				if (smeltResult.isEmpty() || !(smeltResult.getItem() instanceof ItemFood)) {
					return false;
				}

				inputs.add(stack);
				results.add(smeltResult);
				smeltResult.setCount(stack.getCount());
				stack.shrink(stack.getCount());
			}

			// クラフト成功
			else {

				// クラフト処理
				RecipeUtil recipeUtil = RecipeHelper.recipeAllCraft(recipeInfo, player, stack);
				copy = recipeUtil.getHand();
				inputs.addAll(recipeUtil.getInput());
				results.addAll(recipeUtil.getResult());
			}

			pot.startFlag = true;
			pot.handItem = copy;
			pot.inPutList = inputs;
			pot.outPutList = results;

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);
		}

		// 完成後
		else if (this.type == CookingType.FIN) {

			for (ItemStack s : pot.outPutList) {
				world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, s));
			}

			this.setState(world, pos);

			// ハンドアイテムとかの初期化
			TilePot nextTile = (TilePot) world.getTileEntity(pos);
			nextTile.clearItem();
		}
		return true;
	}

    public void setState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;

        Block frypan = null;

        // 通常フライパン
        if (this.data == 0) {
        	switch (this.type) {
        	case PRE:
        		frypan = BlockInit.frypan_on;
        		break;
        	case COOKING:
        		frypan = BlockInit.frypan_re;
        		break;
        	case FIN:
        		frypan = BlockInit.frypan_off;
        		break;
        	}
        }

        // フライパン(赤)
        else if (this.data == 1) {
        	switch (this.type) {
        	case PRE:
        		frypan = BlockInit.frypan_red_on;
        		break;
        	case COOKING:
        		frypan = BlockInit.frypan_red_re;
        		break;
        	case FIN:
        		frypan = BlockInit.frypan_red_off;
        		break;
        	}
        }

		world.setBlockState(pos, frypan.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);

		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (!keepInventory) {

			ItemStack stack = new ItemStack(this.data == 0 ? BlockInit.frypan_off : BlockInit.frypan_red_off);
			TilePot tile = (TilePot) world.getTileEntity(pos);

			// 製粉機（オフ状態）か製粉機（稼働状態）のときtileの入力リストを取り出す
			if (this.type != CookingType.PRE) {
				for (ItemStack s : tile.inPutList) {
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), s));
				}
				world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), tile.handItem));
			}
			tile.startFlag = false;
			spawnAsEntity(world, pos, stack);
		}

		if (world.getBlockState(pos.down()).getBlock() == BlockInit.stove_on) {
			BlockStove.setState(world, pos.down());
		}
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public enum CookingType {
    	PRE,
    	COOKING,
    	FIN;
    }
}
