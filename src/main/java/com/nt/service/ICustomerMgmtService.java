package com.nt.service;

import org.springframework.data.domain.Page;

import com.nt.entity.Customer;

public interface ICustomerMgmtService {
    public  Iterable<Customer> getAllCustomers(boolean ascOrder, String ...properties);
    public   Page<Customer>  getCustomersByPageNo(int pageNo,int pageSize);
    public   Page<Customer>  getSortedCustomersByPageNo(int pageNo,int pageSize,boolean ascOrder,String ...properties);
    public   void   showCustomersPageByPage(int pageSize);
    
    
}
