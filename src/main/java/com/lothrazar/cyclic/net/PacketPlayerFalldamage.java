package com.lothrazar.cyclic.net;

import com.lothrazar.cyclic.base.PacketBase;
import java.util.function.Supplier;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Used by: Fan block; Launch enchant; Air charm; Climbing Glove; Scaffolding Block
 */
public class PacketPlayerFalldamage extends PacketBase {

  public static final int TICKS_FALLDIST_SYNC = 22; //tick every so often

  public static void handle(PacketPlayerFalldamage message, Supplier<NetworkEvent.Context> ctx) {
    ctx.get().enqueueWork(() -> {
      ServerPlayerEntity player = ctx.get().getSender();
      /**
       * if fall damage gets high, they take damage on landing
       */
      player.fallDistance = 0.0F;
      /**
       * Used to keep track of how the player is floating while gamerules should prevent that. Surpassing 80 ticks means kick
       */
      player.connection.floatingTickCount = 0;
    });
    message.done(ctx);
  }

  public static PacketPlayerFalldamage decode(PacketBuffer buf) {
    return new PacketPlayerFalldamage();
  }

  public static void encode(PacketPlayerFalldamage msg, PacketBuffer buf) {
  }
}
