package com.company.sample.service;

import com.company.sample.entity.Customer;
import com.company.sample.entity.NotPersistentCustomer;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.View;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(CustomerService.NAME)
public class CustomerServiceBean implements CustomerService {

    @Inject
    private DataManager dataManager;

    @Override
    public List<NotPersistentCustomer> getCustomers() {
        return getCustomers(null);
    }

    @Override
    public List<NotPersistentCustomer> getCustomers(Map<String, Object> parameters) {
        LoadContext<Customer> loadContext = LoadContext.create(Customer.class)
                .setView(View.LOCAL)
                .setQuery(createQuery(parameters));

        List<Customer> customers = dataManager.loadList(loadContext);

        if (CollectionUtils.isNotEmpty(customers)) {
            return customers.stream()
                    .map(customer -> {
                        NotPersistentCustomer notPersistentCustomer = new NotPersistentCustomer();
                        notPersistentCustomer.setName(customer.getName());
                        notPersistentCustomer.setEmail(customer.getEmail());
                        return notPersistentCustomer;
                    })
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    private LoadContext.Query createQuery(Map<String, Object> parameters) {
        StringBuilder sb = new StringBuilder();
        sb.append("select e from sample$Customer e");

        // remove records with empty value from map
        Map<String, Object> cleanedParameters = cleanParameters(parameters);

        // we need to construct a query in case of passed filter parameters
        String paramSeparator = " and ";
        if (MapUtils.isNotEmpty(cleanedParameters)) {
            sb.append(" where ");
            for (String key : cleanedParameters.keySet()) {
                // for every entry add a 'contains ignore case' comparison
                sb.append("e.")
                        .append(key)
                        .append(" like :")
                        .append(key)
                        .append(paramSeparator);
            }
        }

        // remove last separator
        String queryString = StringUtils.chomp(sb.toString(), paramSeparator);
        LoadContext.Query query = new LoadContext.Query(queryString);

        if (MapUtils.isNotEmpty(cleanedParameters)) {
            for (Map.Entry<String, Object> entry : cleanedParameters.entrySet()) {
                // for every entry add a particular value of parameter
                query.setParameter(entry.getKey(), "(?i)%" + entry.getValue() + "%");
            }
        }

        return query;
    }

    private Map<String, Object> cleanParameters(Map<String, Object> parameters) {
        if (MapUtils.isNotEmpty(parameters)) {
            Map<String, Object> cleanedParameters = new HashMap<>(parameters);
            parameters.entrySet().stream()
                    .filter(entry -> entry.getValue() == null
                            || (entry.getValue() instanceof String
                            && ((String) entry.getValue()).isEmpty()))
                    .forEach(entry -> cleanedParameters.remove(entry.getKey()));

            return cleanedParameters;
        } else {
            return parameters;
        }
    }
}