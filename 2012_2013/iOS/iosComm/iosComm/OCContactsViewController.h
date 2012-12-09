//
//  OCContactsViewController.h
//  iosComm
//
//  Created by Sauhard Bindal on 11/4/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface OCContactsViewController : UITableViewController <NSFetchedResultsControllerDelegate>
{
	NSFetchedResultsController *fetchedResultsController;
}

//@property (nonatomic, strong) IBOutlet UITableView *tableView;
- (IBAction)testButton:(id)sender;

@end
