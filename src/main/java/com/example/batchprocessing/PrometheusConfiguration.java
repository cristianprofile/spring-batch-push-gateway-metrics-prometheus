package com.example.batchprocessing;



import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.PostConstruct;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import io.prometheus.client.exporter.PushGateway;

@Configuration
public class PrometheusConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrometheusConfiguration.class);

    @Value("${prometheus.job.name}")
    private String prometheusJobName;

    @Value("${prometheus.grouping.key}")
    private String prometheusGroupingKey;

    @Value("${prometheus.pushgateway.url}")
    private String prometheusPushGatewayUrl;

    private Map<String, String> groupingKey = new HashMap<>();

    private PushGateway pushGateway;

    private CollectorRegistry collectorRegistry;



@Bean
    String init(PrometheusMeterRegistry prometheusMeterRegistry)
    {
        pushGateway = new PushGateway(prometheusPushGatewayUrl);
        groupingKey.put(prometheusGroupingKey, prometheusJobName);
        collectorRegistry = prometheusMeterRegistry.getPrometheusRegistry();
        Metrics.globalRegistry.add(prometheusMeterRegistry);
        return "adsasdad";
    }


    @Scheduled(fixedRateString = "100")
    public void pushMetrics() {
        try {
            LOGGER.info("*** Sending info push");
            pushGateway.pushAdd(collectorRegistry, prometheusJobName, groupingKey);
        }
        catch (Throwable ex) {
            LOGGER.error("Unable to push metrics to Prometheus Push Gateway", ex);
        }
    }

}
