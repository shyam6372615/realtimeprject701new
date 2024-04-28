//Customer.java
package com.nt.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity  
@Table(name="JPA_CUSTOMER_TAB")
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
	
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)  //generates value to the "cno" property automatically  startng with 1 increment by 50
	@SequenceGenerator(name = "gen1",sequenceName = "CNO_SEQ",initialValue = 100, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "gen1")
	private  Integer cid;
	
	   // name attribute is optional
	@Nonnull
	private  String cname;
	
	@Nonnull
	private  String cadd;

	  //@Column optional
	@Nonnull
	private  Float billamt;

}
