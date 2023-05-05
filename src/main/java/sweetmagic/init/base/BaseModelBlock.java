package sweetmagic.init.base;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BaseModelBlock extends Block {

	private boolean emptyAABB = false;

	public BaseModelBlock (Material material, String name) {
		super(material);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
	}

	// 右クリックの処理
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing face, float hitX, float hitY, float hitZ) {
		return this.actionBlock(world, state, pos, player, player.getHeldItem(hand));
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	// サウンド
	public void playerSound (World world, BlockPos pos, SoundEvent sound, float vol, float pit) {
		world.playSound(null, pos, sound, SoundCategory.BLOCKS, vol, pit);
	}

	// サウンド
	public void playerSound (World world, EntityPlayer player, SoundEvent sound, float vol, float pit) {
		this.playerSound(world, new BlockPos(player), sound, vol, pit);
	}

	// アイテムのスポーン
	public void spawnItem (World world, EntityPlayer player, List<ItemStack> list) {
		this.spawnItem(world, player.getPosition(), list);
	}

	// アイテムのスポーン
	public void spawnItem (World world, BlockPos pos, List<ItemStack> list) {
		for (ItemStack stack : list) {
			world.spawnEntity(new EntityItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, stack));
		}
	}

	// アイテムのスポーン
	public void spawnItem (World world, EntityPlayer player, ItemStack stack) {
		BlockPos pos = player.getPosition();
		world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack));
	}

	// ブロック破壊処理
	public boolean breakBlock(BlockPos pos, World world, boolean dropBlock) {

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if (block.isAir(state, world, pos)) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));

		if (dropBlock) {
			block.dropBlockAsItem(world, pos, state, 0);
		}

		return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
	}

	public void setEmptyAABB () {
		this.emptyAABB = true;
	}

	public boolean getEmptyAABB () {
		return this.emptyAABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) {
		if (!this.getEmptyAABB()) {
			super.addCollisionBoxToList(state, world, pos, aabb, aabbList, entity, flag);
		}
	}

	// ブロックチェック
    public boolean checkBlock (ItemStack stack) {
    	return stack.getItem() == Item.getItemFromBlock(this);
    }

    // ブロック設置
	public boolean setBlockSound (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack, int value, boolean isDown) {

		for (int i = 1; i < value; i++) {

			BlockPos setPos = isDown ? pos.down(i) : pos.up(i);
			if (setPos.getY() > 255 || setPos.getY() < 1) { return false; }

			if (!world.isAirBlock(setPos)) { continue; }

        	world.setBlockState(setPos, this.getState(stack.getItem()), 3);
            if (!player.isCreative()) { stack.shrink(1); }

            SoundType sound = this.getSoundType(state, world, setPos, player);
            this.playerSound(world, pos.down(i), sound.getPlaceSound(),(sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);
        	return true;
		}

		return false;
	}

	public IBlockState getState (Item item) {
		return ((ItemBlock) item).getBlock().getDefaultState();
	}

	public Block getBlock (IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getBlock();
	}

	// ツールチップ取得
	public String getTip (String tip) {
		return new TextComponentTranslation(tip, new Object[0]).getFormattedText();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this);
	}

    // 向き変更対応
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing face) {

    	TileEntity tile = world.getTileEntity(pos);
		boolean flag = super.rotateBlock(world, pos, face);

		if (tile != null) {
            tile.validate();
            world.setTileEntity(pos, tile);
        }

		return flag;
	}
}
