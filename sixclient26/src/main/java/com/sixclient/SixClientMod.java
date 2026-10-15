package com.sixclient;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SixClientMod implements ClientModInitializer {
    public static final String MOD_ID = "sixclient";
    public static final String VERSION = "v1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger("SIX CLIENT");

    @Override
    public void onInitializeClient() {
        LOGGER.info("SIX CLIENT menu aktif.");
    }
}
