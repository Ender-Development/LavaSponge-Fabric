package io.klebe.lavasponge

import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager

object LavaSponge : ModInitializer {
    const val MODID = "lavasponge"
    const val MODNAME = "Lava Sponge"

    val log = LogManager.getLogger(MODID)

    override fun onInitialize() = runBlocking {
        log.info("Start initialization");



        log.info("Initialization completed")
    }

}

