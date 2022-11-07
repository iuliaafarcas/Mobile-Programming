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
import com.example.recommendation.data.Datasource


class UpdateFragment : Fragment() {
    private val datasource: Datasource by activityViewModels()
    private var recommendationId: Int=-1

    companion object {
        const val recommendationIdString: String = "id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update, container, false)
        view.findViewById<Spinner>(R.id.UpdateTypeId).adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.arrayTypes)
        )
        arguments?.let {
            recommendationId=it.getInt(recommendationIdString)
            val recommendation = datasource.findById(recommendationId)
            view.findViewById<EditText>(R.id.UpdateTitleId).setText(recommendation?.title)
            view.findViewById<EditText>(R.id.UpdateAgeId).setText(recommendation?.recommendedAge.toString())
            view.findViewById<EditText>(R.id.UpdateDescriptionId)
                .setText(recommendation?.description)
            view.findViewById<EditText>(R.id.UpdateBenefitsId).setText(recommendation?.benefits)
            view.findViewById<Spinner>(R.id.UpdateTypeId).setSelection(recommendation?.type?.ordinal ?: 0)
        }
        view.findViewById<Button>(R.id.UpdateFinalButtonId).setOnClickListener {
            updateRecommendation(view)
            val toast: Toast =
                Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT)
            toast.show()
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

    private fun updateRecommendation(view: View) {
        val title = view.findViewById<EditText>(R.id.UpdateTitleId).text.toString()
        val age = view.findViewById<EditText>(R.id.UpdateAgeId).text.toString()
        val description = view.findViewById<EditText>(R.id.UpdateDescriptionId).text.toString()
        val benefits = view.findViewById<EditText>(R.id.UpdateBenefitsId).text.toString()
        val type = view.findViewById<Spinner>(R.id.UpdateTypeId).selectedItem.toString()

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
                TypeEnum.valueOf(deleteSpacesAndAddUppercase(type)),
                recommendationId
            )
            datasource.updateItem(recommendation)
            val toast: Toast =
                Toast.makeText(requireContext(), "Updated successfully!", Toast.LENGTH_SHORT)
            toast.show()

        }



    }
    private fun doesStringStartWithZero(str: String): Boolean {
        return str.isEmpty() || str[0] == '0'

    }


}