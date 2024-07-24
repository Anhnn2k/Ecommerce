package com.example.ecommerce.admin;

import com.example.ecommerce.dto.CategoryDTO;
import com.example.ecommerce.entities.Category;
import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.CategoryService;
import com.example.ecommerce.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    private final ObjectMapper objectMapper;

    public CategoryController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @GetMapping("/category")
    public String getAll(Model model) {
        List<Category> categories = this.categoryService.getAll();
        List<CategoryDTO> categoryDTOS = new ArrayList<>();
        for (Category c : categories) {
            categoryDTOS.add(categoryService.convertToDTO(c));
        }
        this.getUser(model);
        model.addAttribute("categories", categoryDTOS);
        return "admin/category/index";
    }

    @GetMapping("/add-category")
    public String addCategory(Model model) {
        Category category = new Category();
        category.setStatus(true);
        CategoryDTO categoryDTO = this.categoryService.convertToDTO(category);
        this.getUser(model);
        model.addAttribute("category", categoryDTO);
        return "admin/category/add";
    }

    @PostMapping("/add-category")
    public String saveCategory(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            return "admin/category/add";
        }
        Category category = this.categoryService.convertToEntity(categoryDTO);
        if (this.categoryService.create(category)) {
            return "redirect:/admin/category";
        } else {
            return "admin/category/add";
        }
    }

    @GetMapping("/update-category/{id}")
    public String updateCategory(Model model, @PathVariable("id") Long id) {
        Category category = this.categoryService.getById(id);
        this.getUser(model);
        model.addAttribute("category", this.categoryService.convertToDTO(category));
        return "admin/category/update";
    }

    @PostMapping("/update-category")
    public String update(@Valid @ModelAttribute("category") CategoryDTO categoryDTO,
                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            return "admin/category/update";
        }
        Category category = this.categoryService.convertToEntity(categoryDTO);
        if (this.categoryService.update(category)) {
            return "redirect:/admin/category";
        } else {
            return "admin/category/update";
        }
    }

    @GetMapping("/delete-category/{id}")

    public String deleteCategory(@PathVariable("id") Long id) {
        if (this.categoryService.delete(id)) {
            return "redirect:/admin/category";
        } else {
            return "redirect:/admin/category";
        }
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
