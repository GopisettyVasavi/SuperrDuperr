package com.superrduperr.todo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.superrduperr.todo.exception.ApplicationException;
import com.superrduperr.todo.exception.InvalidInputException;
import com.superrduperr.todo.exception.ResourceNotFoundException;
import com.superrduperr.todo.model.TodoItemEntity;
import com.superrduperr.todo.model.TodoListEntity;
import com.superrduperr.todo.repository.TodoItemRepository;
import com.superrduperr.todo.repository.TodoListRepository;
import com.superrduperr.todo.util.TodoAppConstants;

/**
 * This service class will have implementation for all the ITodoService methods
 * interface.
 * 
 * @author Vasavi
 *
 */
@Service
public class TodoAppServiceImpl implements ITodoAppService {

	private static Logger logger = LoggerFactory.getLogger(TodoAppServiceImpl.class);

	@Autowired
	TodoListRepository listRepository;
	@Autowired
	TodoItemRepository itemRepository;

	/**
	 * This method will return all the Todo Lists if anything present in repository
	 * else return empty list.
	 */
	public List<TodoListEntity> getTodoLists() throws ApplicationException{
		List<TodoListEntity> todoList = null;
		try {
			todoList = (List<TodoListEntity>) listRepository.findAll();
		} catch (Exception e) {
			throw new ApplicationException("Issue in retrieving todo Lists.");
		}
		if (todoList.size() > 0) {
			return todoList;
		} else {
			return new ArrayList<TodoListEntity>();
		}
	}
/**
 * This method will return the Todo list by given Listid if found else it will return exception.
 */
	public TodoListEntity getListById(Integer listId) throws ResourceNotFoundException,ApplicationException{
		TodoListEntity list=null;
		if(null!=listId) {
			Optional<TodoListEntity>listEntity=listRepository.findById(listId);
			if(listEntity.isPresent()) {
				list=listEntity.get();
				return list;
			}
			else throw new ResourceNotFoundException("Todo List is not found");
		}
		else {
			throw new ApplicationException("List Id is null or empty.");
		}
	}
	/**
	 * This method will create a new TodoList. First it will check whether list id is present or not.
	 *  If present it will update the list or else create a new list.
	 */
	public TodoListEntity createOrUpdateList(TodoListEntity entity)  {
		if (entity.getListId() == null) {
			logger.info("Creating new Todo List");
			if (null == entity.getStatus() || entity.getStatus().isEmpty()) {
				entity.setStatus(TodoAppConstants.ACTIVE);
			}
			entity = listRepository.save(entity);

			return entity;
		} else {
			Optional<TodoListEntity> list = listRepository.findById(entity.getListId());

			if (list.isPresent()) {
				logger.info("List already exists, updating Todo List");
				TodoListEntity newEntity = list.get();
				newEntity.setListName(entity.getListName());
				newEntity.setStatus(entity.getStatus());

				newEntity = listRepository.save(newEntity);

				return newEntity;
			} else {
				logger.info("Creating new Todo List");
				if (null == entity.getStatus() || entity.getStatus().isEmpty()) {
					entity.setStatus(TodoAppConstants.ACTIVE);
				}
				entity = listRepository.save(entity);

				return entity;
			}
		}
	}

