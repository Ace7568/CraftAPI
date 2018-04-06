package com.github.games647.craftapi.resolver;

import com.github.games647.craftapi.model.NameHistory;
import com.github.games647.craftapi.model.Profile;
import com.github.games647.craftapi.model.skin.SkinProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Resolver that fetches Minecraft game profiles, skins and name history.
 */
public interface ProfileResolver {

    /**
     * Find all profiles for a given list of names.
     *
     * @param names case-insensitive player names (only 100 per request call are allowed)
     * @return immutable set of all profiles that are premium
     * @throws IOException I/O error on fetching the profile
     * @throws RateLimitException more than 600 name to UUID requests within 10 minutes
     * (if using proxies including for the chosen proxy)
     */
    ImmutableSet<Profile> findProfiles(String... names) throws IOException, RateLimitException;

    /**
     * Find one profile for the given name
     *
     * @param name case-insensitive player name
     * @return profile or empty if not premium
     * @throws IOException I/O error on fetching the profile
     * @throws RateLimitException more than 600 name to UUID requests within 10 minutes
     * (if using proxies including for the chosen proxy)
     */
    Optional<Profile> findProfile(String name) throws IOException, RateLimitException;

    /**
     * Fetch game profile that had the given name at a specific time. It only works if the player changed the name
     * at least once.
     *
     * @param name case-insensitive name
     * @param time timestamp the player had this name or {@link Instant#ofEpochMilli(long)} with 0 for the current name
     * @return the profile or empty if the player didn't changed the name or isn't premium
     * @throws IOException I/O error on fetching the skin
     * @throws RateLimitException more than 600 name to UUID requests within 10 minutes
     * (if using proxies including for the chosen proxy)
     */
    Optional<Profile> findProfile(String name, Instant time) throws IOException, RateLimitException;

    /**
     * Fetches the name history of that player
     *
     * @param uuid premium UUID
     * @return immutable list of the name history or empty if it isn't a premium player
     * @throws IOException I/O error on fetching the name history
     */
    ImmutableList<NameHistory> findNames(UUID uuid) throws IOException;

    /**
     * Fetches skin.
     *
     * @param uuid premium UUID
     * @return skin or empty if it's not a UUID of a premium player
     * @throws IOException I/O error on fetching the skin
     * @throws RateLimitException more than 1 request within one minute for the same uuid
     */
    Optional<SkinProperty> downloadSkin(UUID uuid) throws IOException, RateLimitException;
}
