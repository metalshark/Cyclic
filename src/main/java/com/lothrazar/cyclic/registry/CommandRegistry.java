package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.command.CommandGetHome;
import com.lothrazar.cyclic.command.CommandHealth;
import com.lothrazar.cyclic.command.CommandHome;
import com.lothrazar.cyclic.command.CommandHunger;
import com.lothrazar.cyclic.command.CommandNbt;
import com.lothrazar.cyclic.command.CommandNetherping;
import com.lothrazar.cyclic.command.CommandTask;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.util.Collection;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandRegistry {

  private static final String ARG_VALUE = "value";
  private static final String ARG_PLAYER = "player";

  private static int executeGlowing(CommandContext<CommandSource> x, Collection<ServerPlayerEntity> players, boolean bool) {
    for (ServerPlayerEntity p : players) {
      p.setGlowing(bool);
    }
    return 0;
  }

  private static int executeGravity(CommandContext<CommandSource> x, Collection<ServerPlayerEntity> players, boolean bool) {
    for (ServerPlayerEntity p : players) {
      p.setNoGravity(bool);
    }
    return 0;
  }

  private static int executeGamemode(CommandContext<CommandSource> x, Collection<ServerPlayerEntity> players, int integer) {
    for (ServerPlayerEntity p : players) {
      switch (integer) {
        case 0:
          p.setGameType(GameType.SURVIVAL);
          break;
        case 1:
          p.setGameType(GameType.CREATIVE);
          break;
        case 2:
          p.setGameType(GameType.ADVENTURE);
          break;
        case 3:
          p.setGameType(GameType.SPECTATOR);
          break;
        default:
          UtilChat.sendFeedback(x, integer + " = ?!");
          break;
      }
    }
    return 0;
  }

  @SubscribeEvent
  public void onRegisterCommandsEvent(RegisterCommandsEvent event) {
    CommandDispatcher<CommandSource> r = event.getDispatcher();
    r.register(LiteralArgumentBuilder.<CommandSource>literal(ModCyclic.MODID)
            .then(Commands.literal(CyclicCommands.HOME.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDHOME.get() ? 3 : 0))
                .executes(CommandHome::execute))
            .then(Commands.literal(CyclicCommands.GETHOME.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDGETHOME.get() ? 3 : 0))
                .executes(CommandGetHome::execute))
            .then(Commands.literal(CyclicCommands.HEALTH.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDHEALTH.get() ? 3 : 0))
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, FloatArgumentType.floatArg(0, 100F))
                        .executes(x -> CommandHealth.execute(x, EntityArgument.getPlayers(x, ARG_PLAYER), FloatArgumentType.getFloat(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.HEARTS.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDHEALTH.get() ? 3 : 0))
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(1, 100))
                        .executes(x -> CommandHealth.executeHearts(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.GAMEMODE.toString())
                .requires((p) -> {
                  return p.hasPermissionLevel(3); // 3 for gamemode
                })
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 3))
                        .executes(x -> CommandRegistry.executeGamemode(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.GRAVITY.toString())
                .requires((p) -> {
                  return p.hasPermissionLevel(3); // 3 for
                })
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                        .executes(x -> CommandRegistry.executeGravity(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.GLOWING.toString())
                .requires((p) -> {
                  return p.hasPermissionLevel(3); // 3 for
                })
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, BoolArgumentType.bool())
                        .executes(x -> CommandRegistry.executeGlowing(x, EntityArgument.getPlayers(x, ARG_PLAYER), BoolArgumentType.getBool(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.HUNGER.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDHUNGER.get() ? 3 : 0))
                .then(Commands.argument(ARG_PLAYER, EntityArgument.players())
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                        .executes(x -> CommandHunger.execute(x, EntityArgument.getPlayers(x, ARG_PLAYER), IntegerArgumentType.getInteger(x, ARG_VALUE))))))
            .then(Commands.literal(CyclicCommands.DEV.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDDEV.get() ? 3 : 0))
                //TODO: copy version. send network packet to client for clipboard
                .then(Commands.literal("nbt")
                    .executes(CommandNbt::executePrintNbt))
                .then(Commands.literal("tags")
                    .executes(CommandNbt::executePrintTags)))
            .then(Commands.literal(CyclicCommands.PING.toString())
                .requires((p) -> p.hasPermissionLevel(ConfigRegistry.COMMANDPING.get() ? 3 : 0))
                .then(Commands.literal("nether")
                    .executes(CommandNetherping::exeNether))
                .then(Commands.literal("here")
                    .executes(CommandNetherping::execute)))
            .then(Commands.literal(CyclicCommands.TODO.toString())
                .requires((p) -> p.hasPermissionLevel(0))
                .then(Commands.literal("add")
                    .then(Commands.argument("arguments", StringArgumentType.greedyString())
                        .executes(x -> CommandTask.add(x, StringArgumentType.getString(x, "arguments")))))
                .then(Commands.literal("remove")
                    .then(Commands.argument(ARG_VALUE, IntegerArgumentType.integer(0, 20))
                        .executes(x -> CommandTask.remove(x, IntegerArgumentType.getInteger(x, ARG_VALUE)))))
                .then(Commands.literal("list")
                    .executes(CommandTask::list)))
        //
    );
  }

  public enum CyclicCommands {

    HOME, GETHOME, HEALTH, HUNGER, DEV, PING, TODO, HEARTS, GAMEMODE, GRAVITY, GLOWING;

    @Override
    public String toString() {
      return this.name().toLowerCase();
    }
  }
}
