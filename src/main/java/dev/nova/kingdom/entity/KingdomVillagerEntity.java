package dev.nova.kingdom.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.PathfinderMob;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

public abstract class KingdomVillagerEntity extends PathfinderMob {

    private static final TrackedData<Integer> SKILL_LEVEL = DataTracker.registerData(KingdomVillagerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> NPC_ROLE = DataTracker.registerData(KingdomVillagerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected int skillXp = 0;

    protected KingdomVillagerEntity(EntityType<? extends PathfinderMob> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(SKILL_LEVEL, 1);
        builder.add(NPC_ROLE, 0); // 0 = Commoner / Citizen
    }

    public static DefaultAttributeContainer.Builder createKingdomAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    public void addSkillXp(int amount) {
        this.skillXp += amount;
        int currentLevel = dataTracker.get(SKILL_LEVEL);
        if (this.skillXp >= currentLevel * 100) {
            dataTracker.set(SKILL_LEVEL, currentLevel + 1);
            this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3f + (currentLevel * 0.01f));
        }
    }

    public int getSkillLevel() {
        return dataTracker.get(SKILL_LEVEL);
    }

    public void setRole(int roleId) {
        dataTracker.set(NPC_ROLE, roleId);
    }

    public int getRole() {
        return dataTracker.get(NPC_ROLE);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("SkillXp", this.skillXp);
        nbt.putInt("SkillLevel", getSkillLevel());
        nbt.putInt("NpcRole", getRole());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.skillXp = nbt.getInt("SkillXp");
        dataTracker.set(SKILL_LEVEL, nbt.getInt("SkillLevel"));
        dataTracker.set(NPC_ROLE, nbt.getInt("NpcRole"));
    }
}
