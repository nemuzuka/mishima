package jp.co.nemuzuka.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * 現在日付、現在日時を取得するUtils.
 * ※本プロジェクトでは、本Utilsを使用して日付を取得することとします。
 * プログラム内で new Date()等をすることを禁止します。
 * @author k-katagiri
 *
 */
public class CurrentDateUtils {

	/** インスタンス. */
	private static CurrentDateUtils currentDateUtils = new CurrentDateUtils();

	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CurrentDateUtils getInstance() {
		return currentDateUtils;
	}

	/**
	 * 現在日付取得.
	 * 時間以降の情報は切り捨てます。
	 * @return 現在日付
	 */
	public Date getCurrentDate() {
		return DateUtils.truncate(new Date(), Calendar.DAY_OF_MONTH);
	}

	/**
	 * 最大日付取得.
	 * システム的に使用されないであろう最大日付を設定します。
	 * @return 最大日付
	 */
	public Date getMaxDate() {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy/MM/dd").parse("2999/12/31");
		} catch (ParseException e) {}

		return date;
	}

	/**
	 * 現在日時取得.
	 * @return 現在日時
	 */
	public Timestamp getCurrentTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	/**
	 * 最大日時取得.
	 * システム的に使用されないであろう最大日時を設定します。
	 * @return 最大日時
	 */
	public Timestamp getMaxCurrentTimestamp() {
		Date date = getMaxDate();
		return new Timestamp(date.getTime());

	}
}
