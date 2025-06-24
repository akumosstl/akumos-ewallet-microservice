package io.github.akumosstl.event.service.kafka;

import io.github.akumosstl.event.constants.AppConstants;
import io.github.akumosstl.event.dto.WalletResponseDto;
import io.github.akumosstl.event.model.TransactionLogs;
import io.github.akumosstl.event.service.OpenSearchService;
import io.github.akumosstl.event.service.TransactionLogsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;


@Service
public class KafKaConsumerService {

    @Autowired
    private TransactionLogsService logsService;

    //TODO need better CPU an RAM to use it
    //@Autowired
    //private OpenSearchService openSearchService;

    private final Logger logger = LoggerFactory.getLogger(KafKaConsumerService.class);

    @KafkaListener(topics = AppConstants.FUNDING_TOPIC_NAME, groupId = AppConstants.FUNDING_GROUP_ID)
    public void consumeFundSuccessDetails(WalletResponseDto message) {
        logger.info(String.format("Funding Transaction Message recieved -> %s", message));

        saveDepositTransactionLogsToDatabase(message);
        sendFundingNotificationMessage(message);

    }

    @KafkaListener(topics = AppConstants.WITHDRAWAL_TOPIC_NAME, groupId = AppConstants.WITHDRAWAL_GROUP_ID)
    public void consumeWithdrawalSuccessDetails(WalletResponseDto message) {
        logger.info(String.format("Withdrawal Transaction Message recieved -> %s", message));

        saveWithdrawalTransactionLogsToDatabase(message);
        sendWithdrawalNotificationMessage(message);

    }

    @KafkaListener(topics = AppConstants.TRANSFER_TOPIC_NAME, groupId = AppConstants.TRANSFER_GROUP_ID)
    public void consumeTransferSuccessDetails(WalletResponseDto message) {
        logger.info(String.format("Transfer Transaction Message recieved -> %s", message));

        saveTransferTransactionLogsToDatabase(message);
        sendTransferNotificationMessage(message);

    }

    @KafkaListener(topics = AppConstants.ACCOUNT_TOPIC_NAME, groupId = AppConstants.ACCOUNT_GROUP_ID)
    public void consumeAccountNotifications(WalletResponseDto message) {
        sendAccountSuccessMessage(message);
    }


    public void sendAccountSuccessMessage(WalletResponseDto message) {
        logger.info("Account Message recieved" + message);
        String recipient = message.getEmail();
        String subject = "NEW WALLET ACCOUNT";
        String messageBody = "Dear " + message.getFirstName() + "\n"
                + "Your wallet account has been successfully created. Please find below the account details" + "\n \n"
                + "Date Created: " + message.getDate() + "\n"
                + "Wallet Address: " + message.getWalletAddress() + "\n"
                + "Wallet Balance: " + message.getWalletBalance() + "\n"
                + "surname: " + message.getSurname() + "\n"
                + "firstName: " + message.getFirstName() + "\n"
                + "lastName: " + message.getLastName() + "\n"
                + "email: " + message.getEmail() + "\n"
                + "phone: " + message.getPhone() + "\n"
                + "accountNo: " + message.getAccountNo() + "\n";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", recipient);
        map.put("topic", subject);
        map.put("body", messageBody);

        //TODO need better CPU an RAM to use it
        //openSearchService.persist(map);

    }

    public void sendFundingNotificationMessage(WalletResponseDto message) {
        logger.info("Funding Account Message recieved" + message);
        String recipient = message.getEmail();
        String subject = "ACCOUNT DEPOSIT NOTIFICATION";
        String messageBody = "Dear " + message.getFirstName() + "\n"
                + "Your wallet account has been successfully funded. Please find the details below" + "\n \n"
                + "Transaction Date: " + message.getDate() + "\n"
                + "Wallet Address" + message.getWalletAddress() + "\n"
                + "Amount Funded" + message.getAmount() + "\n"
                + "Wallet Balance" + message.getWalletBalance() + "\n"
                + "surname" + message.getSurname() + "\n"
                + "First Name" + message.getFirstName() + "\n"
                + "Last Name" + message.getLastName() + "\n"
                + "email" + message.getEmail() + "\n"
                + "phone" + message.getPhone() + "\n"
                + "Account No" + message.getAccountNo() + "\n";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", recipient);
        map.put("topic", subject);
        map.put("body", messageBody);

        //TODO need better CPU an RAM to use it
        //openSearchService.persist(map);

    }

