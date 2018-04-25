package sleuth.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Repository
public class BackendRepository {

    @Autowired
    RestTemplate restTemplate;

    @Async
    public Future<String> callingBackendAsync() {
        return new AsyncResult<>(restTemplate.getForObject("http://localhost:9000/api", String.class));
    }
}
