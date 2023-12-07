package com.example.demo.instant;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqBody {
    @JsonDeserialize(converter = JacksonConverter.class)
    private Instant ms ;
    private int minus_hours ;
}
