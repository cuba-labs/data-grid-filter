package com.company.sample.web.notpersistent;

import com.company.sample.entity.NotPersistentCustomer;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.DataGrid;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.xml.layout.ComponentsFactory;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class NotPersistentCustomerBrowse extends AbstractLookup {
    @Inject
    private DataGrid<NotPersistentCustomer> customersDataGrid;
    @Inject
    private ComponentsFactory componentsFactory;
    @Inject
    private CustomerDatasource customersDs;

    @Override
    public void init(Map<String, Object> params) {
        TextField nameFilter = createTextFieldFilter("name");
        TextField emailFilter = createTextFieldFilter("email");

        DataGrid.HeaderRow filterRow = customersDataGrid.appendHeaderRow();
        filterRow.getCell("name").setComponent(nameFilter);
        filterRow.getCell("email").setComponent(emailFilter);
    }

    private TextField createTextFieldFilter(String property) {
        TextField textField = componentsFactory.createComponent(TextField.class);
        textField.setWidthFull();
        textField.setHeight("25px");
        textField.addTextChangeListener(event -> {
            Map<String, Object> params = new HashMap<>(customersDs.getLastRefreshParameters());
            params.put(property, event.getText());
            customersDs.refresh(params);
        });

        return textField;
    }
}