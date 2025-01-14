package dk.apps.pcps.main.module.tapcash.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MqttJob<T> {
    private String job;
    private T payload;
}
