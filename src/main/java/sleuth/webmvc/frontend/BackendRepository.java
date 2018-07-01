package sleuth.webmvc.frontend;

import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Repository
public class BackendRepository {

    RestTemplate restTemplate;

    public BackendRepository(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    @Async
    public Future<String> callingBackendAsync() {
        return new AsyncResult<>(MDC.get("traceId"));
    }
}
