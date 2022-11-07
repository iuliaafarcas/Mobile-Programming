package com.example.recommendation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import com.example.recommendation.data.Datasource


class CreateFragment : Fragment() {
    private val datasource: Datasource by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create, container, false);
        view.findViewById<Spinner>(R.id.createTypeId).adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.arrayTypes)
        )
        view.findViewById<Button>(R.id.AddButtonId).setOnClickListener {
            addRecommendation(view)

        }

        return view
    }

    private fun deleteSpacesAndAddUppercase(str: String): String {
        var formedStr = ""
        var nextUppercase = false
        for (i in str.indices) {
            if (str[i] == ' ') {
                nextUppercase = true
            } else {
                if (nextUppercase) {
                    formedStr += str[i].uppercase()
                    nextUppercase = false
                } else {
                    formedStr += str[i]
                }
            }
        }
        return formedStr


    }

    private fun addRecommendation(view: View) {
        val title = view.findViewById<EditText>(R.id.createTitleId).text.toString()
        val age = view.findViewById<EditText>(R.id.createAgeId).text.toString()
        val description = view.findViewById<EditText>(R.id.createDescriptionId).text.toString()
        val benefits = view.findViewById<EditText>(R.id.createBenefitsId).text.toString()
        val type = view.findViewById<Spinner>(R.id.createTypeId).selectedItem.toString()

        if (title.isEmpty() || description.isEmpty() || benefits.isEmpty() || doesStringStartWithZero(age)) {
            val toast: Toast =
                Toast.makeText(requireContext(), "Invalid fields!", Toast.LENGTH_SHORT)
            toast.show()
        } else {
            val recommendation = Recommendation(
                title,
                age.toInt(),
                benefits,
                description,
                TypeEnum.valueOf(deleteSpacesAndAddUppercase(type))
            )
            datasource.addItemToList(recommendation)
            val toast: Toast =
                Toast.makeText(requireContext(), "Added successfully!", Toast.LENGTH_SHORT)
            toast.show()

        }


    }


    private fun doesStringStartWithZero(str: String): Boolean {
        return str.isEmpty() || str[0] == '0'

    }


}