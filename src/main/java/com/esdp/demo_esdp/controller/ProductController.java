package com.esdp.demo_esdp.controller;

import com.esdp.demo_esdp.dto.CategoryDTO;
import com.esdp.demo_esdp.dto.ImageDTO;
import com.esdp.demo_esdp.dto.ProductAddForm;
import com.esdp.demo_esdp.entity.User;
import com.esdp.demo_esdp.service.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final PropertiesService propertiesService;
    private final UserService userService;
    private final LocalitiesService localitiesService;
    private final CategoryService categoryService;

    @GetMapping("/add")
    public String getAddPage(Model model) {
        return "add-product";
    }

    @PostMapping("/add")
    public String addNewProduct(@Valid ProductAddForm productAddForm,
                                @Valid ImageDTO imageDTO,
                                BindingResult validationResult,
                                Authentication authentication,
                                RedirectAttributes attributes) {

        if (validationResult.hasFieldErrors()) {
            attributes.addFlashAttribute("errors", validationResult.getFieldErrors());
            return "redirect:/product/add";
        }
        User user = (User) authentication.getPrincipal();
        productService.addNewProduct(productAddForm, user);
        return "redirect:/profile";
    }

    @PostMapping("/delete")
    public String deleteProductById(@RequestParam("productId") Long productId,
                                    Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        productService.deleteProductById(productId, user);
        return "redirect:/product";
    }

    @GetMapping("/search-category")
    public String getProductCategory(@RequestParam("category") @NotBlank String category,
                                     Model model,
                                     Pageable pageable, HttpServletRequest uriBuilder) {
        var products = productService.getProductCategory(category, pageable);
        if (products.isEmpty()) {
            return "error404";
        } else {
            var uri = uriBuilder.getRequestURI();
            propertiesService.fillPaginationDataModel(products, propertiesService.getDefaultPageSize(), model, uri);
            return "index";
        }
    }

    @GetMapping("/search-name")
    public String getProductName(@RequestParam("name") @NotBlank String name, Model model,
                                 Pageable pageable, HttpServletRequest uriBuilder) {
        var products = productService.getProductName(name, pageable);
        if (products.isEmpty()) {
            return "error404";
        } else {
            var uri = uriBuilder.getRequestURI();
            propertiesService.fillPaginationDataModel(products, propertiesService.getDefaultPageSize(), model, uri);
            return "index";
        }
    }

    @GetMapping("/search-price")
    public String getProductPrice(@RequestParam("from") @NonNull @Min(1) Integer from,
                                  @RequestParam("before") @NonNull @Min(1) Integer before,
                                  Model model,
                                  Pageable pageable, HttpServletRequest uriBuilder) {
        var products = productService.getProductPrice(from, before, pageable);
        if (products.isEmpty()) {
            return "error404";
        } else {
            var uri = uriBuilder.getRequestURI();
            propertiesService.fillPaginationDataModel(products, propertiesService.getDefaultPageSize(), model, uri);
            return "index";
        }
    }

    @GetMapping("/create")
    public String createNewProductGET(Model model) {

        model.addAttribute("localities", localitiesService.getLocalitiesDTOs());
        model.addAttribute("select_categories", categoryService.getEndCategory());
        return "create_product";
    }

    @ResponseBody
    @PostMapping("/create")
    public ResponseEntity<Void> createNewProductPOST(@RequestBody ProductAddForm newProduct, Authentication authentication){


        return new ResponseEntity<>(HttpStatus.OK);
    }

}