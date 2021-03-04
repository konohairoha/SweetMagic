package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.DimensionInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileStardustCrystal;
import sweetmagic.util.TeleportUtil;

public class StardustCrystal extends BaseModelBlock {

	public final int data;

	public StardustCrystal (String name, int data, List<Block> list) {
        super(Material.GLASS, name);
        setSoundType(SoundType.GLASS);
        setLightLevel(0.5F);
        this.data = data;
		list.add(this);
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) { }

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return super.canPlaceBlockAt(world, pos) && super.canPlaceBlockAt(world, pos.up());
	}

	// ブロック設置したときの処理(上のブロックを設置)
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		world.setBlockState(pos.up(), BlockInit.sturdust_crystal_top.getDefaultState(), 2);
	}

	// ブロックをこわしたとき(下のブロックを指定)
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.breakBlock(this.data == 0 ? pos.up() : pos.down(), world, true);
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.data == 0 ? new ItemStack(this).getItem() : ItemStack.EMPTY.getItem();
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(BlockInit.sturdust_crystal_bot);
	}

    @Override
	public float getEnchantPowerBonus(World world, BlockPos pos) {
		return 2.5F;
	}

    @Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return this.data == 0 ? new TileStardustCrystal() : null;
	}

	//右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (this.data == 1 && world.getBlockState(pos.down()).getBlock() != BlockInit.sturdust_crystal_bot) { return false; }
		if (world.isRemote) { return true; }

		// topなら下に下げる
		if (this.data == 1) {
			pos = pos.down();
		}

		// tileの取得
		TileStardustCrystal tile = (TileStardustCrystal) world.getTileEntity(pos);

		// まだ一度もクリックしてないなら
		if (!tile.isNew) {

//			tile.pX = pos.getX();
			tile.nowY = pos.getY();
//			tile.pZ = pos.getZ();

			// Y座標が低いなら上に上げる
			if (pos.getY() <= 40) {
				pos = new BlockPos(pos.getX(), 64, pos.getZ());
				tile.pY = 64;
			}
		}

		// すでに作ってるなら座標の取得
		else if (tile.isNew) {
			pos = new BlockPos(tile.pX, tile.pY, tile.pZ);
		}

		// オーバーワールドに戻る
		if (DimensionInit.dimID == player.dimension) {
			TeleportUtil.teleportToDimension(player, 0, pos);
			this.checkBlock(world, player, pos, 0, tile, false);
		}

		// スイートマジックワールドに移動
		else {
			TeleportUtil.teleportToDimension(player, DimensionInit.dimID, pos);
			this.checkBlock(world, player, pos, DimensionInit.dimID, tile, !tile.isNew);
		}

		return true;
	}

	// 周りのブロックを確認
	public void checkBlock (World world, EntityPlayer player, BlockPos move, int dimId, TileStardustCrystal tile, boolean setCystal) {

		// 移動先の座標を取得
		World seWorld = player.getEntityWorld();
        WorldServer sever = seWorld.getMinecraftServer().getWorld(dimId);

		// まだ一度もクリックしてないなら
		if (!tile.isNew) {

			tile.isNew = true;
			move = sever.getTopSolidOrLiquidBlock(new BlockPos(move.getX(), 0, move.getZ()));
			if (move.getY() < 64) { move = new BlockPos(move.getX(), 65, move.getZ()); }
			tile.pX = move.getX();
			tile.pY = move.getY();
			tile.pZ = move.getZ();
			TeleportUtil.teleportToDimension(player, dimId, move);

			for (BlockPos p : BlockPos.getAllInBox(move.add(-2, 0, -2), move.add(2, 4, 2))) {
				if (!sever.isAirBlock(p)) {
					sever.setBlockState(p, Blocks.AIR.getDefaultState(), 3);
				}
			}

			for (BlockPos p : BlockPos.getAllInBox(move.add(-2, -1, -2), move.add(2, -1, 2))) {
				IBlockState state = sever.getBlockState(p);
				if (!state.getBlock().isFullBlock(state)) {
					sever.setBlockState(p, Blocks.GRASS.getDefaultState(), 3);
				}
			}

			// クリスタルを生成するなら
			if (setCystal) {
				sever.setBlockState(move, BlockInit.sturdust_crystal_bot.getDefaultState(), 3);
				sever.setBlockState(move.up(), BlockInit.sturdust_crystal_top.getDefaultState(), 3);
				TileStardustCrystal crystal = (TileStardustCrystal) sever.getTileEntity(move);
				crystal.isNew = true;
				crystal.pX = tile.pX;
				crystal.pY = tile.nowY;
				crystal.pZ = tile.pZ + 1;
			}
		}
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.sturdust_crystal.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tip));
	}
}
