package com.example.recommendation.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.recommendation.R
import com.example.recommendation.model.Recommendation
import com.example.recommendation.model.TypeEnum
import com.example.recommendation.UpdateFragment

class ItemAdapter(
    private val context: Context,
    private val removeFunction: (id: Int) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>()
{
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Affirmation object.
    class ItemViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val textView: TextView = view.findViewById(R.id.item_title)
        val textViewAge: TextView = view.findViewById(R.id.item_age)
        val imageViewTypeSport: ImageView = view.findViewById(R.id.item_iconSport)
        val imageViewTypeBooks: ImageView = view.findViewById(R.id.item_iconBooks)
        val imageViewTypeFoodRecipe: ImageView = view.findViewById(R.id.item_iconFoodRecipe)
        val imageViewTypeOther: ImageView = view.findViewById(R.id.item_iconOther)
        val updateButton: Button = view.findViewById(R.id.UpdateButtonId)
        val deleteButton: Button = view.findViewById(R.id.DeleteButtonId)
    }

    private var recommendationList= emptyList<Recommendation>()
    private var connected: Boolean = false
    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       val adapterLayout= LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = recommendationList[position]
        holder.textView.text= item.title
        holder.textViewAge.text= item.recommendedAge.toString()
        if (item.type== TypeEnum.Sport) {
            holder.imageViewTypeSport.visibility = View.VISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type== TypeEnum.Book) {
            holder.imageViewTypeBooks.visibility = View.VISIBLE
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type== TypeEnum.FoodRecipe) {
            holder.imageViewTypeFoodRecipe.visibility = View.VISIBLE
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type== TypeEnum.Other) {
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.VISIBLE


        }

        holder.updateButton.setOnClickListener{
            if (connected) {
                val bundle = Bundle()
                bundle.putInt(UpdateFragment.recommendationIdString, item.id!!)
                val transaction =
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                val updateFragment = UpdateFragment()
                updateFragment.arguments = bundle
                transaction.replace(R.id.container, updateFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            } else{
                Toast.makeText(context, "Operation unavailable while offline", Toast.LENGTH_SHORT).show()
            }
        }

        holder.deleteButton.setOnClickListener {
            if (connected) {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes") { _, _ -> removeFunction(item.id!!) }
                    .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
                val alert = builder.create()
                alert.show()
            } else {
                Toast.makeText(context, "Operation unavailable while offline", Toast.LENGTH_SHORT).show()
            }
        }

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = recommendationList.size

    fun setRecommendations(recommendations: List<Recommendation>){
        this.recommendationList=recommendations
        notifyDataSetChanged()
    }

    fun setConnected(connected:Boolean) {
        this.connected = connected
    }
}