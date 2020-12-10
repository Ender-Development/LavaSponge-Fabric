package io.klebe.lavasponge

import io.klebe.lavasponge.block.LavaSpongeBlock
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

object LavaSponge : ModInitializer {
    const val MODID = "lavasponge"
    const val MODNAME = "Lava Sponge"

    val log = LogManager.getLogger(MODID)

    override fun onInitialize() = runBlocking {
        log.info("Start initialization")

        Registry.register(Registry.BLOCK, Identifier(MODID, "lavasponge"), LavaSpongeBlock)
        Registry.register(Registry.ITEM, Identifier(MODID, "lavasponge"), BlockItem(LavaSpongeBlock, Item.Settings()))

        log.info("Initialization completed")
    }

}

