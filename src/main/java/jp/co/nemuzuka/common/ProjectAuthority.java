package jp.co.nemuzuka.common;

import java.util.HashMap;
import java.util.Map;

/**
 * プロジェクトユーザ権限enum.
 * @author kazumune
 */
public enum ProjectAuthority {
	type1("projectAdmin", "プロジェクト管理者"),
	type2("developer", "開発者"),
	type3("reporter", "報告者"),
	;
	
	/** キー. */
	private String code;
	/** label. */
	private String label;
	
	/**
	 * コンストラクタ.
	 * @param code code
	 * @param label ラベル
	 */
	private ProjectAuthority(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/** Map. */
	private static Map<String, ProjectAuthority> map = null;
	static {
		map = new HashMap<String, ProjectAuthority>();
		for(ProjectAuthority target : values()) {
			map.put(target.getCode(), target);
		}
	}

	/**
	 * コード値による取得.
	 * @param code コード値
	 * @return 該当データ(存在しない場合、null)
	 */
	public static ProjectAuthority fromCode(String code) {
		return map.get(code);
	}
	
	/**
	 * @return key
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return label
	 */
	public String getLabel() {
		return label;
	}
}
