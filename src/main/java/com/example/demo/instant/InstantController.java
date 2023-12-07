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
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class InstantController {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${time_zone}")
    private String timeZone ;

    @PostMapping(value = "/ms2date", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getDate(@RequestBody ReqBody body) throws JsonProcessingException {

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("fdate", getDateTime(body.getMs(), 0));
        respMap.put("zoneId", timeZone);

        return new ResponseEntity<>(mapper.writeValueAsString(respMap), HttpStatus.OK);
    }

    /**
     * 使用 Spring Converter 去做日期轉換
     * @param ms
     * @param hours
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping(value = "/msbyparam", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMinusDate(@RequestParam("ms") Instant ms,
                                               @RequestParam("hours") int hours) throws JsonProcessingException {

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("fdate", getDateTime(ms, hours));
        respMap.put("zoneId", timeZone);

        return new ResponseEntity<>(mapper.writeValueAsString(respMap), HttpStatus.OK);
    }

    /**
     * 透過Jackson 的 converter 反序列化方式，對欄位做轉換
     * @RequestBody 無法吃 Spring Converter
     * @param body
     * @return
     * @throws JsonProcessingException
     */
    @PostMapping(value = "/msbybody", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMinusDate2(@RequestBody ReqBody body) throws JsonProcessingException {

        Map<String, Object> respMap = new HashMap<>();
        respMap.put("fdate", getDateTime(body.getMs(), body.getMinus_hours()));
        respMap.put("zoneId", timeZone);

        return new ResponseEntity<>(mapper.writeValueAsString(respMap), HttpStatus.OK);
    }

    private String getDateTime(Instant ms, int hours) {
        Instant minus = ms.minus(hours, ChronoUnit.HOURS);
        ZonedDateTime zd = ZonedDateTime.ofInstant(minus, ZoneId.of(timeZone));
        return zd.format(dateTimeFormatter);
    }
}
