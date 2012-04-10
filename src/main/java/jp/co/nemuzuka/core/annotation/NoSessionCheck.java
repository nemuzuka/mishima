package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 前処理でSession有無をチェックしない場合に付与されます
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoSessionCheck {
}
