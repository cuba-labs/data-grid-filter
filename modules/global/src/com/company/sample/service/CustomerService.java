package com.company.sample.service;


import com.company.sample.entity.NotPersistentCustomer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    String NAME = "sample_CustomerService";

    List<NotPersistentCustomer> getCustomers();

    List<NotPersistentCustomer> getCustomers(Map<String, Object> parameters);
}