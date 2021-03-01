package com.superrduperr.todo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.superrduperr.todo.exception.ApplicationException;
import com.superrduperr.todo.exception.InvalidInputException;
import com.superrduperr.todo.exception.ResourceNotFoundException;
import com.superrduperr.todo.model.TodoItemEntity;
import com.superrduperr.todo.model.TodoListEntity;
import com.superrduperr.todo.repository.TodoItemRepository;
import com.superrduperr.todo.repository.TodoListRepository;
import com.superrduperr.todo.util.TodoAppConstants;
import com.superrduperr.todo.util.TodoAppUtil;
/**
 *  This test class have methods for all Service methods.
 * @author Vasavi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TodoAppServiceTests {

	@InjectMocks
	ITodoAppService todoService = new TodoAppServiceImpl();

	@Mock
	TodoListRepository listRepository;

	@Mock
	TodoItemRepository itemRepository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	/**
	 * Test method to check when there are list of tasks available
	 * 
	 * @throws ApplicationException
	 */
	@Test
	public void when_getAllTaskList() throws ApplicationException {
		List<TodoListEntity> lists = populateTodoList();

		Mockito.when(listRepository.findAll()).thenReturn((lists));

		List<TodoListEntity> taskList = todoService.getTodoLists();
		assertEquals(2, taskList.size());

	}

	/**
	 * Test method to check when the todo list is empty
	 * 
	 * @throws ApplicationException
	 */
	@Test
	public void when_getAllTaskList_thenNoList() throws ApplicationException {

		Mockito.when(listRepository.findAll()).thenReturn((new ArrayList<TodoListEntity>()));

		List<TodoListEntity> taskList = todoService.getTodoLists();
		assertEquals(0, taskList.size());

	}

	/**
	 * Test method to check when there are list of tasks available
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_getItemsByListId() throws ApplicationException, ResourceNotFoundException {
		List<TodoListEntity> lists = populateTodoList();
		int listId = 1;

		Mockito.when(listRepository.findById(1)).thenReturn(Optional.of(lists.get(0)));

		List<TodoItemEntity> taskList = todoService.getItems(listId);
		assertEquals(2, taskList.size());

	}

	/**
	 * Test method to check when the todo list is empty
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test(expected = ResourceNotFoundException.class)
	public void when_getItemsByInvalidListId() throws ApplicationException, ResourceNotFoundException {

		Mockito.when(todoService.getItems(1)).thenThrow(new ResourceNotFoundException());
		int listId = 100;
		List<TodoItemEntity> taskList = todoService.getItems(listId);
		assertEquals(0, taskList.size());
	}

	/**
	 * Test method to check when passing list id will return list object
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_getItemByID_thenItemEntity() throws ApplicationException, ResourceNotFoundException {
		List<TodoItemEntity> items = populateItemsList();
		int itemId = 1;
		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(items.get(0)));

		TodoItemEntity item = todoService.getItemById(itemId);
		assertEquals(items.get(0).getItemName(), item.getItemName());

	}

	/**
	 * Test method to check when invalid item id is passed
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_getItemByInvalidItemId_thenNoItemEntity() throws ApplicationException, ResourceNotFoundException {

		int itemId = 1;
		Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(new TodoItemEntity()));
		TodoItemEntity item = todoService.getItemById(itemId);
		assertEquals(null, item.getItemName());

	}

	/**
	 * Test method to check when passing list id will return list object
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_getTaskListByID_thenListEntity() throws ApplicationException, ResourceNotFoundException {
		List<TodoListEntity> lists = populateTodoList();

		Mockito.when(listRepository.findById(1)).thenReturn(Optional.of(lists.get(0)));

		TodoListEntity taskList = todoService.getListById(1);
		assertEquals(lists.get(0).getListName(), taskList.getListName());

	}

	/**
	 * Test method to check when empty list entity returned
	 * 
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_getTaskByInvalidListId_thenNoListEntity() throws ApplicationException, ResourceNotFoundException {

		Mockito.when(listRepository.findById(1)).thenReturn(Optional.of(new TodoListEntity()));

		TodoListEntity taskList = todoService.getListById(1);
		assertEquals(null, taskList.getListName());

	}

	/**
	 * Test method to check when the todo list is empty
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException 
	 */
	@Test
	public void when_createList() throws InvalidInputException, ApplicationException {

		TodoListEntity entity = new TodoListEntity(1, "Task1", new ArrayList<TodoItemEntity>());
		Mockito.when(listRepository.save(entity))
				.thenReturn(new TodoListEntity(1, "Task1", new ArrayList<TodoItemEntity>()));

		TodoListEntity task = todoService.createOrUpdateList(entity);
		assertEquals(task.getListName(), entity.getListName());

	}

	/**
	 * Test method to check creating an item when valid list id is given
	 * 
	 * @throws InvalidInputException
	 * @throws ResourceNotFoundException 
	 */
	@Test
	public void when_createItem_validListId() throws InvalidInputException, ResourceNotFoundException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(itemRepository.save(entity)).thenReturn(new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW,
				"I am Tagged", TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity()));

		TodoItemEntity task = todoService.createOrUpdateItem(entity, 1);
		assertEquals(task.getItemName(), entity.getItemName());

	}

	/**
	 * Test method to check when marking an item when valid item id is given
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_markItem_validItemId()
			throws ResourceNotFoundException, ApplicationException, InvalidInputException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));

		todoService.markItem(1);
		verify(itemRepository, times(1)).save(entity);
		assertEquals(TodoAppConstants.COMPLETED, entity.getStatus());

	}

	/**
	 * Test method to check when marking item, but item already in Completed state
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ApplicationException.class)
	public void when_markItem_alreadyMarked() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.COMPLETED, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));
		todoService.markItem(1);

	}

	/**
	 * Test method to check when marking item, but item does not exist
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ResourceNotFoundException.class)
	public void when_markItem_itemNotExists() throws ResourceNotFoundException, ApplicationException {
		Mockito.when(todoService.markItem(1)).thenThrow(new ResourceNotFoundException());
		todoService.markItem(1);

	}

	/**
	 * Test method to check when deleting an item when valid item id is given
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_deleteItem_validItemId()
			throws ResourceNotFoundException, ApplicationException, InvalidInputException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));

		todoService.deleteItem(1);
		verify(itemRepository, times(1)).save(entity);
		assertEquals(TodoAppConstants.INACTIVE, entity.getActivationFlag());
	}

	/**
	 * Test method to check when deleting item, but item already in Completed state
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ApplicationException.class)
	public void when_deleteItem_alreadyDeleted() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.INACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));
		todoService.deleteItem(1);

	}

	/**
	 * Test method to check when deleting item, but item does not exist
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ResourceNotFoundException.class)
	public void when_deleteItem_itemNotExists() throws ResourceNotFoundException, ApplicationException {

		Mockito.when(todoService.deleteItem(1)).thenThrow(new ResourceNotFoundException());
		todoService.deleteItem(1);

	}

	/**
	 * Test method to check when deleting an item when valid item id is given
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_restoreItem_validItemId() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.INACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));

		todoService.restoreItem(1);
		assertEquals(TodoAppConstants.ACTIVE, entity.getActivationFlag());
	}

	/**
	 * Test method to check when deleting item, but item already in Completed state
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ApplicationException.class)
	public void when_restoreItem_alreadyActive() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));
		todoService.restoreItem(1);

	}

	/**
	 * Test method to check when marking item, but item does not exist
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ResourceNotFoundException.class)
	public void when_restoreItem_itemNotExists() throws ResourceNotFoundException, ApplicationException {

		Mockito.when(todoService.restoreItem(1)).thenThrow(new ResourceNotFoundException());
		todoService.restoreItem(1);

	}

	/**
	 * Test method to check when tagging list of items with the given tag
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_tagItems_validItemId()
			throws ResourceNotFoundException, ApplicationException, InvalidInputException {

		List<Integer> tags = new ArrayList<Integer>();
		tags.add(1);
		tags.add(2);

		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(itemUpdated));

		todoService.tagItems(tags, "I am Tagged");
		assertEquals("I am Tagged", itemUpdated.getTagName());

	}

	/**
	 * Test method to check when tagging an item with the given tag
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_tagItem_validItemId()
			throws ResourceNotFoundException, ApplicationException, InvalidInputException {

		int itemId = 1;

		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(itemUpdated));

		todoService.tagItem(itemId, "I am Tagged");
		assertEquals("I am Tagged", itemUpdated.getTagName());

	}

	/**
	 * Test method to check when tagging item, but item does not exist
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ResourceNotFoundException.class)
	public void when_tagItem_itemNotExists() throws ResourceNotFoundException, ApplicationException {

		List<Integer> tags = new ArrayList<Integer>();
		tags.add(1);
		tags.add(2);
		Mockito.when(todoService.restoreItem(1)).thenThrow(new ResourceNotFoundException());
		todoService.tagItems(tags, "I am Tagged");

	}

	/**
	 * Test method to check when deleting an item when valid item id is given
	 * 
	 * @throws InvalidInputException
	 * @throws ApplicationException
	 * @throws ResourceNotFoundException
	 */
	@Test
	public void when_addRemaindertoItem_validItemId() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, null, new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));

		todoService.addRemainderToItem(1, TodoAppUtil.formatDateTime("22-02-2021 00:00:00"));
		assertNotEquals(null, entity.getRemainder());
	}

	/**
	 * Test method to check when deleting item, but item already in Completed state
	 * 
	 * @throws ResourceNotFoundException
	 * @throws ApplicationException
	 */
	@Test(expected = ApplicationException.class)
	public void when_addRemaindertoItem_itemInactive() throws ResourceNotFoundException, ApplicationException {

		TodoItemEntity entity = new TodoItemEntity(1, "Send Email", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.INACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(itemRepository.findById(1)).thenReturn(Optional.of(entity));
		todoService.addRemainderToItem(1, TodoAppUtil.formatDateTime("22-02-2021 00:00:00"));

	}

	/**
	 * This method will populate list of to do lists
	 * 
	 * @return
	 */
	private List<TodoListEntity> populateTodoList() {
		List<TodoListEntity> list = new ArrayList<TodoListEntity>();
		TodoListEntity list1 = new TodoListEntity(1, "Start Work", populateItemsList());

		TodoListEntity list2 = new TodoListEntity(2, "Process Work", populateItemsList());

		list.add(list1);
		list.add(list2);
		return list;
	}

	/**
	 * This method will populate items list
	 * 
	 * @return
	 */
	private List<TodoItemEntity> populateItemsList() {
		List<TodoItemEntity> list = new ArrayList<TodoItemEntity>();
		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemTwo = new TodoItemEntity(2, "Email Checking", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		list.add(itemOne);
		list.add(itemTwo);
		return list;
	}
}
