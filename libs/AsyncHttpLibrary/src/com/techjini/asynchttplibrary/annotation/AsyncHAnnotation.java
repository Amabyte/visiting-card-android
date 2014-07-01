package com.techjini.asynchttplibrary.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AsyncHAnnotation {

	public String prefix();

	public String postfix();

	public String overrideName();
}
