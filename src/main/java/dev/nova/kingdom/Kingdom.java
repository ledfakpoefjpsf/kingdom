package dev.nova.kingdom;

import dev.nova.kingdom.entity.LaylaEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kingdom implements ModInitializer {
    public static final String MOD_ID = "kingdom";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Register Layla Entity Type
    public static final EntityType<LaylaEntity> LAYLA = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(MOD_ID, "layla"),
            EntityType.Builder.create(LaylaEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 1.8f)
                    .build()
    );

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Kingdom Mod...");
        
        // Register Attributes for Layla
        FabricDefaultAttributeRegistry.register(LAYLA, LaylaEntity.createLaylaAttributes());
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
