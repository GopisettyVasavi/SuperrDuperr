package com.superrduperr.todo.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.superrduperr.todo.exception.ApplicationException;
import com.superrduperr.todo.exception.InvalidInputException;
import com.superrduperr.todo.exception.ResourceNotFoundException;
import com.superrduperr.todo.model.TodoItemEntity;
import com.superrduperr.todo.model.TodoListEntity;
import com.superrduperr.todo.service.ITodoAppService;
import com.superrduperr.todo.util.TodoAppUtil;

/**
 * This class is the main Controller that will have REST APIs for SuperrDuperr
 * Todo list.
 * 
 * @author Vasavi
 *
 */
@RestController
@RequestMapping("/superrduperr")
public class TodoAppController {
	private static Logger logger = LoggerFactory.getLogger(TodoAppController.class);

	@Autowired
	ITodoAppService todoService;

	/**
	 * This method will return todo list.
	 * 
	 * @return
	 */
	@GetMapping("/api/v1/todoLists")
	public ResponseEntity<?> getAllTodoList() {
		List<TodoListEntity> list = null;
		logger.info("Retrieving all Todo List entries.");
		try {
			list = todoService.getTodoLists();
		} catch (ApplicationException e) {
			logger.error("Issue in retrieving all Todo List : {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	/**
	 * This method will return the to do list for the given Id.
	 * 
	 * @param listId
	 * @return
	 */
	@GetMapping("/api/v1/todoLists/{listId}")
	public ResponseEntity<?> getListById(@PathVariable Integer listId) {
		if (null == listId) {
			logger.error("List id is empty to retrieve details.");
			return new ResponseEntity<>("List id is null or empty", HttpStatus.BAD_REQUEST);
		}
		logger.info("Retrieving list for the given id: {}", listId);
		TodoListEntity list = null;

		try {
			list = todoService.getListById(listId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in retrieving list for the given id: {}, issue: {}", listId, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in retrieving list for the given id: {}, issue: {}", listId, e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	// create List
	@PostMapping("/api/v1/createList")
	public ResponseEntity<?> createList(@RequestBody TodoListEntity todoList) {
		// This will check whether list name is empty or not. If empty return a message
		// as bad request
		if (null != todoList && todoList.getListName().isEmpty()) {
			logger.error("List name empty to create list.");
			return new ResponseEntity<>("List name can not be empty", HttpStatus.BAD_REQUEST);
		}
		logger.info("Creating new Todo list with {}", todoList.toString());
		try {
			todoList = todoService.createOrUpdateList(todoList);
		} catch (ApplicationException e) {
			logger.error("Issue in creating list: {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(todoList, HttpStatus.OK);
	}

	/**
	 * This method will get all the items for the given list id.
	 * 
	 * @return
	 */
	@GetMapping("/api/v1/todoItems/{listId}")
	public ResponseEntity<?> getItemsByListId(@PathVariable Integer listId) {

		if (null == listId) {
			logger.error("List id empty to retrieve Items.");
			return new ResponseEntity<>("List id is null or empty.", HttpStatus.BAD_REQUEST);
		}
		List<TodoItemEntity> items = null;
		logger.info("Retrieve Items for the given Id {}", listId);
		try {
			items = todoService.getItems(listId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in retrieving items {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in retrieving items {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(items, HttpStatus.OK);
	}

	/**
	 * This method will get item details for the given item id.
	 * 
	 * @return
	 */
	@GetMapping("/api/v1/todoItem/{itemId}")
	public ResponseEntity<?> getItemsByItemId(@PathVariable Integer itemId) {

		if (null == itemId) {
			logger.error("Item id empty to retrieve Item details.");
			return new ResponseEntity<>("Item id is null or empty.", HttpStatus.BAD_REQUEST);
		}
		TodoItemEntity item = null;
		logger.info("Retrieve Item details for the given Id {}", itemId);
		try {
			item = todoService.getItemById(itemId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in retrieving item details {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in retrieving details {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	/**
	 * This method will create or add an item to the existing list.
	 * 
	 * @param todoItem
	 * @param listId
	 * @return
	 */
	@PostMapping("/api/v1/createItem/{listId}")
	public ResponseEntity<?> createItem(@RequestBody TodoItemEntity todoItem, @PathVariable Integer listId) {

		if (null == listId) {
			logger.error("List id empty to create Item.");
			return new ResponseEntity<>("List id is null or empty.", HttpStatus.BAD_REQUEST);
		}
		if (null == todoItem) {
			logger.error("Item details are empty to create Item.");
			return new ResponseEntity<>("Item details are null or empty.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Creating item {}, for listId {}", todoItem.toString(), listId);
		try {
			try {
				todoItem = todoService.createOrUpdateItem(todoItem, listId);
			} catch (InvalidInputException e) {
				logger.error("Issue in creating item {}", e.getMessage());
				return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in creating item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}

	/**
	 * This method will mark the status as COMPLETED for the given item Id.
	 * 
	 * @param itemId
	 * @return
	 */
	// Mark an item as Completed
	@PatchMapping("/api/v1/mark/{itemId}")
	public ResponseEntity<?> markItem(@PathVariable Integer itemId) {
		if (null == itemId) {
			logger.error("Item id empty to mark Item.");
			return new ResponseEntity<>("Item id empty to mark Item.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Marking item {}", itemId);
		TodoItemEntity todoItem = null;
		try {
			todoItem = todoService.markItem(itemId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in mark Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in mark Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}

	/**
	 * This method will delete the item. It is soft delete(just change the
	 * activation status).
	 * 
	 * @param itemId
	 * @return
	 */
	// o Ability to delete items
	@DeleteMapping("/api/v1/delete/{itemId}")
	public ResponseEntity<?> deleteItem(@PathVariable Integer itemId) {
		if (null == itemId) {
			logger.error("Item id empty to delete Item.");
			return new ResponseEntity<>("Item id empty to mark Item.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Deleting item {}", itemId);
		TodoItemEntity todoItem = null;
		try {
			todoItem = todoService.deleteItem(itemId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in delete Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in delete Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}

	/**
	 * This method will restore the deleted item by changing the activation status.
	 * 
	 * @param itemId
	 * @return
	 */

	@PatchMapping("/api/v1/restore/{itemId}")
	public ResponseEntity<?> restoreItem(@PathVariable Integer itemId) {
		if (null == itemId) {
			logger.error("Item id empty to restore Item.");
			return new ResponseEntity<>("Item id empty to restore Item.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Restoring item {}", itemId);
		TodoItemEntity todoItem = null;
		try {
			todoItem = todoService.restoreItem(itemId);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in restoring Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in restoring Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}

	/**
	 * This method will tag multiple items with the given tag.
	 * 
	 * @param items
	 * @param tag
	 * @return
	 */
	@PatchMapping("/api/v1/tagItems/{tag}")
	public ResponseEntity<?> tagItems(@RequestBody List<Integer> items, @PathVariable String tag) {
		if (null == items || items.isEmpty()) {
			logger.error("Items list is empty to tag.");
			return new ResponseEntity<>("Items list is empty to tag.", HttpStatus.BAD_REQUEST);
		}
		if (null == tag) {
			logger.error("Tag is empty.");
			return new ResponseEntity<>("Tag is empty.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Tagging items with {}", tag);
		List<TodoItemEntity> todoItems = null;
		try {
			todoItems = todoService.tagItems(items, tag);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in tagging Items {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in tagging Items {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItems, HttpStatus.OK);
	}

	/**
	 * This method will tag multiple items with the given tag.
	 * 
	 * @param items
	 * @param tag
	 * @return
	 */
	@PatchMapping("/api/v1/tagItem/{itemId}/{tag}")
	public ResponseEntity<?> tagItem(@PathVariable Integer itemId, @PathVariable String tag) {
		if (null == itemId) {
			logger.error("Item is empty to tag.");
			return new ResponseEntity<>("Item is empty to tag.", HttpStatus.BAD_REQUEST);
		}
		if (null == tag || tag.isEmpty()) {
			logger.error("Tag is empty.");
			return new ResponseEntity<>("Tag is empty.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Tagging item with {}", tag);
		TodoItemEntity todoItem = null;
		try {
			todoItem = todoService.tagItem(itemId, tag);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in tagging Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in tagging Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}

	/**
	 * This method will add remainder timestamp for the given item id.
	 * 
	 * @param itemId
	 * @param remainder
	 * @return
	 */
	@PatchMapping("/api/v1/remainder/{itemId}/{remainder}")
	public ResponseEntity<?> addRemainderToItem(@PathVariable Integer itemId, @PathVariable String remainder) {
		if (null == itemId) {
			logger.error("Item is empty to add remainder.");
			return new ResponseEntity<>("Item is empty to add remainder.", HttpStatus.BAD_REQUEST);
		}
		if (null == remainder || remainder.isEmpty()) {
			logger.error("Remainder is empty.");
			return new ResponseEntity<>("Remainder is empty.", HttpStatus.BAD_REQUEST);
		}
		logger.info("Adding remainder at {}", remainder);

		TodoItemEntity todoItem = null;
		try {
			LocalDateTime timeStamp = TodoAppUtil.formatDateTime(remainder);
			if (null == timeStamp) {
				return new ResponseEntity<>("Remainder format is not correct.", HttpStatus.BAD_REQUEST);
			}
			todoItem = todoService.addRemainderToItem(itemId, timeStamp);
		} catch (ResourceNotFoundException e) {
			logger.error("Issue in adding remainder to Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ApplicationException e) {
			logger.error("Issue in adding remainder to Item {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}catch(DateTimeParseException de) {
			logger.error("Issue in Parsing remainder  {}", de.getMessage());
			return new ResponseEntity<>(de.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(todoItem, HttpStatus.OK);
	}
}
