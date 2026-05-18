package com.e_commerce.e_commerce_api.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colors", schema = "inventories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Color extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(length = 200, name = "Name")
    private String name;

    @Column(length = 200, name = "ColorCode")
    private String colorCode;

    @OneToMany(mappedBy = "color", fetch = FetchType.LAZY)
    private List<Product> products;
}
