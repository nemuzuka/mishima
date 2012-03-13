package jp.co.nemuzuka.common;

import java.util.HashMap;
import java.util.Map;

/**
 * ユーザ権限enum.
 * @author kazumune
 */
public enum Authority {
	admin("admin", "管理者"),
	normal("normal", "一般"),
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
	private Authority(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/** Map. */
	private static Map<String, Authority> map = null;
	static {
		map = new HashMap<String, Authority>();
		for(Authority target : values()) {
			map.put(target.getCode(), target);
		}
	}

	/**
	 * コード値による取得.
	 * @param code コード値
	 * @return 該当データ(存在しない場合、null)
	 */
	public static Authority fromCode(String code) {
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
