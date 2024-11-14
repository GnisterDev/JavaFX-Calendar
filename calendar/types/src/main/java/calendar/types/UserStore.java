package calendar.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserStore manages a mapping between usernames and user IDs, as well as a
 * mapping between user IDs and {@link User} objects. It provides methods for
 * adding, removing, and checking the existence of users by their username or
 * user ID.
 */
public class UserStore {

    /** A map between the username and userID. */
    @JsonProperty
    private Map<String, UUID> usernameToUserId;

    /** A map between the userID and the user. */
    @JsonProperty
    private Map<UUID, RestUser> userIdToUser;

    /**
     * Constructs a new {@code UserStore} with the provided mappings of
     * usernames to user IDs and user IDs to users.
     *
     * @param usernameToUserId a {@code Map} of usernames to their corresponding
     *                         user IDs
     * @param userIdToUser     a {@code Map} of user IDs to {@code User} objects
     */
    public UserStore(
            @JsonProperty("usernameToUserId")
            final Map<String, UUID> usernameToUserId,
            @JsonProperty("userIdToUser")
            final Map<UUID, RestUser> userIdToUser) {
        this.usernameToUserId = new HashMap<>(usernameToUserId);
        this.userIdToUser = new HashMap<>(userIdToUser);
    }

    /**
     * Adds a new {@link User} to the store if the user does not already exist.
     *
     * @param  user the {@link User} to add
     * @return      {@code true} if the user was added, {@code false} if the
     *              user already exists
     */
    public boolean addUser(final RestUser user) {
        if (hasUser(user)) return false;
        usernameToUserId.put(user.getUsername(), user.getUserId());
        userIdToUser.put(user.getUserId(), user);
        return true;
    }

    /**
     * Removes a user from the store based on the username.
     *
     * @param  username the username of the user to remove
     * @return          {@code true} if the user was removed, {@code false} if
     *                  the user does not exist
     */
    public boolean removeUser(final String username) {
        if (!hasUsername(username)) return false;
        return removeUser(getUserId(username).get());
    }

    /**
     * Removes a user from the store based on their user ID.
     *
     * @param  userId the user ID of the user to remove
     * @return        {@code true} if the user was removed, {@code false} if the
     *                user does not exist
     */
    public boolean removeUser(final UUID userId) {
        if (!hasUserId(userId)) return false;
        return removeUser(getUser(userId).get());
    }

    /**
     * Removes a {@link User} from the store.
     *
     * @param  user the {@link User} to remove
     * @return      {@code true} if the user was removed, {@code false} if the
     *              user does not exist
     */
    public boolean removeUser(final RestUser user) {
        if (!hasUser(user)) return false;
        usernameToUserId.remove(user.getUsername());
        userIdToUser.remove(user.getUserId());
        return true;
    }

    /**
     * Checks if a username exists in the store.
     *
     * @param  username the username to check
     * @return          {@code true} if the username exists, {@code false}
     *                  otherwise
     */
    public boolean hasUsername(final String username) {
        return usernameToUserId.containsKey(username);
    }

    /**
     * Checks if a user ID exists in the store.
     *
     * @param  userId the user ID to check
     * @return        {@code true} if the user ID exists, {@code false}
     *                otherwise
     */
    public boolean hasUserId(final UUID userId) {
        return userIdToUser.containsKey(userId);
    }

    /**
     * Checks if a {@link User} exists in the store.
     *
     * @param  user the {@link User} to check
     * @return      {@code true} if the user exists, {@code false} otherwise
     */
    public boolean hasUser(final RestUser user) {
        return hasUsername(user.getUsername()) && hasUserId(user.getUserId());
    }

    /**
     * Retrieves the {@link UUID} associated with a given username.
     *
     * @param  username the username to look up
     * @return          an {@code Optional} containing the user ID if found, or
     *                  an empty {@code Optional} if not found
     */
    public Optional<UUID> getUserId(final String username) {
        return Optional.ofNullable(usernameToUserId.get(username));
    }

    /**
     * Retrieves the {@link User} associated with a given user ID.
     *
     * @param  userId the user ID to look up
     * @return        an {@code Optional} containing the {@link User} if found,
     *                or an empty {@code Optional} if not found
     */
    public Optional<RestUser> getUser(final UUID userId) {
        return Optional.ofNullable(userIdToUser.get(userId));
    }
}
