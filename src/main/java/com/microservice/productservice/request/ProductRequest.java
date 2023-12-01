package com.microservice.productservice.request;

import lombok.Data;

@Data
public class ProductRequest {

	private String productName;
	private String price;
	private String productQuantity;
	private String description;
}
