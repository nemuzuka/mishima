/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.common;

import java.util.HashMap;
import java.util.Map;

/**
 * タイムゾーンenum.
 * @author kazumune
 */
public enum TimeZone {
	GMT_M_12("GMT-12:00","ベーカー島、ハウランド島"),
	GMT_M_11("GMT-11:00","アメリカ領サモア、ニウエ"),
	GMT_M_10("GMT-10:00","ハワイ、アリューシャン列島西部"),
	GMT_M_9("GMT-09:00","アラスカ"),
	GMT_M_8("GMT-08:00","アメリカ・カナダ・メキシコ（太平洋時間）"),
	GMT_M_7("GMT-07:00","アメリカ・カナダ・メキシコ（山岳部時間）"),
	GMT_M_6("GMT-06:00","アメリカ・カナダ・メキシコ（中部時間）"),
	GMT_M_5("GMT-05:00","アメリカ・カナダ（東部時間）、キューバ、ペルー"),
	GMT_M_4_30("GMT-04:30","ベネズエラ"),
	GMT_M_4("GMT-04:00","カナダ（大西洋時間）、小アンティル諸島の大部分、ブラジル（アマゾン時間）、チリ"),
	GMT_M_3_30("GMT-03:30","カナダ（ニューファンドランド）"),
	GMT_M_3("GMT-03:00","ブラジル（ブラジル時間）、アルゼンチン、グリーンランド"),
	GMT_M_2("GMT-02:00","ブラジル（フェルナンド・デ・ノローニャ島）"),
	GMT_M_1("GMT-01:00","アゾレス諸島、カーボベルデ"),
	GMT("GMT","イギリス、アイルランド、ポルトガル、アイスランド"),
	GMT_P_1("GMT+01:00","中央ヨーロッパ時間（フランス、ドイツ等）、西アフリカ時間（ナイジェリア、コンゴ等）"),
	GMT_P_2("GMT+02:00","東ヨーロッパ時間（フィンランド、ギリシャ等）、中央アフリカ時間、南アフリカ"),
	GMT_P_3("GMT+03:00","ロシア（カリーニングラード）、ウクライナ、ベラルーシ、サウジアラビア、東アフリカ時間"),
	GMT_P_3_30("GMT+03:30","イラン"),
	GMT_P_4("GMT+04:00","ロシア（モスクワ）、アゼルバイジャン、グルジア、アラブ首長国連邦、モーリシャス"),
	GMT_P_4_30("GMT+04:30","アフガニスタン"),
	GMT_P_5("GMT+05:00","パキスタン"),
	GMT_P_5_30("GMT+05:30","インド"),
	GMT_P_5_45("GMT+05:45","ネパール"),
	GMT_P_6("GMT+06:00","ロシア（エカテリンブルグ）、カザフスタン、バングラディシュ"),
	GMT_P_6_30("GMT+06:30","ミャンマー、ココス諸島"),
	GMT_P_7("GMT+07:00","ロシア（オムスク）、モンゴル、タイ、ベトナム、ジャカルタ"),
	GMT_P_8("GMT+08:00","ロシア（クラスノヤルスク）、中国、モンゴル、マレーシア、オーストラリア（西オーストラリア州）"),
	GMT_P_8_45("GMT+08:45","オーストラリア（ユークラ）"),
	GMT_P_9("GMT+09:00","日本、ロシア（イルクーツク）、韓国、パラオ"),
	GMT_P_9_30("GMT+09:30","オーストラリア（ノーザンテリトリー、南オーストラリア州）"),
	GMT_P_10("GMT+10:00","ロシア（ヤクーツク）、グアム、オーストラリア東部時間"),
	GMT_P_10_30("GMT+10:30","オーストラリア（ロード・ハウ島）"),
	GMT_P_11("GMT+11:00","ロシア（ウラジオストク）、ソロモン諸島、ニューカレドニア"),
	GMT_P_12("GMT+12:00","ロシア（マガダン）、ニュージーランド、フィジー、キリバス（ギルバート諸島）"),
	GMT_P_12_45("GMT+12:45","ニュージーランド（チャタム島）"),
	GMT_P_13("GMT+13:00","トンガ、キリバス（フェニックス諸島）、サモア"),
	GMT_P_14("GMT+14:00","キリバス（ライン諸島）、トケラウ"),
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
	private TimeZone(String code, String label) {
		this.code = code;
		this.label = label;
	}

	/** Map. */
	private static Map<String, TimeZone> map = null;
	static {
		map = new HashMap<String, TimeZone>();
		for(TimeZone target : values()) {
			map.put(target.getCode(), target);
		}
	}

	/**
	 * コード値による取得.
	 * @param code コード値
	 * @return 該当データ(存在しない場合、null)
	 */
	public static TimeZone fromCode(String code) {
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
