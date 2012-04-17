package jp.co.nemuzuka.common;

import java.util.HashMap;
import java.util.Map;

/**
 * TODOステータスenum.
 * @author kazumune
 */
public enum TodoStatus {
	nothing("1", "未対応"),
	doing("2", "対応中"),
	finish("9", "完了"),
	;
	
	/** 未完了を意味するコード値. */
	public static final String NO_FINISH = "-1";
	/** 未完了を意味するラベル. */
	public static final String NO_FINISH_LABEL = "未完了";
	
	/** キー. */
	private String code;
	/** label. */
	private String label;
	
	/**
	 * コンストラクタ.
	 * @param code code
	 * @param label ラベル
	 */
	private TodoStatus(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/** Map. */
	private static Map<String, TodoStatus> map = null;
	static {
		map = new HashMap<String, TodoStatus>();
		for(TodoStatus target : values()) {
			map.put(target.getCode(), target);
		}
	}

	/**
	 * コード値による取得.
	 * @param code コード値
	 * @return 該当データ(存在しない場合、null)
	 */
	public static TodoStatus fromCode(String code) {
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
