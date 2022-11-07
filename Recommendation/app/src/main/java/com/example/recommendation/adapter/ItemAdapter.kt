package com.example.recommendation.adapter

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.recommendation.R
import com.example.recommendation.Recommendation
import com.example.recommendation.TypeEnum
import com.example.recommendation.UpdateFragment

class ItemAdapter(
    private val context: Context,
    private var dataset: MutableLiveData<MutableList<Recommendation>>


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
        val item = dataset.value!![position]
        holder.textView.text= item.title
        holder.textViewAge.text= item.recommendedAge.toString()
        if (item.type==TypeEnum.Sport) {
            holder.imageViewTypeSport.visibility = View.VISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type==TypeEnum.Book) {
            holder.imageViewTypeBooks.visibility = View.VISIBLE
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type==TypeEnum.FoodRecipe) {
            holder.imageViewTypeFoodRecipe.visibility = View.VISIBLE
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.INVISIBLE

        }
        else if (item.type==TypeEnum.Other) {
            holder.imageViewTypeSport.visibility = View.INVISIBLE
            holder.imageViewTypeBooks.visibility = View.INVISIBLE
            holder.imageViewTypeFoodRecipe.visibility = View.INVISIBLE
            holder.imageViewTypeOther.visibility = View.VISIBLE


        }

        holder.updateButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putInt(UpdateFragment.recommendationIdString, item.id)
            val transaction = (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            val updateFragment = UpdateFragment()
            updateFragment.arguments = bundle
            transaction.replace(R.id.container, updateFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

        holder.deleteButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes"){_ , _ -> deleteItem(item.id) }
                .setNegativeButton("No"){dialog, _ -> dialog.dismiss()}
            val alert = builder.create()
            alert.show()

        }

    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount(): Int {
      return dataset.value!!.size
    }

    private fun deleteItem(id: Int){
        val list= mutableListOf<Recommendation>()
        for(item in dataset.value!!){
            if(item.id!=id){
                list.add(item)
            }
        }
        dataset.value=list

    }

}