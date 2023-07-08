package privatee.oleniuk.learn.springbootobservability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class HelloService {

    private final ObservationRegistry observationRegistry;


    private final KafkaTemplate<String, String> template;

    public String hello() {
        Observation observation = observationRegistry.getCurrentObservation();
        template.send("hello-events", "vlad", "sent hello to vlad " + Instant.now());
        observation.event(Observation.Event.of("Greeting to vlad sent"));
        return "Hello Vlad...";
    }

}
