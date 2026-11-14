package com.example.heavytools.item;

import com.example.heavytools.HeavyTools;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.List;

public final class ModItems {
    private ModItems() {}

    // ---- Balance (ajusta a tu gusto) -----------------------------------
    // Daño final = daño base + daño del material vanilla + 1 (puño).
    private static final float HAMMER_DAMAGE = 6.0f;
    private static final float HAMMER_SPEED = -3.4f;      // muy lento
    private static final float HAMMER_DURABILITY = 2.0f;  // x durabilidad vanilla (rompe 9 bloques a la vez)

    private static final int GREATSWORD_DAMAGE = 6;
    private static final float GREATSWORD_SPEED = -3.0f;
    private static final float GREATSWORD_DURABILITY = 1.5f;

    private static final float HEAVY_AXE_DAMAGE = 8.0f;
    private static final float HEAVY_AXE_SPEED = -3.3f;
    private static final float HEAVY_AXE_DURABILITY = 1.5f;

    // ---- Martillos -----------------------------------------------------
    public static final Item WOODEN_HAMMER = hammer("wooden_hammer", ToolMaterials.WOOD);
    public static final Item STONE_HAMMER = hammer("stone_hammer", ToolMaterials.STONE);
    public static final Item IRON_HAMMER = hammer("iron_hammer", ToolMaterials.IRON);
    public static final Item DIAMOND_HAMMER = hammer("diamond_hammer", ToolMaterials.DIAMOND);
    public static final Item NETHERITE_HAMMER = hammer("netherite_hammer", ToolMaterials.NETHERITE);

    // ---- Grandes espadas -----------------------------------------------
    public static final Item WOODEN_GREATSWORD = greatsword("wooden_greatsword", ToolMaterials.WOOD);
    public static final Item STONE_GREATSWORD = greatsword("stone_greatsword", ToolMaterials.STONE);
    public static final Item IRON_GREATSWORD = greatsword("iron_greatsword", ToolMaterials.IRON);
    public static final Item DIAMOND_GREATSWORD = greatsword("diamond_greatsword", ToolMaterials.DIAMOND);
    public static final Item NETHERITE_GREATSWORD = greatsword("netherite_greatsword", ToolMaterials.NETHERITE);

    // ---- Hachas pesadas ------------------------------------------------
    public static final Item WOODEN_HEAVY_AXE = heavyAxe("wooden_heavy_axe", ToolMaterials.WOOD);
    public static final Item STONE_HEAVY_AXE = heavyAxe("stone_heavy_axe", ToolMaterials.STONE);
    public static final Item IRON_HEAVY_AXE = heavyAxe("iron_heavy_axe", ToolMaterials.IRON);
    public static final Item DIAMOND_HEAVY_AXE = heavyAxe("diamond_heavy_axe", ToolMaterials.DIAMOND);
    public static final Item NETHERITE_HEAVY_AXE = heavyAxe("netherite_heavy_axe", ToolMaterials.NETHERITE);

    private static final List<Item> HAMMERS = List.of(
            WOODEN_HAMMER, STONE_HAMMER, IRON_HAMMER, DIAMOND_HAMMER, NETHERITE_HAMMER);
    private static final List<Item> GREATSWORDS = List.of(
            WOODEN_GREATSWORD, STONE_GREATSWORD, IRON_GREATSWORD, DIAMOND_GREATSWORD, NETHERITE_GREATSWORD);
    private static final List<Item> HEAVY_AXES = List.of(
            WOODEN_HEAVY_AXE, STONE_HEAVY_AXE, IRON_HEAVY_AXE, DIAMOND_HEAVY_AXE, NETHERITE_HEAVY_AXE);

    // ---- Fábricas ------------------------------------------------------
    private static Item hammer(String id, ToolMaterial material) {
        Item.Settings settings = baseSettings(material, HAMMER_DURABILITY)
                .attributeModifiers(MiningToolItem.createAttributeModifiers(material, HAMMER_DAMAGE, HAMMER_SPEED));
        return register(id, new HammerItem(material, settings));
    }

    private static Item greatsword(String id, ToolMaterial material) {
        Item.Settings settings = baseSettings(material, GREATSWORD_DURABILITY)
                .attributeModifiers(SwordItem.createAttributeModifiers(material, GREATSWORD_DAMAGE, GREATSWORD_SPEED));
        return register(id, new SwordItem(material, settings));
    }

    private static Item heavyAxe(String id, ToolMaterial material) {
        Item.Settings settings = baseSettings(material, HEAVY_AXE_DURABILITY)
                .attributeModifiers(MiningToolItem.createAttributeModifiers(material, HEAVY_AXE_DAMAGE, HEAVY_AXE_SPEED));
        return register(id, new AxeItem(material, settings));
    }

    private static Item.Settings baseSettings(ToolMaterial material, float durabilityMultiplier) {
        Item.Settings settings = new Item.Settings()
                .maxDamage(Math.round(material.getDurability() * durabilityMultiplier));
        if (material == ToolMaterials.NETHERITE) {
            settings = settings.fireproof(); // igual que las herramientas de netherita vanilla
        }
        return settings;
    }

    private static Item register(String id, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(HeavyTools.MOD_ID, id), item);
    }

    /** Se llama desde onInitialize(): carga la clase (registra los items) y los añade a las pestañas. */
    public static void register() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(entries -> {
            HAMMERS.forEach(entries::add);
            HEAVY_AXES.forEach(entries::add);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            GREATSWORDS.forEach(entries::add);
            HEAVY_AXES.forEach(entries::add);
        });
    }
}
