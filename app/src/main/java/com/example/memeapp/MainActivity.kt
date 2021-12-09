package com.example.memeapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {
    var url: String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadmeme()
        next.setOnClickListener{
            loadmeme()
        }
        share.setOnClickListener{
            val image:Bitmap?=getBitmap(imageView)
            val share=Intent(Intent.ACTION_SEND)
            share.type="Image/*"
            share.putExtra(Intent.EXTRA_STREAM,getImgurl(this,image!!))
            startActivity(Intent.createChooser(share,"Share via"))
        }
    }

    private fun getBitmap(View:ImageView):Bitmap?{
        val bitmap=Bitmap.createBitmap(View.width,View.height,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        View.draw(canvas)
        return bitmap
    }
    private fun getImgurl(inContext: Context,inImage:Bitmap):Uri?{
        val bytes=ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG,100,bytes)
        val path= MediaStore.Images.Media.insertImage(inContext.contentResolver,inImage,"Title",null)
        return Uri.parse(path)
    }

    private fun loadmeme(){     //VOLLEY LIBRARTY
        // Instantiate the RequestQueue.
        progressBar2.visibility= View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        url = "https://meme-api.herokuapp.com/gimme"

        // Request a JSON response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                val url=response.getString("url")
                Glide.with(this).load(url).listener(object :RequestListener<Drawable>{

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar2.visibility= View.GONE
                        return false
                    }
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar2.visibility= View.GONE
                        return false
                    }

                })
                     .into(imageView)
            },
            Response.ErrorListener { error ->
            }
        )
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest)
    }

}