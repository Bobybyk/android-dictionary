package fr.uparis.learnVocabulary.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.SharedPreferences
import android.net.Uri
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import fr.uparis.learnVocabulary.LearnVocabularyApplication
import fr.uparis.learnVocabulary.R
import fr.uparis.learnVocabulary.activities.MainActivity
import fr.uparis.learnVocabulary.database.entities.Word
import fr.uparis.learnVocabulary.viewModels.MainViewModel
import kotlin.concurrent.thread
import kotlin.random.Random

class LearningService : Service() {

    private lateinit var sharedPref : SharedPreferences
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val CHANNELID = "NewWord"

    private val dao by lazy { (application as LearnVocabularyApplication).database.getDAO() }

    private var currentWord : Word? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sharedPref = getSharedPreferences(getString(R.string.shared_preferences_name),Context.MODE_PRIVATE)
        var sessionsPerDay = sharedPref.getInt(getString(R.string.number_of_sessions_per_day), 1).toString()
        var wordsPerSession = sharedPref.getInt(getString(R.string.number_of_words_per_session), 10).toString()

        createNotificationChannel()

        when(intent?.action) {
            "start" -> {
                Log.d(null, "action test")
                sendNotification()
            }
            "wakeup" -> {
                Log.d(null, "wakeup")
                sendNotification()
            }
            "notificationClosed" -> {
                Log.d(null, "Word known")
                //update word in database
                currentWord!!.timesRemembered++
                thread {
                    dao.updateWord(currentWord!!)
                    setAlarm()
                }
            }
            "notificationButtonClicked" -> {
                Log.d(null, "Word refreshed")

                thread {
                    val dict = dao.loadDictionaryParams(currentWord!!.sourceLanguage, currentWord!!.destinationLanguage).first()
                    val webPage : Uri = Uri.parse( "${dict.url}/${currentWord!!.word}")
                    val browserIntent = Intent(Intent.ACTION_VIEW, webPage)
                    browserIntent.flags = FLAG_ACTIVITY_NEW_TASK
                    startActivity(browserIntent)

                    notificationManager.cancel(15)

                    setAlarm()
                }
            }
            null -> {
                setAlarm()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun setAlarm() {
        val intent = Intent(this, LearningService::class.java).apply {
            action = "wakeup"
        }
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 5 * 1000,
            pendingIntent
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNELID,
            "private channel",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = "private channel" }
        notificationManager.createNotificationChannel(channel)
    }

    private fun sendNotification() {

        //Intent to send when the user dismisses the notification
        val closeIntent = Intent(this, LearningService::class.java).apply {
            action = "notificationClosed"
        }
        val closePendingIntent = PendingIntent.getService(this, 0, closeIntent, PendingIntent.FLAG_IMMUTABLE)

        //Intent to send when the user click on the notification
        val clickIntent = Intent(this, MainActivity::class.java)
        val clickPendingIntent = PendingIntent.getActivity(this, 0, clickIntent,PendingIntent.FLAG_IMMUTABLE)

        val buttonIntent = Intent(this, LearningService::class.java).apply {
            action = "notificationButtonClicked"
        }
        val pendingButtonIntent = PendingIntent.getService(this, 0, buttonIntent, PendingIntent.FLAG_IMMUTABLE)

        thread { //creating a thread to allow simili-synchronous db query
            randomWord()

            if(currentWord != null) {

                val notification = NotificationCompat.Builder(this, CHANNELID)
                    .setContentTitle("${currentWord!!.word}")
                    .setContentText("en ${currentWord!!.destinationLanguage} ?")
                    .setDeleteIntent(closePendingIntent)
                    .setContentIntent(clickPendingIntent)
                    .addAction(
                        R.drawable.ic_launcher_background,
                        "Voir traduction",
                        pendingButtonIntent
                    )
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText("Connaissez-vous la traduction de ce mot ${currentWord!!.sourceLanguage} en ${currentWord!!.destinationLanguage} ?")
                    )
                    .build()

                notificationManager.notify(15, notification)

                Log.d(null, "$currentWord")

            }
        }
    }

    private fun randomWord() {
        val list = dao.loadAllWords()
        if(list.isEmpty())
            return
        val randomIndex = Random.nextInt(list.size)
        currentWord = list[randomIndex]
    }
}