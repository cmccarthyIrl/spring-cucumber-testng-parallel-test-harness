package com.cmccarthy.common.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.boolex.JaninoEventEvaluator;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.filter.EvaluatorFilter;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;
import ch.qos.logback.core.status.NopStatusListener;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogManager extends ForegroundCompositeConverterBase<ILoggingEvent> {

    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        Level level = event.getLevel();
        return switch (level.toInt()) {
            case Level.ERROR_INT -> ANSIConstants.BOLD + ANSIConstants.RED_FG; // same as default color scheme
            case Level.WARN_INT -> ANSIConstants.RED_FG;// same as default color scheme
            case Level.INFO_INT -> ANSIConstants.CYAN_FG; // use CYAN instead of BLUE
            default -> ANSIConstants.DEFAULT_FG;
        };
    }

    private final static ThreadLocal<Logger> logFactory = new ThreadLocal<>();

    public void createNewLogger(String className) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final NopStatusListener nopStatusListener = new NopStatusListener();
        loggerContext.getStatusManager().add(nopStatusListener);

        final JaninoEventEvaluator janinoEventEvaluator = new JaninoEventEvaluator();
        janinoEventEvaluator.setExpression("logger.contains('com.cmccarthy')");
        janinoEventEvaluator.setContext(loggerContext);
        final EvaluatorFilter<ILoggingEvent> evaluatorFilter = new EvaluatorFilter<>();
        evaluatorFilter.setEvaluator(janinoEventEvaluator);

        final FileAppender<ILoggingEvent> fileAppender = fileAppender(className, loggerContext, evaluatorFilter);
        fileAppender.start();

        final ConsoleAppender<ILoggingEvent> consoleAppender = consoleAppender(loggerContext, evaluatorFilter);
        consoleAppender.start();

        final Logger logger = (Logger) LoggerFactory.getLogger(className);
        logger.addAppender(fileAppender);
        logger.addAppender(consoleAppender);

        logger.setLevel(Level.DEBUG);
        logger.setAdditive(false);
        logFactory.set(logger);
    }

    private FileAppender<ILoggingEvent> fileAppender(String className, LoggerContext loggerContext, EvaluatorFilter<ILoggingEvent> evaluatorFilter) {
        final PatternLayout filePattern = new PatternLayout();
        filePattern.setContext(loggerContext);
        filePattern.setPattern("[%d{ISO8601}] %-5level [%logger{100}]: %msg%n%throwable");
        filePattern.start();

        final PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setPattern(filePattern.getPattern());
        encoder.setContext(loggerContext);
        encoder.start();

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile("logs/" + className + ".log");
        fileAppender.setAppend(false);
        fileAppender.setEncoder(encoder);
        fileAppender.setContext(loggerContext);
        fileAppender.addFilter(evaluatorFilter);

        return fileAppender;
    }

    private ConsoleAppender<ILoggingEvent> consoleAppender(LoggerContext loggerContext, EvaluatorFilter<ILoggingEvent> evaluatorFilter) {
        final PatternLayout consolePattern = new PatternLayout();
        consolePattern.setContext(loggerContext);
        consolePattern.setPattern("%blue([%d{ISO8601}]) %highlight(%colourPicker(%-5level)) %blue([%logger{100}]:) %colourPicker(%msg%n%throwable)");
        consolePattern.start();

        final PatternLayoutEncoder consoleEncoder = new PatternLayoutEncoder();
        consoleEncoder.setPattern(consolePattern.getPattern());
        consoleEncoder.setContext(loggerContext);
        consoleEncoder.start();

        final ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setEncoder(consoleEncoder);
        consoleAppender.setContext(loggerContext);
        consoleAppender.addFilter(evaluatorFilter);
        return consoleAppender;
    }

    private static Logger getLogger() {
        return logFactory.get();
    }

    public void info(String value) {
        getLogger().info(value);
    }

    public void debug(String value) {
        getLogger().debug(value);
    }

    public void warn(String value) {
        getLogger().warn(value);
    }

    public void error(String value) {
        getLogger().error(value);
    }
}
