//
//  OCConferenceViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/2/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCConferenceViewController.h"

#import "OCConferenceTableCell.h"
#import "OCHappeningNowConferenceCell.h"

@interface OCConferenceViewController ()

@end

@implementation OCConferenceViewController{
    NSArray *conferencesHappeningNow;
    NSArray *conferencesInvited;
    NSArray *conferencesUpcoming;
    NSArray *conferenceThumbnails;
    NSArray *conferencesInvitedTime;
    NSArray *conferencesUpcomingTime;
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // Initialize conferences
    conferencesHappeningNow = [NSArray arrayWithObjects:@"Conference title 1", @"Conference title 2", nil];
    conferencesInvited = [NSArray arrayWithObjects:@"Invited Conference 1", @"Invited Conference 2", @"Invited Conference 3", @"Invited Conference 4", nil];
    conferencesUpcoming = [NSArray arrayWithObjects:@"Upcoming Conference 1", @"Upcoming Conference 2", @"Upcoming Conference 3", @"Upcoming Conference 4", @"Upcoming Conference 5", nil];
    
    // Initialize thumbnails
    conferenceThumbnails = [NSArray arrayWithObjects:@"zoo1.jpeg", @"zoo2.jpeg", @"zoo3.jpeg", @"zoo4.jpeg", @"zoo5.jpeg", @"zoo6.jpeg", @"zoo7.jpeg", @"zoo8.jpeg", @"zoo9.jpeg", @"zoo10.jpeg", nil];
    
    
    // Initialize Times
    conferencesInvitedTime = [NSArray arrayWithObjects:@"4:00pm, October 27", @"11:00am, October 31", @"2:00pm, November 2", @"9:00am, November 15", nil];
    
    conferencesUpcomingTime = [NSArray arrayWithObjects:@"4:00pm, October 27", @"11:00am, October 31", @"2:00pm, November 1", @"9:00am, November 2", @"9:00am, November 3", nil];
    
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
 
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 3;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    if (section==0)
        return [conferencesHappeningNow count];
    else if (section ==1)
        return [conferencesInvited count];
    else
        return [conferencesUpcoming count];

}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier2 = @"ConferenceTableCell";
    OCConferenceTableCell *cell;
    
    
    if (indexPath.section==0){
        static NSString *simpleTableIdentifier = @"HappeningNowTableCell";
        
        OCHappeningNowConferenceCell *cell = (OCHappeningNowConferenceCell *)[tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
        if (cell == nil)
        {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"HappeningNowTableCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        
        
        cell.conferenceTitleLabel.text = [conferencesHappeningNow objectAtIndex:indexPath.row];
        // cell.imageView.bounds = CGRectMake(0, 0, 25, 55);
        cell.thumbnailImageView.image = [UIImage imageNamed:[conferenceThumbnails objectAtIndex:indexPath.row]];
        return cell;
        
    }
    else if (indexPath.section==1){
        cell = (OCConferenceTableCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier2];
        if (cell == nil)
        {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"ConferenceTableCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        cell.conferenceTitleLabel.text = [conferencesInvited objectAtIndex:indexPath.row];
        cell.conferenceTimeLabel.text = [conferencesInvitedTime objectAtIndex:indexPath.row];
        cell.thumbnailImageView.image = [UIImage imageNamed:[conferenceThumbnails objectAtIndex:indexPath.row]];
    }
    else {
        cell = (OCConferenceTableCell *)[tableView dequeueReusableCellWithIdentifier:CellIdentifier2];
        if (cell == nil)
        {
            NSArray *nib = [[NSBundle mainBundle] loadNibNamed:@"ConferenceTableCell" owner:self options:nil];
            cell = [nib objectAtIndex:0];
        }
        cell.conferenceTitleLabel.text = [conferencesUpcoming objectAtIndex:indexPath.row];
        cell.conferenceTimeLabel.text = [conferencesUpcomingTime objectAtIndex:indexPath.row];
        cell.thumbnailImageView.image = [UIImage imageNamed:[conferenceThumbnails objectAtIndex:indexPath.row]];
    }
    
    
    return cell;
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    if (section==0)
        return @"happening now";
    else if (section ==1)
        return @"invited";
    else
        return @"upcoming";
    //return NSLocalizedString(@"Title for Section", @"test");
}


/*
// Override to support conditional editing of the table view.
- (BOOL)tableView:(UITableView *)tableView canEditRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the specified item to be editable.
    return YES;
}
*/

/*
// Override to support editing the table view.
- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        // Delete the row from the data source
        [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationFade];
    }   
    else if (editingStyle == UITableViewCellEditingStyleInsert) {
        // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
    }   
}
*/

/*
// Override to support rearranging the table view.
- (void)tableView:(UITableView *)tableView moveRowAtIndexPath:(NSIndexPath *)fromIndexPath toIndexPath:(NSIndexPath *)toIndexPath
{
}
*/

/*
// Override to support conditional rearranging of the table view.
- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Return NO if you do not want the item to be re-orderable.
    return YES;
}
*/

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    // Navigation logic may go here. Create and push another view controller.
    /*
     <#DetailViewController#> *detailViewController = [[<#DetailViewController#> alloc] initWithNibName:@"<#Nib name#>" bundle:nil];
     // ...
     // Pass the selected object to the new view controller.
     [self.navigationController pushViewController:detailViewController animated:YES];
     */
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 55;
}
@end
