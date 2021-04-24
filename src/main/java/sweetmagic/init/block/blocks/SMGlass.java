package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockGlass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;

public class SMGlass extends BlockGlass {

	private final int data;
	private boolean isPass;

	public SMGlass(String name, int data, boolean shading, boolean isPass) {
		super(Material.GLASS, false);
		setUnlocalizedName(name);
        setRegistryName(name);
        setSoundType(SoundType.GLASS);
		setHardness(0.2F);
		setResistance(1024F);
		this.data = data;
		//ブロックの光を透過する強さ　数値が高いほどブロックは不透明、光を通さないようになる。
		this.setLightOpacity(shading ? 255 : 0);
		this.isPass = isPass;
		BlockInit.blockList.add(this);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	public int quantityDropped(Random random) {
        return 1;
    }

	// フェンスとかにつながないように
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) {
		if (!(entity instanceof EntityPlayer) || !this.isPass) {
			super.addCollisionBoxToList(state, world, pos, aabb, aabbList, entity, flag);
		}
	}
}
