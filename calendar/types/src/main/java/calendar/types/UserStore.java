package calendar.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserStore {

    @JsonProperty
    private Map<String, UUID> usernameToUserId;

    @JsonProperty
    private Map<UUID, User> userIdToUser;

    public UserStore(
            @JsonProperty("usernameToUserId") Map<String, UUID> usernameToUserId,
            @JsonProperty("userIdToUser") Map<UUID, User> userIdToUser) {
        this.usernameToUserId = new HashMap<>(usernameToUserId);
        this.userIdToUser = new HashMap<>(userIdToUser);
    }

    public boolean addUser(User user) {
        if (hasUser(user))
            return false;
        usernameToUserId.put(user.getUsername(), user.getUserId());
        userIdToUser.put(user.getUserId(), user);
        return true;
    }

    public boolean removeUser(String username) {
        if (!hasUsername(username))
            return false;
        return removeUser(getUserId(username).get());
    }

    public boolean removeUser(UUID userId) {
        if (!hasUserId(userId))
            return false;
        return removeUser(getUser(userId).get());
    }

    public boolean removeUser(User user) {
        if (!hasUser(user))
            return false;
        usernameToUserId.remove(user.getUsername());
        userIdToUser.remove(user.getUserId());
        return true;
    }

    public boolean hasUsername(String username) {
        return usernameToUserId.containsKey(username);
    }

    public boolean hasUserId(UUID userId) {
        return userIdToUser.containsKey(userId);
    }

    public boolean hasUser(User user) {
        return hasUsername(user.getUsername()) && hasUserId(user.getUserId());
    }

    public Optional<UUID> getUserId(String username) {
        return Optional.ofNullable(usernameToUserId.get(username));
    }

    public Optional<User> getUser(UUID userId) {
        return Optional.ofNullable(userIdToUser.get(userId));
    }
}
