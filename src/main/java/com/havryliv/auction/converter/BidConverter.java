package com.havryliv.auction.converter;

import com.havryliv.auction.dto.BidDTO;
import com.havryliv.auction.entity.Bid;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;


@UtilityClass
public class BidConverter {

    public Bid fromDTOtoEntity(BidDTO bidDTO) {
        Bid bid = new Bid();
        bid.setPrice(bidDTO.getPrice());
        bid.setTime(LocalDateTime.now());
        return bid;
    }

    public BidDTO fromEntityToDTO(Bid bid) {
        BidDTO bidDTO = new BidDTO();
        bidDTO.setPrice(bid.getPrice());
        bidDTO.setTime(bid.getTime());
        bidDTO.setSuccess(true);
        return bidDTO;
    }
}
