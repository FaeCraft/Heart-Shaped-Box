package io.github.faecraft.heartshapedbox.networking;

import io.github.faecraft.heartshapedbox.body.AbstractBodyPart;
import io.github.faecraft.heartshapedbox.body.BodyPartProvider;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Optional;

import static io.github.faecraft.heartshapedbox.main.HSBMain.MOD_ID;

public class S2CBodyPartSyncPacket {
    public static final Identifier IDENTIFIER = new Identifier(MOD_ID, "body_part_sync");
    private final HashSet<AbstractBodyPart> parts = new HashSet<>();

    public S2CBodyPartSyncPacket() {}

    public S2CBodyPartSyncPacket(BodyPartProvider provider) {
        parts.addAll(provider.getParts());
    }

    public void addPart(AbstractBodyPart part) {
        parts.add(part);
    }

    public static void update(PacketByteBuf buffer, BodyPartProvider provider) {
        while (buffer.isReadable()) {
            Identifier id = buffer.readIdentifier();
            float health = buffer.readFloat();
            float maxHealth = buffer.readFloat();

            Optional<AbstractBodyPart> optionalPart = provider.maybeGet(id);

            if (optionalPart.isPresent()) {
                AbstractBodyPart part = optionalPart.get();

                part.setHealth(health);
                part.setMaxHealth(maxHealth);
            }
        }
    }

    public static S2CBodyPartSyncPacket from(ServerPlayerEntity player) {
        return new S2CBodyPartSyncPacket((BodyPartProvider) player);
    }

    public void write(PacketByteBuf buffer) {
        for (AbstractBodyPart part : parts) {
            buffer.writeIdentifier(part.getIdentifier());
            buffer.writeFloat(part.getHealth());
            buffer.writeFloat(part.getMaxHealth());
        }
    }

    public void send(ServerPlayerEntity player) {
        PacketByteBuf buffer = PacketByteBufs.create();

        write(buffer);

        ServerPlayNetworking.send(player, IDENTIFIER, buffer);
    }
}
