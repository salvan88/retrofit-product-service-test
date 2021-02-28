package ru.vasiljev.aa.util;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;
import ru.vasiljev.aa.dto.Errors;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ErrorBody {

    private static Errors errors;

    public static Errors getErrorBody(Response<?> response) {

        if (!response.isSuccessful() && response.errorBody() != null) {
            ResponseBody body = response.errorBody();
            Converter<ResponseBody, Errors> converter = RetrofitUtils.getRetrofit().responseBodyConverter(Errors.class, new Annotation[0]);
            try {
                errors = converter.convert(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return errors;
    }
}
