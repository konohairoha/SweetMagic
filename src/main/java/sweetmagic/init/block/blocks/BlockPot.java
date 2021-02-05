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
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.recipe.pot.PotRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseFaceBlock;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockPot extends BaseFaceBlock {

	public static boolean keepInventory = false;
	public final int data;

	public BlockPot(String name, float light, int data, List<Block> list) {
		super(Material.IRON, name);
		setHardness(1.0F);
        setResistance(1024F);
		setSoundType(SoundType.STONE);
		this.setLightLevel(light);
		disableStats();
		this.data = data;
		list.add(this);
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0.19D, 0.38D, 0.19D, 0.81D, 0.0D, 0.81D);
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

	//Tick更新処理が必要なブロックには必ず入れること
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(worldIn, pos, state, rand);
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

        if (world.getBlockState(pos).getBlock() != BlockInit.pot_re) {

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
		if (state.getBlock() == BlockInit.pot_off) {

			// 手持ちアイテムからレシピと一致するかを検索
			PotRecipeInfo recipeInfo = SweetMagicAPI.getPotRecipeInfo(stack, pInv);

			// 入れるアイテム、完成品はItemStackリストに突っ込む
			List<ItemStack> inputs = new ArrayList<ItemStack>();
			List<ItemStack> results = new ArrayList<ItemStack>();
			ItemStack copy = stack.copy();

			// クラフトに失敗したら
			if (!recipeInfo.canComplete) { return false; }

			// クラフトに成功
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

		//完成後
		else if (state.getBlock() == BlockInit.pot_re) {

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

    public static void setState(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        TileEntity tile = world.getTileEntity(pos);
        Block block = state.getBlock();
        keepInventory = true;
		if (block == BlockInit.pot_on) {
			world.setBlockState(pos, BlockInit.pot_re.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else if (block == BlockInit.pot_re) {
			world.setBlockState(pos, BlockInit.pot_off.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
		} else if (block == BlockInit.pot_off) {
			world.setBlockState(pos, BlockInit.pot_on.getDefaultState().withProperty(FACING, state.getValue(FACING)), 2);
			BlockStove.setState(world, pos.down());
		}
		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (!keepInventory) {

			ItemStack stack = new ItemStack(Item.getItemFromBlock(BlockInit.pot_off));
			TilePot tile = (TilePot) world.getTileEntity(pos);

			// 製粉機（オフ状態）か製粉機（稼働状態）のときtileの入力リストを取り出す
			if (state.getBlock() == BlockInit.pot_on || state.getBlock() == BlockInit.pot_re) {

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

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}


	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		if (this.data == 0 || ( this.data == 2 && rand.nextInt(4) != 0 ) ) { return; }

		int count = this.data == 1 ? 4 : 1;

		float x = pos.getX() + 0.1F;
		float y = pos.getY() + 0.75F;
		float z = pos.getZ() + 0.1F;

		for (int i = 0; i < count; i++) {
			float f1 = x + rand.nextFloat() * 0.8F;
			float f2 = y + rand.nextFloat() * 6.0F / 16.0F;
			float f3 = z + rand.nextFloat() * 0.8F;
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f1, f2, f3, 0.0D, 0.0D, 0.0D);
		}
	}
}
