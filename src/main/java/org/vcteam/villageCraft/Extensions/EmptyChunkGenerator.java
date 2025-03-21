package org.vcteam.villageCraft.Extensions;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import javax.annotation.Nonnull;
import java.util.Random;

public class EmptyChunkGenerator extends ChunkGenerator {

    /**
     * Generates an empty chunk.
     *
     * @return empty chunk
     */
    @Override
    @Nonnull
    public ChunkData generateChunkData(@Nonnull World world, @Nonnull Random random, int x, int z, @Nonnull BiomeGrid biome) {
        return createChunkData(world);
    }
}
