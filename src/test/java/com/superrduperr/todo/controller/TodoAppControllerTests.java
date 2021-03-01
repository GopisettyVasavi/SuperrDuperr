package com.superrduperr.todo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
/**
 * 
 */
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.superrduperr.todo.SuperrDuperrApplication;
import com.superrduperr.todo.exception.ResourceNotFoundException;
import com.superrduperr.todo.model.TodoItemEntity;
import com.superrduperr.todo.model.TodoListEntity;
import com.superrduperr.todo.service.ITodoAppService;
import com.superrduperr.todo.util.TodoAppConstants;
import com.superrduperr.todo.util.TodoAppUtil;


/**
 * This test class will have all test methods for TodoAppController
 * @author Vasavi
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = SuperrDuperrApplication.class)
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@AutoConfigureTestDatabase
public class TodoAppControllerTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private ITodoAppService todoService;

	/**
	 * This method will test when requested for get all list items whether list is
	 * returned o not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenrequest_thengetAllList_Status200() throws Exception {
		List<TodoListEntity> lists = populateTodoList();

		Mockito.when(todoService.getTodoLists()).thenReturn((lists));
		mvc.perform(get("/superrduperr/api/v1/todoLists").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.[0].listName", is(lists.get(0).getListName())))
				.andExpect(jsonPath("$.size()", is(lists.size())));

	}

	/**
	 * This method will test when given list id will it return the todo list or not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidListId_thengetListbyId_Status200() throws Exception {
		List<TodoListEntity> lists = populateTodoList();

		Mockito.when(todoService.getListById(1)).thenReturn((lists.get(0)));
		mvc.perform(get("/superrduperr/api/v1/todoLists/{listId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.listName", is(lists.get(0).getListName())));

	}

	/**
	 * This method will test when given list id will it return the todo list or not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInvalidList_thengetListbyId_Status404() throws Exception {

		Mockito.when(todoService.getListById(100)).thenThrow(new ResourceNotFoundException());
		mvc.perform(get("/superrduperr/api/v1/todoLists/{listId}", 100).contentType(MediaType.APPLICATION_JSON))
				.andDo(print());

	}

	/**
	 * This method will test saving a new list will return 200 when necessary list
	 * object is passed
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenValidListObj_thenSave_ReturnJson() throws JsonProcessingException, Exception {
		int id = 1;
		TodoListEntity list = new TodoListEntity(1, "Daily Tasks", new ArrayList<TodoItemEntity>());

		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);

		when(todoService.createOrUpdateList(any(TodoListEntity.class))).thenReturn(list);
		mvc.perform(post("/superrduperr/api/v1/createList").contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(list))).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$").isNotEmpty()).andExpect(jsonPath("$.listId", is(id)));
	}

	/**
	 * This method will test while a BAD_REQUEST response is written when passed
	 * empty request body object is passed
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenInValidListObj_thenSave_Return404() throws JsonProcessingException, Exception {

		mvc.perform(post("/superrduperr/api/v1/createList").contentType(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	/**
	 * This method will test given valid list id whether it will return associated
	 * items or not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenValidListId_thengetItems_Status200() throws Exception {
		List<TodoItemEntity> items = populateItemsList();

		Mockito.when(todoService.getItems(1)).thenReturn((items));
		mvc.perform(get("/superrduperr/api/v1/todoItems/{listId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$.[0].itemName", is(items.get(0).getItemName())))
				.andExpect(jsonPath("$.size()", is(items.size())));

	}

	/**
	 * This method will test given valid list id whether it will return associated
	 * items or not.
	 * 
	 * @throws Exception
	 */
	@Test
	public void givenInValidListId_thengetItems_Status404() throws Exception {
		int listId = 100;
		Mockito.when(todoService.getItems(listId)).thenThrow((new ResourceNotFoundException()));
		mvc.perform(get("/superrduperr/api/v1/todoItems/{listId}", listId).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());

	}

	/**
	 * This method will test while a BAD_REQUEST response is written when passed
	 * empty request body object is passed
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenInValidItemObj_thenSave_Return404() throws JsonProcessingException, Exception {

		mvc.perform(post("/superrduperr/api/v1/createItem/{listId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	/**
	 * This method will check given valid id when mark item
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenItemId_thenMark_Return200() throws JsonProcessingException, Exception {

		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.COMPLETED, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(todoService.getItemById(1)).thenReturn((itemOne));
		Mockito.when(todoService.createOrUpdateItem(itemOne, 1)).thenReturn((itemUpdated));
		mvc.perform(patch("/superrduperr/api/v1/mark/{itemId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * This method will check given invalid item id when mark item
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenInvalidItemId_thenMark_Return404() throws JsonProcessingException, Exception {

		int itemId = 1000;
		Mockito.when(todoService.getItemById(itemId)).thenThrow((new ResourceNotFoundException()));
		mvc.perform(patch("/superrduperr/api/v1/mark/{itemId}", "").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * This method will test given valid item id will delete
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenItemId_thenDelete_Return200() throws JsonProcessingException, Exception {

		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.INACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(todoService.getItemById(1)).thenReturn((itemOne));
		Mockito.when(todoService.createOrUpdateItem(itemOne, 1)).thenReturn((itemUpdated));
		mvc.perform(delete("/superrduperr/api/v1/delete/{itemId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * This method will test delete item when invalid id is passed
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */

	@Test
	public void givenInvalidItemId_thenDelete_Return404() throws JsonProcessingException, Exception {

		int itemId = 1000;
		Mockito.when(todoService.getItemById(itemId)).thenThrow((new ResourceNotFoundException()));
		mvc.perform(delete("/superrduperr/api/v1/delete/{itemId}", "").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * This method will test restore of item.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenItemId_thenRestore_Return200() throws JsonProcessingException, Exception {

		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.INACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		Mockito.when(todoService.getItemById(1)).thenReturn((itemOne));
		Mockito.when(todoService.createOrUpdateItem(itemOne, 1)).thenReturn((itemUpdated));
		mvc.perform(patch("/superrduperr/api/v1/restore/{itemId}", 1).contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk());
	}

	/**
	 * This method will test given invalid item id will return 404
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */

	@Test
	public void givenInvalidItemId_thenRestore_Return404() throws JsonProcessingException, Exception {

		int itemId = 1000;
		Mockito.when(todoService.getItemById(itemId)).thenThrow((new ResourceNotFoundException()));
		mvc.perform(patch("/superrduperr/api/v1/restore/{itemId}", "").contentType(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isNotFound());
	}

	/**
	 * This method will test tagging item with the given tag
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenItemId_thenTagItem_Return200() throws JsonProcessingException, Exception {

		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "Not Taggedd",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());

		Mockito.when(todoService.getItemById(1)).thenReturn((itemOne));
		Mockito.when(todoService.createOrUpdateItem(itemOne, 1)).thenReturn((itemUpdated));
		Mockito.when(todoService.tagItem(1, "I am Tagged")).thenReturn((itemUpdated));
		mvc.perform(patch("/superrduperr/api/v1/tagItem/{itemId}/{tag}", 1, "I am Tagged")
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
	}

	/**
	 * This method will test adding remainder timestamp to the given item.
	 * 
	 * @throws JsonProcessingException
	 * @throws Exception
	 */
	@Test
	public void givenItemId_thenAddRemainderItem_Return200() throws JsonProcessingException, Exception {

		TodoItemEntity itemOne = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "Not Taggedd",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		TodoItemEntity itemUpdated = new TodoItemEntity(1, "Standup Meeting", TodoAppConstants.NEW, "I am Tagged",
				TodoAppConstants.ACTIVE, LocalDateTime.now(), new TodoListEntity());
		int itemId = 1;
		String remainder = "01-03-2021 09:00:00";
		Mockito.when(todoService.getItemById(1)).thenReturn((itemOne));
		Mockito.when(todoService.createOrUpdateItem(itemOne, 1)).thenReturn((itemUpdated));
		Mockito.when(todoService.addRemainderToItem(itemId, TodoAppUtil.formatDateTime(remainder))).thenReturn((itemUpdated));
		mvc.perform(patch("/superrduperr/api/v1/remainder/{itemId}/{remainder}", itemId, remainder)
				.contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
	}

	/**
	 * This method will populate list
	 * 
	 * @return
	 */
	private List<TodoListEntity> populateTodoList() {
		List<TodoListEntity> list = new ArrayList<TodoListEntity>();
		TodoListEntity listOne = new TodoListEntity(1, "Daily Tasks", new ArrayList<TodoItemEntity>());

		TodoListEntity listTwo = new TodoListEntity(2, "Weekly Tasks", new ArrayList<TodoItemEntity>());

		list.add(listOne);
		list.add(listTwo);
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
