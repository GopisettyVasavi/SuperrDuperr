package com.superrduperr.todo.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 *  Entity class for table TBL_TODO_ITEM
 * @author Vasavi
 *
 */
@Entity
@Table(name = "TBL_TODO_ITEM")
public class TodoItemEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer itemId;
	
	@Column(name = "item_name")
	private  String itemName;
	
	@Column(name = "status")
	private  String status;
	
	@Column(name = "tag_name")
	private  String tagName;
	
	@Column(name = "activation_flag")
	private  String activationFlag;
	
	@Column(name = "remainder")
	private  LocalDateTime remainder;

	 @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	    @JoinColumn(name = "list_id")
	    private TodoListEntity listEntity;

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getActivationFlag() {
		return activationFlag;
	}

	public void setActivationFlag(String activationFlag) {
		this.activationFlag = activationFlag;
	}

	public LocalDateTime getRemainder() {
		return remainder;
	}

	public void setRemainder(LocalDateTime remainder) {
		this.remainder = remainder;
	}

	public TodoListEntity getListEntity() {
		return listEntity;
	}

	public void setListEntity(TodoListEntity listEntity) {
		this.listEntity = listEntity;
	}

	public TodoItemEntity(Integer itemId, String itemName, String status, String tagName, String activationFlag,
			LocalDateTime remainder, TodoListEntity listEntity) {
		super();
		this.itemId = itemId;
		this.itemName = itemName;
		this.status = status;
		this.tagName = tagName;
		this.activationFlag = activationFlag;
		this.remainder = remainder;
		this.listEntity = listEntity;
	}

	public TodoItemEntity() {
		super();
	}

	@Override
	public String toString() {
		return "TodoItemEntity [itemId=" + itemId + ", itemName=" + itemName + ", status=" + status + ", tagName="
				+ tagName + ", activationFlag=" + activationFlag + ", remainder=" + remainder + ", listEntity="
				+ listEntity + "]";
	}

	
	 
	 

}
