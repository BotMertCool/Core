package us.zonix.core.api.request;

import lombok.RequiredArgsConstructor;
import us.zonix.core.shared.api.request.Request;
import us.zonix.core.util.MapUtil;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class MessageRequest implements Request {

    private final String path;

    @Override
    public String getPath() {
        return "/message/" + this.path;
    }

    @Override
    public Map<String, Object> toMap() {
        return null;
    }

    public static final class FetchByUuidRequest extends MessageRequest {

        public FetchByUuidRequest(UUID uuid) {
            super("fetch_by_uuid/" + uuid.toString());
        }

    }

    public static final class InsertRequest extends MessageRequest {

        private UUID uuid;
        private String message;
        private long timestamp;

        public InsertRequest(UUID uuid, String message) {
            super("insert/");

            this.uuid = uuid;
            this.message = message;
            this.timestamp = System.currentTimeMillis();
        }

        @Override
        public Map<String, Object> toMap() {
            return MapUtil.of(
                    "uuid", this.uuid.toString(),
                    "message", this.message,
                    "timestamp", this.timestamp
            );
        }

    }

}