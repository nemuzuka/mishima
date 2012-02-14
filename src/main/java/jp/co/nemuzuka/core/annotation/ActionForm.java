package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 本Annotationが付与されているJavaBeanに対して、
 * リクエストパラメータの値を設定します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionForm {
}
