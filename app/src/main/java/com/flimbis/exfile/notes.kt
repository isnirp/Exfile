package com.flimbis.exfile


/*
* ANDROID X
* https://material.io/develop/android/docs/getting-started/
* */

/*
* FRAGMENT
*
* https://www.netguru.com/codestories/whats-new-in-fragments
* */

/*
* RECYCLERVIEW
*
* https://developer.android.com/guide/topics/ui/layout/recyclerview
* */

/*
* FILE PROVIDER
*
* https://developer.android.com/training/secure-file-sharing/retrieve-info
* https://developer.android.com/reference/android/support/v4/content/FileProvider
* https://www.codevoila.com/post/46/android-tutorial-android-external-storage
* https://developer.android.com/reference/android/os/Environment
* https://stackoverflow.com/questions/10202805/how-do-i-get-file-size-of-temp-file-in-android
* */
/*
* #FileProvider is a special subclass of ContentProvider
* #FileProvider generates contentURI for files
* #A content URI allows you to grant read and write access using temporary access permissions.
* #FileProvider can only generate a content URI for files in directories that you specify beforehand in an xml
*
* */
/*
FileProvider is a special subclass of ContentProvider
 facilitates secure sharing of files associated with an app by creating a content:// Uri for a file instead of a file:/// Uri.
A content URI allows you to grant read and write access using temporary access permissions.
Environment.getExternalStorageDirectory(); returns top-level directory of the primary external storage.
Context.getExternalFilesDir(String type); Returns the absolute path to the directory on the primary shared or external storage device
 where the application can place persistent files it owns.
type can be; Environment.DIRECTORY_MUSIC, Environment.DIRECTORY_PICTURES
Environment.getExternalStoragePublicDirectory(String type); returns a top-level shared or external storage directory for placing files of a particular type.
*/

/*
* JOB SCHEDULER
*
* http://midhunhk.github.io/dev/2018/08/05/content-observer-service/
*https://developer.android.com/reference/android/app/job/JobInfo.Builder
* https://developer.android.com/reference/android/app/job/JobInfo.Builder.html?utm_campaign=adp_series_job_scheduler_092216&utm_source=medium&utm_medium=blog#addTriggerContentUri(android.app.job.JobInfo.TriggerContentUri)
* */

/*
* INTENT
* An intent is a messenger object that requests an action from another app component
* properties; component name, action, data(data and mime type), category, extras, flag
* https://stackoverflow.com/questions/6265298/action-view-intent-for-a-file-with-unknown-mimetype
* */


/*
* DATA BINDING
* */
//binds UI to data object
//allows you to make objects, fields observable
/*
* https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4
* https://www.vogella.com/tutorials/AndroidDatabinding/article.html
* https://medium.com/androiddevelopers/android-data-binding-recyclerview-db7c40d9f0e4
* */


/*
* MVVM
* https://www.wintellect.com/model-view-viewmodel-mvvm-explained/
* https://blog.jeremylikness.com/blog/model-view-viewmodel-mvvm-explained/
* https://blogs.msdn.microsoft.com/johngossman/2005/10/08/introduction-to-modelviewviewmodel-pattern-for-building-wpf-apps/
*
*
* https://www.youtube.com/watch?v=v4XO_y3RErI
* https://www.youtube.com/watch?v=EukoLrBLTjE
* https://www.zoftino.com/android-recyclerview-data-binding-example
* https://stfalcon.com/en/blog/post/android-mvvm
* https://www.androidhive.info/android-working-with-databinding/
* https://www.zoftino.com/model-view-viewmodel-mvvm-android-example
* https://android.jlelse.eu/android-mvvm-with-dagger-2-retrofit-rxjava-architecture-components-6f5da1a75135
* https://stackoverflow.com/questions/50270542/what-is-the-difference-between-viewmodel-that-extends-baseobservable-and-android
* */
//The view model may expose the model directly, or properties related to the model, for data-binding
//The view model can contain interfaces to services, configuration data, etc in order to fetch and manipulate the properties it exposes to the view

/*
* ENUM
* https://www.baeldung.com/kotlin-enum
*
* */


/*
* BOTTOM SHEET BEHAVIOUR
*
* The Persistent bottom sheet(BottomSheetBehavior) displays in-app content
* The modal bottom sheets(BottomSheetDialogFragment) used to show deep-linked content from other apps
* */

/*
* CLIP BOARD
*
* https://developer.android.com/guide/topics/text/copy-paste
* https://developer.android.com/reference/android/content/ClipData.html#newPlainText%28java.lang.CharSequence,%20java.lang.CharSequence%29
* */

/*
* BUBBLES
*
* https://developer.android.com/guide/topics/ui/bubbles
* */
