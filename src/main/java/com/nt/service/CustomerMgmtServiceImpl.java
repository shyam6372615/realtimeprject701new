package com.nt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nt.entity.Customer;
import com.nt.repository.ICustomerRepository;

@Service("custService")
public class CustomerMgmtServiceImpl implements ICustomerMgmtService {
	@Autowired
	private  ICustomerRepository  custRepo;

	@Override
	public Iterable<Customer> getAllCustomers(boolean ascOrder, String... properties) {
		Sort sort=Sort.by(ascOrder?Direction.ASC:Direction.DESC, properties);
		Iterable<Customer> it=custRepo.findAll(sort);
		return it;
	}

	@Override
	public Page<Customer> getCustomersByPageNo(int pageNo, int pageSize) {
		//  prepare Pageable object
		Pageable  pageable=PageRequest.of(pageNo, pageSize);
		//  get requested  page details
		Page<Customer> page=custRepo.findAll(pageable);
		return page;
	}

	@Override
	public Page<Customer> getSortedCustomersByPageNo(int pageNo, int pageSize, boolean ascOrder, String... properties) {
		//prepare Sort object
				Sort sort=Sort.by(ascOrder?Direction.ASC:Direction.DESC, properties);
		//  prepare Pageable object
		Pageable  pageable=PageRequest.of(pageNo, pageSize,sort);
		
		//  get requested  page details
		Page<Customer> page=custRepo.findAll(pageable);
		return page;
	}

	@Override
	public void showCustomersPageByPage(int pageSize) {
	   //get total records count
		long count=custRepo.count();
		// get pages count
		long pagesCount=count/pageSize;
		 pagesCount=(count%pageSize==0)?pagesCount:++pagesCount;
		 
		 for(int i=0;i<pagesCount;++i) {
			 //prepare the Pageable object
			 Pageable pageable=PageRequest.of(i,pageSize);
			 Page<Customer> page=custRepo.findAll(pageable);
			 page.forEach(System.out::println);
			 System.out.println("--------------  "+(page.getNumber()+1)+"/"+page.getTotalPages()+"------------------");
		 }
		
	}

}
