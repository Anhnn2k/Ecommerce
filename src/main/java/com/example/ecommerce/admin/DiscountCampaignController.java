package com.example.ecommerce.admin;

import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.DiscountCampaign;
import com.example.ecommerce.entities.ProductDiscountCampaign;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.DiscountCampaignService;
import com.example.ecommerce.services.ProductDiscountCampaignService;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class DiscountCampaignController {
    @Autowired
    private DiscountCampaignService discountCampaignService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductDiscountCampaignService pdcs;
    @Autowired
    private UserService userService;

    @GetMapping("/discount-campaign")
    public String getDiscountCampaign(Model model) {
        List<DiscountCampaign> discountCampaigns = this.discountCampaignService.getAll();
        this.getUser(model);
        model.addAttribute("discountCampaigns", discountCampaigns);
        return "admin/discountcampaign/index";
    }

    @GetMapping("/add-discount-campaign")
    public String addDiscountCampaign(Model model) {
        DiscountCampaign discountCampaign = new DiscountCampaign();
        this.getUser(model);
        model.addAttribute("discountCampaign", discountCampaign);
        return "admin/discountcampaign/add";
    }

    @PostMapping("/add-discount-campaign")
    public String saveDiscountCampaign(@Valid @ModelAttribute("discountCampaign") DiscountCampaign discountCampaign,
                                       BindingResult bindingResult,
                                       Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("discountCampaign", discountCampaign);
            return "admin/discountcampaign/add";
        }
        if (this.discountCampaignService.create(discountCampaign)) {
            return "redirect:/admin/discount-campaign";
        } else {
            return "admin/discountcampaign/add";
        }
    }

    @GetMapping("/update-discount-campaign/{id}")
    public String detailDiscountCampaign(@PathVariable("id") Long id, Model model) {
        DiscountCampaign discountCampaign = this.discountCampaignService.getById(id);
        this.getUser(model);
        model.addAttribute("discountCampaign", discountCampaign);
        return "admin/discountcampaign/update";
    }

    @PostMapping("/update-discount-campaign")
    public String updateDiscountCampaign(@Valid @ModelAttribute("discountCampaign") DiscountCampaign discountCampaign,
                                         BindingResult bindingResult, Model model) {
        discountCampaign.setCreatedAt(new Date());
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("discountCampaign", discountCampaign);
            return "admin/discountcampaign/update";
        }
        if (this.discountCampaignService.update(discountCampaign)) {
            return "redirect:/admin/discount-campaign";
        } else {
            return "admin/discountcampaign/update";
        }
    }

    @GetMapping("/delete-discount-campaign/{id}")
    public String deleteDiscountCampaign(@PathVariable("id") Long id) {
        if (this.discountCampaignService.delete(id)) {
            return "redirect:/admin/discount-campaign";
        } else {
            return "redirect:/admin/discount-campaign";
        }
    }

    private Long idDC;

    @GetMapping("/discount-detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        idDC = id;
        this.getUser(model);
        model.addAttribute("productDiscountCampaigns", this.pdcs.getByDiscountId(id));
        model.addAttribute("id", this.discountCampaignService.getById(id).getId());
        return "admin/discountcampaign/appliesindex";
    }

    @GetMapping("/discount-applies/{id}")
    public String appliesDiscount(@PathVariable("id") Long id, Model model){
        ProductDiscountCampaign productDiscountCampaign = new ProductDiscountCampaign();
        this.getUser(model);
        model.addAttribute("productDiscountCampaign", productDiscountCampaign);
        model.addAttribute("products", this.productService.getAll());
        model.addAttribute("discountCampaign", this.discountCampaignService.getById(id));
        return "admin/discountcampaign/appliesadd";
    }

    @PostMapping("/applies-discount-campaign")
    public String applies(@ModelAttribute("productDiscountCampaign") ProductDiscountCampaign pdc, Model model) {

        if (this.pdcs.create(pdc)) {
            this.getUser(model);
            model.addAttribute("productDiscountCampaigns", this.pdcs.getByDiscountId(pdc.getDiscountCampaign().getId()));
            model.addAttribute("id", pdc.getDiscountCampaign().getId());
            return "admin/discountcampaign/appliesindex";
        } else {
            model.addAttribute("productDiscountCampaigns", this.pdcs.getAll());
            model.addAttribute("id", pdc.getDiscountCampaign().getId());
            return "admin/discountcampaign/appliesindex";
        }

    }

    @GetMapping("/delete-product-discount-campaign/{id}")
    public String deleteApplies(@PathVariable("id") Long id, Model model) {
        this.getUser(model);
        this.pdcs.delete(id);
        model.addAttribute("productDiscountCampaigns", this.pdcs.getByDiscountId(idDC));
        model.addAttribute("id", idDC);
        return "admin/discountcampaign/appliesindex";
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
