/**
 * カレンダーの表示方法を指定
 */
jQuery.extend(jQuery.mobile.datebox.prototype.options.lang, {
	'ja': {
		setDateButtonLabel: "日付を設定",
		setTimeButtonLabel: "時刻を設定",
		setDurationButtonLabel: "期間を設定",
		calTodayButtonLabel: "今日へジャンプ",
		titleDateDialogLabel: "日付を選択します。",
		titleTimeDialogLabel: "時間を選択します。",
		daysOfWeek: ["日曜", "月曜", "火曜", "水曜", "木曜", "金曜", "土曜"],
		daysOfWeekShort: ["日", "月", "火", "水", "木", "金", "土"],
		monthsOfYear: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
		monthsOfYearShort: ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
		durationLabel: ["日", "時間", "分", "秒"],
		durationDays: ["日", "日"],
		tooltip: "開いている日付の選択",
		nextMonth: "次の月",
		prevMonth: "前月",
		timeFormat: 24,
		headerFormat: '%A, %B %-d, %Y',
		dateFieldOrder: ['m', 'd', 'y'],
		timeFieldOrder: ['h', 'i', 'a'],
		slideFieldOrder: ['y', 'm', 'd'],
		dateFormat: "%Y/%m/%d",
		useArabicIndic: false,
		isRTL: false,
		calStartDay: 0,
		clearButton: "クリア",
		durationOrder: ['d', 'h', 'i', 's'],
		meridiem: ["AM", "PM"],
		timeOutput: "%k:%M",
		durationFormat: "%Dd %DA, %Dl:%DM:%DS"
	}
});
jQuery.extend(jQuery.mobile.datebox.prototype.options, {
	useLang: 'ja'
});
