package sweetmagic.init.item.sm.sweetmagic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.WorldHelper;

public class SMSickle extends ItemHoe {

	public SMSickle(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
	}

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot){
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", 0.5D, 0));
        }
        return map;
    }

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase living) {

		if (!world.isRemote) {
			EntityPlayer player = (EntityPlayer) living;
			List<ItemStack> drop = new ArrayList<>();
			int area = 2;

			for (int x = -area; x <= area; x++) {
				for (int z = -area; z <= area; z++) {
					for (int y = 0; y <= area; y++) {

						BlockPos p1 = pos.add(x, y, z);
						IBlockState target = world.getBlockState(p1);
						Block block = target.getBlock();

						//空気ブロックとたいるえんちちーなら何もしない
						if (block == Blocks.AIR || block.hasTileEntity(target)) { continue; }

						if (this.getDestroySpeed(stack, target) >= 5.0F && !(block instanceof BlockStem)) {

							boolean flag = true;
							if (block instanceof IGrowable) {
								flag = ((IGrowable) block).canGrow(world, p1, target, false);
							}

							if (block instanceof IShearable) {
								flag = !((IShearable) block).isShearable(stack, world, p1);
							}

							if (block == Blocks.PUMPKIN || block == Blocks.MELON_BLOCK) {
								flag = false;
							}

							if (block == Blocks.REEDS) {
								Block under = world.getBlockState(p1.down()).getBlock();
								if (under == Blocks.REEDS) {
									flag = false;
								}
							}

							if (!flag) {

								//リストに入れる
								drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p1, false, 0));
								//								world.setBlockToAir(p1);
								world.destroyBlock(p1, false);
							}
						}
					}
				}
			}

			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
		}

		stack.damageItem(1, living);
		return true;
	}

	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		Material mate = state.getMaterial();
		return mate != Material.PLANTS && mate != Material.VINE && mate != Material.CORAL && mate != Material.LEAVES && mate != Material.GOURD ? 1F : 15F;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state) {
		return true;
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float par8, float par9, float par10) {

		if (world.isRemote) {
			return EnumActionResult.FAIL;
		}

		this.getPickPlant(world, player, pos, player.getHeldItem(hand));
		return EnumActionResult.SUCCESS;
	}

	public void getPickPlant (World world, EntityPlayer player, BlockPos pos, ItemStack stack) {

		Block block = world.getBlockState(pos).getBlock();
		if (!(block instanceof ISMCrop)) { return; }

		ISMCrop crop = (ISMCrop) block;
		crop.getPickPlant(world, player, pos, stack);
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_sickle.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN  + tip));
	}
}
