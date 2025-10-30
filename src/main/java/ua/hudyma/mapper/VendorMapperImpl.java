package ua.hudyma.mapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ua.hudyma.domain.users.Vendor;
import ua.hudyma.domain.users.VendorRespDto;

import java.util.List;

@Component
public class VendorMapperImpl implements VendorMapper {
    @Override
    public VendorRespDto toDto(Vendor vendor) {
        return new VendorRespDto(
                vendor.getId(),
                vendor.getVendorCode(),
                vendor.getVendorName(),
                vendor.getVendorAddress());
    }

    @Override
    public Vendor toEntity(VendorRespDto dto) {
        var vendor = new Vendor();
        vendor.setId(dto.id());
        vendor.setVendorAddress(dto.vendorAddress());
        vendor.setVendorCode(dto.vendorCode());
        vendor.setVendorName(dto.vendorName());
        return vendor;
    }

    @Override
    public List<VendorRespDto> toDtoList(List<Vendor> vendorList) {
        return vendorList.stream().map(this::toDto).toList();
    }

    @Override
    public List<Vendor> toEntityList(List<VendorRespDto> vendorDtoslist) {
        return vendorDtoslist.stream().map(this::toEntity).toList();
    }
}
