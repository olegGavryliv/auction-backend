package com.havryliv.auction.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "time"}, callSuper = false)
@Builder
@Entity
public class Review extends BaseEntityAudit {

    @Id
    @GeneratedValue(generator = "review_generator", strategy = GenerationType.AUTO )
    @SequenceGenerator(
            name = "review_generator",
            sequenceName = "review_sequence",
            allocationSize=1
    )
    @Column(nullable = false)
    protected Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    private LocalDateTime time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "REVIEWER_ID", nullable = false)
    private User reviewer;

    private Double rating;

    private String comment;

}
