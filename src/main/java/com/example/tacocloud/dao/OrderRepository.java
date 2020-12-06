package com.example.tacocloud.dao;

import com.example.tacocloud.model.Order;

public interface OrderRepository {
    Order save(Order order);
}
