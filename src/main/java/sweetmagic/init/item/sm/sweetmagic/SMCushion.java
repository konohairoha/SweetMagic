package sweetmagic.init.item.sm.sweetmagic;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.ItemInit;
import sweetmagic.init.entity.projectile.EntityCushion;

public class SMCushion extends SMItem {

	private final int data;

	public SMCushion (String name, int data) {
		super(name, ItemInit.furniList);
		this.data = data;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);

		if (!player.isCreative()) { stack.shrink(1); }

		if (!world.isRemote) {

			BlockPos offPos = pos.offset(face);
			Entity entity = new EntityCushion(world, x, y, z, player, this.data);
			entity.setLocationAndAngles(offPos.getX() + 0.5F, offPos.getY() + 0.5F, offPos.getZ() + 0.5F, player.isSneaking() ? 0 : player.rotationYaw, 0.0F);
//			entity.setPosition(offPos.getX() + 0.5F, offPos.getY() + 0.5F, offPos.getZ() + 0.5F);
	    	world.spawnEntity(entity);
		}

		return EnumActionResult.SUCCESS;
	}
}
