package sweetmagic.init.block.magic;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.handlers.PacketHandler;
import sweetmagic.init.BlockInit;
import sweetmagic.init.base.BaseMFBlock;
import sweetmagic.init.item.sm.sweetmagic.SMFood;
import sweetmagic.init.tile.magic.TileMFPot;
import sweetmagic.packet.PlayerSoundPKT;
import sweetmagic.util.EnchantUtil;
import sweetmagic.util.ParticleHelper;
import sweetmagic.util.PlayerHelper;
import sweetmagic.util.SoundHelper;

public class MFPot extends BaseMFBlock {

	public final int data;
    private Map<Enchantment, Integer> initEnchant;
    private final int expCost = 1000;
    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.4D, 0D, 0.4D, 0.6D, 0.5D, 0.6D);

    public MFPot(String name, int data) {
        super(name);
        this.data = data;
		BlockInit.magicList.add(this);
    }

    /**
     * 0 = ドリズリィの花瓶（雨MF生産）
     * 1 = 黄昏時の夢百合草（夕方MF生産）
     * 2 = スノードロップの花瓶（雪地MF生産）
     * 3 = トルコキキョウの花瓶（経験値吸収）
     * 4 = 群青の薔薇の花瓶（エンチャントはがし）
     * 5 = ソリッド・スターの花瓶（周囲のエンチャパワー回収）
     * 6 = ジニアの花瓶（光源でMF変換）
     * 7 = ハイドラシア（敵モブ倒してMF生産）
     * 8 = カーネーション（食べ物をMF変換）
     * 9 = クリスマスローズエリックスミシィ（雪レイヤーをMF変換）
     *
     */

	// ブロックでのアクション
	@Override
	public void actionBlock (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		if (world.isRemote) { return; }

		// エンチャントはがしてMFに変換
		if (this.data == 4 && EnchantmentHelper.getEnchantments(stack).size() > 0 && this.canMFChange(world, pos)) {
			this.removeEnchant(world, pos, player, stack);
		}

		// 経験値をMFに変換
		else if (this.data == 3 && PlayerHelper.getExpValue(player) >= this.expCost && this.canMFChange(world, pos)) {
			this.removeExp(world, pos, player);
		}

		else if (this.data == 8 && stack.getItem() instanceof ItemFood && this.canMFChange(world, pos)) {
			this.removeFood(world, pos, player, stack);
		}

		// それ以外
		else {
			TileMFPot tile = (TileMFPot) world.getTileEntity(pos);
			TextComponentTranslation tip = new TextComponentTranslation("tip.mf_amount.name");
			player.sendMessage(tip.appendText(String.format("%,d", tile.getMF())));
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public TileEntity createTileEntity(@Nonnull World world, @Nonnull IBlockState state) {
		return new TileMFPot();
	}

	// エンチャMF変換
	public void removeEnchant (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		// マップ（エンチャの種類、レベル）の取得
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
        int cost = 0;

		// エンチャの数だけ回す
		for (Entry<Enchantment, Integer> entry : map.entrySet()) {

			// エンチャを取得してコストを追加
			Enchantment enchant = entry.getKey();
            final int level = entry.getValue() ;

            if (level > 0) {

                float rate = 12F - enchant.getRarity().getWeight();

                if (enchant.getMaxLevel() == 1 && enchant.getRarity().getWeight() <= 1) {
                	rate *= 3F;
                }

                cost += EnchantUtil.calculateNewEnchCost(enchant, level) * rate;
            }
		}

		// コスト分をMFに変換
		if (cost > 0) {

			TileMFPot tile = (TileMFPot) world.getTileEntity(pos);
			tile.setMF(tile.getMF() + cost);
			tile.sentClient();

			if (stack.getItem() == Items.ENCHANTED_BOOK) {
				stack.shrink(1);
				world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, new ItemStack(Items.BOOK)));
			}

			// エンチャを外す
			else {
				stack.getTagCompound().removeTag("ench");
			}

			// クライアント（プレイヤー）へ送りつける
			if (player instanceof EntityPlayerMP) {
				PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_ENCHREMO, 1F, 0.25F), (EntityPlayerMP) player);
			}
		}
	}

	// 経験値をMFに変換
	public void removeExp (World world, BlockPos pos, EntityPlayer player) {

		int expValue = this.expCost * 12;
		PlayerHelper.addExp(player, -this.expCost);

		TileMFPot tile = (TileMFPot) world.getTileEntity(pos);
		tile.setMF(tile.getMF() + expValue);
		tile.sentClient();

		// クライアント（プレイヤー）へ送りつける
		if (player instanceof EntityPlayerMP) {
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_LEVELDOUWN, 1F, 0.25F), (EntityPlayerMP) player);
		}
	}


	// 食べ物をMFに変換
	public void removeFood (World world, BlockPos pos, EntityPlayer player, ItemStack stack) {

		Item item = stack.getItem();
		boolean isSMFood = item instanceof SMFood;
		ItemFood food  = (ItemFood) stack.getItem();
		int amount = food.getHealAmount(stack) * food.getHealAmount(stack) * 12;
		float saturation = isSMFood ? food.getSaturationModifier(stack) * food.getSaturationModifier(stack) : 0.23F;
		saturation = Math.max(saturation, 0.25F);
		int mfValue = (int) (amount * (saturation + 0.1)) * stack.getCount();

		stack.shrink(stack.getCount());

		TileMFPot tile = (TileMFPot) world.getTileEntity(pos);
		tile.setMF(tile.getMF() + mfValue);
		tile.sentClient();
		ParticleHelper.spawnParticle(world, pos, EnumParticleTypes.CRIT);

		// クライアント（プレイヤー）へ送りつける
		if (player instanceof EntityPlayerMP) {
			PacketHandler.sendToPlayer(new PlayerSoundPKT(SoundHelper.S_SHRINK, 1F, 0.25F), (EntityPlayerMP) player);
		}
	}

	public boolean canMFChange (World world, BlockPos pos) {
		return ((TileMFPot) world.getTileEntity(pos)).canMFChange();
	}

	@Override
	public int getMaxMF() {
		return 200000;
	}

    /**
     * 0 = ドリズリィの花瓶（雨MF生産）
     * 1 = 黄昏時の夢百合草（夕方MF生産）
     * 2 = スノードロップの花瓶（雪地MF生産）
     * 3 = トルコキキョウの花瓶（経験値吸収）
     * 4 = 群青の薔薇の花瓶（エンチャントはがし）
     * 5 = ソリッド・スターの花瓶（周囲のエンチャパワー回収）
     * 6 = ジニアの花瓶（光源でMF変換）
     * 7 = ハイドラシア（敵モブ倒してMF生産）
     * 8 = カーネーション（食べ物をMF変換）
     * 9 = クリスマスローズエリックスミシィ（雪レイヤーをMF変換）
     *
     */

	@Override
	public int getTier() {
		switch (this.data) {
		case 0:	  return 1;
		case 1:	  return 3;
		case 2:	  return 1;
		case 3:	  return 2;
		case 4:	  return 2;
		case 5:	  return 2;
		case 6:	  return 2;
		case 7:	  return 2;
		case 8:	  return 1;
		case 9:	  return 1;
		}
		return super.getMaxMF();
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag advanced) {

		super.addInformation(stack, world, tooltip, advanced);

		String tip = "";

		switch (this.data) {
		case 0:
			tip = "tip.mfpot_dm.name";
			break;
		case 1:
			tip = "tip.mfpot_tw.name";
			break;
		case 2:
			tip = "tip.snowdrop_pot.name";
			break;
		case 3:
			tip = "tip.turkey_balloonflower_pot.name";
			break;
		case 4:
			tip = "tip.ultramarine_rose_pot.name";
			break;
		case 5:
			tip = "tip.solid_star_pot.name";
			break;
		case 6:
			tip = "tip.zinnia_pot.name";
			break;
		case 7:
			tip = "tip.hydrangea_pot.name";
			break;
		case 8:
			tip = "tip.carnation_crayola_pot.name";
			break;
		case 9:
			tip = "tip.christmarose_ericsmithii_pot.name";
			break;
		}

		tooltip.add(I18n.format(TextFormatting.GOLD + this.getTip(tip)));
	}

	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

		if (this.data != 5 && this.data != 6) { return; }

		if (this.data == 5) {

			for (int x = -2; x <= 2; ++x) {
				for (int z = -2; z <= 2; ++z) {

					if (rand.nextInt(16) != 0) { continue; }
					if (x > -2 && x < 2 && z == -1) { z = 2; }

					for (int y = 0; y <= 1; ++y) {

						BlockPos bpos = pos.add(x, y, z);
						if (ForgeHooks.getEnchantPower(world, bpos) <= 0) { continue; }
						if (!world.isAirBlock(pos.add(x / 2, 0, z / 2))) { break; }

						world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D,
								(double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D,
								(double) ((float) x + rand.nextFloat()) - 0.5D,
								(double) ((float) y - rand.nextFloat() - 1.0F),
								(double) ((float) z + rand.nextFloat()) - 0.5D);
					}
				}
			}
		}

		else {

			for (int x = -2; x <= 2; ++x) {
				for (int z = -2; z <= 2; ++z) {

					if (rand.nextInt(16) != 0) { continue; }
					if (x > -2 && x < 2 && z == -1) { z = 2; }

					for (int y = 0; y <= 1; ++y) {

						BlockPos bpos = pos.add(x, y, z);
						IBlockState iState = world.getBlockState(bpos);
						Block block = iState.getBlock();
						if (block == Blocks.AIR || block.getLightValue(iState) <= 0) { continue; }
						if (!world.isAirBlock(pos.add(x / 2, 0, z / 2))) { break; }

						world.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, (double) pos.getX() + 0.5D,
								(double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D,
								(double) ((float) x + rand.nextFloat()) - 0.5D,
								(double) ((float) y - rand.nextFloat() - 1.0F),
								(double) ((float) z + rand.nextFloat()) - 0.5D);
					}
				}
			}
		}
	}
}
