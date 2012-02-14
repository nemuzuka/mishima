package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * valitdationの設定を本Annotationで定義します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Validation {
	String method();
	String input();
}
