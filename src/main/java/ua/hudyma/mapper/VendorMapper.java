package ua.hudyma.mapper;

import ua.hudyma.domain.users.Vendor;
import ua.hudyma.domain.users.VendorRespDto;

import java.util.List;

public interface VendorMapper {
    VendorRespDto toDto (Vendor vendor);
    Vendor toEntity (VendorRespDto dto);
    List<VendorRespDto> toDtoList (List<Vendor> vendorList);
    List<Vendor> toEntityList (List<VendorRespDto> vendorDtoslist);
}
