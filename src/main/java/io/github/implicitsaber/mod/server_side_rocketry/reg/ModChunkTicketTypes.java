package io.github.implicitsaber.mod.server_side_rocketry.reg;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ChunkTicketType;

import static io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry.id;
import static net.minecraft.server.world.ChunkTicketType.*;

public class ModChunkTicketTypes {

    public static final RegistryKey<ChunkTicketType> CARGO_ROCKET_KEY = RegistryKey.of(RegistryKeys.TICKET_TYPE, id("cargo_rocket"));
    public static final ChunkTicketType CARGO_ROCKET = Registry.register(
            Registries.TICKET_TYPE,
            CARGO_ROCKET_KEY,
            new ChunkTicketType(40L, SERIALIZE | FOR_LOADING | FOR_SIMULATION | RESETS_IDLE_TIMEOUT)
    );

    public static void load() {}

}
