package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.event.SMHarvestEvent.BrrierBreakEvent;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.magic.TileMagicBarrier;

public class MagicBarrier extends BaseModelBlock {

	private final int data;

    public MagicBarrier(String name, int data) {
		super(Material.GLASS, name);
		setHardness(data == 0 ? 999999 : 1F);
		setResistance(999999F);
		setSoundType(SoundType.GLASS);
		this.data = data;
		BlockInit.magicList.add(this);
    }

    /**
     * 0 = 鍵あり
     * 1 = 鍵なし
     */

	// ブロックでのアクション
	@Override
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote || this.data == 1 || stack.getItem() != ItemInit.magickey) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));
		world.setBlockState(pos, BlockInit.magicbarrier_off.getDefaultState(), 2);
		MinecraftForge.EVENT_BUS.register(new BrrierBreakEvent(pos, player, 1));

		if (!player.isCreative()) { stack.shrink(1); }

		// ワールド内のプレイヤー取得
		List<EntityPlayer> entityList = world.playerEntities;

		for (EntityPlayer entity : entityList) {
			entity.removePotionEffect(PotionInit.breakblock);
		}

		return true;
	}

	// アイテムをドロップ
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return new ItemStack(BlockInit.magicbarrier_off).getItem();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return this.data == 0;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return this.data == 0 ? new TileMagicBarrier() : null;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.data == 1;
	}

	// プレイヤー以外は壊せないように
	public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity) {
		return entity instanceof EntityPlayer;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return this.data == 0 ? BlockRenderLayer.SOLID : BlockRenderLayer.TRANSLUCENT;
	}

}
