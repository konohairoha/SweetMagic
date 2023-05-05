package sweetmagic.init.item.sm.magic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IElementItem;
import sweetmagic.init.BlockInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.blocks.SMSapling;
import sweetmagic.init.block.crop.BlockCornFlower;
import sweetmagic.init.entity.projectile.EntityMagicItem;
import sweetmagic.init.item.sm.eitem.SMElement;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.util.ParticleHelper;
import sweetmagic.worldgen.gen.WorldGenPrsmTree;

public class MagicMeal extends SMItem implements IElementItem {

	private SMElement ele;

	public MagicMeal(String name) {
		super(name, ItemInit.magicList);
		this.setElement(SMElement.BLAST);
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		Random rand = world.rand;

		if (!world.isRemote) {

			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();

			// 地面が土だった時の処理
			if (block instanceof BlockGrass || block instanceof BlockDirt) {

				//設置するブロックの内容（追加も可能）
				Block bloS[] = new Block[] {
					BlockInit.moonblossom_plant, BlockInit.sugarbell_plant, BlockInit.sannyflower_plant
				};

				this.dirtAction(world, rand, block, pos, player, stack, bloS, 0);
			}

			// プリズム、バナナ以外の苗木なら
			else if (block instanceof SMSapling && block != BlockInit.prism_sapling&& block != BlockInit.banana_sapling) {
				this.shrinkItem(player, stack);
				ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				SMSapling sap = (SMSapling) block;
				WorldGenerator gen = new WorldGenPrsmTree(sap.getLog(), sap.getLeave(), false);
		    	gen.generate(world, rand, pos);
			}

			// スイマジの花なら
			else if (block instanceof BlockCornFlower || block instanceof BlockFlower) {
				this.shrinkItem(player, stack);
				ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.VILLAGER_HAPPY);
				world.spawnEntity(new EntityMagicItem(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, new ItemStack(block, rand.nextInt(7) + 4, block.getMetaFromState(state))));
			}
		}

        this.playSound(world, player, SoundEvents.ENTITY_FIREWORK_BLAST_FAR, 0.8F, 1F / (rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);

		return EnumActionResult.SUCCESS;
	}

	// 地面が土だった時の処理
	public void dirtAction (World world, Random rand, Block block, BlockPos pos, EntityPlayer player, ItemStack stack, Block[] bloS, int count) {

		this.shrinkItem(player, stack);

		//渡された座標から再開
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-1, -1, -1), pos.add(1, 1, 1))) {

			if (count >= 3) { return; }

			Block checkAir = world.getBlockState(p).getBlock();
			int setRand = rand.nextInt(2);
			if (setRand != 0 || checkAir != Blocks.AIR) { continue; }

			Block checkDirt = world.getBlockState(p.down()).getBlock();
			if (checkDirt instanceof BlockGrass || checkDirt instanceof BlockDirt) {
				count++;
				world.setBlockState(p, bloS[rand.nextInt(bloS.length)].getDefaultState(), 2);
			}
		}
	}

	@Override
	public SMElement getElement() {
		return this.ele;
	}

	@Override
	public void setElement(SMElement ele) {
		this.ele = ele;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magicmeal.name")));
		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip("tip.magicmeal_flower.name")));
	}
}
