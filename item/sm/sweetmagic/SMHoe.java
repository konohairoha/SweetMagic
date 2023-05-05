package sweetmagic.init.item.sm.sweetmagic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.BlockCornFlower;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.WorldHelper;

public class SMHoe extends ItemHoe {

	public SMHoe(String name, ToolMaterial material) {
		super(material);
		setUnlocalizedName(name);
        setRegistryName(name);
        ItemInit.itemList.add(this);
	}

	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot){
        Multimap<String, AttributeModifier> map = super.getItemAttributeModifiers(slot);
        if (slot == EntityEquipmentSlot.MAINHAND) {
            map.removeAll(SharedMonsterAttributes.ATTACK_SPEED.getName());
            map.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4D, 0));
        }
        return map;
    }

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		ItemStack stack = player.getHeldItem(hand);

		int area = 16;
		boolean isSneak = player.isSneaking();
		boolean canSilk = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0;
		int FOURTUNE = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);

		//リストの作成（めっちゃ大事）
		List<ItemStack> drop = new ArrayList<>();

		for (BlockPos blockPos : BlockPos.getAllInBox(pos.add(-area, -5, -area), pos.add(area, 5, area))) {

			IBlockState target = world.getBlockState(blockPos);
			Block block = target.getBlock();

			//空気ブロックとたいるえんちちーなら何もしない
			if(block == Blocks.AIR || block.hasTileEntity(target)){ continue; }

			if (!isSneak) {
				if (!(block instanceof BlockTallGrass) && !(block instanceof BlockDoublePlant) && !(block instanceof BlockFlower) && !(block instanceof BlockCornFlower)) { continue; }
			}

			else {
				if (!(block instanceof BlockTallGrass) && !(block instanceof BlockDoublePlant)) { continue; }
			}

			drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, blockPos, canSilk, FOURTUNE));
			world.setBlockToAir(blockPos);
		}

		// リストに入れたアイテムをドロップさせる
		if (!world.isRemote) {
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
		}

		stack.damageItem(1, player);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {

		Block block = state.getBlock();

		if (entity.isPotionActive(MobEffects.LUCK) && entity instanceof EntityPlayer && block instanceof ISMCrop && !world.isRemote) {
			ISMCrop crop = (ISMCrop) block;
			EntityPlayer player = (EntityPlayer) entity;
			EntityItem item = crop.getDropItem(world, player, stack, crop.getDropItem(), crop.getFoutuneValue(player));
			world.spawnEntity(item);
		}

		return super.onBlockDestroyed(stack, world, state, pos, entity);
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag advanced) {
		List<String> toolTipBr = Arrays.<String>asList(new TextComponentTranslation("tip.alt_hoe.name").getFormattedText().split("<br>"));

		for (String tip : toolTipBr) {
			tooltip.add(I18n.format(TextFormatting.GREEN + tip));
		}
	}
}
