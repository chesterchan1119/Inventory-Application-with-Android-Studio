# Inventory Application with Android Studio



## Tab 1: Item Types


The first tab have four types, `Games`, `Gifts`, `Materials`, and `Books`, with a list view. Clicking each item would navigate to another list view, showing all the items as returned by the API server.
##
![image](https://user-images.githubusercontent.com/123936429/236678584-59e842c4-fa5e-4da7-9d0f-04ce30ec239f.png)


### Feature: 

:+1:Getting into a category...

![image](https://user-images.githubusercontent.com/123936429/236678285-366aa4bf-c376-4e73-ba41-a3e6f5092e05.png)


:+1:Pagination: once getting to the page buttom, you would see a loading progress icon, and after a few seconds you get to the next page of this item category.

![image](https://user-images.githubusercontent.com/123936429/236678329-6e4d77ab-06c0-47c7-80b4-fd1cc8caba4c.png)

## Tab 2: Search
![image](https://user-images.githubusercontent.com/123936429/236678375-0b79f1f3-5b3b-41be-aae2-88516328db80.png)

##
The second tab should display a search box, allowing the user to type in a keyword. By calling an API, the search result will be returned, and you should develop a list view to display the result.

### Feature: 

:+1:Filter Bar, the selected category's button would turn blue

![image](https://user-images.githubusercontent.com/123936429/232090415-a3d47d7d-a8fb-4ccf-83e5-b46920bdf116.png)

:+1:Loading Icon would be shown before result popped out

![image](https://user-images.githubusercontent.com/123936429/232091560-41b01785-6cd3-4bb3-92d3-da712160973d.png)

:+1: Description would be shown if pressed on the item. 
![image](https://user-images.githubusercontent.com/123936429/236678435-cb4632bf-e8b0-445c-b220-c53025a929b7.png)



## Tab 3: User Login
![Screenshot 2023-04-14 at 11 28 35 PM](https://user-images.githubusercontent.com/123936429/232087784-0c7dec8e-1bf0-4d6d-b8e6-d08ea4661c1c.png)
##
The third tab should allow the user to log in. Only logged-in users can borrow, return, or consume items from the system.
##
### Feature:
:+1:Once You logged in, you would get a notification, informing you you have logged in.
##
![image](https://user-images.githubusercontent.com/123936429/232088083-3bf097bd-d73a-49d9-83f1-09a73536e0f2.png)



:+1: Once you press `Confirm`, you would have a customised User Info Page, which shows you username and credential.
![image](https://user-images.githubusercontent.com/123936429/232088614-5f3df6d1-70bb-4359-b328-6643a5079c9e.png)


:+1:And you access to the borrow/return button would be released. 
##
![image](https://user-images.githubusercontent.com/123936429/236678548-28d5d03f-87b5-42f2-8298-9d797547e52b.png)




## Limitation
1. Sometimes, it might need 2 times of rendering to fetch the visualised Search Result.
2. Filter bar is required with the Searching System


