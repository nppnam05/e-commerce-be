package com.e_commerce.e_commerce_api.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks", schema = "inventories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Stock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "ProductChildrenId", insertable = false, updatable = false)
    private Long productChildrenId;

    @Column(name = "Quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductChildrenId")
    private ProductChildren productChildren;
}
