
![Build Status](https://img.shields.io/github/actions/workflow/status/BHSRobotix/DevilLib/CI.yml?style=for-the-badge)
![Version](https://img.shields.io/github/v/tag/BHSRobotix/DevilLib?label=Version&style=for-the-badge)
![Code Size](https://img.shields.io/github/languages/code-size/BHSRobotix/DevilLib?style=for-the-badge)

![License](https://img.shields.io/github/license/BHSRobotix/DevilLib?style=for-the-badge)
![Issues](https://img.shields.io/github/issues/BHSRobotix/DevilLib?style=for-the-badge)
![Pull Requests](https://img.shields.io/github/issues-pr/BHSRobotix/DevilLib?style=for-the-badge)
![Contributors](https://img.shields.io/github/contributors/BHSRobotix/DevilLib?style=for-the-badge)

![DevilLib](https://raw.githubusercontent.com/BHSRobotix/DevilLib/main/res/devilliblogo.png)

# DevilLib
A library of useful classes and functions for FRC teams.

Developed by the DevilBotz (Team 2876) team.

## Installation

### VendorDeps
Add the file DevilLib.json from the releases page to your vendordeps folder.
Use the wpilib extension to install the library.
1. Open the command palette (Ctrl+Shift+P)
2. Type "WPILib: Manage Vendor Libraries"
3. Select "Install New Library (Online)"
4. Enter the URL to the DevilLib.json file from the releases page.
5. Build your project.
6. You're done!

## Building Locally
1. Append ``-Dlocal=true`` to the end of your gradle build
2. Change the version tag in the target code to be ``local``
3. Add the below code to your build.gradle
```
repositories {
    maven {
        name 'local'
        url 'file:///' + System.getProperty('user.home') + '/.m2/repository'
    }
}
``` 
5. Deploy the library using ``./gradlew publishToMavenLocal -Dlocal=true``

## Releases and Changelog
### v0.1.0
- Initial Release
- Set up project structure
