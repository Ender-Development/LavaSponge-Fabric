package io.klebe.lavasponge

import io.klebe.lavasponge.block.ColdLavaSpongeBlock
import io.klebe.lavasponge.block.LavaSpongeBlock
import io.klebe.lavasponge.block.WetLavaSpongeBlock
import kotlinx.coroutines.runBlocking
import net.fabricmc.api.ModInitializer
import net.minecraft.block.Block
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager

object LavaSponge : ModInitializer {
    const val MODID = "lavasponge"
    const val MODNAME = "Lava Sponge"

    val log = LogManager.getLogger(MODID)

    private fun registerBlock(block: Block, id: String) {
        Registry.register(Registry.BLOCK, Identifier(MODID, id), block)
        Registry.register(Registry.ITEM, Identifier(MODID, id), BlockItem(block, Item.Settings()))
    }

    override fun onInitialize() {
        log.info("Start initialization")

        registerBlock(LavaSpongeBlock, "lavasponge")
        registerBlock(WetLavaSpongeBlock, "wetlavasponge")
        registerBlock(ColdLavaSpongeBlock, "coldlavasponge")

        log.info("Initialization completed")
    }

}

