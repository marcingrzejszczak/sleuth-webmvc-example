package sleuth.webmvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@EnableAutoConfiguration
@RestController
@CrossOrigin // So that javascript can be hosted elsewhere
public class Frontend {

  @Autowired BackendRepository backendRepository;

  @RequestMapping("/")
  public String callBackend() throws ExecutionException, InterruptedException {
    //return restTemplate.getForObject("http://localhost:9000/api", String.class);
    Future<String> result1 = backendRepository.callingBackendAsync();
    Future<String> result2 = backendRepository.callingBackendAsync();
    Future<String> result3 = backendRepository.callingBackendAsync();
    return result1.get() + "<br />" + result2.get() + "<br />" + result3.get();
  }

  public static void main(String[] args) {
    SpringApplication.run(Frontend.class,
        "--spring.application.name=frontend",
        "--server.port=8081"
    );
  }

}
