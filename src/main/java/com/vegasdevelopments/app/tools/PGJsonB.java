package com.vegasdevelopments.app.tools;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.postgresql.util.PGobject;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import java.util.Map;

@Convert
public final class PGJsonB implements AttributeConverter<Map<String, ?>, PGobject> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Map<String, ?> convertToEntityAttribute(PGobject dbData) {
        if (dbData == null || dbData.getValue() == null) {
            return null;
        }
        try {
            return MAPPER.readValue(dbData.getValue(), new TypeReference<Map<String, ?>>() {
            });
        } catch (Exception e) {
            System.out.println("Cannot convert JsonObject to PGobject.");
            return null;
        }
    }

    @Override
    public PGobject convertToDatabaseColumn(Map<String, ?> attribute) {
        final PGobject pGobject = new PGobject();
        pGobject.setType("jsonb");
        try {
            pGobject.setValue(attribute == null ? null : MAPPER.writeValueAsString(attribute));
            return pGobject;
        } catch (Exception e) {
            System.out.println("Cannot convert JsonObject to PGobject.");
            throw new IllegalStateException(e);
        }
    }
}
