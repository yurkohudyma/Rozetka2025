package ua.hudyma.mapper;

import ua.hudyma.domain.users.Buyer;
import ua.hudyma.domain.users.BuyerRespDto;

import java.util.List;

public interface BuyerMapper {
    BuyerRespDto toDto (Buyer buyer);
    Buyer toEntity (BuyerRespDto dto);
    List<BuyerRespDto> toDtoList (List<Buyer> buyerList);
    List<Buyer> toEntityList (List<BuyerRespDto> buyerDtoslist);
}
