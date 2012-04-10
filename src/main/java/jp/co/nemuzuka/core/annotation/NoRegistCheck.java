package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * ログインユーザがメンバーとして登録されているかのチェックを行わない時に
 * 付与します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NoRegistCheck {
}
