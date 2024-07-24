package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {
    private Long id;
    @NotEmpty(message = "Không được bỏ trống tên danh mục")
    private String name;
    @NotEmpty(message = "Không được bỏ trống nội dung")
    private String description;
    private Boolean status;
}
