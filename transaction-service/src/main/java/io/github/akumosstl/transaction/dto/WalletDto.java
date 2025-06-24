package io.github.akumosstl.transaction.dto;

import io.github.akumosstl.transaction.model.Account;
import lombok.Data;

import java.io.Serializable;

@Data
public class WalletDto implements Serializable {

    private Long walletId;

    private String walletAddress;

    private Double walletBalance;

    private Account AccountId;
}
