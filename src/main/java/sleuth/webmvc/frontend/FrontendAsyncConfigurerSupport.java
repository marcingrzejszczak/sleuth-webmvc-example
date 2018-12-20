package sleuth.webmvc.frontend;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class FrontendAsyncConfigurerSupport extends AsyncConfigurerSupport {

    public FrontendAsyncConfigurerSupport() {
        System.out.println("Creation");
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();

        return executor;
        //return new LazyTraceExecutor(beanFactory, executor);
    }

}
