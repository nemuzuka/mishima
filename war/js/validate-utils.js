/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 * Licensed under the Apache License v2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

//stringオブジェクトにformatメソッドを追加する
if (String.prototype.format == undefined) {
	String.prototype.format = function(arg) {
		var rep_fn = undefined;
		
		if (typeof arg == "object") {
			//オブジェクトの場合
			rep_fn = function(m, k) { return arg[k]; }
		} else {
			//複数引数だった場合
			var args = arguments;
			rep_fn = function(m, k) { return args[ parseInt(k) ]; }
		}
		return this.replace( /\{(\w+)\}/g, rep_fn );
	}
}

//validate用クラスを作成
var ValidateClass = {
		create: function() {
			return function() {
				this.initialize.apply(this, arguments);
			}
		}
	}
var Validate = ValidateClass.create();
/**
 * rules_arrayには、
 * value:value値
 * option:チェック項目(required)
 * error:エラー文字列
 * error_args:置換文字列
 * が設定される想定
 */
Validate.prototype = {
	//初期化
	initialize:function(){
		this.error_array = [];
		this.rules_array = [];
		this.e = true;
	},
	//空文字
	isEmpty:function(s){
		return !/\S/.test(s);
	},
	//最大文字数
	maxLength:function(value, size){
		if(value == null) {
			return false;
		}
		if(value.length > size) {
			return true;
		}
		return false;
	},
	//日付チェック(8桁の数値であることのチェック)
	//詳細は、サーバ側で行う
	date:function(value) {
		if(value == null || value == '') {
			return false;
		}
		var regex = /^\d{8}$/;
		return (regex.test(value) == false);

	},
	//数値チェック
	isNum:function(value){
		if(value == null || value == '') {
			return false;
		}
		var numRegExp = /^[0-9]+$/
		return (numRegExp.test(value) == false);
	},
	//ルールの追加
	addRules:function(rules){
		this.rules_array.push(rules);
	},
	//チェックメイン
	check:function(){
		this.error_array = [];
		this.e = true;
		for(var i=0;i<this.rules_array.length;i++){
			switch(this.rules_array[i].option){
				//必須入力
				case'required':
					if (this.isEmpty(this.rules_array[i].value)){
						if(this.rules_array[i].error == undefined) {
							this.rules_array[i].error = "{0}は必須です。";
						}
						this.error_array.push(this.rules_array[i].error.format(
								this.rules_array[i].error_args));
						this.e = false;
					}
					break;
				
				//最大文字数
				case 'maxLength':
					if(this.maxLength(this.rules_array[i].value, this.rules_array[i].size)) {
						if(this.rules_array[i].error == undefined) {
							this.rules_array[i].error = "{0}の長さが最大値({1})を超えています。";
						}
						this.error_array.push(this.rules_array[i].error.format(
								this.rules_array[i].error_args, this.rules_array[i].size));
						this.e = false;
					}
					break;
				//日付
				case "date":
					if (this.date(this.rules_array[i].value)){
						if(this.rules_array[i].error == undefined) {
							this.rules_array[i].error = "{0}は日付(yyyyMMdd)ではありません。";
						}
						this.error_array.push(this.rules_array[i].error.format(
								this.rules_array[i].error_args));
						this.e = false;
					}
					break;
				//数値
				case "number":
					if (this.isNum(this.rules_array[i].value)){
						if(this.rules_array[i].error == undefined) {
							this.rules_array[i].error = "{0}は数値ではありません。";
						}
						this.error_array.push(this.rules_array[i].error.format(
								this.rules_array[i].error_args));
						this.e = false;
					}
					break;
			}
		}
	},
	//チェック実行
	//エラーが存在する場合、alertを表示します。
	execute:function(el){
		this.check();
		if(this.e){
			return true;
		}else{
			var endMsg = this.error_array;
			alert(this.error_array.toString().replace(/\,/gi,"\n"));
			return false;
		}
	}
}

