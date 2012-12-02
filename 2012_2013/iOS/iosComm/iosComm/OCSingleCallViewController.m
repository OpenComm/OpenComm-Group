//
//  OCSingleCallViewController.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/28/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCSingleCallViewController.h"
#import "pjmedia.h"

BOOL callActive = YES;


@implementation OCSingleCallViewController{
    NSMutableDictionary* boxIcon;
    NSMutableArray* contactArray;
    
    NSMutableArray* imageNames;
    NSMutableArray* imagesArray;
    NSMutableArray* boxColorImages;
    NSMutableArray* boxLocations;
    
    NSMutableDictionary* iconBeingMoved;
}

//@synthesize callActive;
@synthesize imageJoker;
@synthesize imageBane;
@synthesize imageCatwoman;

@synthesize nBox;
@synthesize wBox;
@synthesize eBox;

@synthesize nwBox;
@synthesize neBox;
@synthesize swBox;
@synthesize seBox;


- (void) touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event
{
    iconBeingMoved = nil;
    
    NSMutableDictionary* box;
    UIImageView* iconImage;
    
    UITouch *touch = [[event allTouches] anyObject];
    CGPoint location = [touch locationInView:touch.view];
    //imageJoker.center = location;
//    if(CGRectContainsPoint(imageJoker.frame, location)){
//        imageJoker.center = location;
//    }
    
    for (int i =0; i<[contactArray count]; i++){
        box = [contactArray objectAtIndex:i];
        iconImage = [box valueForKey:@"ContactIcon"];
        
        if((iconImage!=nil) && CGRectContainsPoint(iconImage.frame, location)){
            //NSLog(@"Current Item being moved is: %@", [box valueForKey:@"Name"]);
            iconBeingMoved = box;
            iconImage.center = location;
            break;
        }
    }
    
    
}

- (void) touchesMoved:(NSSet *)touches withEvent:(UIEvent *)event
{
    //[self touchesBegan:touches withEvent:event];
    UIImageView* iconImage;
    UITouch *touch = [[event allTouches] anyObject];
    CGPoint location = [touch locationInView:touch.view];
    
    if (iconBeingMoved != nil){
          iconImage = [iconBeingMoved valueForKey:@"ContactIcon"];
          if(CGRectContainsPoint(iconImage.frame, location)){
            iconImage.center = location;
        }
    }
}

- (void) touchesEnded:(NSSet *)touches withEvent:(UIEvent *)event
{
    UITouch *touch = [[event allTouches] anyObject];
    CGPoint location = [touch locationInView:touch.view];
    UIImageView* iconImage ,*boxObject, *originBox, *existingImage;
    NSMutableDictionary* box;
    
    
    if (iconBeingMoved != nil){
        originBox = [iconBeingMoved valueForKey:@"BoxObject"];
        iconImage = [iconBeingMoved valueForKey:@"ContactIcon"];
        for (int i =0; i<[contactArray count]; i++){
            box = [contactArray objectAtIndex:i];
            if(![box isEqual:iconBeingMoved]){
                boxObject = [box valueForKey:@"BoxObject"];
            
                if(CGRectContainsPoint(boxObject.frame, location)){
                
                    if([box valueForKey:@"ContactIcon"]==nil){
                        //NSLog(@"Empty box");
                        iconImage.center = boxObject.center;
                        
                        [box setValue:iconImage forKey:@"ContactIcon"];
                        [iconBeingMoved setValue:nil forKey:@"ContactIcon"];
                    }
                    else{
                        //NSLog(@"Box is currently occupied, need to swap");
                        
                        existingImage = [box valueForKey:@"ContactIcon"];
                        iconImage.center = boxObject.center;
                        existingImage.center = originBox.center;
                        [box setValue:iconImage forKey:@"ContactIcon"];
                        [iconBeingMoved setValue:existingImage forKey:@"ContactIcon"];                        
                    }
                    //originBox = boxObject;
                    break;
                }
                else{
                    iconImage.center = originBox.center;
                }
            }
        }
        
        
    }
    [self updateBoxColors];
    
//    if(CGRectContainsPoint(imageJoker.frame, location)){
//        //imageJoker.center = location;
//        if(CGRectContainsPoint(nwBox.frame, location)){
//            imageJoker.center = nwBox.center;
//        }
//        else{
//            imageJoker.center = wBox.center;
//        }
//    }


}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    
    contactArray = [[NSMutableArray alloc] init];
    
    imageNames = [NSMutableArray arrayWithObjects:@"Joker", @"Bane", @"Catwoman", nil];
    imagesArray = [NSMutableArray arrayWithObjects:imageJoker,imageBane,imageCatwoman, nil];
    boxColorImages = [NSMutableArray arrayWithObjects:@"green_box.png", @"orange_box.png",@"blue_box.png", @"user-box.png", nil];
    boxLocations = [NSMutableArray arrayWithObjects:swBox, wBox, nwBox, nBox, neBox, eBox, seBox, nil];
    
    imageJoker.center = wBox.center;
    imageBane.center = nBox.center;
    imageCatwoman.center = eBox.center;
    
    [self setUpDictionary];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void) setUpDictionary
{
    
//    int boxLocationsSize = [boxLocations count];
//    for (int i = 0; i<numContacts; i++){
//        boxIcon = [[NSMutableDictionary alloc] init];
//        
//        NSString* name = [imageNames objectAtIndex:i];
//        [boxIcon setValue:name forKey:@"Name"];
//        
//        UIImageView* image = [imagesArray objectAtIndex:i];
//        [boxIcon setValue:image forKey:@"Image"];
//        
//        for (int j = 0; j<boxLocationsSize; j++){
//            UIImageView* box = [boxLocations objectAtIndex:j];
//            if(CGPointEqualToPoint(box.center, image.center)){
//                NSLog(@"In if");
//                [boxIcon setValue:box forKey:@"CurrentBox"];
//                //[contactIcon setValue:box.center forKey:@"Origin"];
//                
//                UIImage* boxImage = [UIImage imageNamed:[boxColorImages objectAtIndex:i]];
//                box.image = boxImage;
//                [boxIcon setValue:boxImage forKey:@"BoxImage"];
//            }
//        }
//        
//        [contactArray addObject:boxIcon];
//    }
    
    int numContacts = [imagesArray count];
    int numBoxes = [boxLocations count];
    for (int i = 0; i<numBoxes; i++)
    {
        boxIcon = [[NSMutableDictionary alloc] init];
        UIImageView* box = [boxLocations objectAtIndex:i];
        [boxIcon setValue:box forKey:@"BoxObject"];
        
        for (int j = 0; j<numContacts; j++)
        {
            UIImageView* image = [imagesArray objectAtIndex:j];
            UIImage* boxImage;
            
            if(CGPointEqualToPoint(box.center, image.center)){
                [boxIcon setValue:image forKey:@"ContactIcon"];
                boxImage = [UIImage imageNamed:[boxColorImages objectAtIndex:j]];
                box.image = boxImage;
                [boxIcon setValue:boxImage forKey:@"BoxImage"];
                break;
            }
            else {
                [boxIcon setValue:nil forKey:@"ContactIcon"];
                boxImage = [UIImage imageNamed:[boxColorImages objectAtIndex:3]];
                //NSLog(@"test: %i", i);
                box.image = boxImage;
                [boxIcon setValue:boxImage forKey:@"BoxImage"];
            }
            
        }
        [contactArray addObject:boxIcon];
    }
    NSLog(@"Array: %i", [contactArray count]);
    
}

