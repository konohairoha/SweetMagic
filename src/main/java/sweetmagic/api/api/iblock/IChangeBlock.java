package sweetmagic.api.iblock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IChangeBlock {

	void setBlock (World world, BlockPos pos, EntityPlayer player);
}
