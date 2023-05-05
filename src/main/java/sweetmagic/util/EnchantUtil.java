package sweetmagic.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EnchantUtil {

    public static int calculateNewEnchCost (Enchantment enchant, int level) {

        int cost = 45 * Math.max(11 - enchant.getRarity().getWeight(), 1) * level;
		cost *= 1.5F;

		if (enchant.isCurse()) {
			cost *= 1.5F;
		}

        else if (enchant.isTreasureEnchantment()) {
            cost *= 4F;
        }

        EnchantCostEvent event = new EnchantCostEvent(cost, enchant, level);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getCost();
    }

    public static class EnchantCostEvent extends Event {

        private final Enchantment enchant;
        private final int level;
        private final int originalCost;
        private int cost;

        public EnchantCostEvent (int cost, Enchantment enchant, int level) {
            super();
            this.cost = cost;
            this.originalCost = cost;
            this.enchant = enchant;
            this.level = level;
        }

        public Enchantment getEnchantment () {
            return this.enchant;
        }

        public int getLevel () {
            return this.level;
        }

        public int getCost () {
            return this.cost;
        }

        public void setCost (int cost) {
            this.cost = cost;
        }

        public int getOriginalCost () {
            return this.originalCost;
        }
    }
}
