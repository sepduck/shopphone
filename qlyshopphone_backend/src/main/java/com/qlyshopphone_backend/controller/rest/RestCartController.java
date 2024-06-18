package com.qlyshopphone_backend.controller.rest;

import com.qlyshopphone_backend.model.Cart;
import com.qlyshopphone_backend.model.Product;
import com.qlyshopphone_backend.model.Users;
import com.qlyshopphone_backend.service.ProductService;
import com.qlyshopphone_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping()
public class RestCartController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @PostMapping("/cart/add/{productId}")
    public ResponseEntity<?> addCartItem(@PathVariable("productId") int productId, Principal principal) {
        Users users = userService.findByUsername(principal.getName());
        Product product = productService.findByProductId(productId).
                orElseThrow(() -> new RuntimeException("Product not found"));
        Cart cart = new Cart();
        cart.setProduct(product);
        cart.setQuantity(1);
        users.getCart().add(cart);
        userService.saveUser(users);
        return ResponseEntity.ok().body("Product added successfully");
    }

    @GetMapping("/list-cart")
    public ResponseEntity<?> viewCart(Principal principal) {
        Users user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(user.getCart());
    }
}
