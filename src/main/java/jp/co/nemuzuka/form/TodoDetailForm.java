package jp.co.nemuzuka.form;

import java.io.Serializable;

import jp.co.nemuzuka.utils.HtmlStringUtils;

/**
 * TODO詳細Form
 * @author k-katagiri
 */
public class TodoDetailForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** TodoForm. */
	public TodoForm form;

	
	//TODO TODOに紐付くコメントの一覧をプロパティに持つ
	

	/**
	 * 表示用内容取得.
	 * @return 表示用内容
	 */
	public String getContentView() {
		return HtmlStringUtils.escapeTextAreaString(form.content);
	}
	
	/**
	 * @return the form
	 */
	public TodoForm getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(TodoForm form) {
		this.form = form;
	}
}
