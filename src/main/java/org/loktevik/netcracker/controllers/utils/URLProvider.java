package org.loktevik.netcracker.controllers.utils;

public class URLProvider {
    private static String customerServiceUrl = "https://node110268-env-8474609.mircloud.ru";

    public static String getCustomerServiceUrl() {
        return customerServiceUrl;
    }

    public static void setCustomerServiceUrl(String customerServiceUrl) {
        URLProvider.customerServiceUrl = customerServiceUrl;
    }
}
