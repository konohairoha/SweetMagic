package sweetmagic.init.block.magic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.SMGuiHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.tile.magic.TileAetherHopper;
import sweetmagic.util.SMChunkLoader;
import sweetmagic.util.SMChunkLoader.IChunkBlock;

public class AetherHopper extends BaseMFBlock implements IChunkBlock {

	// 向きをいれておく変数として FACING を宣言
	public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
	private final int data;

    public AetherHopper(String name, int data) {
		super(name);
		this.data = data;
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
		BlockInit.magicList.add(this);
    }

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {
		if (world.isRemote) { return; }
		this.openGui(world, player, pos, SMGuiHandler.AETHERHOPPER);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {

		switch (this.data) {
		case 0: return new TileAetherHopper();
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.aether_hopper.name")));
		super.addInformation(stack, world, tooltip, advanced);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {

		super.breakBlock(world, pos, state);

		// チャンクのローダーを削除
		int d = world.provider.getDimension();
		SMChunkLoader.getInstance().deleteBlockTicket(world, pos.getX(), pos.getY(), pos .getZ(), pos.getX() >> 4, pos.getZ() >> 4, d);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		int d = world.provider.getDimension();
		SMChunkLoader.getInstance().setBlockTicket(world, pos.getX(), pos.getY(), pos.getZ(), pos.getX() >> 4, pos.getZ() >> 4, d);
	}

	@Override
	public boolean canLoad(World world, int x, int y, int z) {
		return true;
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing face, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.getStateForPlacement(world, pos, face, hitX, hitY, hitZ, meta, placer);
		return state.withProperty(FACING, face);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
