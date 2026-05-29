package com.e_commerce.e_commerce_api.projection;

public interface TotalProjection {
    long getTotalUsers();
    long getTotalOrders();
    long getTotalSales();
    long getTotalPending();
} 
