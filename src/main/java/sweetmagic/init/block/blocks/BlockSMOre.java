package sweetmagic.init.block.blocks;

import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;

public class BlockSMOre extends SMIron {

	private final int data;

	public BlockSMOre(String name, int meta) {
        super(Material.ROCK, name);
        setHardness(5.5F);
        setResistance(64F);
        setHarvestLevel("pickaxe", 2);
        setSoundType(SoundType.STONE);
        setLightLevel(0.25F);
        this.data = meta;
		BlockInit.blockList.add(this);
    }

	//ドロップさせるアイテム
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		switch(this.data) {
		case 0:
			return ItemInit.aether_crystal;
		case 1:
			return ItemInit.cosmic_crystal_shard;
		default:
			return ItemInit.aether_crystal;
		}
	}

	//ドロップさせる数
	@Override
	public int quantityDropped(Random random) {
		switch(this.data) {
		case 0:
			return random.nextInt(2) + 1;
		case 1:
			return 1;
		default:
			return 1;
		}
	}

	//幸運のエンチャントによる加算
	@Override
	public int quantityDroppedWithBonus(int fortune, Random rand) {
    	if (fortune > 0 &&
    			Item.getItemFromBlock(this) != this.getItemDropped((IBlockState)this.getBlockState().getValidStates().iterator().next(), rand, fortune)) {
    		int i = rand.nextInt(fortune + 2) - 1;
			if (i < 0) { i = 0; }
			return this.quantityDropped(rand) * (i + 1);
		} else {
			return this.quantityDropped(rand);
		}
    }

    //経験値ドロップ
    @Override
	public int getExpDrop(IBlockState state, IBlockAccess world, BlockPos pos, int fortune) {
		Random rand = world instanceof World ? ((World) world).rand : new Random();
    	return MathHelper.getInt(rand, 3, 6);
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
}
