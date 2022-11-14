package com.stoe.mp3player

import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.Directory
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    val MEDIA_PATH: String = Environment.getExternalStorageDirectory().path + "/"
    var songList: ArrayList<String> = ArrayList()
    lateinit var adapter: MusicAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("media path", MEDIA_PATH)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //daca nu a acceptat permission, il cerem
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {  //ne vedem de ale noastre
            getAllAudioFiles()
        }

    }

//    private fun getAllAudioFiles(){
//        if(MEDIA_PATH != null){
//            val mainFile: File = File(MEDIA_PATH)
//            val fileList: Array<File> = mainFile.listFiles()
//            for (file: File in fileList){
//                Log.e("Media path", file.toString())
//
//                if(file.isDirectory){
//                    scanDirectory(file)
//                } else {
//                    var path: String = file.absolutePath
//                    if(path.endsWith(".mp3")){
//                        songList.add(path)
//                        adapter.notifyDataSetChanged()
//                    }
//                }
//            }
//        }
//
//        adapter = MusicAdapter(songList, this)
//        recyclerView.adapter = adapter
//    }

    fun getAllAudioFiles(){
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val stringPath = arrayOf(MediaStore.Audio.Media.DATA)
        var cursor = contentResolver.query(uri, stringPath, null, null, null)

        if(cursor == null){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
        } else if (!cursor.moveToFirst()){
            Toast.makeText(this, "No music", Toast.LENGTH_SHORT).show()
        } else {
            var path: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            do {
                var st: String = cursor.getString(path)
                Log.e("mainpath", st)
                songList.add(st)
            } while (cursor.moveToNext())
        }
        adapter = MusicAdapter(songList, this)
        recyclerView.adapter = adapter
    }

//    private fun scanDirectory(directory: File?){
//        if (directory!!.isDirectory) {
//
//            if (directory != null) {
//                val fileList: Array<File> = directory.listFiles()
//                for (file: File in fileList) {
//                    Log.e("Media path", file.toString())
//
//                    if (file.isDirectory) {
//                        scanDirectory(file)
//                    } else {
//                        var path: String = file.absolutePath
//                        if (path.endsWith(".mp3")) {
//                            songList.add(path)
//                        }
//                    }
//                }
//            } else {
//                return
//            }
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getAllAudioFiles()
        }
    }
}