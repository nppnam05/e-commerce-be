package com.e_commerce.e_commerce_api.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts", schema = "sales")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "UserId", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "ProductChildrenId", insertable = false, updatable = false)
    private Long productChildrenId;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "SinglePrice")
    private BigDecimal singlePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductChildrenId", nullable = false)
    private ProductChildren productChildren;
}
