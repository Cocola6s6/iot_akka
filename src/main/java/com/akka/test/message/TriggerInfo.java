package com.akka.test.message;

import com.akka.test.message.enums.TriggerType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;


@Data
@Builder
@ToString
public class TriggerInfo {
    private TriggerType type;
    private String id;
}
