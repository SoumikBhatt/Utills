package com.soumik.utilitieslibrary

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener { share(this,"Share with your friends & family","Hey Download this app!!, Its have some really cool feature","Share Via") }
        btn2.setOnClickListener { feedback(this,"demo@demo.com","Feedback for ${getString(R.string.app_name)}","Please add like feature") }
        btn3.setOnClickListener { rateApp(this) }
        btn4.setOnClickListener { likeOnFB(this,826057894180419.toString(),"itmedicus") }
    }

    fun share(context: Context, subject:String, body:String, chooserTitle:String){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        shareIntent.putExtra(Intent.EXTRA_TEXT, body)
        context.startActivity(Intent.createChooser(shareIntent, chooserTitle))
    }

    fun feedback(context: Context, email:String, subject: String, body:String){
        val installed = appInstalledOrNot(context,"com.google.android.gm")
        if (installed) {
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.type = "text/html"
            val pm = context.packageManager
            val matches = pm.queryIntentActivities(emailIntent, 0)
            var className: String? = null
            for (info in matches) {
                if (info.activityInfo.packageName == "com.google.android.gm") {
                    className = info.activityInfo.name
                    if (className != null && className.isNotEmpty()) {
                        break
                    }
                }
            }
            emailIntent.setClassName("com.google.android.gm", className!!)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, body)
            context.startActivity(emailIntent)
        } else {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                val data = Uri.parse("mailto:$email?subject=$subject")
                intent.data = data
                intent.putExtra(Intent.EXTRA_TEXT,body)
                context.startActivity(intent)

            } catch (e: Exception) {
                e.printStackTrace()
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https:/mail.google.com")))
            }
        }
    }

    fun rateApp(context: Context){
        val appId = context.packageName
        val rateIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("market://details?id=$appId")
        )
        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager
            .queryIntentActivities(rateIntent, 0)
        for (otherApp in otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {

                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name
                )
                // make sure it does NOT open in the stack of your activity
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                // task reparenting if needed
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                // if the Google Play was already open in a search result
                //  this make sure it still go to the app page you requested
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                // this make sure only the Google Play app is allowed to
                // intercept the intent
                rateIntent.component = componentName
                context.startActivity(rateIntent)
                marketFound = true
                break

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$appId")
            )
            context.startActivity(webIntent)
        }
    }

    fun likeOnFB(context: Context, pageID:String, pageUserName:String){
        return try {
            context.packageManager.getPackageInfo("com.facebook.katana", 0)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/$pageID"))
            return  context.startActivity(intent)

        } catch (e: Exception) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/$pageUserName")))
        }
    }

    private fun appInstalledOrNot(context: Context, uri: String): Boolean {
        val pm = context.packageManager
        val appInstalled: Boolean
        appInstalled = try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return appInstalled
    }
}
