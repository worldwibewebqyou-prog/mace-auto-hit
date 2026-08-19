package com.macemod.autohit;

import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil.Type;
import net.minecraft.entity.Entity;
import net.minecraft.hit.EntityHitResult;
import net.minecraft.hit.HitResult;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public class MaceAutoHitClient implements ClientModInitializer {

	private static KeyBinding toggleKey;
	private boolean enabled = false;

	private static final float FALL_DISTANCE_THRESHOLD = 1.5f;

	private int cooldownTicks = 0;
	private static final int COOLDOWN_TICKS = 4;

	@Override
	public void onInitializeClient() {
		toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.maceautohit.toggle",
				Type.KEYSYM,
				InputUtil.UNKNOWN_KEY.getCode(),
				"category.maceautohit"
		));

		ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
	}

	private void onTick(MinecraftClient client) {
		while (toggleKey.wasPressed()) {
			enabled = !enabled;
			if (client.player != null) {
				client.player.sendMessage(
						Text.literal("[Mace Auto Hit] " + (enabled ? "ON" : "OFF")),
						true
				);
			}
		}

		if (cooldownTicks > 0) {
			cooldownTicks--;
		}

		if (!enabled || client.player == null || client.interactionManager == null) {
			return;
		}

		boolean holdingMace = client.player.getMainHandStack().isOf(Items.MACE);
		boolean isFalling = client.player.fallDistance >= FALL_DISTANCE_THRESHOLD && !client.player.isOnGround();

		if (!holdingMace || !isFalling || cooldownTicks > 0) {
			return;
		}

		HitResult target = client.crosshairTarget;
		if (target instanceof EntityHitResult entityHit) {
			Entity entity = entityHit.getEntity();
			client.interactionManager.attackEntity(client.player, entity);
			client.player.swingHand(Hand.MAIN_HAND);
			cooldownTicks = COOLDOWN_TICKS;
		}
	}
}
