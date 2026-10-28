package dev.nova.kingdom.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.DefaultAttributeContainer;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public class LaylaEntity extends KingdomVillagerEntity {

    private static final TrackedData<Integer> AFFECTION = DataTracker.defineId(LaylaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EMOTION_STATE = DataTracker.defineId(LaylaEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public LaylaEntity(EntityType<? extends LaylaEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData(DataTracker.Builder builder) {
        super.defineSynchedData(builder);
        builder.add(AFFECTION, 100);
        builder.add(EMOTION_STATE, 0);
    }

    public static DefaultAttributeContainer.Builder createLaylaAttributes() {
        return KingdomVillagerEntity.createKingdomAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0);
    }

    public void adjustAffection(int amount) {
        int current = entityData.get(AFFECTION);
        int updated = Math.max(0, Math.min(200, current + amount));
        entityData.set(AFFECTION, updated);
        
        if (updated < 30) {
            entityData.set(EMOTION_STATE, 2);
        } else {
            entityData.set(EMOTION_STATE, 0);
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceSquared) {
        return false; // Prevents despawning
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Affection", entityData.get(AFFECTION));
        nbt.putInt("EmotionState", entityData.get(EMOTION_STATE));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        entityData.set(AFFECTION, nbt.getInt("Affection"));
        entityData.set(EMOTION_STATE, nbt.getInt("EmotionState"));
    }
}
