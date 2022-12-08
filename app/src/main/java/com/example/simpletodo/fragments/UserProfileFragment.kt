package com.example.simpletodo.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.parse.ParseUser
import com.parse.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.*
import com.example.simpletodo.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 * Use the [UserProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserProfileFragment : Fragment() {

    val PICK_PHOTO_CODE = 1046

    lateinit var profilePic: ImageView
    lateinit var username: TextView
    lateinit var bio: EditText
    lateinit var averageSpeed: TextView
    lateinit var averageAcc: TextView
    lateinit var totalPoints: TextView
    lateinit var submitButton: Button
    lateinit var user : ParseUser
    lateinit var GameLogRecyclerView: RecyclerView
    lateinit var adapter: GameLogAdapter
    var allGameLogs: MutableList<GameResults> = mutableListOf()
    var photoFile: File?= null


    val photoFileName = "pfp.jpg"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<ImageButton>(R.id.settingsButton)
            .setOnClickListener(View.OnClickListener {
                val intent = Intent(context, SettingsActivity::class.java)
                context?.startActivity(intent)
            })

        super.onViewCreated(view, savedInstanceState)
        user = ParseUser.getCurrentUser()

        profilePic = view.findViewById(R.id.profilePic)
        username = view.findViewById(R.id.username)
        bio = view.findViewById(R.id.bio)
        totalPoints = view.findViewById(R.id.userPointsTotal)
        averageSpeed = view.findViewById(R.id.userSpeed)
        averageAcc = view.findViewById(R.id.userAcc)
        GameLogRecyclerView = view.findViewById(R.id.gamedetails)
        adapter = GameLogAdapter(requireContext(),allGameLogs)
        GameLogRecyclerView.adapter = adapter
        GameLogRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        Log.d(TAG, user.objectId)
        profiledetails()
        queryGameLogs()


        view.findViewById<Button>(R.id.submit).setOnClickListener {

            Toast.makeText(requireContext(), "bio" + bio.text, Toast.LENGTH_SHORT).show()
            val query = ParseQuery.getQuery(Player::class.java)
            query.include(Player.KEY_USER)
            query.whereEqualTo(Player.KEY_USER, user)
            query.findInBackground { detail, e ->
                if (e == null) {
                    Log.d(TAG, "Objects: $detail")
                    for (element in detail) {
                        element.setBio(bio.text.toString())
                        if(photoFile!=null){
                            element.setPImage(ParseFile(photoFile))
                        }
                        element.saveInBackground { exception ->
                            if (exception != null) {
                                Log.e(TAG, "Error while saving changes")
                                Toast.makeText(requireContext(), "Error: Something went wrong trying to save your profile changes!", Toast.LENGTH_SHORT).show()
                            } else {
                                Log.i(TAG, "Successfully saved changes")
                                Toast.makeText(requireContext(), "Profile Update!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        profilePic.setOnClickListener {
            onPickPhoto()
        }
    }

    fun profiledetails() {
        val query = ParseQuery.getQuery(Player::class.java)
        query.include(Player.KEY_USER)
        query.whereEqualTo(Player.KEY_USER, user)
        query.findInBackground { detail , e ->
            if (e == null) {
                Log.d(TAG, "Objects: $detail")
                for (element in detail) {
                    Log.i(TAG, "this is the bio" + element.getBio().toString())
                    element.getBio()

                    username.setText(user.username.toString())
                    Log.i(TAG, getContext().toString())
                    if (element.getPImage() != null) {
                        activity?.let {
                            Glide.with(it)
                                .load(element.getPImage()?.url)
                                .transform(CircleCrop())
                                .into(profilePic)
                        }
                    } else {
                        activity?.let {
                            Glide.with(it)
                                .load(R.drawable.instagram_user_filled_24)
                                .transform(CircleCrop())
                                .into(profilePic)
                        }
                    }
                    bio.setText(element.getBio()).toString()
                    totalPoints.setText(element.getTotal().toString())
                    averageAcc.setText(element.getAccuracy().toString()+"%")
                    averageSpeed.setText(element.getSpeed().toString()+" WPM")


                }
            } else {
                Log.e(TAG, "Parse Error: ", e)
            }
        }
        Log.e(TAG, "This works")

    }

    fun queryGameLogs(){
        val query = ParseQuery.getQuery(GameResults::class.java)
        query.include(GameResults.KEY_USER)
        query.whereEqualTo(GameResults.KEY_USER, user)
        query.findInBackground{ detail, e ->
            if (e == null) {
                Log.d(TAG, "Objects: $detail")
                for (element in detail) {
//                    Log.i(TAG, "this is the bio" + element.getDate().toString())

                    allGameLogs.addAll(detail)
                    adapter.notifyDataSetChanged()
                }
            }else {
                        Log.i("Fragment:","GameLog is null")
                    }
                }


        }



    fun loadFromUri(photoUri: Uri?): Bitmap? {
        var image: Bitmap? = null
        try {
            // check version of Android on device
            if (Build.VERSION.SDK_INT > 27 && photoUri != null) {
                // on newer versions of Android, use the new decodeBitmap method
                val source: ImageDecoder.Source =
                    ImageDecoder.createSource(requireContext().getContentResolver(), photoUri)
                image = ImageDecoder.decodeBitmap(source)
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), photoUri)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun onPickPhoto() {
        // Create intent for picking a photo from the gallery
        val intent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(requireContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (data != null && requestCode == PICK_PHOTO_CODE) {
            val photoUri: Uri? = data.data

            // Load the image located at photoUri into selectedImage
            val selectedImage = loadFromUri(photoUri)

            // See BitmapScaler.java: https://gist.github.com/nesquena/3885707fd3773c09f1bb
            val resizedBitmap = BitmapScaler.scaleToFitWidth(selectedImage!!, profilePic.measuredWidth)
            // Configure byte output stream
            val bytes = ByteArrayOutputStream()
            // Compress the image further
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
            // Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
            val resizedFile = getPhotoFileUri(photoFileName + "_resized")
            resizedFile.createNewFile()
            photoFile = resizedFile
            val fos = FileOutputStream(resizedFile)
            // Write the bytes of the bitmap to file
            fos.write(bytes.toByteArray())
            fos.close()

            if (photoUri != null) {
                user.put(KEY_PFP, ParseFile(resizedFile))
                Log.i(TAG, "Photo success ${photoUri.path}")
            }

            user.saveInBackground { e ->
                if (e == null) {
                    //Save successfull
                    Toast.makeText(requireContext(), "Profile picture updated!", Toast.LENGTH_SHORT).show()
                    Glide.with(requireContext())
                        .load(user.getParseFile(KEY_PFP)?.url)
                        .transform(CircleCrop())
                        .into(profilePic)
                } else {
                    // Something went wrong while saving
                    e.printStackTrace()
                    Toast.makeText(requireContext(), "Error: Something went wrong trying to save your profile image!", Toast.LENGTH_SHORT).show()
                }
            }

//            // Load the selected image into a preview
//            ivProfilePic.setImageBitmap(BitmapFactory.decodeFile(resizedFile!!.absolutePath))
        }
    }

    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                UserProfileFragment.TAG
            )

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(UserProfileFragment.TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }

    companion object {
            const val KEY_PFP = "profilePic"
        private const val TAG = "ProfileFragment"
    }
}