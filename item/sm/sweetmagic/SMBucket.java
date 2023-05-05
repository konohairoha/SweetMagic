package sweetmagic.init.item.sm.sweetmagic;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.FluidBucketHandler;
import sweetmagic.init.ItemInit;
import sweetmagic.util.ItemHelper;

public class SMBucket extends SMItem {

	public static final String AMOUNT = "amount";
	private static final IBlockState AIR = Blocks.AIR.getDefaultState();

	public SMBucket(String name) {
		super(name);
		this.setMaxStackSize(1);
	}

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    //クラフトしても帰ってくるように
    @Override
    public ItemStack getContainerItem(ItemStack stack) {

    	NBTTagCompound tags = ItemHelper.getNBT(stack);
    	Item item = stack.getItem();

    	if (item != ItemInit.alt_bucket_water) {
    		return super.getContainerItem(stack);
    	}

    	else if (this.getAmount(tags) <= 1) {
    		return new ItemStack(ItemInit.alt_bucket);
    	}

    	this.shrinkAmount(tags);
    	ItemStack copy = stack.copy();
    	copy.setTagCompound(tags);

        return copy;
    }

	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {

        ItemStack stack = player.getHeldItem(hand);
        RayTraceResult result = this.rayTrace(world, player, true);
        ActionResult<ItemStack> ret = ForgeEventFactory.onBucketUse(player, world, stack, result);
        if (ret != null) { return ret; }

        if (result == null) {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
        }

        else if (result.typeOfHit != RayTraceResult.Type.BLOCK) {
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
    			for (BlockPos blockPos : BlockPos.getAllInBox(pos.add(-1, -2, -1), pos.add(1, 0, 1))) {

        			IBlockState state2 = world.getBlockState(blockPos);
                    Material material2 = state2.getMaterial();
        			if (material2 == Material.WATER && ((Integer)state2.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
        				count++;
        				world.setBlockState(blockPos, AIR, 3);
        			}
    			}

                player.addStat(StatList.getObjectUseStats(this));
                player.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);

                // 無印バケツ
            	if (stack.getItem() == ItemInit.alt_bucket) {
            		ItemStack stack2 = this.fillBucket(stack, player, ItemInit.alt_bucket_water);
            		this.setAmout(this.setTag(stack2, stack2.getTagCompound()), count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack2);
            	}

                // 水バケツ
        		NBTTagCompound tags = this.setTag(stack, stack.getTagCompound());
        		this.setAmout(tags, this.getAmount(tags) + count);
                return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        	}
        }

        // マグマに右クリックしたとき
    	else if (material == Material.LAVA && ((Integer) state.getValue(BlockLiquid.LEVEL)).intValue() == 0) {

        	if (stack.getItem() == ItemInit.alt_bucket_lava || stack.getItem() == ItemInit.alt_bucket) {

        		// 範囲回収
    			for (BlockPos blockPos : BlockPos.getAllInBox(pos.add(-2, -4, -2), pos.add(2, 0, 2))) {

        			IBlockState state2 = world.getBlockState(blockPos);
                    Material material2 = state2.getMaterial();
        			if (material2 == Material.LAVA && ((Integer) state2.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
        				count++;
        				world.setBlockState(blockPos, AIR, 3);
        			}
    			}

                player.addStat(StatList.getObjectUseStats(this));
                player.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);

            	// 無印バケツ
            	if (stack.getItem() == ItemInit.alt_bucket) {
            		ItemStack stack2 = this.fillBucket(stack, player, ItemInit.alt_bucket_lava);
            		this.setAmout(this.setTag(stack2, stack.getTagCompound()), count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack2);
            	}

                // マグマバケツ
            	else {
            		NBTTagCompound tags = this.setTag(stack, stack.getTagCompound());
            		this.setAmout(tags, this.getAmount(tags) + count);
                    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
            	}
        	}
        }

