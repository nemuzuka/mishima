package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * システム管理者でなければならない設定を
 * 本Annotationで定義します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemManager {
}
