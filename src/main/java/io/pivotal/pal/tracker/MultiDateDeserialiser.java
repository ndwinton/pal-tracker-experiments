package io.pivotal.pal.tracker;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MultiDateDeserialiser extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        String dateString = parser.getText();
        if (dateString.matches("\\d{2}/\\d{2}/\\d{4}")) {
            return LocalDate.parse(dateString, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } else {
            return LocalDate.parse(dateString);
        }
    }
}
