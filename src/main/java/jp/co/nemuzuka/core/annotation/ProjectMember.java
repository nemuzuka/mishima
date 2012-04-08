package jp.co.nemuzuka.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * プロジェクト参加者でなければならない機能を
 * 本Annotationで定義します。
 * @author k-katagiri
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectMember {
}
