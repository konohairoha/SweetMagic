package sweetmagic.init.base;

import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.item.sm.armor.MagiciansPouch;

public class BaseCookBlock extends BaseFaceBlock {

	public BaseCookBlock(String name) {
		super(Material.IRON, name);
		setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.STONE);
		this.disableStats();
		BlockInit.furniList.add(this);
	}
	public BaseCookBlock(String name, Material mate) {
		super(mate, name);
		setHardness(0.2F);
		setResistance(1024F);
		setSoundType(SoundType.GLASS);
		this.disableStats();
		BlockInit.furniList.add(this);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

    public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
        return ItemStack.EMPTY;
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return null;
    }

    public boolean hasFork (EntityPlayer player) {
        return MagiciansPouch.hasAcce(player, ItemInit.mysterious_fork);
    }

    public void spawnXp (EntityPlayer player, List<ItemStack> outList, boolean hasFok) {

    	if (!hasFok) { return; }

    	int xp = 0;
    	World world = player.world;

    	for (ItemStack stack : outList) {

    		Item item = stack.getItem();
    		if ( !(item instanceof ItemFood) ) { continue; }

    		ItemFood food = (ItemFood) item;
    		float amount = Math.max(food.getHealAmount(stack), 1F) * Math.max(food.getSaturationModifier(stack), 0.1F) * stack.getCount() * 0.2F;
    		xp += (int) (Math.max(1, amount));
    	}

    	EntityXPOrb entity = new EntityXPOrb(world, player.posX, player.posY, player.posZ, xp);
    	world.spawnEntity(entity);
    }
}
