package com.gigamog.democitrine.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
public class Si {

    @JsonProperty("unit_name")
    private String unitName;

    @JsonProperty("multiplication_factor")
    @JsonSerialize(using = precisionDesirializer.class)
    private BigDecimal multiplicationFactor;

    public static class precisionDesirializer extends JsonSerializer<BigDecimal> {
        @Override
        public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException, JsonProcessingException {
            gen.writeNumber(value.setScale(14, BigDecimal.ROUND_UP).toPlainString());
        }

    }
}
