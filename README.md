# react-native-scratch

Locally stored version of the original package (https://github.com/ConduitMobileRND/react-native-scratch) with some updates to ensure it matches our needs

## Getting started

TODO: post module to npm

### Automatic installation

Starting from react-native version >= 0.60 package is linked automatically, no changes required

## Usage

The ScratchView will fill its containing view and cover all other content untill you scratch it
Just put it as the last component in your view

```javascript
import React, { Component } from 'react';
import { View } from 'react-native';
import ScratchView from 'react-native-scratch'

class MyView extends Component {

	onImageLoadFinished = ({ id, success }) => {
		// Do something
	}

	onScratchProgressChanged = ({ value, id }) => {
		// Do domething like showing the progress to the user
	}

	onScratchDone = ({ isScratchDone, id }) => {
		// Do something
	}

	onScratchTouchStateChanged = ({ id, touchState }) => {
		// Example: change a state value to stop a containing
		// FlatList from scrolling while scratching
		this.setState({ scrollEnabled: !touchState });
	}

	render() {
		return (<View style={{ width: 300, height: 300 }}>
			<ComponentA> // will be covered by the ScratchView
			<ComponentB> // will be covered by the ScratchView
			<ScratchView
				id={1} // ScratchView id (Optional)
				brushSize={10} // Default is 10% of the smallest dimension (width/height)
				threshold={70} // Report full scratch after 70 percentage, change as you see fit. Default is 50
				fadeOut={false} // Disable the fade out animation when scratch is done. Default is true
				placeholderColor="#AAAAAA" // Scratch color while image is loading (or while image not present)
				imageUrl="http://yourUrlToImage.jpg" // A url to your image (Optional)
				resourceName="your_image" // An image resource name (without the extension like '.png/jpg etc') in the native bundle of the app (drawble for Android, Images.xcassets in iOS) (Optional)
				resizeMode="cover|contain|stretch" // Resize the image to fit or fill the scratch view. Default is stretch
				onImageLoadFinished={this.onImageLoadFinished} // Event to indicate that the image has done loading
				onTouchStateChanged={this.onTouchStateChangedMethod} // Touch event (to stop a containing FlatList for example)
				onScratchProgressChanged={this.onScratchProgressChanged} // Scratch progress event while scratching
				onScratchDone={this.onScratchDone} // Scratch is done event
			/>}
		</View>)
	}

export default MyView;
```
