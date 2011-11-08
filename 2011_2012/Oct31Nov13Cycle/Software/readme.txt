Hello,

Please don't touch this code unless you are on Opencomm Software Development Team or Network Team. Network team - please please PLEASE don't commit anything that does not run, as our team will be working extensively on this file.

Nora 
11/1/2011



********For Integration Team 11/5/11 - Risa and Nora*********

ISSUES and NOTES:

1) When I call inviteUser() from the invitationController class, it gives a popup on the network to the user to accept or decline the invitation, which is as expected. However, 80% of the time it will not call join() after I have accepted the invite using Pidgin. Please, make sure join() is always called. 

Thus, I made a semi-hack. I created a new joined() method within the ParticipantController class (not the ParticipantStatusListener class b/c I was not sure if that was accessible from outside classes) This new joined() method deals with the UI component of adding a person to a space and the icon to a SpaceView. I also simply copied the splitUserRoomInfo class to it as well for joined() to access it.

To call joined(), all you need to do is press the J key, which should add mucopencomm to the mainspace.

2) UI team has not yet changed the double click to a press and hold, however it will be done soon. In the meantime, once we have double-clicked we call kickoutUser() from KickoutListener class. Everytime we try and delete a person however, we keep on getting an XMPP Exception with my error message "Darn, can't kick this person out!". Could you take a look at this?

3) I made it so that the S key would initiate new private spaces. 

**********For Integration Team 11/5/11 - Vinay and Nora***************

In the code I've marked places you should look at with //TODO - VINAY

I've already spoken to you about this, but just as a reminder, please fix the code such that

1) When a privatespace icon is tapped once, then the popup preview appears, and the icon is highlighted (we might take the highlight out later, but keep in
for now)
2) Tapping the private space icon a second time will close the popup preview and unhighlight the icon
3) Tapping the popup preview itself will change the spaceview such that it represents the same room that the popup preview represents.

I wrote the methods I thought necessary in the PrivateSpaceIconController and privateSpacePreviewPopupController classes. However, for some reason, after clicking on a PS icon and displaying the popup preview, the program no longer detects it when i try and tap the private space icon a 2nd time. I think it may have something to do with the differnet xml layouts. You are more familiar with it so please look at it.



