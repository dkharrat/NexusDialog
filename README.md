NexusDialog
===========
**Simple Form Generator for Android**

NexusDialog is a library that allows you to dynamically generate forms in Android with little code. It's great
for apps with many form-based UIs, since it reduces the boilerplate code to setup the view layout and tie things
together in the Activity. Currently, it supports Android API 10+.

This library follows [semantic versioning](http://semver.org/). Note that since this library is still active in
development, new releases might introduce interface-breaking changes, which will be indicated in the changelog.
NexusDialog 1.0.0 will be the first stable release.

A Simple Example
----------------
To give you an idea of the simplicity of NexusDialog, here's a screenshot of a simple example:

![screenshot](http://dkharrat.github.io/NexusDialog/images/screenshot01.png "NexusDialog Screenshot")

Here's the code for that example (less than 7 lines of real code!):

    import java.util.Arrays;
    import com.github.dkharrat.nexusdialog.FormActivity;
    import com.github.dkharrat.nexusdialog.controllers.*;

    public class SimpleExample extends FormActivity {

        @Override protected void initForm() {
            setTitle("Simple Example");

            FormSectionController section = new FormSectionController(this, "Personal Info");
            section.addElement(new EditTextController(this, "firstName", "First name"));
            section.addElement(new EditTextController(this, "lastName", "Last name"));
            section.addElement(new SelectionController(this, "gender", "Gender", true, "Select", Arrays.asList("Male", "Female"), true));

            getFormController().addSection(section);
        }
    }

For more examples, browse the [sample](http://github.com/dkharrat/NexusDialog/tree/master/sample) directory.

Features
--------
NexusDialog supports many built-in fields for your form, like text boxes, date pickers, spinners, etc. The framework is
also designed to be extensible so that you can easily add custom form elements if needed. Contributions are also
welcome! If you've implemented a custom control that is useful, pull requests are welcome and appreciated! Currently,
the following form elements are supported:

* `ValueController`: Shows a TextView containing a value
* `EditTextController`: EditText view that allows for free-form text input.
* `CheckBoxController`: CheckBox view that allows for two-states: either checked or unchecked.
* `DatePickerController`: Displays a date picker to allow choosing a specific date
* `TimePickerController`: Displays a time picker to allow choosing a specific time
* `SelectionController`: Displays a spinner with a list of item to select from
* `SearchableSelectionController`: Displays a (typically large) list of items to select from, with the ability to
    search the list and also allow free-form text.

Apps Using NexusDialog
----------------------
Do you have an app that's utilizing NexusDialog? [Let me know](mailto:dkharrat@gmail.com) and I'll add a link to it here!

How to Add NexusDialog to Your Project
--------------------------------------
There are multiple ways to include your project, depending on your build environment:

#### Gradle

Add the following dependency to your build.gradle file for your project:

    dependencies {
      compile 'com.github.dkharrat.nexusdialog:nexusdialog:0.4.2'
    }

Make sure your application is using the Android SDK v23 or later in your build.gradle file:

    android {
        compileSdkVersion 23
        buildToolsVersion "23"
        ...
    }

#### Maven

Add the following dependency to your pom.xml file for your project (requires android-maven-plugin 3.8.0+):

    <dependency>
        <groupId>com.github.dkharrat.nexusdialog</groupId>
        <artifactId>nexusdialog</artifactId>
        <version>0.4.2</version>
        <type>aar</type>
    </dependency>

#### Android Studio or IntelliJ 13+

Add the appropriate dependency in your build.gradle file and refresh your project.

How to Use NexusDialog
----------------------
Once NexusDialog is setup as a dependency in your project (by following the instructions above), you can start creating
forms right away! The main classes you will be working with the most are:

* `FormActivity` or `FormActivityWithActionBar`: If you wish to use the default Activity implementation for NexusDialog,
  this is the base class for each activity you wish to display a form in. Your activity must inherit from it. Form setup
  is done in the `initForm()` method.

* `FormController`: This is the main class that manages the form elements of NexusDialog. It provides simple APIs to
  quickly create and manage form fields. `FormActivity` creates this class for you, or you can be create one manually
  for custom Activities.

* `FormElementController`: Although you will not be using this class directly, you will be using its subclasses. All
  form elements (text boxes, labels, sections, etc.) inherit from this base class, which provides them common
  functionality and properties they need. Also, you could use `FormSectionController` to group a set of form fields
  together.

* `FormModel`: This class abstracts the data model your form will use. It's the interface that NexusDialog uses to
  access and update the underlying data model the form is based on. A `FormActivity` uses it to initialize the field
  values to the desired values when its first displayed, as well as update the underlying model when values change in
  the UI. `FromActivity` uses a default generic `FormModel` based on a key-value store that is usually sufficient for
  most use cases. However, if you need more control over how the form data is retrieved and stored, you can provide your
  custom implementation (via `FormActivity#setModel`).

#### Add some fields grouped by section

    public class RegistrationForm extends FormActivity {
        @Override protected void initForm() {
            setTitle("Register Account");

            FormSectionController section1 = new FormSectionController(this, "Personal Info");
            section1.addElement(new EditTextController(this, "firstName", "First name"));
            section1.addElement(new EditTextController(this, "lastName", "Last name"));
            getFormController().addSection(section1);

            FormSectionController section2 = new FormSectionController(this, "Account");
            section2.addElement(new EditTextController(this, "username", "Username"));
            section2.addElement(new EditTextController(this, "password", "Password") {{
                setSecureEntry(true);
            }});
            getFormController().addSection(section2);
        }
    }

#### Initialize fields to certain values when the form is first displayed

    @Override protected void initForm() {
        // form setup
        // ...

        getModel().setValue("firstName", "John");
        getModel().setValue("lastName", "Smith");
    }

#### To retrieve the current field value at any time:

    getModel().getValue("firstName");

#### Listen to changes in fields:

    getModel().addPropertyChangeListener("firstName", new PropertyChangeListener() {
        @Override public void propertyChange(PropertyChangeEvent event) {
            LOG.i("tag", "Value was: " + event.getOldValue() + ", now: " + event.getNewValue());
        }
    });

Please browse through the samples included with the project for examples on how NexusDialog can be used.

Documentation
-------------
See the current [Javadoc](http://dkharrat.github.io/NexusDialog/javadoc/).

Styling
-------
TODO

Adding Custom Elements
----------------------
If the built-in form controls provided by NexusDialog don't meet your needs, you can easily extend NexusDialog to
provide custom form elements. The common parent class for all form elements is `FormElementController`. Among other
things, `FormElementController` tells NexusDialog how to construct the view to display in the form.

Typically your custom element falls under one of these cases:

1. Your custom element needs to show a label before a custom field: in this case, consider inheriting from
   `LabeledFieldController` which can provide the label functionality for you.

2. Your custom element needs full customization for how it's displayed: in this case, inherit from
   `FormElementController` and implement the `createView` method to tell NexusDialog how to create the custom view.

Browse through the _catalog_ sample for an example of implementing a custom element, or go over the code for the
built-in form elements to get an idea how they work.

Planned Features
----------------
The framework is constantly being improved and new features are being implemented. The following improvements are
planned:

* Support buttons
* Support sliders

Contributing
------------
Contributions via pull requests are welcome! For suggestions, feedback, or feature requests, please submit an issue.

Author
------
Dia Kharrat - dkharrat@gmail.com<br/>
Twitter: [http://twitter.com/dkharrat](http://twitter.com/dkharrat)

License
-------
    Copyright 2013 Dia Kharrat

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

