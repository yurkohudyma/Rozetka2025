package ua.hudyma.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ua.hudyma.domain.orders.BuyerReqDto;
import ua.hudyma.domain.users.*;
import ua.hudyma.mapper.BuyerMapper;
import ua.hudyma.mapper.VendorMapper;
import ua.hudyma.repository.BuyerRepository;
import ua.hudyma.repository.VendorRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final BuyerRepository buyerRepository;
    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final BuyerMapper buyerMapper;

    public VendorRespDto createVendor (VendorReqDto dto) {
        var vendor = new Vendor();
        vendor.setVendorAddress(dto.vendorAddress());
        vendor.setVendorName(dto.vendorName());
        vendorRepository.save(vendor);
        log.info(":::: Vendor {} CREATED", vendor.getVendorCode());
        return vendorMapper.toDto(vendor);
    }

    public BuyerRespDto createBuyer (BuyerReqDto dto) {
        var buyer = new Buyer();
        buyer.setBuyerAddress(dto.buyerAddress());
        buyer.setFullName(dto.buyerFullName());
        buyerRepository.save(buyer);
        log.info(":::: Buyer {} CREATED", buyer.getBuyerCode());
        return buyerMapper.toDto(buyer);
    }
}
