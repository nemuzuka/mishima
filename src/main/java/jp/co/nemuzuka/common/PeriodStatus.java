package jp.co.nemuzuka.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 期限ステータスenum.
 * @author kazumune
 */
public enum PeriodStatus {
	today("1", "期限日"),
	periodDate("2", "期限切れ"),
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
	private PeriodStatus(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/** Map. */
	private static Map<String, PeriodStatus> map = null;
	static {
		map = new HashMap<String, PeriodStatus>();
		for(PeriodStatus target : values()) {
			map.put(target.getCode(), target);
		}
	}

	/**
	 * コード値による取得.
	 * @param code コード値
	 * @return 該当データ(存在しない場合、null)
	 */
	public static PeriodStatus fromCode(String code) {
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
