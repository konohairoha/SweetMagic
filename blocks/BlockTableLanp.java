package sweetmagic.init.block.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;

public class BlockTableLanp extends BaseModelBlock {

	private final boolean isChange;
	private final int data;
	private final static AxisAlignedBB AABB = new AxisAlignedBB(0.3D, 0.8D, 0.3D, 0.7D, 0D, 0.7D);
	private final static AxisAlignedBB SPOT = new AxisAlignedBB(0D, 0.25D, 0.46875D, 1D, 1D, 0.53125D);

	public BlockTableLanp (String name, float lightLevel, int data, boolean isChange, List<Block> list) {
		super(Material.GLASS, name);
		setSoundType(SoundType.GLASS);
		setHardness(0.5F);
        setResistance(1024F);
		setLightLevel(lightLevel);
		this.isChange = isChange;
		this.data = data;
		list.add(this);
	}

	// 右クリックの処理
	public boolean actionBlock (World world, IBlockState state, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (!this.isChange) { return false; }

		if (world.isRemote) { return true; }

		Block block = world.getBlockState(pos).getBlock();

		if (block == BlockInit.table_modernlamp_off) {
			world.setBlockState(pos, BlockInit.table_modernlamp_on.getDefaultState(), 2);
		}

		else {
			world.setBlockState(pos, BlockInit.table_modernlamp_off.getDefaultState(), 2);
		}

		world.playSound(null, pos, SoundEvents.UI_BUTTON_CLICK, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 1.2F);

		return true;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this.isChange ? BlockInit.table_modernlamp_off : this);
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return new ItemStack(this.isChange ? BlockInit.table_modernlamp_off : this);
    }

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return this.data == 1 ? SPOT : AABB;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> aabbList, Entity entity, boolean flag) {
		if (this.data == 1) {
			super.addCollisionBoxToList(state, world, pos, aabb, aabbList, entity, flag);
		}
	}
}
