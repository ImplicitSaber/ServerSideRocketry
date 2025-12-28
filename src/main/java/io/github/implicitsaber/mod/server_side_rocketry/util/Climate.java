package io.github.implicitsaber.mod.server_side_rocketry.util;

public enum Climate {
    
    NORMAL(false, false),
    EXTREME(true, false),
    VERY_EXTREME(true, true);

    private final boolean causesDamage;
    private final boolean requiresAdvancedGear;

    Climate(boolean causesDamage, boolean requiresAdvancedGear) {
        this.causesDamage = causesDamage;
        this.requiresAdvancedGear = requiresAdvancedGear;
    }

    public boolean causesDamage() {
        return this.causesDamage;
    }

    public boolean requiresAdvancedGear() {
        return this.requiresAdvancedGear;
    }

}
