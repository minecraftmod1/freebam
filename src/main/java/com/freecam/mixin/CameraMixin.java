package com.freecam.mixin;

import com.freecam.Freecam;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow private boolean detached;
    @Shadow protected abstract void setPosition(double x, double y, double z);
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Inject(method = { "setup", "update" }, at = @At("TAIL"), require = 0)
    private void freecam$setup(CallbackInfo ci) {
        if (!Freecam.active) return;
        Freecam.update();
        setRotation(Freecam.yaw, Freecam.pitch);
        setPosition(Freecam.x, Freecam.y, Freecam.z);
        detached = true;
    }
}
