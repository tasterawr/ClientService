package org.loktevik.netcracker.controllers.utils;

public class URLProvider {
    private static String customerServiceUrl = "http://localhost:8081";

    public static String getCustomerServiceUrl() {
        return customerServiceUrl;
    }

    public static void setCustomerServiceUrl(String customerServiceUrl) {
        URLProvider.customerServiceUrl = customerServiceUrl;
    }
}
