package io.github.akumosstl.event.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.akumosstl.event.service.kafka.KafKaConsumerService;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.action.index.IndexResponse;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.opensearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

//TODO need more ram and cpu to implement it
//@Service
public class OpenSearchService {

    private final Logger logger = LoggerFactory.getLogger(OpenSearchService.class);

    //@Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public void persist(Map<String, Object> data) {
        String id = UUID.randomUUID().toString();
        IndexRequest request = null;
        try {
            request = new IndexRequest("messages")
                    .id(id)
                    .source(objectMapper.writeValueAsString(data), XContentType.JSON);
            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            logger.info("OpenSearchService ok: " + response.toString());
        } catch (IOException e) {
            logger.error("Error at OpenSearchService: " + e.getMessage());
        }

    }
}

