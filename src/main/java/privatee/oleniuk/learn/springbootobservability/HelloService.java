package privatee.oleniuk.learn.springbootobservability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HelloService {

    private final ObservationRegistry observationRegistry;

    public String hello() {
        Observation observation = observationRegistry.getCurrentObservation();
        observation.event(Observation.Event.of("Greeting to vlad sent"));
        observation.error(new IllegalArgumentException("Something went wrong"));
        return "Hello Vlad...";
    }

}
