package jp.co.nemuzuka.core.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * Session情報.
 * @author kazumune
 */
public class UserInfo implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 更新開始時刻. */
	//この時間を超えた場合、参照可能プロジェクトListを再設定する
	public Calendar refreshStartTime;
	
	/** 参照可能プロジェクトList. */
	public List<LabelValueBean> projectList;
	
}
