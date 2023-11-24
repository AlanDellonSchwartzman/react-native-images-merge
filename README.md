# react-native-images-merge

Additional edits from `react-native-images-merge` https://www.npmjs.com/package/react-native-images-merge

## Platform Supported
* [x] Android
* [x] iOS

## Getting started

```bash
$ npm install react-native-images-merge --save
```
```
cd ios 
pod install
```
## Mostly automatic installation

```bash
$ react-native link react-native-images-merge
```

## Usage
```javascript
import ImagesMerge from 'react-native-images-merge';

//Android
    ImagesMerge.mergeImages([
        {uri: 'data:image/png;base64,...'},
        { uri: 'data:image/png;base64,...' }
    ],orient)

//IOS
    ImagesMerge.mergeImages([
        {uri: 'data:image/png;base64,...',orients: "horizontal"},
        {uri: 'data:image/png;base64,...',orients: "horizontal"}
    ],(result) => {
        console.log(result)
    });
```
*orient : string{"vertical","horizontal"} default "horizontal"