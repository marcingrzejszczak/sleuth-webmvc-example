package sleuth.webmvc;

import java.util.Date;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.util.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Backend {

  @RequestMapping("/api")
  public String printDate() {
    return new Date().toString();
  }

  /** The spring application name is used for the Zipkin service name */
  public static void main(String[] args) {
    ExceptionUtils.setFail(true);
    SpringApplication.run(Backend.class,
        "--spring.application.name=backend", "--server.port=9000");
  }
}
