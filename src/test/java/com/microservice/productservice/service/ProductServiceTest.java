package com.microservice.productservice.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.microservice.productservice.dao.ProductDao;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.request.ProductRequest;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void createListOfProducts_savesEachProduct() {
        List<ProductRequest> requests = Arrays.asList(
            buildRequest("Prod1", "10", "5", "Desc1"),
            buildRequest("Prod2", "20", "6", "Desc2")
        );

        when(productDao.save(any(Product.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        List<Product> result = productService.createListOfProducts(requests);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productDao, times(requests.size())).save(captor.capture());

        List<Product> saved = captor.getAllValues();
        assertEquals(requests.size(), result.size());
        for (int i = 0; i < requests.size(); i++) {
            ProductRequest req = requests.get(i);
            Product savedProd = saved.get(i);
            Product resultProd = result.get(i);

            assertEquals(req.getProductName(), savedProd.getProductName());
            assertEquals(req.getPrice(), savedProd.getPrice());
            assertEquals(req.getProductQuantity(), savedProd.getProductQuantity());
            assertEquals(req.getDescription(), savedProd.getDescription());

            assertEquals(req.getProductName(), resultProd.getProductName());
            assertEquals(req.getPrice(), resultProd.getPrice());
            assertEquals(req.getProductQuantity(), resultProd.getProductQuantity());
            assertEquals(req.getDescription(), resultProd.getDescription());
        }
    }

    private ProductRequest buildRequest(String name, String price, String qty, String desc) {
        ProductRequest req = new ProductRequest();
        req.setProductName(name);
        req.setPrice(price);
        req.setProductQuantity(qty);
        req.setDescription(desc);
        return req;
    }
}
