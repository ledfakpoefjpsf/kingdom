package dev.nova.kingdom.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public class LaylaEntity extends KingdomVillagerEntity {

    private static final TrackedData<Integer> AFFECTION = DataTracker.registerData(LaylaEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> EMOTION_STATE = DataTracker.registerData(LaylaEntity.class, TrackedDataHandlerRegistry.INTEGER); // 0=Happy, 1=Upset, 2=Betrayed

    public LaylaEntity(EntityType<? extends LaylaEntity> entityType, World world) {
        super(entityType, world);
        this.setPersistent(); // Ensures Layla never despawns naturally
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(AFFECTION, 100); // Start with high loyalty/affection
        builder.add(EMOTION_STATE, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new FollowOwnerGoal(this, 1.2, 5.0f, 2.0f)); // Follows the player king closely
        this.goalSelector.add(3, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0));
    }

    public static DefaultAttributeContainer.Builder createLaylaAttributes() {
        return KingdomVillagerEntity.createKingdomAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0) // Stronger than standard citizens
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0);
    }

    public void adjustAffection(int amount) {
        int current = dataTracker.get(AFFECTION);
        int updated = Math.max(0, Math.min(200, current + amount));
        dataTracker.set(AFFECTION, updated);
        
        // Trigger emotional changes based on affection (e.g. cheating penalty)
        if (updated < 30) {
            dataTracker.set(EMOTION_STATE, 2); // Betrayed / Upset state
        } else {
            dataTracker.set(EMOTION_STATE, 0); // Normal / Happy
        }
    }

    @Override
    public boolean cannotDespawn() {
        return true; // Queen Layla is immortal/permanent to the world save
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("Affection", dataTracker.get(AFFECTION));
        nbt.putInt("EmotionState", dataTracker.get(EMOTION_STATE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dataTracker.set(AFFECTION, nbt.getInt("Affection"));
        dataTracker.set(EMOTION_STATE, nbt.getInt("EmotionState"));
    }
}
