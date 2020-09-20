# Thirdfort Coding Challenge

1. How to run the application?
   * need java 11
   * open command prompt
   * move to folder src/main/resources/
   * execute command "java -jar note.jar"
   * port 8080 should not be allocated for other services

2. Instruction to UX team
   * To add note - call the endpoint note/add with json body
        example json:
        {
            "userId" : "user2",
            "noteContent" : "this is user update message"
        }
        default archive value will be set to "false"
   * To update note - call the endpoint note/update with json body
        example json:
        {
             "noteId" : 1,
             "userId" : "user2",
             "noteContent" : "this is user update message"
        }
   * Delete a existing note - call the endpoint note/delete with json body
        example json
        {
            "noteId" : 1
        }
   * Archive a note - call the endpoint note/archive with json body
        example json
        {
            "noteId" : 1
        }
   * Unarchive a note - call the endpoint note/unarchive with json body
        example json
        {
            "noteId" : 1
        }
   * List archived notes - call the endpoint note/search with json body
        example json
        {
            "userId" : "user_1",
            "archive" : true
        }
   * List unarchived notes - call the endpoint note/search with json body
        example json
        {
            "userId" : "user_1",
            "archive" : true
        }
   * List all notes (include archive and unarchive) - call endpoint note/
        example json
        {
            "userId" : "user_1"
        }
   * fetch note by id - call endpoint note/getnote
        example json
        {
            "noteId" : 2
        }
   
 3. Choice technologies and the reason for selection
    * Spring Boot
        Compatiable with restful api's
        Easy development and maintainable solutions can be provided with spring features
    * h2 - Due to demo purpose, we can use as in-memory db
    
 4. Spends about 6hrs
    * added additional validation
        * Note titles cannot be empty or exceed 50 characters
        * Notes content cant be empty or exceed more than 250 characters
        * Throws an error when execute update/ archive/ unarchive/ delete for non exist notes
    * additional features
        * User does not need fetch note content when user need to view notes, so note content will become empty when fetch all/ archive/ unarchive notes
        * Users can fetch note by id, then note content will be fetched
    * Should add
        * User authorization when doing the operations.