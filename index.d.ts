declare module "react-native-scratcher" {
  import React from "react";
  import { ViewProperties } from "react-native";

  export default class ScratchView extends React.Component<
    ViewProperties & {
      id?: number; // ScratchView id (Optional)
      brushSize: number; // Default is 10% of the smallest dimension (width/height)
      threshold: number; // Report full scratch after 70 percentage, change as you see fit. Default is 50
      fadeOut: boolean; // Disable the fade out animation when scratch is done. Default is true
      placeholderColor: string; // Scratch color while image is loading (or while image not present)
      imageUrl?: string;
      resourceName?: string; // A url to your image (Optional)
      onImageLoadFinished?: (event: { id: number; success: boolean }) => void; // Event to indicate that the image has done loading
      onTouchStateChanged?: (event: {
        id?: number;
        touchState: boolean;
      }) => void; // Touch event (to stop a containing FlatList for example)
      onScratchProgressChanged?: (event: {
        value: number;
        id?: number;
      }) => void; // Scratch progress event while scratching
      onScratchDone?: (event: { isScratchDone: boolean; id?: number }) => void; // Scratch is done event
    }
  > {}
}
