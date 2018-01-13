package com.jdkgroup.constant;

public interface RestConstant {

    //BROWSER URL https://www.coindesk.com/api/

    String BASE_URL = "https://api.coindesk.com/v1/bpi/";
    String IMAGE_URL = "";

    String API_GET_CURRENT_PRICE = "currentprice.json";
    String API_GET_CLOSE = "historical/close.json";
    String API_GET_SUPPORTED_CURRENCIES = "supported-currencies.json";
    String API_GET_CURRENT_PRICE_WITH_CURRENCY = "currentprice";

    int REQUEST_AUTH = 1;
    int REQUEST_NO_AUTH = 0;

    /* DB STATUS */
    int TYPE_DELETION = 0;
    int TYPE_INSERTION = 1;
    int TYPE_MODIFICATION = 2;

    /* ERROR CODE */
    int ERROR_SERVICE_UNAVAILABLE = 503;
    int ERROR_INTERNAL_SERVER = 500;
    int ERROR_BAD_Gateway = 502;
    int ERROR_NOT_FOUND = 404;
    int ERROR_Forbidden = 403;
    int ERROR_PRECONDITION_FAILED = 403;
    int ERROR_OK = 200;
    int ERROR_No_Content = 204;
    int ERROR_Method_Not_Allowed = 405;
    int ERROR_RESPONSE_ERROR = 400;
}
