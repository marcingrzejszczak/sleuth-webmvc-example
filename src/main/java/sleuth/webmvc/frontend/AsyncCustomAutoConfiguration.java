package sleuth.webmvc.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.instrument.async.AsyncDefaultAutoConfiguration;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceAsyncCustomizer;
import org.springframework.cloud.sleuth.instrument.scheduling.TraceSchedulingAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

@Configuration
@ConditionalOnBean({AsyncConfigurer.class})
@AutoConfigureBefore({AsyncDefaultAutoConfiguration.class})
@ConditionalOnProperty(
        value = {"spring.sleuth.async.enabled"},
        matchIfMissing = true
)
@AutoConfigureAfter({TraceSchedulingAutoConfiguration.class})
public class AsyncCustomAutoConfiguration implements BeanPostProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncCustomAutoConfiguration.class);

    @Autowired
    private BeanFactory beanFactory;

    public AsyncCustomAutoConfiguration() {
        System.out.println("Inside constructor of AsyncCustomAutoConfiguration");
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AsyncConfigurer && !(bean instanceof LazyTraceAsyncCustomizer)) {
            System.out.println("Wrapping a AsyncConfigurer with LazyTraceAsyncCustomizer");
            AsyncConfigurer configurer = (AsyncConfigurer)bean;
            return new LazyTraceAsyncCustomizer(this.beanFactory, configurer);
        } else {
            return bean;
        }
    }
}

