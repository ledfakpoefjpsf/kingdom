package dev.nova.kingdom.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.DefaultAttributeContainer;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.PathfinderMobEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;

public abstract class KingdomVillagerEntity extends PathfinderMobEntity {

    private static final TrackedData<Integer> SKILL_LEVEL = DataTracker.defineId(KingdomVillagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> NPC_ROLE = DataTracker.defineId(KingdomVillagerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected int skillXp = 0;

    protected KingdomVillagerEntity(EntityType<? extends PathfinderMobEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void defineSynchedData(DataTracker.Builder builder) {
        super.defineSynchedData(builder);
        builder.add(SKILL_LEVEL, 1);
        builder.add(NPC_ROLE, 0);
    }

    public static DefaultAttributeContainer.Builder createKingdomAttributes() {
        return PathfinderMobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    public void addSkillXp(int amount) {
        this.skillXp += amount;
        int currentLevel = entityData.get(SKILL_LEVEL);
        if (this.skillXp >= currentLevel * 100) {
            entityData.set(SKILL_LEVEL, currentLevel + 1);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3f + (currentLevel * 0.01f));
        }
    }

    public int getSkillLevel() {
        return entityData.get(SKILL_LEVEL);
    }

    public void setRole(int roleId) {
        entityData.set(NPC_ROLE, roleId);
    }

    public int getRole() {
        return entityData.get(NPC_ROLE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("SkillXp", this.skillXp);
        nbt.putInt("SkillLevel", getSkillLevel());
        nbt.putInt("NpcRole", getRole());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.skillXp = nbt.getInt("SkillXp");
        entityData.set(SKILL_LEVEL, nbt.getInt("SkillLevel"));
        entityData.set(NPC_ROLE, nbt.getInt("NpcRole"));
    }
}