	/**
	 * This method will return all the items for the given list id.
	 */
	public List<TodoItemEntity> getItems(Integer listId) throws ResourceNotFoundException, ApplicationException{
		List<TodoItemEntity> todoItems = null;
		
		if(null!=listId) {
			Optional<TodoListEntity>listEntity=listRepository.findById(listId);
			if(listEntity.isPresent()) {
				todoItems=listEntity.get().getItems();
				return todoItems;
			}
			else throw new ResourceNotFoundException("Todo List is not found");
		}
		else {
			throw new ApplicationException("List Id is null or empty.");
		}
	}
	/**
	 * This method will return the Todo list by given Listid if found else it will return exception.
	 */
		public TodoItemEntity getItemById(Integer itemId) throws ResourceNotFoundException,ApplicationException{
			TodoItemEntity item=null;
			if(null!=itemId) {
				Optional<TodoItemEntity>itemEntity=itemRepository.findById(itemId);
				if(itemEntity.isPresent()) {
					item=itemEntity.get();
					return item;
				}
				else throw new ResourceNotFoundException("Todo Item is not found");
			}
			else {
				throw new ApplicationException("Item Id is null or empty.");
			}
		}
	/**
	 * This method will check if the itemId is present or not. If not, it will create new item for the given list or update the existing item.
	 * It will check for the availability of list by invoking retrieveListById().
	 * @throws ResourceNotFoundException 
	 */
	public TodoItemEntity createOrUpdateItem(TodoItemEntity entity, Integer listId) throws InvalidInputException, ResourceNotFoundException {
		if (entity.getItemId() == null) {
			logger.info("Creating new Todo Item");
			
				TodoListEntity list = retrieveListById(listId);
				if (null != list) {
					entity.setListEntity(list);
				} else
					throw new ResourceNotFoundException("Can not create item with out list.");

			
			entity = itemRepository.save(entity);

			return entity;
		} else {
			Optional<TodoItemEntity> item = itemRepository.findById(entity.getItemId());

			if (item.isPresent()) {
				logger.info("Item exists, updating Todo Item");
				TodoItemEntity newEntity = item.get();
				newEntity.setItemName(entity.getItemName());
				newEntity.setStatus(entity.getStatus());
				newEntity.setActivationFlag(entity.getActivationFlag());
				newEntity.setRemainder(entity.getRemainder());
				newEntity.setTagName(entity.getTagName());
				newEntity = itemRepository.save(newEntity);

				return newEntity;
			} else {
				logger.info("Creating new Todo Item");
				entity = itemRepository.save(entity);

				return entity;
			}
		}
	}

	/**
	 * This method will mark the given item as Completed if it's not in Completed
	 * and deleted status.
	 */
	public TodoItemEntity markItem(Integer itemId) throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity item = retrieveItemById(itemId);
		
