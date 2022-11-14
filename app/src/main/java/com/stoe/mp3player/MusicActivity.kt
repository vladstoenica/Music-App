package com.stoe.mp3player

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.io.IOException
import java.text.FieldPosition

class MusicActivity : AppCompatActivity() {

//    lateinit var title: String
//    var position: Int = 0
//    var list: ArrayList<String> = ArrayList()

    lateinit var mediaPlayer: MediaPlayer

    lateinit var runnable: Runnable
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)

        val buttonPlayPause: Button = findViewById(R.id.buttonPause)
        val buttonNext: Button = findViewById(R.id.buttonNext)
        val buttonPrevious: Button = findViewById(R.id.buttonPrevious)
        val textViewFilenameMusic: TextView = findViewById(R.id.text1)
        val textViewProgress: TextView = findViewById(R.id.textViewProgress)
        val textViewTotalTime: TextView = findViewById(R.id.textViewTotalTime)
        val seekBarMusic: SeekBar = findViewById(R.id.musicSeekBar)
        val seekBarVolume: SeekBar = findViewById(R.id.volumeSeekBar)

        val title: String = intent.getStringExtra("title").toString()
        val filePath: String = intent.getStringExtra("filepath").toString()
        Log.e("path to play ", filePath)
        Log.e("title to play ", title)

        var position: Int = intent.getIntExtra("position", 0)
        val list: ArrayList<String> = intent.getStringArrayListExtra("list")!!

        textViewFilenameMusic.text = title

        mediaPlayer = MediaPlayer()

        try {
            Log.e("checkif", filePath)
            mediaPlayer.setDataSource(filePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException){
            e.printStackTrace()
        }

        buttonPrevious.setOnClickListener{
            mediaPlayer.reset()
            if(position == 0){
                position = list.size - 1
            } else {
                position--
            }
            var newFilePath: String = list[position]
            try {
                mediaPlayer.setDataSource(newFilePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                buttonPlayPause.setBackgroundResource(R.drawable.ic_pause)

                val newTitle: String = newFilePath.substring(newFilePath.lastIndexOf("/") + 1)   //formatam numele fisierelor cum sa apara in recycler
                textViewFilenameMusic.text = newTitle

            } catch (e: IOException){
                e.printStackTrace()
            }
        }

        buttonNext.setOnClickListener{
            mediaPlayer.reset()
            if(position == list.size - 1){
                position = 0
            } else {
                position++
            }
            var newFilePath: String = list[position]
            try {
                mediaPlayer.setDataSource(newFilePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                buttonPlayPause.setBackgroundResource(R.drawable.ic_pause)

                val newTitle: String = newFilePath.substring(newFilePath.lastIndexOf("/") + 1)   //formatam numele fisierelor cum sa apara in recycler
                textViewFilenameMusic.text = newTitle

            } catch (e: IOException){
                e.printStackTrace()
            }
        }

        buttonPlayPause.setOnClickListener{
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                buttonPlayPause.setBackgroundResource(R.drawable.ic_play)
            } else {
                mediaPlayer.start()
                buttonPlayPause.setBackgroundResource(R.drawable.ic_pause)
            }
        }

        seekBarVolume.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarVolume.setProgress(progress)
                var volumeLevel: Float = progress / 100f;  //standard practice pt mai jos
                mediaPlayer.setVolume(volumeLevel, volumeLevel)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }
        })

        seekBarMusic.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer.seekTo(progress)
                    seekBarMusic.setProgress(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })

        handler = Handler()
        runnable = Runnable {
            var totalTime: Int = mediaPlayer.duration
            seekBarMusic.max = totalTime

            var currentPosition: Int = mediaPlayer.currentPosition
            seekBarMusic.progress = currentPosition
            handler.postDelayed(runnable, 1000)

            var elapsedTime: String = createTimeLabel(currentPosition)
            var lastTime: String = createTimeLabel(totalTime)

            textViewProgress.text = elapsedTime
            textViewTotalTime.text = lastTime

            if (elapsedTime.equals(lastTime)){  //a ajuns melodia la final, trecem la urmatoarea
                mediaPlayer.reset()
                if(position == list.size - 1){
                    position = 0
                } else {
                    position++
                }
                var newFilePath: String = list[position]
                try {
                    mediaPlayer.setDataSource(newFilePath)
                    mediaPlayer.prepare()
                    mediaPlayer.start()
                    buttonPlayPause.setBackgroundResource(R.drawable.ic_pause)

                    val newTitle: String = newFilePath.substring(newFilePath.lastIndexOf("/") + 1)   //formatam numele fisierelor cum sa apara in recycler
                    textViewFilenameMusic.text = newTitle

                } catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
        handler.post(runnable)
    }

    fun createTimeLabel(currentPossition: Int): String{
        //1 min = 60 sec , 1 sec = 1000 millisec
        var timeLabel: String
        var minute: Int = currentPossition / 1000 / 60
        var second: Int = currentPossition / 1000 % 60

        if(second < 10){
            timeLabel = "$minute:0$second"
        }else{
            timeLabel = "$minute:$second"
        }
        return timeLabel
    }
}