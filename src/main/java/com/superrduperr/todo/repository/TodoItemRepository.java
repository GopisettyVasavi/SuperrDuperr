package com.superrduperr.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.superrduperr.todo.model.TodoItemEntity;
/** JPA Repository TodoItem 
 * 
 * @author Vasavi
 *
 */
@Repository
public interface TodoItemRepository extends CrudRepository<TodoItemEntity, Integer>{

}
