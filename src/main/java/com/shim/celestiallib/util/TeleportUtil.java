package com.shim.celestiallib.util;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.capabilities.CLibCapabilities;
import com.shim.celestiallib.api.capabilities.ISpaceFlight;
import com.shim.celestiallib.config.CLibCommonConfig;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import com.shim.celestiallib.world.portal.CelestialTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;

public class TeleportUtil {

    protected static final Map<ResourceKey<Level>, List<Block>> DIMENSION_STRUCTURE_BLOCKS = new HashMap<>();

    public static List<Block> getDimensionStructureBlocks(ResourceKey<Level> dimension) {
        return DIMENSION_STRUCTURE_BLOCKS.get(dimension);
    }

    public static void addDimensionStructureBlocks(ResourceKey<Level> dimension, List<Block> blocks) {
        DIMENSION_STRUCTURE_BLOCKS.put(dimension, blocks);
    }

    public static final Map<ResourceKey<Level>, Galaxy> DATAPACK_PLANETS = new HashMap<>();

    public static void clearDatapackPlanets() {
        DATAPACK_PLANETS.clear();
    }

    public static void addDatapackPlanet(ResourceKey<Level> datapackDimension, Galaxy galaxy) {
        DATAPACK_PLANETS.put(datapackDimension, galaxy);
    }

    public static Galaxy getDatapackPlanetsGalaxy(ResourceKey<Level> datapackDimension) {
        if (DATAPACK_PLANETS.containsKey(datapackDimension))
            return DATAPACK_PLANETS.get(datapackDimension);
        return null;
    }

    public static void clearDimensionStructureBlocks() {
        DIMENSION_STRUCTURE_BLOCKS.clear();
    }

    private static final Map<ResourceKey<Level>, List<ResourceKey<Level>>> PLANET_MOONS_WITH_PLANET = new HashMap<>();

    public static void addMoons(ResourceKey<Level> dimension, List<ResourceKey<Level>> moons) {
        PLANET_MOONS_WITH_PLANET.put(dimension, moons);
    }

    public static void addMoon(ResourceKey<Level> dimension, ResourceKey<Level> moon) {
        if (PLANET_MOONS_WITH_PLANET.containsKey(dimension)) {
            ArrayList<ResourceKey<Level>> list = new ArrayList<>(PLANET_MOONS_WITH_PLANET.get(dimension));
            list.add(moon);
            PLANET_MOONS_WITH_PLANET.put(dimension, list);

        } else {
            PLANET_MOONS_WITH_PLANET.put(dimension, Collections.singletonList(moon));
        }
    }

    public static void displayTeleportMessage(Entity entity, int teleportCooldown, ResourceKey<Level> destination) {
        if (entity instanceof Player player) {
            if (teleportCooldown % 20 == 0 && teleportCooldown != 0) {
//                player.displayClientMessage(Component.nullToEmpty("Teleporting to " +
//                        new TranslatableComponent("dimension." + destination.location().getNamespace() + "." + destination.location().getPath()).getString() + " in… " + teleportCooldown / 20), true);

                player.displayClientMessage(new TranslatableComponent("celestial.teleport.message_1")
                        .append(CelestialUtil.getDisplayName(destination)
                                .append(new TranslatableComponent("celestial.teleport.message_2"))
                                .append(new TextComponent("" + teleportCooldown / 20))), true);
            } else if (teleportCooldown == 0) {
                player.displayClientMessage(new TranslatableComponent("celestial.teleport.teleporting"), true);
            }
        }
    }

    public static void displayLockedPlanetMessage(Entity entity, ResourceKey<Level> destination) {
        if (entity instanceof Player player) {
            player.displayClientMessage(CelestialUtil.getDisplayName(destination).append(" ")
                    .append(new TranslatableComponent("celestial.teleport.locked")), true);
        }
    }

