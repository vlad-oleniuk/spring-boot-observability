package privatee.oleniuk.learn.springbootobservability;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.transport.Propagator;
import io.micrometer.observation.transport.ReceiverContext;
import io.micrometer.observation.transport.SenderContext;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HelloService {

    private final ObservationRegistry observationRegistry;

    public static class TaskSchedulerContext extends SenderContext {

        public TaskSchedulerContext(TaskData taskData) {
            super(new TaskDataTraceContextSetter());
            setCarrier(taskData);
        }
    }

    public static class TaskExecutionContext extends ReceiverContext {

        public TaskExecutionContext(TaskData taskData) {
            super(new TaskDataTraceContextGetter());
            setCarrier(taskData);
        }
    }

    public static class TaskDataTraceContextSetter implements Propagator.Setter<TaskData> {

        @Override
        public void set(TaskData carrier, String key, String value) {
            carrier.put(key, value);
        }
    }

    public static class TaskDataTraceContextGetter implements Propagator.Getter<TaskData> {

        @Override
        public String get(TaskData carrier, String key) {
            return carrier.get(key);
        }
    }

    /**
     * carrier
     */
    public static class TaskData {
        private Map<String, String> traceStorage = new HashMap<>();

        public void put(String key, String value) {
            System.out.printf("Setting trace data: %s -> %s\n", key, value);
            traceStorage.put(key, value);
        }

        public String get(String key) {
            return traceStorage.get(key);
        }

    }


    private final KafkaTemplate<String, String> template;

    public String hello() {
        //let's pretend it was transmitted over wire
        TaskData taskData= new TaskData();

        Observation outAdapterObservation = Observation
                .createNotStarted("hello-in-out-adapter",
                        () -> new TaskSchedulerContext(taskData),
                        observationRegistry);

        outAdapterObservation.start();

        Thread thread = new Thread(() -> {

            try {
                Thread.sleep(5_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            Observation inAdapterObservation = Observation
                    .createNotStarted("hello-in-incoming-adapter",
                            () -> new TaskExecutionContext(taskData),
                            observationRegistry);
            try {
                inAdapterObservation.start();
                template.send("hello-events", "vlad", "sent hello to vlad " + Instant.now());
                inAdapterObservation.event(Observation.Event.of("Greeting from thread"));
                System.out.println("Hello in thread");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                inAdapterObservation.stop();
            }
        });

        thread.start();
        outAdapterObservation.event(Observation.Event.of("Greeting to vlad sent"));
        outAdapterObservation.stop();
        return "Hello Vlad...";
    }

}