-(void) updateBoxColors
{
    //NSLog(@"In color Change");
    
    NSMutableDictionary* box;
    UIImageView* iconImage, *boxObject;
    UIImage* boxImage;
    
    for (int i =0; i<[contactArray count]; i++)
    {
        box = [contactArray objectAtIndex:i];
        iconImage = [box valueForKey:@"ContactIcon"];
        boxObject = [box valueForKey:@"BoxObject"];
        if (iconImage!=nil){
            // Update the box color
            for(int j=0; j<[imagesArray count]; j++)
            {
                if([iconImage isEqual:[imagesArray objectAtIndex:j]]){
                    boxImage = [UIImage imageNamed:[boxColorImages objectAtIndex:j]];
                }
            }
        }
        else{
            boxImage = [UIImage imageNamed:[boxColorImages objectAtIndex:3]];
        }
        boxObject.image = boxImage;
        [box setValue:boxImage forKey:@"BoxImage"];
    }
}

- (IBAction)cancelButtonPressed:(id)sender {
    if (callActive == YES) {
        
        pjmedia_antimain();
        callActive = NO;
        //[self dismissModalViewControllerAnimated:YES];
        [[delegateHandler getJingle] terminate];
    }
    else{
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Call not active"
                             message:@"The call is not active press Back button to go back to conferences page"
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
        
    }
    
    
}

- (IBAction)conferencesButtonPressed:(id)sender {
    if (callActive == YES) {
        UIAlertView *info = [
                             [UIAlertView alloc]
                             initWithTitle:@"Call active"
                             message:@"The call is active please cancel call before going back"
                             delegate:self
                             cancelButtonTitle:@"Dismiss"
                             otherButtonTitles:nil
                             ];
        [info show];
    }
    else {
        [self performSegueWithIdentifier:@"backToConferences" sender:self];
    }
}

- (IBAction)infoButtonPressed:(id)sender {
    NSString* msg;
    NSLog(@"Info Button Pressed");
    if (callActive == YES) {
        msg = @"The call is currently active";
    }
    else {
        msg = @"The call is not active";
        
    }
    UIAlertView *info = [
                         [UIAlertView alloc]
                         initWithTitle:@"Call Status"
                         message:msg
                         delegate:self
                         cancelButtonTitle:@"Dismiss"
                         otherButtonTitles:nil
                         ];
    [info show];
}

- (void)viewDidUnload {
    [self setImageJoker:nil];
    [self setImageBane:nil];
    [self setImageCatwoman:nil];
    [self setNwBox:nil];
    [self setNeBox:nil];
    [self setSeBox:nil];
    [self setSwBox:nil];
    [self setNBox:nil];
    [self setWBox:nil];
    [self setEBox:nil];
    [super viewDidUnload];
}
@end
