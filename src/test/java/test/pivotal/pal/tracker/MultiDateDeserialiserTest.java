package test.pivotal.pal.tracker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import io.pivotal.pal.tracker.MultiDateDeserialiser;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiDateDeserialiserTest {

    @Test
    public void should_handle_iso_dates_correctly() throws IOException {
        JsonParser parser = new JsonFactory().createParser("\"2019-07-31\"");
        parser.nextToken();
        MultiDateDeserialiser deserialiser = new MultiDateDeserialiser();
        Object result = deserialiser.deserialize(parser, null);
        assertThat(result).isEqualTo(LocalDate.of(2019, 7, 31));
    }

    @Test
    public void should_handle_us_dates_correctly() throws IOException {
        JsonParser parser = new JsonFactory().createParser("\"07/04/2019\"");
        parser.nextToken();
        MultiDateDeserialiser deserialiser = new MultiDateDeserialiser();
        Object result = deserialiser.deserialize(parser, null);
        assertThat(result).isEqualTo(LocalDate.of(2019, 7, 4));
    }
}
