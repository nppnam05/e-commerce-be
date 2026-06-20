package com.e_commerce.e_commerce_api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "orders", schema = "sales")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(name = "UserId", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "AddressId", insertable = false, updatable = false)
    private Long addressId;

    @Column(name = "Code")
    private String code;

    @Column(name = "TotalQuantity")
    private Integer totalQuantity;

    @Column(name = "TotalPrice")
    private BigDecimal totalPrice;

    @Column(name = "PaymentStatus", length = 10)
    private String paymentStatus;

    @Column(name = "PaymentLinkId", length = 255)
    private String paymentLinkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AddressId", nullable = false)
    private Address address;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<OrderProduct> orderProducts;
}
