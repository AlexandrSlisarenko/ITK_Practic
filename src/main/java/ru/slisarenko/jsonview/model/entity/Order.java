package ru.slisarenko.jsonview.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.slisarenko.jsonview.model.enums.StatusOrder;


@Entity
@Table(name = "customer_order")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements BaseEntity<Long>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private StatusOrder status;

    @Getter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "totalprice", nullable = false)
    @Getter
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    public void addProduct(Product product) {
        this.products.add(product);
        this.totalPrice.add(product.getPrice());
        product.setOrder(this);
    }
}
