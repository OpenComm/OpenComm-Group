In this File:

Setting up the Program
Running the Program
Playing with the Program 
Features not Implemented
What is on the screen in relation to the code
Additional Notes

==================================================================

SETTING UP THE PROGRAM

==========


1) Make sure you get Android version 2.2 when downloading Android onto eclipse

2) Create a new ANDROID Project. A screen will pop up. You can name the project name and application name anything you want. However, you must write "Login" in the Create activity slot (case sensitive) and "edu.cornell.opencomm" under the package name.

3) Copy and paste my code files. If there are errors in the code, try refreshing the project.

4) Go to the AndroidManifest.xml, between the line </activity> and </application, insert this line
<activity android:name=".MainApplication"></activity>

5) Then run the application. If it still doesn't work out then please email me. <norangquinn@gmail.com>



==================================================================

RUNNING THE PROGRAM

==========
 
1) When you have loaded the code on eclipse, click run. An android emulator window should pop up that will take a few minutes to load.

2) The login screen should automatically pop up, but if not then you will have to look for the application icon on the "mobile screen"

3) When you have the login screen, just click "Login!". This code does not require an actual login yet for the purposes of testing the code.

4) A screen with 4 icons will show up - this is your "main" chat room. This will hold all the people you want in the conference (assumes you already invited these people from your buddylist and they all agreed).

5) If an error shows up, then a grey screen will pop up saying "Sorry! blahblahblah...". Just click Ok, but do NOT close the whole window... simply click Run again from Eclipse. Closing the emulator window means you would have to wait another 3 minutes for it to load.

==================================================================

PLAYING WITH THE PROGRAM

==========


1) Can highlight single or multiple user icons by "tapping" on them once

2) When user icons are highlighted you can click the trash button to delete them

3) Click the "Add" button to create another Private Room icon at the bottom of the screen

4) If you create too many Private Room icons to fit in the bar, then you can drag the bar left and right to access the hidden icons

5) You can highlight multiple Private Room icons by tapping on them once, you can also delete them the same way with the trash button

6) Clicking on an empty part of the screen should unhighlight all icons

7) Click and drag a user icon over a Private Room icon, the Private Room icon should highlight when you hover over it

8) Releasing a user icon over a Private Room icon should return the user icon back to its original position. Now click on the Private Room icon you released it over.... it should change to a different screen with the person you dropped in it displayed.

9) You can switch between rooms by clicking on the Private Room icons twice (tapping once on a Private Room icon should highlight it, a second tap will open up the room)

10) Clicking on the "Main" button should bring you to your very first chat room created. This supposedly should be the main chat room with everybody.

==================================================================

FEATURES NOT IMPLEMENTED

==========

1) Buddylist
2) Logout button
3) Description of person when you highlight a user's icon
4) A small preview popup of who is in the a Private Room when you highlight a PrivateRoom Icon
5) When you hover over a PrivateRoom icon with a User Icon, the Preview should popup
6) Sending invitations and accepting/declining invites to join a private chat

==================================================================

WHAT IS ON THE SCREEN IN RELATION TO THE CODE

==========

1) LOGIN CLASS: This is the first "activity" (hopefully you read up on that) that is called. Activities are called with onCreate(), and furthermore activities can call other activities. In this case the Login "activity" called the MainApplication "activity" (well there should be activities in between that start the internet service and retrieve the buddylists, etc., but for my code there are only these two classes). Note that this class stuffed some information into an "intent" to pass along to the next activity.

2) MAINAPPLICATION CLASS: This is the important class that is called in the code. Notice that onCreate() instantiates everything and calls setContentView(R.layout.main). The "R" refers to the items you have put in your "res" folder (We had to create our own icons and stick them in this folder). In this case it is calling the XML file "layout.main" which has instructions on how to build the GUI. In Android it is not necessary to construct GUI's through an xml file (you can do it with java too) but it is helpful.

3) ADD, MAIN, TRASH BUTTONS, PRIVATESPACE BAR: These are all features that are initiated in the XML layout.main file. The instructions of what these buttons should do are declared in the MainApplication class.

4) USER ICONS: The user icons you see are referred to in my code as "PersonView" 's ... the PersonView class holds the image and image location for the corresponding "Person" class which holds the concrete information of a user. Since one user may be in multiple private rooms, we need a separate PersonView class so that you can have multiple icons (or PersonView's) for a single "Person"

5) ROOMS: A "Space" object represents a room with people in it. A space object is simply a class that holds information on who is in the room, and the icons in the room. Each Space object should have a corresponding "PrivateSpaceView" object, which would be one of the square icons at the bottom of the screen.

6) DISPLAYING THE ROOM: Notice that in layout.main that we have created a "spaceview" object that we declared in a different class. The "SpaceView" object represents the portion of the screen NOT occupied by the Main, Trash, PrivateRoom Bar, and Add button. This object or view holds the user icons that are to be displayed in the moment. Should a user open up a different room, then the spaceview object's contents are emptied and filled up again to match the new room's display contents. (This is my way of doing switching between rooms, you may find a better way to do this).

7) PREVIEW CLASS: Ignore for now.

==================================================================

ADDITIONAL NOTES

==========

1) DECLARING XML VALUES: If you open up res>values>strings.xml you can input values for strings, colors, etc. so that you can call these easily from your xml file.

2) DEBUGGING: The (import android.util.Log) resource allows you to do something similar to System.out.println for Android. You declare a string "tag" at the top of each class, and then use Log.v(tag, "message i want to say"). When you run your program, you can open up LogCat (with a green Android button) on your eclipse. You can then see your message when the program gets to it. This is very useful for debugging.

3) ANDROID MANIFEST: Should you choose to add another "activity", you need to let the Android Manifest file know

4) R.JAVA: Don't touch this class, it will screw you over.

5) ADDING FILES TO RES: If you need to put in content (such as user content, or more xml layout files) into this folder, make sure you refresh the whole project. These files need to be generated in the R.java class before your code will recognize it.

==================================================================


==================================================================