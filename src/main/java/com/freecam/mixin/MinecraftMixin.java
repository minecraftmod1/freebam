package com.freecam.mixin;

import com.freecam.Freecam;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Inject(method = "tick", at = @At("HEAD"), require = 0)
    private void freecam$tick(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;

        if (Freecam.active && (mc.player == null || mc.level == null || !mc.player.isAlive())) {
            Freecam.active = false;
        }

        boolean pressed = false;
        while (mc.options.keyTogglePerspective.consumeClick()) {
            pressed = true;
        }
        if (pressed && mc.screen == null && mc.player != null && mc.level != null) {
            Freecam.toggle(mc);
        }
    }
}
