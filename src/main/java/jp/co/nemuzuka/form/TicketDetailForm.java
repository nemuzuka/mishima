package jp.co.nemuzuka.form;

import java.io.Serializable;
import java.util.List;

import jp.co.nemuzuka.entity.CommentModelEx;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.utils.HtmlStringUtils;

/**
 * Ticket詳細Form
 * @author k-katagiri
 */
public class TicketDetailForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** TicketForm. */
	public TicketForm form;

	/** コメント一覧. */
	public List<CommentModelEx> commentList;
	
	/** 親チケット情報. */
	public TicketModel parentTicket;
	
	/** 子チケット情報. */
	public List<TicketModel> childTicketList;
	
	/**
	 * 表示用内容取得.
	 * @return 表示用内容
	 */
	public String getContentView() {
		return HtmlStringUtils.escapeTextAreaString(form.content);
	}
	
	/**
	 * 終了条件取得.
	 * @return 終了条件
	 */
	public String getEndConditionView() {
		return HtmlStringUtils.escapeTextAreaString(form.endCondition);
	}
	

	/**
	 * @return the parentTicket
	 */
	public TicketModel getParentTicket() {
		return parentTicket;
	}

	/**
	 * @param parentTicket the parentTicket to set
	 */
	public void setParentTicket(TicketModel parentTicket) {
		this.parentTicket = parentTicket;
	}

	/**
	 * @return the childTicketList
	 */
	public List<TicketModel> getChildTicketList() {
		return childTicketList;
	}

	/**
	 * @param childTicketList the childTicketList to set
	 */
	public void setChildTicketList(List<TicketModel> childTicketList) {
		this.childTicketList = childTicketList;
	}

	/**
	 * @return the form
	 */
	public TicketForm getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(TicketForm form) {
		this.form = form;
	}

	/**
	 * @return the commentList
	 */
	public List<CommentModelEx> getCommentList() {
		return commentList;
	}

	/**
	 * @param commentList the commentList to set
	 */
	public void setCommentList(List<CommentModelEx> commentList) {
		this.commentList = commentList;
	}
}
