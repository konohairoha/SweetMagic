package sweetmagic.init.item.sm.magic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.api.iitem.IMFTool;
import sweetmagic.api.iitem.IRangePosTool;
import sweetmagic.init.BlockInit;
import sweetmagic.init.EnchantInit;
import sweetmagic.init.ItemInit;
import sweetmagic.init.PotionInit;
import sweetmagic.init.item.sm.sweetmagic.SMPick;
import sweetmagic.key.ClientKeyHelper;
import sweetmagic.key.SMKeybind;
import sweetmagic.util.ItemHelper;
import sweetmagic.util.WorldHelper;

public class AetherHammer extends SMPick implements IMFTool, IRangePosTool {

	private int maxMF;
	private static final int USERMF = 50;

	public AetherHammer (String name, ToolMaterial material) {
		super(name, material);
		this.setMaxMF(100000);
		this.setMaxStackSize(1);
		this.setMaxDamage(10000);
        ItemInit.itemList.remove(this);
        ItemInit.magicList.add(this);
	}

	//右クリックをした際の処理
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

		// 破壊無効がついていたら設置できない
		ItemStack stack = player.getHeldItem(hand);
		ActionResult<ItemStack> action = new ActionResult(EnumActionResult.PASS, stack);
		int mf = this.getMF(stack);
		boolean isCreateive = player.isCreative();

		int costMF = USERMF - Math.min(9, this.getEnchantLevel(EnchantInit.mfCostDown, stack)) * 5;
		if ( ( player.isPotionActive(PotionInit.breakblock) || mf < costMF ) && !isCreateive) { return action; }

		NBTTagCompound tags = ItemHelper.getNBT(stack);
		if (!tags.hasKey(STARTX)) { return action; }

		if (!isCreateive) {

			int xRange = Math.abs(tags.getInteger(ENDX) - tags.getInteger(STARTX)) + 1;
			int yRange = Math.abs(tags.getInteger(ENDY) - tags.getInteger(STARTY)) + 1;
			int zRange = Math.abs(tags.getInteger(ENDZ) - tags.getInteger(STARTZ)) + 1;

			if ((xRange * yRange * zRange) > 1024) {

				if (!world.isRemote) {
					player.sendMessage(new TextComponentTranslation("tip.aetherhammer_overrange.name"));
				}

				return action;
			}
		}

		int needMF = 0;

		Block bAir = Blocks.AIR;
		IBlockState AIR = bAir.getDefaultState();

		//リストの作成（めっちゃ大事）
		List<ItemStack> drop = new ArrayList<>();
		Iterable<BlockPos> posList = BlockPos.getAllInBox(this.getStartPos(tags), this.getEndPos(tags));

		for (BlockPos pos : posList) {

			//空気ブロックとたいるえんちちーなら何もしない、破壊可能なブロックかどうか
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			if (block == bAir || block.hasTileEntity(state) || this.canBreakBlock(block, state.getMaterial())) { continue; }

			drop.addAll(WorldHelper.getBlockDrops(world, player, state, block, pos, false, 0));
			world.setBlockState(pos, AIR, 3);

			// 必要MFを加算して足りなくなったら終了
			needMF += costMF;
			if (mf <= needMF && !isCreateive) { continue; }
		}

		// MF消費
		if (!isCreateive) {
			this.setMF(stack, mf - needMF);
		}

		//リストに入れたアイテムをドロップさせる
		if (!world.isRemote) {
			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entity) {

		if (state.getBlockHardness(world, pos) > 0D && !world.isRemote) {

			EntityPlayer player = (EntityPlayer) entity;
			EnumFacing sideHit = this.rayTrace(world, player, false).sideHit;
			int xa = 0, za = 0;	//向きに合わせて座標を変えるための変数

			int area = 1;
			int silk = this.getEnchant(Enchantments.SILK_TOUCH, stack);
			boolean canSilk = silk > 0;

			int fortune = this.getEnchant(Enchantments.FORTUNE, stack);
			Block bAir = Blocks.AIR;
			IBlockState AIR = bAir.getDefaultState();

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
    		Iterable<BlockPos> posList = BlockPos.getAllInBox(pos.add(-area + xa, 0, -area + za), pos.add(area + xa, area * 2, area + za));

    		for (BlockPos p : posList) {

				//空気ブロックとたいるえんちちーなら何もしない
				IBlockState target = world.getBlockState(p);
				Block block = target.getBlock();
				if (block == bAir || block.hasTileEntity(target)) { continue; }

				drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p, canSilk, fortune));
				world.setBlockState(p, AIR, 3);
    		}

			//リストに入れたアイテムをドロップさせる
    		if (!world.isRemote) {
    			WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
    		}

			stack.damageItem(3, player);

		}
		return true;
	}

	// 破壊可能なブロックかどうか
	public boolean canBreakBlock (Block block, Material material) {
		return material == Material.WATER || material == Material.LAVA || block == Blocks.BEDROCK || block == BlockInit.spawn_stone;
	}

	@Override
	public void setMaxMF(int maxMF) {
		this.maxMF = maxMF;
	}

  	// 最大MFを取得
	@Override
  	public int getMaxMF (ItemStack stack) {
		float addMaxMF = (this.getEnchantLevel(EnchantInit.maxMFUP, stack) * 7.5F) * (this.maxMF / 100F);
  		return (int) (this.maxMF + addMaxMF);
  	}

	// エンチャレベル取得
	public int getEnchantLevel (Enchantment enchant, ItemStack stack) {
		return Math.min(EnchantmentHelper.getEnchantmentLevel(enchant, stack), 10);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return this.getMF(stack) != this.getMaxMF(stack);
	}

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1D - ( (double) this.getMF(stack) / (double) this.getMaxMF(stack) );
	}

	// 特定のアイテムで修復可能に
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return false;
    }

	//アイテムにダメージを与える処理を無効
	@Override
	public void setDamage(ItemStack stack, int damage) { }

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.starlightwand_left.name")));
		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.aetherhammer_right.name")));
		tooltip.add(I18n.format(this.getTip(TextFormatting.GOLD, "tip.aetherhammer_range.name")));

		// シフトを押したとき
		if (Keyboard.isKeyDown(42)) {
			NBTTagCompound tags = ItemHelper.getNBT(stack);
			BlockPos startPos = this.getStartPos(tags);
			BlockPos endPos = this.getEndPos(tags);
			String start = tags.hasKey(STARTX) ? startPos.getX() + ", " + startPos.getY() + ", " + startPos.getZ() : this.getTip(TextFormatting.RED, "tip.starlightwand_empty.name");
			String end = tags.hasKey(ENDX) ? endPos.getX() + ", " + endPos.getY() + ", " + endPos.getZ() : this.getTip(TextFormatting.RED, "tip.starlightwand_empty.name");

			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.sl_reset.name") + TextFormatting.GOLD + ClientKeyHelper.getKeyName(SMKeybind.NEXT)));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_start.name") + start));
			tooltip.add(I18n.format(this.getTip(TextFormatting.GREEN, "tip.starlightwand_end.name") + end));
		}

		else {
			tooltip.add(I18n.format(this.getTip(TextFormatting.RED, "tip.shift.name")));
		}
	}

	public String getTip (TextFormatting type, String tip) {
		return type + new TextComponentTranslation(tip).getFormattedText();
	}
}
