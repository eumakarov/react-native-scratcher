
# react-native-scratch

## Getting started

`$ npm install react-native-scratch --save`

### Mostly automatic installation

`$ react-native link react-native-scratch`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-scratch` and add `RNScratch.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNScratch.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.como.RNTScratchView.ScratchViewPackage;` to the imports at the top of the file
  - Add `new ScratchViewPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-scratch'
  	project(':react-native-scratch').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-scratch/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      implementation project(':react-native-scratch')
  	```


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
