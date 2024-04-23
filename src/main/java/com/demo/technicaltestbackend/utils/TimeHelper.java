package com.demo.technicaltestbackend.utils;

import java.util.Date;

public class TimeHelper {
    public static Date getExpireDate(final Long expires) {
        return new Date(new Date().getTime() + expires);
    }
}
