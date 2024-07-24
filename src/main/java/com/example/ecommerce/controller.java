package com.example.ecommerce;

import com.example.ecommerce.entities.Cart;
import com.example.ecommerce.entities.CartDetail;
import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.ProductDetails;
import com.example.ecommerce.entities.ProductDiscountCampaign;
import com.example.ecommerce.entities.Review;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CartDetailService;
import com.example.ecommerce.services.CartService;
import com.example.ecommerce.services.CategoryService;
import com.example.ecommerce.services.ProductDetailsService;
import com.example.ecommerce.services.ProductDiscountCampaignService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.ReviewService;
import com.example.ecommerce.services.SlideService;
import com.example.ecommerce.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class controller {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SlideService slideService;
    @Autowired
    private ProductDetailsService productDetailsService;
    @Autowired
    private CartDetailService cartDetailService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;

    @GetMapping("/home/category/{id}")
    public String getProductByCategory(Model model, @PathVariable("id") Long id) {
        model.addAttribute("categories", this.categoryService.getAll());
        model.addAttribute("slides", this.slideService.getAll());
        model.addAttribute("products", this.productDetailsService.getAll());
        model.addAttribute("id", id);
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "client/login/login";
    }


    @GetMapping("/home/product_view/{id}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        Long idCategory = this.productService.getById(id).getCategory().getId();
        show(model);
        model.addAttribute("products", this.productService.getByCategoryId(idCategory, id));
        model.addAttribute("colors", this.productDetailsService.getCoLorByProductId(id));
        model.addAttribute("sizes", this.productDetailsService.getSizeByProductId(id));
        model.addAttribute("product", this.productService.getById(id));
        return "client/product/index";
    }

    @PostMapping("/home/add-to-cart/{id}")
    public String addToCart(@PathVariable("id") Long id,
                            @RequestParam("size") Long idSize,
                            @RequestParam("color") Long idColor,
                            @RequestParam("qty") int qty) {
        Product product = productService.getById(id);
        ProductDetails productDetails = this.productDetailsService.getByProductAndSizeAndColor(product.getId(), idSize, idColor);
        User user = getUserByType();
        Long idCart = cartService.getByUserId(user.getId()).getId();
        Cart cart = this.cartService.getById(idCart);
        CartDetail cartDetail = new CartDetail();
        cartDetail.setProductDetails(productDetails);
        cartDetail.setCart(cart);
        cartDetail.setQuantity(qty);
        cartDetail.setPrice(productDetails.getProduct().getPrice());
        if (this.cartDetailService.createCartDetail(cartDetail, idCart)) {
            productDetails.setQuantity(productDetails.getQuantity() - qty);
            this.productDetailsService.update(productDetails);
            System.out.println();
        }
        String url = "redirect:/home/product_view/" + id;
        return url;
    }

    @GetMapping("/home/search")
    public String search(@RequestParam("q") String name,
                         Model model) {
        List<Product> products = this.productService.getBySearch(name);
        show(model);
        model.addAttribute("products", products);
        model.addAttribute("pageName","Search");
        model.addAttribute("title","Search Results");
        return "client/search/index";
    }

    @GetMapping("/home/product-category/{id}")
    public String productCategory(@PathVariable("id") Long id, Model model) {
        show(model);
        List<Product> products = this.productService.getByCategoryId(id);
        model.addAttribute("products", products);
        model.addAttribute("pageName","Product_Category");
        model.addAttribute("title",this.categoryService.getById(id).getName());
        return "client/search/index";
    }

    @PostMapping("/home/product/add-review/{id}")
    public String createReview(@PathVariable("id") Long id,
                               @RequestParam("cmt") String cmt,
                               @RequestParam("rating") int rating) {
        Review review = new Review();
        Product product = this.productService.getById(id);
        User user = getUserByType();
        review.setProduct(product);
        review.setUser(user);
        review.setReview(cmt);
        review.setVote(rating);
        this.reviewService.create(review);
        String url = "redirect:/home/product_view/" + id;
        return url;
    }

    public void show(Model model) {
        User user = getUserByType();
        Long idCart = cartService.getByUserId(user.getId()).getId();
        model.addAttribute("id", String.valueOf(idCart));
        model.addAttribute("showCart", true);
        model.addAttribute("id1", String.valueOf(user.getId()));
        model.addAttribute("showAcount", true);
        model.addAttribute("imageUrl",user.getImageUrl());
        model.addAttribute("name", (user.getFirstName()!=null && user.getLastName()!=null) ? user.getFirstName() + " " + user.getLastName():"Client");
        model.addAttribute("carts", this.cartDetailService.getCartDetailByCartId(idCart));
        model.addAttribute("cartQuantity", this.cartDetailService.getCartQuantity(idCart));
        model.addAttribute("totalPrice", this.cartDetailService.getTotalPrice(idCart));
        model.addAttribute("showAdmin", false);
        model.addAttribute("categories",this.categoryService.getByStatus());
    }
    public User getUserByType(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        User user;
        if(principal instanceof CustomUserDetails){
            user = ((CustomUserDetails) principal).getUser();
            return user;
        }
        else if (principal instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
            user = this.userService.findByEmail(oidcUser.getEmail());
            return user;
        }
        else if (authentication.getPrincipal() instanceof OAuth2User) {
            DefaultOAuth2User oAuth2User = (DefaultOAuth2User) principal;
            user = this.userService.findByEmail(oAuth2User.getAttribute("email"));
            return user;
        }
        else {
            throw new IllegalStateException("Unexpected user type: " + principal.getClass());
        }
    }
}
