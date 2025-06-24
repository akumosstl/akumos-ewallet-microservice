package io.github.akumosstl.account.service.kafka;

import io.github.akumosstl.account.constants.AppConstants;
import io.github.akumosstl.account.dto.WalletResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafKaProducerService 
{
    private static final Logger logger = LoggerFactory.getLogger(KafKaProducerService.class);

    @Setter
    @Getter
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    public void sendMessage(WalletResponseDto message) {
        logger.info(String.format("Message sent -> %s", message));
        this.kafkaTemplate.send(AppConstants.TOPIC_NAME, message);
    }
}