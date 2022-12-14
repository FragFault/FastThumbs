import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.PlayActivity
import com.example.simpletodo.Player
import com.example.simpletodo.R
import com.parse.ParseQuery
import com.parse.ParseUser
import okhttp3.OkHttpClient

import okhttp3.*

import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

//Similar to a recycler view a carousel adapter is created

class CarouselRVAdapter(private val carouselDataList: Map<String, Drawable>, private val context: Context, private val comp: Boolean) : //The carousel data list must be a map with a key, value pair of String Drawable

    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

    // Set Global variable isCompetitive
    var isCompetitive:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(R.layout.item_carousel, parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.textview)
        textView?.background = carouselDataList.values.elementAt(position) //The textView that is being bound contains a background xml element
        textView?.text = carouselDataList.keys.elementAt(position)


    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }

    inner class CarouselItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener { //Made the item View holder as an inner class to listen for clicks on each recyclerview item

        private val POETRY_URL = "http://52.87.203.225/api/getPoetryPrompt"

        private val LYRICS_URL = "http://52.87.203.225/api/getLyricsPrompt"

        private val MOVIES_URL = "http://52.87.203.225/api/getMoviePrompt"

        var prompt_Data = POETRY_URL

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            val category = carouselDataList.keys.elementAt(adapterPosition)
            Log.i(TAG, "$category")

            retrieveIsCompetitive()

            if(category == "Poetry"){
                prompt_Data = POETRY_URL
            }else if(category == "Movies"){
                prompt_Data = MOVIES_URL
            }else{
                prompt_Data = LYRICS_URL
            }

            dataRetrieve(prompt_Data, category)
            if (v != null) {
                v.setOnClickListener(null)
            };

        }
    }

    fun dataRetrieve(CATEGORY_URL: String, selection: String) {

        val client = OkHttpClient.Builder().connectTimeout(100,TimeUnit.SECONDS).writeTimeout(100,TimeUnit.SECONDS).readTimeout(300,TimeUnit.SECONDS).build()

        Log.e("dataRetrieve",CATEGORY_URL)
        val request = okhttp3.Request.Builder().url(CATEGORY_URL).build()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("dataRetrieve:","FAIL")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                if(body!=null){
                    val jObject = JSONObject(body)
                    val intent = Intent(context,PlayActivity::class.java)
                    intent.putExtra("prompt",jObject.get("prompt").toString())
                    Log.i("Adapter", comp.toString())
                    intent.putExtra("category", selection.lowercase())
                    intent.putExtra("isCompetitive", isCompetitive)
                    intent.putExtra("isDaily", false)
                    context.startActivity(intent)
                }
            }
        })

//        client.get(CATEGORY_URL, object: JsonHttpResponseHandler() {
//
//            override fun onSuccess(statusCode: Int, headers: okhttp3.Headers?, json: JSON?) {
//                Log.i(SelectionFragment.TAG, "onSuccess: JSON data $json")
//                try {
//                    val promptJsonArray = json?.jsonObject?.getJSONArray("results")
//                    val prompt = promptJsonArray?.getJSONObject(INDEX)
//                    val theString = prompt?.getString("overview")
//                    Log.i(SelectionFragment.TAG, "The prompt: $theString")
//
//                    //Add Data to Play Activity
//                    val intent = Intent(context, PlayActivity::class.java)
//                    intent.putExtra("prompt", theString);
//                    context.startActivity(intent)
//
//                } catch (e: JSONException) {
//                    Log.e(SelectionFragment.TAG, "Encountered exception $e")


    }

    fun retrieveIsCompetitive() {
        val user = ParseUser.getCurrentUser()!!
        val query = ParseQuery.getQuery(Player::class.java)
        query.whereEqualTo(Player.KEY_USER, user)
        query.findInBackground { playerObjects , e ->
            if (e == null) {
                for (playerObject in playerObjects) {
                    isCompetitive = playerObject.getIsCompetitive()!!
                }
            } else {
                Log.i("Adapter", "Error: Failure to query user stats in Players table")
            }
        }
    }

    companion object {
        private const val TAG = "ADAPTER"
    }




}
