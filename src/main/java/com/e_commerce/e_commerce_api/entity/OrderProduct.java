package com.e_commerce.e_commerce_api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "order_products", schema = "sales")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class OrderProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "OrderId", insertable = false, updatable = false)
    private Long orderId;

    @Column(name = "ProductChildrenId", insertable = false, updatable = false)
    private Long productChildrenId;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "SinglePrice")
    private BigDecimal singlePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderId", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductChildrenId", nullable = false)
    private ProductChildren productChildren;
}
