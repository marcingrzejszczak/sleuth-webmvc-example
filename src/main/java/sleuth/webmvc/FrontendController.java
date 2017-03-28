package sleuth.webmvc;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanNamer;
import org.springframework.cloud.sleuth.TraceKeys;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.async.TraceableScheduledExecutorService;
import org.springframework.cloud.sleuth.util.ExceptionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * @author Marcin Grzejszczak
 */
@RestController
class FrontendController {
	private static final Logger log = LoggerFactory
			.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired RestTemplate template;
	@Autowired Tracer tracer;

	@RequestMapping("/") public String callBackend() {
		log.info("I'm here");
		log.info("Current span [{}]", tracer.getCurrentSpan());
		return Flux
				.just(1,2,3)
				.flatMap((d) -> Mono.fromCallable(() -> {
					log.info("Current span [{}]", tracer.getCurrentSpan());
					return template.getForObject("http://localhost:9000/api", String.class);
				}).subscribeOn(Schedulers.elastic()))
				.log()
				.blockLast();
	}

	@Autowired BeanFactory beanFactory;
	@Autowired TraceKeys traceKeys;
	@Autowired SpanNamer spanNamer;

	@PostConstruct public void foo() {
		ExceptionUtils.setFail(true);
		Schedulers.setFactory(new Schedulers.Factory() {
			@Override public ScheduledExecutorService decorateScheduledExecutorService(
					String schedulerType,
					Supplier<? extends ScheduledExecutorService> actual) {
				return new TraceableScheduledExecutorService(actual.get(), tracer,
						traceKeys, new SpanNamer() {
					@Override public String name(Object object, String defaultValue) {
						return "foo";
					}
				});
			}
		});
	}
}
