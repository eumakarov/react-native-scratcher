#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTViewManager.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>
#import "RNTScratchViewManager.h"
#import "ScratchView.h"

@implementation RNTScratchViewManager

@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();
RCT_EXPORT_VIEW_PROPERTY(placeholderColor, NSString)
RCT_EXPORT_VIEW_PROPERTY(threshold, float)
RCT_EXPORT_VIEW_PROPERTY(brushSize, float)
RCT_EXPORT_VIEW_PROPERTY(imageUrl, NSString);
RCT_EXPORT_VIEW_PROPERTY(resourceName, NSString);
RCT_EXPORT_VIEW_PROPERTY(localImageName, NSString); // deprecated
RCT_EXPORT_VIEW_PROPERTY(resizeMode, NSString);
RCT_EXPORT_VIEW_PROPERTY(onImageLoadFinished, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onTouchStateChanged, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onScratchProgressChanged, RCTBubblingEventBlock);
RCT_EXPORT_VIEW_PROPERTY(onScratchDone, RCTBubblingEventBlock);

RCT_EXPORT_METHOD(reset:(nonnull NSNumber *)reactTag)
{
  [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
    ScratchView *scratchView = (ScratchView*)viewRegistry[reactTag];
    if ([scratchView isKindOfClass:[ScratchView class]]) {
      [scratchView reset];
    }
  }];
}

-(ScratchView *) view
{
  ScratchView *scratchView = [[ScratchView alloc] init];
  scratchView._delegate = self;
  return scratchView;
}

- (void)onImageLoadFinished:(ScratchView *)sender successState:(BOOL)success {
  if (sender.onImageLoadFinished) {
    sender.onImageLoadFinished(@{@"success": success ? @"true" : @"false"});
  }
}

- (void)onScratchProgressChanged:(ScratchView *)sender didChangeProgress:(CGFloat)scratchProgress {
  NSString* formattedScratchProgress = [NSString stringWithFormat:@"%.02f", scratchProgress];
  if (sender.onScratchProgressChanged) {
    sender.onScratchProgressChanged(@{@"progressValue": formattedScratchProgress});
  }
}

- (void)onScratchDone:(ScratchView *)sender isScratchDone:(BOOL)isDone {
  if (sender.onScratchDone) {
    sender.onScratchDone(@{@"isScratchDone": isDone ? @"true" : @"false"});
  }
}

- (void)onTouchStateChanged:(ScratchView *)sender touchState:(BOOL)state {
  if (sender.onTouchStateChanged) {
    sender.onTouchStateChanged(@{@"touchState": state ? @"true" : @"false"});
  }
}

@end
