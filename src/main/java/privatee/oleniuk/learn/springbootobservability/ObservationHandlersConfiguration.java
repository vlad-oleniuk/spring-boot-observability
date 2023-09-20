package privatee.oleniuk.learn.springbootobservability;


import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.handler.DefaultTracingObservationHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class ObservationHandlersConfiguration {

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        return new ObservedAspect(observationRegistry);
    }


//    @Bean
//    public DefaultTracingObservationHandler defaultTracingObservationHandler(Tracer tracer){
//        return new DefaultTracingObservationHandler(tracer) {
//            @Override
//            public boolean supportsContext(Observation.Context context) {
//                return !(context instanceof HelloService.HelloContext);
//            }
//        };
//    }

}
