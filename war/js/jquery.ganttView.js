/*
jQuery.ganttView v.0.8.8
Copyright (c) 2010 JC Grubbs - jc.grubbs@devmynd.com
MIT License Applies
*/

/*
Options
-----------------
showWeekends: boolean
data: object
cellWidth: number
cellHeight: number
slideWidth: number
dataUrl: string
behavior: {
	clickable: boolean,
	draggable: boolean,
	resizable: boolean,
	onClick: function,
	onDrag: function,
	onResize: function
}
*/

(function (jQuery) {
	
	jQuery.fn.ganttView = function () {
		
		var args = Array.prototype.slice.call(arguments);
		
		if (args.length == 1 && typeof(args[0]) == "object") {
			build.call(this, args[0]);
		}
		
		if (args.length == 2 && typeof(args[0]) == "string") {
			handleMethod.call(this, args[0], args[1]);
		}
	};
	
	function build(options) {
		
		var els = this;
		var defaults = {
			showWeekends: true,
			cellWidth: 21,
			cellHeight: 31,
			slideWidth: 400,
			vHeaderWidth: 100,
			start: null,
			end: null,
			behavior: {
				clickable: true,
				draggable: true,
				resizable: true
			}
		};
		
		var opts = jQuery.extend(true, defaults, options);

		if (opts.data) {
			build();
		} else if (opts.dataUrl) {
			jQuery.getJSON(opts.dataUrl, function (data) { opts.data = data; build(); });
		}

		function build() {
			
			var minDays = Math.floor((opts.slideWidth / opts.cellWidth)  + 5);
			var startEnd = DateUtils.getBoundaryDatesFromData(opts.data, minDays);
			if(opts.start == undefined) {
				opts.start = startEnd[0];
			}
			if(opts.end == undefined) {
				opts.end = startEnd[1];
			}
			els.each(function () {

				var container = jQuery(this);
				var div = jQuery("<div>", { "class": "ganttview" });
				new Chart(div, opts).render();
				container.append(div);
				
				var w = jQuery("div.ganttview-vtheader", container).outerWidth() +
					jQuery("div.ganttview-slide-container", container).outerWidth();
				container.css("width", (w + 2) + "px");
				
				new Behavior(container, opts).apply();
			});
		}
	}

	function handleMethod(method, value) {
		
		if (method == "setSlideWidth") {
			var div = $("div.ganttview", this);
			div.each(function () {
				var vtWidth = $("div.ganttview-vtheader", div).outerWidth();
				$(div).width(vtWidth + value + 1);
				$("div.ganttview-slide-container", this).width(value);
			});
		}
	}

	var Chart = function(div, opts) {
		
		function render() {
			addVtHeader(div, opts.data, opts.cellHeight);

			var slideDiv = jQuery("<div>", {
				"class": "ganttview-slide-container",
				"css": { "width": opts.slideWidth + "px" }
			});
			
			dates = getDates(opts.start, opts.end);
			addHzHeader(slideDiv, dates, opts.cellWidth);
			addGrid(slideDiv, opts.data, dates, opts.cellWidth, opts.showWeekends);
			addBlockContainers(slideDiv, opts.data);
			addBlocks(slideDiv, opts.data, opts.cellWidth, opts.start);
			div.append(slideDiv);
			applyLastClass(div.parent());
		}
		
		var monthNames = ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"];

		// Creates a 3 dimensional array [year][month][day] of every day 
		// between the given start and end dates
		function getDates(start, end) {
			var dates = [];
			dates[start.getFullYear()] = [];
			dates[start.getFullYear()][start.getMonth()] = [start]
			var last = start;
			while (last.compareTo(end) == -1) {
				var next = last.clone().addDays(1);
				if (!dates[next.getFullYear()]) { dates[next.getFullYear()] = []; }
				if (!dates[next.getFullYear()][next.getMonth()]) { 
					dates[next.getFullYear()][next.getMonth()] = []; 
				}
				dates[next.getFullYear()][next.getMonth()].push(next);
				last = next;
			}
			return dates;
		}

		function addVtHeader(div, data, cellHeight) {
			var headerDiv = jQuery("<div>", { "class": "ganttview-vtheader" });
			
			$.each(data, function(i){
				var $a = jQuery("<a />").attr({href:"javascript:void(0)"}).text(data[i].name);
				var no = data[i].no;
				var id = data[i].id;
				$a.click(function(){
		   			openDetailTicketDialog(id);
				});
				if(id == null || id == '') {
					$a = jQuery("<span />").text(data[i].name);
				}
				var itemDiv = jQuery("<div>", { "class": "ganttview-vtheader-item" });
				itemDiv.append(jQuery("<div>", {
					"class": "ganttview-vtheader-item-name",
					"css": { "height": (data[i].series.length * cellHeight) + "px" }
				}).append($a));
				
				var seriesDiv = jQuery("<div>", { "class": "ganttview-vtheader-series" });
				for (var j = 0; j < data[i].series.length; j++) {
					
					var $targetName = jQuery("<div />").text(data[i].series[j].name);
					
					var $ticketNest = "";
					if(data[i].series[j].nestingLevel != 0) {
						var nestingLevel = "子" + data[i].series[j].nestingLevel;
						$ticketNest = jQuery("<span />").text(nestingLevel).css({"margin-right":"3px"}).addClass("child-ticket-label child-ticket-label-info");
					}
					var $closeTicket = "";
					if(data[i].series[j].periodStatusCode == "1" || 
							data[i].series[j].periodStatusCode == "2") {
						$closeTicket = jQuery("<span />").text(data[i].series[j].periodStatusLabel)
							.addClass("child-ticket-label child-ticket-label-warning");
					}
					seriesDiv.append(jQuery("<div>", { "class": "ganttview-vtheader-series-name" })
						.append($targetName));
					var $infoDiv = jQuery("<div />").css({"height":"12px", "line-height":"12px"}).append($ticketNest).append($closeTicket);
					seriesDiv.append($infoDiv);
					
				}
				itemDiv.append(seriesDiv);
				headerDiv.append(itemDiv);
			});
			div.append(headerDiv);
		}

		function addHzHeader(div, dates, cellWidth) {
			var headerDiv = jQuery("<div>", { "class": "ganttview-hzheader" });
			var monthsDiv = jQuery("<div>", { "class": "ganttview-hzheader-months" });
			var daysDiv = jQuery("<div>", { "class": "ganttview-hzheader-days" });
			var totalW = 0;
			var dataFormat = new DateFormat("yyyy/MM/dd");
			var dataFormat2 = new DateFormat("yyyyMMdd");
			var currentDate = dataFormat.format(new Date());

			for (var y in dates) {
				for (var m in dates[y]) {
					var w = dates[y][m].length * cellWidth;
					totalW = totalW + w;
					monthsDiv.append(jQuery("<div>", {
						"class": "ganttview-hzheader-month",
						"css": { "width": (w - 1) + "px" }
					}).append(y + "年" + monthNames[m]));
					for (var d in dates[y][m]) {
						var addClass = "";
						if(currentDate == dataFormat.format(dates[y][m][d])) {
							addClass = "ganttview-currentDate"; 
						}
						addClass = addClass + " date_" + dataFormat2.format(dates[y][m][d]);

						daysDiv.append(jQuery("<div>", { "class": "ganttview-hzheader-day" }).addClass(addClass)
							.append(dates[y][m][d].getDate()));
					}
				}
			}
			monthsDiv.css("width", totalW + "px");
			daysDiv.css("width", totalW + "px");
			headerDiv.append(monthsDiv).append(daysDiv);
			div.append(headerDiv);
		}

		function addGrid(div, data, dates, cellWidth, showWeekends) {
			var gridDiv = jQuery("<div>", { "class": "ganttview-grid" });
			var rowDiv = jQuery("<div>", { "class": "ganttview-grid-row" });
			var dataFormat = new DateFormat("yyyy/MM/dd");
			var currentDate = dataFormat.format(new Date());

			for (var y in dates) {
				for (var m in dates[y]) {
					for (var d in dates[y][m]) {
						var cellDiv = jQuery("<div>", { "class": "ganttview-grid-row-cell" });
						if (DateUtils.isWeekend(dates[y][m][d]) && showWeekends) { 
							cellDiv.addClass("ganttview-weekend"); 
						}
						if(currentDate == dataFormat.format(dates[y][m][d])) {
							cellDiv.removeClass("ganttview-weekend");
							cellDiv.addClass("ganttview-currentDate"); 
						}
						rowDiv.append(cellDiv);
					}
				}
			}
			var w = jQuery("div.ganttview-grid-row-cell", rowDiv).length * cellWidth;
			rowDiv.css("width", w + "px");
			gridDiv.css("width", w + "px");
			for (var i = 0; i < data.length; i++) {
				for (var j = 0; j < data[i].series.length; j++) {
					gridDiv.append(rowDiv.clone());
				}
			}
			div.append(gridDiv);
		}

		function addBlockContainers(div, data) {
			var blocksDiv = jQuery("<div>", { "class": "ganttview-blocks" });
			for (var i = 0; i < data.length; i++) {
				for (var j = 0; j < data[i].series.length; j++) {
					blocksDiv.append(jQuery("<div>", { "class": "ganttview-block-container" }));
				}
			}
			div.append(blocksDiv);
		}

		function addBlocks(div, data, cellWidth, start) {
			var rows = jQuery("div.ganttview-blocks div.ganttview-block-container", div);
			var rowIdx = 0;
			
			$.each(data, function(i){
				var targetData = this;
				var id = this.id;
				var name = this.name;
				$.each(targetData.series, function(j){
					var series = data[i].series[j];
					var size = DateUtils.daysBetween(series.start, series.end) + 1;
					var offset = DateUtils.daysBetween(start, series.start);
					
					var title = "";
					if(series.milestone == false) {
						title = "【件名】" + name;
						
						if(series.name != null && series.name != "") {
							title = title + "\n" + "【担当者】" + series.name;
						}						
						
						if(series.status != null && series.status != '') {
							if(title != null && title != '') {
								title = title + "\n";
							}
							title = title + "【ステータス】" + series.status;
						}
						if(series.periodStatusLabel != null && series.periodStatusLabel != '') {
							title = title + "(" + series.periodStatusLabel + ")";
						}
					} else {
						//マイルストーンの場合
						//開始日、終了日を設定
						title = name;
					}
					
					var dataFormat = new DateFormat("yyyy年M月d日");
					var startDate = null;
					if(series.start != null) {
						if(series.updateStartDate == false) {
							startDate = dataFormat.format(series.start);
						}
					}
					var endDate = null;
					if(series.end != null) {
						if(series.updatePeriod == false) {
							endDate = dataFormat.format(series.end);
						}
					}
					var dateStr = "";
					if(startDate != null) {
						dateStr = startDate + "～";
					}
					
					if(endDate != null) {
						if(dateStr == '') {
							dateStr = "～";
						}
						dateStr = dateStr + endDate;
					}
					if(dateStr != '') {
						dateStr = "【期間】" + dateStr;
					}
					title = title + "\n" +  dateStr;
					
					var cursor = "pointer";
					if(id == null || id == '') {
						cursor = "default";
					}
					var block = jQuery("<div>", {
						"class": "ganttview-block",
						"title": title,
						"css": {
							"width": ((size * cellWidth) - 9) + "px",
							"margin-left": ((offset * cellWidth) + 3) + "px",
							"cursor": cursor
						}
					});
					if(id != null && id != '') {
						block.click(function(){
								openDetailTicketDialog(id);
						});
					}
					
					addBlockData(block, data[i], series);
					if (data[i].series[j].color) {
						block.addClass(data[i].series[j].color);
					}
					block.append(jQuery("<div>", { "class": "ganttview-block-text" }));
					jQuery(rows[rowIdx]).append(block);
					rowIdx = rowIdx + 1;
				});
			});
		}
		
		function addBlockData(block, data, series) {
			// This allows custom attributes to be added to the series data objects
			// and makes them available to the 'data' argument of click, resize, and drag handlers
			var blockData = { id: data.id, name: data.name };
			jQuery.extend(blockData, series);
			block.data("block-data", blockData);
		}

		function applyLastClass(div) {
			jQuery("div.ganttview-grid-row div.ganttview-grid-row-cell:last-child", div).addClass("last");
			jQuery("div.ganttview-hzheader-days div.ganttview-hzheader-day:last-child", div).addClass("last");
			jQuery("div.ganttview-hzheader-months div.ganttview-hzheader-month:last-child", div).addClass("last");
		}
		
		return {
			render: render
		};
	}

	var Behavior = function (div, opts) {
		
		function apply() {
			
			if (opts.behavior.clickable) { 
				bindBlockClick(div, opts.behavior.onClick); 
			}
			
			if (opts.behavior.resizable) { 
				bindBlockResize(div, opts.cellWidth, opts.start, opts.behavior.onResize); 
			}
			
			if (opts.behavior.draggable) { 
				bindBlockDrag(div, opts.cellWidth, opts.start, opts.behavior.onDrag); 
			}
		}

		function bindBlockClick(div, callback) {
			jQuery("div.ganttview-block", div).live("click", function () {
				if (callback) { callback(jQuery(this).data("block-data")); }
			});
		}
		
		function bindBlockResize(div, cellWidth, startDate, callback) {
			jQuery("div.ganttview-block", div).resizable({
				grid: cellWidth, 
				handles: "e,w",
				stop: function () {
					var block = jQuery(this);
					updateDataAndPosition(div, block, cellWidth, startDate);
					if (callback) { callback(block.data("block-data")); }
				}
			});
		}
		
		function bindBlockDrag(div, cellWidth, startDate, callback) {
			jQuery("div.ganttview-block", div).draggable({
				axis: "x", 
				grid: [cellWidth, cellWidth],
				stop: function () {
					var block = jQuery(this);
					updateDataAndPosition(div, block, cellWidth, startDate);
					if (callback) { callback(block.data("block-data")); }
				}
			});
		}
		
		function updateDataAndPosition(div, block, cellWidth, startDate) {
			var container = jQuery("div.ganttview-slide-container", div);
			var scroll = container.scrollLeft();
			var offset = block.offset().left - container.offset().left - 1 + scroll;
			
			// Set new start date
			var daysFromStart = Math.round(offset / cellWidth);
			var newStart = startDate.clone().addDays(daysFromStart);
			block.data("block-data").start = newStart;

			// Set new end date
			var width = block.outerWidth();
			var numberOfDays = Math.round(width / cellWidth) - 1;
			block.data("block-data").end = newStart.clone().addDays(numberOfDays);
			jQuery("div.ganttview-block-text", block).text(numberOfDays + 1);
			
			// Remove top and left properties to avoid incorrect block positioning,
			// set position to relative to keep blocks relative to scrollbar when scrolling
			block.css("top", "").css("left", "")
				.css("position", "relative").css("margin-left", offset + "px");
		}
		
		return {
			apply: apply	
		};
	}

	var ArrayUtils = {
	
		contains: function (arr, obj) {
			var has = false;
			for (var i = 0; i < arr.length; i++) { if (arr[i] == obj) { has = true; } }
			return has;
		}
	};

	var DateUtils = {
		
		daysBetween: function (start, end) {
			if (!start || !end) { return 0; }
			start = Date.parse(start); end = Date.parse(end);
			if (start.getYear() == 1901 || end.getYear() == 8099) { return 0; }
			var count = 0, date = start.clone();
			while (date.compareTo(end) == -1) { count = count + 1; date.addDays(1); }
			return count;
		},
		
		isWeekend: function (date) {
			return date.getDay() % 6 == 0;
		},

		getBoundaryDatesFromData: function (data, minDays) {
			var minStart = new Date(); maxEnd = new Date();
			for (var i = 0; i < data.length; i++) {
				for (var j = 0; j < data[i].series.length; j++) {
					var start = Date.parse(data[i].series[j].start);
					var end = Date.parse(data[i].series[j].end)
					if (i == 0 && j == 0) { minStart = start; maxEnd = end; }
					if (minStart.compareTo(start) == 1) { minStart = start; }
					if (maxEnd.compareTo(end) == -1) { maxEnd = end; }
				}
			}
			// Insure that the width of the chart is at least the slide width to avoid empty
			// whitespace to the right of the grid
			if (DateUtils.daysBetween(minStart, maxEnd) < minDays) {
				maxEnd = minStart.clone().addDays(minDays);
			}
			
			return [minStart, maxEnd];
		}
	};
})(jQuery);