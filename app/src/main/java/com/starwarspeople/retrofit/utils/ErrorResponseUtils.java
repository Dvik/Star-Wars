package com.starwarspeople.retrofit.utils;

import com.starwarspeople.data.model.PeopleResponseError;
import com.starwarspeople.retrofit.rest.PeopleDataClient;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Divya on 1/22/2017.
 */

public class ErrorResponseUtils {

    public static PeopleResponseError parseError(Response<?> response) {
        Converter<ResponseBody, PeopleResponseError> converter = PeopleDataClient.getClient()
                .responseBodyConverter(PeopleResponseError.class, new Annotation[0]);

        PeopleResponseError error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new PeopleResponseError();
        }

        return error;
    }

}
