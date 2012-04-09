package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.meta.Slim3ModelMeta;
import jp.co.nemuzuka.model.Slim3Model;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * データストアテスト用のService.
 * @author kazumune
 */
public class Slim3Service {

	private Slim3Service() {
	}

	public static Slim3Model newAndPut(String prop1) {
		Slim3Model model = new Slim3Model();
		model.setProp1(prop1);
		Key key = GlobalTransaction.transaction.get().getTransaction().put(model);
		model.setKey(key);
		return model;
	}

	public static List<Slim3Model> queryAll() {
		return Datastore.query(Slim3ModelMeta.get()).asList();
	}
}
