package ua.hudyma.mapper;

import org.springframework.stereotype.Component;
import ua.hudyma.domain.users.Buyer;
import ua.hudyma.domain.users.BuyerRespDto;

import java.util.List;

@Component
public class BuyerMapperImpl implements BuyerMapper {
    @Override
    public BuyerRespDto toDto(Buyer buyer) {
        return new BuyerRespDto(
                buyer.getId(),
                buyer.getBuyerCode(),
                buyer.getFullName(),
                buyer.getBuyerAddress());
    }

    @Override
    public Buyer toEntity(BuyerRespDto dto) {
        var buyer = new Buyer();
        buyer.setId(dto.id());
        buyer.setBuyerAddress(dto.buyerAddress());
        buyer.setBuyerCode(dto.buyerCode());
        buyer.setFullName(dto.buyerName());
        return buyer;
    }

    @Override
    public List<BuyerRespDto> toDtoList(List<Buyer> buyerList) {
        return buyerList.stream().map(this::toDto).toList();
    }

    @Override
    public List<Buyer> toEntityList(List<BuyerRespDto> buyerDtoslist) {
        return buyerDtoslist.stream().map(this::toEntity).toList();
    }
}