		// Check the item status is not completed
		if (null != item && !((TodoAppConstants.COMPLETED.equalsIgnoreCase(item.getStatus()))
				|| (TodoAppConstants.INACTIVE.equalsIgnoreCase(item.getActivationFlag())))) {
			item.setStatus(TodoAppConstants.COMPLETED);
			logger.info("Marking item {} to Completed status.", itemId);
			item = itemRepository.save(item);
			return item;
		}
		throw new ApplicationException("Item is in Completed Status or Inactive.");

	}

	/**
	 * This method will soft delete the items. Means change the activation flag to
	 * Inactive
	 */
	public TodoItemEntity deleteItem(Integer itemId) throws ResourceNotFoundException, ApplicationException {
		TodoItemEntity item = retrieveItemById(itemId);
		// Check the item activation flag is not already inactive
		if (null != item && !(TodoAppConstants.INACTIVE.equalsIgnoreCase(item.getActivationFlag()))) {
			item.setActivationFlag(TodoAppConstants.INACTIVE);
			logger.info("Deleting item {}.", itemId);
			item = itemRepository.save(item);
			return item;
		}
		// This scenario might not occur if the UI is not rendering the deleted records.
		throw new ApplicationException("Item is already deleted.");
	}

	/**
	 * This method will restore the deleted Item back to Active state
	 */
	public TodoItemEntity restoreItem(Integer itemId) throws ApplicationException, ResourceNotFoundException {
		TodoItemEntity item = retrieveItemById(itemId);
		// Check the item Activation flag is not already Active
		if (null != item && !(TodoAppConstants.ACTIVE.equalsIgnoreCase(item.getActivationFlag()))) {
			item.setActivationFlag(TodoAppConstants.ACTIVE);
			logger.info("Restoring item {} .", itemId);
			item = itemRepository.save(item);
			return item;
		}
		throw new ApplicationException("Item is already in Active state.");
	}

	/**
	 * This method will tag the list of items with the given tagname.
	 */
	public List<TodoItemEntity> tagItems(List<Integer> items, String tag)
			throws ApplicationException {
		List<TodoItemEntity> entityList = new ArrayList<TodoItemEntity>();
		if (null != items && items.size() > 0) {
			items.forEach(item -> {
				try {
					TodoItemEntity entity = retrieveItemById(item);
					if (null != entity) {
						
							entity=tagItem(entity.getItemId(), tag);
						
						entityList.add(entity);
					} else {
						throw new ResourceNotFoundException("Item not found for tagging.");
					}
				} catch (ResourceNotFoundException | ApplicationException e) {
					logger.error("Error occured while tagging items.");
					
				}
			});
		} else

			throw new ApplicationException("No items are selected to tag.");

		return entityList;
	}
	
	/**
	 * This method will restore the deleted Item back to Active state
	 */
	public TodoItemEntity tagItem(Integer itemId, String tag) throws ApplicationException, ResourceNotFoundException {
		TodoItemEntity item = retrieveItemById(itemId);
		// Check the item Activation flag is not  Active. Tag only active items
		if (null != item && !TodoAppConstants.INACTIVE.equalsIgnoreCase(item.getActivationFlag())) {
			item.setTagName(tag);
			logger.info("Tagging item {} .", itemId);
			item = itemRepository.save(item);
			return item;
		}
		throw new ApplicationException("Item is not in Active state to tag.");
	}

	/**
	 * This method will add remainder to the item.
	 */
	public TodoItemEntity addRemainderToItem(Integer itemId, LocalDateTime timeStamp)
			throws ApplicationException, ResourceNotFoundException {
		TodoItemEntity item = retrieveItemById(itemId);
		// Check the item activation flag is not inactive
		if (null != item && !TodoAppConstants.INACTIVE.equalsIgnoreCase(item.getActivationFlag())) {
			item.setRemainder(timeStamp);
			logger.info("Setting remainder to item {}.", itemId);
			item = itemRepository.save(item);
			return item;
		}
		// This scenario might not occur if the UI is not rendering the deleted records.
		throw new ApplicationException("Item " + itemId + " is already deleted.");
	}

	

	/**
	 * This method will retrieve the Item Entity from repository for the given
	 * itemId
	 * 
	 * @param itemId
	 * @return TodoItemEntity
	 * @throws ResourceNotFoundException
	 */
	private TodoItemEntity retrieveItemById(Integer itemId) throws ResourceNotFoundException {
		TodoItemEntity item = null;
		if (null != itemId) {
			logger.info("Retrieving Item Entity for {}", itemId);
			Optional<TodoItemEntity> entity = itemRepository.findById(itemId);
			if (entity.isPresent()) {
				item = entity.get();
			} else {
				logger.info("Item with id {} does not exist.", itemId);
				throw new ResourceNotFoundException("Item with id " + itemId + " does not exist.");
			}

		} else {
			logger.info("Item id is null to mark it as completed.");
			throw new ResourceNotFoundException("Item Id to mark complete can not be null.");
		}
		logger.info("Item found..{}", item.getItemName());
		return item;
	}

	/**
	 * This method will retrieve the List Entity from repository for the given
	 * itemId
	 * 
	 * @param itemId
	 * @return TodoItemEntity
	 * @throws ResourceNotFoundException
	 */
	private TodoListEntity retrieveListById(Integer listId) throws ResourceNotFoundException {
		TodoListEntity item = null;
		if (null != listId) {
			logger.info("Retrieving List Entity for {}", listId);
			Optional<TodoListEntity> entity = listRepository.findById(listId);
			if (entity.isPresent()) {
				item = entity.get();
			} else {
				logger.info("List with id {} does not exist.", listId);
				throw new ResourceNotFoundException("Item with id " + listId + " does not exist.");
			}

		} else {
			logger.info("List id is null to mark it as completed.");
			throw new ResourceNotFoundException("List Id to mark complete can not be null.");
		}
		return item;
	}

}
