package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.EntityRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilString;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityMagicNetEmpty extends ProjectileItemEntity {

  public static final String NBT_ENTITYID = ModCyclic.MODID + ":magicnet_id";
  public static final int PARTICLE_CAPTURE_COUNT = 8;

  public EntityMagicNetEmpty(EntityType<? extends ProjectileItemEntity> entityType, World world) {
    super(entityType, world);
  }

  public EntityMagicNetEmpty(World worldIn, LivingEntity livingEntityIn) {
    super(EntityRegistry.NETBALL, livingEntityIn, worldIn);
  }

  @Override
  protected Item getDefaultItem() {
    return ItemRegistry.magic_net;
  }

  @Override
  protected void onImpact(RayTraceResult result) {
    RayTraceResult.Type type = result.getType();
    BasicParticleType particleType = null;
    double targetHeightOffset = 0.0d;
    if (type == RayTraceResult.Type.ENTITY) {
      //now grab and kill the entity
      EntityRayTraceResult entityRayTrace = (EntityRayTraceResult) result;
      Entity target = entityRayTrace.getEntity();
      if (target instanceof PlayerEntity || !target.isAlive()) {
        return;
      }
      //Wake up the mob in case they're sleeping in a bed see Issue #1599
      if (target instanceof LivingEntity) {
        ((LivingEntity) target).wakeUp();
      }
      CompoundNBT compound = new CompoundNBT();
      target.writeUnlessPassenger(compound);
      //
      if (target.getType() == EntityType.CHEST_MINECART
          || target.getType() == EntityType.COMMAND_BLOCK_MINECART
          || target.getType() == EntityType.FURNACE_MINECART
          || target.getType() == EntityType.HOPPER_MINECART) {
        //non-config; hardcoded any minecart with inventory
        //because they cheat
        //and dont follow the logic of other mobs
        //example: donkey with chest and chest containing diamonds, no issues
        return;
      }
      String id = EntityType.getKey(target.getType()).toString();
      if (UtilString.isInList(ConfigRegistry.getMagicNetList(), EntityType.getKey(target.getType()))) {
        ModCyclic.LOGGER.info("ignored by: CONFIG LIST" + id);
        return;
      }
      //
      //
      //
      compound.putString(NBT_ENTITYID, id);
      ItemStack drop = new ItemStack(ItemRegistry.mob_container);
      drop.setTag(compound);
      targetHeightOffset = target.getHeight() / 2;
      particleType = ParticleTypes.PORTAL;
      UtilItemStack.drop(world, this.getPosition(), drop);
      UtilSound.playSound(target, SoundRegistry.MONSTER_BALL_CAPTURE);
      target.remove();
    } else if (type == RayTraceResult.Type.BLOCK) {
      //      BlockRayTraceResult bRayTrace = (BlockRayTraceResult) result;
      BlockPos pos = this.getPosition();
      targetHeightOffset = 0.0D;
      particleType = ParticleTypes.POOF;
      UtilItemStack.drop(world, pos, new ItemStack(ItemRegistry.magic_net));
    }
    if (particleType != null) {
      Vector3d hitVec = result.getHitVec();
      for (int i = 0; i < PARTICLE_CAPTURE_COUNT; i++) {
        world.addParticle(particleType, hitVec.getX(), hitVec.getY() + targetHeightOffset, hitVec.getZ(), this.rand.nextGaussian() * 0.1D, 0.0D, this.rand.nextGaussian() * 0.1D);
      }
    }
    this.remove();
  }

  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
