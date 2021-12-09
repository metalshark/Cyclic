package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.conveyor.ConveyorItemEntity;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntity;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityCarry;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityDamage;
import com.lothrazar.cyclic.item.boomerang.BoomerangEntityStun;
import com.lothrazar.cyclic.item.elemental.FireEntity;
import com.lothrazar.cyclic.item.elemental.LightningEntity;
import com.lothrazar.cyclic.item.elemental.SnowEntity;
import com.lothrazar.cyclic.item.endereye.EyeOfEnderEntityNodrop;
import com.lothrazar.cyclic.item.findspawner.EntityDungeonEye;
import com.lothrazar.cyclic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclic.item.slingshot.StoneEntity;
import com.lothrazar.cyclic.item.torchthrow.EntityTorchBolt;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {

  @ObjectHolder(ModCyclic.MODID + ":dungeon")
  public static EntityType<EntityDungeonEye> DUNGEON;
  @ObjectHolder(ModCyclic.MODID + ":magic_net")
  public static EntityType<EntityMagicNetEmpty> NETBALL;
  @ObjectHolder(ModCyclic.MODID + ":torch_bolt")
  public static EntityType<EntityTorchBolt> torchbolt;
  @ObjectHolder(ModCyclic.MODID + ":lightning_bolt")
  public static EntityType<LightningEntity> lightningbolt;
  @ObjectHolder(ModCyclic.MODID + ":snow_bolt")
  public static EntityType<SnowEntity> snowbolt;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_stun")
  public static EntityType<BoomerangEntity> boomerang_stun;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_carry")
  public static EntityType<BoomerangEntity> boomerang_carry;
  @ObjectHolder(ModCyclic.MODID + ":boomerang_damage")
  public static EntityType<BoomerangEntity> boomerang_damage;
  @ObjectHolder(ModCyclic.MODID + ":eye")
  public static EntityType<EyeOfEnderEntityNodrop> eye;
  @ObjectHolder(ModCyclic.MODID + ":fire_bolt")
  public static EntityType<FireEntity> fire_bolt;
  @ObjectHolder(ModCyclic.MODID + ":stone_bolt")
  public static EntityType<StoneEntity> stone_bolt;
  @ObjectHolder(ModCyclic.MODID + ":conveyor_item")
  public static EntityType<ConveyorItemEntity> conveyor_item;

  @SubscribeEvent
  public static void registerEntity(RegistryEvent.Register<EntityType<?>> e) {
    IForgeRegistry<EntityType<?>> r = e.getRegistry();
    r.register(
        EntityType.Builder.<EntityMagicNetEmpty>create(EntityMagicNetEmpty::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("magic_net")
            .setRegistryName("magic_net"));
    r.register(
        EntityType.Builder.<EntityTorchBolt>create(EntityTorchBolt::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("torch_bolt")
            .setRegistryName("torch_bolt"));
    r.register(
        EntityType.Builder.<BoomerangEntityStun>create(BoomerangEntityStun::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("boomerang_stun")
            .setRegistryName("boomerang_stun"));
    r.register(
        EntityType.Builder.<BoomerangEntityCarry>create(BoomerangEntityCarry::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("boomerang_carry")
            .setRegistryName("boomerang_carry"));
    r.register(
        EntityType.Builder.<BoomerangEntityDamage>create(BoomerangEntityDamage::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("boomerang_damage")
            .setRegistryName("boomerang_damage"));
    r.register(
        EntityType.Builder.<EntityDungeonEye>create(EntityDungeonEye::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("dungeon")
            .setRegistryName("dungeon"));
    r.register(
        EntityType.Builder.<EyeOfEnderEntityNodrop>create(EyeOfEnderEntityNodrop::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("eye")
            .setRegistryName("eye"));
    r.register(
        EntityType.Builder.<LightningEntity>create(LightningEntity::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("lightning_bolt")
            .setRegistryName("lightning_bolt"));
    r.register(
        EntityType.Builder.<SnowEntity>create(SnowEntity::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("snow_bolt")
            .setRegistryName("snow_bolt"));
    r.register(
        EntityType.Builder.<FireEntity>create(FireEntity::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("fire_bolt")
            .setRegistryName("fire_bolt"));
    r.register(
        EntityType.Builder.<StoneEntity>create(StoneEntity::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(128)
            .size(.6f, .6f)
            .build("stone_bolt")
            .setRegistryName("stone_bolt"));
    r.register(
        EntityType.Builder.<ConveyorItemEntity>create(ConveyorItemEntity::new, EntityClassification.MISC)
            .setShouldReceiveVelocityUpdates(true)
            .setUpdateInterval(1)
            .setTrackingRange(1)
            .size(0.45F, 0.45F)
            .build("conveyor_item")
            .setRegistryName("conveyor_item"));
  }
}
