This application is implemented using Java8 and Springboot2.0.6

This project can be imported into any IDE with the pom file as Maven Project. 
In the application.properties file, port number has been changed to 8082. Change to a different one if required. 

Source code is available at:
https://github.com/GopisettyVasavi/SuperrDuperr

***********************************************************************************************************************************************

This application is developed using h2 database, which does not require any extra setup. 

2 tables are created to use in the application. 
a. TBL_TODO_LIST: Stores List details. List_ID, List_NAME and Status can be stored.
List_ID is primary key.
Status is added just as an extension to delete entire list along with items if required.
b. TBL_TODO_ITEM: Stores item details like Item_ID, Item_Name, Status, Activation_Flag, Remainder and List_ID (Foreign Key)
Item_ID is Primary Key
Status values can be New, Completed etc. This field will be updated when Marking the item.
Activation_Flag is used for soft deletion. Values can be ACTIVE or INACTIVE.
Remainder is the timestamp attached to the timestamp.

Relation for TBL_TODO_LIST to TBL_TODO_ITEM is One --> Many


*********************************************************************************************************************************************
Understanding/Assumptions:

1. Adding remainder is attaching the timestamp by updating remainder field. No other action has been written as part of this implementation.

2. UI implementation is not done as part of this code challenge. Only required REST API services are implemented.
**************************************************************************************************************************************************
1. getlsits:
 Endpoint: localhost:8082/superrduperr/api/v1/todoLists
Method Type: GET
Input: 

Output:[
    {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    },
    {
        "listId": 2345,
        "listName": "Week Tasks",
        "status": "ACTIVE"
    },
    {
        "listId": 5678,
        "listName": "Month Tasks",
        "status": "ACTIVE"
    }
]

	Response Codes: 200 -Ok
			400 - Bad Request
Postman Outputs:

![](/images/todolist1.png?raw=true)

![](/images/todolist2.png?raw=true)

2. getListByListId
	Endpoint:localhost:8082/superrduperr/api/v1/todoLists/{listId}
	Method Type: GET
	Input: PathVariable :Integer<ListID>
	
	Output: {
    	"listId": 1234,
    	"listName": "Day Tasks",
    	"status": "ACTIVE"
	}

	Response Codes: 200 -Ok
			404 - ListId is not found.
			400 - ListId is null

Postman Output:
![](/images/todolist3.png?raw=true)


3. createList
	Endpoint: localhost:8082/superrduperr/api/v1/createList
	Method Type: POST
	Input: Request Body: {

		"listName":"New Weekly Tasks",
		"status" : "Active"
		}
	Output: {
    		"listId": 5680,
    		"listName": "New Weekly Tasks",
    		"status": "Active"
		}

Response Codes: 	200 -Ok
			400 - Bad Request (List name is empty)

Postman Output:
![](/images/createList1.png?raw=true)

![](/images/createList2.png?raw=true)

4. getItemsByListId:
	Endpoint: localhost:8082/superrduperr/api/v1/todoItems/{listId}
	Method Type: GET
	Input: Path Variable - LisId(Integer)
	Output: [
    {
        "itemId": 1,
        "itemName": "second item",
        "status": "Active",
        "tagName": null,
        "activationFlag": null,
        "remainder": null
    },
    {
        "itemId": 2,
        "itemName": "Standup Meetings",
        "status": "Active",
        "tagName": null,
        "activationFlag": null,
        "remainder": null,
        
    }
    
    }
]

Response Codes: 	200 -Ok
			404 - List Id not Found 

Postman Output:
![](/images/todoItems1.png?raw=true)

![](/images/todoItems2.png?raw=true)

5. getItemsByItemId
	Endpoint: localhost:8082/superrduperr/api/v1/todoItem/{itemId}
	Method Type: GET
	Input: Path Variable - itemId(Integer)
	Output: {
    "itemId": 2,
    "itemName": "Standup Meetings",
    "status": "Active",
    "tagName": null,
    "activationFlag": null,
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}

Response Codes: 	200 -Ok
			404 - Item Id not Found 


Postman Output:
![](/images/todoItems3.png?raw=true)

![](/images/todoItems4.png?raw=true)

6. createItem

Endpoint: localhost:8082/superrduperr/api/v1/createItem/{listId}
	Method Type: POST
	Input: Request Body: {
        
        "itemName": "Standup Meetings",
        "status": "Active",
        "tagName": null,
        "activationFlag": null,
        "remainder": null
        
    	}

	Output: {
    "itemId": 3,
    "itemName": "Standup Meetings",
    "status": "Active",
    "tagName": null,
    "activationFlag": null,
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}

