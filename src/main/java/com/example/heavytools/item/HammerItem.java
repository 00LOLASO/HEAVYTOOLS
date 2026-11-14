package com.example.heavytools.item;

import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Martillo: es un PickaxeItem de verdad (mismos bloques minables, mismas etiquetas,
 * mismos encantamientos). El área 3x3 la aplica {@link HammerHandler}.
 */
public class HammerItem extends PickaxeItem {
    /** 1 = área 3x3. (2 sería 5x5.) */
    public static final int RADIUS = 1;

    public HammerItem(ToolMaterial material, Item.Settings settings) {
        super(material, settings);
    }

    /** Devuelve los bloques del plano perpendicular a la cara golpeada, centrados en {@code center}. */
    public static List<BlockPos> getAreaBlocks(BlockPos center, Direction.Axis axis) {
        List<BlockPos> blocks = new ArrayList<>();
        for (int a = -RADIUS; a <= RADIUS; a++) {
            for (int b = -RADIUS; b <= RADIUS; b++) {
                blocks.add(switch (axis) {
                    case X -> center.add(0, a, b); // pared este/oeste
                    case Y -> center.add(a, 0, b); // suelo / techo
                    case Z -> center.add(a, b, 0); // pared norte/sur
                });
            }
        }
        return blocks;
    }
}
