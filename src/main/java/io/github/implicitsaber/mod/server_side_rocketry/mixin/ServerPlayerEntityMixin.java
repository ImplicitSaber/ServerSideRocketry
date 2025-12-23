package io.github.implicitsaber.mod.server_side_rocketry.mixin;

import io.github.implicitsaber.mod.server_side_rocketry.ServerSideRocketry;
import io.github.implicitsaber.mod.server_side_rocketry.reg.ModDataComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends LivingEntityMixin {

    @Shadow
    public abstract ServerWorld getEntityWorld();

    @Unique
    private static final Text DEFAULT_BOSSBAR_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_boss_bar")
            .formatted(Formatting.AQUA);
    @Unique
    private static final Text WARNING_BOSSBAR_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_boss_bar")
            .formatted(Formatting.YELLOW);
    @Unique
    private static final Text DANGER_BOSSBAR_TEXT = Text.translatable(ServerSideRocketry.MOD_ID + ".oxygen_boss_bar")
            .formatted(Formatting.RED, Formatting.BOLD);

    @Unique
    private final ServerBossBar server_side_rocketry$oxygenBossBar = new ServerBossBar(DEFAULT_BOSSBAR_TEXT, BossBar.Color.BLUE, BossBar.Style.PROGRESS);

    @Unique
    private boolean server_side_rocketry$bossBarVisible = false;

    @Override
    protected boolean server_side_rocketry$attemptConsumeOxygen() {
        int maxOxygen = 0;
        int oxygen = 0;
        boolean canBreathe = false;
        ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;
        PlayerInventory inv = p.getInventory();
        for(int i = 0; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if(stack.contains(ModDataComponents.MAX_OXYGEN)) {
                maxOxygen += Math.max(0, stack.getOrDefault(ModDataComponents.MAX_OXYGEN, 0));
                int o = Math.max(0, stack.getOrDefault(ModDataComponents.OXYGEN, 0));
                oxygen += o;
                if(o > 0 && !canBreathe) {
                    stack.set(ModDataComponents.OXYGEN, o - 1);
                    canBreathe = true;
                }
            }
        }
        server_side_rocketry$oxygenBossBar.setPercent((float) oxygen / Math.max(1, maxOxygen));
        if(oxygen <= 0) {
            server_side_rocketry$oxygenBossBar.setColor(BossBar.Color.RED);
            server_side_rocketry$oxygenBossBar.setName(DANGER_BOSSBAR_TEXT);
        } else if(oxygen <= 600) {
            if(Objects.requireNonNull(this.getEntityWorld().getServer()).getTicks() % 10 < 5) {
                server_side_rocketry$oxygenBossBar.setColor(BossBar.Color.BLUE);
                server_side_rocketry$oxygenBossBar.setName(DEFAULT_BOSSBAR_TEXT);
            } else {
                server_side_rocketry$oxygenBossBar.setColor(BossBar.Color.YELLOW);
                server_side_rocketry$oxygenBossBar.setName(WARNING_BOSSBAR_TEXT);
            }
        } else {
            server_side_rocketry$oxygenBossBar.setColor(BossBar.Color.BLUE);
            server_side_rocketry$oxygenBossBar.setName(DEFAULT_BOSSBAR_TEXT);
        }
        if(!server_side_rocketry$bossBarVisible) {
            server_side_rocketry$oxygenBossBar.addPlayer(p);
            server_side_rocketry$bossBarVisible = true;
        }
        return canBreathe;
    }

    @Override
    protected void server_side_rocketry$oxygenNotInUse() {
        ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;
        if(server_side_rocketry$bossBarVisible) {
            server_side_rocketry$oxygenBossBar.removePlayer(p);
            server_side_rocketry$bossBarVisible = false;
        }
    }

    @Override
    protected void server_side_rocketry$onRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        ServerPlayerEntity p = (ServerPlayerEntity) (Object) this;
        if(server_side_rocketry$bossBarVisible) {
            server_side_rocketry$oxygenBossBar.removePlayer(p);
            server_side_rocketry$bossBarVisible = false;
        }
    }

}
