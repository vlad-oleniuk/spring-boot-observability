package privatee.oleniuk.learn.springbootobservability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationHandler;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
public class HelloController {

    private final HelloService service;

    @GetMapping("/hello")
    @Observed(name = "HelloController.hello",
            contextualName = "saying-hello")
    public String hello() {
        log.info("Got a request");
        return service.hello();
    }

}
