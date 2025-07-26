package com.project.service;

import com.project.dao.CustomerDao;
import com.project.dto.ApiResponse;
import com.project.dto.CustomerDto;
import com.project.dto.CustomerLoginDto;
import com.project.entities.Customer;
import com.project.exceptions.ResourseNotFound;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    CustomerDao customerDao;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public ApiResponse saveCustomer(CustomerDto customer) {
        Customer customer1=modelMapper.map(customer,Customer.class);
        customerDao.save(customer1);
        return new ApiResponse("Customer created "+customer1.getId());
    }

    @Override
    public ApiResponse validateCustomer(CustomerLoginDto customerLoginDto) {
        Customer customer= customerDao.
                findByEmail(customerLoginDto.getEmail()).
                orElseThrow(() ->
                new ResourseNotFound("Customer Not Found"));

        if(!customer.getPassword().equals(customerLoginDto.getPassword()))
        {
            throw new ResourseNotFound("In valid password");
        }
        return new ApiResponse("Login successful for customer "+customer.getId());
    }

    @Override
    public List<Customer> getAllCustomerForEvent(Long id) {
        return customerDao.findAll();
    }

    @Override
    public ApiResponse updateProfile(Long id , CustomerDto customerDto) {
        Customer customer=customerDao.findById(id).
                orElseThrow(()->new ResourseNotFound("Customer not found"));

        Customer updatedCustomer = modelMapper.map(customerDto, Customer.class);
        updatedCustomer.setId(customer.getId()); // preserve original ID
        updatedCustomer.setCreatedBy(customer.getCreatedBy()); // if needed
        updatedCustomer.setCreationDate(customer.getCreationDate());
        updatedCustomer.setUpdatedOn(customer.getUpdatedOn());
        customerDao.save(updatedCustomer);



        return new ApiResponse("Customer details updated successfully "+updatedCustomer.getId());
    }

    @Override
    public ApiResponse changePassword(Long id, String password) {
       Customer customer=customerDao.findById(id)
               .orElseThrow(()->new ResourseNotFound("Customer Not Found"));
       customer.setPassword(password);
       customerDao.save(customer);
       return new ApiResponse("Password updated successfully for " + customer.getId());
    }

    @Override
    public ApiResponse deleteCustomer(Long id) {
        Customer customer=customerDao.findById(id)
                .orElseThrow(()->new ResourseNotFound("Customer Not Found"));
        customer.setDeleted(true);
        customerDao.save(customer);
        return new ApiResponse("Customer deleted successfully "+customer.getId());
    }

	@Override
	public List<Customer> getAllCustomers() {
		return customerDao.findAll();
	}
}
