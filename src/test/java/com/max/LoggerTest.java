package com.max;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class);
    @Test
    public void testLogger() {
        LOGGER.info("info");

    }
}
