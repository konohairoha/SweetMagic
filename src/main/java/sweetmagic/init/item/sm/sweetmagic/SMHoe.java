package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;

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
		String tip = new TextComponentTranslation("tip.alt_hoe.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN  + tip));
	}
}
