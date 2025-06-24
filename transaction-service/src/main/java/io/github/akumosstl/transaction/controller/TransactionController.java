package io.github.akumosstl.transaction.controller;


import io.github.akumosstl.transaction.dto.*;
import io.github.akumosstl.transaction.service.WalletService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/wallet")
public class TransactionController {

    @Autowired
    WalletService walletService;

    @PostMapping("/fund")
    @ApiOperation(value = "Fund Wallet",notes = "Add funds to wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> fundWallet(@Valid @RequestBody FundWalletRequestDto data) {
        return ResponseEntity.ok().body(walletService.fundWallet(data.getAmount(), data.getWalletAddress()));
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "Withdraw Funds",notes = "Withdraw funds from wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> withdrawFromWallet(@Valid @RequestBody WithdrawWalletRequestDto data){
        return ResponseEntity.ok().body(walletService.withdrawFromWallet(data.getAmount(), data.getWalletAddress()));
    }

    @PostMapping("/transfer")
    @ApiOperation(value = "Withdraw Funds",notes = "Withdraw funds from wallet account")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 401, message = "Not Authorized!"),
            @ApiResponse(code = 403, message = "Forbidden!"),
            @ApiResponse(code = 404, message = "Not Found!"),
            @ApiResponse(code = 201, message = "Created", response = WalletDto.class) })
    public ResponseEntity<ApiResponseDto> transferFromWalletToWallet(@Valid @RequestBody WalletTransferRequestDto data){
        return ResponseEntity.ok().body(walletService.transferToWallet(data.getAmount(), data.getSourceWalletAddress(), data.getDestinationWalletAddress()));
    }


}
