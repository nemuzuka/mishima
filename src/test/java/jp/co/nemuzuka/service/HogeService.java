package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.meta.HogeModelMeta;
import jp.co.nemuzuka.model.HogeModel;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * データストアテスト用のService.
 * @author kazumune
 */
public class HogeService {

	private HogeService() {
	}

	public static HogeModel newAndPut(String keyStr, String prop1) {
		HogeModel model = new HogeModel();
		model.setProp1(prop1);
		Key key = Datastore.createKey(HogeModelMeta.get(), keyStr);
		model.setKey(key);
		GlobalTransaction.transaction.get().getTransaction().put(model);
		return model;
	}

	public static List<HogeModel> queryAll() {
		return Datastore.query(HogeModelMeta.get()).asList();
	}
}
