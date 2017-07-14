package com.company.sample.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.cuba.core.entity.AbstractNotPersistentEntity;
import com.haulmont.chile.core.annotations.NamePattern;

@NamePattern("%s|name")
@MetaClass(name = "sample$NotPersistentCustomer")
public class NotPersistentCustomer extends AbstractNotPersistentEntity {
    private static final long serialVersionUID = 1837357727113741060L;

    @MetaProperty(mandatory = true)
    protected String name;

    @MetaProperty
    protected String email;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }


}