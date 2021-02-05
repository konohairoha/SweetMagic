package sweetmagic.init.block.blocks;

import javax.annotation.Nonnull;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseModelBlock;
import sweetmagic.init.tile.plant.TileCleroLanp;

public class CleroLanp extends BaseModelBlock {

    public CleroLanp(String name) {
    	super(Material.GLASS, name);
        setHardness(1.0F);
        setResistance(1024F);
        setSoundType(SoundType.GLASS);
        setLightLevel(1F);
		BlockInit.blockList.add(this);
    }

    @Override
	public boolean hasTileEntity(IBlockState state){
		return true;
	}

    // 設置時にプレイヤーIdをtileに保存
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack stack) {
		if (entity instanceof EntityPlayer) {
			TileCleroLanp clero = (TileCleroLanp) world.getTileEntity(pos);
			clero.uuId = entity.getUniqueID();
			clero.player = (EntityPlayer) entity;
		}
	}

    @Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state){
    	return new TileCleroLanp();
	}
}
