package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * プロジェクト管理者機能でなければならない設定を
 * 本Annotationで定義します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectAdmin {
}
