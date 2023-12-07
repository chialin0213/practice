package com.example.demo.instant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class InstantController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    @Value("${time_zone}")
    private String timeZone ;

    @PostMapping(value = "/ms2date", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getInstant(@RequestBody ReqBody body) throws JsonProcessingException {

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("fdate", getDateTime(body.getMs()));
        respMap.put("zoneId", timeZone);

        ObjectMapper mapper = new ObjectMapper();

        return new ResponseEntity<>(mapper.writeValueAsString(respMap), HttpStatus.OK);
    }

    private String getDateTime(long ms) {
        Instant inst = Instant.ofEpochMilli(ms);
        ZonedDateTime zd = ZonedDateTime.ofInstant(inst, ZoneId.of(timeZone));
        return zd.format(dateTimeFormatter);
    }

}
