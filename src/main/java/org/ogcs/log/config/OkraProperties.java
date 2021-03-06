/*
 *     Copyright 2016-2026 TinyZ
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ogcs.log.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Okra Properties.
 *
 * @author TinyZ
 * @date 2016-07-07.
 */
public final class OkraProperties {

    private static final Logger LOG = LogManager.getLogger(OkraProperties.class);
    private static final String DEFAULT_CONFIG_PATH = "./config/config.properties";
    private static OkraConfig okraConfig;

    public static int LOG_PORT = 9005;
    public static long LOG_TASK_INTERVAL = 5000L;
    public static int LOG_MAX_BATCH_SIZE = 100;
    public static String LOG_PATH = "./config/aolog.xml";
    public static char LOG_SEPARATOR = '|';
    public static String LOG_XSD_PATH = "/okra-log.xsd";
    public static int LOG_RING_BUFFER_SIZE = 16;
    public static String DATABASE_JDBC_URL = "";
    public static String DATABASE_USER = "";
    public static String DATABASE_PSW = "";
    public static String HIKARI_CONFIG_PATH = "./config/hikari.properties";

    static {
        load();
    }

    /**
     * Load properties.
     */
    public static void load() {
        String filepath = System.getProperty("okra.config.path", DEFAULT_CONFIG_PATH);
        try {
            InputStream is = new FileInputStream(filepath);
            Properties prop = new Properties();
            prop.load(is);
            // get
            HIKARI_CONFIG_PATH = prop.getProperty("okra.hikari.path", HIKARI_CONFIG_PATH);

            DATABASE_JDBC_URL = prop.getProperty("okra.db.jdbcUrl", DATABASE_JDBC_URL);
            DATABASE_USER = prop.getProperty("okra.db.username", DATABASE_USER);
            DATABASE_PSW = prop.getProperty("okra.db.password", DATABASE_PSW);

            LOG_PATH = prop.getProperty("okra.log.path", LOG_PATH);
            LOG_XSD_PATH = prop.getProperty("okra.log.xsd.path", LOG_XSD_PATH);
            LOG_SEPARATOR = prop.getProperty("okra.log.separator", String.valueOf(LOG_SEPARATOR)).charAt(0);
            LOG_RING_BUFFER_SIZE = Integer.valueOf(prop.getProperty("okra.log.rb.size", String.valueOf(LOG_RING_BUFFER_SIZE)));
            LOG_PORT = Integer.valueOf(prop.getProperty("okra.log.port", String.valueOf(LOG_PORT)));
            LOG_TASK_INTERVAL = Long.valueOf(prop.getProperty("okra.log.task.interval", String.valueOf(LOG_TASK_INTERVAL)));
            LOG_MAX_BATCH_SIZE = Integer.valueOf(prop.getProperty("okra.log.max.batchSize", String.valueOf(LOG_MAX_BATCH_SIZE)));
            // set
            System.setProperty("okra.log.xsd.path", LOG_XSD_PATH);
            LOG.error("Load config successful.");
        } catch (Exception e) {
            LOG.error("Load config failure.", e);
        }
    }

    /**
     * @return Return Okra Config.
     * @see {@link OkraConfig}
     */
    public static OkraConfig getConfig() {
        if (okraConfig != null) {
            return okraConfig;
        }
        okraConfig = new OkraConfig(
                LOG_PORT, HIKARI_CONFIG_PATH, DATABASE_JDBC_URL, DATABASE_USER, DATABASE_PSW,
                LOG_RING_BUFFER_SIZE, LOG_XSD_PATH, LOG_PATH, LOG_SEPARATOR, LOG_TASK_INTERVAL,
                LOG_MAX_BATCH_SIZE
        );
        return okraConfig;
    }
}
