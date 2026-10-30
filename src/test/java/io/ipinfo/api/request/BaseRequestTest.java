package io.ipinfo.api.request;

import static org.junit.jupiter.api.Assertions.*;

import io.ipinfo.api.errors.ErrorResponseException;
import io.ipinfo.api.errors.RateLimitedException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import org.junit.jupiter.api.Test;

public class BaseRequestTest {

    @Test
    public void testRateLimitedResponseIsClosed() {
        TrackingBody body = new TrackingBody("{\"error\":\"rate limited\"}");
        BaseRequest<Void> request = requestReturning(429, body);

        assertThrows(RateLimitedException.class, () ->
            request.handleRequest(new Request.Builder().url("https://api.ipinfo.io/lookup/8.8.8.8"))
        );
        assertTrue(body.closed, "429 response body should be closed");
    }

    @Test
    public void testErrorResponseIsClosed() {
        TrackingBody body = new TrackingBody("{\"error\":\"forbidden\"}");
        BaseRequest<Void> request = requestReturning(403, body);

        assertThrows(ErrorResponseException.class, () ->
            request.handleRequest(new Request.Builder().url("https://api.ipinfo.io/lookup/8.8.8.8"))
        );
        assertTrue(body.closed, "error response body should be closed");
    }

    private static BaseRequest<Void> requestReturning(int code, ResponseBody body) {
        OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(chain -> new Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message("test")
                .body(body)
                .build())
            .build();
        return new BaseRequest<Void>(client, "test_token") {
            @Override
            public Void handle() {
                return null;
            }
        };
    }

    /** Records whether the underlying source was closed, which is what releases the connection. */
    private static final class TrackingBody extends ResponseBody {
        private final Buffer buffer;
        private final BufferedSource source;
        boolean closed;

        TrackingBody(String content) {
            this.buffer = new Buffer().writeUtf8(content);
            this.source = Okio.buffer(new ForwardingSource(buffer) {
                @Override
                public void close() throws java.io.IOException {
                    closed = true;
                    super.close();
                }
            });
        }

        @Override
        public MediaType contentType() {
            return MediaType.get("application/json");
        }

        @Override
        public long contentLength() {
            return buffer.size();
        }

        @Override
        public BufferedSource source() {
            return source;
        }
    }
}
