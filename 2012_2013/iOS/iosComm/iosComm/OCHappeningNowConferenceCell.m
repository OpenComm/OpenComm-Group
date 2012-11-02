//
//  OCHappeningNowConferenceCell.m
//  iosComm
//
//  Created by Sauhard Bindal on 11/2/12.
//  Copyright (c) 2012 OpenComm. All rights reserved.
//

#import "OCHappeningNowConferenceCell.h"

@implementation OCHappeningNowConferenceCell
@synthesize conferenceTitleLabel = _conferenceTitleLabel;
@synthesize thumbnailImageView = _thumbnailImageView;

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
