package com.company.sample.web.notpersistentcustomer;

import com.company.sample.entity.NotPersistentCustomer;
import com.company.sample.service.CustomerService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class CustomerDatasource extends CustomCollectionDatasource<NotPersistentCustomer, UUID> {

    private CustomerService customerService = AppBeans.get(CustomerService.NAME);

    @Override
    protected Collection<NotPersistentCustomer> getEntities(Map<String, Object> params) {
        return customerService.getCustomers(params);
    }
}
