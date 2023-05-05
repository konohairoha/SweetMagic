package sweetmagic.init.tile.magic;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.client.particle.Particle;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import sweetmagic.client.particle.ParticleNomal;
import sweetmagic.init.enchant.EnchantWand;
import sweetmagic.util.EnchantUtil;

public class TileMagiaReIncarnation extends TileMagiaWrite {

	public int maxMagiaFlux = 30000000; 	// 最大MF量を設定

	// エンチャントリスト
	private static final List<Enchantment> notEnchaList = Arrays.<Enchantment> asList(
		Enchantments.FORTUNE, Enchantments.LOOTING
	);

	// エンチャのコストを取得
	public int getEnchantCost () {

		int cost = 0;
		ItemStack stack = this.getToolItem();

		// マップ（エンチャの種類、レベル）の取得
        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

		// エンチャの数だけ回す
		for (Entry<Enchantment, Integer> entry : map.entrySet()) {

			// エンチャを取得してコストを追加
			Enchantment enchant = entry.getKey();
			if (enchant.getMaxLevel() == 1 || notEnchaList.contains(enchant)) { continue; }

            final int level = entry.getValue() ;

            if (level > 0 && level < 10) {

            	boolean isSMEncha = enchant instanceof EnchantWand;
            	int rate = isSMEncha ? 20 : 100;
            	int weight = enchant.getRarity().getWeight();
            	float addRate = (isSMEncha ? 1F : (1F + (weight - 1)));

            	float rarityCost = (float) (11F - weight) / 2F * rate * addRate;
                cost += EnchantUtil.calculateNewEnchCost(enchant, level + 1) * rarityCost;
            }
		}

		return cost;
	}

	// エンチャレベルの書き換え
	public void enchantWrite () {

		if (this.isSever()) {

			// アイテムと必要コストの取得
			ItemStack stack = this.getToolItem();
			int useMF = this.getEnchantCost();

			// マップ（エンチャの種類、レベル）の取得
	        Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);

			// エンチャの数だけ回す
			for (Entry<Enchantment, Integer> entry : map.entrySet()) {

				// エンチャの最大レベルが1までなら次へ
				Enchantment enchant = entry.getKey();
				if (enchant.getMaxLevel() == 1 || notEnchaList.contains(enchant)) { continue; }

				// レベルが条件外なら次へ
	            int level = entry.getValue() ;
	            if (level <= 0 || level >= 10) { continue; }

	            // マップに入れなおす
				level++;
				map.put(enchant, Integer.valueOf(level));

				// アイテムにエンチャを再設定
				EnchantmentHelper.setEnchantments(map, stack);
			}

			// コスト分のMFを消費
			this.setMF(this.getMF() - useMF);
			this.sentClient();
		}

		else {
			this.spawnParticleRing(this.world, this.pos.getX() + 0.5F, this.pos.getY() + 1.5F, this.pos.getZ() + 0.5F, 1D, 0.1D);
			this.spawnParticleRing(this.world, this.pos.getX() + 0.5F, this.pos.getY() + 1.5F, this.pos.getZ() + 0.5F, 0.75D, 0.2D);
			this.spawnParticleRing(this.world, this.pos.getX() + 0.5F, this.pos.getY() + 1.5F, this.pos.getZ() + 0.5F, 0.5D, 0.3D);
		}

		this.playSound(this.pos, SoundEvents.ENTITY_PLAYER_LEVELUP, 1F, 0.5F);
		this.resetData();
	}

    public void spawnParticleRing (World world, double x, double y, double z, double step, double speed) {
        for (double degree = 0D; degree < 2D * Math.PI; degree += step) {
			Particle effect = ParticleNomal.create(world, x, y, z, -Math.cos(degree) * speed, 0, -Math.sin(degree) * speed);
			this.getParticle().addEffect(effect);
        }
    }

	public void spawnParticl () { }

	@Override
	public void finishParticle () { }

	// 最大MF量を取得
	@Override
	public int getMaxMF() {
		return this.maxMagiaFlux;
	}

    // 送信するMF量
	@Override
    public int getUseMF () {
    	return 1000000;
    }

	public int getData () {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
