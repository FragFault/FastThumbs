# SpeedType
Original App Design Project - README Template
===

# SpeedType

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Game app that scores how fast users can type.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Gaming/Skill Learning
- **Mobile:** We want this application exclusively for mobile devices as our purpose is to improve users mobile texting skills. Functionality would not be limited to just mobile devices but on principal it is best to keep it mobile for the sake of being unique.
- **Story:** App helps build user mobile typing skills by giving timed prompts. Users can be competitive with one another with global leaderboards.
- **Market:** Our market is for those who want to improve mobile typing skills and competitive natured players.
- **Habit:** This app is encouraged to be used daily because of daily challenges and building skills incrementally.
- **Scope:**First we would start with our app determining how fast and accurate they can type. We would also give users the option to select from a competitive or casual game and view a leaderboard. Our app can expand into either having teams of players competing against other teams (like Clash of Clans) or allow users to create their own prompts/levels that other players can try.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can register an account and login 
* User can select the category, text prompt and if they want casual or competitive and hit play
* Application determines the accuracy and speed at which the user is typing in game
* Users can go to settings and toggle dark mode and can delete their account and report a bug
* User can go to their profile, edit their profile picture, add bio, see past 3 games, and see accuracy & speed stats for both competitive and casual
* Leaderboard shows top 20 players & has infinite pagination. Each user card shows profile picture, username, competitive games played, score(speed & accuracy). User can click on someones name and profile picture and see their profile. 
* User completes prompts in portrait mode with keyboard on the bottom half and prompt on top half.
* Remove ability to copy and paste in the playing fragment.
* After a level was completed, user can share score, category, 

**Optional Nice-to-have Stories**

* Implementing friends that are associated with other users. Users can search up people’s full names, send them a friend request, and the person on the other end can accept the request.
* Giving the ability to play a random prompt the user did not select.
* Users can gain experience points per prompt completed and have a number level associated with them.
* Users can play in Landscape mode which could be separately scored.
* In settings, users can toggle notifications for daily challenge and turn off daily challenge appearing immediately after login


### 2. Screen Archetypes

* Login (Logo, Name of App) 
   * button to move to register
   * The user is able to sign back in automatically if they close out the application 

* Register 
   * Upon Download/Reopening of the application, the user is prompted to log in to gain access to their profile information

* Selection Screen
   * Upon logging in or Registering the user is shown the categories of the types of prompts. Also users can select if they are playing casual or competitive as a toggle button. 

* Gameplay Screen
    * User can type and see their progress and errors in real time. Theres a back button that directs the user back to the game selection screen
 
* Leaderboard
    * The user can see the top 20 scores of every player and click on a user profile which directs them to their profile

* Profile
    * The user can see their average competetive Acuracy and Speed. They can also see their number of points. If they scroll further down they can see the resukts of the last three games played. There is a button on the top right that takes the user to the settings page
    
* Settings
    * The user can change the app theme, toggle notifications, delete their account or report a bug

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Leaderboard
* Play Game
* Profile

**Flow Navigation** (Screen to Screen)

* Login 
   * Registration
   * Selection Screen (Home)
   
* Registration
   * Selection Screen (Home)
   * Login

* Selection Screen
   * Jumps to game

* Gameplay Screen
   * Keyboard
   * Results screen jumps to home

* Leaderboard
   * Jumps to profile

* Profile 
   * Text Fields to be modified
   * Jumps to settings

* Settings
   * Toggle settings
   * Button to report or delete account
  
## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="https://github.com/FragFault/SpeedType/blob/main/3.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups
<img src="https://github.com/FragFault/SpeedType/blob/main/2.png" width=600>

<img src="https://github.com/FragFault/SpeedType/blob/main/1.PNG" width=600>

### [BONUS] Interactive Prototype

## Schema
[This section will be completed in Unit 9]
### Models
#### Post

   | Property  	| Type 	| Description |
   | ------------- | -------- | ------------|
   | Player	| Pointer to User    | unique id for user (default field) |
   | Average Accuracy   |  Number  | Value for user accuracy |
   | Average Speed     	| Number  | Value for user speed |
   | Points  	| Number  | Cumulative points for games played and daily challenges done |
   | Profile Picture | File   | User profile image |
   | Bio	| String   | Profile caption from user  |
   | Games played	| Number | Number of games user has played |
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]


