package com.heeyjinny.cameraandgallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.heeyjinny.cameraandgallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //1
        //카메라 UI화면 만들고 권한 요청하기

        //1-1
        //activity_main.xml 화면 수정 및 배치

        //1-2
        //카메라 권한과 촬영한 사진에 대한 접근권한 선언
        //app - manifests - AndroidManifest.xml


    }//onCreate
}//MainActivity