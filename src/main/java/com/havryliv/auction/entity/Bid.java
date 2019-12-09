package com.havryliv.auction.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "time"}, callSuper = false)
@Entity
public class Bid extends BaseEntityAudit {

    private BigDecimal price;

    private LocalDateTime time;

    @Id
    @GeneratedValue(generator = "bid_generator",  strategy = GenerationType.AUTO )
    @SequenceGenerator(
            name = "bid_generator",
            sequenceName = "bid_sequence",
            allocationSize=1
    )
    @Column(nullable = false)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BIDDER_ID", nullable = false)
    private User bidder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

}
