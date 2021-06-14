package sweetmagic.advanced;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import sweetmagic.SweetMagicCore;

public class AdvancedTrigger implements ICriterionTrigger<AdvancedTrigger.Instance> {

    private final ResourceLocation id;
    private final SetMultimap<PlayerAdvancements, Listener<? extends ICriterionInstance>> listeners = HashMultimap.create();

    public static class Instance extends AbstractCriterionInstance {
        public Instance(ResourceLocation id) {
            super(id);
        }
    }

    public AdvancedTrigger(String name) {
        super();
        this.id = new ResourceLocation(SweetMagicCore.MODID, name);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public void addListener(PlayerAdvancements advance, Listener<Instance> listener) {
    	this.listeners.put(advance, listener);
    }

    @Override
    public void removeListener(PlayerAdvancements advance, Listener<Instance> listener) {
    	this.listeners.remove(advance, listener);
    }

    @Override
    public void removeAllListeners(PlayerAdvancements advance) {
    	this.listeners.removeAll(advance);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new AdvancedTrigger.Instance(this.id);
    }

    public void triggerFor(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            final PlayerAdvancements advances = ((EntityPlayerMP) player).getAdvancements();
            this.listeners.get(advances).forEach((listener) -> listener.grantCriterion(advances));
        }
    }

    public void triggerLevel(EntityPlayer player, int need, int level) {
        if (player instanceof EntityPlayerMP && level >= need) {
            final PlayerAdvancements advances = ((EntityPlayerMP) player).getAdvancements();
            this.listeners.get(advances).forEach((listener) -> listener.grantCriterion(advances));
        }
    }
}
