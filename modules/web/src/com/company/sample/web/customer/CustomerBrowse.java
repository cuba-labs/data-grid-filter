package com.company.sample.web.customer;

import com.company.sample.entity.Customer;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.data.CollectionDatasource;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.Map;
import java.util.UUID;

public class CustomerBrowse extends AbstractLookup {
    @Inject
    private DataGrid<Customer> customersDataGrid;
    @Inject
    private ComponentsFactory componentsFactory;
    @Inject
    private CollectionDatasource<Customer, UUID> customersDs;

    @Override
    public void init(Map<String, Object> params) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setWidthFull();
        textField.setHeight("25px");
        textField.addTextChangeListener(event ->
                customersDs.refresh(ParamsMap.of("name", "(?i)%" + event.getText() + "%")));

        DataGrid.HeaderRow filterRow = customersDataGrid.appendHeaderRow();
        filterRow.getCell("name").setComponent(textField);
    }
}