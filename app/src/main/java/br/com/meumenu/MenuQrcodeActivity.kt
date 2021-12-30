package br.com.meumenu

import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.core.graphics.set
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.encoder.QRCode

class MenuQrcodeActivity : AppCompatActivity() {

    private lateinit var ivQRCode: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_qrcode)

        ivQRCode = findViewById(R.id.ivQrcode)

        val writer = QRCodeWriter()
        try{
            val bitMatrix = writer.encode("manda salve",BarcodeFormat.QR_CODE, 512,512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
            for (x in 0 until width){
                for (y in 0 until height){
                    bmp.setPixel(x,y, if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
                }
            }
            ivQRCode.setImageBitmap(bmp)
        }catch (e: WriterException){
            e.printStackTrace()
        }

    }
}