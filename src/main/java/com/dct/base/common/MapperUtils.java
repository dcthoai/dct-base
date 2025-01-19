package com.dct.base.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class MapperUtils {

    private static final Logger log = LoggerFactory.getLogger(MapperUtils.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public MapperUtils() {
        objectMapper.registerModule(new JavaTimeModule()); // To support Instant datatype of Java 8
    }

    public static <T> T mapObject(Object object, Class<T> classType) {
        try {
            return objectMapper.convertValue(object, classType);
        } catch (IllegalArgumentException e) {
            log.error("Cannot mapper object: {}", e.getMessage());

            try {
                return classType.getDeclaredConstructor().newInstance();
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException exception) {
                log.error("Cannot invoke new instance when mapper failed: {}", exception.getMessage());
            }
        }

        return null;
    }
}
