package com.microservice.productservice.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.productservice.model.Product;
import com.microservice.productservice.request.ProductRequest;
import com.microservice.productservice.service.ProductService;
import javax.persistence.EntityNotFoundException;



@RestController // requests come from front end to controller
@RequestMapping(value = "/om/v1/product") // it maps web requests url/endpoints
public class ProductController {
	
	// loggers keeps track of the application
	Logger logger = LoggerFactory.getLogger(ProductController.class);

	/*
	 * create - post update - patch/put retrieve/read - get delete - delete
	 */

//	API - application programming interface - communication between request 
//	and response(REST API - Represntation state transfer API)

	// from controller requests go to service class

	@Autowired
	private ProductService productService;

	// @RequestBody- it takes the input from the front end in json format and passes
	// it to API
	@PostMapping(value = "/save") // other way @RequestMapping(value="/save",method=RequestMethod.POST)
	public ResponseEntity<?> saveProduct(@RequestBody ProductRequest productRequest) {
		// call service method createProduct and return product body in the
		// responseentity
		logger.info("saveProduct API has started");
		Product product = productService.createProduct(productRequest);
		logger.info("saveProduct API is executed and saved the product into database");
		return ResponseEntity.ok().body(product);
		

	}

	// @PathVariable - it will specified in the url
	@GetMapping(value = "/find/{productId}")
	public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId) {
		// take productId from the url and call findProductById which is in
		// productService
		// and return the response binding to responseentity
		logger.info("getProductById API has started");
		Optional<Product> cust = productService.findProductById(productId);
		return ResponseEntity.ok().body(cust);
	}

	@GetMapping(value = "/findall")
	public ResponseEntity<?> getAllProducts() {
		// call findAllProducts() present in productService and get the list of products
		// present in database
		// and return the response binding to responseentity
		logger.info("getAllProducts API has started");
		List<Product> custList = productService.findAllProducts();
		return ResponseEntity.ok().body(custList);
	}

	@DeleteMapping(value = "/delete/{productId}")
	public ResponseEntity<?> deleteProductById(@PathVariable("productId") Long productId) {
		// call deleteProductById method in service class and it will not return
		// anything
		// so we are passing an explicit deleted message in responseentity
		logger.info("deleteProductById API has started");
		productService.deleteProductById(productId);
		return ResponseEntity.ok().body("The product id " + productId + " got deleted!");
	}

        @PutMapping(value = "/update/{productId}")
        public ResponseEntity<?> updateProduct(@RequestBody ProductRequest productRequest,
                        @PathVariable("productId") Long productId) {
                // take productrequest which contains new data to update and productId which is
                // used to fetch old data from datbase
                // call updateProduct of productService class
                logger.info("updateProduct API has started");
                try {
                        Product product = productService.updateProduct(productRequest, productId);
                        return ResponseEntity.ok().body(product);
                } catch (EntityNotFoundException ex) {
                        logger.error("Product not found", ex);
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
                }
        }

	/*
	 * when there are large data in database we cannot load whole result rather we
	 * break into records of certain size and give it we can achieve this with
	 * pagination we need two parmater page size-how many records per page and page
	 * number-the page you wish to see page size - 10 total numbers of record - 37
	 * 
	 * 0th page - 1-10 records 1st page - 11-20 2nd page - 21-30 3rd page - 31-37
	 * 
	 * page size - 6 total number of records - 27
	 * 
	 * 0th page - 1-6 1st page - 7-12 2nd page - 13-18 3rd page - 19-24 4th page -
	 * 25-27
	 * 
	 * page size -2 total numbers o records - 7
	 * 
	 * 0th page - 1-2 1st page - 3-4 2nd page - 5-6 3rd page - 7
	 */
	// @RequestParam - takes one parameter input from UI or postman
	@GetMapping(value = "/withpage")
	public ResponseEntity<?> getProductWithPagination(@RequestParam("pagenumber") Integer pageNumber,
			@RequestParam("pagesize") Integer pageSize) {
		logger.info("getProductWithPagination API has started");
		List<Product> productList = productService.getProductWithPage(pageNumber, pageSize);
		return ResponseEntity.ok().body(productList);
	}

	@PostMapping(value = "/savelist")
	public ResponseEntity<?> saveListOfProducts(@RequestBody List<ProductRequest> productRequests) {
		logger.info("saveListOfProducts API has started");
               List<Product> productList = productService.createListOfProducts(productRequests);
		return ResponseEntity.ok().body(productList);
	}
}
