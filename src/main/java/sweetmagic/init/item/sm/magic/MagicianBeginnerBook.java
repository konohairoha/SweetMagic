package sweetmagic.init.item.sm.magic;

import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.event.SMSoundEvent;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.sweetmagic.SMItem;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.SMUtil;
import sweetmagic.worldgen.gen.WorldGenHouse;

public class MagicianBeginnerBook extends SMItem {

	public final int data;
	public final static String FACING = "facing";
	public final static String SNEAK = "sneak";
	public final static String X = "posX";
	public final static String Y = "posY";
	public final static String Z = "posZ";
	public String name;

	public MagicianBeginnerBook (String name, int data) {
		super(name, ItemInit.magicList);
		this.data = data;
	}

	// 攻撃スピードなどの追加
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot slot) {
		Multimap<String, AttributeModifier> modMap = super.getItemAttributeModifiers(slot);
		if (slot == EntityEquipmentSlot.MAINHAND) {
			modMap.put(EntityPlayer.REACH_DISTANCE.getName(),
					new AttributeModifier(SMUtil.TOOL_REACH, "Weapon modifier", 12, 0));
		}
		return modMap;
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound tags = ItemHelper.getNBT(stack);

		if (this.isSneak(tags)) {
			player.setActiveHand(hand);
			BlockPos pos = new BlockPos(tags.getInteger(X), tags.getInteger(Y), tags.getInteger(Z));

			if (world instanceof WorldServer && !player.isPotionActive(PotionInit.breakblock)) {
				this.genHouse(world, pos, tags);
			}
			if (!player.capabilities.isCreativeMode) { stack.shrink(1); }
			return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
		}

		return super.onItemRightClick(world, player, hand);
	}

	@Override
	public EnumActionResult useStack (World world, EntityPlayer player, ItemStack stack, BlockPos pos, EnumFacing face) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);

		if (player.isSneaking()) {
			this.sneakAction(pos, tags);
			return EnumActionResult.SUCCESS;
		}

		if (tags.hasKey(SNEAK) && tags.getBoolean(SNEAK)) {
			pos = new BlockPos(tags.getInteger(X), tags.getInteger(Y) + 1, tags.getInteger(Z));
		}

		if (!player.canPlayerEdit(pos.offset(face), face, stack)) { return EnumActionResult.FAIL; }
		if (face != EnumFacing.UP || !world.isAirBlock(pos.up()) || world.isRemote) { return EnumActionResult.FAIL; }

		IBlockState state = world.getBlockState(pos);
		if (state.getBlockHardness(world, pos) <= 0.0D) { return EnumActionResult.FAIL; }

		if (!player.capabilities.isCreativeMode) { stack.shrink(1); }

		if (world instanceof WorldServer && !player.isPotionActive(PotionInit.breakblock)) {
			this.genHouse(world, pos, tags);
		}

		return EnumActionResult.SUCCESS;
	}

	public void sneakAction (BlockPos pos, NBTTagCompound tags) {

		// NBTがないなら
		if (!tags.hasKey(SNEAK)) {
			tags.setBoolean(SNEAK, true);
			tags.setInteger(X, pos.getX());
			tags.setInteger(Y, pos.getY());
			tags.setInteger(Z, pos.getZ());
		}

		// NBTがあるなら
		else {

			// フラグをfalseに
			if (tags.getBoolean(SNEAK)) {
				tags.setBoolean(SNEAK, false);
			}

			// フラグをtrueにして座標登録
			else {
				tags.setBoolean(SNEAK, true);
				tags.setInteger(X, pos.getX());
				tags.setInteger(Y, pos.getY());
				tags.setInteger(Z, pos.getZ());
			}
		}
	}

	public boolean isSneak (NBTTagCompound tags) {
		return tags.hasKey(SNEAK) && tags.getBoolean(SNEAK);
	}

	public void genHouse (World world, BlockPos pos, NBTTagCompound tags) {

		BlockPos startPos = pos.up();
		BlockPos endPos = pos;
		EnumFacing fa = EnumFacing.WEST;

		switch (tags.getInteger(FACING)) {
		case 0:
			fa = EnumFacing.SOUTH;
			endPos = pos.add(13, 10, 13);
			break;
		case 90:
			fa = EnumFacing.EAST;
			endPos = pos.add(13, 10, -13);
			break;
		case 180:
			fa = EnumFacing.NORTH;
			endPos = pos.add(-13, 10, -13);
			break;
		case 270:
			fa = EnumFacing.WEST;
			endPos = pos.add(-13, 10, 13);
			break;
		}

		for (BlockPos p : BlockPos.getAllInBox(startPos, endPos)) {
			if (!world.isAirBlock(p)) {
				this.breakBlock(world, p);
			}
		}

        WorldGenerator gen = new WorldGenHouse("house", fa);
		gen.generate(world, world.rand, pos);

		world.playSound(null, pos, SMSoundEvent.HORAMAGIC, SoundCategory.NEUTRAL, 0.5F, 1F);
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos) {

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
		if (block.isAir(state, world, pos)) { return false; }

		world.playEvent(2001, pos, Block.getStateId(state));
		block.dropBlockAsItem(world, pos, state, 0);
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

	public RayTraceResult getHitBlock(EntityPlayer player) {
		return this.rayTrace(player.getEntityWorld(), player, player.isSneaking());
	}

	//ツールチップの表示
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);

		// 向きのNBTがないなら
		if (!tags.hasKey(FACING)) {
			tags.setInteger(FACING, 0);
		}

		if (!tags.hasKey(SNEAK)) {
			tags.setBoolean(SNEAK, false);
		}

		String face = "";

		switch (tags.getInteger(FACING)) {
		case 0:
			face = EnumFacing.NORTH.toString();
			break;
		case 90:
			face = EnumFacing.WEST.toString();
			break;
		case 180:
			face = EnumFacing.SOUTH.toString();
			break;
		case 270:
			face = EnumFacing.EAST.toString();
			break;
		}

  		String tipFace = new TextComponentTranslation("tip.sm_face.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tipFace + " : " + face));

  		String tipFixe = new TextComponentTranslation("tip.sm_fixed.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN + tipFixe + " : " + tags.getBoolean(SNEAK)));

		// シフトを押したとき
		if (Keyboard.isKeyDown(42)) {

	  		String tipLeft = new TextComponentTranslation("tip.sm_mbleftclick.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GOLD + tipLeft));

	  		String tipSneak = new TextComponentTranslation("tip.sm_sneakclick.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GOLD + tipSneak));

	  		String tipRight = new TextComponentTranslation("tip.sm_rightclick.name", new Object[0]).getFormattedText();
			tooltip.add(I18n.format(TextFormatting.GOLD + tipRight));
		}

		else {
			tooltip.add(I18n.format(TextFormatting.RED + new TextComponentTranslation("tip.shift.name", new Object[0]).getFormattedText()));
		}
	}
}
