package com.example.ecommerce.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Không được để trống tên sản phẩm")
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Hãy chọn danh mục hàng nhé")
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", nullable = false)
    @NotNull(message = "Hãy chọn brand cho mặt hàng của bạn")
    private Brand brand;

    @NotEmpty(message = "Không được để trống thông tin")
    @Column(name = "description")
    private String description;

    @Column(name = "import_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date importDate;

    @NotNull(message = "Không được bỏ trống giá nhập")
    @Min(value = 0,message = "Giá nhập phải lớn hơn 0")
    @Column(name = "import_price")
    private Double importPrice;

    @NotNull(message = "Không được bỏ trống giá bán")
    @Min(value = 0,message = "Giá bán phải lớn hơn 0")
    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductDiscountCampaign> pdc = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductDetails> productDetails = new ArrayList<>();


    @PrePersist
    protected void onCreate() {
        importDate = new Date();
    }

    public float getVote(List<Review> reviews) {
        Integer v = 0;
        int i = 0;
        float a;
        if (reviews.size() == 0) {
            a = 0;
        } else {
            for (Review r : reviews) {
                v += r.getVote();
                i++;
            }
            a = v / i;
        }
        return a;
    }

    public Double getOldPrice(List<ProductDiscountCampaign> pdc){
        Double oldPrice=0.0;
        if(pdc.size()!=0){
            oldPrice = pdc.get(0).getProduct().getPrice();
        }
        for (ProductDiscountCampaign pdc1:pdc) {
            oldPrice = oldPrice/(1-pdc1.getDiscountCampaign().getDiscountPercentage()/100);
        }
        return oldPrice;
    }

}
