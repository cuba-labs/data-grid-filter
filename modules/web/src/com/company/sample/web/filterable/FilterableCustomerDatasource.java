package com.company.sample.web.filterable;

import com.company.sample.entity.NotPersistentCustomer;
import com.company.sample.service.CustomerService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.gui.data.impl.CustomCollectionDatasource;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class FilterableCustomerDatasource extends CustomCollectionDatasource<NotPersistentCustomer, UUID> {

    private CustomerService customerService = AppBeans.get(CustomerService.NAME);
    private List<NotPersistentCustomer> customers;
    private boolean filtering = false;

    @Override
    protected Collection<NotPersistentCustomer> getEntities(Map<String, Object> params) {
        if (filtering) {
            if (MapUtils.isNotEmpty(params)) {
                return customers.stream()
                        .filter(customer -> {
                            for (Map.Entry<String, Object> entry : params.entrySet()) {
                                String value = customer.getValue(entry.getKey());
                                String filterValue = (String) entry.getValue();
                                if (!StringUtils.containsIgnoreCase(value, filterValue)) {
                                    return false;
                                }
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
            }
        } else {
            customers = customerService.getCustomers(params);
        }
        return customers;
    }

    public void filter(Map<String, Object> params) {
        filtering = true;
        refresh(params);
        filtering = false;
    }
}
