Changelog
=========

0.4.2
-----
* Correct behaviour of CheckBoxController with values ([#59](https://github.com/dkharrat/NexusDialog/pull/59))
* Honor set prompt for SelectionController
* Fix missed notifications for changes in CheckBox
* Fix bad values shown for CheckBoxController
* Use latest Android gradle plugin and Android SDK

0.4.1
-----
* Fix crash when using Fragments ([#23](https://github.com/dkharrat/NexusDialog/pull/23))
* Form can now be created from Fragments ([#27](https://github.com/dkharrat/NexusDialog/pull/27))
* Fix layout issue with CheckBoxController ([#36](https://github.com/dkharrat/NexusDialog/pull/36))
* CheckBoxController now refreshes the value of each checkbox in createFieldView method ([#19](https://github.com/dkharrat/NexusDialog/issues/19))

0.4.0
-----
* Add TimePickerController ([#15](https://github.com/dkharrat/NexusDialog/pull/15))
* Add CheckBoxController ([#10](https://github.com/dkharrat/NexusDialog/pull/10))
* Support inline errors display method ([#13](https://github.com/dkharrat/NexusDialog/pull/13))
* Support custom field validations ([#16](https://github.com/dkharrat/NexusDialog/pull/16))
* Fix crash on screen rotation ([#11](https://github.com/dkharrat/NexusDialog/issues/11))
* Fix field value duplication after screen rotation ([#12](https://github.com/dkharrat/NexusDialog/issues/12))
* Fix method `FormSectionController#removeElement` having no effect ([#8](https://github.com/dkharrat/NexusDialog/issues/8))

0.3.0
-----
* Updated to use Android SDK 23
* Support adding form elements dynamically ([#5](https://github.com/dkharrat/NexusDialog/issues/5))

0.2.2
-----
* Use same timezone of the specified SimpleDateFormat for DatePickerController.

0.2.1
-----
* Fixed crash in drop-down selection field in Android API 21+.

0.2.0
-----
* Refactored out form management into FormController. Form management is no longer dependent Activity base class.
* Reliably detect text changes of EditText.
* Fixed issue where apps depending on NexusDialog complain about AndroidManifest.xml conflicts.
* Updated project structure to use latest android-gradle.
* Sources are now published to Maven repo too.

0.1.1
-----
* Fixed incorrect packaging name in maven artifact.

0.1.0
-----
* Initial release.
