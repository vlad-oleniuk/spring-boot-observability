package privatee.oleniuk.learn.springbootobservability;

import io.micrometer.observation.annotation.Observed;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

//@Component
public class KafkaListenerConfiguration {

    @KafkaListener(
            id = "hello-listener",
            topics = "hello-events",
            containerFactory = "observingListenerContainerFactory"
    )
    @Observed(
            name = "hello-listener",
            contextualName = "hello-listener"
    )
    public void listen(String in) {
        System.out.println("Got event from kafka: " + in);
    }

}
