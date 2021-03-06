package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.flourmill.FlourMillRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TileFlourMill;

public class BlockFlourMill extends BaseFaceBlock {

	public static boolean keepInventory = false;

	public BlockFlourMill(String name, List<Block> list) {
		super(Material.IRON, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.STONE);
		disableStats();
		list.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileFlourMill();
	}

	//Tick更新処理が必要なブロックには必ず入れること
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	// 副産物計算用メソッド　ランダムに３分の１の確率で個数を計上する
	public int decMainStack(int amt, World world) {

		int ret = 0;
		Random rand = world.rand;

		for (int i = 0; i < amt; i++) {
			if (rand.nextInt(3) == 0) {
				ret++;
			}
		}

		return ret;
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

		TileFlourMill tile = (TileFlourMill) world.getTileEntity(pos);

		//プレイヤーのInventoryの取得
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;
		Block block = state.getBlock();

		if (block == BlockInit.flourmill_off) {

			//製粉機レシピ情報を取得
			FlourMillRecipeInfo recipeinfo = SweetMagicAPI.getFlourMillRecipeInfo(stack, pInv);

			// canComplete = Falseの場合レシピ処理をしない
			if (!recipeinfo.canComplete) { return false; }

			ItemStack handitem = stack.copy();
			ItemStack recipeItem = recipeinfo.getHandList().get(0);
			List<ItemStack> outs = recipeinfo.getOutputs();

			//入れるアイテム、完成品はItemStackリストに突っ込む
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();

			// 手に持っているアイテムを処理する
			// 減らすべき個数を計算
			int handamt = stack.getCount() % recipeItem.getCount();
			int amt = stack.getCount() / recipeItem.getCount();
			// 減らすアイテムを作成
			ItemStack send = new ItemStack(handitem.getItem(), stack.getCount() - handamt, handitem.getMetadata());
			// レシピリストに減らしたアイテムをぶち込む
			inputs.add(send);
			// 手に持っているアイテムから計算された個数を減らす
			stack.shrink(send.getCount());

			// 完成品と副産物を計算していく
			for (int i = 0; i < outs.size(); i++) {

				ItemStack output = outs.get(i);
				//完成品
				if (i == 0) {

					// 稼働後の個数の計算
					int stackCount = amt * output.getCount();
					boolean finishFlag = false;

					// フラグがtrueになるまで続ける
					while (!finishFlag) {

						// 個数が64以上なら64にさせる
						int count = stackCount >= 64 ? 64 : stackCount;
						results.add(new ItemStack(output.getItem(), count, output.getMetadata()));

						// リストに入れた分だけ合計個数から減らす
						stackCount -= count;

						// 合計個数が0以下なら終了
						finishFlag = stackCount <= 0;
					}

				//副産物
				} else {

					int ret = this.decMainStack(amt, world);
					if (ret > 0) {
						results.add(new ItemStack(outs.get(i).getItem(), ret * output.getCount(), output.getMetadata()));
					}
				}
			}

			//TileEntityにアイテムリストのNBTを突っ込む
			tile.handItem = handitem;
			tile.inPutList = inputs;
			tile.outPutList = results;
			tile.tickTime = 0;
			tile.tickSet = true;

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);

		} else if (block == BlockInit.flourmill_re) {

			//TileEntityの完成品リストからアイテムをスポーンさせる
			this.spawnItem(world, player, tile.outPutList);

			this.setState(world, pos);

			// ハンドアイテムとかの初期化
			TileFlourMill nextTile = (TileFlourMill) world.getTileEntity(pos);
			nextTile.clearItem();
		}

		return true;
	}

    public static void setState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        Block block = state.getBlock();
        keepInventory = true;
		if (block == BlockInit.flourmill_off) {
			world.setBlockState(pos, BlockInit.flourmill_on.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else if (block == BlockInit.flourmill_on) {
			world.setBlockState(pos, BlockInit.flourmill_re.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else if (block == BlockInit.flourmill_re) {
			world.setBlockState(pos, BlockInit.flourmill_off.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		}
		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (keepInventory) { return; }

		ItemStack stack = new ItemStack(Item.getItemFromBlock(BlockInit.flourmill_off));
		// 製粉機（オフ状態）か製粉機（稼働状態）のときtileの入力リストを取り出す
		if (state.getBlock() == BlockInit.flourmill_re || state.getBlock() == BlockInit.flourmill_on) {
			TileFlourMill tile = (TileFlourMill) world.getTileEntity(pos);
			this.spawnItem(world, pos, tile.inPutList);
		}
		spawnAsEntity(world, pos, stack);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }
}
