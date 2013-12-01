package net.minecraft.server;

import java.io.IOException;

public class PacketPlayInArmAnimation implements Packet<PacketListenerPlayIn> {

    public PacketPlayInArmAnimation() {}

    public void a(PacketDataSerializer packetdataserializer) throws IOException {}

    public void b(PacketDataSerializer packetdataserializer) throws IOException {}

    public void a(PacketListenerPlayIn packetlistenerplayin) {
        packetlistenerplayin.a(this);
    }
}
