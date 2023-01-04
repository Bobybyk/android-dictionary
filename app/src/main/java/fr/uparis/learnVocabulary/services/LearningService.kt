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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

class LearningService : Service() {

    private lateinit var sharedPref : SharedPreferences
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val CHANNELID = "NewWord"

    private val dao by lazy { (application as LearnVocabularyApplication).database.getDAO() }

    private var currentWord : Word? = null
    private var sessionsPerDay : Int = 0
    private var wordsPerSession : Int = 0
    private var trainingStartHour : Int = 0
    private var trainingStartMinutes : Int = 0
    private var trainingStopHour : Int = 0
    private var trainingStopMinutes : Int = 0

    private var currentSessionRemainingWords : Int = 0
    private var sessionsRemainingToday : Int = 0

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        sharedPref = getSharedPreferences(getString(R.string.shared_preferences_name),Context.MODE_PRIVATE)
        sessionsPerDay = sharedPref.getInt(getString(R.string.number_of_sessions_per_day), 1)
        wordsPerSession = sharedPref.getInt(getString(R.string.number_of_words_per_session), 10)

        trainingStartHour = sharedPref.getInt(getString(R.string.training_start_hour), 8)
        trainingStartMinutes = sharedPref.getInt(getString(R.string.training_start_minutes), 0)
        trainingStopHour = sharedPref.getInt(getString(R.string.training_stop_hour), 8)
        trainingStopMinutes = sharedPref.getInt(getString(R.string.training_stop_minutes), 0)


        createNotificationChannel()

        when(intent?.action) {
            "start" -> {
                Log.d(null, "action test")
                sendNotification()
                currentSessionRemainingWords = wordsPerSession
                sessionsRemainingToday = sessionsPerDay
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
                    val dict = dao.loadDictionaryParams(currentWord!!.sourceLanguage, currentWord!!.destinationLanguage).first { it.favoriteDictionary }
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

        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun setAlarm() {
        val intent = Intent(this, LearningService::class.java).apply {
            action = "wakeup"
        }
        val pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        var nextWordTime = 5

        if(currentSessionRemainingWords > 0 && sessionsRemainingToday > 0) {
            currentSessionRemainingWords--

            Log.d(null, "cas 1 - $currentSessionRemainingWords words remaining")
        } else if(currentSessionRemainingWords > 0 && sessionsRemainingToday == 0) {

            currentSessionRemainingWords--

            Log.d(null, "cas 2 - ${nextWordTime}sec before next word")

        } else if(currentSessionRemainingWords == 0 && sessionsRemainingToday > 0){
            currentSessionRemainingWords = wordsPerSession

            //calculer temps pour plus tard
            val sdf = SimpleDateFormat("HH", Locale.FRANCE)
            val hoursNow = sdf.format(Date()).toInt()

            val sdf2 = SimpleDateFormat("mm", Locale.FRANCE)
            val minutesNow = sdf2.format(Date()).toInt()

            var minutesDiff = 0
            if(minutesNow > trainingStopMinutes) {
                trainingStopHour--
                trainingStopMinutes += 60
            }

            minutesDiff = trainingStopMinutes - minutesNow
            val hoursDiff = trainingStopHour - hoursNow

            minutesDiff += hoursDiff * 60

            nextWordTime = ( minutesDiff / sessionsRemainingToday ) * 60

            sessionsRemainingToday--

            Log.d(null, "cas 3 - next word in ${nextWordTime / 60 }")

        } else {

            //calculer temps le lendemain
            val sdf = SimpleDateFormat("HH", Locale.FRANCE)
            val hoursNow = sdf.format(Date()).toInt()

            val sdf2 = SimpleDateFormat("mm", Locale.FRANCE)
            val minutesNow = sdf2.format(Date()).toInt()

            var minutesDiff = 0
            if(trainingStartMinutes > minutesNow) {
                trainingStartHour--
                trainingStartMinutes += 60
            }

            minutesDiff = minutesNow - trainingStartMinutes
            val hoursDiff = hoursNow - trainingStartHour

            minutesDiff += hoursDiff * 60

            nextWordTime = (24 * 60) - minutesDiff

            Log.d(null, "cas 4 see you tomorrow")
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + nextWordTime  * 1000,
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