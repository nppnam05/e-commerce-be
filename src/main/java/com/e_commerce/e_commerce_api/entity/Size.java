package com.e_commerce.e_commerce_api.entity;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sizes", schema = "inventories")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Size extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "Id")
    private Long id;

    @Column(length = 200, name = "Name")
    private String name;

    @Column(columnDefinition = "TEXT", name = "Description")
    private String description;

    @OneToMany(mappedBy = "size", fetch = FetchType.LAZY)
    private List<ProductChildren> productChildren;
}
