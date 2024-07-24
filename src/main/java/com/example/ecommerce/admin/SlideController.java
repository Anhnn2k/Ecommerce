package com.example.ecommerce.admin;

import com.example.ecommerce.entities.CustomUserDetails;
import com.example.ecommerce.entities.Slides;
import com.example.ecommerce.entities.User;
import com.example.ecommerce.services.SlideService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/admin/")
public class SlideController {
    @Autowired
    private SlideService slideService;
    @Autowired
    private UserService userService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    @GetMapping("slide")
    public String slides(Model model) {
        this.getUser(model);
        model.addAttribute("slides", this.slideService.getAll());
        return "admin/slide/index";
    }

    @GetMapping("add-slide")
    public String addSlide(Model model) {
        this.getUser(model);
        Slides slides = new Slides();
        slides.setOder(this.slideService.getAll().size() + 1);
        model.addAttribute("slide", slides);
        return "admin/slide/add";
    }

    @PostMapping("add-slide")
    public String saveSlide(@RequestParam("file") MultipartFile multipartFile,
                            @Valid @ModelAttribute("slide") Slides slide,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("slide", slide);
            return "admin/slide/add";
        }
        if (multipartFile.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:/admin/add-slide";
        }
        try {
            // Tạo thư mục uploads nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = multipartFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.copy(multipartFile.getInputStream(), path);

            // Lưu đường dẫn file vào ProductDetails
            slide.setUrl("/uploads/" + fileName);

            // Lưu ProductDetails vào cơ sở dữ liệu
            slideService.create(slide);

            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + fileName + "'");
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "File upload failed");
            return "redirect:/admin/add-slide";
        }

        if (slideService.create(slide)) {
            return "redirect:/admin/slide";
        } else {
            redirectAttributes.addFlashAttribute("message", "Product save failed");
            return "redirect:/admin/add-slide";
        }
    }

    @GetMapping("update-slide/{id}")
    public String updateSlide(@PathVariable("id") Long id, Model model) {
        this.getUser(model);
        model.addAttribute("slide", this.slideService.getById(id));
        return "admin/slide/update";
    }

    @PostMapping("update-slide")
    public String update(@RequestParam(value = "file", required = false) MultipartFile multipartFile,
                         @RequestParam("existingImageUrl") String existingImageUrl,
                         @Valid @ModelAttribute("slide") Slides slide,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes, Model model) {
        if (bindingResult.hasErrors()) {
            this.getUser(model);
            model.addAttribute("slide", slide);
            return "admin/slide/update";
        }
        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                // Tạo thư mục uploads nếu chưa tồn tại
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = multipartFile.getOriginalFilename();
                Path path = Paths.get(UPLOAD_DIR + fileName);
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                slide.setUrl("/uploads/" + fileName);
                redirectAttributes.addFlashAttribute("message",
                        "You successfully uploaded '" + fileName + "'");
            } else {
                slide.setUrl(existingImageUrl);
            }
            slideService.update(slide);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "File upload failed" + e.getMessage());
            return "redirect:/admin/update-slide";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Slide update failed" + e.getMessage());
        }


        if (slideService.update(slide)) {
            return "redirect:/admin/slide";
        } else {
            redirectAttributes.addFlashAttribute("message", "Product save failed");
            return "redirect:/admin/update-slide";
        }
    }

    @GetMapping("delete-slide/{id}")
    public String deleteSlide(@PathVariable("id") Long id) {
        if (this.slideService.delete(id)) {
            return "redirect:/admin/slide";
        }
        return "redirect:/admin/slide";
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
