package com.heeyjinny.cameraandgallery

import android.Manifest
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.heeyjinny.cameraandgallery.databinding.ActivityMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    //뷰바인딩
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    //2
    //권한 처리를 위한 런처 미리 선언하기
    //카메라 및 촬영한 사진을 저장할 외부 저장소의 권한을 요청하는 코드 작성
    //런처와 관련된 변수 3개 선언
    //실제 촬영 이미지를 외부저장소에 저장하기 때문에
    //카메라 권한과 저장소 권한 처리 런처를 저장해둘 변수 2개 선언하고 카메라 요청 런처 선언

    //2-1
    //카메라 권한: 런처의 컨트랙트로 RequestPermission 사용하기 때문에 제네릭 문자 String 지정
    lateinit var cameraPermission: ActivityResultLauncher<String>
    //2-2
    //저장소 권한: 런처의 컨트랙트로 RequestPermission 사용하기 때문에 제네릭 문자 String 지정
    lateinit var storagePermission: ActivityResultLauncher<String>
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

        //3-1
        //카메라 권한 변수 초기화
        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){

            //5
            //카메라 권한처리 런처 코드 작성
            //권한요청이 정상적으로 승인(true)되었다면 openCamera()메서드 호출해 카메라 실행
            if (it) {
                openCamera()
            }else{
                Toast.makeText(baseContext, "카메라 권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }

        }

        //3-2
        //저장소 권한 변수 초기화
        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){

            //5-1
            //외부 저장소 권한처리 런처코드 작성
            //권한요청이 정상적으로 승인(true)되었다면 setView()메서드 호출해 카메라 권한요청 실행
            if (it){
                setViews()
            }else{
                Toast.makeText(baseContext, "외부 저장소 권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

        }

        //6
        //기본코드 작성 끝... 하지만 실행 시 권한오류가 발생함
        //이는 카메라에서 외부저장소에 촬영 결과물을 저장하고 결과물의 Uri를 사용하기 위해서는
        //부가적으로 FileProvider를 통한 권한 처리가 필요함
        //FileProvider설정을 위한 xml생성하여 작성...
        //res - xml폴더 작성 - file_paths.xml 파일 생성

        //3-3
        //카메라 앱 호출런처 초기화
        //실제 카메라의 촬영 결과물을 주고받는 용도로 사용되는 TakePicture()사용
        //런처의 결과값으로 사진 촬영이 정상일경우 true, 오류가 있을경우 false가 넘어옴
        //isSuccess 별칭 사용하여 알아보기 쉽게 변경
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            isSuccess ->

            //3-4
            //TakePicture()의 결과값은 Boolean타입이어서 실제 사진정보는 얻을 수 없음
            //그래서 사진 정보를 담을 변수 프로퍼티 미리 선언해야함 onCreate()메서드 위에 전역변수로 생성

            //3-5
            //사진 촬영이 정상(isSucces)일 경우 사진정보를 가지고 있는 변수 photoUri를 화면에 세팅
            if (isSuccess){
                binding.imagePreview.setImageURI(photoUri)
            }

            storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }//cameraLauncher


    }//onCreate

    //3
    //카메라 앱 권한 호출
    //외부저장소 권한 승인이 되었을 때 호출할 setViews()메서드 생성하여
    //카메라 버튼 클릭 시 카메라 권한을 요청하는 코드 작성
    //미리 선언했던 변수 cameraPermission를 런치하여 프로퍼티에 카메라 권한을 담아 호출
    fun setViews(){
        binding.buttonCamera.setOnClickListener {

            Toast.makeText(this,"rrr", Toast.LENGTH_SHORT).show()
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }//setView

    //4
    //카메라 요청하는 메서드 작성(openCamera())
    fun openCamera(){

        //4-1
        //사진 촬영 후 저장할 임시파일 생성후 변수에 담아둠
        //임시파일 prefix: IMG_, suFFix: .jpg
        //파일의 이름 앞 뒤로 고정이고 중간은 자동랜덤생성됨: ex) /tmp/IMG_1224234243.jpg
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",

            //4-2
            //앱 내에만 있는 사용자에게 가치를 제공하는 미디어 파일과 앱이 호환되면 다음 코드 스니펫과 같이
            //외부 저장소 내 앱별 디렉터리에 미디어 파일을 저장하는 것이 가장 좋음
            //DIRECTORY_PICTURES같은 API 상수에서 제공하는 디렉터리 이름을 사용
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        //4-3
        //앞에서 생성한 파일의 Uri생성하여 실제 사진 정보를 정장하는 변수 photoUri에 저장
        photoUri = FileProvider.getUriForFile(this, "${packageName}.provider", photoFile)

        //4-4
        //카메라 앱을 호출하는 cameraLauncher에 런치launch()하여
        //실제 사진정보를 가지고 있는 변수 photoUri 전달해 카메라 호출
        cameraLauncher.launch(photoUri)

    }//openCamera()



}//MainActivity