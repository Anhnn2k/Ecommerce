package com.example.ecommerce.admin;

import com.example.ecommerce.entities.Color;
import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.Image;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.Size;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.BrandService;
import com.example.ecommerce.services.CategoryService;
import com.example.ecommerce.services.ColorService;
import com.example.ecommerce.services.ImageService;
import com.example.ecommerce.services.ProductDetailsService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.SizeService;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ColorService colorService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping("/admin/product")
    public String productDetails(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                 Model model) {
        Page<Product> productDetails = this.productService.findAll(pageNo, size);
        this.getUser(model);
        model.addAttribute("productDetails", productDetails);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("size", size);
        return "admin/product/index";
    }

    @GetMapping("/admin/add-product")
    public String addProduct(@RequestParam("pageNo") int pageNo,
                             @RequestParam("size") int size,
                             Model model) {
        this.getUser(model);
        model.addAttribute("brands", this.brandService.getAll());
        model.addAttribute("categories", this.categoryService.getByStatus());
        Product product = new Product();
        product.setStatus(true);
        model.addAttribute("product", product);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("size", size);
        return "admin/product/add";
    }

    @PostMapping("/admin/add-product")
    public String saveProduct(@RequestParam("file") MultipartFile[] multipartFile,
                              @RequestParam("pageNo") int pageNo,
                              @RequestParam("size") int size,
                              @Valid @ModelAttribute("product") Product product,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("product", product);
            model.addAttribute("brands", this.brandService.getAll());
            model.addAttribute("categories", this.categoryService.getByStatus());
            return "admin/product/add";
        }
        if (multipartFile.length == 0) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/admin/add-product?pageNo=" + pageNo + "&size=" + size;
        }
        List<Image> imageList = new ArrayList<>();
        for (MultipartFile image : multipartFile) {
            try {
                // Tạo thư mục uploads nếu chưa tồn tại
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = image.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.deleteIfExists(path);
                Files.copy(image.getInputStream(), path);

                // Tạo đối tượng Image và thiết lập thuộc tính
                Image img = new Image();
                img.setUrl("/uploads/" + fileName); // Đường dẫn URL để truy cập ảnh
                img.setProduct(product);
                imageList.add(img);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "File upload failed");
                return "redirect:/admin/add-product";
            }
        }
        // Thiết lập danh sách ảnh cho sản phẩm
        product.setImages(imageList);
        product.setStatus(true);
        // Lưu sản phẩm vào cơ sở dữ liệu
        productService.create(product);

        redirectAttributes.addFlashAttribute("message", "Product added successfully");
        return "redirect:/admin/product?pageNo=" + pageNo + "&size=" + size;

    }

    @GetMapping("/admin/uploadStatus")
    public String uploadStatus() {
        return "admin/product/uploadStatus";
    }

    @GetMapping("/admin/update-product/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @RequestParam("pageNo") int pageNo,
                                @RequestParam("size") int size,
                                Model model) {
        this.getUser(model);
        model.addAttribute("brands", this.brandService.getAll());
        model.addAttribute("categories", this.categoryService.getByStatus());
        Product product = this.productService.getById(id);
        model.addAttribute("product", product);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("size", size);
        return "admin/product/update";
    }

    @PostMapping("/admin/update-product")
    public String update(@RequestParam(value = "files", required = false) MultipartFile[] multipartFiles,
                         @RequestParam("pageNo") int pageNo,
                         @RequestParam("size") int size,
                         @RequestParam("existingImageUrls") List<Long> existingImageIds,
                         @Valid @ModelAttribute("product") Product product,
                         RedirectAttributes redirectAttributes,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("brands", this.brandService.getAll());
            model.addAttribute("categories", this.categoryService.getByStatus());
            model.addAttribute("product", product);
            model.addAttribute("pageNo", pageNo);
            model.addAttribute("size", size);
            return "admin/product/update";
        }
        try {
            // Danh sách để lưu ảnh mới
            List<Image> newImages = new ArrayList<>();

            // Xóa các file ảnh cũ nếu có ảnh mới được tải lên
            if (multipartFiles != null && multipartFiles.length > 0 && multipartFiles[0] != null && !multipartFiles[0].isEmpty()) {
                for (Long id : existingImageIds) {
                    Path oldFilePath = Paths.get(System.getProperty("user.dir") + imageService.getById(id).getUrl());
                    Files.deleteIfExists(oldFilePath);
                    this.imageService.delete(id);
                }
                // Thêm các file ảnh mới
                for (MultipartFile file : multipartFiles) {
                    if (file != null && !file.isEmpty()) {
                        // Tạo thư mục uploads nếu chưa tồn tại
                        Path uploadPath = Paths.get(UPLOAD_DIR);
                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }
                        String fileName = file.getOriginalFilename();
                        Path path = Paths.get(UPLOAD_DIR + fileName);
                        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                        Image image = new Image();
                        image.setUrl("/uploads/" + fileName);
                        image.setProduct(product);
                        newImages.add(image);
                    }
                }
                product.setImages(newImages);
                productService.update(product);
            }

            redirectAttributes.addFlashAttribute("message", "You successfully updated the product.");
            return "redirect:/admin/product?pageNo=" + pageNo + "&size=" + size;
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "An error occurred while updating the product.");
            return "redirect:/admin/update-product";
        }
    }

    @GetMapping("/admin/delete-product/{id}")
    public String deleteProduct(@PathVariable("id") Long id,
                                @RequestParam("pageNo") int pageNo,
                                @RequestParam("size") int size) {
        if (this.productService.delete(id)) {
            return "redirect:/admin/product?pageNo=" + pageNo + "&size=" + size;
        }
        return "redirect:/admin/product?pageNo=" + pageNo + "&size=" + size;
    }

    Long idPd;

    @GetMapping("/admin/create-detail-product/{id}")
    public String detailProduct(@PathVariable("id") Long id,
                                @RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
                                @RequestParam(name = "size", defaultValue = "5") int size,
                                Model model) {

        idPd = id;
        this.getUser(model);
        model.addAttribute("productDetails", this.productDetailsService.findAllByProductId(pageNo, size, id));
        model.addAttribute("id", this.productService.getById(id).getId());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("size", size);
        return "admin/product/detail_index";
    }

    @GetMapping("/admin/add-product-detail/{id}")
    public String addDetailProduct(@RequestParam("pageNo") int pageNo,
                                   @RequestParam("size") int size,
                                   @PathVariable("id") Long id, Model model) {
        this.getUser(model);
        model.addAttribute("products", this.productService.getById(id));
        model.addAttribute("colors", this.colorService.getAll());
        model.addAttribute("sizes", this.sizeService.getAll());
        ProductDetails productDetails = new ProductDetails();
        productDetails.setStatus(true);
        model.addAttribute("productDetail", productDetails);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("size", size);
        return "admin/product/detail_add";
    }

    @PostMapping("/admin/add-detail-product")
    public String createProductDetail(@RequestParam("color") List<Long> colorId,
                                      @RequestParam("size") List<Long> sizeId,
                                      @RequestParam("pageNo") int pageNo,
                                      @RequestParam("sizes") int size,
                                      @Valid @ModelAttribute("productDetail") ProductDetails productDetails,
                                      BindingResult bindingResult,
                                      Model model
    ) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("productDetail", productDetails);
            model.addAttribute("products", productDetails.getProduct());
            model.addAttribute("colors", this.colorService.getAll());
            model.addAttribute("sizes", this.sizeService.getAll());
            return "admin/product/detail_add";
        }
        List<Color> colors = this.colorService.findAllById(colorId);
        List<Size> sizes = this.sizeService.getAllById(sizeId);
        for (Color c : colors) {
            for (Size s : sizes) {
                ProductDetails productDetails1 = new ProductDetails();
                productDetails1.setProduct(productDetails.getProduct());
                productDetails1.setColor(c);
                productDetails1.setSize(s);
                productDetails1.setQuantity(productDetails.getQuantity());
                productDetails1.setStatus(true);
                this.productDetailsService.create(productDetails1);
            }
        }
        return "redirect:/admin/create-detail-product/" + productDetails.getProduct().getId() + "?pageNo=" + pageNo + "&size=" + size;
    }

    @GetMapping("/admin/delete-product-detail/{id}")
    public String deleteProductDetail(@PathVariable("id") Long id,
                                      @RequestParam("pageNo") int pageNo,
                                      @RequestParam("size") int size) {
        this.productDetailsService.delete(id);
        String url = "redirect:/admin/create-detail-product/" + idPd + "?pageNo=" + pageNo + "&size=" + size;
        return url;
    }

    public void getUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof CustomUserDetails) {
            user = ((CustomUserDetails) principal).getUser();
        } else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
        }else if (authentication.getPrincipal() instanceof OAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            user = this.userService.findByEmail(oAuth2User.getAttribute("email"));
        } else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }
        model.addAttribute("user", user);
    }
}
