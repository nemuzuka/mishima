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
package jp.co.nemuzuka.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

/**
 * 子Ticket管理用Entity.
 * @author k-katagiri
 */
public class ChildKeyListEntity {

	/** 子TicketKeyList. */
	public List<Key> childKeys = new ArrayList<Key>();
	
	/** 
	 * 子TicketKey管理Map.
	 * Keyは子TicketのKey、Valueは子TicketKeyListのindex
	 */
	public Map<Key, Integer> childMap = new HashMap<Key, Integer>();
	
	/** 
	 * 孫Ticket管理用EntityList.
	 * 子TicketKeyの更に子のTicket情報を管理します。
	 * 子TicketKeyListのサイズ = 孫Ticket管理用EntityListのサイズの関係を維持します
	 * 子のTicket情報が存在しない場合、要素にはnullが設定されます。
	 */
	public List<ChildKeyListEntity> grandchildList = new ArrayList<ChildKeyListEntity>();
	
}
