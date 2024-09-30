package calendar.types;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserStore {
    @JsonProperty
    private Map<String, UUID> usernameToUserId;
    @JsonProperty
    private Map<UUID, User> userIdToUser;

    public UserStore(
            @JsonProperty("usernameToUserId") Map<String, UUID> usernameToUserId,
            @JsonProperty("userIdToUser") Map<UUID, User> userIdToUser) {
        this.usernameToUserId = usernameToUserId;
        this.userIdToUser = userIdToUser;
    }

    public boolean hasUsername(String username) {
        return usernameToUserId.containsKey(username);
    }

    public Optional<UUID> getUserId(String username) {
        return Optional.ofNullable(usernameToUserId.get(username));
    }

    public boolean hasUserId(UUID userId) {
        return userIdToUser.containsKey(userId);
    }

    public Optional<User> getUser(UUID userId) {
        return Optional.ofNullable(userIdToUser.get(userId));
    }
}
