package com.heeyjinny.cameraandgallery

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.heeyjinny.cameraandgallery.databinding.ActivityMainBinding
import java.io.File
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //2
    //런처와 관련된 변수 3개 선언
    //실제 촬영 이미지를 외부저장소에 저장하기 때문에
    //카메라 권한과 저장소 권한 처리 런처를 저장해둘 변수 2개 선언하고 카메라 요청 런처 선언

    //2-1
    //카메라 권한: 런처의 컨트랙트로 RequestPermission 사용하기 때문에 제네릭 문자 String 지정
    lateinit var cameraPermission: ActivityResultLauncher<Array<String>>
    //2-2
    //저장소 권한: 런처의 컨트랙트로 RequestPermission 사용하기 때문에 제네릭 문자 String 지정
    lateinit var storagePermission: ActivityResultLauncher<Array<String>>
    //2-3
    //카메라 앱 호출 런처: TakePicture를 사용하기 따문에 제네릭 Uri 지정
    lateinit var cameraLauncher: ActivityResultLauncher<Uri>

    //3-4
    //cameraLauncher가 실행되고 TakePicture()의 실제 사진정보를 저장할 변수생성
    var photoUri: Uri? = null

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


        //3
        //카메라 권한과 저장소 권한 초기화
//        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
//
//        }

//        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
//
//        }

        //3-3
        //실제 카메라의 촬영 결과물을 주고받는 용도로 사용되는 TakePicture()사용
        //런처의 결과값으로 사진 촬영이 정상일경우 true, 오류가 있을경우 false가 넘어옴
        //isSuccess 별칭 사용하여 알아보기 쉽게 변경
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            isSucces ->

            //3-4
            //TakePicture()의 결과값은 Boolean타입이어서 실제 사진정보는 얻을 수 없음
            //그래서 사진 정보를 담을 변수 프로퍼티 미리 선언해야함 onCreate()메서드 위에 전역변수로 생성

            //3-5
            //사진 촬영이 정상(isSucces)일 경우 사진정보를 가지고 있는 변수 photoUri를 화면에 세팅
            if (isSucces){
                binding.imagePreview.setImageURI(photoUri)
            }

        }//cameraLauncher


    }//onCreate

    //3
    //외부저장소 권한 승인이 되었을 때 호출할 setViews()메서드 생성하여
    //카메라 버튼 클릭 시 카메라 권한을 요청하는 코드 작성
    //미리 선언했던 변수 cameraPermission를 런치하여 프로퍼티에 카메라 권한을 담아 호출
    fun setViews(){
        binding.buttonCamera.setOnClickListener {
            cameraPermission.launch(arrayOf(android.Manifest.permission.CAMERA))
        }
    }//setView

    //카메라 요청하는 메서드
    //먼저 사진 촬영 후 저장할 임시파일 생성후 변수에 담아둠
    fun openCamera(){
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        //앞에서 생성한 파일의 Uri생성 후 photoUri에 담고 launch()메서드에 전달해 카메라 호출

한
    }



}//MainActivity