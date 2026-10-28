package dev.nova.kingdom;

import dev.nova.kingdom.entity.LaylaEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobCategory;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kingdom implements ModInitializer {
    public static final String MOD_ID = "kingdom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final EntityType<LaylaEntity> LAYLA = Registry.register(
            BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(MOD_ID, "layla"),
            EntityType.Builder.of(LaylaEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .build("layla")
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Kingdom Mod with Mojang Mappings...");
        FabricDefaultAttributeRegistry.register(LAYLA, LaylaEntity.createLaylaAttributes());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