Response Codes: 	200 -Ok
			400 - Bad Request (List name is empty)
			404 - List Id is not found.

Postman Output:
![](/images/createItem1.png?raw=true)

![](/images/createItem2.png?raw=true)


7. markItem: 
	
	Endpoint: localhost:8082/superrduperr/api/v1/mark/{itemId}
	Method Type: PATCH
	Input: Pathvariable - Integer - itemId
	Output: {
    "itemId": 1,
    "itemName": "Standup Meetings",
    "status": "COMPLETED",
    "tagName": null,
    "activationFlag": null,
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}

Response Codes: 	200 -Ok
			400 - Bad Request 
			404 - Item is not found.

Postman Output:
![](/images/markItem1.png?raw=true)

![](/images/markItem2.png?raw=true)

![](/images/markItem3.png?raw=true)

8. deleteItem: 
	
	Endpoint: localhost:8082/superrduperr/api/v1/delete/{itemId}
	Method Type: DELETE
	Input: Pathvariable - Integer - itemId

	Output: {
    "itemId": 1,
    "itemName": "Standup Meetings",
    "status": "COMPLETED",
    "tagName": null,
    "activationFlag": "INACTIVE",
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}

Response Codes: 	200 -Ok
			400 - Bad Request 
			404 - Item is not found.

Postman Output:
![](/images/deleteItem1.png?raw=true)

![](/images/deleteItem2.png?raw=true)

![](/images/deleteItem3.png?raw=true)

9. restoreItem
	
	Endpoint: localhost:8082/superrduperr/api/v1/restore/{itemId}
	Method Type: PATCH
	Input: Pathvariable - Integer - itemId

	Output: {
    "itemId": 1,
    "itemName": "Standup Meetings",
    "status": "COMPLETED",
    "tagName": null,
    "activationFlag": "ACTIVE",
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}

Response Codes: 	200 -Ok
			400 - Bad Request 
			404 - Item is not found.

Postman Output:
![](/images/restoreItem1.png?raw=true)

![](/images/restoreItem12.png?raw=true)

![](/images/restoreItem3.png?raw=true)

10. tagItems

	EndPoint: localhost:8082/superrduperr/api/v1/tagItems/{Tag Name}
	Method: PATCH
	Input: Requestbody List of itemIds [itemId1, itemId2]
	Output: [
    {
        "itemId": 1,
        "itemName": "New Item For tagging",
        "status": "Active",
        "tagName": "Mandatory Taks",
        "activationFlag": null,
        "remainder": null,
        "listEntity": {
            "listId": 1234,
            "listName": "Day Tasks",
            "status": "ACTIVE"
        }
    },
    {
        "itemId": 2,
        "itemName": "Send Email",
        "status": "Active",
        "tagName": "Mandatory Taks",
        "activationFlag": null,
        "remainder": null,
        "listEntity": {
            "listId": 1234,
            "listName": "Day Tasks",
            "status": "ACTIVE"
        }
    }
]

Response Codes:
		200- OK
		400 - Bad Request (empty tag name)
		404 - Item id not found.

Postman Output:
![](/images/tagItems1.png?raw=true)

11. tagItem:
End Point: localhost:8082/superrduperr/api/v1/tagItem/{itemId}/{tagName}
Method: PATCH
Input: item Id and tag Name
Output: {
    "itemId": 1,
    "itemName": "New Item For tagging",
    "status": "Active",
    "tagName": "Mandatory Taks",
    "activationFlag": null,
    "remainder": null,
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}
Response Codes:
		200- OK
		400 - Bad Request (empty tag name)
		404 - Item id not found.

Postman Output:
![](/images/tagItem1.png?raw=true)

12. addRemainderToItem

End Point: localhost:8082/superrduperr/api/v1/remainder/{itemId}/{timeStamp}
Method: PATCH
Input: item Id and timeStamp (Format: dd-MM-yyyy HH:mm:SS eg: 01-03-2021 00:00:00)
{
    "itemId": 1,
    "itemName": "New Item For tagging",
    "status": "Active",
    "tagName": "Mandatory Taks",
    "activationFlag": null,
    "remainder": "2021-03-01T00:00:00",
    "listEntity": {
        "listId": 1234,
        "listName": "Day Tasks",
        "status": "ACTIVE"
    }
}
	
Response Codes:
		200- OK
		400 - Bad Request (incorrect or empty timestamp)
		404 - Item id not found.	

Postman Output:
![](/images/remainderItem1.png?raw=true)
	
![](/images/remainderItem2.png?raw=true)
