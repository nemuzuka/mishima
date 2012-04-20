package jp.co.nemuzuka.service;

import jp.co.nemuzuka.entity.TicketMstEntity;

/**
 * チケットマスタに関するService.
 * @author k-katagiri
 */
public interface TicketMstService {

	/**
	 * チケットマスタ取得.
	 * 指定したプロジェクトに紐付く、チケットマスタ情報を取得します。
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return 該当チケットマスタ情報
	 */
	TicketMstEntity.TicketMst getTicketMst(String projectKeyString);
}
