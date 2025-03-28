package agus.ramdan.cdt.gateway.itsjeck.service.transfer;

import agus.ramdan.cdt.base.dto.gateway.TransferServiceCode;
import agus.ramdan.cdt.core.gateway.controller.dto.transfer.TransferBalanceRequestDTO;
import agus.ramdan.cdt.core.gateway.controller.dto.transfer.TransferBalanceResponseDTO;
import agus.ramdan.cdt.gateway.itsjeck.service.bank.BankCodeService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Objects;

@Mapper(componentModel = "spring")
public abstract class TransferMapper {
    @Autowired
    private BankCodeService bankCodeService;

    @Named("formatAmount")
    public String formatAmount(BigDecimal amount) {
        if (amount == null) {
            return "0.00";
        }
        return amount.setScale(2, BigDecimal.ROUND_DOWN).toPlainString(); // 2 desimal, tanpa koma
    }
    @Named("mapToTransferServiceCode")
    public TransferServiceCode mapToTransferServiceCode(String transferServiceCode) {
        if (Objects.equals(transferServiceCode, "1")) {
            return TransferServiceCode.BI_FAST;
        }
        return TransferServiceCode.SMART_ROUTE;
    }
    @Named("mapToPayerBankCode")
    public Integer mapToPayerBankCode(String bankCode) {
        return bankCodeService.getPayerBankCode(bankCode);
    }

    @Mapping(target = "referenceId", source = "transactionNo")
//    @Mapping(target = "callbackUrl", source = "callbackUrl")
    //@Mapping(target = "balanceId")
    @Mapping(target = "payerId", source = "destinationBankCode", qualifiedByName = "mapToPayerBankCode")
    @Mapping(target = "mode", constant = "DESTINATION")
    @Mapping(target = "sender.firstname", source = "destinationAccountFirstname")
    @Mapping(target = "sender.lastname", source = "destinationAccountLastname")
    @Mapping(target = "sender.countryIsoCode", constant = "IDN")
    @Mapping(target = "sender.transferServiceCode", source = "transferType", qualifiedByName = "mapToTransferServiceCode")
    @Mapping(target = "source.amount", source = "amount", qualifiedByName = "formatAmount")
    @Mapping(target = "source.currency", constant = "IDR")
    @Mapping(target = "source.countryIsoCode", source = "destinationCountryCode")
    @Mapping(target = "destination.amount", source = "amount", qualifiedByName = "formatAmount")
    @Mapping(target = "destination.currency", constant = "IDR")
    @Mapping(target = "destination.countryIsoCode", source = "destinationCountryCode")
    @Mapping(target = "beneficiary.firstname", source = "destinationAccountFirstname")
    @Mapping(target = "beneficiary.lastname", source = "destinationAccountLastname")
    @Mapping(target = "beneficiary.countryIsoCode", source = "destinationCountryCode")
    @Mapping(target = "beneficiary.account", source = "destinationAccount")
    @Mapping(target = "compliance.sourceOfFunds", constant = "BUSINESS_INVESTMENT")
    @Mapping(target = "compliance.beneficiaryRelationships", constant = "MYSELF")
    @Mapping(target = "compliance.purposeOfRemittances", constant = "BUSINESS")
    @Mapping(target = "notes", source = "description")
    public abstract TransferRequest mapToTransferRequestDTO(TransferBalanceRequestDTO requestDTO);

    @Mapping(target = "transactionId", source = "data.id")
    @Mapping(target = "transactionNo", source = "data.referenceId")
//    @Mapping(target = "transactionDate", source = "data.createdAt")
//    @Mapping(target = "transactionStatus", source = "status")
//    @Mapping(target = "transactionAmount", source = "data.amount", qualifiedByName = "formatAmount")
    @Mapping(target = "transactionFee", source = "data.fee", qualifiedByName = "formatAmount")
//    @Mapping(target = "transactionRate", source = "data.rate", qualifiedByName = "formatAmount")
//    @Mapping(target = "transactionType", constant = "TRANSFER")
//    @Mapping(target = "transactionCurrency", source = "data.source.currency")
//    @Mapping(target = "transactionDestinationAccount", source = "data.destination.account")
//    @Mapping(target = "transactionDestinationAccountName", source = "data.beneficiary.firstname")
//    @Mapping(target = "transactionDestinationBank", source = "data.destination.bank")
//    @Mapping(target = "transactionDestinationBankCode", source = "data.destination.bankCode")
    //@Mapping(target = "state", source = "state")
    @Mapping(target = "description", source = "data.notes")
    @Mapping(target = "message", source = "message")
    public abstract TransferBalanceResponseDTO mapToTransferBalanceResponseDTO(TransferResponse response);
}
