package ua.hudyma.domain.users;

public record VendorRespDto (
    Long id,
    String vendorCode,
    String vendorName,
    String vendorAddress
    ){}
