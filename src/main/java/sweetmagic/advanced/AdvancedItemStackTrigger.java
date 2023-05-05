package sweetmagic.advanced;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AdvancedItemStackTrigger implements ICriterionTrigger<AdvancedItemStackTrigger.Instance> {

	private final ResourceLocation id;
	private final Map<PlayerAdvancements, AdvancedItemStackTrigger.Listeners> listeners = Maps.newHashMap();

	public AdvancedItemStackTrigger(ResourceLocation id) {
		this.id = id;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public void addListener(PlayerAdvancements advance, Listener<AdvancedItemStackTrigger.Instance> listener) {

		AdvancedItemStackTrigger.Listeners listeners = this.listeners.get(advance);

		if (listeners == null) {
			listeners = new AdvancedItemStackTrigger.Listeners(advance);
			this.listeners.put(advance, listeners);
		}

		listeners.add(listener);
	}

	public void removeListener(PlayerAdvancements advance, Listener<AdvancedItemStackTrigger.Instance> listener) {

		AdvancedItemStackTrigger.Listeners listeners = this.listeners.get(advance);

		if (listeners != null) {
			listeners.remove(listener);

			if (listeners.isEmpty()) {
				this.listeners.remove(advance);
			}
		}
	}

	public void removeAllListeners(PlayerAdvancements advance) {
		this.listeners.remove(advance);
	}

	public AdvancedItemStackTrigger.Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
		return new AdvancedItemStackTrigger.Instance(this.id, ItemPredicate.deserialize(json.get("item")));
	}

	public void trigger(EntityPlayerMP player, ItemStack stack) {

		AdvancedItemStackTrigger.Listeners listeners = this.listeners.get(player.getAdvancements());

		if (listeners != null) {
			listeners.trigger(stack);
		}
	}

	public static class Instance extends AbstractCriterionInstance {

		private final ItemPredicate item;

		public Instance(ResourceLocation src, ItemPredicate item) {
			super(src);
			this.item = item;
		}

		public boolean test(ItemStack stack) {
			return this.item.test(stack);
		}
	}

	static class Listeners {

		private final PlayerAdvancements playerAdvance;
		private final Set<Listener<AdvancedItemStackTrigger.Instance>> listeners = Sets.newHashSet();

		public Listeners(PlayerAdvancements advance) {
			this.playerAdvance = advance;
		}

		public boolean isEmpty() {
			return this.listeners.isEmpty();
		}

		public void add(Listener<AdvancedItemStackTrigger.Instance> listener) {
			this.listeners.add(listener);
		}

		public void remove(Listener<AdvancedItemStackTrigger.Instance> listener) {
			this.listeners.remove(listener);
		}

		public void trigger(ItemStack stack) {

			List<Listener<AdvancedItemStackTrigger.Instance>> list = null;

			for (Listener<AdvancedItemStackTrigger.Instance> listener : this.listeners) {

				if (listener.getCriterionInstance().test(stack)) {

					if (list == null) {
						list = Lists.newArrayList();
					}

					list.add(listener);
				}
			}

			if (list != null) {
				for (Listener<AdvancedItemStackTrigger.Instance> listener : list) {
					listener.grantCriterion(this.playerAdvance);
				}
			}
		}
	}
}