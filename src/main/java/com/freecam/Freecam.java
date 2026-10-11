package com.freecam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.phys.Vec3;

public final class Freecam {
    public static boolean active = false;
    public static double x, y, z;
    public static float yaw, pitch;
    private static long last;

    private static final double BASE_SPEED = 12.0; // blocks per second
    private static final double SPRINT_MULT = 3.0;

    public static void toggle(Minecraft mc) {
        LocalPlayer p = mc.player;
        if (active) {
            active = false;
            return;
        }
        if (p == null) return;
        Vec3 eye = p.getEyePosition();
        x = eye.x;
        y = eye.y;
        z = eye.z;
        yaw = p.getYRot();
        pitch = p.getXRot();
        last = System.nanoTime();
        active = true;
    }

    public static void look(double dYaw, double dPitch) {
        yaw += (float) dYaw * 0.15f;
        pitch += (float) dPitch * 0.15f;
        if (pitch > 90f) pitch = 90f;
        if (pitch < -90f) pitch = -90f;
    }

    /** Called once per rendered frame while active. */
    public static void update() {
        Minecraft mc = Minecraft.getInstance();
        long now = System.nanoTime();
        double dt = Math.min((now - last) / 1.0e9, 0.1);
        last = now;
        if (mc.screen != null) return;

        Options o = mc.options;
        double f = (o.keyUp.isDown() ? 1 : 0) - (o.keyDown.isDown() ? 1 : 0);
        double s = (o.keyLeft.isDown() ? 1 : 0) - (o.keyRight.isDown() ? 1 : 0);
        double v = (o.keyJump.isDown() ? 1 : 0) - (o.keyShift.isDown() ? 1 : 0);
        double speed = BASE_SPEED * (o.keySprint.isDown() ? SPRINT_MULT : 1.0) * dt;

        double yr = Math.toRadians(yaw);
        double pr = Math.toRadians(pitch);
        double fx = -Math.sin(yr) * Math.cos(pr);
        double fy = -Math.sin(pr);
        double fz = Math.cos(yr) * Math.cos(pr);
        double lx = Math.cos(yr);
        double lz = Math.sin(yr);

        x += (fx * f + lx * s) * speed;
        y += (fy * f + v) * speed;
        z += (fz * f + lz * s) * speed;
    }
}