    public void sendWithdrawalNotificationMessage(WalletResponseDto message) {
        logger.info("Account Withdrawal Message recieved" + message);
        String recipient = message.getEmail();
        String subject = "ACCOUNT WITHDRAWAL NOTIFICATION";
        String messageBody = "Dear " + message.getFirstName() + "\n"
                + "Your wallet account has been debited. Please find the details below" + "\n \n"
                + "Wallet Address" + message.getWalletAddress() + "\n"
                + "Amount Withdrawn" + message.getAmount() + "\n"
                + "Wallet Balance" + message.getWalletBalance() + "\n"
                + "surname" + message.getSurname() + "\n"
                + "First Name" + message.getFirstName() + "\n"
                + "Last Name" + message.getLastName() + "\n"
                + "email" + message.getEmail() + "\n"
                + "phone" + message.getPhone() + "\n"
                + "Account No" + message.getAccountNo() + "\n";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", recipient);
        map.put("topic", subject);
        map.put("body", messageBody);

        //TODO need better CPU an RAM to use it
        //openSearchService.persist(map);

    }

    public void sendTransferNotificationMessage(WalletResponseDto message) {
        logger.info("Account Message recieved" + message);
        String recipient = message.getEmail();
        String subject = "TRANSFER REQUEST NOTIFICATION";
        String messageBody = "Dear " + message.getFirstName() + "\n"
                + "Your wallet transfer request is successful. Please find the details below" + "\n \n"
                + "Wallet Address" + message.getWalletAddress() + "\n"
                + "Amount Transferred" + message.getAmount() + "\n"
                + "Wallet Balance" + message.getWalletBalance() + "\n"
                + "surname" + message.getSurname() + "\n"
                + "First Name" + message.getFirstName() + "\n"
                + "Last Name" + message.getLastName() + "\n"
                + "email" + message.getEmail() + "\n"
                + "phone" + message.getPhone() + "\n"
                + "Account No" + message.getAccountNo() + "\n";

        HashMap<String, Object> map = new HashMap<>();
        map.put("email", recipient);
        map.put("topic", subject);
        map.put("body", messageBody);

        //TODO need better CPU an RAM to use it
        //openSearchService.persist(map);

    }

    void saveDepositTransactionLogsToDatabase(WalletResponseDto data) {
        TransactionLogs logs = new TransactionLogs();
        logs.setDate(data.getDate());
        logs.setAccountNo(data.getAccountNo());
        logs.setAmount(data.getAmount());
        logs.setTransactionType("DEPOSIT");
        logs.setWalletAddress(data.getWalletAddress());

        logsService.saveTransactionLogs(logs);
    }

    void saveWithdrawalTransactionLogsToDatabase(WalletResponseDto data) {
        TransactionLogs logs = new TransactionLogs();
        logs.setDate(data.getDate());
        logs.setAccountNo(data.getAccountNo());
        logs.setAmount(data.getAmount());
        logs.setTransactionType("WITHDRAWAL");
        logs.setWalletAddress(data.getWalletAddress());

        logsService.saveTransactionLogs(logs);
    }

    void saveTransferTransactionLogsToDatabase(WalletResponseDto data) {
        TransactionLogs logs = new TransactionLogs();
        logs.setDate(data.getDate());
        logs.setAccountNo(data.getAccountNo());
        logs.setAmount(data.getAmount());
        logs.setTransactionType("TRANSFER");
        logs.setWalletAddress(data.getWalletAddress());

        logsService.saveTransactionLogs(logs);
    }

}