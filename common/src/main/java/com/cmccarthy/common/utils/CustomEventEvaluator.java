package com.cmccarthy.common.utils;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.boolex.EventEvaluatorBase;

public class CustomEventEvaluator extends EventEvaluatorBase<ILoggingEvent> {

    @Override
    public boolean evaluate(ILoggingEvent event) {
        // Check if logger name contains 'com.cmccarthy'
        return event.getLoggerName().contains("com.cmccarthy");
    }
}
