package sleuth.webmvc.frontend;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class FrontendController {

    @Autowired
    BackendRepository backendRepository;

    @RequestMapping("/")
    public List<String> callBackend() throws ExecutionException, InterruptedException {
        //return restTemplate.getForObject("http://localhost:9000/api", String.class);
        Future<String> result1 = backendRepository.callingBackendAsync();
        Future<String> result2 = backendRepository.callingBackendAsync();
        Future<String> result3 = backendRepository.callingBackendAsync();
        return Arrays.asList(MDC.get("traceId"), result1.get(), result2.get(), result3.get());
    }

}
