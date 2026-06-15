package com.e_commerce.e_commerce_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.cart.CreateCartRequest;
import com.e_commerce.e_commerce_api.dto.request.cart.UpdateCartRequest;
import com.e_commerce.e_commerce_api.dto.response.CartResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOfCartResponse;
import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.entity.Color;
import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.entity.ProductImage;
import com.e_commerce.e_commerce_api.entity.Size;
import com.e_commerce.e_commerce_api.entity.User;
import com.e_commerce.e_commerce_api.mapper.CartMapper;
import com.e_commerce.e_commerce_api.projection.CartWithProductProjection;
import com.e_commerce.e_commerce_api.repository.CartRepository;
import com.e_commerce.e_commerce_api.repository.ColorRepository;
import com.e_commerce.e_commerce_api.repository.ProductImageRepository;
import com.e_commerce.e_commerce_api.repository.ProductRepository;
import com.e_commerce.e_commerce_api.repository.SizeRepository;
import com.e_commerce.e_commerce_api.repository.UserRepository;
import com.e_commerce.e_commerce_api.utils.ExceptionGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper _cartMapper;
    private final CartRepository _cartRepository;
    private final ProductRepository _productRepository;
    private final ProductImageRepository _productImageRepository;
    private final ColorRepository _ColorRepository;
    private final SizeRepository _SizeRepository;
    private final UserRepository _UserRepository;

    @Transactional
    public CartResponse createCart(CreateCartRequest request) {
        Product product = _productRepository.findById(request.getProductId())
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundProduct(request.getProductId()));
        List<ProductImage> images = _productImageRepository.findByProductId(product.getId());
        Color color = _ColorRepository.findById(product.getColorId())
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundColor(product.getColorId()));
        Size size = _SizeRepository.findById(product.getSizeId())
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundSize(product.getSizeId()));

        product.setProductImages(images);
        product.setColor(color);
        product.setSize(size);

        User user = _UserRepository.findById(request.getUserId())
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundUser(request.getUserId()));

        BigDecimal price = product.getPrice();

        Cart cart = Cart.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .user(user)
                .product(product)
                .quantity(request.getQuantity())
                .singlePrice(price)
                .build();

        _cartRepository.save(cart);

        CartResponse response = _cartMapper.toResponse(cart);

        ProductOfCartResponse productResponse = _cartMapper.toResponse(product);
        response.setProduct(productResponse);

        return response;
    }

    @Transactional
    public boolean updateCart(long id, UpdateCartRequest request) {
        Cart cart = _cartRepository.findById(id)
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundCart(id));

        cart.setQuantity(request.getQuantity());
        _cartRepository.save(cart);
        return true;
    }

    @Transactional
    public boolean deleteCart(long id) {
        Cart cart = _cartRepository.findById(id).orElseThrow(() -> ExceptionGenerator.handleNotFoundCart(id));
        _cartRepository.delete(cart);
        return true;
    }

    public List<CartResponse> getAllCartByUserId(long id){
        List<CartWithProductProjection> carts = _cartRepository.findByUserId(id);

        List<CartResponse> response = carts.stream().map(
                cart -> {
                    CartResponse cartResponse = _cartMapper.toResponse(cart);
                    ProductOfCartResponse productResponse = _cartMapper.toProductResponse(cart);
                    cartResponse.setProduct(productResponse);
                    return cartResponse;
                }).toList();
        return response;
    }

    public List<CartResponse> getAllCart() {
        List<CartWithProductProjection> carts = _cartRepository.findAllWithProducts();

        List<CartResponse> response = carts.stream().map(
                cart -> {
                    CartResponse cartResponse = _cartMapper.toResponse(cart);
                    ProductOfCartResponse productResponse = _cartMapper.toProductResponse(cart);
                    cartResponse.setProduct(productResponse);
                    return cartResponse;
                }).toList();
        return response;
    }
}
