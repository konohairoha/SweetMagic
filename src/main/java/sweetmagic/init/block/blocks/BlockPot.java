package sweetmagic.init.block.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.SweetMagicAPI;
import sweetmagic.api.enumblock.EnumCook;
import sweetmagic.api.enumblock.EnumCook.FaceCookMeta;
import sweetmagic.api.enumblock.EnumCook.PropertyCook;
import sweetmagic.api.recipe.pot.PotRecipeInfo;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseCookBlock;
import sweetmagic.init.tile.cook.TilePot;
import sweetmagic.init.tile.cook.TileStove;
import sweetmagic.util.RecipeHelper;
import sweetmagic.util.RecipeUtil;

public class BlockPot extends BaseCookBlock {

	private static boolean keepInventory = false;
	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.16D, 0.38D, 0.16D, 0.84D, 0D, 0.84D);
	public static final PropertyCook COOK = new PropertyCook("cook", EnumCook.getCookList());

	public BlockPot(String name, int data, boolean isRegister) {
		super(name);
		setHardness(0.1F);
		this.data = data;
		setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(COOK, EnumCook.OFF));

		if (isRegister) {
			BlockInit.potList.add(this);
		}
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePot();
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return true; }

        // 下のブロックがコンロではないなら終了
        if (!this.getCook(state).isFIN()) {
            Block uBlock = world.getBlockState(pos.down()).getBlock();
            if (!(uBlock instanceof BlockStove)) { return false;}
        }

		// tileとプレイヤーのInventoryの取得
		TilePot pot = (TilePot) world.getTileEntity(pos);
		NonNullList<ItemStack> pInv = player.inventory.mainInventory;

		// 未起動
		if (this.getCook(state).isOFF()) {

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

	    		// 必要燃焼時間を超えていないまたはスロットに燃焼アイテムがないなら終了
	    		TileStove stove = (TileStove) world.getTileEntity(pos.down());
	    		if (!stove.canWork()) { return false; }

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
			pot.hasFork = this.hasFork(player);

			//ブロックをON用に置き換え　※起動処理
			this.setState(world, pos);
		}

		//完成後
		else if (this.getCook(state).isFIN()) {

			this.spawnXp(player, pot.outPutList, pot.hasFork);
			this.spawnItem(world, player, pot.outPutList);
			this.setState(world, pos);

			// ハンドアイテムとかの初期化
			TilePot nextTile = (TilePot) world.getTileEntity(pos);
			nextTile.clearItem();
		}
		return true;
	}

    public static void setState(World world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        keepInventory = true;
		world.setBlockState(pos, EnumCook.transitionCook(world.getBlockState(pos), COOK), 3);
		keepInventory = false;
		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }
    }

    public void breakBlock(World world, BlockPos pos, IBlockState state) {

		if (!keepInventory) {

			TilePot tile = (TilePot) world.getTileEntity(pos);

			// オフ状態以外ならアイテム取り出し
			if (!this.getCook(state).isOFF()) {
				this.spawnItem(world, pos, tile.inPutList);
				world.spawnEntity(tile.getEntityItem(pos, tile.handItem));
			}

			tile.startFlag = false;
			spawnAsEntity(world, pos, new ItemStack(this));
		}

		if (BlockStove.isStoveOn(world, pos.down())) {
			BlockStove.setState(world, pos.down());
		}
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	public EnumCook getCook (IBlockState state) {
		return state.getValue(COOK);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, COOK });
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + state.getValue(COOK).getMeta();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		FaceCookMeta fcMeta = EnumCook.getMeta(meta);
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(fcMeta.getMeta())).withProperty(COOK, fcMeta.getCook());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		EnumCook cook = this.getCook(state);
		if (cook.isOFF() || (cook.isFIN() && rand.nextInt(4) != 0)) { return; }

		int count = cook == EnumCook.ON ? 4 : 1;
		float x = pos.getX() + 0.1F;
		float y = pos.getY() + 0.75F;
		float z = pos.getZ() + 0.1F;

		for (int i = 0; i < count; i++) {
			float f1 = x + rand.nextFloat() * 0.8F;
			float f2 = y + rand.nextFloat() * 6F / 16F;
			float f3 = z + rand.nextFloat() * 0.8F;
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, f1, f2, f3, 0D, 0D, 0D);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		if (this.data == 0) { return; }
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.smpot.name")));
	}
}
