package com.e_commerce.e_commerce_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.e_commerce.e_commerce_api.dto.request.product.CreateProductRequest;
import com.e_commerce.e_commerce_api.dto.request.product.UpdateProductRequest;
import com.e_commerce.e_commerce_api.dto.response.ProductDetailResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductResponse;
import com.e_commerce.e_commerce_api.dto.response.ProductStockResponse;
import com.e_commerce.e_commerce_api.dto.response.base.PageResponse;
import com.e_commerce.e_commerce_api.entity.Product;
import com.e_commerce.e_commerce_api.entity.ProductImage;
import com.e_commerce.e_commerce_api.entity.Stock;
import com.e_commerce.e_commerce_api.projection.ProductDetailProjection;
import com.e_commerce.e_commerce_api.projection.ProductProjection;
import com.e_commerce.e_commerce_api.projection.ProductStockProjection;
import com.e_commerce.e_commerce_api.repository.CategoryRepository;
import com.e_commerce.e_commerce_api.repository.ColorRepository;
import com.e_commerce.e_commerce_api.repository.ProductImageRepository;
import com.e_commerce.e_commerce_api.repository.ProductRepository;
import com.e_commerce.e_commerce_api.repository.SizeRepository;
import com.e_commerce.e_commerce_api.repository.StockRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final MinioService minioService;
    private final ProductImageRepository productImageRepository;
    private final StockRepository stockRepository;

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

    public PageResponse<List<ProductStockResponse>> getProductsStock(String keyword, int pageNumber,
            int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        String kw = keyword == null ? "" : keyword;
        List<ProductStockProjection> projections =
                productRepository.findProductStocks(kw, pageSize, offset);
        long total = productRepository.countProducts(kw);

        List<ProductStockResponse> data = projections.stream()
                .map(p -> (ProductStockResponse) ProductStockResponse.builder().id(p.getId())
                        .name(p.getName()).quantity(p.getQuantity()).category(p.getCategory())
                        .size(p.getSize()).color(p.getColor()).price(p.getPrice())
                        .imageUrls(p.getImageUrls() != null ? List.of(p.getImageUrls().split(","))
                                : List.of())
                        .build())
                .toList();

        return PageResponse.mapToPageResponse(data, pageNumber, pageSize, total);
    }

    public ProductDetailResponse getProductById(Long id) {
        ProductDetailProjection p = productRepository.findProductDetailById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductDetailResponse.builder().id(p.getId()).name(p.getName())
                .description(p.getDescription()).price(p.getPrice()).categoryId(p.getCategoryId())
                .colorCode(p.getColorCode()).sizeId(p.getSizeId())
                .imageUrls(
                        p.getImageUrls() != null ? List.of(p.getImageUrls().split(",")) : List.of())
                .build();
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        var category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        var color = colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found"));
        var size = sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found"));

        List<String> imageUrls = request.getImages().stream().map(minioService::upload).toList();

        var product = productRepository.save(
                Product.builder().name(request.getName()).category(category).color(color).size(size)
                        .description(request.getDescription()).price(request.getPrice()).build());

        stockRepository.save(Stock.builder().product(product).quantity(0).build());

        List<ProductImage> images = imageUrls.stream()
                .map(url -> ProductImage.builder().url(url).product(product).build()).toList();

        productImageRepository.saveAll(images);

        return ProductResponse.builder().id(product.getId()).categoryId(category.getId())
                .colorId(color.getId()).sizeId(size.getId()).name(product.getName())
                .description(product.getDescription()).price(product.getPrice()).imageUrl(imageUrls)
                .build();
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
        product.setColor(colorRepository.findById(request.getColorId())
                .orElseThrow(() -> new RuntimeException("Color not found")));
        product.setSize(sizeRepository.findById(request.getSizeId())
                .orElseThrow(() -> new RuntimeException("Size not found")));
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
        var stock = stockRepository.findByProductId(id)
                .orElseThrow(() -> new RuntimeException("Stock not found"));
        if (stock.getQuantity() > 0) {
            throw new RuntimeException("Product is in stock");
        }
        stockRepository.delete(stock);
        productImageRepository.deleteAllByProduct(product);
        productRepository.delete(product);
        return true;
    }
}
