package sleuth.webmvc.frontend;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class FrontendAsyncConfigurerSupport {

    @Autowired
    BeanFactory beanFactory;

    @Bean
    public AsyncConfigurer asyncConfigurer() {
        return new AsyncConfigurerSupport() {
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
        };
    }

}
