package ru.slisarenko.objectmapper.config;

public class JsonQueryTestStringStatic {
    public static final String CUSTOMER_CREATE_JSON = """
            {
             "firstName":"testCustomer",
             "lastName":"testCustomer",
             "email":"testCustomer@mail.ru",
             "contactNumber":"+7(951)555-55-55"
             }
            """;
    public static final String CUSTOMER_CREATE_ERROR_JSON = """
            {
             "firstName":"testCustomer",
             "lastName":"testCustomer",
             "email":"testCustomermail.ru",
             "contactNumber":"rrrr"
             }
            """;
    public static final String CUSTOMER_INFORMATION_JSON = """
            {
             "email":"testCustomer@mail.ru",
             "contactNumber":"+7(951)555-55-55"
            }
            """;
    public static final String PRODUCT_CREATE_JSON = """
            {
             "name":"testProduct",
             "description":"testProduct",
             "price":123.54,
             "quantityInStock":10
            }
            """;
    public static final String PRODUCT_CREATE_ERROR_PRICE_NULL_QUANTITY_0_JSON = """
            {
             "name":"testProduct",
             "description":"testProduct",
             "price":null,
             "quantityInStock":0
            }
            """;
    public static final String PRODUCT_UPDATE_JSON = """
            {
             "name":"testProductUpdate",
             "description":"testProductUpdate",
             "price":123.54,
             "quantityInStock":145
            }
            """;
    public static final String ORDER_CREATE_JSON = """
            {
             "emailOrContactNumber":"testCustomer@mail.ru",
             "productIds":%s,
             "shippingAddress":"shippingAddress"    
            }
            """;

}
