# Thirdfort Coding Challenge

1. How to run the application?
   * open command prompt
   * move to folder src/main/resources/
   * execute command "java -jar note.jar"
   * port 8080 should not be allocated for other services

2. Instruction to UX team
   * To add note - call the endpoint note/save with json body
        ex. for json:
        {
            "userId" : "user2",
            "noteContent" : "this is user update message"
        }
        default archive value will be set to "false"
   * To update note - call the endpoint note/save with json body
        ex. for json:
        {
             "noteId" : 1,
             "userId" : "user2",
             "noteContent" : "this is user update message"
        }
   * Delete a existing note - call the endpoint note/delete with json body
        ex. for json
        {
            "noteId" : 1
        }
   * Archive a note - call the endpoint note/archive with json body
        ex. for json
        {
            "noteId" : 1
        }
   * Unarchive a note - call the endpoint note/unarchive with json body
        ex. for json
        {
            "noteId" : 1
        }
   * List archived notes - call the endpoint note/search with json body
        ex. for json
        {
            "userId" : "user_1",
            "archive" : true
        }
   * List unarchived notes - call the endpoint note/search with json body
        ex. for json
        {
            "userId" : "user_1",
            "archive" : true
        }
   
 3. Choice technologies and the reason for selection
    * Spring Boot
        Compatiable with restful api's
        Easy development and maintainable solutions can be provided with spring features
    * h2 - Due to demo purpose, we can use as in-memory db
    
 4. Spends about 4-5hrs
    * added additional validation 
        * Notes content cant be exceed more than 250 characters
        * Notes cant add/update with empty note
    * identify some development issues while test cases implementation 