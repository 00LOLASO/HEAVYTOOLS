package com.example.heavytools;

import com.example.heavytools.item.HammerHandler;
import com.example.heavytools.item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeavyTools implements ModInitializer {
    public static final String MOD_ID = "heavytools";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModItems.register();      // registra items y pestañas creativas
        HammerHandler.register(); // lógica de minado 3x3 del martillo
        LOGGER.info("Heavy Tools cargado");
    }
}
