import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Test {
    public static void main(String[] args) throws Exception {
        String json = "{\"id\": 111,\"source\": {\"color\": \"red\",\"weight\": 100}}";
        Hit<Apple> appleHit = convert(json, Apple.class);
        System.out.println(appleHit);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Hit<T>{
        private Integer id;
        private T source;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Apple {
        private String color;
        private Integer weight;
    }

    public static <T> Hit<T> convert(String json, Class<T> targetClass) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        Hit<T> hit = null;

        JavaType clazzType = objectMapper.getTypeFactory().constructParametricType(Hit.class, targetClass);
        hit = objectMapper.readValue(json, clazzType);

        return hit;
    }
}