    public static ResourceKey<Level> getTeleportLocation(Vec3 location, BlockState blockWeSee, Galaxy galaxy) {
        ResourceKey<Level> planet = null;
        List<ResourceKey<Level>> moons;
        ChunkPos planetChunkPos;

        if (galaxy == null)
            return null;

        if (blockWeSee.isAir())
            return null;

        //check if we're in the general area of a planet
        for (ResourceKey<Level> loc : CelestialUtil.getPlanetLocations().keySet()) {
            Planet planetToCheck = Planet.getPlanet(loc);

            //if we're a valid planet (but not a moon) OR a datapack planet
            if (planetToCheck != null && !planetToCheck.isMoon() || DATAPACK_PLANETS.containsKey(loc)) {
                if (planetToCheck != null) {
                    CelestialLib.LOGGER.debug("planetToCheck isn't null, planet is: " + loc);
                    System.out.println("planetToCheck isn't null, planet is: " + loc);
                } else if (DATAPACK_PLANETS.containsKey(loc)) {
                    CelestialLib.LOGGER.debug("is datapack planet, is: " + loc);
                    System.out.println("is datapack planet, is: " + loc);
                }

                if ((planetToCheck != null && planetToCheck.getGalaxy() == galaxy) || (getDatapackPlanetsGalaxy(loc) == galaxy)) {
                    planetChunkPos = CelestialUtil.getPlanetChunkCoordinates(loc, galaxy);
                    if (planetChunkPos == null)
                        CelestialLib.LOGGER.error("getTeleportLocation could not find location for planet {}", loc);

                    ChunkPos locationChunk = new ChunkPos(new BlockPos(location.x, location.y, location.z));

                    //check if we're somewhat nearby
                    if (CelestialUtil.isInRectangle(planetChunkPos.x, planetChunkPos.z, 6, locationChunk.x, locationChunk.z)) {
                        planet = loc;
                        break;
                    }
                }
            } else {
                CelestialLib.LOGGER.error("getTeleportLocation could not find planet for dimension {}", loc);
            }

//                if (planetToCheck == null) {
//                    //check for a datapack planet
//                    if (DATAPACK_PLANETS.containsKey(loc)) {
//
//                    }
//
//                    //if not a datapack planet, then print error
//                    CelestialLib.LOGGER.error("getTeleportLocation could not find planet for dimension {}", loc);
//                } else {
//                    //and eliminates planets that aren't in the galaxy we're in
//                    if (Planet.getPlanet(loc).getGalaxy() == galaxy) {
//                        planetChunkPos = CelestialUtil.getPlanetChunkCoordinates(loc);
//                        if (planetChunkPos == null)
//                            CelestialLib.LOGGER.error("getTeleportLocation could not find location for planet {}", loc);
//
//                        ChunkPos locationChunk = new ChunkPos(new BlockPos(location.x, location.y, location.z));
//
//                        //check if we're somewhat nearby
//                        if (CelestialUtil.isInRectangle(planetChunkPos.x, planetChunkPos.z, 6, locationChunk.x, locationChunk.z)) {
//                            planet = loc;
//                            break;
//                        }
//                    }
//                }

        }

        if (planet == null) return null;

        //check if what we're looking at matches said planet…
        List<Block> blocksToComp = getDimensionStructureBlocks(planet);
        if (blocksToComp == null) return null;

        for (Block block : blocksToComp) {
            //  return planet
            if (block.defaultBlockState().is(blockWeSee.getBlock())) {
                return planet;
            }
        }
        //…or check one of its moons
        moons = PLANET_MOONS_WITH_PLANET.get(planet);

        if (moons != null) {
            for (ResourceKey<Level> moon : moons) {
                blocksToComp = getDimensionStructureBlocks(moon);

                for (Block block : blocksToComp) {
                    //  return moon
                    if (block.defaultBlockState().is(blockWeSee.getBlock())) return moon;
                }
            }
        }
        //otherwise, we're not near and/or looking at anything relevant
        return null;
    }

    public static void handleLightSpeedTravel(ServerPlayer player, Entity spaceVehicle, ArrayList<Entity> passengers, ResourceKey<Level> galaxy, BlockPos planetPos) {
        TeleportUtil.teleportInDimension(spaceVehicle, passengers, planetPos);

//        ArrayList<Integer> entityIds = null;
//        if (passengers != null) {
//            entityIds = new ArrayList<>();
//            for (Entity entity : passengers) {
//                entityIds.add(entity.getId());
//            }
//        }

//        CLibPacketHandler.INSTANCE.sendTo(new ServerDidLightTravelPacket(spaceVehicle.getId(), entityIds, planetPos), player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);

    }

