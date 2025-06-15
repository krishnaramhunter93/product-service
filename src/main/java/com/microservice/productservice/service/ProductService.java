package com.microservice.productservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.microservice.productservice.dao.ProductDao;
import com.microservice.productservice.model.Product;
import com.microservice.productservice.request.ProductRequest;



@Service
public class ProductService {
	
	// loggers keeps track of the application
	Logger logger = LoggerFactory.getLogger(ProductService.class);

	// from service the flow goes to ProductDao

	@Autowired
	private ProductDao productDao;

	/*
	 * create - createProduct read / retrieve - findProductById, findAllProduct
	 * update - updateProduct delete - deleteProductById
	 */

	public Product createProduct(ProductRequest productRequest) {
		// take all the request fields from productRequest using getters
		// and set it in product model/entity class using setter
		// save that product model object into database
		// return the save product
		logger.info("createProduct methos has started");
		Product product = new Product();
		product.setProductName(productRequest.getProductName());
		product.setPrice(productRequest.getPrice());
		product.setProductQuantity(productRequest.getProductQuantity());
		product.setDescription(productRequest.getDescription());
		logger.info("product is saving in the database");
		product = productDao.save(product);
		logger.info("Product saved successfully");
		return product;

	}

	public Optional<Product> findProductById(Long productId) {
		// take productId as input and find that product in the table if present else
		// not found
		Optional<Product> product = productDao.findById(productId);
		return product;

	}

	public List<Product> findAllProducts() {

		// find all the products in the database with findAll() method and that returns
		// list of products
		List<Product> productList = productDao.findAll();
		return productList;
	}

	public void deleteProductById(Long productId) {
		// takes productId as input and deleted it in the database table product
		productDao.deleteById(productId);
	}

	public Product updateProduct(ProductRequest productRequest, Long productId) {
		// 1. get old/saved product values from database table using findByID and store
		// it in product object
		// 2. set product object old data to productrequest object new data
		// 3. save product object to database

		// productRequest contains new data that needs to be updated in database, coming
		// from the front end
		// product object contains old data from database
		// remove old data from product object and update it with the new data of
		// productRequest
		Product updatedProduct = null;
		// step1
                Product product = productDao.findById(productId)
                                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
                // step2 - update fields
                product.setProductName(productRequest.getProductName());
                product.setPrice(productRequest.getPrice());
                product.setProductQuantity(productRequest.getProductQuantity());
                product.setDescription(productRequest.getDescription());

                // step3
                updatedProduct = productDao.save(product);

		return updatedProduct;

	}
	
	public List<Product> getProductWithPage(Integer pageNumber, Integer pageSize) {
		// call findAll in productDao and pass pagenumber and pagesize as parameter and sorting based on price ascending order
		// it return page of product as output
		Page<Product> productPage = productDao.findAll(PageRequest.of(pageNumber, pageSize,Sort.by("productName").ascending()));
		// we need to convert the page of product into list of product
		List<Product> productList = new ArrayList<>();
		// we are traversing productPage using foreach loop and adding single element into productList
		for(Product pro : productPage) {
			productList.add(pro);
		}
		// returning productlist
		return productList;
	}
	
	// this will take list of productRequets and call traverse it using foreach loop and gets single product
	// calls createProduct and save it in database 
	// same step is repeated for all products in the list
	// and it return list of saved product
       public List<Product> createListOfProducts(List<ProductRequest> productRequests) {
		List<Product> products = new ArrayList<>();
		for(ProductRequest prodReq : productRequests) {
			Product prod = createProduct(prodReq);
			products.add(prod);
		}
		return products;
	}
}
