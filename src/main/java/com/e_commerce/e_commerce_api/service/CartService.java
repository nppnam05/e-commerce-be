package com.e_commerce.e_commerce_api.service;

import com.e_commerce.e_commerce_api.dto.request.cart.CreateCartRequest;
import com.e_commerce.e_commerce_api.dto.request.cart.UpdateCartRequest;
import com.e_commerce.e_commerce_api.dto.response.CartResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductOfCartResponse;
import com.e_commerce.e_commerce_api.entity.Cart;
import com.e_commerce.e_commerce_api.exception.NotFoundException;
import com.e_commerce.e_commerce_api.mapper.CartMapper;
import com.e_commerce.e_commerce_api.projection.CartWithProductProjection;
import com.e_commerce.e_commerce_api.repository.CartRepository;
import com.e_commerce.e_commerce_api.repository.ProductChildrenRepository;
import com.e_commerce.e_commerce_api.repository.UserRepository;
import com.e_commerce.e_commerce_api.utils.ExceptionGenerator;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductChildrenRepository productChildrenRepository;

    @Transactional
    public CartResponse createCart(CreateCartRequest request) {
        var productChildren =
                productChildrenRepository.findByIdWithDetails(request.getProductChildrenId())
                        .orElseThrow(() -> new NotFoundException("ProductChildren not found"));

        if (!userRepository.existsById(request.getUserId())) {
            throw ExceptionGenerator.handleNotFoundUser(request.getUserId());
        }
        var user = userRepository.getReferenceById(request.getUserId());

        var product = productChildren.getProduct();

        var cart = Cart.builder().user(user).productChildren(productChildren)
                .quantity(request.getQuantity()).singlePrice(product.getPrice()).build();

        var savedCart = cartRepository.save(cart);

        var response = cartMapper.toResponse(savedCart);
        var productResponse = cartMapper.toProductResponse(productChildren);
        response.setProduct(productResponse);

        return response;
    }

    public boolean updateCart(long id, UpdateCartRequest request) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundCart(id));

        cart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return true;
    }

    public boolean deleteCart(long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> ExceptionGenerator.handleNotFoundCart(id));
        cartRepository.delete(cart);
        return true;
    }

    public List<CartResponse> getAllCartByUserId(long id) {
        List<CartWithProductProjection> carts = cartRepository.findByUserId(id);

        List<CartResponse> response = carts.stream().map(cart -> {
            CartResponse cartResponse = cartMapper.toResponse(cart);
            ProductOfCartResponse productResponse = cartMapper.toProductResponse(cart);
            cartResponse.setProduct(productResponse);
            return cartResponse;
        }).toList();
        return response;
    }
}
