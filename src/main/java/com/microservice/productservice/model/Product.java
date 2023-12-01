package com.microservice.productservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // generates getter and setters
@Entity//refers that this is an entity class
@Table(name="product")// creates table product in database oms
@AllArgsConstructor // all fields constructor
@NoArgsConstructor // no field constructors
public class Product {

	@Id // refers to primary key of the table
	@Column(name="product_id", nullable=false)// column corresponds to table column and it cannot be null
	@GeneratedValue(strategy = GenerationType.AUTO)// automatically generates id value
	private Long productId;
	
	@Column(name="product_name", nullable=false)
	private String productName;
	
	@Column(name="price", nullable=false)
	private String price;
	
	@Column(name="product_quantity")
	private String productQuantity;
	
	@Column(name="description")
	private String description;
}
