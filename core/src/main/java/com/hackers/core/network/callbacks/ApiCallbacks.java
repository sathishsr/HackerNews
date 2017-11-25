package com.hackers.core.network.callbacks;

/**
 * Created by SR on 25/11/17.
 */

public interface ApiCallbacks {

    void handleResponse(Object o);
    void handleError(Object o);
}
