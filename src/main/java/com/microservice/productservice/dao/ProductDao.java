package com.microservice.productservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservice.productservice.model.Product;


@Repository
public interface ProductDao extends JpaRepository<Product, Long>{

}
