package io.klebe.lavasponge.block

import com.google.common.collect.Lists
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.fluid.Fluids
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.tag.FluidTags
import net.minecraft.util.Pair
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

object LavaSpongeBlock : Block(
    FabricBlockSettings.of(Material.SPONGE)
        .strength(0.6F)
        .sounds(BlockSoundGroup.GRASS)) {

    override fun onBlockAdded(
        state: BlockState,
        world: World,
        pos: BlockPos,
        oldState: BlockState,
        notify: Boolean
    ) {
        if (!oldState.isOf(state.block)) {
            update(world, pos)
        }
    }

    override fun neighborUpdate(
        state: BlockState?,
        world: World,
        pos: BlockPos,
        block: Block?,
        fromPos: BlockPos?,
        notify: Boolean
    ) {
        update(world, pos)
        super.neighborUpdate(state, world, pos, block, fromPos, notify)
    }

    private fun update(world: World, pos: BlockPos) {
        if (absorbWater(world, pos)) {
            world.setBlockState(pos, WetLavaSpongeBlock.defaultState, 2)
            world.syncWorldEvent(2001, pos, getRawIdFromState(Blocks.LAVA.defaultState))
        }
    }

    private fun absorbWater(world: World, pos: BlockPos): Boolean {
        val absorptionQueue: Queue<Pair<BlockPos, /*distanceFormSponge:*/ Int>>
                = Lists.newLinkedList()

        absorptionQueue.add(Pair(pos, 0))

        var counterOfAbsorptions = 0
        while (!absorptionQueue.isEmpty()) {
            val pair: Pair<BlockPos, Int> = absorptionQueue.poll()
            val blockPos = pair.left
            val distanceFormSponge = pair.right

            val allDirections = Direction.values()
            for (direction in allDirections) {
                val neighborPosition = blockPos.offset(direction)
                val neighborBlockState = world.getBlockState(neighborPosition)
                val neighborFluidState = world.getFluidState(neighborPosition)
                val neighborMaterial = neighborBlockState.material

                if (neighborFluidState.isIn(FluidTags.LAVA)) {
                    if (
                        // if the neighbor is drainable and ...
                        (neighborBlockState.block is FluidDrainable) && (
                            // the drained fluid is not empty / nothing(?)
                            (neighborBlockState.block as FluidDrainable)
                                .tryDrainFluid( // (removes the lava (?)
                                    world,
                                    neighborPosition,
                                    neighborBlockState)
                        ) !== Fluids.EMPTY)
                    {   // then is a fluid there and:
                        counterOfAbsorptions += 1
                        if (distanceFormSponge < 6) {
                            // add this neighbor to queue
                            absorptionQueue.add(Pair(neighborPosition, distanceFormSponge + 1))
                        }

                    } else if (neighborBlockState.block is FluidBlock) {
                        // remove neighbor
                        world.setBlockState(neighborPosition, Blocks.AIR.defaultState, 3)

                        counterOfAbsorptions += 1
                        if (distanceFormSponge < 6) {
                            // add this neighbor to queue
                            // probably in case lava runs after here
                            absorptionQueue.add(Pair(neighborPosition, distanceFormSponge + 1))
                        }

                    } else if (neighborMaterial == Material.UNDERWATER_PLANT || neighborMaterial == Material.REPLACEABLE_UNDERWATER_PLANT) {
                        val plantBlockEntity =
                            if (neighborBlockState.block.hasBlockEntity())
                                world.getBlockEntity(neighborPosition)
                            else
                                null

                        // drop BlockEntity Container
                        dropStacks(neighborBlockState, world, neighborPosition, plantBlockEntity)
                        // and remove block
                        world.setBlockState(neighborPosition, Blocks.AIR.defaultState, 3)

                        ++counterOfAbsorptions

                        if (distanceFormSponge < 6) {
                            // add this neighbor to queue
                            absorptionQueue.add(Pair(neighborPosition, distanceFormSponge + 1))
                        }
                    }
                }
            }
            if (counterOfAbsorptions > 64) {
                // abort with 64 blocks removed
                break
            }
        }
        return counterOfAbsorptions > 0 // true if sponge should be wet
    }
}