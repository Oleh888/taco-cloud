package com.example.tacocloud.dao;

import com.example.tacocloud.model.Taco;

public interface TacoRepository {
    Taco save(Taco taco);
}
