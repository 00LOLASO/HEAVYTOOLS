package com.example.heavytools.item;

import com.example.heavytools.HeavyTools;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Minado en área del martillo.
 *
 * Compatibilidad: cada bloque extra se rompe con ServerPlayerEntity.interactionManager.tryBreakBlock(),
 * la MISMA ruta que usa el juego al minar. Así se disparan los eventos normales de Fabric
 * (mods de protección/claims), se aplican drops, Fortuna/Toque de seda, experiencia
 * y el desgaste de la herramienta. No hay mixins.
 */
public final class HammerHandler {
    private HammerHandler() {}

    private static final double REACH = 6.0;
    /** Evita recursión: los bloques extra también disparan el evento BEFORE. */
    private static boolean breakingArea = false;

    private static final Identifier LATE_PHASE = Identifier.of(HeavyTools.MOD_ID, "late");

    public static void register() {
        // Fase tardía: si un mod de protección cancela el bloque central antes, no hacemos nada.
        PlayerBlockBreakEvents.BEFORE.addPhaseOrdering(Event.DEFAULT_PHASE, LATE_PHASE);
        PlayerBlockBreakEvents.BEFORE.register(LATE_PHASE, HammerHandler::onBeforeBreak);
    }

    private static boolean onBeforeBreak(World world, PlayerEntity player, BlockPos pos,
                                         BlockState state, BlockEntity blockEntity) {
        if (breakingArea || world.isClient() || !(player instanceof ServerPlayerEntity serverPlayer)) {
            return true;
        }

        ItemStack tool = serverPlayer.getMainHandStack();
        // Agacharse = minar un solo bloque (útil para trabajo de precisión).
        if (!(tool.getItem() instanceof HammerItem hammer) || serverPlayer.isSneaking()) {
            return true;
        }

        // Para saber qué plano romper necesitamos la cara que el jugador está mirando.
        HitResult hit = serverPlayer.raycast(REACH, 0.0f, false);
        if (!(hit instanceof BlockHitResult blockHit) || !blockHit.getBlockPos().equals(pos)) {
            return true;
        }

        float centerHardness = state.getHardness(world, pos);

        breakingArea = true;
        try {
            for (BlockPos target : HammerItem.getAreaBlocks(pos, blockHit.getSide().getAxis())) {
                if (tool.isEmpty()) break;                 // el martillo se rompió
                if (target.equals(pos)) continue;          // el central lo rompe el juego

                BlockState targetState = world.getBlockState(target);
                if (targetState.isAir()) continue;

                float hardness = targetState.getHardness(world, target);
                if (hardness < 0) continue;                // irrompibles (bedrock...)
                if (hardness > Math.max(centerHardness, 5.0f)) continue; // no rompe obsidiana "gratis"
                if (!hammer.isCorrectForDrops(tool, targetState)) continue; // solo lo que un pico mina bien

                serverPlayer.interactionManager.tryBreakBlock(target);
            }
        } finally {
            breakingArea = false;
        }
        return true; // el bloque central se rompe de forma normal
    }
}
