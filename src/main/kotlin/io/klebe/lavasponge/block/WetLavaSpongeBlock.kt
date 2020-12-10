package io.klebe.lavasponge.block

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

object WetLavaSpongeBlock : Block(
    FabricBlockSettings.of(Material.SPONGE)
        .strength(1F)
        .sounds(BlockSoundGroup.GRAVEL)) {

    override fun onBlockAdded(
        state: BlockState?,
        world: World,
        pos: BlockPos?,
        oldState: BlockState?,
        notify: Boolean
    ) {
        if (!world.dimension.isUltrawarm) {
            world.setBlockState(pos, ColdLavaSpongeBlock.defaultState, 3)
            world.syncWorldEvent(2009, pos, 0)
            world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_FIRE_EXTINGUISH,
                SoundCategory.BLOCKS,
                1.0f,
                (1.0f + world.getRandom().nextFloat() * 0.2f) * 0.7f
            )
        }
    }
}