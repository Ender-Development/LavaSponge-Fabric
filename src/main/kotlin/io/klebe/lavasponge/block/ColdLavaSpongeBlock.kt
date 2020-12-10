package io.klebe.lavasponge.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.sound.BlockSoundGroup

object ColdLavaSpongeBlock : Block(
    FabricBlockSettings.of(Material.STONE)
        .requiresTool()
        .strength(20.0F, 1200.0F)
        .sounds(BlockSoundGroup.STONE)) {}