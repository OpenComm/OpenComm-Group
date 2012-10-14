hi

I've added a remote git branch called "iOS" already.  So this should all work. :)

Please do a "git pull"
If this is unsuccessful because of conflict, evaluate whether you'd like to keep your working copy or not.
Most likely for now, you don't need to save your working copy.  So do "git stash save" this command saves your local changes on a stack and you can reapply them later.
When "git pull" is complete, do a "git branch"
If there is "master" and "iOS", you're good.
If you're not good (only a "master"), then do a "git checkout --track origin/iOS" This command creates a local branch iOS and your local iOS branch will correspond with the remote(origin) iOS branch.
Remember when you did a "git branch"? Now, you should have "master" and "iOS" for sure.  So... do it again, but there should be a "*" to the left of one of the names.
If the * is left of "master", then issue a "git checkout iOS" this switches branches to iOS.  So you'll be working in the iOS branch. (anytime you want to switch to any branch, just use "git checkout <branch_name>", but we should stay in iOS)
If the * is in "iOS", then horray, you're good!

Now, since you're working in iOS already, let's commit something: 
Create file in the iOS FOLDER named "<netid>.txt".
Issue "git status" - this command tells you all that's changed from the most recent pull/fetch.  You should see something like "on branch iOS \n changes not tracked <netid>.txt"
Add add changes to the branch to be tracked (to be recorded) with "git add <netid>.txt"
Then issue a "git commit -m '<Write some message here>'" this command says that these changes have been recorded.
IMPORTANT ******************* issue "git push origin iOS"  ******************** IMPORTANT 
There are many ways of pushing to the remote iOS branch, but this is the safest branching wise.

Then you should have no errors, if you do, just email me.