        // それ以外のとき
    	else {
        	boolean flag1 = world.getBlockState(pos).getBlock().isReplaceable(world, pos);
            BlockPos pos1 = flag1 && result.sideHit == EnumFacing.UP ? pos : pos.offset(result.sideHit);

            if (!player.canPlayerEdit(pos1, result.sideHit, stack)) {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
            }

            else if (this.tryPlaceContainedLiquid(player, world, pos1, stack)) {
				if (player instanceof EntityPlayerMP) {
					CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos1, stack);
                }

                player.addStat(StatList.getObjectUseStats(this));
                NBTTagCompound tags = stack.getTagCompound();

				if (tags != null) {
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.isAmountEmpty(tags) ? new ItemStack(ItemInit.alt_bucket) : stack);
				}
            }
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
        }
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, stack);
    }

	// バケツ入れ替え処理
	public ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {

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
        if (stack.getItem() == ItemInit.alt_bucket_water && this.getAmount(tags) >= 1) {
            this.playSound(world, player, SoundEvents.ITEM_BUCKET_EMPTY, 1F, this.itemRand.nextFloat() * 0.4F + 0.8F);
            world.setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState(), 11);
    		this.shrinkAmount(tags);
        }

        // マグマバケツ設置処理
        else if (stack.getItem() == ItemInit.alt_bucket_lava && this.getAmount(tags) >= 1) {

            int l = pos.getX();
            int i = pos.getY();
            int j = pos.getZ();

        	for (int k = 0; k < 8; ++k) {
                world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, l + Math.random(), i + Math.random(), j + Math.random(), 0D, 0D, 0D);
            }

            world.setBlockState(pos, Blocks.FLOWING_LAVA.getDefaultState(), 11);
    		this.shrinkAmount(tags);
    		Random rand = world.rand;
            this.playSound(world, player, SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 2.6F + (rand.nextFloat() - rand.nextFloat()) * 0.8F);
        }
        return true;
    }

	// NBTを設定
	public NBTTagCompound setTag (ItemStack stack, NBTTagCompound tags) {

		if (tags == null) {
    		tags = ItemHelper.getNBT(stack);
			this.setAmout(tags, 0);
		}

		if (!tags.hasKey(AMOUNT) || this.isAmountEmpty(tags)) {
			this.setAmout(tags, 0);
		}

		return tags;
	}

	// 水量の取得
	public int getAmount (NBTTagCompound tags) {
		return tags.hasKey(AMOUNT) ? tags.getInteger(AMOUNT) : 0;
	}

	// 水量の設定
	public void setAmout (NBTTagCompound tags, int amount) {
		tags.setInteger(AMOUNT, Math.max(amount, 0));
	}

	// 1バケツ分減らす
	public void shrinkAmount (NBTTagCompound tags) {
		this.setAmout(tags, this.getAmount(tags) - 1);
	}

	// 水量が空かどうか
	public boolean isAmountEmpty (NBTTagCompound tags) {
		return this.getAmount(tags) <= 0;
	}

	// バケツの使用
	public ItemStack useBucket (ItemStack stack) {

		NBTTagCompound tags = ItemHelper.getNBT(stack);
		this.shrinkAmount(tags);

		if (this.isAmountEmpty(tags)) {
			stack = new ItemStack(ItemInit.alt_bucket);
			stack.setTagCompound(tags);
		}

		return stack;
	}

	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
		if (this.getClass() == SMBucket.class) {
			return new FluidBucketHandler(stack, FluidRegistry.WATER);
		}
		return super.initCapabilities(stack, nbt);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {

		World world = player.world;
		if ( !(entity instanceof EntityCow) ) { return false; }

		Item item = stack.getItem();
		if (item != ItemInit.alt_bucket) { return false; }

        this.playSound(world, player, SoundEvents.ENTITY_COW_MILK, 1F, 1F);

		if (!world.isRemote) {
			ItemStack milkPack = new ItemStack(ItemInit.milk_pack, 8);
			world.spawnEntity(new EntityItem(world, player.posX, player.posY + 0.5D, player.posZ, milkPack));
		}

		return true;
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		NBTTagCompound tags = stack.getTagCompound();
		tooltip.add(I18n.format( (tags == null ? 0 : String.format("%,d", (long) this.getAmount(tags) * 1000L)) + (TextFormatting.GREEN + "mB")));
	}
}
