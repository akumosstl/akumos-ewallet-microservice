package io.github.akumosstl.event.config;

import org.apache.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//TODO need better CPU an RAM to use it
//@Configuration
public class OpenSearchConfig {

    @Value("${open.search.host}")
    private String openSearchHost;

    @Value("${open.search.port}")
    private int openSearchPort;

    //@Bean(destroyMethod = "close")
    public RestHighLevelClient openSearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(openSearchHost, openSearchPort, "http"))
        );
    }
}

