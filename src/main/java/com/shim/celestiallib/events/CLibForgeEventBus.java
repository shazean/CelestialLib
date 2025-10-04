package com.shim.celestiallib.events;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.armor.ISpacesuit;
import com.shim.celestiallib.api.world.tags.CLibTags;
import com.shim.celestiallib.capabilities.*;
import com.shim.celestiallib.api.capabilities.ISpaceFlight;
import com.shim.celestiallib.config.CLibCommonConfig;
import com.shim.celestiallib.api.effects.GravityEffect;
import com.shim.celestiallib.packets.*;
import com.shim.celestiallib.util.CLibKeybinds;
import com.shim.celestiallib.util.CelestialUtil;
import com.shim.celestiallib.util.TeleportUtil;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.world.celestials.ICelestial;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CelestialLib.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CLibForgeEventBus {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Entity spaceVehicle = null;

        if (player.level.isClientSide()) {
            if (CLibKeybinds.OPEN_LIGHT_SPEED_TRAVEL.isDown()) {
                if (Galaxy.isGalaxyDimension(player.level.dimension())) {
                    CLibPacketHandler.INSTANCE.sendToServer(new LightSpeedMenuPacket());
                } else {
                    player.displayClientMessage(new TranslatableComponent("menu.celestiallib.light_speed_travel.invalid"), true);
                }
            }
        }

        if (Galaxy.isGalaxyDimension(player.level.dimension())) {
            //Warn players they're approaching min Y levels in outer space
            if (player.position().y <= player.level.getMinBuildHeight() + 10) {
                player.displayClientMessage(new TranslatableComponent("space_travel.celestiallib.space_min_height"), true);

            }
        }

        //should player teleport to/from space logic
        if (!player.level.isClientSide()) {
            //check for ISpaceFlight capability. This is checked against both the player and the player's vehicle, if applicable

            ISpaceFlight flightCap;
            if (player.getVehicle() != null) {
                flightCap = CelestialLib.getCapability(player.getVehicle(), CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
                if (flightCap != null) spaceVehicle = player.getVehicle();
            } else {
                flightCap = CelestialLib.getCapability(player, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
                if (flightCap != null) spaceVehicle = player;
            }

//            ISpaceFlight flightCap = CelestialLib.getCapability(player, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
//
//            if (flightCap != null) spaceVehicle = player;
//            else {
//                if (player.getVehicle() != null) {
//                    flightCap = CelestialLib.getCapability(player.getVehicle(), CLibCapabilities.SPACE_FLIGHT_CAPABILITY);
//                    if (flightCap != null) spaceVehicle = player.getVehicle();
//                }
//            }

            //once we've acquired our capability, regardless of if it comes from the player or the vehicle…
            if (spaceVehicle != null) {
                //this is the logic for traveling from a dimension TO its appropriate galaxy
                //check if space travel is allowed, and we're at the appropriate height
                if (flightCap.canSpaceTravel(spaceVehicle) && flightCap.isTeleportHeight(spaceVehicle) && !Galaxy.isGalaxyDimension(spaceVehicle.level.dimension())) {//&& !spaceVehicle.level.dimension().equals(CelestialDimensions.SPACE)) {
                    //find out what the destination galaxy is based off current dimension
                    ResourceKey<Level> destination = TeleportUtil.getGalaxyDestination(spaceVehicle.level.dimension());

                    if (destination == null) {
                        CLibPacketHandler.INSTANCE.sendToServer(new LightSpeedMenuPacket());
                        return;
                    }

                    //get a list of all of our teleporting entities
                    ArrayList<Entity> passengers = flightCap.getAdditionalEntitiesToTeleport(spaceVehicle);
                    //display message that we're teleporting to the galaxy
                    TeleportUtil.displayTeleportMessage(player, flightCap.getTeleportationCooldown(), destination);

                    //if the cooldown is 0, we're good to go
                    if (flightCap.getTeleportationCooldown() == 0) {
                        //determine where in space we're teleporting to
                        BlockPos pos = new BlockPos(spaceVehicle.position().x, spaceVehicle.position().y, spaceVehicle.position().z);

                        BlockPos teleportLocation = CelestialUtil.getDimensionToSpaceCoordinates(spaceVehicle.level.dimension(), new ChunkPos(pos));

                        //teleport and reset capability cooldown
                        TeleportUtil.teleportToDimension(spaceVehicle, passengers, destination, teleportLocation);
                        flightCap.resetTeleportationCooldown();
                    } else { //decrease cooldown
                        if (event.phase.equals(TickEvent.Phase.END)) {
                            flightCap.decrementTeleportationCooldown();
                        }
                    }
                } else if (flightCap.canSpaceTravel(spaceVehicle) && Galaxy.isGalaxyDimension(spaceVehicle.level.dimension())) {
                    //this is the logic for traveling from a galaxy to a planet
                    //like above, check if space travel is allowed and that we're in a galaxy dimension

                    //now we determine what planet (or moon) we should teleport to based off our general location and what block we're looking at
                    //first, find what block we can see
                    BlockHitResult hitResult;
                    if (spaceVehicle.is(player) || (spaceVehicle.isVehicle() && spaceVehicle.getControllingPassenger().is(player))) {
                        hitResult = (BlockHitResult) player.pick(flightCap.pickDistance(spaceVehicle), 0.0F, false);
                    } else {
                        hitResult = null;
                    }
                    if (hitResult != null) {

                        BlockState blockState = spaceVehicle.level.getBlockState(hitResult.getBlockPos());

                        if (!spaceVehicle.level.isClientSide()) {
                            //now check to see what planet/moon based off this block
                            ResourceKey<Level> destination = TeleportUtil.getTeleportLocation(spaceVehicle.position(), blockState, Galaxy.getGalaxy(spaceVehicle.level.dimension()));

                            //get our full list of passengers
                            ArrayList<Entity> passengers = flightCap.getAdditionalEntitiesToTeleport(spaceVehicle);

                            //if we're looking at a block that corresponds with a destination…
                            if (destination != null) {

                                IUnlock travelCap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

                                //check to make sure destination isn't locked
                                if (travelCap != null) {
                                    //if destination is a real planet and not a datapack planet
                                    if (Planet.getPlanet(destination) != null) {
                                        if (travelCap.isCelestialLocked(Planet.getPlanet(destination))) {
                                            //planet is locked, so cancel all travel
                                            //display message to player so they know what's happening
                                            TeleportUtil.displayLockedPlanetMessage(player, destination);
                                            return;
                                        }
                                    }
                                }

                                //display message
                                TeleportUtil.displayTeleportMessage(player, flightCap.getTeleportationCooldown(), destination);

                                //check for cooldown to be 0
                                if (flightCap.getTeleportationCooldown() == 0) {
                                    //send a packet back to the client to update cooldown
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CLibPacketHandler.INSTANCE.sendTo(new SpaceFlightPacket(flightCap.getTeleportationCooldown()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                                    }

                                    //teleport and reset cooldown
                                    TeleportUtil.teleportToDimension(spaceVehicle, passengers, destination, spaceVehicle.blockPosition());
                                    flightCap.resetTeleportationCooldown();

                                } else {
                                    //update cooldown on the client, and decrease cooldown
                                    if (player instanceof ServerPlayer serverPlayer) {
                                        CLibPacketHandler.INSTANCE.sendTo(new SpaceFlightPacket(flightCap.getTeleportationCooldown()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
                                    }
                                    flightCap.decrementTeleportationCooldown();
                                }
                            } else { //if we haven't found a destination (or looked away before cooldown reached 0), reset cooldown
                                flightCap.resetTeleportationCooldown();
                            }
                        }
                    }
                }
            }
        }
//

        //at the end of the tick, decrease light speed travel cooldowns, if applicable to the player
        PlanetCoolDownHandler coolDownCap = (PlanetCoolDownHandler) CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);
        if (coolDownCap != null) {
            if (event.phase.equals(TickEvent.Phase.END)) {
                coolDownCap.decrementCooldowns();
            }
        }
//
//        if (CelestialCommonConfig.STORMS.get()) {
//            //if a player is in a dust storm, apply slowness
//            //and do damage if they're not wearing any armor at all
//            //note: this doesn't care if a player is in a building or underground at all…
//            if (player.level.isRaining() && player.level.getBiome(player.blockPosition()).is(CelestialTags.Biomes.DUST_STORM_BIOMES)) {
//                ItemStack boots = player.getInventory().getArmor(0);
//                ItemStack leggings = player.getInventory().getArmor(1);
//                ItemStack breastplate = player.getInventory().getArmor(2);
//                ItemStack helmet = player.getInventory().getArmor(3);
//
//                if (helmet.isEmpty() && breastplate.isEmpty() && leggings.isEmpty() && boots.isEmpty()) {
//                    player.hurt(CelestialDamageSource.DUST_STORM, 0.5F);
//                }
//
//                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 0, false, false, true));
//            }
//            if (!player.level.isRaining() && player.level.getBiome(player.blockPosition()).is(CelestialTags.Biomes.DUST_STORM_BIOMES)) {
//                player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
//            }
//        }
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        Entity entity = event.getEntity();
        ResourceKey<Level> dimension = event.getWorld().dimension();

        if (event.getEntity() instanceof Player player) {
            ICoolDown travelCap = CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);

            if (travelCap != null) {
                travelCap.sync(player);
            }

            IUnlock unlockCap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

            if (unlockCap != null) {
                unlockCap.sync(player);
            }

        }

        if (CLibCommonConfig.GRAVITY_EFFECTS.get()) {

            if (entity instanceof ServerPlayer player) {
                ItemStack itemStack = player.getItemBySlot(EquipmentSlot.FEET);
                ICelestial celestial = Planet.getPlanet(dimension);
                if (celestial == null) celestial = Galaxy.getGalaxy(dimension);

                if (celestial != null) {
                    GravityEffect gravity = celestial.getGravity();
                    if (gravity != null) {
                        if (itemStack.getItem() instanceof ISpacesuit suit && suit.shouldNegateGravity(gravity, itemStack)) {
                            player.removeEffect(gravity);
                        } else {
                            player.addEffect(new MobEffectInstance(gravity, 120000, 0, false, false, true));
                        }
                    } else {
                        for (GravityEffect effect : GravityEffect.GRAVITY_EFFECTS) {
                            player.removeEffect(effect);
                        }
                    }
                }

            } else if (entity instanceof LivingEntity livingEntity) {
                ICelestial celestial = Planet.getPlanet(dimension);
                if (celestial == null) celestial = Galaxy.getGalaxy(dimension);

                if (celestial != null) {
                    GravityEffect gravity = celestial.getGravity();
                    if (gravity != null) {
                        //check if entity is not exempt from gravity by way of tags
                        if ((gravity.isLowGravity() && !livingEntity.getType().is((CLibTags.EntityTypes.LOW_GRAVITY_EXEMPT))) || (gravity.isHighGravity() && !livingEntity.getType().is((CLibTags.EntityTypes.HIGH_GRAVITY_EXEMPT))))
                            livingEntity.addEffect(new MobEffectInstance(gravity, 120000, 0, false, false, true));
                    } else {
                        for (GravityEffect effect : GravityEffect.GRAVITY_EFFECTS) {
                            livingEntity.removeEffect(effect);
                        }
                    }
                }
            }
        }
//        if (CelestialCommonConfig.STORMS.get()) {
//            if (event.getWorld().isRaining() && event.getWorld().getBiome(entity.blockPosition()).is(CelestialTags.Biomes.DUST_STORM_BIOMES)) {
//                if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
//                    livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 7000, 0, false, false, true));
//                }
//            }
//            if (!event.getWorld().isRaining() && event.getWorld().getBiome(entity.blockPosition()).is(CelestialTags.Biomes.DUST_STORM_BIOMES)) {
//                if (entity instanceof LivingEntity livingEntity && !(entity instanceof Player)) {
//                    livingEntity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
//                }
//            }
//        }
    }

    @SubscribeEvent
    public static void onAdvancement(AdvancementEvent event) {
        Player player = event.getPlayer();
        IUnlock travelCap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

        if (travelCap != null) {

            List<ICelestial> lockedCelestials = CelestialUtil.getLockedCelestials(event.getAdvancement().getId());
            if (lockedCelestials != null) {

                for (ICelestial celestial : lockedCelestials) {
                    travelCap.unlockCelestial(celestial);
                    if (player instanceof ServerPlayer serverPlayer)
                        CLibPacketHandler.INSTANCE.sendTo(new ServerUnlockedCelestialPacket(player.getId(), celestial.getDimension(), false, celestial.isGalaxy()), serverPlayer.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

                }
            }

            lockedCelestials = CelestialUtil.getLockedLightSpeedCelestials(event.getAdvancement().getId());
            if (lockedCelestials != null) {

                for (ICelestial celestial : lockedCelestials) {
                    travelCap.unlockCelestialLightSpeed(celestial);

                    if (player instanceof ServerPlayer serverPlayer) {
                        final PacketDistributor.PacketTarget targetPlayer = PacketDistributor.PLAYER.with(() -> serverPlayer);
                        CLibPacketHandler.INSTANCE.send(targetPlayer, new ServerUnlockedCelestialPacket(player.getId(), celestial.getDimension(), true, celestial.isGalaxy()));

                    }
                }
            }

        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getPlayer();
        ICoolDown travelCap = CelestialLib.getCapability(player, CLibCapabilities.COOLDOWN_CAPABILITY);

        if (travelCap != null) {

            Planet planet = Planet.getPlanet(event.getTo());

            if (planet != null) {
                travelCap.setVisited(planet);
            }

            travelCap.sync(player);
        }

        IUnlock unlockCap = CelestialLib.getCapability(player, CLibCapabilities.UNLOCK_CAPABILITY);

        if (unlockCap != null) {
            unlockCap.sync(player);
        }
    }

    @SubscribeEvent
    public static void onEntityEquipmentChange(LivingEquipmentChangeEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof ServerPlayer player) {
            ItemStack itemStack = event.getTo();
            ICelestial celestial = Planet.getPlanet(player.level.dimension());
            if (celestial == null) celestial = Galaxy.getGalaxy(player.level.dimension());

            if (celestial != null) {
                MobEffect gravity = celestial.getGravity();
                if (gravity != null) {
                    if (itemStack.getItem() instanceof ISpacesuit suit && suit.shouldNegateGravity(gravity, itemStack)) {
                        player.removeEffect(gravity);
                    } else {
                        player.addEffect(new MobEffectInstance(gravity, 120000, 0, false, false, true));
                    }
                } else {
                    for (GravityEffect effect : GravityEffect.GRAVITY_EFFECTS) {
                        player.removeEffect(effect);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            if (event.getOriginal() != null && event.getPlayer() != null) {
                event.getOriginal().reviveCaps();

                ICoolDown oldTravelData = event.getOriginal().getCapability(CLibCapabilities.COOLDOWN_CAPABILITY).orElse(null);
                ICoolDown newTravelData = event.getPlayer().getCapability(CLibCapabilities.COOLDOWN_CAPABILITY).orElse(null);
                if (oldTravelData != null && newTravelData != null) newTravelData.setData(oldTravelData.getData());

                IUnlock oldUnlockData = event.getOriginal().getCapability(CLibCapabilities.UNLOCK_CAPABILITY).orElse(null);
                IUnlock newUnlockData = event.getPlayer().getCapability(CLibCapabilities.UNLOCK_CAPABILITY).orElse(null);
                if (oldUnlockData != null && newUnlockData != null) newUnlockData.setData(oldUnlockData.getData());

                event.getOriginal().invalidateCaps();
            }
        }
    }

    @SubscribeEvent
    public static void onDatapackSync(final OnDatapackSyncEvent event) {
        for (ServerPlayer player : event.getPlayer() == null ? event.getPlayerList().getPlayers() : List.of(event.getPlayer())) {
            final PacketDistributor.PacketTarget targetPlayer = PacketDistributor.PLAYER.with(() -> player);
            CLibPacketHandler.INSTANCE.send(targetPlayer, new SyncPlanetLocPacket());
            CLibPacketHandler.INSTANCE.send(targetPlayer, new SyncMasterLockedCelestialsPacket());
        }
    }

}