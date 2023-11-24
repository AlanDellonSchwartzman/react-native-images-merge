
import Foundation
import UIKit
@objc(ImagesMerge)
class ImagesMerge : NSObject{
  
  @objc
  func mergeImages(_ imagesData:NSArray, callback: @escaping RCTResponseSenderBlock) {
    DispatchQueue.main.async {
      var images = [UIImage]()
      var orient = "horizontal"
      for imgDdict in imagesData as! [NSDictionary]{
       let img = imgDdict["uri"] as! String
        if img.contains("data:image/png;base64,"){
          images.append(UIImage(data: Data(base64Encoded: img.replacingOccurrences(of: "data:image/png;base64,", with: ""))!)!)
        }
        orient = imgDdict["orients"] as! String
      }

      var allWidth : CGFloat = CGFloat(0)
      var allHeight : CGFloat = CGFloat(0)
      for img in images{
        if orient == "vertical" {
          if img.size.width > allWidth {
            allWidth = img.size.width
          }
          allHeight = allHeight + img.size.height
        }else{
          allWidth = allWidth + img.size.width
          if img.size.height > allHeight {
            allHeight = img.size.height
          }
        }
      }
      let size = CGSize(width: allWidth , height: allHeight)
      UIGraphicsBeginImageContext(size)
      var lastPos : CGFloat = CGFloat(0)
      if orient == "vertical" {
        for img in images{
          img.draw(in: CGRect(x: 0, y:lastPos, width: size.width, height: img.size.height ))
          lastPos += img.size.height
        }
      }else {
        for img in images{
          img.draw(in: CGRect(x: lastPos, y:0, width: img.size.width, height: size.height ))
          lastPos += img.size.width
        }
      }

      // if orient == "vertical" {
      //   let size = CGSize(width: images[0].size.width , height: images[0].size.height * CGFloat(imagesData.count))
      //   UIGraphicsBeginImageContext(size)
      //   var lastPos : CGFloat = CGFloat(0)
      //   for img in images{
      //     img.draw(in: CGRect(x: 0, y:lastPos, width: size.width, height: img.size.height ))
      //     lastPos += img.size.height
      //   }
      // }else {
      //   let size = CGSize(width: images[0].size.width * CGFloat(imagesData.count) , height: images[0].size.height)
      //   UIGraphicsBeginImageContext(size)
      //   var lastPos : CGFloat = CGFloat(0)
      //   for img in images{
      //     img.draw(in: CGRect(x: lastPos, y:0, width: img.size.width, height: size.height ))
      //     lastPos += img.size.width
      //   }
      // }

      let myImage = UIGraphicsGetImageFromCurrentImageContext()
      let imgDt = myImage!.pngData()!
      
      UIGraphicsEndImageContext()
      callback([imgDt.base64EncodedString()])
    }
  }
  
  
  @objc
  static func requiresMainQueueSetup() -> Bool {
    return true
  }
  
}


