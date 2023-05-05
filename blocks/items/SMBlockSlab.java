package sweetmagic.init.block.blocks.items;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.api.iblock.ISlabItemBlock;
import sweetmagic.init.block.blocks.AntiqueSlab;
import sweetmagic.init.block.blocks.AntiqueSlab.EnumBlockSlab;

public class SMBlockSlab extends ItemBlock {

	private final ISlabItemBlock slab;

	public SMBlockSlab (Block block, ISlabItemBlock slab) {
		super(block);
		this.slab = slab;
	}

    @Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);
		IBlockState stateBlock = world.getBlockState(pos);
		Block block = stateBlock.getBlock();

		// スニークでアイテムを複数持ってる場合は半ブロを重ねた状態で設置
    	if (player.isSneaking() && ( ( !player.isCreative() && stack.getCount() >= 2 ) || player.isCreative() ) &&
    			world.isAirBlock(pos.offset(face)) && ( !(block instanceof AntiqueSlab) || block.isOpaqueCube(stateBlock)) ) {

    		IBlockState state = this.block.getDefaultState();
            world.setBlockState(pos.offset(face), state.withProperty(AntiqueSlab.HALF, EnumBlockSlab.FULL));
            if (!player.isCreative()) { stack.shrink(2); }

            SoundType sound = state.getBlock().getSoundType(state, world, pos, player);
            world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F);
            return EnumActionResult.SUCCESS;
    	}

		boolean checkFace = face != EnumFacing.UP && face != EnumFacing.DOWN;
		if (!player.canPlayerEdit(pos.offset(face), face, stack) || checkFace) { return super.onItemUse(player, world, pos, hand, face, x, y, z); }

		EnumActionResult action = this.slab.setBlock(world, player, pos, stack);
        return action == EnumActionResult.SUCCESS ? action : super.onItemUse(player, world, pos, hand, face, x, y, z);
	}
}
