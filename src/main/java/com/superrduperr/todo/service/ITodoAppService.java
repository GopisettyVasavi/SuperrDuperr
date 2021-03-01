package com.superrduperr.todo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.superrduperr.todo.exception.ApplicationException;
import com.superrduperr.todo.exception.InvalidInputException;
import com.superrduperr.todo.exception.ResourceNotFoundException;
import com.superrduperr.todo.model.TodoItemEntity;
import com.superrduperr.todo.model.TodoListEntity;
/**
 *  Service methods for creating/fetching todo list and items
 * 
 * @author Vasavi
 *
 */
public interface ITodoAppService {

	public List<TodoListEntity> getTodoLists() throws ApplicationException;

	public TodoListEntity getListById(Integer listId) throws ResourceNotFoundException, ApplicationException;

	public List<TodoItemEntity> getItems(Integer listId) throws ResourceNotFoundException, ApplicationException;

	public TodoItemEntity getItemById(Integer itemId) throws ResourceNotFoundException, ApplicationException;

	public TodoListEntity createOrUpdateList(TodoListEntity list) throws  ApplicationException;

	public TodoItemEntity createOrUpdateItem(TodoItemEntity item, Integer listId) throws ResourceNotFoundException,InvalidInputException;

	public TodoItemEntity markItem(Integer itemId) throws ResourceNotFoundException, ApplicationException;

	public TodoItemEntity deleteItem(Integer itemId) throws ResourceNotFoundException, ApplicationException;

	public TodoItemEntity restoreItem(Integer itemId) throws ResourceNotFoundException, ApplicationException;

	public TodoItemEntity tagItem(Integer itemId, String tag) throws ResourceNotFoundException, ApplicationException;

	public List<TodoItemEntity> tagItems(List<Integer> items, String tag)
			throws ResourceNotFoundException, ApplicationException;

	public TodoItemEntity addRemainderToItem(Integer itemId, LocalDateTime timeStamp)
			throws ResourceNotFoundException, ApplicationException;

}
