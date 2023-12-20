package com.github.whatasame.aop;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AopLogger {

    private final List<String> logs = new ArrayList<>();

    public void log(final String log) {
        logs.add(log);
    }

    public void clear() {
        logs.clear();
    }

    public List<String> getLogs() {
        return logs;
    }
}
