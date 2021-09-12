package sweetmagic.init.item.sm.sweetmagic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Multimap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockStem;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
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
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.init.ItemInit;
import sweetmagic.init.block.crop.icrop.ISMCrop;
import sweetmagic.util.ItemHelper;
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

		if (world.isRemote) { return true; }

		EntityPlayer player = (EntityPlayer) living;
		List<ItemStack> drop = new ArrayList<>();
		int area = 2;

		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, area, area))) {

			IBlockState target = world.getBlockState(p);
			Block block = target.getBlock();

			//空気ブロックとたいるえんちちーなら何もしない
			if (block == Blocks.AIR) { continue; }

			if (this.getDestroySpeed(stack, target) >= 5.0F && !(block instanceof BlockStem)) {

				boolean flag = true;
				if (block instanceof IGrowable) {
					flag = ((IGrowable) block).canGrow(world, p, target, false);
				}

				if (block instanceof IShearable) {
					flag = !((IShearable) block).isShearable(stack, world, p);
				}

				if (block == Blocks.PUMPKIN || block == Blocks.MELON_BLOCK) {
					flag = false;
				}

				if (block == Blocks.REEDS) {
					Block under = world.getBlockState(p.down()).getBlock();
					if (under == Blocks.REEDS) {
						flag = false;
					}
				}

				if (!flag) {

					//リストに入れる
					drop.addAll(WorldHelper.getBlockDrops(world, player, target, block, p, false, 0));
					world.destroyBlock(p, false);
				}
			}
		}

		WorldHelper.createLootDrop(drop, world, player.posX, player.posY, player.posZ);
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
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing face, float x, float y, float z) {

		if (world.isRemote) {
			return EnumActionResult.SUCCESS;
		}

		this.getPickPlant(world, player, pos, player.getHeldItem(hand));
		return EnumActionResult.SUCCESS;
	}

	public void getPickPlant (World world, EntityPlayer player, BlockPos pos, ItemStack stack) {

		// blockの取得
		IBlockState st = world.getBlockState(pos);
		Block block = st.getBlock();
		Random rand = world.rand;

		// 作物以外なら終了
		if (!(block instanceof IGrowable)) { return; }

		// 範囲とstackListの初期化
		int area = 2 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
		List<ItemStack> stackList = new ArrayList();

		// 範囲分回す
		for (BlockPos p : BlockPos.getAllInBox(pos.add(-area, 0, -area), pos.add(area, area, area))) {

			Item item = null;
			IBlockState plant = null;
			IBlockState state = world.getBlockState(p);
			Block b = state.getBlock();
			List<ItemStack> dropList = new ArrayList<>();

			// まずはスイマジ作物なら右クリック処理を呼び出し
			if (b instanceof ISMCrop) {
				if (!((IGrowable) b).canGrow(world, p, state, false)) {
					((ISMCrop) b).getPickPlant(world, player, p, stack);
				}
			}

			// 通常の作物なら
			else if (b instanceof IGrowable) {
				if (!((IGrowable) b).canGrow(world, p, state, false)) {
					dropList = b.getDrops(world, p, state, 0);
					ItemHelper.compactItemListNoStacksize(dropList);
				}
			}

			// リストが空なら終了
			if (dropList.isEmpty()) { continue; }

			// 作物の種の取得
			if (b instanceof BlockBush) {
				item = b.getDrops(world, p, b.getDefaultState(), 0).get(0).getItem();
				System.out.println("=====item:" + item);
			}

			System.out.println("=====dropList:" + dropList);

			// ドロップリスト分回す
			for (ItemStack drop : dropList) {

				Item dropItem = drop.getItem();

				// 取得した種を植える
				if (plant == null && item != null && item == dropItem && dropItem instanceof IPlantable) {
					drop.shrink(1);
					plant = ((IPlantable) item).getPlant(world, p);
				}
			}

			ItemHelper.compactItemListNoStacksize(dropList);
			System.out.println("=====dropList2:" + dropList);

			// stackListに追加
			stackList.addAll(dropList);
			this.breakBlock(world, p);
			this.playCropSound(world, rand, p);

			if (plant != null) {
				world.setBlockState(p, plant, 2);
			}
		}

		// Listが空なら終了
		if (stackList.isEmpty()) { return; }

		// リスト分スポーン
		for (ItemStack s : stackList) {
			world.spawnEntity(new EntityItem(world, player.posX + 0.5D, player.posY, player.posZ + 0.5D, s));
		}
	}

	// ブロック破壊処理
	public boolean breakBlock(World world, BlockPos pos) {
		world.playEvent(2001, pos, Block.getStateId(world.getBlockState(pos)));
        return world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
    }

	// 作物回収時の音
	public void playCropSound (World world, Random rand, BlockPos pos) {
        world.playSound(null, pos,SoundEvents.BLOCK_GRASS_PLACE, SoundCategory.PLAYERS, 0.5F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + 1 * 0.5F);
	}

	// ツールチップの表示
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {
		String tip = new TextComponentTranslation("tip.alt_sickle.name", new Object[0]).getFormattedText();
		tooltip.add(I18n.format(TextFormatting.GREEN  + tip));
	}
}
