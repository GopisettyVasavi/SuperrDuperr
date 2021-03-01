
DROP TABLE IF EXISTS TBL_TODO_LIST;
 
DROP TABLE IF EXISTS TBL_TODO_ITEM;


CREATE TABLE TBL_TODO_LIST (
list_id INT AUTO_INCREMENT PRIMARY KEY,
  list_name VARCHAR(250) NOT NULL,
  status VARCHAR(250)    
);

CREATE TABLE TBL_TODO_ITEM (
 item_id INT AUTO_INCREMENT PRIMARY KEY,
  item_name VARCHAR(250) NOT NULL,
  status VARCHAR(250),
  tag_name VARCHAR(250),
  activation_flag VARCHAR(250) ,
  remainder TIMESTAMP,
  list_id INT,
  foreign key (list_id) references TBL_TODO_LIST(list_id)
);





