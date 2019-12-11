package com.havryliv.auction.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"id", "name"}, callSuper = false)
@Builder
@Entity
@Document(indexName = "products", type = "product")
public class Product extends BaseEntityAudit {

    @Id
    @GeneratedValue(generator = "product_generator", strategy = GenerationType.AUTO)
    @SequenceGenerator(
            name = "product_generator",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @Column(nullable = false)
    protected Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false, length = 123)
    private String description;

    private BigDecimal price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    @Field(type = FieldType.Nested, includeInParent = true)
    private User user;

    private LocalDate expireTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Bid> bids = new ArrayList<>();

    private String image;

    private Double rating;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Review> reviews = new ArrayList<>();

}
