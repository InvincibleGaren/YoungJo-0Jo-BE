package com.youngjo.ssg.domain.product.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.youngjo.ssg.global.common.BaseEntity;
import com.youngjo.ssg.global.common.FileNameGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/***
 * imgTitle : 파일명 -> 예) SSGMALL_20220813_랜덤값
 * imgPath : 파일경로 -> 실제 데이터는 URL로
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ProductImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_img_id")
    private Long id;
    private String imgTitle = FileNameGenerator.generate();
    private String imgPath;
    private String imgAlt;

    // == Mapping ==
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_thumb_id")
    private ProductBoard productBoardThumb;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_board_detail_id")
    private ProductBoard productBoardDetail;

    @Builder
    public ProductImg(String imgPath, String imgAlt) {
        this.imgPath = imgPath;
        this.imgAlt = imgAlt;
    }

    public ProductImg linkToProductBoardThumb(ProductBoard productBoard) {
        this.productBoardThumb = productBoard;
        return this;
    }

    public ProductImg linkToProductBoardDetail(ProductBoard productBoard) {
        this.productBoardDetail = productBoard;
        return this;
    }
}
