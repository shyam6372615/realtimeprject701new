package com.nt.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.nt.entity.Customer;
import com.nt.service.ICustomerMgmtService;

@Component
public class PAndSRepoRunner implements CommandLineRunner {
	@Autowired
	private  ICustomerMgmtService  custService;

	@Override
	public void run(String... args) throws Exception {
	     
		//custService.getAllCustomers(false, "billamt").forEach(System.out::println);
		
		/*	Page<Customer> page=custService.getCustomersByPageNo(2, 4);   // pageNo -0 , pageSize 4
			System.out.println("request page records are ::");
			 page.getContent().forEach(System.out::println);
			 System.out.println("total pages count ::"+page.getTotalPages());
			 System.out.println("current page no::"+page.getNumber());
			 System.out.println("total no.of records"+page.getTotalElements());
			 System.out.println(" no.of records in the requested page ::"+page.getNumberOfElements());
			 System.out.println(" Is the request page is first page ::"+page.isFirst());
			 System.out.println(" Is the request page is last page ::"+page.isLast());
			 */
		
		/*	Page<Customer> page=custService.getSortedCustomersByPageNo(10, 3,true,"billamt","cadd");
			page.getContent().forEach(System.out::println);*/
		
		     custService.showCustomersPageByPage(3);
		
	}

	
}
