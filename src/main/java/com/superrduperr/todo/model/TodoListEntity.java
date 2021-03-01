package com.superrduperr.todo.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Entity class for table TBL_TODO_LISTS
 * @author Vasavi
 *
 */
@Entity
@Table(name = "TBL_TODO_LIST")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","items"})
public class TodoListEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer listId;
	
	@Column(name = "list_name")
	private String listName;
	
	
	@Column(name = "status")
	private String status;
	
	@OneToMany(mappedBy = "listEntity",  fetch = FetchType.EAGER, cascade = {
	        CascadeType.ALL
	    })
	    private List< TodoItemEntity > items= new ArrayList<TodoItemEntity>();

	public Integer getListId() {
		return listId;
	}

	public void setListId(Integer listId) {
		this.listId = listId;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TodoItemEntity> getItems() {
		return items;
	}

	public void setItems(List<TodoItemEntity> items) {
		this.items = items;
	}

	public TodoListEntity() {
		super();
	}

	public TodoListEntity(Integer listId, String listName, List<TodoItemEntity> items) {
		super();
		this.listId = listId;
		this.listName = listName;
		this.items = items;
	}

	@Override
	public String toString() {
		return "TodoListsEntity [listId=" + listId + ", listName=" + listName + ", status=" + status + ", items="
				+ items + "]";
	}
	

}
