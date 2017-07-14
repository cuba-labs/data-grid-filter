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
        // check if we're in filtering mode and don't want to load data from DB
        if (filtering) {
            if (MapUtils.isNotEmpty(params)) {
                return customers.stream()
                        .filter(customer -> {
                            for (Map.Entry<String, Object> entry : params.entrySet()) {
                                // we assume that entry's key correspond to an entity attribute
                                // so we can get value from entity by attribute name
                                String value = customer.getValue(entry.getKey());
                                // and entry's value is a string, so we can use containsIgnoreCase util method
                                String filterValue = (String) entry.getValue();
                                if (!StringUtils.containsIgnoreCase(value, filterValue)) {
                                    // if one of the attributes' value does not match to the filtering rules,
                                    // we exclude entity
                                    return false;
                                }
                            }
                            return true;
                        })
                        .collect(Collectors.toList());
            }
        } else {
            // if we're not in filtering mode, then reload data from DB using service
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
