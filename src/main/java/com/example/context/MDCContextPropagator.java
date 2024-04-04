package com.example.context;

import io.temporal.api.common.v1.Payload;
import io.temporal.common.context.ContextPropagator;
import io.temporal.common.converter.GlobalDataConverter;
import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MDCContextPropagator implements ContextPropagator {
    public String getName() {
        return this.getClass().getName();
    }

    public Object getCurrentContext() {
        Map<String, String> context = new HashMap<>();
        for (Map.Entry<String, String> entry : MDC.getCopyOfContextMap().entrySet()) {
            if (entry.getKey().startsWith("simple-")) {
                context.put(entry.getKey(), entry.getValue());
            }
        }
        return context;
    }

    public void setCurrentContext(Object context) {
        @SuppressWarnings("unchecked")
        Map<String, String> contextMap = (Map<String, String>) context;
        for (Map.Entry<String, String> entry : contextMap.entrySet()) {
            MDC.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<String, Payload> serializeContext(Object context) {
        @SuppressWarnings("unchecked")
        Map<String, String> contextMap = (Map<String, String>) context;
        Map<String, Payload> serializedContext = new HashMap<>();
        for (Map.Entry<String, String> entry : contextMap.entrySet()) {
            Optional<Payload> payload = GlobalDataConverter.get().toPayload(entry.getValue());
            payload.ifPresent(value -> serializedContext.put(entry.getKey(), value));
        }
        return serializedContext;
    }

    @Override
    public Object deserializeContext(Map<String, Payload> context) {
        Map<String, String> contextMap = new HashMap<>();
        for (Map.Entry<String, Payload> entry : context.entrySet()) {
            contextMap.put(entry.getKey(), GlobalDataConverter.get().fromPayload(entry.getValue(), String.class, String.class));
        }
        return contextMap;
    }
}