    public static void finishLightSpeedTravel(Entity spaceVehicle, ArrayList<Entity> passengers, BlockPos planetPos) {

        ISpaceFlight flightCap = CelestialLib.getCapability(spaceVehicle, CLibCapabilities.SPACE_FLIGHT_CAPABILITY);

        if (flightCap != null) {

            if (!spaceVehicle.level.isClientSide) {
                ServerLevel level = (ServerLevel) spaceVehicle.getLevel();
                level.getProfiler().push("placing");

                teleportTo(planetPos, spaceVehicle, passengers);

                level.getProfiler().pop();


//            if (spaceVehicle.level.isClientSide()) {
//                spaceVehicle.moveTo(planetPos, 0, 0);

                if (passengers != null) {
                    for (Entity passenger : passengers) {
                        if (passenger != null) {
//                        passenger.moveTo(planetPos, 0, 0);
                            passenger.startRiding(spaceVehicle);
                        }
                    }
                }
            }
        }
    }

    public static void teleportToDimension(Entity spaceVehicle, @Nullable ArrayList<Entity> passengers, ResourceKey<Level> destinationDim, BlockPos locationInPlace) {
        if (spaceVehicle.canChangeDimensions()) {

            //get server and level
            Level entityWorld = spaceVehicle.level;
            MinecraftServer minecraftserver = entityWorld.getServer();
            if (minecraftserver != null) {
                ServerLevel destinationWorld = minecraftserver.getLevel(destinationDim);
                if (destinationWorld != null) {

                    //if we're teleporting FROM a galaxy, travelers' Y level should be the max build height minus 10 blocks (so as to not immediately start a teleport back TO space)
                    if (!(Galaxy.isGalaxyDimension(destinationDim))) {
                        locationInPlace = new BlockPos(locationInPlace.getX(), destinationWorld.getMaxBuildHeight() - 10, locationInPlace.getZ());
                    } else { //otherwise grab y value from galaxy
                        locationInPlace = new BlockPos(locationInPlace.getX(), Galaxy.getGalaxy(destinationDim).getYHeight(), locationInPlace.getZ());
                    }

                    //move players to the right coordinates BEFORE changing dimensions
                    //otherwise, in mods like Celestial Exploration (the reason this library was written),
                    //there's a good chance of trying to load the admittedly massive sun structure,
                    //which caused lag if the space dimension hadn't been loaded before
                    //and sometimes the lag would cause discrepancies between client/server that the game didn't always recover from
                    if (!entityWorld.isClientSide) {
                        ServerLevel level = (ServerLevel) spaceVehicle.getLevel();
                        level.getProfiler().push("placing");

                        teleportTo(locationInPlace, spaceVehicle, passengers);

                        level.getProfiler().pop();
                    }

                    Entity newSpaceVehicle = null;
                    //check if spaceVehicle is a player or not
                    //because for all entities NOT players, changing dimensions returns a new instance of the entity with the same data
                    //but you do NOT create a new instance of a player
                    //so this is important to check
                    if (spaceVehicle instanceof Player) {
                        spaceVehicle.changeDimension(destinationWorld, new CelestialTeleporter(destinationWorld));
                        if (!entityWorld.isClientSide)
                            spaceVehicle.moveTo(locationInPlace.getX(), locationInPlace.getY(), locationInPlace.getZ(), spaceVehicle.getYRot(), spaceVehicle.getXRot());

                    } else {
                        newSpaceVehicle = spaceVehicle.changeDimension(destinationWorld, new CelestialTeleporter(destinationWorld));
                        if (!entityWorld.isClientSide)
                            newSpaceVehicle.moveTo(locationInPlace.getX(), locationInPlace.getY(), locationInPlace.getZ(), newSpaceVehicle.getYRot(), newSpaceVehicle.getXRot());

                    }

                    //this assumes that if the player is the spaceVehicle, that there are not additional passengers besides the player
                    if (newSpaceVehicle != null && passengers != null) {
                        //for all of our passengers…
                        ArrayList<Entity> newPassengers = new ArrayList<>();
                        for (Entity passenger : passengers) {
                            Entity newPassenger = null;
                            if (!passenger.level.dimension().equals(destinationDim)) {
                                //check if they're players or not to handle changing dimensions appropriately (see above)
                                if (passenger instanceof Player) {
                                    passenger.changeDimension(destinationWorld, new CelestialTeleporter(destinationWorld));
                                    newPassengers.add(passenger);
                                    if (!entityWorld.isClientSide)
                                        passenger.moveTo(locationInPlace.getX(), locationInPlace.getY(), locationInPlace.getZ(), passenger.getYRot(), passenger.getXRot());

                                } else {
                                    newPassenger = passenger.changeDimension(destinationWorld, new CelestialTeleporter(destinationWorld));
                                    newPassengers.add(newPassenger);
                                    if (!entityWorld.isClientSide)
                                        newPassenger.moveTo(locationInPlace.getX(), locationInPlace.getY(), locationInPlace.getZ(), newPassenger.getYRot(), newPassenger.getXRot());
                                }
                            }

                            //have passengers, player or otherwise, start riding the vehicle again
                            if (!entityWorld.isClientSide) {
                                if (passenger instanceof ServerPlayer) {
                                    passenger.startRiding(newSpaceVehicle);
                                } else if (newPassenger != null) {
                                    newPassenger.startRiding(newSpaceVehicle);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void teleportInDimension(Entity spaceVehicle, @Nullable ArrayList<Entity> passengers, BlockPos locationInPlace) {

        //get server and level
        Level entityWorld = spaceVehicle.level;
        MinecraftServer minecraftserver = entityWorld.getServer();
        if (minecraftserver != null) {
            ServerLevel destinationWorld = minecraftserver.getLevel(spaceVehicle.level.dimension());
            if (destinationWorld != null) {

                //adjust y height to galaxy y height
                locationInPlace = new BlockPos(locationInPlace.getX(), Galaxy.getGalaxy(spaceVehicle.level.dimension()).getYHeight(), locationInPlace.getZ());

                //teleport all entities
                if (!entityWorld.isClientSide) {
                    ServerLevel level = (ServerLevel) spaceVehicle.getLevel();
                    level.getProfiler().push("placing");

                    teleportTo(locationInPlace, spaceVehicle, passengers);

                    level.getProfiler().pop();
                }

                //for all of our passengers…
                for (Entity passenger : passengers) {
                    //…start riding the vehicle again
                    if (!entityWorld.isClientSide) {
                        if (!passenger.isPassenger())
                            passenger.startRiding(spaceVehicle);
                    }
                }
            }
        }
    }

    public static void teleportTo(BlockPos pos, Entity entity, ArrayList<Entity> passengers) {
        if (entity.level instanceof ServerLevel) {
            entity.moveTo(pos.getX(), pos.getY(), pos.getZ(), entity.getYRot(), entity.getXRot());

            if (passengers != null) {
                passengers.forEach((e) -> {
                    positionRider(entity, e, Entity::moveTo);
                });
            }
        }
    }

    private static void positionRider(Entity vehicle, Entity rider, Entity.MoveFunction moveFunction) {
        if (vehicle.hasPassenger(rider)) {
            double d0 = vehicle.getY() + vehicle.getPassengersRidingOffset() + rider.getMyRidingOffset();
            moveFunction.accept(rider, vehicle.getX(), d0, vehicle.getZ());
        }
    }

    @Nullable
    public static ResourceKey<Level> getGalaxyDestination(ResourceKey<Level> currentDimension) {
        //first check if current destination is the overworld,
        //because there might be multiple options on what galaxy the overworld "belongs" to
        if (currentDimension.equals(Level.OVERWORLD)) {

            if (Galaxy.DIMENSIONS.size() == 1) {
                //only one possible option
                return Galaxy.getFirstDimension();
            } else if (!CLibCommonConfig.DEFAULT_OVERWORLD_GALAXY.get().isEmpty()) {
                //default galaxy has been set
                return CelestialUtil.getDimensionFromString(CLibCommonConfig.DEFAULT_OVERWORLD_GALAXY.get());
            } else { //one of these not set, so player will need to choose a destination directly
                return null;
            }
        } else { //not the overworld
            //check for a properly registered planet…
            Planet planet = Planet.getPlanet(currentDimension);
            if (planet != null)
                return planet.getGalaxy().getDimension();

            //…or check for a datapack planet…
            if (DATAPACK_PLANETS.containsKey(currentDimension))
                return DATAPACK_PLANETS.get(currentDimension).getDimension();

            //otherwise, return null
            return null;
        }
    }
}