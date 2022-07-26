package com.youngjo.ssg.domain.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youngjo.ssg.domain.product.domain.MainProduct;
import com.youngjo.ssg.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NormalCart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "normal_cart_id")
    private Long id;
    private Integer pdtQty;

    //==매핑==
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_product_id")
    private MainProduct mainProduct;

    @Builder
    public NormalCart(Integer pdtQty) {
        this.pdtQty = pdtQty;
    }

    public NormalCart linkToUser(User user) {
        this.user = user;
        user.linkToNormalCart(this);
        return this;
    }

    public NormalCart linkToMainProduct(MainProduct mainProduct) {
        this.mainProduct = mainProduct;
        mainProduct.linkToNormalCart(this);
        return this;
    }

    public NormalCart setPdtQty(Integer pdtQty) {
        this.pdtQty = pdtQty;
        return this;
    }

    public NormalCart addPdtQty(Integer pdtQtyAmount) {
        this.pdtQty += pdtQtyAmount;
        return this;
    }
}
