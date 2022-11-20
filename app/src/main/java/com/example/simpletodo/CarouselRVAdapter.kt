import android.content.Intent
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.simpletodo.PlayActivity
import com.example.simpletodo.R

//Similar to a recycler view a carousel adapter is created

class CarouselRVAdapter(private val carouselDataList: Map<String, Drawable>, private val context: Context) : //The carousel data list must be a map with a key, value pair of String Drawable
    RecyclerView.Adapter<CarouselRVAdapter.CarouselItemViewHolder>() {

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
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val category = carouselDataList.keys.elementAt(adapterPosition)
            Log.i(TAG, "$category")
            val intent = Intent(context, PlayActivity::class.java)

            context.startActivity(intent)

        }
    }

    companion object {
        private const val TAG = "ADAPTER"
    }




}