# Basic example showing distributed tracing with @Async

# AsyncCustomAutoConfiguration does not post process a bean of type AsyncConfigurer

Sample application that has a custom `AsyncConfigurer` with a `ThreadPoolTaskExecutor` and core pool size of 10. When the spring boot application starts the following info log shows up-

`INFO  [main] o.s.c.s.PostProcessorRegistrationDelegate$BeanPostProcessorChecker - Bean 'asyncConfigurer' of type [sleuth.webmvc.frontend.FrontendAsyncConfigurerSupport$1] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
 1203 --- [           main] trationDelegate$BeanPostProcessorChecker : Bean 'asyncConfigurer' of type [sleuth.webmvc.frontend.FrontendAsyncConfigurerSupport$1] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)`


The application spawns 3 threads per request, and the expectation is the traceId is same for the spawned threads as that of the main thread. The 4th request (http://localhost:8080/) to the RestController is when the threads from the thread pool are reused and these reused threads aren't propagated the new traceId but hold onto the traceId from the previous requests.

The only way to solve this issue at present is to update application code; and wrap a ThreadPoolTaskExecutor in a LazyTraceExecutor. 
