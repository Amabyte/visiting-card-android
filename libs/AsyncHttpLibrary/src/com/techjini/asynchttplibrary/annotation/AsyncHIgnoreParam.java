package com.techjini.asynchttplibrary.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncHIgnoreParam {

	public String ignore();

}
