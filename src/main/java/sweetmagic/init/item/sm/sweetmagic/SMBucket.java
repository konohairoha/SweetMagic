package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;

public class SMBucket extends SMItem {

	public SMBucket(String name) {
		super(name);
		this.setMaxStackSize(1);
	}

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult result = this.rayTrace(world, player, true);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, result);

        if (ret != null) { return ret; }

        if (result == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        } else if (result.typeOfHit != RayTraceResult.Type.BLOCK) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }

        BlockPos pos = result.getBlockPos();

        if (!world.isBlockModifiable(player, pos)) { return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack); }
        if (!player.canPlayerEdit(pos.offset(result.sideHit), result.sideHit, stack)) { return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack); }

        IBlockState state = world.getBlockState(pos);
        Material material = state.getMaterial();
    	int count = 0;

        // 水に右クリックしたとき
    	if (material == Material.WATER && ((Integer)state.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
        	if (stack.getItem() == ItemInit.alt_bucket_water || stack.getItem() == ItemInit.alt_bucket) {

        		// 範囲回収
            	for(int x = -1; x <= 1; x++) {
            		for(int z = -1; z <= 1; z++) {
                		for(int y = -2; y <= 0; y++) {

                			IBlockState state2 = world.getBlockState(pos.add(x, y, z));
                            Material material2 = state2.getMaterial();
                			if (material2 == Material.WATER && ((Integer)state2.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                				count++;
                				world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 11);
                			}
                		}
                	}
            	}

                player.addStat(StatList.getObjectUseStats(this));
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

                // 無印バケツ
            	if (stack.getItem() == ItemInit.alt_bucket) {

            		ItemStack stack2 = this.fillBucket(stack, player, ItemInit.alt_bucket_water);
                	NBTTagCompound tags = stack2.getTagCompound();
                	stack2.setTagCompound(new NBTTagCompound());
            		tags = stack2.getTagCompound();
            		tags.setInteger("amount", count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack2);
//                    UniversalBucket.class;
            	}

                // 水バケツ
        		NBTTagCompound tags = stack.getTagCompound();
        		this.setTag(stack, tags);
        		tags.setInteger("amount", tags.getInteger("amount") + count);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        	}

        // マグマに右クリックしたとき
        } else if (material == Material.LAVA && ((Integer)state.getValue(BlockLiquid.LEVEL)).intValue() == 0) {

        	if (stack.getItem() == ItemInit.alt_bucket_lava || stack.getItem() == ItemInit.alt_bucket) {
        		for(int x = -2; x <= 2; x++) {
            		for(int z = -2; z <= 2; z++) {
                		for(int y = -4; y <= 0; y++) {

                			IBlockState state2 = world.getBlockState(pos.add(x, y, z));
                            Material material2 = state2.getMaterial();
                			if (material2 == Material.LAVA && ((Integer)state2.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                				count++;
                				world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState(), 11);
                			}
                		}
                	}
            	}

                player.addStat(StatList.getObjectUseStats(this));
                player.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);

            	// 無印バケツ
            	if (stack.getItem() == ItemInit.alt_bucket) {

            		ItemStack stack2 = this.fillBucket(stack, player, ItemInit.alt_bucket_lava);
                	NBTTagCompound tags = stack2.getTagCompound();
                	stack2.setTagCompound(new NBTTagCompound());
            		tags = stack2.getTagCompound();
            		tags.setInteger("amount", count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack2);

                // マグマバケツ
            	} else {
            		NBTTagCompound tags = stack.getTagCompound();
            		this.setTag(stack, tags);
            		tags.setInteger("amount", tags.getInteger("amount") + count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
            	}
        	}

        // それ以外のとき
        } else {
        	boolean flag1 = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
            BlockPos pos1 = flag1 && result.sideHit == EnumFacing.UP ? pos : pos.offset(result.sideHit);

            if (!player.canPlayerEdit(pos1, result.sideHit, stack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
            } else if (this.tryPlaceContainedLiquid(player, world, pos1, stack)) {
                if (player instanceof EntityPlayerMP) {
                    CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos1, stack);
                }

                player.addStat(StatList.getObjectUseStats(this));
                NBTTagCompound tags = stack.getTagCompound();

				if (tags != null) {
					if (tags.getInteger("amount") > 0) {
						return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
					} else {
						return new ActionResult(EnumActionResult.SUCCESS, new ItemStack(ItemInit.alt_bucket));
					}
				}
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

	// NBTを設定
	public void setTag (ItemStack stack, NBTTagCompound tags) {
		if (tags == null) {
			stack.setTagCompound(new NBTTagCompound());
    		tags = stack.getTagCompound();
			tags.setInteger("amount", 0);
		}
	}

	// バケツ入れ替え処理
	private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {

		emptyBuckets.shrink(1);
        if (emptyBuckets.isEmpty()) { return new ItemStack(fullBucket); }

        if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket))) {
            player.dropItem(new ItemStack(fullBucket), false);
        }

        return emptyBuckets;
    }

	// バケツに中身があるときの設置処理
	public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World world, BlockPos pos, ItemStack stack) {

        NBTTagCompound tags = stack.getTagCompound();
        if (tags == null) { return true; }

        // 水バケツ設置処理
        if (stack.getItem() == ItemInit.alt_bucket_water && tags.getInteger("amount") >= 1) {
        	world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1F, itemRand.nextFloat() * 0.4F + 0.8F);
            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 11);
            tags.setInteger("amount", tags.getInteger("amount") - 1);

        // マグマバケツ設置処理
        } else if (stack.getItem() == ItemInit.alt_bucket_lava && tags.getInteger("amount") >= 1) {

            int l = pos.getX();
            int i = pos.getY();
            int j = pos.getZ();

        	for (int k = 0; k < 8; ++k) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, l + Math.random(), i + Math.random(), j + Math.random(), 0D, 0D, 0D);
            }

            world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState(), 11);
            tags.setInteger("amount", tags.getInteger("amount") - 1);
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        }
        return true;
    }

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		NBTTagCompound tags = stack.getTagCompound();
		tooltip.add(I18n.format(TextFormatting.GREEN + this.getTip("tip.bkt.name") + " : " + (tags == null ? 0 : tags.getInteger("amount"))));
	}
}
