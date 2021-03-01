package com.superrduperr.todo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.superrduperr.todo.model.TodoListEntity;
/**
 *  JPA Repository for TodoList.
 * @author Vasavi
 *
 */
@Repository
public interface TodoListRepository extends CrudRepository<TodoListEntity, Integer> {

}
