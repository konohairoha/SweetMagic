package sweetmagic.init.item.sm.sweetmagic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.util.WorldHelper;

public class SMShovel extends ItemSpade {

	private static final List<Material> materialList = Arrays.<Material> asList(
			Material.GROUND, Material.GRASS, Material.SAND
	);

	public SMShovel(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		ItemInit.itemList.add(this);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (state.getBlockHardness(world, pos) > 0D && !world.isRemote && !living.isSneaking()) {

			EntityPlayer player = (EntityPlayer) living;
			EnumFacing sideHit = this.rayTrace(world, player, false).sideHit;
			int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数
			int area = 1;
			boolean canSilk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
			int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

			//上と下以外は採掘する座標を変える
            switch (sideHit) {
                case UP:
                case DOWN:
                	break;
                case NORTH:
                	za = area;
                	break;
                case SOUTH:
                	za = -area;
                	break;
                case EAST:
                	xa = -area;
                	break;
                case WEST:
                	xa = area;
                	break;
            }

    		//リストの作成（めっちゃ大事）
    		List<ItemStack> drop = new ArrayList<>();

			for (BlockPos blockPos : BlockPos.getAllInBox(pos.add(-area + xa, 0, -area + za), pos.add(area + xa, area * 2, area + za))) {

				IBlockState target = world.getBlockState(blockPos);
				Block block = target.getBlock();

				//空気ブロックとたいるえんちちーなら何もしない
				if(block == Blocks.AIR || block.hasTileEntity(target)){ continue; }
				if (!materialList.contains(target.getMaterial())) { continue; }

				drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, blockPos, canSilk, FOURTUNE));
				world.setBlockToAir(blockPos);
			}

			//リストに入れたアイテムをドロップさせる
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
			stack.damageItem(1, living);
		}

		return true;
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_shovel.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN  + tip));
	}
}