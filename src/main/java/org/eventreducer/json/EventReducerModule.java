package org.eventreducer.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.net.ntp.TimeStamp;
import org.eventreducer.Serializable;
import org.eventreducer.Serializer;

import java.io.IOException;

public class EventReducerModule extends SimpleModule {

    public EventReducerModule() {
        super("EventReducerModule", new Version(1,0,0,null, "org.eventreducer.json", "EventReducerModule"));

        addSerializer(new TimestampSerializer());
        addDeserializer(TimeStamp.class, new TimestampDeserializer());
        setMixInAnnotation(Serializable.class, SerializableMixin.class);
    }

    public static abstract class SerializableMixin {

        @JsonIgnore
        private Serializer serializer;

        @JsonProperty("@hash")
        public abstract <T extends Serializable> Serializer<T> entitySerializer() throws ClassNotFoundException, IllegalAccessException, InstantiationException;

    }


    static class TimestampSerializer extends StdSerializer<TimeStamp> {

        protected TimestampSerializer() {
            super(TimeStamp.class);
        }

        @Override
        public void serialize(TimeStamp value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(value.toString());
        }

        @Override
        public void serializeWithType(TimeStamp value, JsonGenerator gen, SerializerProvider serializers, TypeSerializer typeSer) throws IOException {
            gen.writeString(value.toString());
        }
    }

    static class TimestampDeserializer extends StdDeserializer<TimeStamp> {
        public TimestampDeserializer() {
            super(TimeStamp.class);
        }

        @Override
        public TimeStamp deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new TimeStamp(p.readValueAs(String.class));
        }

        @Override
        public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
            return new TimeStamp(p.readValueAs(String.class));
        }
    }

}