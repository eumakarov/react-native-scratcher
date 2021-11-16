require 'json'
package = JSON.parse(File.read(File.join(__dir__, 'package.json')))

Pod::Spec.new do |s|
  s.name                = "RNScratch"
  s.version             = package["version"]
  s.description         = package["description"]
  s.summary             = package['summary']
  s.license             = package['license']
  s.homepage            = package['homepage']
  s.authors             = "Invertase Limited"
  s.source              = { :git => "https://github.com/eumakarov/react-native-scratcher.git", :tag => "v#{s.version}" }

  s.platform            = :ios, "9.0"
  s.source_files  = "ios/**/*.{h,m}"
  s.dependency      'React'
  s.dependency      'React-Core'
end