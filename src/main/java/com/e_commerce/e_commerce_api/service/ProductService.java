package com.e_commerce.e_commerce_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.product.CreateProductRequest;
import com.e_commerce.e_commerce_api.dto.request.product.UpdateProductRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.entity.ProductImage;
import com.e_commerce.e_commerce_api.projection.ProductDetailProjection;
import com.e_commerce.e_commerce_api.projection.ProductFilterProjection;
import com.e_commerce.e_commerce_api.projection.ProductProjection;
import com.e_commerce.e_commerce_api.repository.CategoryRepository;
import com.e_commerce.e_commerce_api.repository.ProductImageRepository;
import com.e_commerce.e_commerce_api.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final MinioService minioService;
    private final ProductImageRepository productImageRepository;

    public PageResponse<List<ProductResponse>> getProducts(String keyword, int pageNumber,
            int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String kw = keyword == null ? "" : keyword;

        List<ProductProjection> projections =
                productRepository.findAllProducts(kw, pageSize, offset);
        long total = productRepository.countProducts(kw);

        List<ProductResponse> data = projections.stream().map(p -> (ProductResponse) ProductResponse
                .builder().id(p.getId()).name(p.getName()).description(p.getDescription())
                .price(p.getPrice())
                .imageUrl(
                        p.getImageUrls() != null ? List.of(p.getImageUrls().split(",")) : List.of())
                .build()).toList();

        return PageResponse.mapToPageResponse(data, pageNumber, pageSize, total);
    }

    public ProductDetailResponse getProductById(long id) {
        ProductDetailProjection projection = productRepository.findProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductDetailResponse.builder().id(projection.getId()).name(projection.getName())
                .description(projection.getDescription()).price(projection.getPrice())
                .categoryId(projection.getCategoryId())
                .imageUrls(projection.getImageUrls() != null
                        ? List.of(projection.getImageUrls().split(","))
                        : List.of())
                .build();
    }

    public List<ProductFilterProjection> getProductFilters() {
        return productRepository.findProductFilters();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        List<String> imageUrls = request.getImages().stream().map(minioService::upload).toList();

        var product =
                productRepository.save(Product.builder().name(request.getName()).category(category)
                        .description(request.getDescription()).price(request.getPrice()).build());

        List<ProductImage> images = imageUrls.stream()
                .map(url -> ProductImage.builder().url(url).product(product).build()).toList();

        productImageRepository.saveAll(images);

        return ProductResponse.builder().id(product.getId()).categoryId(category.getId())
                .name(product.getName()).description(product.getDescription())
                .price(product.getPrice()).imageUrl(imageUrls).build();
    }

    @Transactional
    public Boolean updateProduct(Long id, UpdateProductRequest request) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setCategory(categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            productImageRepository.deleteAllByProduct(product);

            List<String> imageUrls =
                    request.getImages().stream().map(minioService::upload).toList();

            List<ProductImage> images = imageUrls.stream()
                    .map(url -> ProductImage.builder().url(url).product(product).build()).toList();

            productImageRepository.saveAll(images);
        }
        productRepository.save(product);
        return true;
    }

    @Transactional
    public Boolean deleteProduct(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productImageRepository.deleteAllByProduct(product);
        productRepository.delete(product);
        return true;
    }
}
