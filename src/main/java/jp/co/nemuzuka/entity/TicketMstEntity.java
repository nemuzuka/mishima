package jp.co.nemuzuka.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * チケット用選択肢マスタ.
 * @author k-katagiri
 */
public class TicketMstEntity implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * チケットマスタMap.
	 * KeyはプロジェクトKey文字列、valueはそれに紐付くマスタ情報になります。
	 */
	public Map<String, TicketMst> map = new HashMap<String, TicketMstEntity.TicketMst>();
	
	/**
	 * チケットマスタ.
	 * @author k-katagiri
	 */
	public static class TicketMst implements Serializable {
		
		/**
		 * serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;
		
		/** 更新開始時刻. */
		//この時間を超えた場合、チケットマスタの値を再設定する
		public Date refreshStartTime;
		
		/** 優先度選択肢. */
		public List<LabelValueBean> priorityList;
		
		/** ステータス選択肢. */
		public List<LabelValueBean> statusList;

		/** 未完了を意味するステータス. */
		public String[] openStatus;
		
		/** ステータス検索条件. */
		public List<LabelValueBean> searchStatusList;

		/** 種別選択肢. */
		public List<LabelValueBean> kindList;
		
		/** カテゴリ選択肢. */
		public List<LabelValueBean> categoryList;
		
		/** マイルストーン選択肢. */
		public List<LabelValueBean> milestoneList;
		
		/** バージョン選択肢. */
		public List<LabelValueBean> versionList;
	}
}
